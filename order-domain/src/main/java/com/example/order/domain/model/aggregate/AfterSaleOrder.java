package com.example.order.domain.model.aggregate;

import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.event.AfterSaleOrderApprovedEvent;
import com.example.order.domain.model.event.AfterSaleOrderCreatedEvent;
import com.example.order.domain.model.event.AfterSaleOrderRefundedEvent;
import com.example.order.domain.model.event.DomainEvent;
import com.example.order.domain.model.vo.AfterSaleStatus;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.Price;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 售后单聚合根
 */
public class AfterSaleOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String afterSaleNo;
    private Long orderId;
    private String orderNo;
    private Long userId;
    private AfterSaleType type;
    private AfterSaleStatus status;
    private Price totalRefundAmount;
    private String reason;
    private String description;
    private String images;
    private String logisticsCompany;
    private String trackingNumber;
    private Boolean adminInitiated;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer version = 0;
    private Long customerServiceId;
    private String reverseLogisticsNo;
    private String reviewReason;
    private String refundReason;
    private final List<AfterSaleItem> afterSaleItems; // 售后商品项列表
    private final List<DomainEvent> domainEvents = new ArrayList<>(); // 领域事件列表

    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private AfterSaleOrder(String afterSaleNo, Long orderId, String orderNo, Long userId,
                          AfterSaleType type, String reason, String description, String images,
                          List<AfterSaleItem> afterSaleItems) {
        this.afterSaleNo = afterSaleNo;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.type = type;
        this.status = AfterSaleStatus.PENDING_PROCESS;
        this.reason = reason;
        this.description = description;
        this.images = images;
        this.adminInitiated = false;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.version = 0;

        // 复制售后项列表，并设置售后单号
        this.afterSaleItems = new ArrayList<>(afterSaleItems);
        this.afterSaleItems.forEach(item -> item.setAfterSaleNo(afterSaleNo));

        // 计算总退款金额
        this.calculateTotalRefundAmount();
    }

    // ======================== 工厂方法 ========================

    /**
     * 创建售后单
     * @param afterSaleNo 售后单号
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param type 售后类型
     * @param reason 售后原因
     * @param description 售后详情
     * @param images 售后图片
     * @param afterSaleItems 售后商品项列表
     * @param adminInitiated 是否由管理员发起（超级退款）
     */
    public static AfterSaleOrder create(String afterSaleNo, Long orderId, String orderNo,
                                       Long userId, AfterSaleType type,
                                       String reason, String description, String images,
                                       List<AfterSaleItem> afterSaleItems,
                                       boolean adminInitiated) {
        validateBasicInfo(afterSaleNo, orderId, orderNo, userId, type, reason);
        validateAfterSaleItems(afterSaleItems);

        AfterSaleOrder afterSaleOrder = new AfterSaleOrder(afterSaleNo, orderId, orderNo, userId, type, reason, description, images, afterSaleItems);
        afterSaleOrder.adminInitiated = adminInitiated;
        afterSaleOrder.createAfterSaleOrderCreatedEvent();
        return afterSaleOrder;
    }

    /**
     * 创建超级退款（客服发起）
     */
    public static AfterSaleOrder createSuperRefund(String afterSaleNo, Long orderId, String orderNo,
                                                 Long userId, List<AfterSaleItem> afterSaleItems) {
        return create(afterSaleNo, orderId, orderNo, userId,
                     AfterSaleType.REFUND_ONLY, "超级退款", null, null,
                     afterSaleItems, true);
    }

    // ======================== 状态转换方法 ========================

    /**
     * 审核通过
     */
    public void approve(String reason) {
        ensureStatus(AfterSaleStatus.PENDING_PROCESS);

        this.status = (this.type == AfterSaleType.REFUND_ONLY) ?
                     AfterSaleStatus.PENDING_REFUND : AfterSaleStatus.PENDING_RETURN;
        this.updateTime = LocalDateTime.now();
        this.reviewReason = reason;
        this.createAfterSaleOrderApprovedEvent(reason);
    }

    /**
     * 审核拒绝
     */
    public void reject(String reason) {
        ensureStatus(AfterSaleStatus.PENDING_PROCESS);

        this.status = AfterSaleStatus.REJECTED;
        this.updateTime = LocalDateTime.now();
        this.reviewReason = reason;
    }

    /**
     * 提交退货物流信息
     */
    public void submitReturnLogistics(String logisticsCompany, String trackingNumber) {
        if (this.type != AfterSaleType.REFUND_WITH_RETURN) {
            throw new IllegalArgumentException("只有退货退款类型才能提交物流信息");
        }
        validateLogisticsInfo(logisticsCompany, trackingNumber);

        this.logisticsCompany = logisticsCompany;
        this.trackingNumber = trackingNumber;
        this.status = AfterSaleStatus.PENDING_RETURN_CONFIRM;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 确认收到退货
     */
    public void confirmReturn() {
        ensureStatus(AfterSaleStatus.PENDING_RETURN_CONFIRM);

        this.status = AfterSaleStatus.PENDING_REFUND;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 取消售后订单
     */
    public void cancel() {
        ensureStatus(AfterSaleStatus.PENDING_PROCESS);

        this.status = AfterSaleStatus.CANCELLED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 确认退款
     */
    public void confirmRefund(Price refundAmount) {
        ensureStatus(AfterSaleStatus.PENDING_REFUND);

        this.status = AfterSaleStatus.REFUNDED;
        this.updateTime = LocalDateTime.now();
        this.createAfterSaleOrderRefundedEvent(refundAmount);
    }

    /**
     * 完成售后
     */
    public void complete() {
        ensureStatus(AfterSaleStatus.REFUNDED);

        this.status = AfterSaleStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }

    // ======================== 售后商品项管理方法 ========================

    /**
     * 添加售后商品项
     */
    public void addAfterSaleItem(AfterSaleItem item) {
        if (item == null) {
            throw new IllegalArgumentException("售后商品项不能为空");
        }
        item.setAfterSaleNo(this.afterSaleNo);
        this.afterSaleItems.add(item);
        this.updateTotalRefundAmount();
    }

    /**
     * 移除售后商品项
     */
    public void removeAfterSaleItem(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        this.afterSaleItems.removeIf(item -> item.getProductId().equals(productId));
        this.updateTotalRefundAmount();
    }

    /**
     * 更新总退款金额
     */
    public void updateTotalRefundAmount() {
        this.calculateTotalRefundAmount();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 标记所有商品项为已退货
     */
    public void markAllItemsAsReturned() {
        this.afterSaleItems.forEach(AfterSaleItem::markAsReturned);
        this.updateTime = LocalDateTime.now();
    }

    // ======================== 领域事件方法 ========================

    /**
     * 创建售后订单创建事件
     */
    private void createAfterSaleOrderCreatedEvent() {
        if (!this.afterSaleItems.isEmpty()) {
            AfterSaleItem firstItem = this.afterSaleItems.get(0);
            AfterSaleOrderCreatedEvent event = new AfterSaleOrderCreatedEvent(
                this.id, this.afterSaleNo, this.orderNo, this.userId,
                firstItem.getProductId(), firstItem.getQuantity(),
                firstItem.getProductPrice(), this.type
            );
            this.domainEvents.add(event);
        }
    }

    /**
     * 创建售后订单审核通过事件
     */
    private void createAfterSaleOrderApprovedEvent(String reason) {
        AfterSaleOrderApprovedEvent event = new AfterSaleOrderApprovedEvent(
            this.id, this.afterSaleNo, this.orderNo, reason
        );
        this.domainEvents.add(event);
    }

    /**
     * 创建售后订单退款事件
     */
    private void createAfterSaleOrderRefundedEvent(Price refundAmount) {
        AfterSaleOrderRefundedEvent event = new AfterSaleOrderRefundedEvent(
            this.id, this.afterSaleNo, this.orderNo, refundAmount
        );
        this.domainEvents.add(event);
    }

    /**
     * 获取并清空已收集的领域事件
     */
    public List<DomainEvent> getDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    /**
     * 检查是否有未发布的领域事件
     */
    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    // ======================== 验证方法 ========================

    /**
     * 验证基本信息
     */
    private static void validateBasicInfo(String afterSaleNo, Long orderId, String orderNo,
                                         Long userId, AfterSaleType type, String reason) {
        if (afterSaleNo == null || afterSaleNo.isEmpty()) {
            throw new IllegalArgumentException("售后单号不能为空");
        }
        if (orderId == null || orderNo == null || orderNo.isEmpty()) {
            throw new IllegalArgumentException("订单信息不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (type == null) {
            throw new IllegalArgumentException("售后类型不能为空");
        }
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("售后原因不能为空");
        }
    }

    /**
     * 验证售后商品项
     */
    private static void validateAfterSaleItems(List<AfterSaleItem> afterSaleItems) {
        if (afterSaleItems == null || afterSaleItems.isEmpty()) {
            throw new IllegalArgumentException("售后商品项不能为空");
        }
    }

    /**
     * 验证物流信息
     */
    private static void validateLogisticsInfo(String logisticsCompany, String trackingNumber) {
        if (logisticsCompany == null || logisticsCompany.isEmpty() ||
            trackingNumber == null || trackingNumber.isEmpty()) {
            throw new IllegalArgumentException("物流信息不能为空");
        }
    }

    /**
     * 确保当前状态符合预期
     */
    private void ensureStatus(AfterSaleStatus expectedStatus) {
        if (this.status != expectedStatus) {
            throw new IllegalArgumentException("当前状态不符合操作要求");
        }
    }

    // ======================== 私有辅助方法 ========================

    /**
     * 计算总退款金额
     */
    private void calculateTotalRefundAmount() {
        if (this.afterSaleItems.isEmpty()) {
            this.totalRefundAmount = Price.zero();
            return;
        }

        Price total = this.afterSaleItems.stream()
            .map(AfterSaleItem::getRefundAmount)
            .reduce(Price.zero(), Price::add);

        this.totalRefundAmount = total;
    }

    // ======================== Getters and Setters ========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAfterSaleNo() {
        return afterSaleNo;
    }

    public void setAfterSaleNo(String afterSaleNo) {
        this.afterSaleNo = afterSaleNo;
        // 同步售后单号到所有商品项
        this.afterSaleItems.forEach(item -> item.setAfterSaleNo(afterSaleNo));
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public AfterSaleType getType() {
        return type;
    }

    public AfterSaleStatus getStatus() {
        return status;
    }

    public void setStatus(AfterSaleStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("售后状态不能为空");
        }
        this.status = status;
        this.updateTime = LocalDateTime.now();
    }

    public Price getTotalRefundAmount() {
        return totalRefundAmount;
    }



    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getImages() {
        return images;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Boolean getAdminInitiated() {
        return adminInitiated;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public List<AfterSaleItem> getAfterSaleItems() {
        return Collections.unmodifiableList(this.afterSaleItems);
    }

    public Long getCustomerServiceId() {
        return customerServiceId;
    }

    public void setCustomerServiceId(Long customerServiceId) {
        this.customerServiceId = customerServiceId;
    }

    public String getReverseLogisticsNo() {
        return reverseLogisticsNo;
    }

    public void setReverseLogisticsNo(String reverseLogisticsNo) {
        this.reverseLogisticsNo = reverseLogisticsNo;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }



    // ======================== toString方法 ========================

    @Override
    public String toString() {
        return "AfterSaleOrder{" +
                "id=" + id +
                ", afterSaleNo='" + afterSaleNo + "'" +
                ", orderNo='" + orderNo + "'" +
                ", userId=" + userId +
                ", type=" + type +
                ", status=" + status +
                ", totalRefundAmount=" + totalRefundAmount +
                ", afterSaleItemsSize=" + this.afterSaleItems.size() +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", domainEventsSize=" + domainEvents.size() +
                '}';
    }
}