package com.example.order.domain.model.aggregate;

import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Order聚合根测试
 */
public class OrderTest {

    @Test
    public void testCreateOrder() {
        // 准备测试数据
        Id userId = Id.of(1L);
        List<OrderItem> orderItems = new ArrayList<>();
        String orderNo = "ORDER-001";
        Address address = new Address(Id.of(1L), "张三", "13800138000", "北京市", "北京市", "朝阳区", "建国路1号", "100000");
        PaymentMethod paymentMethod = new PaymentMethod(Id.of(1L), PaymentMethod.MethodType.ALIPAY, "支付宝");

        OrderItem orderItem1 = OrderItem.create(Id.of(1L), "Product 1", 2, new Price(100L, "CNY"));
        orderItems.add(orderItem1);

        OrderItem orderItem2 = OrderItem.create(Id.of(2L), "Product 2", 1, new Price(200L, "CNY"));
        orderItems.add(orderItem2);

        // 创建订单
        Order order = Order.create(userId, orderItems, address, paymentMethod);

        // 手动设置订单号用于测试
        order.setOrderNo(orderNo);

        // 验证订单属性
        Assertions.assertNotNull(order);
        Assertions.assertEquals(userId, order.getUserId());
        Assertions.assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
        Assertions.assertEquals(orderNo, order.getOrderNo());
        Assertions.assertEquals(2, order.getOrderItems().size());
        Assertions.assertEquals(400L, order.getTotalAmount().getAmount());
        Assertions.assertEquals("CNY", order.getTotalAmount().getCurrency());

        // 验证订单项
        OrderItem item1 = order.getOrderItems().get(0);
        Assertions.assertEquals(Id.of(1L), item1.getProductId());
        Assertions.assertEquals(200L, item1.getTotalPrice().getAmount());

        OrderItem item2 = order.getOrderItems().get(1);
        Assertions.assertEquals(Id.of(2L), item2.getProductId());
        Assertions.assertEquals(200L, item2.getTotalPrice().getAmount());
    }

    @Test
    public void testPayOrder() {
        // 准备测试数据
        Order order = createTestOrder();

        // 支付订单
        order.pay("PAY-001");

        // 验证支付状态
        Assertions.assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    public void testPayOrderWithInvalidStatus() {
        // 准备测试数据
        Order order = createTestOrder();
        order.pay("PAY-001"); // 先支付

        // 尝试再次支付，应该抛出异常
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.pay("PAY-002"));
    }

    @Test
    public void testShipOrder() {
        // 准备测试数据
        Order order = createTestOrder();
        order.pay("PAY-001"); // 先支付

        // 发货订单
        order.ship("顺丰速运", "SF1234567890");

        // 验证发货状态
        Assertions.assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    public void testShipOrderWithInvalidStatus() {
        // 准备测试数据
        Order order = createTestOrder();

        // 未支付的订单尝试发货，应该抛出异常
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.ship("顺丰速运", "SF1234567890"));
    }

    @Test
    public void testCompleteOrder() {
        // 准备测试数据
        Order order = createTestOrder();
        order.pay("PAY-001"); // 先支付
        order.ship("顺丰速运", "SF1234567890"); // 再发货

        // 完成订单
        order.complete();

        // 验证完成状态
        Assertions.assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    public void testCompleteOrderWithInvalidStatus() {
        // 准备测试数据
        Order order = createTestOrder();

        // 未支付的订单尝试完成，应该抛出异常
        Assertions.assertThrows(IllegalArgumentException.class, order::complete);
    }

    @Test
    public void testCancelOrder() {
        // 准备测试数据
        Order order = createTestOrder();

        // 取消订单
        order.cancel();

        // 验证取消状态
        Assertions.assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    public void testCancelOrderWithInvalidStatus() {
        // 准备测试数据
        Order order = createTestOrder();
        order.pay("PAY-001"); // 先支付

        // 已支付的订单尝试取消，应该抛出异常
        Assertions.assertThrows(IllegalArgumentException.class, order::cancel);
    }

    /**
     * 创建测试订单
     */
    private Order createTestOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = OrderItem.create(Id.of(1L), "Product 1", 2, Price.ofCNY(50L));
        orderItems.add(orderItem);
        Address address = new Address(Id.of(1L), "张三", "13800138000", "北京市", "北京市", "朝阳区", "建国路1号", "100000");
        PaymentMethod paymentMethod = new PaymentMethod(Id.of(1L), PaymentMethod.MethodType.ALIPAY, "支付宝");
        Order order = Order.create(Id.of(1L), orderItems, address, paymentMethod);
        order.setId(Id.of(1L)); // 设置订单id
        order.setOrderNo("ORDER-001"); // 设置订单号
        return order;
    }
}
