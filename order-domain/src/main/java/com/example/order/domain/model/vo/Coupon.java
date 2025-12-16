package com.example.order.domain.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券值对象
 */
public class Coupon implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long couponId;
    private Long couponTemplateId;
    private String name;
    private String description;
    private Price discountAmount;
    private Price minOrderAmount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Coupon(Long couponId, Long couponTemplateId, String name, String description, 
                 Price discountAmount, Price minOrderAmount, 
                 LocalDateTime startTime, LocalDateTime endTime) {
        if (couponId == null) {
            throw new IllegalArgumentException("优惠券ID不能为空");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("优惠券名称不能为空");
        }
        if (discountAmount == null || discountAmount.getAmount() <= 0) {
            throw new IllegalArgumentException("优惠券折扣金额必须大于0");
        }
        if (startTime == null || endTime == null || endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("优惠券有效期设置不正确");
        }
        this.couponId = couponId;
        this.couponTemplateId = couponTemplateId;
        this.name = name;
        this.description = description;
        this.discountAmount = discountAmount;
        this.minOrderAmount = minOrderAmount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 检查优惠券是否可用
     */
    public boolean isValid(LocalDateTime now, Price orderAmount) {
        // 检查有效期
        if (now.isBefore(startTime) || now.isAfter(endTime)) {
            return false;
        }
        // 检查订单金额限制
        if (minOrderAmount != null && orderAmount.getAmount() < minOrderAmount.getAmount()) {
            return false;
        }
        return true;
    }

    // Getters
    public Long getCouponId() {
        return couponId;
    }

    public Long getCouponTemplateId() {
        return couponTemplateId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Price getDiscountAmount() {
        return discountAmount;
    }

    public Price getMinOrderAmount() {
        return minOrderAmount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponId=" + couponId +
                ", couponTemplateId=" + couponTemplateId +
                ", name='" + name + '\'' +
                ", discountAmount=" + discountAmount +
                ", minOrderAmount=" + minOrderAmount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}