package com.example.order.api.vo.req;

/**
 * 订单状态枚举（API层使用）
 */
public enum OrderStatusEnum {
    /**
     * 待支付
     */
    PENDING_PAYMENT,
    
    /**
     * 已支付
     */
    PAID,
    
    /**
     * 已发货
     */
    SHIPPED,
    
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
    REFUNDED
}
