package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.entity.ShoppingCartItem;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 购物车DTO映射器
 */
@Mapper(componentModel = "spring")
public interface ShoppingCartDtoAssembler {

    ShoppingCartDtoAssembler INSTANCE = Mappers.getMapper(ShoppingCartDtoAssembler.class);

    /**
     * 购物车命令转领域实体
     */
    default ShoppingCart toShoppingCart(ShoppingCartCommand request) {
        if (request == null) {
            return null;
        }

        // 创建购物车
        ShoppingCart shoppingCart = ShoppingCart.create(request.getUserId());

        // 添加购物车项
        if (request.getItem() != null) {
            ShoppingCartItem item = toShoppingCartItem(request.getItem());
            shoppingCart.addItem(item);
        }

        return shoppingCart;
    }

    /**
     * 购物车项命令转领域实体
     */
    default ShoppingCartItem toShoppingCartItem(ShoppingCartCommand.ItemCommand requestItem) {
        if (requestItem == null) {
            return null;
        }

        // 使用工厂方法创建购物车项
        return ShoppingCartItem.create(
                requestItem.getProductId(),
                requestItem.getProductName(),
                requestItem.getProductImage(),
                requestItem.getQuantity(),
                new Price(requestItem.getPrice(), requestItem.getCurrency()),
                true // 默认商品可用
        );
    }

    /**
     * 领域购物车转响应DTO
     */
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalAmount", expression = "java(shoppingCart.getItems().stream().mapToLong(item -> item.getSubtotal().getAmount()).sum())")
    @Mapping(target = "currency", expression = "java(shoppingCart.getItems().isEmpty() ? null : shoppingCart.getItems().get(0).getPrice().getCurrency())")
    @Mapping(target = "itemCount", expression = "java(shoppingCart.getItems().size())")
    ShoppingCartResponse toShoppingCartResponse(ShoppingCart shoppingCart);

    /**
     * 领域购物车项转响应DTO
     */
    @Mappings({
            @Mapping(target = "price", source = "price.amount"),
            @Mapping(target = "currency", source = "price.currency"),
            @Mapping(target = "totalPrice", source = "subtotal.amount")
    })
    ShoppingCartResponse.ItemResponse toShoppingCartItemResponse(ShoppingCartItem item);
}
