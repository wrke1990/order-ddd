package com.example.order.domain.service;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.vo.Id;

public interface OrderDomainService {

    /**
     * 创建订单
     * @param order 订单聚合根
     * @return 创建后的订单
     */
    Order createOrder(Order order);



    /**
     * 支付订单
     * @param orderNo 订单号
     * @return 支付后的订单
     */
    Order payOrder(String orderNo);

    /**
     * 取消订单
     * @param orderNo 订单号
     * @return 取消后的订单
     */
    Order cancelOrder(String orderNo);

    /**
     * 发货
     * @param orderNo 订单号
     * @return 发货后的订单
     */
    Order shipOrder(String orderNo);

    /**
     * 完成订单
     * @param orderNo 订单号
     * @return 完成后的订单
     */
    Order completeOrder(String orderNo);



    /**
     * 应用优惠券到订单
     * @param orderNo 订单号
     * @param couponId 优惠券ID
     * @return 更新后的订单
     */
    Order applyCoupon(String orderNo, Id couponId);

    /**
     * 修改订单支付方式
     * @param orderNo 订单号
     * @param paymentMethodId 支付方式ID
     * @return 更新后的订单
     */
    Order changePaymentMethod(String orderNo, Id paymentMethodId);

    /**
     * 修改订单地址
     * @param orderNo 订单号
     * @param addressId 地址ID
     * @return 更新后的订单
     */
    Order changeAddress(String orderNo, Id addressId);
}
