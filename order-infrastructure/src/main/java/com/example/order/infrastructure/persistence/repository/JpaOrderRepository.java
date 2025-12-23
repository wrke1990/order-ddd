package com.example.order.infrastructure.persistence.repository;

import com.example.order.infrastructure.persistence.po.OrderPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA订单仓库接口
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderPO, Long> {

    /**
     * 根据订单号查询订单
     */
    Optional<OrderPO> findByOrderNo(String orderNo);

    /**
     * 根据用户ID和订单号查询订单
     */
    Optional<OrderPO> findByUserIdAndOrderNo(Long userId, String orderNo);

    /**
     * 根据用户ID和订单ID查询订单
     */
    Optional<OrderPO> findByUserIdAndId(Long userId, Long orderId);

    /**
     * 根据用户ID查询订单列表
     */
    List<OrderPO> findByUserId(Long userId);

    /**
     * 根据用户ID和状态查询订单列表
     */
    List<OrderPO> findByUserIdAndStatus(Long userId, String status);

    /**
     * 根据状态和更新时间查询过期订单
     */
    List<OrderPO> findByStatusAndUpdateTimeBefore(String status, LocalDateTime expireTime);

    /**
     * 根据用户ID分页查询订单
     */
    Page<OrderPO> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户ID和状态分页查询订单
     */
    Page<OrderPO> findByUserIdAndStatus(Long userId, String status, Pageable pageable);

    /**
     * 根据订单号前缀查询订单
     */
    List<OrderPO> findByOrderNoStartingWith(String prefix);

    /**
     * 根据创建时间范围查询订单
     */
    List<OrderPO> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和创建时间范围分页查询订单
     */
    Page<OrderPO> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
