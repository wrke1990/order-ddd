package com.example.order.domain.model.event;

import com.example.order.domain.model.vo.Address;

/**
 * 订单地址修改事件
 */
public class OrderAddressUpdatedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNo;
    private final Address oldAddress;
    private final Address newAddress;
    public OrderAddressUpdatedEvent(Long orderId, String orderNo, Address oldAddress, Address newAddress) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.oldAddress = oldAddress;
        this.newAddress = newAddress;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Address getOldAddress() {
        return oldAddress;
    }

    public Address getNewAddress() {
        return newAddress;
    }

    public String getEventType() {
        return "order.address.updated";
    }

    @Override
    public String toString() {
        return "OrderAddressUpdatedEvent{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + "'" +
                ", oldAddress=" + oldAddress +
                ", newAddress=" + newAddress +
                ", occurredTime=" + getOccurredTime() +
                "}";
    }
}