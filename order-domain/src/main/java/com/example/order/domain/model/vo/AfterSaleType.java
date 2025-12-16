package com.example.order.domain.model.vo;

import java.util.Objects;

/**
 * 售后类型枚举
 */
public enum AfterSaleType {

    /**
     * 仅退款
     */
    REFUND_ONLY(1, "仅退款"),
    
    /**
     * 退货退款
     */
    REFUND_WITH_RETURN(2, "退货退款");

    private final Integer code;
    private final String desc;

    AfterSaleType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     */
    public static AfterSaleType getByCode(Integer code) {
        for (AfterSaleType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid AfterSaleType code: " + code);
    }

    /**
     * 根据desc获取枚举
     */
    public static AfterSaleType getByDesc(String desc) {
        for (AfterSaleType type : values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid AfterSaleType desc: " + desc);
    }
}