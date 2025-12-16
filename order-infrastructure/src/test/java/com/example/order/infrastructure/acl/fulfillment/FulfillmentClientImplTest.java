package com.example.order.infrastructure.acl.fulfillment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.LogisticsInfo;
import com.example.order.domain.model.vo.LogisticsInfo.LogisticsTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 物流系统客户端实现测试类
 */
class FulfillmentClientImplTest {

    private FulfillmentClient fulfillmentClient;

    private Id orderId;
    private Id userId;
    private LogisticsInfo logisticsInfo;
    private String logisticsNo;

    @BeforeEach
    void setUp() {
        // 手动创建测试对象
        fulfillmentClient = new FulfillmentClientImpl();
        orderId = Id.of(1001L);
        userId = Id.of(2001L);
        logisticsNo = "LOGISTICS" + System.currentTimeMillis();
        List<LogisticsTrack> tracks = new ArrayList<>();
        tracks.add(new LogisticsTrack(LocalDateTime.now(), "下单地点", "订单已创建"));
        logisticsInfo = new LogisticsInfo("顺丰速运", logisticsNo, tracks);
    }

    @Test
    void testCreateDeliveryOrder() {
        String deliveryOrderId = fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);
        assertNotNull(deliveryOrderId);
        assertTrue(deliveryOrderId.startsWith("DO"));
    }

    @Test
    void testConfirmDelivery() {
        // 先创建发货单
        String deliveryOrderId = fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);

        boolean confirmed = fulfillmentClient.confirmDelivery(deliveryOrderId);
        assertTrue(confirmed);

        // 验证发货单状态已更新
        String status = fulfillmentClient.getDeliveryOrderStatus(deliveryOrderId);
        assertEquals("DELIVERED", status);
    }

    @Test
    void testCreateReturnOrder() {
        String returnOrderId = fulfillmentClient.createReturnOrder(orderId, userId, logisticsInfo);
        assertNotNull(returnOrderId);
        assertTrue(returnOrderId.startsWith("RO"));
    }

    @Test
    void testConfirmReceipt() {
        // 先创建退货单
        String returnOrderId = fulfillmentClient.createReturnOrder(orderId, userId, logisticsInfo);

        boolean confirmed = fulfillmentClient.confirmReceipt(returnOrderId);
        assertTrue(confirmed);

        // 验证退货单状态已更新
        String status = fulfillmentClient.getReturnOrderStatus(returnOrderId);
        assertEquals("RECEIVED", status);
    }

    @Test
    void testGetLogisticsInfo() {
        // 先创建发货单，保存物流信息
        fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);

        LogisticsInfo retrievedLogisticsInfo = fulfillmentClient.getLogisticsInfo(logisticsNo);
        assertNotNull(retrievedLogisticsInfo);
        assertEquals(logisticsNo, retrievedLogisticsInfo.getTrackingNumber());
        assertEquals(1, retrievedLogisticsInfo.getTracks().size());
    }

    @Test
    void testGetLogisticsInfoNotFound() {
        LogisticsInfo retrievedLogisticsInfo = fulfillmentClient.getLogisticsInfo("NOT_EXIST_LOGISTICS_NO");
        assertNull(retrievedLogisticsInfo);
    }

    @Test
    void testUpdateLogisticsTrack() {
        // 先创建发货单，保存物流信息
        fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);

        String logisticsTime = "2023-12-01 10:00:00";
        boolean updated = fulfillmentClient.updateLogisticsTrack(logisticsNo, "IN_TRANSIT", logisticsTime, "包裹正在运输中");
        assertTrue(updated);

        // 验证物流轨迹已更新
        LogisticsInfo retrievedLogisticsInfo = fulfillmentClient.getLogisticsInfo(logisticsNo);
        assertNotNull(retrievedLogisticsInfo);
        assertTrue(retrievedLogisticsInfo.getTracks().stream()
                .anyMatch(track -> track.getDescription().equals("包裹正在运输中")));
    }

    @Test
    void testUpdateLogisticsTrackNotFound() {
        boolean updated = fulfillmentClient.updateLogisticsTrack("NOT_EXIST_LOGISTICS_NO", "IN_TRANSIT", "2023-12-01 10:00:00", "包裹正在运输中");
        assertFalse(updated);
    }

    @Test
    void testCancelDeliveryOrder() {
        // 先创建发货单
        String deliveryOrderId = fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);

        boolean cancelled = fulfillmentClient.cancelDeliveryOrder(deliveryOrderId);
        assertTrue(cancelled);

        // 验证发货单状态已更新
        String status = fulfillmentClient.getDeliveryOrderStatus(deliveryOrderId);
        assertEquals("CANCELLED", status);
    }

    @Test
    void testCancelReturnOrder() {
        // 先创建退货单
        String returnOrderId = fulfillmentClient.createReturnOrder(orderId, userId, logisticsInfo);

        boolean cancelled = fulfillmentClient.cancelReturnOrder(returnOrderId);
        assertTrue(cancelled);

        // 验证退货单状态已更新
        String status = fulfillmentClient.getReturnOrderStatus(returnOrderId);
        assertEquals("CANCELLED", status);
    }

    @Test
    void testGetDeliveryOrderStatus() {
        // 先创建发货单
        String deliveryOrderId = fulfillmentClient.createDeliveryOrder(orderId, userId, logisticsInfo);

        String status = fulfillmentClient.getDeliveryOrderStatus(deliveryOrderId);
        assertEquals("CREATED", status);
    }

    @Test
    void testGetDeliveryOrderStatusNotFound() {
        String status = fulfillmentClient.getDeliveryOrderStatus("NOT_EXIST_DELIVERY_ORDER_ID");
        assertNull(status);
    }

    @Test
    void testGetReturnOrderStatus() {
        // 先创建退货单
        String returnOrderId = fulfillmentClient.createReturnOrder(orderId, userId, logisticsInfo);

        String status = fulfillmentClient.getReturnOrderStatus(returnOrderId);
        assertEquals("CREATED", status);
    }

    @Test
    void testGetReturnOrderStatusNotFound() {
        String status = fulfillmentClient.getReturnOrderStatus("NOT_EXIST_RETURN_ORDER_ID");
        assertNull(status);
    }
}
