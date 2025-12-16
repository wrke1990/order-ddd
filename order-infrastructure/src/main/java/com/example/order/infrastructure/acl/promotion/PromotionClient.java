package com.example.order.infrastructure.acl.promotion;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.promotion.dto.CouponDTO;
import com.example.order.infrastructure.acl.promotion.dto.PromotionActivityDTO;

import java.util.List;
import java.util.Optional;

/**
 * 促销系统客户端接口
 * 定义与外部促销系统的交互方法
 */
public interface PromotionClient {

    /**
     * 根据优惠券ID获取优惠券信息
     *
     * @param couponId 优惠券ID
     * @return 优惠券DTO
     */
    Optional<CouponDTO> getCouponById(Id couponId);

    /**
     * 根据用户ID获取可用优惠券列表
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<CouponDTO> getAvailableCoupons(Id userId);

    /**
     * 检查优惠券是否可用于订单
     *
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @param totalAmount 订单总金额
     * @return 是否可用
     */
    boolean checkCouponValidity(Id couponId, Id userId, Price totalAmount);

    /**
     * 领取优惠券
     *
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 是否领取成功
     */
    boolean receiveCoupon(Id couponId, Id userId);

    /**
     * 使用优惠券
     *
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 是否使用成功
     */
    boolean useCoupon(Id couponId, Id userId, String orderNo);

    /**
     * 回退优惠券
     *
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 是否回退成功
     */
    boolean refundCoupon(Id couponId, Id userId, String orderNo);

    /**
     * 根据促销活动ID获取促销活动信息
     *
     * @param activityId 促销活动ID
     * @return 促销活动DTO
     */
    Optional<PromotionActivityDTO> getActivityById(Id activityId);

    /**
     * 根据商品ID获取适用的促销活动列表
     *
     * @param productId 商品ID
     * @return 适用的促销活动列表
     */
    List<PromotionActivityDTO> getActivitiesByProductId(Id productId);

    /**
     * 获取当前有效的促销活动列表
     *
     * @return 有效的促销活动列表
     */
    List<PromotionActivityDTO> getCurrentActivities();

    /**
     * 计算促销优惠金额
     *
     * @param activityId 促销活动ID
     * @param productId 商品ID
     * @param quantity 商品数量
     * @param originalPrice 商品原价
     * @return 优惠金额
     */
    Optional<Price> calculateDiscount(Id activityId, Id productId, Integer quantity, Price originalPrice);
}
