package com.example.order.server.application.service.impl;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.acl.payment.PaymentClient;
import com.example.order.infrastructure.acl.payment.dto.PaymentOrderDTO;
import com.example.order.server.application.service.OrderScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单定时任务服务实现
 */
@Service
public class OrderScheduledTaskServiceImpl implements OrderScheduledTaskService {

    private static final Logger logger = LoggerFactory.getLogger(OrderScheduledTaskServiceImpl.class);

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    public OrderScheduledTaskServiceImpl(OrderRepository orderRepository, PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
    }

    @Override
    @Transactional
    public int autoCancelExpiredOrders() {
        logger.info("开始执行自动取消过期订单任务");

        // 查询过期的待支付订单
        List<Order> expiredOrders = orderRepository.findExpiredOrders(OrderStatus.PENDING_PAYMENT, LocalDateTime.now());

        // 取消所有过期订单
        int count = 0;
        for (Order order : expiredOrders) {
            try {
                order.cancel();
                orderRepository.save(order);

                // 取消对应的支付订单
                Optional<PaymentOrderDTO> paymentOrder = paymentClient.getPaymentOrderByOrderNo(order.getOrderNo());
                boolean cancelResult = false;
                if (paymentOrder.isPresent()) {
                    cancelResult = paymentClient.cancelPayment(Id.of(paymentOrder.get().getPaymentOrderId()));
                }
                if (cancelResult) {
                    logger.info("自动取消订单和支付订单成功: {}", order.getOrderNo());
                } else {
                    logger.warn("自动取消订单成功，但取消支付订单失败: {}", order.getOrderNo());
                }

                count++;
            } catch (Exception e) {
                // 记录日志，但不中断处理
                logger.error("自动取消订单失败: {}", order.getOrderNo(), e);
            }
        }

        logger.info("自动取消过期订单任务完成，取消订单数量: {}", count);
        return count;
    }
}
