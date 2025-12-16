package com.example.order.server.application.service;

import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;

import java.util.List;
import java.util.Map;

/**
 * 商品验证服务接口
 * 用于封装商品存在性和库存验证逻辑，供订单创建和购物车操作流程复用
 */
public interface ProductValidationService {

    /**
     * 批量验证商品存在性和库存充足性
     *
     * @param productIds         商品ID列表
     * @param productQuantityMap 商品ID到数量的映射
     * @return 商品信息映射
     */
    Map<Id, ProductDTO> validateProductsAndStock(List<Id> productIds, Map<Id, Integer> productQuantityMap);

    /**
     * 检查单个商品是否存在且库存大于0
     *
     * @param productId 商品ID
     * @return 是否可用
     */
    boolean isProductAvailable(Id productId);

    /**
     * 获取单个商品的详细信息
     *
     * @param productId 商品ID
     * @return 商品信息，如果商品不存在则返回null
     */
    ProductDTO getProductById(Id productId);
}
