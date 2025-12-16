package com.example.order.common.cache;

/**
 * 缓存键常量类
 */
public class CacheKeyConstants {

    // 购物车相关缓存键
    public static final String SHOPPING_CART_PREFIX = "order:shopping_cart:";
    public static final String SHOPPING_CART_LOCK_PREFIX = "order:shopping_cart:lock:";

    // 售后订单相关缓存键
    public static final String AFTER_SALE_ORDER_PREFIX = "order:after_sale:";

    // 订单相关缓存键
    public static final String ORDER_PREFIX = "order:order:";

    /**
     * 获取购物车缓存键
     */
    public static String getShoppingCartKey(Long userId) {
        return SHOPPING_CART_PREFIX + userId;
    }

    /**
     * 获取购物车锁键
     */
    public static String getShoppingCartLockKey(Long userId) {
        return SHOPPING_CART_LOCK_PREFIX + userId;
    }

    /**
     * 获取售后订单缓存键
     */
    public static String getAfterSaleOrderKey(String afterSaleNo) {
        return AFTER_SALE_ORDER_PREFIX + afterSaleNo;
    }

    /**
     * 获取订单缓存键
     */
    public static String getOrderKey(String orderNo) {
        return ORDER_PREFIX + orderNo;
    }

    /**
     * 获取订单列表缓存键
     */
    public static String getOrderListKey(Long userId) {
        return ORDER_PREFIX + "list:" + userId;
    }
}
