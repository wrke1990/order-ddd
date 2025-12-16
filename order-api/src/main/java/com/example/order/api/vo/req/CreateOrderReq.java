package com.example.order.api.vo.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求VO
 */
public class CreateOrderReq {

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

    @NotNull(message = "订单商品不能为空")
    private List<OrderItemReq> items;

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

    public List<OrderItemReq> getItems() {
        return items;
    }

    public void setItems(List<OrderItemReq> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CreateOrderReq{" +
                "userId=" + userId +
                ", addressId=" + addressId +
                ", paymentMethodId=" + paymentMethodId +
                ", couponId=" + couponId +
                ", items=" + items +
                '}';
    }

    public static class OrderItemReq {

        @NotNull(message = "商品ID不能为空")
        @Min(value = 1, message = "商品ID必须大于0")
        private Long productId;

        @NotNull(message = "商品名称不能为空")
        private String productName;

        @NotNull(message = "商品数量不能为空")
        @Min(value = 1, message = "商品数量必须大于0")
        private Integer quantity;

        @NotNull(message = "商品价格不能为空")
        @Min(value = 1, message = "商品价格必须大于0")
        private Long price;

        @NotNull(message = "货币类型不能为空")
        private String currency;

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
            return "OrderItemReq{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }
}
