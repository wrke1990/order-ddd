package com.example.order.domain.model.vo;

import java.util.Objects;

/**
 * 售后状态枚举
 */
public enum AfterSaleStatus {

    /**
     * 待处理
     */
    PENDING_PROCESS(1, "待处理"),

    /**
     * 待退款
     */
    PENDING_REFUND(2, "待退款"),

    /**
     * 待退货
     */
    PENDING_RETURN(3, "待退货"),

    /**
     * 待确认退货
     */
    PENDING_RETURN_CONFIRM(4, "待确认退货"),

    /**
     * 已退款
     */
    REFUNDED(5, "已退款"),

    /**
     * 已完成
     */
    COMPLETED(6, "已完成"),

    /**
     * 已拒绝
     */
    REJECTED(7, "已拒绝"),

    /**
     * 已取消
     */
    CANCELLED(8, "已取消");

    private final Integer code;
    private final String desc;

    AfterSaleStatus(Integer code, String desc) {
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
    public static AfterSaleStatus getByCode(Integer code) {
        for (AfterSaleStatus status : values()) {
            if (Objects.equals(status.code, code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AfterSaleStatus code: " + code);
    }

    /**
     * 根据desc获取枚举
     */
    public static AfterSaleStatus getByDesc(String desc) {
        for (AfterSaleStatus status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AfterSaleStatus desc: " + desc);
    }
}