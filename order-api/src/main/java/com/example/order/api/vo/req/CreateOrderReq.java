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


}
