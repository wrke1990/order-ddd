package com.example.order.domain.model.event;

import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.Price;

/**
 * 售后订单创建事件
 */
public class AfterSaleOrderCreatedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long afterSaleId;
    private final String afterSaleNo;
    private final String orderNo;
    private final Long userId;
    private final Long productId;
    private final Integer quantity;
    private final Price productPrice;
    private final AfterSaleType type;

    public AfterSaleOrderCreatedEvent(Long afterSaleId, String afterSaleNo, String orderNo, Long userId,
                                     Long productId, Integer quantity, Price productPrice, AfterSaleType type) {
        this.afterSaleId = afterSaleId;
        this.afterSaleNo = afterSaleNo;
        this.orderNo = orderNo;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.type = type;
    }

    public Long getAfterSaleId() {
        return afterSaleId;
    }

    public String getAfterSaleNo() {
        return afterSaleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public AfterSaleType getType() {
        return type;
    }

    @Override
    public String getEventType() {
        return "afterSaleOrder.created";
    }

    @Override
    public String toString() {
        return "AfterSaleOrderCreatedEvent{" +
                "afterSaleId=" + afterSaleId +
                ", afterSaleNo='" + afterSaleNo + "'" +
                ", orderNo='" + orderNo + "'" +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", productPrice=" + productPrice +
                ", type=" + type +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}