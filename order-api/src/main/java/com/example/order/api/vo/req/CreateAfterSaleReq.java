package com.example.order.api.vo.req;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建售后请求VO
 */
public class CreateAfterSaleReq {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "商品项列表不能为空")
    @Size(min = 1, message = "至少需要一个商品项")
    private List<AfterSaleItemReq> afterSaleItems;

    @NotEmpty(message = "售后类型不能为空")
    private String afterSaleType;

    private String reason;

    private String description;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<AfterSaleItemReq> getAfterSaleItems() {
        return afterSaleItems;
    }

    public void setAfterSaleItems(List<AfterSaleItemReq> afterSaleItems) {
        this.afterSaleItems = afterSaleItems;
    }

    public String getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(String afterSaleType) {
        this.afterSaleType = afterSaleType;
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
}