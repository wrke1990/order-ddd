package com.example.order.infrastructure.persistence.po;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后订单持久化对象
 */
@Entity
@Table(name = "t_after_sale_order")
public class AfterSaleOrderPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "after_sale_no", nullable = false, unique = true, length = 32)
    private String afterSaleNo;

    @Column(name = "order_no", nullable = false, length = 32)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "after_sale_type", nullable = false, length = 20)
    private String afterSaleType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    // 关联售后商品项
    @OneToMany(mappedBy = "afterSaleNo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AfterSaleItemPO> afterSaleItems;

    // 总退款金额和货币
    @Column(name = "total_refund_amount", nullable = false)
    private Long totalRefundAmount;

    @Column(name = "total_currency", nullable = false, length = 10)
    private String totalCurrency;

    @Column(name = "reason", nullable = false, length = 200)
    private String reason;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "customer_service_id")
    private Long customerServiceId;

    @Column(name = "reverse_logistics_no", length = 50)
    private String reverseLogisticsNo;

    @Column(name = "review_reason", length = 200)
    private String reviewReason;

    @Column(name = "refund_reason", length = 200)
    private String refundReason;

    // 关联退货地址
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "return_address_id", referencedColumnName = "id")
    private AddressPO returnAddress;

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
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(String afterSaleType) {
        this.afterSaleType = afterSaleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AfterSaleItemPO> getAfterSaleItems() {
        return afterSaleItems;
    }

    public void setAfterSaleItems(List<AfterSaleItemPO> afterSaleItems) {
        this.afterSaleItems = afterSaleItems;
    }

    public Long getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(Long totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public String getTotalCurrency() {
        return totalCurrency;
    }

    public void setTotalCurrency(String totalCurrency) {
        this.totalCurrency = totalCurrency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public AddressPO getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(AddressPO returnAddress) {
        this.returnAddress = returnAddress;
    }
}
