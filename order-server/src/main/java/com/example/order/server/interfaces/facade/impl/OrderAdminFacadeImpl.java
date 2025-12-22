package com.example.order.server.interfaces.facade.impl;

import org.springframework.stereotype.Service;

import com.example.order.admin.facade.OrderAdminFacade;
import com.example.order.admin.vo.req.OrderQueryReq;
import com.example.order.admin.vo.resp.OrderResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.common.response.PageResponse;
import com.example.order.domain.model.vo.Id;
import com.example.order.server.application.dto.OrderResponse;
import com.example.order.server.application.service.OrderCommandService;
import com.example.order.server.application.service.OrderQueryService;

/**
 * 订单管理后台接口实现
 */
@Service
public class OrderAdminFacadeImpl implements OrderAdminFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderAdminFacadeImpl(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    @Override
    public CommonResponse<PageResponse<OrderResp>> queryOrders(OrderQueryReq req) {
        // TODO: 实现订单分页查询
        throw new UnsupportedOperationException("尚未实现");
    }

    @Override
    public CommonResponse<OrderResp> getOrder(Long orderId) {
        // TODO: 实现查询订单详情
        throw new UnsupportedOperationException("尚未实现");
    }

    @Override
    public CommonResponse<Void> shipOrder(Long orderId) {
        // 管理员操作，先查询订单获取userId
        OrderResponse order = orderQueryService.getOrderById(orderId, null);
        orderCommandService.shipOrder(String.valueOf(orderId), Id.of(order.getUserId()));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> completeOrder(Long orderId) {
        // 管理员操作，先查询订单获取userId
        OrderResponse order = orderQueryService.getOrderById(orderId, null);
        orderCommandService.completeOrder(String.valueOf(orderId), Id.of(order.getUserId()));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> cancelOrder(Long orderId) {
        // 管理员操作，先查询订单获取userId
        OrderResponse order = orderQueryService.getOrderById(orderId, null);
        orderCommandService.cancelOrder(String.valueOf(orderId), Id.of(order.getUserId()));
        return CommonResponse.success();
    }
}
