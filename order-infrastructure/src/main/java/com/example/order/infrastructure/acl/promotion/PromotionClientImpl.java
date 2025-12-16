package com.example.order.infrastructure.acl.promotion;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.promotion.dto.CouponDTO;
import com.example.order.infrastructure.acl.promotion.dto.PromotionActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 促销系统客户端实现类
 * 实现与外部促销系统的交互
 * 使用内存模拟外部系统调用
 */
@Component
public class PromotionClientImpl implements PromotionClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionClientImpl.class);

    // 模拟优惠券数据库
    private static final ConcurrentMap<Long, CouponDTO> COUPON_MAP = new ConcurrentHashMap<>();
    // 模拟促销活动数据库
    private static final ConcurrentMap<Long, PromotionActivityDTO> ACTIVITY_MAP = new ConcurrentHashMap<>();
    // 模拟用户优惠券关系
    private static final ConcurrentMap<Long, List<Long>> USER_COUPON_MAP = new ConcurrentHashMap<>();

    // 初始化模拟数据
    static {
        // 初始化优惠券数据
        CouponDTO coupon1 = new CouponDTO();
        coupon1.setCouponId(1L);
        coupon1.setCouponCode("COUPON001");
        coupon1.setCouponName("满100减10元优惠券");
        coupon1.setCouponDesc("订单满100元可使用，立减10元");
        coupon1.setCouponType(1); // 满减券
        coupon1.setCouponValue(new BigDecimal(10));
        coupon1.setMinOrderAmount(new BigDecimal(100));
        coupon1.setMaxUseCount(1);
        coupon1.setUseCount(0);
        coupon1.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        coupon1.setEndDate(new Date(System.currentTimeMillis() + 7 * 86400000));
        coupon1.setStatus(1);
        coupon1.setStock(100);
        coupon1.setReceivedCount(0);
        coupon1.setActivityId(1L);
        COUPON_MAP.put(coupon1.getCouponId(), coupon1);

        CouponDTO coupon2 = new CouponDTO();
        coupon2.setCouponId(2L);
        coupon2.setCouponCode("COUPON002");
        coupon2.setCouponName("9折优惠券");
        coupon2.setCouponDesc("所有商品9折优惠，最高减50元");
        coupon2.setCouponType(2); // 折扣券
        coupon2.setCouponValue(new BigDecimal(0.9));
        coupon2.setMinOrderAmount(new BigDecimal(50));
        coupon2.setMaxUseCount(1);
        coupon2.setUseCount(0);
        coupon2.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        coupon2.setEndDate(new Date(System.currentTimeMillis() + 7 * 86400000));
        coupon2.setStatus(1);
        coupon2.setStock(200);
        coupon2.setReceivedCount(0);
        coupon2.setActivityId(2L);
        COUPON_MAP.put(coupon2.getCouponId(), coupon2);

        // 初始化促销活动数据
        PromotionActivityDTO activity1 = new PromotionActivityDTO();
        activity1.setActivityId(1L);
        activity1.setActivityCode("ACT001");
        activity1.setActivityName("新用户专享活动");
        activity1.setActivityDesc("新用户注册即可获得满100减10元优惠券");
        activity1.setActivityType(1); // 优惠券活动
        activity1.setDiscountValue(new BigDecimal(10));
        activity1.setDiscountType(1); // 满减
        activity1.setMinOrderAmount(new BigDecimal(100));
        activity1.setMaxDiscountAmount(10);
        activity1.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        activity1.setEndDate(new Date(System.currentTimeMillis() + 30 * 86400000));
        activity1.setStatus(1);
        activity1.setApplicableProducts("1,2");
        activity1.setApplicableCategories("1");
        activity1.setApplicableBrands("1");
        activity1.setMaxParticipateCount(1000);
        activity1.setParticipatedCount(0);
        ACTIVITY_MAP.put(activity1.getActivityId(), activity1);

        PromotionActivityDTO activity2 = new PromotionActivityDTO();
        activity2.setActivityId(2L);
        activity2.setActivityCode("ACT002");
        activity2.setActivityName("全场9折活动");
        activity2.setActivityDesc("全场商品9折优惠，最高减50元");
        activity2.setActivityType(2); // 折扣活动
        activity2.setDiscountValue(new BigDecimal(0.9));
        activity2.setDiscountType(2); // 折扣
        activity2.setMinOrderAmount(new BigDecimal(50));
        activity2.setMaxDiscountAmount(50);
        activity2.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        activity2.setEndDate(new Date(System.currentTimeMillis() + 15 * 86400000));
        activity2.setStatus(1);
        activity2.setApplicableProducts("1,2");
        activity2.setApplicableCategories("1");
        activity2.setApplicableBrands("1");
        activity2.setMaxParticipateCount(5000);
        activity2.setParticipatedCount(0);
        ACTIVITY_MAP.put(activity2.getActivityId(), activity2);
    }

    @Override
    public Optional<CouponDTO> getCouponById(Id couponId) {
        LOGGER.info("获取优惠券信息，优惠券ID：{}", couponId.getValue());
        return Optional.ofNullable(COUPON_MAP.get(couponId.getValue()));
    }

    @Override
    public List<CouponDTO> getAvailableCoupons(Id userId) {
        LOGGER.info("获取用户可用优惠券，用户ID：{}", userId.getValue());
        List<Long> couponIds = USER_COUPON_MAP.getOrDefault(userId.getValue(), new ArrayList<>());
        List<CouponDTO> coupons = new ArrayList<>();
        Date now = new Date();
        
        for (Long couponId : couponIds) {
            CouponDTO coupon = COUPON_MAP.get(couponId);
            if (coupon != null && coupon.getStatus() == 1 && now.after(coupon.getStartDate()) && now.before(coupon.getEndDate()) && coupon.getUseCount() < coupon.getMaxUseCount()) {
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    @Override
    public boolean checkCouponValidity(Id couponId, Id userId, Price totalAmount) {
        LOGGER.info("检查优惠券有效性，优惠券ID：{}，用户ID：{}，订单金额：{}", 
                couponId.getValue(), userId.getValue(), totalAmount.getAmount());
        CouponDTO coupon = COUPON_MAP.get(couponId.getValue());
        if (coupon == null) {
            LOGGER.warn("优惠券不存在，优惠券ID：{}", couponId.getValue());
            return false;
        }

        // 检查优惠券状态
        if (coupon.getStatus() != 1) {
            LOGGER.warn("优惠券无效，优惠券ID：{}，状态：{}", couponId.getValue(), coupon.getStatus());
            return false;
        }

        // 检查优惠券时间
        Date now = new Date();
        if (now.before(coupon.getStartDate()) || now.after(coupon.getEndDate())) {
            LOGGER.warn("优惠券不在有效期内，优惠券ID：{}，开始时间：{}，结束时间：{}", 
                    couponId.getValue(), coupon.getStartDate(), coupon.getEndDate());
            return false;
        }

        // 检查用户是否拥有该优惠券
        List<Long> userCoupons = USER_COUPON_MAP.getOrDefault(userId.getValue(), new ArrayList<>());
        if (!userCoupons.contains(couponId.getValue())) {
            LOGGER.warn("用户未拥有该优惠券，用户ID：{}，优惠券ID：{}", userId.getValue(), couponId.getValue());
            return false;
        }

        // 检查订单金额
        if (totalAmount.getAmount() < coupon.getMinOrderAmount().longValue()) {
            LOGGER.warn("订单金额不足，优惠券ID：{}，订单金额：{}，最低金额：{}", 
                    couponId.getValue(), totalAmount.getAmount(), coupon.getMinOrderAmount());
            return false;
        }

        return true;
    }

    @Override
    public boolean receiveCoupon(Id couponId, Id userId) {
        LOGGER.info("领取优惠券，优惠券ID：{}，用户ID：{}", couponId.getValue(), userId.getValue());
        CouponDTO coupon = COUPON_MAP.get(couponId.getValue());
        if (coupon == null || coupon.getStatus() != 1 || coupon.getStock() <= 0) {
            LOGGER.warn("领取优惠券失败，优惠券ID：{}，状态：{}，库存：{}", 
                    couponId.getValue(), coupon.getStatus(), coupon.getStock());
            return false;
        }

        // 模拟领取优惠券操作
        coupon.setStock(coupon.getStock() - 1);
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        COUPON_MAP.put(couponId.getValue(), coupon);

        // 添加用户优惠券关系
        USER_COUPON_MAP.computeIfAbsent(userId.getValue(), k -> new ArrayList<>()).add(couponId.getValue());
        
        LOGGER.info("领取优惠券成功，优惠券ID：{}，用户ID：{}", couponId.getValue(), userId.getValue());
        return true;
    }

    @Override
    public boolean useCoupon(Id couponId, Id userId, String orderNo) {
        LOGGER.info("使用优惠券，优惠券ID：{}，用户ID：{}，订单号：{}", 
                couponId.getValue(), userId.getValue(), orderNo);
        CouponDTO coupon = COUPON_MAP.get(couponId.getValue());
        if (coupon == null || coupon.getStatus() != 1) {
            LOGGER.warn("使用优惠券失败，优惠券不存在或已失效，优惠券ID：{}", couponId.getValue());
            return false;
        }

        // 检查用户是否拥有该优惠券
        List<Long> userCoupons = USER_COUPON_MAP.getOrDefault(userId.getValue(), new ArrayList<>());
        if (!userCoupons.contains(couponId.getValue())) {
            LOGGER.warn("使用优惠券失败，用户未拥有该优惠券，用户ID：{}，优惠券ID：{}", userId.getValue(), couponId.getValue());
            return false;
        }

        // 模拟使用优惠券操作
        coupon.setUseCount(coupon.getUseCount() + 1);
        COUPON_MAP.put(couponId.getValue(), coupon);
        
        LOGGER.info("使用优惠券成功，优惠券ID：{}，用户ID：{}，订单号：{}", 
                couponId.getValue(), userId.getValue(), orderNo);
        return true;
    }

    @Override
    public boolean refundCoupon(Id couponId, Id userId, String orderNo) {
        LOGGER.info("回退优惠券，优惠券ID：{}，用户ID：{}，订单号：{}", 
                couponId.getValue(), userId.getValue(), orderNo);
        CouponDTO coupon = COUPON_MAP.get(couponId.getValue());
        if (coupon == null) {
            LOGGER.warn("回退优惠券失败，优惠券不存在，优惠券ID：{}", couponId.getValue());
            return false;
        }

        // 模拟回退优惠券操作
        if (coupon.getUseCount() > 0) {
            coupon.setUseCount(coupon.getUseCount() - 1);
            COUPON_MAP.put(couponId.getValue(), coupon);
        }
        
        LOGGER.info("回退优惠券成功，优惠券ID：{}，用户ID：{}，订单号：{}", 
                couponId.getValue(), userId.getValue(), orderNo);
        return true;
    }

    @Override
    public Optional<PromotionActivityDTO> getActivityById(Id activityId) {
        LOGGER.info("获取促销活动信息，活动ID：{}", activityId.getValue());
        return Optional.ofNullable(ACTIVITY_MAP.get(activityId.getValue()));
    }

    @Override
    public List<PromotionActivityDTO> getActivitiesByProductId(Id productId) {
        LOGGER.info("获取商品适用促销活动，商品ID：{}", productId.getValue());
        List<PromotionActivityDTO> activities = new ArrayList<>();
        Date now = new Date();
        
        for (PromotionActivityDTO activity : ACTIVITY_MAP.values()) {
            if (activity.getStatus() == 1 && now.after(activity.getStartDate()) && now.before(activity.getEndDate())) {
                // 检查商品是否适用
                String applicableProducts = activity.getApplicableProducts();
                if (applicableProducts != null && applicableProducts.contains(String.valueOf(productId.getValue()))) {
                    activities.add(activity);
                }
            }
        }
        return activities;
    }

    @Override
    public List<PromotionActivityDTO> getCurrentActivities() {
        LOGGER.info("获取当前有效促销活动");
        List<PromotionActivityDTO> activities = new ArrayList<>();
        Date now = new Date();
        
        for (PromotionActivityDTO activity : ACTIVITY_MAP.values()) {
            if (activity.getStatus() == 1 && now.after(activity.getStartDate()) && now.before(activity.getEndDate())) {
                activities.add(activity);
            }
        }
        return activities;
    }

    @Override
    public Optional<Price> calculateDiscount(Id activityId, Id productId, Integer quantity, Price originalPrice) {
        LOGGER.info("计算促销优惠金额，活动ID：{}，商品ID：{}，数量：{}，原价：{}", 
                activityId.getValue(), productId.getValue(), quantity, originalPrice.getAmount());
        PromotionActivityDTO activity = ACTIVITY_MAP.get(activityId.getValue());
        if (activity == null || activity.getStatus() != 1) {
            LOGGER.warn("促销活动不存在或已失效，活动ID：{}", activityId.getValue());
            return Optional.empty();
        }

        // 检查商品是否适用
        String applicableProducts = activity.getApplicableProducts();
        if (applicableProducts != null && !applicableProducts.contains(String.valueOf(productId.getValue()))) {
            LOGGER.warn("商品不适用该促销活动，商品ID：{}，活动ID：{}", productId.getValue(), activityId.getValue());
            return Optional.empty();
        }

        long totalOriginalAmount = originalPrice.getAmount() * quantity;
        long discountAmount = 0;
        
        // 根据活动类型计算优惠金额
        if (activity.getDiscountType() == 1) { // 满减
            if (totalOriginalAmount >= activity.getMinOrderAmount().longValue()) {
                discountAmount = Math.min(activity.getDiscountValue().longValue(), activity.getMaxDiscountAmount());
            }
        } else if (activity.getDiscountType() == 2) { // 折扣
            long calculatedDiscount = (long) (totalOriginalAmount * (1 - activity.getDiscountValue().doubleValue()));
            discountAmount = Math.min(calculatedDiscount, activity.getMaxDiscountAmount());
        }
        
        LOGGER.info("计算促销优惠金额成功，活动ID：{}，优惠金额：{}", activityId.getValue(), discountAmount);
        return Optional.of(new Price(discountAmount, originalPrice.getCurrency()));
    }
}
