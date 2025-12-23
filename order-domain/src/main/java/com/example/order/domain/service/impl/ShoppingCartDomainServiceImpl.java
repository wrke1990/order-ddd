package com.example.order.domain.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.repository.ShoppingCartRepository;
import com.example.order.domain.service.ShoppingCartDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShoppingCartDomainServiceImpl implements ShoppingCartDomainService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartDomainServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartDomainServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart clearShoppingCart(Long userId) {
        logger.info("清空购物车，用户ID: {}", userId);
        if (userId == null || userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> ShoppingCart.create(userId));
        shoppingCart.clear();
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        logger.info("购物车已清空，用户ID: {}", userId);
        return savedShoppingCart;
    }

    @Override
    public ShoppingCart removeItemsByProductIds(Long userId, List<Id> productIds) {
        logger.info("批量删除购物车商品，用户ID: {}, 商品ID列表: {}", userId, productIds);
        if (userId == null || userId <= 0) {
            throw new BusinessException("用户ID无效");
        }
        if (productIds == null || productIds.isEmpty()) {
            logger.info("没有需要删除的商品，直接返回");
            // 如果没有商品需要删除，返回空购物车
            return ShoppingCart.create(userId);
        }

        // 获取或创建购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> ShoppingCart.create(userId));

        // 批量删除商品
        shoppingCart.removeItemsByProductIds(productIds);

        // 保存购物车
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);

        logger.info("批量删除购物车商品完成，用户ID: {}", userId);
        return savedShoppingCart;
    }
}
