package com.example.order.infrastructure.acl.payment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.payment.dto.PaymentOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 支付系统客户端实现类
 * 实现与外部支付系统的交互
 * 使用内存模拟外部系统调用
 */
@Component
public class PaymentClientImpl implements PaymentClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentClientImpl.class);

    // 模拟支付单数据库
    private static final ConcurrentMap<Long, PaymentOrderDTO> PAYMENT_ORDER_MAP = new ConcurrentHashMap<>();
    // 模拟订单号与支付单ID的映射
    private static final ConcurrentMap<String, Long> ORDER_NO_PAYMENT_MAP = new ConcurrentHashMap<>();
    // 支付单ID生成器
    private static final AtomicLong PAYMENT_ORDER_ID_GENERATOR = new AtomicLong(1);

    // 支付状态常量
    private static final String PAYMENT_STATUS_CREATED = "CREATED";
    private static final String PAYMENT_STATUS_PAID = "PAID";
    private static final String PAYMENT_STATUS_CANCELLED = "CANCELLED";
    private static final String PAYMENT_STATUS_EXPIRED = "EXPIRED";
    private static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    // 退款状态常量
    private static final String REFUND_STATUS_NOT_REFUNDED = "NOT_REFUNDED";
    private static final String REFUND_STATUS_REFUNDED = "REFUNDED";
    private static final String REFUND_STATUS_REFUNDING = "REFUNDING";

    @Override
    public Optional<PaymentOrderDTO> createPaymentOrder(String orderNo, Id userId, Price amount, String paymentMethod) {
        LOGGER.info("创建支付单，订单号：{}，用户ID：{}，支付金额：{}，支付方式：{}", 
                orderNo, userId.getValue(), amount.getAmount(), paymentMethod);

        // 检查订单号是否已存在
        if (ORDER_NO_PAYMENT_MAP.containsKey(orderNo)) {
            LOGGER.warn("订单号已存在，订单号：{}", orderNo);
            return Optional.empty();
        }

        // 创建支付单
        PaymentOrderDTO paymentOrder = new PaymentOrderDTO();
        long paymentOrderId = PAYMENT_ORDER_ID_GENERATOR.incrementAndGet();
        paymentOrder.setPaymentOrderId(paymentOrderId);
        paymentOrder.setOrderNo(orderNo);
        paymentOrder.setUserId(userId.getValue());
        paymentOrder.setAmount(new BigDecimal(amount.getAmount()));
        paymentOrder.setCurrency(amount.getCurrency());
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setPaymentChannel(getPaymentChannel(paymentMethod));
        paymentOrder.setPaymentStatus(PAYMENT_STATUS_CREATED);
        paymentOrder.setCreateTime(new Date());
        // 设置支付过期时间为30分钟后
        paymentOrder.setExpireTime(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
        paymentOrder.setRefundStatus(REFUND_STATUS_NOT_REFUNDED);

        // 保存支付单
        PAYMENT_ORDER_MAP.put(paymentOrderId, paymentOrder);
        ORDER_NO_PAYMENT_MAP.put(orderNo, paymentOrderId);

        LOGGER.info("创建支付单成功，支付单ID：{}，订单号：{}", paymentOrderId, orderNo);
        return Optional.of(paymentOrder);
    }

    @Override
    public Optional<PaymentOrderDTO> getPaymentOrderById(Id paymentOrderId) {
        LOGGER.info("获取支付单信息，支付单ID：{}", paymentOrderId.getValue());
        return Optional.ofNullable(PAYMENT_ORDER_MAP.get(paymentOrderId.getValue()));
    }

    @Override
    public Optional<PaymentOrderDTO> getPaymentOrderByOrderNo(String orderNo) {
        LOGGER.info("根据订单号获取支付单信息，订单号：{}", orderNo);
        Long paymentOrderId = ORDER_NO_PAYMENT_MAP.get(orderNo);
        if (paymentOrderId == null) {
            LOGGER.warn("支付单不存在，订单号：{}", orderNo);
            return Optional.empty();
        }
        return Optional.ofNullable(PAYMENT_ORDER_MAP.get(paymentOrderId));
    }

    @Override
    public boolean confirmPayment(Id paymentOrderId) {
        LOGGER.info("确认支付单，支付单ID：{}", paymentOrderId.getValue());
        PaymentOrderDTO paymentOrder = PAYMENT_ORDER_MAP.get(paymentOrderId.getValue());
        if (paymentOrder == null) {
            LOGGER.warn("支付单不存在，支付单ID：{}", paymentOrderId.getValue());
            return false;
        }

        // 检查支付单状态
        if (!PAYMENT_STATUS_CREATED.equals(paymentOrder.getPaymentStatus())) {
            LOGGER.warn("支付单状态错误，支付单ID：{}，当前状态：{}", 
                    paymentOrderId.getValue(), paymentOrder.getPaymentStatus());
            return false;
        }

        // 检查支付单是否过期
        if (new Date().after(paymentOrder.getExpireTime())) {
            LOGGER.warn("支付单已过期，支付单ID：{}", paymentOrderId.getValue());
            paymentOrder.setPaymentStatus(PAYMENT_STATUS_EXPIRED);
            PAYMENT_ORDER_MAP.put(paymentOrderId.getValue(), paymentOrder);
            return false;
        }

        // 模拟支付确认操作
        paymentOrder.setPaymentStatus(PAYMENT_STATUS_PAID);
        paymentOrder.setPayTime(new Date());
        paymentOrder.setTransactionNo(generateTransactionNo());
        PAYMENT_ORDER_MAP.put(paymentOrderId.getValue(), paymentOrder);

        LOGGER.info("确认支付单成功，支付单ID：{}", paymentOrderId.getValue());
        return true;
    }

    @Override
    public boolean cancelPayment(Id paymentOrderId) {
        LOGGER.info("取消支付单，支付单ID：{}", paymentOrderId.getValue());
        PaymentOrderDTO paymentOrder = PAYMENT_ORDER_MAP.get(paymentOrderId.getValue());
        if (paymentOrder == null) {
            LOGGER.warn("支付单不存在，支付单ID：{}", paymentOrderId.getValue());
            return false;
        }

        // 检查支付单状态
        if (!PAYMENT_STATUS_CREATED.equals(paymentOrder.getPaymentStatus())) {
            LOGGER.warn("支付单状态错误，支付单ID：{}，当前状态：{}", 
                    paymentOrderId.getValue(), paymentOrder.getPaymentStatus());
            return false;
        }

        // 模拟取消支付操作
        paymentOrder.setPaymentStatus(PAYMENT_STATUS_CANCELLED);
        PAYMENT_ORDER_MAP.put(paymentOrderId.getValue(), paymentOrder);

        LOGGER.info("取消支付单成功，支付单ID：{}", paymentOrderId.getValue());
        return true;
    }

    @Override
    public boolean refundPayment(Id paymentOrderId, Price refundAmount, String refundReason) {
        LOGGER.info("退款操作，支付单ID：{}，退款金额：{}，退款原因：{}", 
                paymentOrderId.getValue(), refundAmount.getAmount(), refundReason);
        PaymentOrderDTO paymentOrder = PAYMENT_ORDER_MAP.get(paymentOrderId.getValue());
        if (paymentOrder == null) {
            LOGGER.warn("支付单不存在，支付单ID：{}", paymentOrderId.getValue());
            return false;
        }

        // 检查支付单状态
        if (!PAYMENT_STATUS_PAID.equals(paymentOrder.getPaymentStatus())) {
            LOGGER.warn("支付单状态错误，支付单ID：{}，当前状态：{}", 
                    paymentOrderId.getValue(), paymentOrder.getPaymentStatus());
            return false;
        }

        // 检查退款状态
        if (!REFUND_STATUS_NOT_REFUNDED.equals(paymentOrder.getRefundStatus())) {
            LOGGER.warn("退款状态错误，支付单ID：{}，当前退款状态：{}", 
                    paymentOrderId.getValue(), paymentOrder.getRefundStatus());
            return false;
        }

        // 检查退款金额
        if (refundAmount.getAmount() > paymentOrder.getAmount().longValue()) {
            LOGGER.warn("退款金额超过支付金额，支付单ID：{}，支付金额：{}，退款金额：{}", 
                    paymentOrderId.getValue(), paymentOrder.getAmount().longValue(), refundAmount.getAmount());
            return false;
        }

        // 模拟退款操作
        paymentOrder.setRefundStatus(REFUND_STATUS_REFUNDED);
        paymentOrder.setRefundAmount(new BigDecimal(refundAmount.getAmount()));
        paymentOrder.setRefundReason(refundReason);
        paymentOrder.setRefundTime(new Date());
        paymentOrder.setPaymentStatus(PAYMENT_STATUS_REFUNDED);
        PAYMENT_ORDER_MAP.put(paymentOrderId.getValue(), paymentOrder);

        LOGGER.info("退款操作成功，支付单ID：{}", paymentOrderId.getValue());
        return true;
    }

    @Override
    public String getPaymentStatus(Id paymentOrderId) {
        LOGGER.info("查询支付单状态，支付单ID：{}", paymentOrderId.getValue());
        PaymentOrderDTO paymentOrder = PAYMENT_ORDER_MAP.get(paymentOrderId.getValue());
        if (paymentOrder == null) {
            LOGGER.warn("支付单不存在，支付单ID：{}", paymentOrderId.getValue());
            return null;
        }

        // 检查支付单是否过期
        if (PAYMENT_STATUS_CREATED.equals(paymentOrder.getPaymentStatus()) && new Date().after(paymentOrder.getExpireTime())) {
            paymentOrder.setPaymentStatus(PAYMENT_STATUS_EXPIRED);
            PAYMENT_ORDER_MAP.put(paymentOrderId.getValue(), paymentOrder);
        }

        return paymentOrder.getPaymentStatus();
    }

    /**
     * 根据支付方式获取支付渠道
     */
    private String getPaymentChannel(String paymentMethod) {
        switch (paymentMethod) {
            case "ALIPAY":
                return "ALIPAY_CHANNEL";
            case "WECHAT":
                return "WECHAT_CHANNEL";
            case "BANK_CARD":
                return "BANK_CARD_CHANNEL";
            default:
                return "OTHER_CHANNEL";
        }
    }

    /**
     * 生成交易流水号
     */
    private String generateTransactionNo() {
        return "TXN" + System.currentTimeMillis() + (int) (Math.random() * 10000);
    }
}
