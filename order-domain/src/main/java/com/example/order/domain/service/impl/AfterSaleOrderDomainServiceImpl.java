package com.example.order.domain.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.AfterSaleStatus;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.AfterSaleOrderDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AfterSaleOrderDomainServiceImpl implements AfterSaleOrderDomainService {

    private static final Logger logger = LoggerFactory.getLogger(AfterSaleOrderDomainServiceImpl.class);

    private final AfterSaleOrderRepository afterSaleOrderRepository;
    private final OrderRepository orderRepository;

    public AfterSaleOrderDomainServiceImpl(AfterSaleOrderRepository afterSaleOrderRepository, OrderRepository orderRepository) {
        this.afterSaleOrderRepository = afterSaleOrderRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public AfterSaleOrder createAfterSaleOrder(AfterSaleOrder afterSaleOrder) {
        logger.info("创建售后单，订单号: {}, 售后类型: {}, 申请金额: {}",
                afterSaleOrder.getOrderNo(), afterSaleOrder.getType(), afterSaleOrder.getTotalRefundAmount());

        if (afterSaleOrder == null) {
            throw new BusinessException("售后单不能为空");
        }

        // 验证订单是否存在
        Order order = orderRepository.findByOrderNo(afterSaleOrder.getOrderNo())
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 保存售后单
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        logger.info("售后单创建成功，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder createAfterSaleOrder(String afterSaleNo, Long orderId, String orderNo, Long userId,
                                             AfterSaleType type, String reason, String description, String images,
                                             List<AfterSaleItem> afterSaleItems) {
        logger.info("创建多商品售后单，订单号: {}, 售后类型: {}, 商品数量: {}",
                orderNo, type, afterSaleItems != null ? afterSaleItems.size() : 0);

        // 验证订单是否存在
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 验证订单状态是否允许申请售后
        validateOrderStatusForAfterSale(order);

        // 验证订单项是否可以申请售后
        validateOrderItemsForAfterSale(order, afterSaleItems);

        // 创建售后单
        AfterSaleOrder afterSaleOrder = AfterSaleOrder.create(afterSaleNo, orderId, orderNo, userId,
                type, reason, description, images, afterSaleItems, false);

        // 保存售后单
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        logger.info("多商品售后单创建成功，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder createSuperRefund(String afterSaleNo, Long orderId, String orderNo, Long userId,
                                          List<AfterSaleItem> afterSaleItems) {
        logger.info("创建多商品超级退款，订单号: {}, 商品数量: {}",
                orderNo, afterSaleItems != null ? afterSaleItems.size() : 0);

        // 验证订单是否存在
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 验证订单状态是否允许申请售后
        validateOrderStatusForAfterSale(order);

        // 验证订单项是否可以申请售后
        validateOrderItemsForAfterSale(order, afterSaleItems);

        // 创建超级退款
        AfterSaleOrder afterSaleOrder = AfterSaleOrder.createSuperRefund(afterSaleNo, orderId, orderNo, userId, afterSaleItems);

        // 保存售后单
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        logger.info("多商品超级退款创建成功，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder approveOrRejectAfterSaleOrder(String afterSaleNo, boolean approved, String reason) {
        logger.info("审批售后单，售后单号: {}, 审批结果: {}, 原因: {}", afterSaleNo, approved, reason);

        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        if (approved) {
            afterSaleOrder.approve(reason);
            logger.info("售后单审批通过，售后单号: {}", afterSaleNo);
        } else {
            afterSaleOrder.reject(reason);
            logger.info("售后单审批拒绝，售后单号: {}", afterSaleNo);
        }

        return afterSaleOrderRepository.save(afterSaleOrder);
    }

    @Override
    public AfterSaleOrder approveAfterSaleOrder(Long afterSaleId, String reason) {
        logger.info("审批通过售后单，售后ID: {}, 原因: {}", afterSaleId, reason);

        validateAfterSaleId(afterSaleId);
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        afterSaleOrder.approve(reason);

        AfterSaleOrder updatedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        // 如果是退货退款类型，将原订单标记为售后中
        if (updatedAfterSaleOrder.getType() == AfterSaleType.REFUND_WITH_RETURN) {
            Order order = orderRepository.findByOrderNo(updatedAfterSaleOrder.getOrderNo())
                    .orElseThrow(() -> new BusinessException("原订单不存在"));
            order.setStatus(OrderStatus.AFTER_SALES_PROCESSING);
            orderRepository.save(order);
            logger.info("原订单已标记为售后中，订单号: {}", order.getOrderNo());
        }

        logger.info("售后单审批通过，售后单号: {}", updatedAfterSaleOrder.getAfterSaleNo());

        return updatedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder rejectAfterSaleOrder(Long afterSaleId, String reason) {
        logger.info("拒绝售后单，售后ID: {}, 原因: {}", afterSaleId, reason);

        validateAfterSaleId(afterSaleId);
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        afterSaleOrder.reject(reason);

        AfterSaleOrder updatedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);
        logger.info("售后单已拒绝，售后单号: {}", updatedAfterSaleOrder.getAfterSaleNo());

        return updatedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder submitReturnLogistics(String afterSaleNo, String logisticsCompany, String trackingNumber) {
        logger.info("提交退货物流，售后单号: {}, 物流公司: {}, 运单号: {}", afterSaleNo, logisticsCompany, trackingNumber);
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        afterSaleOrder.submitReturnLogistics(logisticsCompany, trackingNumber);
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);
        logger.info("退货物流已提交，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder confirmRefund(String afterSaleNo, Price refundAmount) {
        logger.info("确认退款，售后单号: {}, 退款金额: {}", afterSaleNo, refundAmount);
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        // 确认退款
        afterSaleOrder.confirmRefund(refundAmount);
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        // 同步更新原订单的状态和订单项的售后状态
        updateOrderStatusAfterRefund(savedAfterSaleOrder);

        logger.info("退款已确认，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder confirmReturn(String afterSaleNo) {
        logger.info("确认收到退货，售后单号: {}", afterSaleNo);

        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findByAfterSaleNo(afterSaleNo)
                .orElseThrow(() -> new BusinessException("售后单不存在"));

        afterSaleOrder.confirmReturn();

        AfterSaleOrder updatedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);
        logger.info("已确认收到退货，售后单号: {}", updatedAfterSaleOrder.getAfterSaleNo());

        return updatedAfterSaleOrder;
    }

    @Override
    public AfterSaleOrder updateAfterSaleOrder(AfterSaleOrder afterSaleOrder) {
        logger.info("更新售后单，售后单号: {}", afterSaleOrder.getAfterSaleNo());
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);
        logger.info("售后单已更新，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());
        return savedAfterSaleOrder;
    }

    // 辅助方法：验证订单状态是否允许申请售后
    private void validateOrderStatusForAfterSale(Order order) {
        OrderStatus status = order.getStatus();
        if (status == OrderStatus.CANCELLED) {
            throw new BusinessException("已取消的订单不能申请售后");
        } else if (status == OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("未支付的订单不能申请售后");
        }
        // 只有已完成、已发货、已支付、待发货、待收货的订单可以申请售后
    }

    // 辅助方法：验证订单项是否可以申请售后
    private void validateOrderItemsForAfterSale(Order order, List<AfterSaleItem> afterSaleItems) {
        for (AfterSaleItem afterSaleItem : afterSaleItems) {
            boolean found = false;
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getProductId().getValue().equals(afterSaleItem.getProductId())) {
                    found = true;
                    if (!orderItem.canApplyAfterSale()) {
                        throw new BusinessException("商品" + orderItem.getProductName() + "已申请过售后");
                    }
                    if (afterSaleItem.getRefundQuantity() > orderItem.getQuantity()) {
                        throw new BusinessException("商品" + orderItem.getProductName() + "的退款数量不能超过购买数量");
                    }
                    break;
                }
            }
            if (!found) {
                throw new BusinessException("订单中不存在商品ID为" + afterSaleItem.getProductId() + "的商品");
            }
        }
    }

    // 辅助方法：验证售后ID
    private void validateAfterSaleId(Long afterSaleId) {
        if (afterSaleId == null || afterSaleId <= 0) {
            throw new BusinessException("售后单ID无效");
        }
    }

    // 辅助方法：退款后更新原订单状态
    private void updateOrderStatusAfterRefund(AfterSaleOrder afterSaleOrder) {
        // 获取原订单
        Order order = orderRepository.findByOrderNo(afterSaleOrder.getOrderNo())
                .orElseThrow(() -> new BusinessException("原订单不存在"));

        // 标记订单项为已退款和已退货
        for (AfterSaleItem afterSaleItem : afterSaleOrder.getAfterSaleItems()) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getProductId().getValue().equals(afterSaleItem.getProductId())) {
                    // 标记为已退款
                    orderItem.refund(afterSaleItem.getRefundAmount());

                    // 如果是退货退款类型，标记为已退货
                    if (afterSaleOrder.getType() == AfterSaleType.REFUND_WITH_RETURN) {
                        orderItem.markAsReturned();
                    }
                    break;
                }
            }
        }

        // 更新订单状态
        updateOrderStatusBasedOnItems(order);

        // 保存更新后的订单
        orderRepository.save(order);

        logger.info("原订单状态已更新，订单号: {}", order.getOrderNo());
    }

    // 辅助方法：根据订单项状态更新订单状态
    private void updateOrderStatusBasedOnItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        boolean allRefunded = orderItems.stream().allMatch(OrderItem::isRefunded);
        boolean allReturned = orderItems.stream().allMatch(OrderItem::isReturned);
        boolean anyRefunded = orderItems.stream().anyMatch(OrderItem::isRefunded);

        if (allRefunded && allReturned) {
            order.setStatus(OrderStatus.REFUNDED);
        } else if (allRefunded) {
            order.setStatus(OrderStatus.REFUNDED);
        } else if (anyRefunded) {
            order.setStatus(OrderStatus.PARTIALLY_REFUNDED);
        }

        // 如果订单还有未处理的售后，则标记为售后中
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderRepository.findByOrderNo(order.getOrderNo());
        boolean hasPendingAfterSale = afterSaleOrders.stream()
                .anyMatch(afterSale -> afterSale.getStatus() != AfterSaleStatus.COMPLETED &&
                                    afterSale.getStatus() != AfterSaleStatus.REJECTED);

        if (hasPendingAfterSale) {
            order.setStatus(OrderStatus.AFTER_SALES_PROCESSING);
        }
    }
}
