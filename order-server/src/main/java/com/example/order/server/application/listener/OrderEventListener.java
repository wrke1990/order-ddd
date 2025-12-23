package com.example.order.server.application.listener;

import com.example.order.domain.model.event.*;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.service.ShoppingCartDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单事件监听器
 * 负责处理订单相关的领域事件
 */
@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    private final ShoppingCartDomainService shoppingCartDomainService;

    @Autowired
    public OrderEventListener(ShoppingCartDomainService shoppingCartDomainService) {
        this.shoppingCartDomainService = shoppingCartDomainService;
    }

    /**
     * 监听订单创建事件
     */
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("[订单事件监听器] 收到订单创建事件: {}", event);

        // 实现订单创建后的业务逻辑
        // 1. 记录订单创建日志
        logger.info("[订单创建日志] 订单号: {}, 用户ID: {}, 创建时间: {}",
                event.getOrderNo(), event.getUserId(), event.getOccurredTime());

        // 2. 模拟发送订单确认通知
        sendOrderConfirmationNotification(event);

        // 3. 模拟触发库存锁定
        lockInventoryForOrder(event);

        // 4. 模拟发送消息到消息队列供其他系统消费
        publishOrderCreatedToMessageQueue(event);

        // 5. 删除对应购物车记录
        deleteShoppingCartItems(event);
    }

    /**
     * 发送订单确认通知
     */
    private void sendOrderConfirmationNotification(OrderCreatedEvent event) {
        // 实际项目中应该调用短信/邮件服务
        logger.info("[通知服务] 向用户 {} 发送订单确认通知，订单号: {}",
                event.getUserId(), event.getOrderNo());
    }

    /**
     * 锁定订单所需的库存
     */
    private void lockInventoryForOrder(OrderCreatedEvent event) {
        // 实际项目中应该调用库存服务
        logger.info("[库存服务] 锁定订单 {} 的库存", event.getOrderNo());
    }

    /**
     * 发布订单创建事件到消息队列
     */
    private void publishOrderCreatedToMessageQueue(OrderCreatedEvent event) {
        // 实际项目中应该调用消息队列服务（如RabbitMQ、Kafka等）
        logger.info("[消息队列] 发布订单创建事件，订单号: {}", event.getOrderNo());
    }

    /**
     * 删除对应购物车记录
     */
    private void deleteShoppingCartItems(OrderCreatedEvent event) {
        if (event.getOrderItems() != null && !event.getOrderItems().isEmpty()) {
            // 提取商品ID列表
            List<Id> productIds = event.getOrderItems().stream()
                    .map(item -> item.getProductId())
                    .collect(Collectors.toList());

            // 调用领域服务批量删除购物车商品
            shoppingCartDomainService.removeItemsByProductIds(event.getUserId(), productIds);

            logger.info("[购物车服务] 批量删除购物车商品，用户ID: {}, 商品数量: {}",
                    event.getUserId(), productIds.size());
        }
    }

    /**
     * 监听订单支付事件
     */
    @EventListener
    public void handleOrderPaidEvent(OrderPaidEvent event) {
        logger.info("[订单事件监听器] 收到订单支付事件: {}", event);

        // 记录订单支付日志
        logger.info("[订单支付日志] 订单号: {}, 订单ID: {}, 支付金额: {}, 支付时间: {}",
                event.getOrderNo(), event.getOrderId(), event.getTotalAmount(), event.getOccurredTime());

        // 执行支付成功后的业务逻辑
        sendPaymentSuccessNotification(event);
        updateInventoryStatus(event);
        triggerLogisticsPreparation(event);
        recordFinancialTransaction(event);
    }

    /**
     * 发送支付成功通知
     */
    private void sendPaymentSuccessNotification(OrderPaidEvent event) {
        logger.info("[通知服务] 向用户发送订单号: {} 的支付成功通知", event.getOrderNo());
    }

    /**
     * 更新库存状态
     */
    private void updateInventoryStatus(OrderPaidEvent event) {
        logger.info("[库存服务] 更新订单 {} 的库存状态为已锁定", event.getOrderNo());
    }

    /**
     * 触发物流系统准备发货
     */
    private void triggerLogisticsPreparation(OrderPaidEvent event) {
        logger.info("[物流服务] 为订单 {} 准备发货", event.getOrderNo());
    }

    /**
     * 记录财务流水
     */
    private void recordFinancialTransaction(OrderPaidEvent event) {
        logger.info("[财务服务] 记录订单 {} 的支付流水", event.getOrderNo());
    }

    /**
     * 监听订单取消事件
     */
    @EventListener
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        logger.info("[订单事件监听器] 收到订单取消事件: {}", event);

        // 实现订单取消后的业务逻辑
        // 1. 记录订单取消日志
        logger.info("[订单取消日志] 订单号: {}, 用户ID: {}, 取消时间: {}, 取消原因: {}",
                event.getOrderNo(), event.getUserId(), event.getOccurredTime(), event.getReason());

        // 2. 发送订单取消通知
        sendOrderCancellationNotification(event);

        // 3. 释放锁定的库存
        releaseLockedInventory(event);

        // 4. 处理退款流程
        processRefund(event);
    }

    /**
     * 发送订单取消通知
     */
    private void sendOrderCancellationNotification(OrderCancelledEvent event) {
        logger.info("[通知服务] 向用户 {} 发送订单取消通知，订单号: {}",
                event.getUserId(), event.getOrderNo());
    }

    /**
     * 释放锁定的库存
     */
    private void releaseLockedInventory(OrderCancelledEvent event) {
        logger.info("[库存服务] 释放订单 {} 锁定的库存", event.getOrderNo());
    }

    /**
     * 处理退款流程
     */
    private void processRefund(OrderCancelledEvent event) {
        logger.info("[退款服务] 为订单 {} 处理退款申请", event.getOrderNo());
    }

    /**
     * 监听订单发货事件
     */
    @EventListener
    public void handleOrderShippedEvent(OrderShippedEvent event) {
        logger.info("[订单事件监听器] 收到订单发货事件: {}", event);

        // TODO: 实现订单发货后的业务逻辑，例如:
        // 1. 发送发货通知短信/邮件
        // 2. 更新物流信息
    }

    /**
     * 监听订单收货事件
     */
    @EventListener
    public void handleOrderReceivedEvent(OrderReceivedEvent event) {
        logger.info("[订单事件监听器] 收到订单收货事件: {}", event);

        // TODO: 实现订单收货后的业务逻辑，例如:
        // 1. 发送交易完成短信/邮件
        // 2. 更新商品评价状态
        // 3. 计算并发放积分
    }

    /**
     * 监听订单地址更新事件
     */
    @EventListener
    public void handleOrderAddressUpdatedEvent(OrderAddressUpdatedEvent event) {
        logger.info("[订单事件监听器] 收到订单地址更新事件: {}", event);

        // TODO: 实现订单地址更新后的业务逻辑，例如:
        // 1. 发送地址变更通知
        // 2. 更新物流系统的配送地址（如果还未发货）
    }
}
