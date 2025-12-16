package com.example.order.api.vo.req;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 购物车请求VO
 */
public class ShoppingCartReq {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    @Valid
    private ItemReq item;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ItemReq getItem() {
        return item;
    }

    public void setItem(ItemReq item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "ShoppingCartReq{" +
                "userId=" + userId +
                ", item=" + item +
                '}';
    }

    /**
     * 购物车项请求VO
     */
    public static class ItemReq {

        @NotNull(message = "商品ID不能为空")
        @Min(value = 1, message = "商品ID必须大于0")
        private Long productId;

        @NotEmpty(message = "商品名称不能为空")
        private String productName;

        private String productImage;

        @NotNull(message = "商品价格不能为空")
        @Min(value = 0, message = "商品价格必须大于等于0")
        private Long price;

        private String currency = "CNY";

        @NotNull(message = "商品数量不能为空")
        @Min(value = 1, message = "商品数量必须大于0")
        private Integer quantity;

        private Boolean selected = true;

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

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            return "ItemReq{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", productImage='" + productImage + '\'' +
                    ", price=" + price +
                    ", currency='" + currency + '\'' +
                    ", quantity=" + quantity +
                    ", selected=" + selected +
                    '}';
        }
    }
}
