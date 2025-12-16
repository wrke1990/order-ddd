package com.example.order.infrastructure.acl.fulfillment.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退货单DTO
 * 封装外部履约系统的退货单数据
 */
public class ReturnOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 退货单ID
    private String returnOrderId;

    // 订单ID
    private Long orderId;

    // 用户ID
    private Long userId;

    // 物流公司
    private String logisticsCompany;

    // 物流单号
    private String logisticsNo;

    // 退货单状态
    private String returnStatus;

    // 退货商品列表
    private List<ReturnItemDTO> items;

    // 创建时间
    private Date createTime;

    // 退货时间
    private Date returnTime;

    // 收货时间
    private Date receiptTime;

    // 退款金额
    private Long refundAmount;

    // 备注
    private String remark;

    // 退货原因
    private String returnReason;

    // 获取退货单ID
    public String getReturnOrderId() {
        return returnOrderId;
    }

    // 设置退货单ID
    public void setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
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

    // 获取退货单状态
    public String getReturnStatus() {
        return returnStatus;
    }

    // 设置退货单状态
    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    // 获取退货商品列表
    public List<ReturnItemDTO> getItems() {
        return items;
    }

    // 设置退货商品列表
    public void setItems(List<ReturnItemDTO> items) {
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

    // 获取退货时间
    public Date getReturnTime() {
        return returnTime;
    }

    // 设置退货时间
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    // 获取收货时间
    public Date getReceiptTime() {
        return receiptTime;
    }

    // 设置收货时间
    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    // 获取退款金额
    public Long getRefundAmount() {
        return refundAmount;
    }

    // 设置退款金额
    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    // 获取备注
    public String getRemark() {
        return remark;
    }

    // 设置备注
    public void setRemark(String remark) {
        this.remark = remark;
    }

    // 获取退货原因
    public String getReturnReason() {
        return returnReason;
    }

    // 设置退货原因
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}
