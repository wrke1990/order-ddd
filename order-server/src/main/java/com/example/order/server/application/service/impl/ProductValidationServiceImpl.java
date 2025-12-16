package com.example.order.server.application.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.product.ProductClient;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import com.example.order.server.application.service.ProductValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品验证服务实现类
 * 封装了商品存在性和库存验证的共享逻辑
 */
@Service
public class ProductValidationServiceImpl implements ProductValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ProductValidationServiceImpl.class);

    private final ProductClient productClient;

    public ProductValidationServiceImpl(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    @Cacheable(value = "validatedProducts", key = "#productIds.toString() + '-' + #productQuantityMap.toString()")
    public Map<Id, ProductDTO> validateProductsAndStock(List<Id> productIds, Map<Id, Integer> productQuantityMap) {
        logger.info("批量验证商品存在性和库存，商品ID列表: {}", productIds);

        // 批量获取商品信息
        List<ProductDTO> products = productClient.getProductsByIds(productIds);

        // 检查商品是否全部存在
        if (products.size() != productIds.size()) {
            Set<Id> foundProductIds = products.stream()
                    .map(p -> Id.of(p.getProductId()))
                    .collect(Collectors.toSet());

            List<Id> missingProductIds = productIds.stream()
                    .filter(id -> !foundProductIds.contains(id))
                    .collect(Collectors.toList());

            throw new BusinessException("以下商品不存在: " + missingProductIds);
        }

        // 构建商品ID到ProductDTO的映射
        Map<Id, ProductDTO> productMap = new HashMap<>();
        for (ProductDTO product : products) {
            productMap.put(Id.of(product.getProductId()), product);
        }

        // 检查库存
        Map<Id, Integer> stockMap = productClient.getProductStocksByIds(productIds);
        for (Map.Entry<Id, ProductDTO> entry : productMap.entrySet()) {
            Id productId = entry.getKey();
            ProductDTO product = entry.getValue();
            int requestedQuantity = productQuantityMap.get(productId);
            int availableStock = stockMap.getOrDefault(productId, 0);

            if (availableStock < requestedQuantity) {
                throw new BusinessException(
                        String.format("商品库存不足，商品ID: %d，请求数量: %d，可用库存: %d",
                                productId.getValue(), requestedQuantity, availableStock)
                );
            }

            logger.info("商品库存检查通过，商品ID: {}, 请求数量: {}, 可用库存: {}",
                    productId.getValue(), requestedQuantity, availableStock);
        }

        return productMap;
    }

    @Override
    @Cacheable(value = "productAvailability", key = "#productId")
    public boolean isProductAvailable(Id productId) {
        logger.info("检查商品是否可用，商品ID: {}", productId);

        // 检查商品是否存在
        List<ProductDTO> products = productClient.getProductsByIds(Arrays.asList(productId));
        if (products.isEmpty()) {
            logger.info("商品不存在，商品ID: {}", productId);
            return false;
        }

        // 检查库存是否大于0
        Map<Id, Integer> stockMap = productClient.getProductStocksByIds(Arrays.asList(productId));
        Integer stock = stockMap.getOrDefault(productId, 0);
        boolean hasStock = stock > 0;
        if (!hasStock) {
            logger.info("商品库存不足，商品ID: {}, 库存: {}", productId, stock);
        }

        return hasStock;
    }

    @Override
    @Cacheable(value = "productDetail", key = "#productId")
    public ProductDTO getProductById(Id productId) {
        logger.info("获取单个商品详情，商品ID: {}", productId);

        List<ProductDTO> products = productClient.getProductsByIds(Arrays.asList(productId));
        if (products.isEmpty()) {
            logger.info("未找到商品，商品ID: {}", productId);
            return null;
        }

        return products.get(0);
    }
}
