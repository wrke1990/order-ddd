package com.example.order.server.application.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后订单响应DTO
 */
public class AfterSaleOrderResponse {

    private Long id;
    private String afterSaleNo;
    private String orderNo;
    private Long userId;
    private Long refundAmount;
    private String afterSaleType;
    private String status;
    private String reason;
    private String description;
    private String customerRemark;
    private String customerServiceId;
    private String reverseLogisticsNo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<AfterSaleItemResponse> afterSaleItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAfterSaleNo() {
        return afterSaleNo;
    }

    public void setAfterSaleNo(String afterSaleNo) {
        this.afterSaleNo = afterSaleNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<AfterSaleItemResponse> getAfterSaleItems() {
        return afterSaleItems;
    }

    public void setAfterSaleItems(List<AfterSaleItemResponse> afterSaleItems) {
        this.afterSaleItems = afterSaleItems;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(String afterSaleType) {
        this.afterSaleType = afterSaleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerRemark() {
        return customerRemark;
    }

    public void setCustomerRemark(String customerRemark) {
        this.customerRemark = customerRemark;
    }

    public String getCustomerServiceId() {
        return customerServiceId;
    }

    public void setCustomerServiceId(String customerServiceId) {
        this.customerServiceId = customerServiceId;
    }

    public String getReverseLogisticsNo() {
        return reverseLogisticsNo;
    }

    public void setReverseLogisticsNo(String reverseLogisticsNo) {
        this.reverseLogisticsNo = reverseLogisticsNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
