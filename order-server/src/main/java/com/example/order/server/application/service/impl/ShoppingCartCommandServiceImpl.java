package com.example.order.server.application.service.impl;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.entity.ShoppingCartItem;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.repository.ShoppingCartRepository;
import com.example.order.domain.service.ShoppingCartDomainService;
import com.example.order.infrastructure.acl.product.ProductClient;
import com.example.order.server.application.assember.ShoppingCartDtoAssembler;
import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;
import com.example.order.server.application.service.ProductValidationService;
import com.example.order.server.application.service.ShoppingCartCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车命令服务实现
 */
@Service
public class ShoppingCartCommandServiceImpl implements ShoppingCartCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCommandServiceImpl.class);

    private final ShoppingCartDomainService shoppingCartDomainService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDtoAssembler shoppingCartDtoAssembler;
    private final ProductClient productClient;
    private final ProductValidationService productValidationService;

    public ShoppingCartCommandServiceImpl(ShoppingCartDomainService shoppingCartDomainService,
                                          ShoppingCartRepository shoppingCartRepository,
                                          ShoppingCartDtoAssembler shoppingCartDtoAssembler,
                                          ProductClient productClient,
                                          ProductValidationService productValidationService) {
        this.shoppingCartDomainService = shoppingCartDomainService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDtoAssembler = shoppingCartDtoAssembler;
        this.productClient = productClient;
        this.productValidationService = productValidationService;
    }

    @Override
    @Transactional
    @CachePut(value = "shoppingCart", key = "#request.userId")
    public ShoppingCartResponse addItem(ShoppingCartCommand request) {
        LOGGER.info("添加商品到购物车，用户ID：{}，商品ID：{}，数量：{}",
                request.getUserId(), request.getItem().getProductId(), request.getItem().getQuantity());

        // 验证商品存在性和库存
        Id productId = Id.of(request.getItem().getProductId());
        int quantity = request.getItem().getQuantity();
        productValidationService.validateProductsAndStock(
                java.util.Collections.singletonList(productId),
                java.util.Collections.singletonMap(productId, quantity)
        );

        // 获取或创建购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> ShoppingCart.create(request.getUserId()));

        // 添加商品到购物车
        shoppingCart.addItem(shoppingCartDtoAssembler.toShoppingCartItem(request.getItem()));

        // 保存购物车
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }

    @Override
    @Transactional
    @CachePut(value = "shoppingCart", key = "#request.userId")
    public ShoppingCartResponse increaseItemQuantity(ShoppingCartCommand request) {
        LOGGER.info("增加购物车商品数量，用户ID：{}，商品ID：{}，增加数量：{}",
                request.getUserId(), request.getItem().getProductId(), request.getItem().getQuantity());

        // 获取购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("购物车不存在"));

        // 计算增加后的总数量
        Id productId = Id.of(request.getItem().getProductId());
        int addQuantity = request.getItem().getQuantity();
        ShoppingCartItem existingItem = shoppingCart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getItem().getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("购物车中不存在该商品"));
        int totalQuantity = existingItem.getQuantity() + addQuantity;

        // 验证库存
        productValidationService.validateProductsAndStock(
                java.util.Collections.singletonList(productId),
                java.util.Collections.singletonMap(productId, totalQuantity)
        );

        // 增加商品数量
        shoppingCart.increaseItemQuantity(request.getItem().getProductId(), request.getItem().getQuantity());

        // 保存购物车
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }

    @Override
    @Transactional
    @CachePut(value = "shoppingCart", key = "#request.userId")
    public ShoppingCartResponse decreaseItemQuantity(ShoppingCartCommand request) {
        LOGGER.info("减少购物车商品数量，用户ID：{}，商品ID：{}，减少数量：{}",
                request.getUserId(), request.getItem().getProductId(), request.getItem().getQuantity());

        // 获取购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("购物车不存在"));

        // 减少商品数量
        shoppingCart.decreaseItemQuantity(request.getItem().getProductId(), request.getItem().getQuantity());

        // 保存购物车
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }

    @Override
    @Transactional
    @CachePut(value = "shoppingCart", key = "#userId")
    public ShoppingCartResponse removeItem(Long userId, Long productId) {
        LOGGER.info("删除购物车商品，用户ID：{}，商品ID：{}", userId, productId);

        // 获取购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("购物车不存在"));

        // 删除商品
        shoppingCart.removeItem(productId);

        // 保存购物车
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }

    @Override
    @Transactional
    @CachePut(value = "shoppingCart", key = "#userId")
    public ShoppingCartResponse removeUnavailableItems(Long userId) {
        LOGGER.info("删除购物车中下架商品，用户ID：{}", userId);

        // 获取购物车
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("购物车不存在"));

        // 检查并删除不可用的商品
        List<ShoppingCartItem> itemsToRemove = shoppingCart.getItems().stream()
                .filter(item -> {
                    // 检查商品是否存在且库存大于0
                    Id productId = Id.of(item.getProductId());
                    String productName = productClient.getProductNameById(productId);
                    Integer stock = productClient.getProductStockById(productId);
                    return productName == null || stock == null || stock <= 0;
                })
                .collect(java.util.stream.Collectors.toList());

        // 删除不可用商品
        for (ShoppingCartItem item : itemsToRemove) {
            shoppingCart.removeItem(item.getProductId());
            LOGGER.info("删除不可用商品，用户ID：{}，商品ID：{}", userId, item.getProductId());
        }

        // 保存购物车
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // 转换为响应DTO
        return shoppingCartDtoAssembler.toShoppingCartResponse(shoppingCart);
    }

    @Override
    @Transactional
    @CacheEvict(value = "shoppingCart", key = "#userId")
    public void clearShoppingCart(Long userId) {
        LOGGER.info("清空购物车，用户ID：{}", userId);

        // 清空购物车
        shoppingCartDomainService.clearShoppingCart(userId);
    }
}