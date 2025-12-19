package com.example.order.domain.model.aggregate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.event.DomainEvent;
import com.example.order.domain.model.event.OrderAddressUpdatedEvent;
import com.example.order.domain.model.event.OrderCancelledEvent;
import com.example.order.domain.model.event.OrderCreatedEvent;
import com.example.order.domain.model.event.OrderPaidEvent;
import com.example.order.domain.model.event.OrderReceivedEvent;
import com.example.order.domain.model.event.OrderShippedEvent;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Coupon;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.LogisticsInfo;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.PaymentMethod;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.service.validator.OrderBusinessRuleValidator;

/**
 * 订单聚合根
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int ORDER_VALIDITY_MINUTES = 15;

    private Id id;
    private final Id userId;
    private String orderNo;
    private OrderStatus status;
    private Price totalAmount;
    private Price actualAmount;
    private final List<OrderItem> orderItems;
    private final LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime expireTime;
    private Address shippingAddress;
    private PaymentMethod paymentMethod;
    private String paymentNo;
    private LogisticsInfo logisticsInfo;
    private Coupon usedCoupon;
    private Integer version;

    // 领域事件集合
    private transient final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 创建订单
     */
    public static Order create(Id userId, List<OrderItem> orderItems,
                              Address shippingAddress, PaymentMethod paymentMethod) {
        return create(userId, orderItems, shippingAddress, paymentMethod, null);
    }

    /**
     * 创建订单（支持优惠券）
     */
    public static Order create(Id userId, List<OrderItem> orderItems,
                              Address shippingAddress, PaymentMethod paymentMethod, Coupon coupon) {
        // 使用业务规则验证器进行验证
        OrderBusinessRuleValidator.validateOrderCreation(userId, orderItems);

        if (shippingAddress == null) {
            throw new IllegalArgumentException("收货地址不能为空");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("支付方式不能为空");
        }

        // 复制订单项列表，避免外部修改
        List<OrderItem> copiedItems = new ArrayList<>(orderItems.size());
        for (OrderItem item : orderItems) {
            OrderItem copiedItem = OrderItem.create(
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice()
            );
            if (item.getId() != null) {
                copiedItem.setId(item.getId());
            }
            copiedItems.add(copiedItem);
        }

        Order order = new Order(userId, copiedItems, null, shippingAddress, paymentMethod);
        order.status = OrderStatus.PENDING_PAYMENT;
        order.calculateTotalAmount();
        order.actualAmount = order.totalAmount;

        // 如果有优惠券，应用优惠券
        if (coupon != null) {
            order.applyCoupon(coupon);
        }

        order.createOrderEvent();

        return order;
    }

    /**
     * 从持久化数据重建订单（仅用于基础设施层）
     * 此方法用于从数据库加载订单数据时重建订单对象，跳过业务规则验证
     */
    public static Order reconstruct(Id id, Id userId, String orderNo, OrderStatus status,
                                   Price totalAmount, LocalDateTime createTime, LocalDateTime updateTime,
                                   LocalDateTime expireTime, Integer version, List<OrderItem> orderItems) {
        // 复制订单项列表，避免外部修改
        List<OrderItem> copiedItems = new ArrayList<>(orderItems.size());
        for (OrderItem item : orderItems) {
            OrderItem copiedItem = OrderItem.create(
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice()
            );
            if (item.getId() != null) {
                copiedItem.setId(item.getId());
            }
            if (item.getOrderNo() != null) {
                copiedItem.setOrderNo(item.getOrderNo());
            }
            copiedItems.add(copiedItem);
        }

        // 创建订单对象，shippingAddress和paymentMethod设为null，因为从PO中无法获取
        Order order = new Order(userId, copiedItems, orderNo, null, null);

        // 设置从数据库加载的属性
        order.id = id;
        order.status = status;
        order.totalAmount = totalAmount;
        order.updateTime = updateTime;
        order.expireTime = expireTime;
        order.version = version;

        return order;
    }

    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private Order(Id userId, List<OrderItem> orderItems, String orderNo,
                 Address shippingAddress, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.orderItems = orderItems; // 已经是副本，直接使用
        this.orderNo = orderNo;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;

        // 不为订单项设置订单号，订单号将在setOrderNo方法中设置

        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.expireTime = createTime.plusMinutes(ORDER_VALIDITY_MINUTES);
        this.version = 0;
    }

    /**
     * 计算订单总价
     */
    private void calculateTotalAmount() {
        long total = 0;
        String currency = "CNY";

        for (OrderItem item : orderItems) {
            item.calculateTotalPrice();
            total += item.getTotalPrice().getAmount();
            currency = item.getTotalPrice().getCurrency();
        }

        this.totalAmount = new Price(total, currency);
    }

    /**
     * 支付订单
     */
    public void pay(String paymentNo) {
        // 使用业务规则验证器进行验证
        OrderBusinessRuleValidator.validateOrderPayment(this);

        if (paymentNo == null || paymentNo.isEmpty()) {
            throw new IllegalArgumentException("支付单号不能为空");
        }

        this.status = OrderStatus.PAID;
        this.paymentNo = paymentNo;
        this.updateTime = LocalDateTime.now();

        // 生成订单支付事件
        this.createOrderPaidEvent();
    }

    /**
     * 修改支付方式
     */
    public void changePaymentMethod(PaymentMethod newPaymentMethod) {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("只有待支付状态的订单可以修改支付方式");
        }
        if (newPaymentMethod == null) {
            throw new IllegalArgumentException("新支付方式不能为空");
        }

        this.paymentMethod = newPaymentMethod;
        this.updateTime = LocalDateTime.now();
        // 清空旧的支付单号，因为需要重新生成
        this.paymentNo = null;
    }

    /**
     * 修改地址（仅未发货时可修改）
     */
    public void changeShippingAddress(Address newAddress) {
        if (this.status != OrderStatus.PAID && this.status != OrderStatus.PENDING_SHIPMENT) {
            throw new IllegalArgumentException("只有已支付或待发货状态的订单可以修改地址");
        }
        if (newAddress == null) {
            throw new IllegalArgumentException("新地址不能为空");
        }

        Address oldAddress = this.shippingAddress;
        this.shippingAddress = newAddress;
        this.updateTime = LocalDateTime.now();
        this.createOrderAddressUpdatedEvent(oldAddress, newAddress);
    }

    /**
     * 确认收货
     */
    public void confirmReceipt() {
        if (this.status != OrderStatus.SHIPPED && this.status != OrderStatus.PENDING_RECEIPT) {
            throw new IllegalArgumentException("只有已发货或待收货状态的订单可以确认收货");
        }

        this.status = OrderStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
        this.createOrderReceivedEvent();
    }

    /**
     * 取消订单
     */
    public void cancel() {
        cancel("用户主动取消");
    }

    /**
     * 取消订单（带原因）
     */
    public void cancel(String reason) {
        // 使用业务规则验证器进行验证
        OrderBusinessRuleValidator.validateOrderCancellation(this);

        this.status = OrderStatus.CANCELLED;
        this.updateTime = LocalDateTime.now();
        this.createOrderCancelledEvent(reason);
    }

    /**
     * 标记订单已发货
     */
    public void ship(String logisticsCompany, String trackingNumber) {
        if (this.status != OrderStatus.PAID) {
            throw new IllegalArgumentException("只有已支付的订单可以发货");
        }
        if (logisticsCompany == null || logisticsCompany.isEmpty() ||
            trackingNumber == null || trackingNumber.isEmpty()) {
            throw new IllegalArgumentException("物流信息不能为空");
        }

        this.status = OrderStatus.SHIPPED;
        this.logisticsInfo = new LogisticsInfo(logisticsCompany, trackingNumber);
        this.updateTime = LocalDateTime.now();
        this.createOrderShippedEvent(logisticsCompany, trackingNumber);
    }

    /**
     * 更新物流轨迹
     */
    public void updateLogistics(LocalDateTime time, String location, String description) {
        if (this.status != OrderStatus.SHIPPED && this.status != OrderStatus.PENDING_RECEIPT) {
            throw new IllegalArgumentException("只有已发货或待收货状态的订单可以更新物流信息");
        }
        if (this.logisticsInfo == null) {
            throw new IllegalStateException("订单尚未发货，无法更新物流轨迹");
        }

        this.logisticsInfo.addTrack(time, location, description);
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 应用优惠券
     */
    public void applyCoupon(Coupon coupon) {
        // 使用业务规则验证器进行验证
        OrderBusinessRuleValidator.validateCouponUsage(this, coupon);

        if (coupon == null) {
            this.usedCoupon = null;
            this.actualAmount = this.totalAmount;
            this.updateTime = LocalDateTime.now();
            return;
        }

        this.usedCoupon = coupon;
        // 计算实付金额，确保不小于0
        long actualAmountValue = Math.max(0, this.totalAmount.getAmount() - coupon.getDiscountAmount().getAmount());
        this.actualAmount = new Price(actualAmountValue, this.totalAmount.getCurrency());
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 检查订单是否已过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime) && this.status == OrderStatus.PENDING_PAYMENT;
    }

    /**
     * 超时自动取消
     */
    public void autoCancel() {
        if (isExpired()) {
            this.status = OrderStatus.CANCELLED;
            this.updateTime = LocalDateTime.now();
            this.createOrderCancelledEvent("订单超时自动取消");
        }
    }

    /**
     * 完成订单
     */
    public void complete() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException("只有已发货的订单可以完成");
        }

        this.status = OrderStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 创建订单创建事件
     */
    private void createOrderEvent() {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, this.orderNo, this.userId.getValue(), this.orderItems);
        this.domainEvents.add(event);
    }

    /**
     * 创建订单支付事件
     */
    private void createOrderPaidEvent() {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderPaidEvent event = new OrderPaidEvent(orderId, this.orderNo, this.totalAmount);
        this.domainEvents.add(event);
    }

    /**
     * 创建订单取消事件
     */
    private void createOrderCancelledEvent(String reason) {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderCancelledEvent event = new OrderCancelledEvent(orderId, this.orderNo, this.userId.getValue(), reason);
        this.domainEvents.add(event);
    }

    /**
     * 创建订单发货事件
     */
    private void createOrderShippedEvent(String logisticsCompany, String trackingNumber) {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderShippedEvent event = new OrderShippedEvent(orderId, this.orderNo, this.userId.getValue(), logisticsCompany, trackingNumber);
        this.domainEvents.add(event);
    }

    /**
     * 创建订单确认收货事件
     */
    private void createOrderReceivedEvent() {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderReceivedEvent event = new OrderReceivedEvent(orderId, this.orderNo, this.userId.getValue());
        this.domainEvents.add(event);
    }

    /**
     * 创建订单地址修改事件
     */
    private void createOrderAddressUpdatedEvent(Address oldAddress, Address newAddress) {
        Long orderId = this.id != null ? this.id.getValue() : null;
        OrderAddressUpdatedEvent event = new OrderAddressUpdatedEvent(orderId, this.orderNo, oldAddress, newAddress);
        this.domainEvents.add(event);
    }

    // 仅提供必要的getter方法，避免过度暴露内部状态
    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        if (id == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
        this.id = id;
    }

    public Id getUserId() {
         return userId;
     }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        this.orderNo = orderNo;
        // 同步订单号到所有订单项
        for (OrderItem item : this.orderItems) {
            item.setOrderNo(orderNo);
        }
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * 设置订单状态
     * 仅用于内部状态管理和领域服务协调
     */
    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("订单状态不能为空");
        }
        this.status = status;
        this.updateTime = LocalDateTime.now();
    }

    public Price getTotalAmount() {
        return totalAmount;
    }

    public Price getActualAmount() {
        return actualAmount;
    }

    public List<OrderItem> getOrderItems() {
        // 返回不可变副本，防止外部修改
        return Collections.unmodifiableList(orderItems);
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public LogisticsInfo getLogisticsInfo() {
        return logisticsInfo;
    }

    public Coupon getUsedCoupon() {
        return usedCoupon;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", actualAmount=" + actualAmount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", expireTime=" + expireTime +
                ", paymentMethod=" + paymentMethod +
                ", shippingAddress=" + shippingAddress +
                ", usedCoupon=" + usedCoupon +
                ", version=" + version +
                '}';
    }
}
