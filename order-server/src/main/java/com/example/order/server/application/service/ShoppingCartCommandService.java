package com.example.order.server.application.service;

import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;

/**
 * 购物车命令服务接口
 * 负责处理所有购物车相关的命令操作（写操作）
 */
public interface ShoppingCartCommandService {

    /**
     * 添加商品到购物车
     */
    ShoppingCartResponse addItem(ShoppingCartCommand request);

    /**
     * 增加购物车商品数量
     */
    ShoppingCartResponse increaseItemQuantity(ShoppingCartCommand request);

    /**
     * 减少购物车商品数量
     */
    ShoppingCartResponse decreaseItemQuantity(ShoppingCartCommand request);

    /**
     * 删除单个购物车商品
     */
    ShoppingCartResponse removeItem(Long userId, Long productId);

    /**
     * 删除全部下架商品
     */
    ShoppingCartResponse removeUnavailableItems(Long userId);

    /**
     * 清空购物车
     */
    void clearShoppingCart(Long userId);
}