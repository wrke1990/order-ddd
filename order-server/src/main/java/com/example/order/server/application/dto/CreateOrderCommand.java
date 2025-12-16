package com.example.order.server.application.dto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单命令
 */
public class CreateOrderCommand {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    @NotNull(message = "地址ID不能为空")
    @Min(value = 1, message = "地址ID必须大于0")
    private Long addressId;

    @NotNull(message = "支付方式ID不能为空")
    @Min(value = 1, message = "支付方式ID必须大于0")
    private Long paymentMethodId;

    // 优惠券ID可以为空
    private Long couponId;

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemCommand> items;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public List<OrderItemCommand> getItems() {
        return items;
    }

    public void setItems(List<OrderItemCommand> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CreateOrderCommand{" +
                "userId=" + userId +
                ", addressId=" + addressId +
                ", paymentMethodId=" + paymentMethodId +
                ", couponId=" + couponId +
                ", items=" + items +
                '}';
    }

    public static class OrderItemCommand {

        @NotNull(message = "商品ID不能为空")
        @Min(value = 1, message = "商品ID必须大于0")
        private Long productId;

        @NotEmpty(message = "商品名称不能为空")
        private String productName;

        @NotNull(message = "商品数量不能为空")
        @Min(value = 1, message = "商品数量必须大于0")
        private Integer quantity;

        @NotNull(message = "商品价格不能为空")
        @Min(value = 0, message = "商品价格必须大于等于0")
        private Long price;

        private String currency = "CNY";

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

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
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

        @Override
        public String toString() {
            return "OrderItemRequest{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }
}
