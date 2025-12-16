package com.example.order.infrastructure.acl.fulfillment.dto;

import java.io.Serializable;

/**
 * 发货商品项DTO
 * 封装外部履约系统的发货商品项数据
 */
public class DeliveryItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 商品ID
    private Long productId;

    // 商品名称
    private String productName;

    // 商品数量
    private Integer quantity;

    // 商品单价
    private Long price;

    // 商品SKU
    private String skuCode;

    // 商品图片
    private String productImage;

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

    // 获取商品数量
    public Integer getQuantity() {
        return quantity;
    }

    // 设置商品数量
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
}
