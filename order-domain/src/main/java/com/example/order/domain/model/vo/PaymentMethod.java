package com.example.order.domain.model.vo;

import java.io.Serializable;

/**
 * 支付方式值对象
 */
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum MethodType {
        ALIPAY, WECHAT, CREDIT_CARD, BANK_TRANSFER
    }

    private Id paymentMethodId;
    private MethodType type;
    private String name;

    public PaymentMethod(Id paymentMethodId, MethodType type, String name) {
        if (type == null) {
            throw new IllegalArgumentException("支付方式类型不能为空");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("支付方式名称不能为空");
        }
        this.paymentMethodId = paymentMethodId;
        this.type = type;
        this.name = name;
    }

    // Getters
    public Id getPaymentMethodId() {
        return paymentMethodId;
    }

    public MethodType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "paymentMethodId=" + paymentMethodId +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}