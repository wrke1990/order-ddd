package com.example.order.server.interfaces.rest;

import com.example.order.common.response.CommonResponse;
import com.example.order.domain.model.vo.Id;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderResponse;
import com.example.order.server.application.service.OrderCommandService;
import com.example.order.server.application.service.OrderQueryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * 订单REST控制器
 */
@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderController(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    /**
     * 创建订单
     */
    @PostMapping
    public CommonResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        OrderResponse response = orderCommandService.createOrder(command);
        return CommonResponse.success(response);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{orderId}")
    public CommonResponse<OrderResponse> getOrderById(@PathVariable @Min(1) Long orderId, @RequestParam Long userId) {
        OrderResponse response = orderQueryService.getOrderById(orderId, userId);
        return CommonResponse.success(response);
    }

    /**
     * 支付订单
     */
    @PutMapping("/{orderId}/pay")
    public CommonResponse<Void> payOrder(@PathVariable @Min(1) Long orderId, @RequestParam Long userId) {
        orderCommandService.payOrder(String.valueOf(orderId), Id.of(userId));
        return CommonResponse.success();
    }

    /**
     * 取消订单
     */
    @PutMapping("/{orderId}/cancel")
    public CommonResponse<Void> cancelOrder(@PathVariable @Min(1) Long orderId, @RequestParam Long userId) {
        orderCommandService.cancelOrder(String.valueOf(orderId), Id.of(userId));
        return CommonResponse.success();
    }

    /**
     * 发货
     */
    @PutMapping("/{orderId}/ship")
    public CommonResponse<Void> shipOrder(@PathVariable @Min(1) Long orderId, @RequestParam Long userId) {
        orderCommandService.shipOrder(String.valueOf(orderId), Id.of(userId));
        return CommonResponse.success();
    }

    /**
     * 完成订单
     */
    @PutMapping("/{orderId}/complete")
    public CommonResponse<Void> completeOrder(@PathVariable @Min(1) Long orderId, @RequestParam Long userId) {
        orderCommandService.completeOrder(String.valueOf(orderId), Id.of(userId));
        return CommonResponse.success();
    }
}
