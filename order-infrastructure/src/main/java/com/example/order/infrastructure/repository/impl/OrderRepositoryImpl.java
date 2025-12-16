package com.example.order.infrastructure.repository.impl;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.event.DomainEventPublisher;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Page;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.assember.OrderAssembler;
import com.example.order.infrastructure.persistence.po.OrderPO;
import com.example.order.infrastructure.persistence.repository.JpaOrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单仓储实现
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderAssembler orderAssembler;
    private final DomainEventPublisher domainEventPublisher;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository, OrderAssembler orderAssembler, DomainEventPublisher domainEventPublisher) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.orderAssembler = orderAssembler;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Order save(Order order) {
        OrderPO orderPO = orderAssembler.toOrderPO(order);
        OrderPO savedPO = jpaOrderRepository.save(orderPO);

        // 发布领域事件
        if (order.hasDomainEvents()) {
            domainEventPublisher.publishAll(order.getDomainEvents());
        }

        return orderAssembler.toOrder(savedPO);
    }

    @Override
    public Optional<Order> findById(Id orderId) {
        return jpaOrderRepository.findById(orderId.getValue())
                .map(orderAssembler::toOrder);
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        return jpaOrderRepository.findByOrderNo(orderNo)
                .map(orderAssembler::toOrder);
    }

    @Override
    public List<Order> findByUserId(Id userId) {
        return jpaOrderRepository.findByUserId(userId.getValue()).stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByUserIdAndStatus(Id userId, OrderStatus status) {
        return jpaOrderRepository.findByUserIdAndStatus(userId.getValue(), status.name()).stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findExpiredOrders(OrderStatus status, LocalDateTime expireTime) {
        return jpaOrderRepository.findByStatusAndUpdateTimeBefore(status.name(), expireTime).stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Id orderId) {
        jpaOrderRepository.deleteById(orderId.getValue());
    }

    @Override
    public Page<Order> findByUserId(Id userId, int pageNum, int pageSize) {
        // 使用Spring Data JPA的PageRequest创建分页请求
        org.springframework.data.domain.Page<OrderPO> jpaPage =
                jpaOrderRepository.findByUserId(userId.getValue(), PageRequest.of(pageNum, pageSize));

        // 转换为领域模型的Page对象
        List<Order> orderList = jpaPage.getContent().stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());

        return Page.of(
                orderList,
                jpaPage.getNumber(),
                jpaPage.getSize(),
                jpaPage.getTotalElements()
        );
    }

    @Override
    public Page<Order> findByUserIdAndStatus(Id userId, OrderStatus status, int pageNum, int pageSize) {
        // 使用Spring Data JPA的PageRequest创建分页请求
        org.springframework.data.domain.Page<OrderPO> jpaPage =
                jpaOrderRepository.findByUserIdAndStatus(userId.getValue(), status.name(), PageRequest.of(pageNum, pageSize));

        // 转换为领域模型的Page对象
        List<Order> orderList = jpaPage.getContent().stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());

        return Page.of(
                orderList,
                jpaPage.getNumber(),
                jpaPage.getSize(),
                jpaPage.getTotalElements()
        );
    }

    @Override
    public List<Order> findByOrderNoStartingWith(String prefix) {
        return jpaOrderRepository.findByOrderNoStartingWith(prefix).stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return jpaOrderRepository.findByCreateTimeBetween(startTime, endTime).stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findByUserIdAndCreateTimeBetween(Id userId, LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize) {
        // 使用Spring Data JPA的PageRequest创建分页请求
        org.springframework.data.domain.Page<OrderPO> jpaPage =
                jpaOrderRepository.findByUserIdAndCreateTimeBetween(userId.getValue(), startTime, endTime, PageRequest.of(pageNum, pageSize));

        // 转换为领域模型的Page对象
        List<Order> orderList = jpaPage.getContent().stream()
                .map(orderAssembler::toOrder)
                .collect(Collectors.toList());

        return Page.of(
                orderList,
                jpaPage.getNumber(),
                jpaPage.getSize(),
                jpaPage.getTotalElements()
        );
    }
}
