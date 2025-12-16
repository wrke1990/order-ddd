package com.example.order.infrastructure.acl.fulfillment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.LogisticsInfo;

/**
 * 履约系统客户端接口
 * 定义与外部履约系统的交互方法
 */
public interface FulfillmentClient {

    /**
     * 创建发货单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param logisticsInfo 物流信息
     * @return 发货单ID
     */
    String createDeliveryOrder(Id orderId, Id userId, LogisticsInfo logisticsInfo);

    /**
     * 确认发货
     * @param deliveryOrderId 发货单ID
     * @return 是否成功
     */
    boolean confirmDelivery(String deliveryOrderId);

    /**
     * 创建退货单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param logisticsInfo 物流信息
     * @return 退货单ID
     */
    String createReturnOrder(Id orderId, Id userId, LogisticsInfo logisticsInfo);

    /**
     * 确认收货
     * @param returnOrderId 退货单ID
     * @return 是否成功
     */
    boolean confirmReceipt(String returnOrderId);

    /**
     * 获取物流信息
     * @param logisticsNo 物流单号
     * @return 物流信息
     */
    LogisticsInfo getLogisticsInfo(String logisticsNo);

    /**
     * 更新物流轨迹
     * @param logisticsNo 物流单号
     * @param logisticsStatus 物流状态
     * @param logisticsTime 物流时间
     * @param logisticsDescription 物流描述
     * @return 是否成功
     */
    boolean updateLogisticsTrack(String logisticsNo, String logisticsStatus, String logisticsTime, String logisticsDescription);

    /**
     * 取消发货单
     * @param deliveryOrderId 发货单ID
     * @return 是否成功
     */
    boolean cancelDeliveryOrder(String deliveryOrderId);

    /**
     * 取消退货单
     * @param returnOrderId 退货单ID
     * @return 是否成功
     */
    boolean cancelReturnOrder(String returnOrderId);

    /**
     * 获取发货单状态
     * @param deliveryOrderId 发货单ID
     * @return 发货单状态
     */
    String getDeliveryOrderStatus(String deliveryOrderId);

    /**
     * 获取退货单状态
     * @param returnOrderId 退货单ID
     * @return 退货单状态
     */
    String getReturnOrderStatus(String returnOrderId);
}