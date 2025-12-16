package com.example.order.domain.model.vo;

import java.io.Serializable;

/**
 * 价格值对象
 */
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long amount;
    private final String currency;

    public Price(Long amount, String currency) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("价格金额必须大于等于0");
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("货币类型不能为空");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

    /**
     * 创建人民币价格
     */
    public static Price ofCNY(Long amount) {
        return new Price(amount, "CNY");
    }

    /**
     * 创建美元价格
     */
    public static Price ofUSD(Long amount) {
        return new Price(amount, "USD");
    }
    
    /**
     * 创建零价格（人民币）
     */
    public static Price zero() {
        return new Price(0L, "CNY");
    }
    
    /**
     * 价格加法运算
     */
    public Price add(Price other) {
        if (other == null) {
            throw new IllegalArgumentException("待相加的价格不能为空");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("货币类型不匹配，无法进行加法运算");
        }
        return new Price(this.amount + other.amount, this.currency);
    }
}
