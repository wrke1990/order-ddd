package com.example.order.infrastructure.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.event.DomainEventPublisher;
import com.example.order.domain.model.vo.AfterSaleStatus;
import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.infrastructure.assember.AfterSaleOrderAssembler;
import com.example.order.infrastructure.persistence.po.AfterSaleOrderPO;
import com.example.order.infrastructure.persistence.repository.JpaAfterSaleOrderRepository;

/**
 * 售后订单仓储实现
 */
@Repository
public class AfterSaleOrderRepositoryImpl implements AfterSaleOrderRepository {

    private final JpaAfterSaleOrderRepository jpaAfterSaleOrderRepository;
    private final AfterSaleOrderAssembler afterSaleOrderAssembler;
    private final DomainEventPublisher domainEventPublisher;

    public AfterSaleOrderRepositoryImpl(JpaAfterSaleOrderRepository jpaAfterSaleOrderRepository, AfterSaleOrderAssembler afterSaleOrderAssembler, DomainEventPublisher domainEventPublisher) {
        this.jpaAfterSaleOrderRepository = jpaAfterSaleOrderRepository;
        this.afterSaleOrderAssembler = afterSaleOrderAssembler;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public AfterSaleOrder save(AfterSaleOrder afterSaleOrder) {
        AfterSaleOrderPO afterSaleOrderPO = afterSaleOrderAssembler.toAfterSaleOrderPO(afterSaleOrder);
        AfterSaleOrderPO savedPO = jpaAfterSaleOrderRepository.save(afterSaleOrderPO);

        // 发布领域事件
        if (afterSaleOrder.hasDomainEvents()) {
            domainEventPublisher.publishAll(afterSaleOrder.getDomainEvents());
        }

        return afterSaleOrderAssembler.toAfterSaleOrder(savedPO);
    }

    @Override
    public Optional<AfterSaleOrder> findByAfterSaleNo(String afterSaleNo) {
        return jpaAfterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .map(afterSaleOrderAssembler::toAfterSaleOrder);
    }

    @Override
    public List<AfterSaleOrder> findByOrderNo(String orderNo) {
        return jpaAfterSaleOrderRepository.findByOrderNo(orderNo).stream()
                .map(afterSaleOrderAssembler::toAfterSaleOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<AfterSaleOrder> findByUserIdAndOrderNo(Long userId, String orderNo) {
        return jpaAfterSaleOrderRepository.findByUserIdAndOrderNo(userId, orderNo).stream()
                .map(afterSaleOrderAssembler::toAfterSaleOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<AfterSaleOrder> findByUserIdAndStatus(Long userId, AfterSaleStatus status) {
        List<AfterSaleOrderPO> afterSaleOrderPOs;
        if (status != null) {
            afterSaleOrderPOs = jpaAfterSaleOrderRepository.findByUserIdAndStatus(userId, status.name());
        } else {
            // 如果status为null，查询该用户的所有售后订单
            afterSaleOrderPOs = jpaAfterSaleOrderRepository.findByUserId(userId);
        }
        return afterSaleOrderPOs.stream()
                .map(afterSaleOrderAssembler::toAfterSaleOrder)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByAfterSaleNo(String afterSaleNo) {
        jpaAfterSaleOrderRepository.deleteByAfterSaleNo(afterSaleNo);
    }


}
