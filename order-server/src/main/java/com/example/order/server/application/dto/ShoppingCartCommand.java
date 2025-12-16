package com.example.order.server.application.dto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 购物车命令
 */
public class ShoppingCartCommand {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    @Valid
    private ItemCommand item;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ItemCommand getItem() {
        return item;
    }

    public void setItem(ItemCommand item) {
        this.item = item;
    }

    /**
     * 购物车项命令
     */
    public static class ItemCommand {

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
    }
}
