package com.example.order.api.facade;

import com.example.order.api.vo.req.ShoppingCartReq;
import com.example.order.api.vo.resp.ShoppingCartResp;
import com.example.order.common.response.CommonResponse;

/**
 * 购物车API接口
 */
public interface ShoppingCartApiFacade {
    
    /**
     * 添加商品到购物车
     * @param req 购物车请求参数
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> addItem(ShoppingCartReq req);
    
    /**
     * 增加商品数量
     * @param req 购物车请求参数
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> increaseQuantity(ShoppingCartReq req);
    
    /**
     * 减少商品数量
     * @param req 购物车请求参数
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> decreaseQuantity(ShoppingCartReq req);
    
    /**
     * 从购物车移除商品
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> removeItem(Long userId, Long productId);
    
    /**
     * 清空购物车
     * @param userId 用户ID
     * @return 操作结果
     */
    CommonResponse<Void> clearCart(Long userId);
    
    /**
     * 获取用户购物车
     * @param userId 用户ID
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> getCart(Long userId);
    
    /**
     * 移除购物车中所有不可用商品
     * @param userId 用户ID
     * @return 购物车响应
     */
    CommonResponse<ShoppingCartResp> removeUnavailableItems(Long userId);
}
