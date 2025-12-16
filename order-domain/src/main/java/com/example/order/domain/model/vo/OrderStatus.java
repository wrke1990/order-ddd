package com.example.order.domain.model.vo;

/**
 * 订单状态枚举
 */
public enum OrderStatus {

    /**
     * 待支付
     */
    PENDING_PAYMENT,

    /**
     * 已支付
     */
    PAID,

    /**
     * 待发货
     */
    PENDING_SHIPMENT,

    /**
     * 已发货
     */
    SHIPPED,

    /**
     * 待收货
     */
    PENDING_RECEIPT,

    /**
     * 已完成
     */
    COMPLETED,

    /**
     * 已取消
     */
    CANCELLED,

    /**
     * 已退款
     */
    REFUNDED,

    /**
     * 部分退款
     */
    PARTIALLY_REFUNDED,

    /**
     * 售后中
     */
    AFTER_SALES_PROCESSING
}
