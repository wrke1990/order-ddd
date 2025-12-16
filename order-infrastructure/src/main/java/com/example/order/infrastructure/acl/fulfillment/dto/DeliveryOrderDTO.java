package com.example.order.infrastructure.acl.fulfillment.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发货单DTO
 * 封装外部履约系统的发货单数据
 */
public class DeliveryOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 发货单ID
    private String deliveryOrderId;

    // 订单ID
    private Long orderId;

    // 用户ID
    private Long userId;

    // 仓库ID
    private Long warehouseId;

    // 仓库名称
    private String warehouseName;

    // 物流公司
    private String logisticsCompany;

    // 物流单号
    private String logisticsNo;

    // 发货单状态
    private String deliveryStatus;

    // 发货商品列表
    private List<DeliveryItemDTO> items;

    // 创建时间
    private Date createTime;

    // 发货时间
    private Date deliveryTime;

    // 完成时间
    private Date completeTime;

    // 备注
    private String remark;

    // 获取发货单ID
    public String getDeliveryOrderId() {
        return deliveryOrderId;
    }

    // 设置发货单ID
    public void setDeliveryOrderId(String deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    // 获取订单ID
    public Long getOrderId() {
        return orderId;
    }

    // 设置订单ID
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // 获取用户ID
    public Long getUserId() {
        return userId;
    }

    // 设置用户ID
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // 获取仓库ID
    public Long getWarehouseId() {
        return warehouseId;
    }

    // 设置仓库ID
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    // 获取仓库名称
    public String getWarehouseName() {
        return warehouseName;
    }

    // 设置仓库名称
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    // 获取物流公司
    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    // 设置物流公司
    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    // 获取物流单号
    public String getLogisticsNo() {
        return logisticsNo;
    }

    // 设置物流单号
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    // 获取发货单状态
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    // 设置发货单状态
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    // 获取发货商品列表
    public List<DeliveryItemDTO> getItems() {
        return items;
    }

    // 设置发货商品列表
    public void setItems(List<DeliveryItemDTO> items) {
        this.items = items;
    }

    // 获取创建时间
    public Date getCreateTime() {
        return createTime;
    }

    // 设置创建时间
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 获取发货时间
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    // 设置发货时间
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    // 获取完成时间
    public Date getCompleteTime() {
        return completeTime;
    }

    // 设置完成时间
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    // 获取备注
    public String getRemark() {
        return remark;
    }

    // 设置备注
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
