package com.example.order.domain.service.validator;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Coupon;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Price;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单业务规则验证器
 * 集中管理订单相关的业务规则验证
 */
public class OrderBusinessRuleValidator {

    /**
     * 最大订单金额限制（单位：分）
     */
    private static final long MAX_ORDER_AMOUNT = 100000000; // 100万元

    /**
     * 最大订单项数量限制
     */
    private static final int MAX_ORDER_ITEMS = 50;

    /**
     * 最小订单项数量
     */
    private static final int MIN_ORDER_ITEMS = 1;

    /**
     * 订单有效期（分钟）
     */
    private static final int ORDER_VALIDITY_MINUTES = 15;

    /**
     * 验证创建订单的业务规则
     */
    public static void validateOrderCreation(Id userId, List<OrderItem> orderItems) {
        validateUserId(userId);
        validateOrderItems(orderItems);
        validateTotalAmount(calculateTotalAmount(orderItems));
    }

    /**
     * 验证用户ID
     */
    private static void validateUserId(Id userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
    }

    /**
     * 验证订单项
     */
    private static void validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("订单至少包含一个订单项");
        }
        if (orderItems.size() > MAX_ORDER_ITEMS) {
            throw new IllegalArgumentException("订单包含的商品数量不能超过" + MAX_ORDER_ITEMS + "个");
        }

        // 验证每个订单项
        for (OrderItem item : orderItems) {
            validateOrderItem(item);
        }
    }

    /**
     * 验证单个订单项
     */
    private static void validateOrderItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("订单项不能为空");
        }
        if (item.getProductId() == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        if (item.getProductName() == null || item.getProductName().isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("商品数量必须大于0");
        }
        if (item.getPrice() == null || item.getPrice().getAmount() <= 0) {
            throw new IllegalArgumentException("商品价格必须大于0");
        }
    }

    /**
     * 验证订单总金额
     */
    private static void validateTotalAmount(Price totalAmount) {
        if (totalAmount == null) {
            throw new IllegalArgumentException("订单总金额不能为空");
        }
        if (totalAmount.getAmount() <= 0) {
            throw new IllegalArgumentException("订单总金额必须大于0");
        }
        if (totalAmount.getAmount() > MAX_ORDER_AMOUNT) {
            throw new IllegalArgumentException("订单总金额不能超过" + MAX_ORDER_AMOUNT / 100.0 + "元");
        }
    }

    /**
     * 验证订单支付业务规则
     */
    public static void validateOrderPayment(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("订单不能为空");
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("订单只能在待支付状态下进行支付");
        }
        if (order.isExpired()) {
            throw new IllegalArgumentException("订单已过期，无法支付");
        }
        validateTotalAmount(order.getTotalAmount());
    }

    /**
     * 验证优惠券使用规则
     */
    public static void validateCouponUsage(Order order, Coupon coupon) {
        if (order == null) {
            throw new IllegalArgumentException("订单不能为空");
        }
        if (coupon == null) {
            return; // 允许不使用优惠券
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("只有待支付状态的订单可以使用优惠券");
        }
        if (!coupon.isValid(LocalDateTime.now(), order.getTotalAmount())) {
            throw new IllegalArgumentException("优惠券无效");
        }
        // 计算使用优惠券后的金额，确保不小于0
        long actualAmountValue = Math.max(0, order.getTotalAmount().getAmount() - coupon.getDiscountAmount().getAmount());
        if (actualAmountValue < 0) {
            throw new IllegalArgumentException("使用优惠券后订单金额不能为负数");
        }
    }

    /**
     * 验证订单取消规则
     */
    public static void validateOrderCancellation(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("订单不能为空");
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("只有待支付状态的订单可以取消");
        }
    }

    /**
     * 验证订单发货规则
     */
    public static void validateOrderShipping(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("订单不能为空");
        }
        if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.PENDING_SHIPMENT) {
            throw new IllegalArgumentException("只有已支付或待发货状态的订单可以发货");
        }
        if (order.getShippingAddress() == null) {
            throw new IllegalArgumentException("订单收货地址不能为空");
        }
    }

    /**
     * 计算订单总金额
     */
    private static Price calculateTotalAmount(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return Price.ofCNY(0L);
        }

        long total = 0;
        String currency = orderItems.get(0).getPrice().getCurrency();

        for (OrderItem item : orderItems) {
            total += item.getTotalPrice().getAmount();
            if (!currency.equals(item.getPrice().getCurrency())) {
                throw new IllegalArgumentException("订单中所有商品的货币类型必须一致");
            }
        }

        return new Price(total, currency);
    }
}
