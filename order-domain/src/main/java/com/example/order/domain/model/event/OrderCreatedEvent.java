package com.example.order.domain.model.event;

import com.example.order.domain.model.entity.OrderItem;

import java.util.List;

/**
 * 订单创建事件
 */
public class OrderCreatedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNo;
    private final Long userId;
    private final List<OrderItem> orderItems;

    public OrderCreatedEvent(Long orderId, String orderNo, Long userId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.orderItems = orderItems;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getEventType() {
        return "order.created";
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", orderItems.size()=" + (orderItems != null ? orderItems.size() : 0) +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}
