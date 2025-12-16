package com.example.order.domain.model.entity;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;

import java.io.Serializable;

/**
 * 订单项实体
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Id id;
    private String orderNo;
    private final Id productId;
    private final String productName;
    private final Integer quantity;
    private final Price price;
    private Price totalPrice;
    private boolean refunded;
    private Price refundAmount;
    private boolean returned;

    /**
     * 创建订单项
     * 订单号将在订单聚合根创建时统一设置
     */
    public static OrderItem create(Id productId, String productName, Integer quantity, Price price) {
        if (productId == null || productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("商品ID和商品名称不能为空");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("商品数量必须大于0");
        }
        if (price == null || price.getAmount() <= 0) {
            throw new IllegalArgumentException("商品价格必须大于0");
        }

        OrderItem orderItem = new OrderItem(productId, productName, quantity, price);
        orderItem.calculateTotalPrice();
        return orderItem;
    }

    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private OrderItem(Id productId, String productName, Integer quantity, Price price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.refunded = false;
        this.refundAmount = new Price(0L, price.getCurrency());
        this.returned = false;
    }

    /**
     * 申请退款
     */
    public void refund(Price amount) {
        if (amount == null || amount.getAmount() <= 0) {
            throw new IllegalArgumentException("退款金额必须大于0");
        }
        if (amount.getAmount() > this.totalPrice.getAmount()) {
            throw new IllegalArgumentException("退款金额不能超过商品小计金额");
        }
        if (!this.price.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException("货币类型不匹配");
        }

        this.refunded = true;
        this.refundAmount = amount;
    }

    /**
     * 标记为已退货
     */
    public void markAsReturned() {
        this.returned = true;
    }

    /**
     * 检查是否可以申请售后
     */
    public boolean canApplyAfterSale() {
        // 未退款且未退货的商品可以申请售后
        return !this.refunded && !this.returned;
    }

    /**
     * 计算订单项总价
     */
    public void calculateTotalPrice() {
        this.totalPrice = new Price(price.getAmount() * quantity, price.getCurrency());
    }

    // 仅提供必要的getter方法，避免过度暴露内部状态
    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        if (this.orderNo != null && !this.orderNo.equals(orderNo)) {
            throw new IllegalArgumentException("订单号不能修改");
        }
        this.orderNo = orderNo;
    }

    public Id getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public Price getRefundAmount() {
        return refundAmount;
    }

    public boolean isReturned() {
        return returned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;
        return productId.equals(orderItem.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderNo='" + orderNo + "'" +
                ", productId=" + productId +
                ", productName='" + productName + "'" +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", refunded=" + refunded +
                ", refundAmount=" + refundAmount +
                ", returned=" + returned +
                '}';
    }
}
