package com.example.order.infrastructure.repository.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.event.DomainEventPublisher;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Page;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.assember.OrderAssembler;
import com.example.order.infrastructure.persistence.po.OrderPO;
import com.example.order.infrastructure.persistence.repository.JpaOrderRepository;

/**
 * 订单仓储实现
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderAssembler orderAssembler;
    private final DomainEventPublisher domainEventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository, OrderAssembler orderAssembler, DomainEventPublisher domainEventPublisher, RedisTemplate<String, Object> redisTemplate) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.orderAssembler = orderAssembler;
        this.domainEventPublisher = domainEventPublisher;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @CachePut(value = "order", key = "#result.id.value")
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
    @Cacheable(value = "order", key = "#orderNo")
    public Optional<Order> findByUserIdAndOrderNo(Id userId, String orderNo) {
        return jpaOrderRepository.findByUserIdAndOrderNo(userId.getValue(), orderNo)
                .map(orderAssembler::toOrder);
    }

    @Override
    @Cacheable(value = "order", key = "#orderId.value")
    public Optional<Order> findByUserIdAndId(Id userId, Id orderId) {
        if (userId == null) {
            // 管理员操作，直接根据订单ID查询
            return jpaOrderRepository.findById(orderId.getValue())
                    .map(orderAssembler::toOrder);
        } else {
            // 普通用户，需要同时验证用户ID和订单ID
            return jpaOrderRepository.findByUserIdAndId(userId.getValue(), orderId.getValue())
                    .map(orderAssembler::toOrder);
        }
    }

    @Override
    public List<Order> findByUserId(Id userId) {
        return orderAssembler.batchToOrder(jpaOrderRepository.findByUserId(userId.getValue()));
    }

    @Override
    public List<Order> findByUserIdAndStatus(Id userId, OrderStatus status) {
        return orderAssembler.batchToOrder(jpaOrderRepository.findByUserIdAndStatus(userId.getValue(), status.name()));
    }

    @Override
    public List<Order> findExpiredOrders(OrderStatus status, LocalDateTime expireTime) {
        return orderAssembler.batchToOrder(jpaOrderRepository.findByStatusAndUpdateTimeBefore(status.name(), expireTime));
    }

    @Override
    @CacheEvict(value = "order", key = "#orderId.value")
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
        return orderAssembler.batchToOrder(jpaOrderRepository.findByOrderNoStartingWith(prefix));
    }

    @Override
    public List<Order> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return orderAssembler.batchToOrder(jpaOrderRepository.findByCreateTimeBetween(startTime, endTime));
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

    @Override
    @Transactional
    public int batchUpdateStatus(List<Id> orderIds, OrderStatus newStatus, LocalDateTime updateTime) {
        // 将Id对象列表转换为Long列表
        List<Long> orderIdValues = orderIds.stream()
                .map(Id::getValue)
                .collect(Collectors.toList());

        // 调用JPA方法批量更新状态
        int result = jpaOrderRepository.batchUpdateStatus(orderIdValues, newStatus.name(), updateTime);
        
        // 手动清除缓存
        for (Id orderId : orderIds) {
            String cacheKey = "ORDER_order::" + orderId.getValue();
            redisTemplate.delete(cacheKey);
        }
        
        return result;
    }
}
