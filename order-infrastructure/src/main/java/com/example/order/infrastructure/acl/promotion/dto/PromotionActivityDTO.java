package com.example.order.infrastructure.acl.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 促销系统促销活动DTO（数据传输对象）
 * 外部促销系统的数据结构
 */
public class PromotionActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long activityId;
    private String activityCode;
    private String activityName;
    private String activityDesc;
    private Integer activityType;
    private BigDecimal discountValue;
    private Integer discountType;
    private BigDecimal minOrderAmount;
    private Integer maxDiscountAmount;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private String applicableProducts;
    private String applicableCategories;
    private String applicableBrands;
    private Integer maxParticipateCount;
    private Integer participatedCount;

    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Integer getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Integer maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApplicableProducts() {
        return applicableProducts;
    }

    public void setApplicableProducts(String applicableProducts) {
        this.applicableProducts = applicableProducts;
    }

    public String getApplicableCategories() {
        return applicableCategories;
    }

    public void setApplicableCategories(String applicableCategories) {
        this.applicableCategories = applicableCategories;
    }

    public String getApplicableBrands() {
        return applicableBrands;
    }

    public void setApplicableBrands(String applicableBrands) {
        this.applicableBrands = applicableBrands;
    }

    public Integer getMaxParticipateCount() {
        return maxParticipateCount;
    }

    public void setMaxParticipateCount(Integer maxParticipateCount) {
        this.maxParticipateCount = maxParticipateCount;
    }

    public Integer getParticipatedCount() {
        return participatedCount;
    }

    public void setParticipatedCount(Integer participatedCount) {
        this.participatedCount = participatedCount;
    }
}
