package com.example.order.domain.service;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.vo.Id;

import java.util.List;

/**
 * 购物车领域服务接口
 */
public interface ShoppingCartDomainService {

    /**
     * 清空购物车
     * @param userId 用户ID
     * @return 更新后的购物车
     */
    ShoppingCart clearShoppingCart(Long userId);

    /**
     * 根据商品ID列表删除购物车商品
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @return 更新后的购物车
     */
    ShoppingCart removeItemsByProductIds(Long userId, List<Id> productIds);
}
