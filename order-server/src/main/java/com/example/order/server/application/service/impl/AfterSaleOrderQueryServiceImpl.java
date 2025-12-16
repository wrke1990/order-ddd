package com.example.order.server.application.service.impl;

import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.server.application.assember.AfterSaleOrderDtoAssembler;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.service.AfterSaleOrderQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后订单查询服务实现类
 */
@Service
public class AfterSaleOrderQueryServiceImpl implements AfterSaleOrderQueryService {

    private static final Logger log = LoggerFactory.getLogger(AfterSaleOrderQueryServiceImpl.class);

    private final AfterSaleOrderRepository afterSaleOrderRepository;
    private final AfterSaleOrderDtoAssembler afterSaleOrderDtoAssembler;

    public AfterSaleOrderQueryServiceImpl(AfterSaleOrderRepository afterSaleOrderRepository,
                                          AfterSaleOrderDtoAssembler afterSaleOrderDtoAssembler) {
        this.afterSaleOrderRepository = afterSaleOrderRepository;
        this.afterSaleOrderDtoAssembler = afterSaleOrderDtoAssembler;
    }

    @Override
    public AfterSaleOrderResponse getAfterSaleOrderByNo(String afterSaleNo) {
        log.info("根据售后单号查询售后订单，单号: {}", afterSaleNo);
        return afterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .map(afterSaleOrderDtoAssembler::toAfterSaleOrderResponse)
                .orElseThrow(() -> new IllegalArgumentException("售后订单不存在: " + afterSaleNo));
    }

    @Override
    public AfterSaleOrderResponse getAfterSaleOrderById(Long afterSaleId) {
        log.info("根据售后订单ID查询售后订单，ID: {}", afterSaleId);
        return afterSaleOrderRepository.findById(afterSaleId)
                .map(afterSaleOrderDtoAssembler::toAfterSaleOrderResponse)
                .orElseThrow(() -> new IllegalArgumentException("售后订单不存在: " + afterSaleId));
    }

    @Override
    public List<AfterSaleOrderResponse> getAfterSaleOrdersByUserId(Long userId) {
        log.info("根据用户ID查询所有售后订单，用户ID: {}", userId);
        // 查询所有状态的售后订单
        List<com.example.order.domain.model.aggregate.AfterSaleOrder> afterSaleOrders =
                afterSaleOrderRepository.findByUserIdAndStatus(userId, null);
        return afterSaleOrders.stream()
                .map(afterSaleOrderDtoAssembler::toAfterSaleOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AfterSaleOrderResponse> getAfterSaleOrdersByUserId(Long userId, Integer page, Integer size) {
        log.info("根据用户ID分页查询售后订单，用户ID: {}, 页码: {}, 每页大小: {}", userId, page, size);
        // 这里简化实现，实际项目中应该调用仓储层的分页查询方法
        List<com.example.order.domain.model.aggregate.AfterSaleOrder> afterSaleOrders =
                afterSaleOrderRepository.findByUserIdAndStatus(userId, null);
        return afterSaleOrders.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(afterSaleOrderDtoAssembler::toAfterSaleOrderResponse)
                .collect(Collectors.toList());
    }
}
