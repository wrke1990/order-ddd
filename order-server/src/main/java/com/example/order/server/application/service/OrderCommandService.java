package com.example.order.server.application.service;

import com.example.order.domain.model.vo.Id;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderResponse;

/**
 * 订单命令服务接口
 * 负责处理所有订单相关的命令操作（写操作）
 */
public interface OrderCommandService {

    /**
     * 创建订单
     */
    OrderResponse createOrder(CreateOrderCommand command);

    /**
     * 支付订单
     */
    void payOrder(Id orderId);

    /**
     * 取消订单
     */
    void cancelOrder(Id orderId);

    /**
     * 发货
     */
    void shipOrder(Id orderId);

    /**
     * 确认收货
     */
    void confirmReceipt(Id orderId);

    /**
     * 修改订单地址（仅未发货时可修改）
     */
    void changeShippingAddress(Id orderId, Id addressId);

    /**
     * 修改支付方式（仅未支付时可修改）
     */
    void changePaymentMethod(Id orderId, Id paymentMethodId);

    /**
     * 应用优惠券
     */
    void applyCoupon(Id orderId, Id couponId);

    /**
     * 完成订单
     */
    void completeOrder(Id orderId);
}
