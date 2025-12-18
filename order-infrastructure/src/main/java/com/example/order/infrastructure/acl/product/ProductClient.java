package com.example.order.infrastructure.acl.product;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;

import java.util.List;
import java.util.Map;

/**
 * 商品系统客户端接口（反腐层）
 * 用于与外部商品系统交互，获取商品信息和库存
 */
public interface ProductClient {

    /**
     * 批量获取商品信息
     *
     * @param productIds 商品ID列表
     * @return 商品信息列表
     */
    List<ProductDTO> getProductsByIds(List<Id> productIds);

    /**
     * 批量锁定商品库存
     * @param productIdQuantities 商品ID和锁定数量的映射
     * @return 是否全部锁定成功
     */
    boolean lockProductStocks(Map<Id, Integer> productIdQuantities);

    /**
     * 批量解锁商品库存
     * @param productIdQuantities 商品ID和解锁数量的映射
     * @return 是否解锁成功
     */
    boolean unlockProductStock(Map<Id, Integer> productIdQuantities);

    /**
     * 批量扣减商品库存
     * @param productIdQuantities 商品ID和扣减数量的映射
     * @return 是否扣减成功
     */
    boolean deductProductStock(Map<Id, Integer> productIdQuantities);

}
