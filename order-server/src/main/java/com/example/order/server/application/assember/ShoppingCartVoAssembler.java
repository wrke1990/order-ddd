package com.example.order.server.application.assember;

import com.example.order.api.vo.req.ShoppingCartReq;
import com.example.order.api.vo.resp.ShoppingCartResp;
import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 购物车VO与应用服务DTO转换器
 */
@Mapper(componentModel = "spring")
public interface ShoppingCartVoAssembler {

    ShoppingCartVoAssembler INSTANCE = Mappers.getMapper(ShoppingCartVoAssembler.class);

    /**
     * API请求VO转应用服务DTO
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "item", source = "item")
    ShoppingCartCommand toShoppingCartCommand(ShoppingCartReq req);

    /**
     * API请求项VO转应用服务DTO
     */
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "productImage", source = "productImage")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "selected", source = "selected")
    ShoppingCartCommand.ItemCommand toShoppingCartItemCommand(ShoppingCartReq.ItemReq itemReq);

    /**
     * 应用服务响应DTO转API响应VO
     */
    @Mapping(target = "items", source = "items")
    ShoppingCartResp toShoppingCartResponse(ShoppingCartResponse resp);

    /**
     * 应用服务响应项DTO转API响应VO
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "productName", source = "productName"),
            @Mapping(target = "productImage", source = "productImage"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "quantity", source = "quantity")
    })
    ShoppingCartResp.ShoppingCartItemResp toShoppingCartItemResponse(ShoppingCartResponse.ItemResponse itemResp);
}
