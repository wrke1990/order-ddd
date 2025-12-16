package com.example.order.domain.model.event;

import com.example.order.domain.model.vo.Price;

/**
 * 订单支付事件
 */
public class OrderPaidEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNo;
    private final Price totalAmount;

    public OrderPaidEvent(Long orderId, String orderNo, Price totalAmount) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Price getTotalAmount() {
        return totalAmount;
    }

    public String getEventType() {
        return "order.paid";
    }

    @Override
    public String toString() {
        return "OrderPaidEvent{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", totalAmount=" + totalAmount +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}
