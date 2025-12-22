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
    void payOrder(String orderNo, Id userId);

    /**
     * 取消订单
     */
    void cancelOrder(String orderNo, Id userId);

    /**
     * 发货
     */
    void shipOrder(String orderNo, Id userId);

    /**
     * 确认收货
     */
    void confirmReceipt(String orderNo, Id userId);

    /**
     * 修改订单地址（仅未发货时可修改）
     */
    void changeShippingAddress(String orderNo, Id addressId, Id userId);

    /**
     * 修改支付方式（仅未支付时可修改）
     */
    void changePaymentMethod(String orderNo, Id paymentMethodId, Id userId);

    /**
     * 应用优惠券
     */
    void applyCoupon(String orderNo, Id couponId, Id userId);

    /**
     * 完成订单
     */
    void completeOrder(String orderNo, Id userId);
}
