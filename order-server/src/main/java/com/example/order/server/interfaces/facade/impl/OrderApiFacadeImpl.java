package com.example.order.server.interfaces.facade.impl;

import com.example.order.api.facade.OrderApiFacade;
import com.example.order.api.vo.req.CreateOrderReq;
import com.example.order.api.vo.req.OrderStatusEnum;
import com.example.order.api.vo.resp.OrderResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.common.response.PageResponse;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.server.application.assember.OrderVoAssembler;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderResponse;
import com.example.order.server.application.service.OrderCommandService;
import com.example.order.server.application.service.OrderQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单API接口实现
 */
@Service
public class OrderApiFacadeImpl implements OrderApiFacade {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;
    private final OrderVoAssembler orderVoAssembler;

    public OrderApiFacadeImpl(OrderCommandService orderCommandService, OrderQueryService orderQueryService, OrderVoAssembler orderVoAssembler) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
        this.orderVoAssembler = orderVoAssembler;
    }

    @Override
    public CommonResponse<OrderResp> createOrder(CreateOrderReq req) {
        CreateOrderCommand command = orderVoAssembler.toCreateOrderCommand(req);
        OrderResponse response = orderCommandService.createOrder(command);
        OrderResp orderResp = orderVoAssembler.toOrderResponse(response);
        return CommonResponse.success(orderResp);
    }

    @Override
    public CommonResponse<OrderResp> getOrder(String orderNo, Long userId) {
        OrderResponse response = orderQueryService.getOrderByOrderNo(orderNo, userId);
        OrderResp orderResp = orderVoAssembler.toOrderResponse(response);
        return CommonResponse.success(orderResp);
    }

    @Override
    public CommonResponse<PageResponse<OrderResp>> getOrdersByUserId(Long userId, Integer page, Integer size) {
        // 使用orderQueryService.getOrdersByUserId获取分页数据
        com.example.order.domain.model.vo.Page<OrderResponse> pageData = orderQueryService.getOrdersByUserId(userId, page, size);

        // 转换为OrderResp列表
        List<OrderResp> orderResps = pageData.getContent().stream()
                .map(orderVoAssembler::toOrderResponse)
                .collect(Collectors.toList());

        // 构建PageResponse
        return CommonResponse.success(PageResponse.success(page, size, pageData.getTotalElements(), orderResps));
    }

    @Override
    public CommonResponse<PageResponse<OrderResp>> getOrdersByUserIdAndStatus(Long userId, OrderStatusEnum status, Integer page, Integer size) {
        // 将API层的OrderStatusEnum转换为领域层的OrderStatus
        OrderStatus domainStatus = status != null ? OrderStatus.valueOf(status.name()) : null;

        // 使用orderQueryService.getOrdersByUserIdAndStatus获取分页数据
        com.example.order.domain.model.vo.Page<OrderResponse> pageData = orderQueryService.getOrdersByUserIdAndStatus(userId, domainStatus, page, size);

        // 转换为OrderResp列表
        List<OrderResp> orderResps = pageData.getContent().stream()
                .map(orderVoAssembler::toOrderResponse)
                .collect(Collectors.toList());

        // 构建PageResponse
        return CommonResponse.success(PageResponse.success(page, size, pageData.getTotalElements(), orderResps));
    }

    @Override
    public CommonResponse<Void> payOrder(String orderNo, Long userId) {
        orderCommandService.payOrder(orderNo, Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> cancelOrder(String orderNo, Long userId) {
        orderCommandService.cancelOrder(orderNo, Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> shipOrder(String orderNo, Long userId) {
        orderCommandService.shipOrder(orderNo, Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> confirmReceipt(String orderNo, Long userId) {
        orderCommandService.confirmReceipt(orderNo, Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> completeOrder(String orderNo, Long userId) {
        orderCommandService.completeOrder(orderNo, Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> changeShippingAddress(String orderNo, Long addressId, Long userId) {
        orderCommandService.changeShippingAddress(orderNo, Id.of(addressId), Id.of(userId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> changePaymentMethod(String orderNo, Long paymentMethodId, Long userId) {
        orderCommandService.changePaymentMethod(orderNo, Id.of(paymentMethodId), Id.of(userId));
        return CommonResponse.success();
    }

}
