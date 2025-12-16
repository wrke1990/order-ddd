package com.example.order.server.application.service.impl;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.repository.ShoppingCartRepository;
import com.example.order.server.application.assember.ShoppingCartDtoAssembler;
import com.example.order.server.application.dto.ShoppingCartResponse;
import com.example.order.server.application.service.ShoppingCartQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 购物车查询服务实现
 */
@Service
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartQueryServiceImpl.class);
    
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDtoAssembler shoppingCartDtoAssembler;

    public ShoppingCartQueryServiceImpl(ShoppingCartRepository shoppingCartRepository, 
                                        ShoppingCartDtoAssembler shoppingCartDtoAssembler) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDtoAssembler = shoppingCartDtoAssembler;
    }

    @Override
    @Cacheable(value = "shoppingCart", key = "#userId")
    public ShoppingCartResponse getShoppingCart(Long userId) {
        LOGGER.info("获取用户购物车，用户ID：{}", userId);
        
        // 获取购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("购物车不存在"));

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }
}