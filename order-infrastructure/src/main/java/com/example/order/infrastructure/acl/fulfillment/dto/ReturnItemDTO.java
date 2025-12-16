package com.example.order.infrastructure.acl.fulfillment.dto;

import java.io.Serializable;

/**
 * 退货商品项DTO
 * 封装外部履约系统的退货商品项数据
 */
public class ReturnItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 商品ID
    private Long productId;

    // 商品名称
    private String productName;

    // 退货数量
    private Integer quantity;

    // 商品单价
    private Long price;

    // 商品SKU
    private String skuCode;

    // 商品图片
    private String productImage;

    // 退货原因
    private String returnReason;

    // 退款金额
    private Long refundAmount;

    // 获取商品ID
    public Long getProductId() {
        return productId;
    }

    // 设置商品ID
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // 获取商品名称
    public String getProductName() {
        return productName;
    }

    // 设置商品名称
    public void setProductName(String productName) {
        this.productName = productName;
    }

    // 获取退货数量
    public Integer getQuantity() {
        return quantity;
    }

    // 设置退货数量
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // 获取商品单价
    public Long getPrice() {
        return price;
    }

    // 设置商品单价
    public void setPrice(Long price) {
        this.price = price;
    }

    // 获取商品SKU
    public String getSkuCode() {
        return skuCode;
    }

    // 设置商品SKU
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    // 获取商品图片
    public String getProductImage() {
        return productImage;
    }

    // 设置商品图片
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    // 获取退货原因
    public String getReturnReason() {
        return returnReason;
    }

    // 设置退货原因
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    // 获取退款金额
    public Long getRefundAmount() {
        return refundAmount;
    }

    // 设置退款金额
    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }
}
