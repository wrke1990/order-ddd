package com.example.order.domain.model.event;

/**
 * 订单发货事件
 */
public class OrderShippedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNo;
    private final Long userId;
    private final String logisticsCompany;
    private final String trackingNumber;
    public OrderShippedEvent(Long orderId, String orderNo, Long userId, String logisticsCompany, String trackingNumber) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.logisticsCompany = logisticsCompany;
        this.trackingNumber = trackingNumber;
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

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getEventType() {
        return "order.shipped";
    }

    @Override
    public String toString() {
        return "OrderShippedEvent{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", logisticsCompany='" + logisticsCompany + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}