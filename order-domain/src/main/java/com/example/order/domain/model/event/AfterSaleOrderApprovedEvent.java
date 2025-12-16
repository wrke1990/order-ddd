package com.example.order.domain.model.event;

/**
 * 售后订单审核通过事件
 */
public class AfterSaleOrderApprovedEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final Long afterSaleId;
    private final String afterSaleNo;
    private final String orderNo;
    private final String reason;

    public AfterSaleOrderApprovedEvent(Long afterSaleId, String afterSaleNo, String orderNo, String reason) {
        this.afterSaleId = afterSaleId;
        this.afterSaleNo = afterSaleNo;
        this.orderNo = orderNo;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    @Override
    public String getEventType() {
        return "afterSaleOrder.approved";
    }

    @Override
    public String toString() {
        return "AfterSaleOrderApprovedEvent{" +
                "afterSaleId=" + afterSaleId +
                ", afterSaleNo='" + afterSaleNo + "'" +
                ", orderNo='" + orderNo + "'" +
                ", reason='" + reason + "'" +
                ", occurredTime=" + getOccurredTime() +
                '}';
    }
}