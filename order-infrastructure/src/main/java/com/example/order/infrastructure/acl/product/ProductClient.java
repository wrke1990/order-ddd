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
     * 根据商品ID获取完整商品信息
     * @param productId 商品ID
     * @return 商品完整信息
     */
    ProductDTO getProductById(Id productId);

    /**
     * 根据商品ID获取商品名称
     * @param productId 商品ID
     * @return 商品名称
     */
    String getProductNameById(Id productId);

    /**
     * 根据商品ID获取商品图片
     * @param productId 商品ID
     * @return 商品图片URL
     */
    String getProductImageById(Id productId);

    /**
     * 根据商品ID获取商品价格
     * @param productId 商品ID
     * @return 商品价格
     */
    Price getProductPriceById(Id productId);

    /**
     * 根据商品ID获取商品库存
     * @param productId 商品ID
     * @return 商品库存数量
     */
    Integer getProductStockById(Id productId);

    /**
     * 批量获取商品信息
     *
     * @param productIds 商品ID列表
     * @return 商品信息列表
     */
    List<ProductDTO> getProductsByIds(List<Id> productIds);

    /**
     * 批量获取商品库存
     * @param productIds 商品ID列表
     * @return 商品库存映射表，key为商品ID，value为库存数量
     */
    Map<Id, Integer> getProductStocksByIds(List<Id> productIds);

    /**
     * 锁定商品库存
     * @param productId 商品ID
     * @param quantity 锁定数量
     * @return 是否锁定成功
     */
    boolean lockProductStock(Id productId, Integer quantity);

    /**
     * 批量锁定商品库存
     * @param productIdQuantities 商品ID和数量的映射表
     * @return 是否全部锁定成功
     */
    boolean lockProductStocks(Map<Id, Integer> productIdQuantities);

    /**
     * 解锁商品库存
     * @param productId 商品ID
     * @param quantity 解锁数量
     * @return 是否解锁成功
     */
    boolean unlockProductStock(Id productId, Integer quantity);

    /**
     * 扣减商品库存
     * @param productId 商品ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    boolean deductProductStock(Id productId, Integer quantity);

    /**
     * 增加商品库存
     * @param productId 商品ID
     * @param quantity 增加数量
     * @return 是否增加成功
     */
    boolean addProductStock(Id productId, Integer quantity);
}
