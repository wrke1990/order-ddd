package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import com.example.order.server.application.dto.OrderItemResponse;
import com.example.order.server.application.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 订单DTO映射器
 */
@Mapper(componentModel = "spring")
public interface OrderDtoAssembler {

    OrderDtoAssembler INSTANCE = Mappers.getMapper(OrderDtoAssembler.class);

    /**
     * 订单命令项转领域实体
     */
    default OrderItem toOrderItem(Id productId, String productName, int quantity, Price productPrice) {
        if (productId == null || productName == null || productPrice == null) {
            throw new RuntimeException("商品信息不完整");
        }

        // 使用工厂方法创建订单项
        return OrderItem.create(
                productId,
                productName,
                quantity,
                productPrice
        );
    }
    
    /**
     * 从ProductDTO创建OrderItem
     */
    default OrderItem toOrderItem(ProductDTO productDTO, int quantity) {
        if (productDTO == null) {
            throw new RuntimeException("商品信息不完整");
        }

        // 使用工厂方法创建订单项
        return OrderItem.create(
                Id.of(productDTO.getProductId()),
                productDTO.getProductName(),
                quantity,
                Price.ofCNY(productDTO.getPrice().longValue())
        );
    }

    /**
     * 领域订单转响应DTO
     */
    @Mappings({
            @Mapping(target = "id", expression = "java(order.getId().getValue())"),
            @Mapping(target = "userId", expression = "java(order.getUserId().getValue())"),
            @Mapping(target = "totalAmount", source = "totalAmount.amount"),
            @Mapping(target = "currency", source = "totalAmount.currency"),
            @Mapping(target = "items", source = "orderItems"),
            @Mapping(target = "createTime", expression = "java(order.getCreateTime().toString())"),
            @Mapping(target = "updateTime", expression = "java(order.getUpdateTime().toString())")
    })
    OrderResponse toOrderResponse(Order order);

    /**
     * 领域订单项转响应DTO
     */
    @Mappings({
            @Mapping(target = "id", expression = "java(orderItem.getId().getValue())"),
            @Mapping(target = "productId", expression = "java(orderItem.getProductId().getValue())"),
            @Mapping(target = "price", source = "price.amount"),
            @Mapping(target = "currency", source = "price.currency"),
            @Mapping(target = "totalAmount", source = "totalPrice.amount")
    })
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
