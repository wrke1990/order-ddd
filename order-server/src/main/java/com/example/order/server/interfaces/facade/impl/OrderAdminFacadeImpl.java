package com.example.order.server.interfaces.facade.impl;

import com.example.order.admin.facade.OrderAdminFacade;
import com.example.order.admin.vo.req.OrderQueryReq;
import com.example.order.admin.vo.resp.OrderResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.common.response.PageResponse;
import com.example.order.domain.model.vo.Id;
import com.example.order.server.application.service.OrderCommandService;
import org.springframework.stereotype.Service;

/**
 * 订单管理后台接口实现
 */
@Service
public class OrderAdminFacadeImpl implements OrderAdminFacade {

    private final OrderCommandService orderCommandService;

    public OrderAdminFacadeImpl(OrderCommandService orderCommandService) {
        this.orderCommandService = orderCommandService;
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
        orderCommandService.shipOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> completeOrder(Long orderId) {
        orderCommandService.completeOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> cancelOrder(Long orderId) {
        orderCommandService.cancelOrder(Id.of(orderId));
        return CommonResponse.success();
    }
}
