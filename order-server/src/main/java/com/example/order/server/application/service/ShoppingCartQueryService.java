package com.example.order.server.application.service;

import com.example.order.server.application.dto.ShoppingCartResponse;

/**
 * 购物车查询服务接口
 * 负责处理所有购物车相关的查询操作（读操作）
 */
public interface ShoppingCartQueryService {

    /**
     * 获取用户购物车
     */
    ShoppingCartResponse getShoppingCart(Long userId);
}