package com.example.order.api.facade;

import com.example.order.api.vo.req.CreateOrderReq;
import com.example.order.api.vo.req.OrderStatusEnum;
import com.example.order.api.vo.resp.OrderResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.common.response.PageResponse;

/**
 * 订单API接口
 */
public interface OrderApiFacade {

    /**
     * 创建订单
     */
    CommonResponse<OrderResp> createOrder(CreateOrderReq req);

    /**
     * 查询订单详情
     */
    CommonResponse<OrderResp> getOrder(String orderNo, Long userId);

    /**
     * 查询用户订单列表
     */
    CommonResponse<PageResponse<OrderResp>> getOrdersByUserId(Long userId, Integer page, Integer size);

    /**
     * 按状态查询订单
     */
    CommonResponse<PageResponse<OrderResp>> getOrdersByUserIdAndStatus(Long userId, OrderStatusEnum status, Integer page, Integer size);

    /**
     * 支付订单
     */
    CommonResponse<Void> payOrder(String orderNo, Long userId);

    /**
     * 取消订单
     */
    CommonResponse<Void> cancelOrder(String orderNo, Long userId);

    /**
     * 发货
     */
    CommonResponse<Void> shipOrder(String orderNo, Long userId);

    /**
     * 确认收货
     */
    CommonResponse<Void> confirmReceipt(String orderNo, Long userId);

    /**
     * 完成订单
     */
    CommonResponse<Void> completeOrder(String orderNo, Long userId);

    /**
     * 修改配送地址
     */
    CommonResponse<Void> changeShippingAddress(String orderNo, Long addressId, Long userId);

    /**
     * 修改支付方式
     */
    CommonResponse<Void> changePaymentMethod(String orderNo, Long paymentMethodId, Long userId);

}
