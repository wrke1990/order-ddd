package com.example.order.domain.model.event;

import com.example.order.domain.model.vo.Price;

/**
 * 售后订单退款成功事件
 */
public class AfterSaleOrderRefundedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long afterSaleId;
    private final String afterSaleNo;
    private final String orderNo;
    private final Price refundAmount;

    public AfterSaleOrderRefundedEvent(Long afterSaleId, String afterSaleNo, String orderNo, Price refundAmount) {
        this.afterSaleId = afterSaleId;
        this.afterSaleNo = afterSaleNo;
        this.orderNo = orderNo;
        this.refundAmount = refundAmount;
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

    public Price getRefundAmount() {
        return refundAmount;
    }

    @Override
    public String getEventType() {
        return "afterSaleOrder.refunded";
    }

    @Override
    public String toString() {
        return "AfterSaleOrderRefundedEvent{" +
                "afterSaleId=" + afterSaleId +
                ", afterSaleNo='" + afterSaleNo + "'" +
                ", orderNo='" + orderNo + "'" +
                ", refundAmount=" + refundAmount +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}