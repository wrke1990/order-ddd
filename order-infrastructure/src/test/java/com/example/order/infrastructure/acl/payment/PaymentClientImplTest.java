package com.example.order.infrastructure.acl.payment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.payment.dto.PaymentOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 支付系统客户端实现测试类
 */
class PaymentClientImplTest {

    private PaymentClient paymentClient;

    private Id userId;
    private Price amount;
    private String orderNo;
    private String paymentMethod;

    @BeforeEach
    void setUp() {
        // 手动创建测试对象
        paymentClient = new PaymentClientImpl();
        userId = Id.of(1001L);
        amount = new Price(100L, "CNY");
        orderNo = "ORDER001";
        paymentMethod = "ALIPAY";
    }

    @Test
    void testCreatePaymentOrder() {
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.createPaymentOrder(orderNo, userId, amount, paymentMethod);
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals(orderNo, paymentOrderOptional.get().getOrderNo());
        assertEquals("CREATED", paymentOrderOptional.get().getPaymentStatus());
    }

    @Test
    void testCreatePaymentOrderDuplicateOrderNo() {
        // 先创建一个支付单
        paymentClient.createPaymentOrder(orderNo, userId, amount, paymentMethod);

        // 再次使用相同订单号创建
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.createPaymentOrder(orderNo, userId, amount, paymentMethod);
        assertFalse(paymentOrderOptional.isPresent());
    }

    @Test
    void testGetPaymentOrderById() {
        // 先创建一个支付单
        Optional<PaymentOrderDTO> createdOrderOptional = paymentClient.createPaymentOrder("ORDER002", userId, amount, paymentMethod);
        long paymentOrderId = createdOrderOptional.get().getPaymentOrderId();

        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderById(Id.of(paymentOrderId));
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals(paymentOrderId, paymentOrderOptional.get().getPaymentOrderId());
    }

    @Test
    void testGetPaymentOrderByIdNotFound() {
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderById(Id.of(999L));
        assertFalse(paymentOrderOptional.isPresent());
    }

    @Test
    void testGetPaymentOrderByOrderNo() {
        // 先创建一个支付单
        paymentClient.createPaymentOrder("ORDER003", userId, amount, paymentMethod);

        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderByOrderNo("ORDER003");
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals("ORDER003", paymentOrderOptional.get().getOrderNo());
    }

    @Test
    void testGetPaymentOrderByOrderNoNotFound() {
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderByOrderNo("NOT_EXIST_ORDER");
        assertFalse(paymentOrderOptional.isPresent());
    }

    @Test
    void testConfirmPayment() {
        // 先创建一个支付单
        Optional<PaymentOrderDTO> createdOrderOptional = paymentClient.createPaymentOrder("ORDER004", userId, amount, paymentMethod);
        long paymentOrderId = createdOrderOptional.get().getPaymentOrderId();

        boolean confirmed = paymentClient.confirmPayment(Id.of(paymentOrderId));
        assertTrue(confirmed);

        // 验证支付状态已更新
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderById(Id.of(paymentOrderId));
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals("PAID", paymentOrderOptional.get().getPaymentStatus());
    }

    @Test
    void testCancelPayment() {
        // 先创建一个支付单
        Optional<PaymentOrderDTO> createdOrderOptional = paymentClient.createPaymentOrder("ORDER005", userId, amount, paymentMethod);
        long paymentOrderId = createdOrderOptional.get().getPaymentOrderId();

        boolean cancelled = paymentClient.cancelPayment(Id.of(paymentOrderId));
        assertTrue(cancelled);

        // 验证支付状态已更新
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderById(Id.of(paymentOrderId));
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals("CANCELLED", paymentOrderOptional.get().getPaymentStatus());
    }

    @Test
    void testRefundPayment() {
        // 先创建并确认支付单
        Optional<PaymentOrderDTO> createdOrderOptional = paymentClient.createPaymentOrder("ORDER006", userId, amount, paymentMethod);
        long paymentOrderId = createdOrderOptional.get().getPaymentOrderId();
        paymentClient.confirmPayment(Id.of(paymentOrderId));

        // 退款
        Price refundAmount = new Price(100L, "CNY");
        boolean refunded = paymentClient.refundPayment(Id.of(paymentOrderId), refundAmount, "用户退款");
        assertTrue(refunded);

        // 验证支付状态已更新
        Optional<PaymentOrderDTO> paymentOrderOptional = paymentClient.getPaymentOrderById(Id.of(paymentOrderId));
        assertTrue(paymentOrderOptional.isPresent());
        assertEquals("REFUNDED", paymentOrderOptional.get().getPaymentStatus());
    }

    @Test
    void testGetPaymentStatus() {
        // 先创建一个支付单
        Optional<PaymentOrderDTO> createdOrderOptional = paymentClient.createPaymentOrder("ORDER007", userId, amount, paymentMethod);
        long paymentOrderId = createdOrderOptional.get().getPaymentOrderId();

        String status = paymentClient.getPaymentStatus(Id.of(paymentOrderId));
        assertEquals("CREATED", status);

        // 确认支付后再次查询状态
        paymentClient.confirmPayment(Id.of(paymentOrderId));
        status = paymentClient.getPaymentStatus(Id.of(paymentOrderId));
        assertEquals("PAID", status);
    }
}
