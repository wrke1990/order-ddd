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
    public CommonResponse<OrderResp> getOrder(Long orderId) {
        OrderResponse response = orderQueryService.getOrderById(orderId);
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
    public CommonResponse<Void> payOrder(Long orderId) {
        orderCommandService.payOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> cancelOrder(Long orderId) {
        orderCommandService.cancelOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> shipOrder(Long orderId) {
        orderCommandService.shipOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> confirmReceipt(Long orderId) {
        orderCommandService.confirmReceipt(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> completeOrder(Long orderId) {
        orderCommandService.completeOrder(Id.of(orderId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> changeShippingAddress(Long orderId, Long addressId) {
        orderCommandService.changeShippingAddress(Id.of(orderId), Id.of(addressId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> changePaymentMethod(Long orderId, Long paymentMethodId) {
        orderCommandService.changePaymentMethod(Id.of(orderId), Id.of(paymentMethodId));
        return CommonResponse.success();
    }

}
