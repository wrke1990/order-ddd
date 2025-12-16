package com.example.order.server.application.service;

import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Page;
import com.example.order.server.application.dto.OrderResponse;

import java.util.List;

/**
 * 订单查询服务接口
 * 负责处理所有订单相关的查询操作（读操作）
 */
public interface OrderQueryService {

    /**
     * 查询订单详情
     */
    OrderResponse getOrderById(Long orderId);

    /**
     * 查询用户订单列表
     */
    List<OrderResponse> getOrdersByUserId(Long userId);

    /**
     * 根据状态查询用户订单列表
     */
    List<OrderResponse> getOrdersByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * 根据状态查询用户订单列表（带分页）
     */
    Page<OrderResponse> getOrdersByUserIdAndStatus(Long userId, OrderStatus status, Integer page, Integer size);

    /**
     * 分页查询用户订单
     */
    Page<OrderResponse> getOrdersByUserId(Long userId, int pageNum, int pageSize);
}
