package com.example.order.admin.facade;

import com.example.order.admin.vo.req.OrderQueryReq;
import com.example.order.admin.vo.resp.OrderResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.common.response.PageResponse;

/**
 * 订单管理后台接口
 */
public interface OrderAdminFacade {

    /**
     * 查询订单列表（分页）
     */
    CommonResponse<PageResponse<OrderResp>> queryOrders(OrderQueryReq req);

    /**
     * 查询订单详情
     */
    CommonResponse<OrderResp> getOrder(Long orderId);

    /**
     * 发货
     */
    CommonResponse<Void> shipOrder(Long orderId);

    /**
     * 完成订单
     */
    CommonResponse<Void> completeOrder(Long orderId);

    /**
     * 取消订单
     */
    CommonResponse<Void> cancelOrder(Long orderId);
}
