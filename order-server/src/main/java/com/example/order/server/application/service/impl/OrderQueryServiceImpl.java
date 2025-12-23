package com.example.order.server.application.service.impl;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Page;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.server.application.assember.OrderDtoAssembler;
import com.example.order.server.application.dto.OrderResponse;
import com.example.order.server.application.service.OrderQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单查询服务实现类
 * 负责处理所有订单相关的查询操作（读操作）
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderDtoAssembler orderDtoAssembler;

    public OrderQueryServiceImpl(OrderRepository orderRepository, OrderDtoAssembler orderDtoAssembler) {
        this.orderRepository = orderRepository;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Override
    @Cacheable(value = "orders", key = "#orderId + '-' + #userId")
    public OrderResponse getOrderById(Long orderId, Long userId) {
        logger.info("查询订单详情，订单ID: {}, 用户ID: {}", orderId, userId);

        // 统一使用findByUserIdAndId方法查询订单，内部已处理管理员和普通用户的权限逻辑
        Order order = orderRepository.findByUserIdAndId(
                userId != null ? Id.of(userId) : null,
                Id.of(orderId)
        ).orElseThrow(() -> new RuntimeException("订单不存在"));

        return orderDtoAssembler.toOrderResponse(order);
    }

    @Override
    @Cacheable(value = "ordersByOrderNo", key = "#orderNo + '-' + #userId")
    public OrderResponse getOrderByOrderNo(String orderNo, Long userId) {
        logger.info("根据订单号查询订单详情，订单号: {}, 用户ID: {}", orderNo, userId);

        // 使用findByUserIdAndOrderNo方法查询订单，内部已处理管理员和普通用户的权限逻辑
        Order order = orderRepository.findByUserIdAndOrderNo(
                userId != null ? Id.of(userId) : null,
                orderNo
        ).orElseThrow(() -> new RuntimeException("订单不存在"));

        return orderDtoAssembler.toOrderResponse(order);
    }

    @Override
    @Cacheable(value = "ordersByUser", key = "#userId")
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        logger.info("查询用户所有订单，用户ID: {}", userId);
        // 直接调用repository查询
        List<Order> orders = orderRepository.findByUserIdAndStatus(Id.of(userId), null);
        return orders.stream()
                .map(orderDtoAssembler::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "ordersByUserAndStatus", key = "#userId + '-' + #status")
    public List<OrderResponse> getOrdersByUserIdAndStatus(Long userId, OrderStatus status) {
        logger.info("查询用户订单列表，用户ID: {}, 订单状态: {}", userId, status);
        // 直接调用repository查询
        List<Order> orders = orderRepository.findByUserIdAndStatus(Id.of(userId), status);
        return orders.stream()
                .map(orderDtoAssembler::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "ordersByUserAndStatusPage", key = "#userId + '-' + #status + '-' + #page + '-' + #size")
    public Page<OrderResponse> getOrdersByUserIdAndStatus(Long userId, OrderStatus status, Integer page, Integer size) {
        logger.info("分页查询用户订单列表，用户ID: {}, 订单状态: {}, 页码: {}, 每页大小: {}", userId, status, page, size);
        // 调用repository的分页查询方法
        Page<Order> orderPage = orderRepository.findByUserIdAndStatus(Id.of(userId), status, page, size);

        // 将Order对象转换为OrderResponse对象
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderDtoAssembler::toOrderResponse)
                .collect(Collectors.toList());

        // 创建新的Page对象，封装转换后的OrderResponse列表
        return Page.of(orderResponses, orderPage.getPageNum(), orderPage.getPageSize(), orderPage.getTotalElements());
    }

    @Override
    @Cacheable(value = "ordersByUserPage", key = "#userId + '-' + #pageNum + '-' + #pageSize")
    public Page<OrderResponse> getOrdersByUserId(Long userId, int pageNum, int pageSize) {
        logger.info("分页查询用户订单，用户ID: {}, 页码: {}, 每页大小: {}", userId, pageNum, pageSize);
        // 调用repository的分页查询方法
        Page<Order> orderPage = orderRepository.findByUserId(Id.of(userId), pageNum, pageSize);

        // 将Order对象转换为OrderResponse对象
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderDtoAssembler::toOrderResponse)
                .collect(Collectors.toList());

        // 创建新的Page对象，封装转换后的OrderResponse列表
        return Page.of(orderResponses, orderPage.getPageNum(), orderPage.getPageSize(), orderPage.getTotalElements());
    }
}
