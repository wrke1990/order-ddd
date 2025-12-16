package com.example.order.api.vo.resp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车响应VO
 */
public class ShoppingCartResp {

    private Long id;
    private Long userId;
    private List<ShoppingCartItemResp> items;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ShoppingCartItemResp> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItemResp> items) {
        this.items = items;
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

    @Override
    public String toString() {
        return "ShoppingCartResp{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    /**
     * 购物车商品项响应VO
     */
    public static class ShoppingCartItemResp {
        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer price;
        private Integer quantity;
        private String currency;
        private Long skuId;
        private String skuName;
        private String skuProperties;
        private boolean available;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuProperties() {
            return skuProperties;
        }

        public void setSkuProperties(String skuProperties) {
            this.skuProperties = skuProperties;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
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

        @Override
        public String toString() {
            return "ShoppingCartItemResp{" +
                    "id=" + id +
                    ", productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", productImage='" + productImage + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", currency='" + currency + '\'' +
                    ", skuId=" + skuId +
                    ", skuName='" + skuName + '\'' +
                    ", skuProperties='" + skuProperties + '\'' +
                    ", available=" + available +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }
    }
}
