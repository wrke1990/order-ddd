package com.example.order.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Page;

/**
 * 订单仓储接口
 */
public interface OrderRepository {

    /**
     * 保存订单
     */
    Order save(Order order);

    /**
     * 根据用户ID和订单号查询订单
     */
    Optional<Order> findByUserIdAndOrderNo(Id userId, String orderNo);

    /**
     * 根据用户ID和订单ID查询订单
     */
    Optional<Order> findByUserIdAndId(Id userId, Id orderId);

    /**
     * 根据用户ID查询订单列表
     */
    List<Order> findByUserId(Id userId);

    /**
     * 根据用户ID查询订单列表（分页）
     */
    Page<Order> findByUserId(Id userId, int pageNum, int pageSize);

    /**
     * 根据用户ID和订单状态查询订单列表
     */
    List<Order> findByUserIdAndStatus(Id userId, OrderStatus status);

    /**
     * 根据用户ID和订单状态查询订单列表（分页）
     */
    Page<Order> findByUserIdAndStatus(Id userId, OrderStatus status, int pageNum, int pageSize);

    /**
     * 查询过期订单
     */
    List<Order> findExpiredOrders(OrderStatus status, LocalDateTime expireTime);

    /**
     * 删除订单
     */
    void deleteById(Id orderId);

    /**
     * 根据订单号前缀查询订单（用于搜索）
     */
    List<Order> findByOrderNoStartingWith(String prefix);

    /**
     * 根据创建时间范围查询订单
     */
    List<Order> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据创建时间范围和用户ID查询订单（分页）
     */
    Page<Order> findByUserIdAndCreateTimeBetween(Id userId, LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize);
}
