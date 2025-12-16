package com.example.order.server.application.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 订单定时任务服务
 */
public interface OrderScheduledTaskService {

    /**
     * 自动取消过期订单
     * @return 取消的订单数量
     */
    @Transactional
    int autoCancelExpiredOrders();
}
