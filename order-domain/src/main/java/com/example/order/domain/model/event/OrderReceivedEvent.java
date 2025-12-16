package com.example.order.domain.model.event;

/**
 * 订单确认收货事件
 */
public class OrderReceivedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNo;
    private final Long userId;
    public OrderReceivedEvent(Long orderId, String orderNo, Long userId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
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

    public String getEventType() {
        return "order.received";
    }

    @Override
    public String toString() {
        return "OrderReceivedEvent{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}