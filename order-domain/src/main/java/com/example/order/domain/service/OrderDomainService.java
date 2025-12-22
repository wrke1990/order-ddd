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
     * @param order 订单聚合根
     * @return 支付后的订单
     */
    Order payOrder(Order order);

    /**
     * 取消订单
     * @param order 订单聚合根
     * @return 取消后的订单
     */
    Order cancelOrder(Order order);

    /**
     * 发货
     * @param order 订单聚合根
     * @return 发货后的订单
     */
    Order shipOrder(Order order);

    /**
     * 完成订单
     * @param order 订单聚合根
     * @return 完成后的订单
     */
    Order completeOrder(Order order);

    /**
     * 确认收货
     * @param order 订单聚合根
     * @return 确认收货后的订单
     */
    Order confirmReceipt(Order order);

    /**
     * 应用优惠券到订单
     * @param order 订单聚合根
     * @param couponId 优惠券ID
     * @return 更新后的订单
     */
    Order applyCoupon(Order order, Id couponId);

    /**
     * 修改订单支付方式
     * @param order 订单聚合根
     * @param paymentMethodId 支付方式ID
     * @return 更新后的订单
     */
    Order changePaymentMethod(Order order, Id paymentMethodId);

    /**
     * 修改订单地址
     * @param order 订单聚合根
     * @param addressId 地址ID
     * @return 更新后的订单
     */
    Order changeAddress(Order order, Id addressId);
}
