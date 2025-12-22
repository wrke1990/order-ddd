package com.example.order.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.vo.AfterSaleStatus;

/**
 * 售后单仓储接口
 */
public interface AfterSaleOrderRepository {

    /**
     * 保存售后单
     */
    AfterSaleOrder save(AfterSaleOrder afterSaleOrder);

    /**
     * 根据售后单号查询售后单
     */
    Optional<AfterSaleOrder> findByAfterSaleNo(String afterSaleNo);

    /**
     * 根据订单号查询售后单列表
     */
    List<AfterSaleOrder> findByOrderNo(String orderNo);

    /**
     * 根据用户ID和订单号查询售后单列表
     */
    List<AfterSaleOrder> findByUserIdAndOrderNo(Long userId, String orderNo);

    /**
     * 根据用户ID和售后状态查询售后单列表
     */
    List<AfterSaleOrder> findByUserIdAndStatus(Long userId, AfterSaleStatus status);

    /**
     * 删除售后单
     */
    void deleteByAfterSaleNo(String afterSaleNo);


}
