package com.example.order.server.application.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建售后订单命令
 */
public class CreateAfterSaleOrderCommand {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @NotEmpty(message = "售后商品项不能为空")
    private List<AfterSaleItemRequest> afterSaleItems;

    @NotEmpty(message = "售后类型不能为空")
    private String afterSaleType;

    private String reason;

    private String description;

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

    public List<AfterSaleItemRequest> getAfterSaleItems() {
        return afterSaleItems;
    }

    public void setAfterSaleItems(List<AfterSaleItemRequest> afterSaleItems) {
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
