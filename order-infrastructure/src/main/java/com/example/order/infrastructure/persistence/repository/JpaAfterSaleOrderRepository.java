package com.example.order.infrastructure.persistence.repository;

import com.example.order.infrastructure.persistence.po.AfterSaleOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA售后订单仓库接口
 */
@Repository
public interface JpaAfterSaleOrderRepository extends JpaRepository<AfterSaleOrderPO, Long> {

    /**
     * 根据售后单号查询售后订单
     */
    Optional<AfterSaleOrderPO> findByAfterSaleNo(String afterSaleNo);

    /**
     * 根据订单号查询售后订单列表
     */
    List<AfterSaleOrderPO> findByOrderNo(String orderNo);

    /**
     * 根据用户ID和状态查询售后订单列表
     */
    List<AfterSaleOrderPO> findByUserIdAndStatus(Long userId, String status);

    /**
     * 根据售后单号删除售后订单
     */
    void deleteByAfterSaleNo(String afterSaleNo);

    /**
     * 根据用户ID查询售后订单列表
     */
    List<AfterSaleOrderPO> findByUserId(Long userId);
}
