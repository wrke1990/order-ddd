package com.example.order.server.interfaces.facade.impl;

import com.example.order.api.facade.ShoppingCartApiFacade;
import com.example.order.api.vo.req.ShoppingCartReq;
import com.example.order.api.vo.resp.ShoppingCartResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.server.application.assember.ShoppingCartVoAssembler;
import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;
import com.example.order.server.application.service.ShoppingCartCommandService;
import com.example.order.server.application.service.ShoppingCartQueryService;
import org.springframework.stereotype.Service;

/**
 * 购物车API接口实现
 */
@Service
public class ShoppingCartApiFacadeImpl implements ShoppingCartApiFacade {

    private final ShoppingCartCommandService shoppingCartCommandService;
    private final ShoppingCartQueryService shoppingCartQueryService;
    private final ShoppingCartVoAssembler shoppingCartVoAssembler;

    public ShoppingCartApiFacadeImpl(ShoppingCartCommandService shoppingCartCommandService,
                                     ShoppingCartQueryService shoppingCartQueryService,
                                     ShoppingCartVoAssembler shoppingCartVoAssembler) {
        this.shoppingCartCommandService = shoppingCartCommandService;
        this.shoppingCartQueryService = shoppingCartQueryService;
        this.shoppingCartVoAssembler = shoppingCartVoAssembler;
    }

    @Override
    public CommonResponse<ShoppingCartResp> addItem(ShoppingCartReq req) {
        // 转换VO为应用服务DTO
        ShoppingCartCommand request = shoppingCartVoAssembler.toShoppingCartCommand(req);
        // 调用命令服务
        ShoppingCartResponse response = shoppingCartCommandService.addItem(request);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }

    @Override
    public CommonResponse<ShoppingCartResp> increaseQuantity(ShoppingCartReq req) {
        // 转换VO为应用服务DTO
        ShoppingCartCommand request = shoppingCartVoAssembler.toShoppingCartCommand(req);
        // 调用命令服务
        ShoppingCartResponse response = shoppingCartCommandService.increaseItemQuantity(request);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }

    @Override
    public CommonResponse<ShoppingCartResp> decreaseQuantity(ShoppingCartReq req) {
        // 转换VO为应用服务DTO
        ShoppingCartCommand request = shoppingCartVoAssembler.toShoppingCartCommand(req);
        // 调用命令服务
        ShoppingCartResponse response = shoppingCartCommandService.decreaseItemQuantity(request);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }

    @Override
    public CommonResponse<ShoppingCartResp> removeItem(Long userId, Long productId) {
        // 调用命令服务
        ShoppingCartResponse response = shoppingCartCommandService.removeItem(userId, productId);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }

    @Override
    public CommonResponse<Void> clearCart(Long userId) {
        // 调用命令服务
        shoppingCartCommandService.clearShoppingCart(userId);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<ShoppingCartResp> getCart(Long userId) {
        // 调用查询服务
        ShoppingCartResponse response = shoppingCartQueryService.getShoppingCart(userId);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }

    @Override
    public CommonResponse<ShoppingCartResp> removeUnavailableItems(Long userId) {
        // 调用命令服务
        ShoppingCartResponse response = shoppingCartCommandService.removeUnavailableItems(userId);
        // 转换DTO为API响应VO
        ShoppingCartResp resp = shoppingCartVoAssembler.toShoppingCartResponse(response);
        return CommonResponse.success(resp);
    }
}
