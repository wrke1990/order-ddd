package com.example.order.domain.repository;

import com.example.order.domain.model.aggregate.ShoppingCart;

import java.util.Optional;

/**
 * 购物车仓储接口
 */
public interface ShoppingCartRepository {

    /**
     * 保存购物车
     */
    ShoppingCart save(ShoppingCart shoppingCart);

    /**
     * 根据用户ID查询购物车
     */
    Optional<ShoppingCart> findByUserId(Long userId);

    /**
     * 删除购物车
     */
    void deleteByUserId(Long userId);
}
