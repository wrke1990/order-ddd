package com.example.order.domain.model.entity;

import com.example.order.domain.model.vo.Price;

import java.io.Serializable;

/**
 * 售后商品项实体
 */
public class AfterSaleItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long afterSaleId;
    private String afterSaleNo;
    private final Long productId;
    private final String productName;
    private final String productImage;
    private final Integer quantity;
    private final Price productPrice;
    private Price refundAmount;
    private Integer refundQuantity;
    private boolean returned;
    private String reason;
    
    /**
     * 创建售后商品项
     */
    public static AfterSaleItem create(Long productId, String productName, String productImage,
                                      Integer quantity, Price productPrice, Integer refundQuantity) {
        if (productId == null || productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("商品信息不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("购买数量必须大于0");
        }
        if (productPrice == null || productPrice.getAmount() <= 0) {
            throw new IllegalArgumentException("商品价格必须大于0");
        }
        if (refundQuantity == null || refundQuantity <= 0) {
            throw new IllegalArgumentException("退款数量必须大于0");
        }
        if (refundQuantity > quantity) {
            throw new IllegalArgumentException("退款数量不能超过购买数量");
        }
        
        AfterSaleItem item = new AfterSaleItem(productId, productName, productImage, quantity, productPrice, refundQuantity);
        // 计算默认退款金额
        long defaultRefundAmount = productPrice.getAmount() * refundQuantity;
        item.setRefundAmount(new Price(defaultRefundAmount, productPrice.getCurrency()));
        return item;
    }
    
    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private AfterSaleItem(Long productId, String productName, String productImage,
                         Integer quantity, Price productPrice, Integer refundQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.refundQuantity = refundQuantity;
        this.returned = false;
    }
    
    /**
     * 标记为已退货
     */
    public void markAsReturned() {
        this.returned = true;
    }
    
    /**
     * 检查是否可以退款
     */
    public boolean canRefund() {
        return !returned;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAfterSaleId() {
        return afterSaleId;
    }
    
    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
    
    public String getAfterSaleNo() {
        return afterSaleNo;
    }
    
    public void setAfterSaleNo(String afterSaleNo) {
        this.afterSaleNo = afterSaleNo;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getProductImage() {
        return productImage;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public Price getProductPrice() {
        return productPrice;
    }
    
    public Price getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(Price refundAmount) {
        if (refundAmount == null) {
            throw new IllegalArgumentException("退款金额不能为空");
        }
        if (refundAmount.getAmount() < 0) {
            throw new IllegalArgumentException("退款金额不能小于0");
        }
        this.refundAmount = refundAmount;
    }
    
    public Integer getRefundQuantity() {
        return refundQuantity;
    }
    
    public void setRefundQuantity(Integer refundQuantity) {
        if (refundQuantity == null || refundQuantity <= 0) {
            throw new IllegalArgumentException("退款数量必须大于0");
        }
        if (refundQuantity > quantity) {
            throw new IllegalArgumentException("退款数量不能超过购买数量");
        }
        this.refundQuantity = refundQuantity;
    }
    
    public boolean isReturned() {
        return returned;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Override
    public String toString() {
        return "AfterSaleItem{" +
                "id=" + id +
                ", afterSaleNo='" + afterSaleNo + "'" +
                ", productId=" + productId +
                ", productName='" + productName + "'" +
                ", quantity=" + quantity +
                ", refundQuantity=" + refundQuantity +
                ", refundAmount=" + refundAmount +
                ", returned=" + returned +
                '}';
    }
}