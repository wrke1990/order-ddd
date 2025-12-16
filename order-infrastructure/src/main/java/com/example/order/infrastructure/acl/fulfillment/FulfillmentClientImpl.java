package com.example.order.infrastructure.acl.fulfillment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.LogisticsInfo;
import com.example.order.infrastructure.acl.fulfillment.dto.DeliveryOrderDTO;
import com.example.order.infrastructure.acl.fulfillment.dto.ReturnOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 履约系统客户端实现类
 * 模拟与外部履约系统的交互
 */
@Component
public class FulfillmentClientImpl implements FulfillmentClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentClientImpl.class);

    // 模拟发货单数据
    private static final ConcurrentMap<String, DeliveryOrderDTO> DELIVERY_ORDER_MAP = new ConcurrentHashMap<>();
    // 模拟退货单数据
    private static final ConcurrentMap<String, ReturnOrderDTO> RETURN_ORDER_MAP = new ConcurrentHashMap<>();
    // 模拟物流信息数据
    private static final ConcurrentMap<String, LogisticsInfo> LOGISTICS_INFO_MAP = new ConcurrentHashMap<>();

    // 发货单ID生成器
    private static final AtomicInteger DELIVERY_ORDER_ID_GENERATOR = new AtomicInteger(1);
    // 退货单ID生成器
    private static final AtomicInteger RETURN_ORDER_ID_GENERATOR = new AtomicInteger(1);

    // 物流状态
    private static final String LOGISTICS_STATUS_CREATED = "CREATED";
    private static final String LOGISTICS_STATUS_IN_TRANSIT = "IN_TRANSIT";
    private static final String LOGISTICS_STATUS_DELIVERED = "DELIVERED";
    private static final String LOGISTICS_STATUS_RETURNED = "RETURNED";

    // 发货单状态
    private static final String DELIVERY_STATUS_CREATED = "CREATED";
    private static final String DELIVERY_STATUS_DELIVERED = "DELIVERED";
    private static final String DELIVERY_STATUS_COMPLETED = "COMPLETED";
    private static final String DELIVERY_STATUS_CANCELLED = "CANCELLED";

    // 退货单状态
    private static final String RETURN_STATUS_CREATED = "CREATED";
    private static final String RETURN_STATUS_RETURNED = "RETURNED";
    private static final String RETURN_STATUS_RECEIVED = "RECEIVED";
    private static final String RETURN_STATUS_COMPLETED = "COMPLETED";
    private static final String RETURN_STATUS_CANCELLED = "CANCELLED";

    // 日期时间格式化器
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String createDeliveryOrder(Id orderId, Id userId, LogisticsInfo logisticsInfo) {
        LOGGER.info("Creating delivery order for orderId: {}, userId: {}", orderId, userId);

        String deliveryOrderId = "DO" + System.currentTimeMillis() + "-" + DELIVERY_ORDER_ID_GENERATOR.incrementAndGet();
        DeliveryOrderDTO deliveryOrder = new DeliveryOrderDTO();
        deliveryOrder.setDeliveryOrderId(deliveryOrderId);
        deliveryOrder.setOrderId(orderId.getValue());
        deliveryOrder.setUserId(userId.getValue());
        deliveryOrder.setDeliveryStatus(DELIVERY_STATUS_CREATED);
        deliveryOrder.setCreateTime(new Date());
        deliveryOrder.setLogisticsNo(logisticsInfo.getTrackingNumber());

        DELIVERY_ORDER_MAP.put(deliveryOrderId, deliveryOrder);

        // 保存物流信息
        LOGISTICS_INFO_MAP.put(logisticsInfo.getTrackingNumber(), logisticsInfo);

        LOGGER.info("Created delivery order: {}", deliveryOrderId);
        return deliveryOrderId;
    }

    @Override
    public boolean confirmDelivery(String deliveryOrderId) {
        LOGGER.info("Confirming delivery for deliveryOrderId: {}", deliveryOrderId);

        DeliveryOrderDTO deliveryOrder = DELIVERY_ORDER_MAP.get(deliveryOrderId);
        if (deliveryOrder == null) {
            LOGGER.error("Delivery order not found: {}", deliveryOrderId);
            return false;
        }

        if (!DELIVERY_STATUS_CREATED.equals(deliveryOrder.getDeliveryStatus())) {
            LOGGER.error("Delivery order status is not CREATED: {}", deliveryOrder.getDeliveryStatus());
            return false;
        }

        deliveryOrder.setDeliveryStatus(DELIVERY_STATUS_DELIVERED);
        deliveryOrder.setDeliveryTime(new Date());

        LOGGER.info("Confirmed delivery for deliveryOrderId: {}", deliveryOrderId);
        return true;
    }

    @Override
    public String createReturnOrder(Id orderId, Id userId, LogisticsInfo logisticsInfo) {
        LOGGER.info("Creating return order for orderId: {}, userId: {}", orderId, userId);

        String returnOrderId = "RO" + System.currentTimeMillis() + "-" + RETURN_ORDER_ID_GENERATOR.incrementAndGet();
        ReturnOrderDTO returnOrder = new ReturnOrderDTO();
        returnOrder.setReturnOrderId(returnOrderId);
        returnOrder.setOrderId(orderId.getValue());
        returnOrder.setUserId(userId.getValue());
        returnOrder.setReturnStatus(RETURN_STATUS_CREATED);
        returnOrder.setCreateTime(new Date());
        returnOrder.setLogisticsNo(logisticsInfo.getTrackingNumber());

        RETURN_ORDER_MAP.put(returnOrderId, returnOrder);

        // 保存物流信息
        LOGISTICS_INFO_MAP.put(logisticsInfo.getTrackingNumber(), logisticsInfo);

        LOGGER.info("Created return order: {}", returnOrderId);
        return returnOrderId;
    }

    @Override
    public boolean confirmReceipt(String returnOrderId) {
        LOGGER.info("Confirming receipt for returnOrderId: {}", returnOrderId);

        ReturnOrderDTO returnOrder = RETURN_ORDER_MAP.get(returnOrderId);
        if (returnOrder == null) {
            LOGGER.error("Return order not found: {}", returnOrderId);
            return false;
        }

        if (!RETURN_STATUS_CREATED.equals(returnOrder.getReturnStatus()) && !RETURN_STATUS_RETURNED.equals(returnOrder.getReturnStatus())) {
            LOGGER.error("Return order status is not CREATED or RETURNED: {}", returnOrder.getReturnStatus());
            return false;
        }

        returnOrder.setReturnStatus(RETURN_STATUS_RECEIVED);
        returnOrder.setReceiptTime(new Date());

        LOGGER.info("Confirmed receipt for returnOrderId: {}", returnOrderId);
        return true;
    }

    @Override
    public LogisticsInfo getLogisticsInfo(String logisticsNo) {
        LOGGER.info("Getting logistics info for logisticsNo: {}", logisticsNo);

        LogisticsInfo logisticsInfo = LOGISTICS_INFO_MAP.get(logisticsNo);
        if (logisticsInfo == null) {
            LOGGER.error("Logistics info not found: {}", logisticsNo);
            return null;
        }

        LOGGER.info("Got logistics info for logisticsNo: {}", logisticsNo);
        return logisticsInfo;
    }

    @Override
    public boolean updateLogisticsTrack(String logisticsNo, String logisticsStatus, String logisticsTime, String logisticsDescription) {
        LOGGER.info("Updating logistics track for logisticsNo: {}, status: {}, time: {}, description: {}", logisticsNo, logisticsStatus, logisticsTime, logisticsDescription);

        LogisticsInfo logisticsInfo = LOGISTICS_INFO_MAP.get(logisticsNo);
        if (logisticsInfo == null) {
            LOGGER.error("Logistics info not found for logisticsNo: {}", logisticsNo);
            return false;
        }

        // 解析时间字符串为LocalDateTime
        try {
            LocalDateTime time = LocalDateTime.parse(logisticsTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // 使用物流状态作为location参数，因为LogisticsInfo.addTrack方法需要location参数
            logisticsInfo.addTrack(time, logisticsStatus, logisticsDescription);
        } catch (Exception e) {
            LOGGER.error("Failed to parse logistics time: {}", logisticsTime, e);
            return false;
        }

        LOGGER.info("Updated logistics track for logisticsNo: {}", logisticsNo);
        return true;
    }

    @Override
    public boolean cancelDeliveryOrder(String deliveryOrderId) {
        LOGGER.info("Cancelling delivery order for deliveryOrderId: {}", deliveryOrderId);

        DeliveryOrderDTO deliveryOrder = DELIVERY_ORDER_MAP.get(deliveryOrderId);
        if (deliveryOrder == null) {
            LOGGER.error("Delivery order not found: {}", deliveryOrderId);
            return false;
        }

        if (!DELIVERY_STATUS_CREATED.equals(deliveryOrder.getDeliveryStatus())) {
            LOGGER.error("Delivery order status is not CREATED: {}", deliveryOrder.getDeliveryStatus());
            return false;
        }

        deliveryOrder.setDeliveryStatus(DELIVERY_STATUS_CANCELLED);
        deliveryOrder.setCompleteTime(new Date());

        LOGGER.info("Cancelled delivery order: {}", deliveryOrderId);
        return true;
    }

    @Override
    public boolean cancelReturnOrder(String returnOrderId) {
        LOGGER.info("Cancelling return order for returnOrderId: {}", returnOrderId);

        ReturnOrderDTO returnOrder = RETURN_ORDER_MAP.get(returnOrderId);
        if (returnOrder == null) {
            LOGGER.error("Return order not found: {}", returnOrderId);
            return false;
        }

        if (!RETURN_STATUS_CREATED.equals(returnOrder.getReturnStatus())) {
            LOGGER.error("Return order status is not CREATED: {}", returnOrder.getReturnStatus());
            return false;
        }

        returnOrder.setReturnStatus(RETURN_STATUS_CANCELLED);
        // ReturnOrderDTO没有completeTime字段，使用returnTime代替
        returnOrder.setReturnTime(new Date());

        LOGGER.info("Cancelled return order: {}", returnOrderId);
        return true;
    }

    @Override
    public String getDeliveryOrderStatus(String deliveryOrderId) {
        LOGGER.info("Getting delivery order status for deliveryOrderId: {}", deliveryOrderId);

        DeliveryOrderDTO deliveryOrder = DELIVERY_ORDER_MAP.get(deliveryOrderId);
        if (deliveryOrder == null) {
            LOGGER.error("Delivery order not found: {}", deliveryOrderId);
            return null;
        }

        LOGGER.info("Got delivery order status: {} for deliveryOrderId: {}", deliveryOrder.getDeliveryStatus(), deliveryOrderId);
        return deliveryOrder.getDeliveryStatus();
    }

    @Override
    public String getReturnOrderStatus(String returnOrderId) {
        LOGGER.info("Getting return order status for returnOrderId: {}", returnOrderId);

        ReturnOrderDTO returnOrder = RETURN_ORDER_MAP.get(returnOrderId);
        if (returnOrder == null) {
            LOGGER.error("Return order not found: {}", returnOrderId);
            return null;
        }

        LOGGER.info("Got return order status: {} for returnOrderId: {}", returnOrder.getReturnStatus(), returnOrderId);
        return returnOrder.getReturnStatus();
    }
}