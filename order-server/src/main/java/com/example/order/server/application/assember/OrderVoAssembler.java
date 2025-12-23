package com.example.order.server.application.assember;

import com.example.order.api.vo.req.CreateOrderReq;
import com.example.order.api.vo.req.OrderItemReq;
import com.example.order.api.vo.resp.OrderItemResp;
import com.example.order.api.vo.resp.OrderResp;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderItemCommand;
import com.example.order.server.application.dto.OrderItemResponse;
import com.example.order.server.application.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 订单VO与应用服务DTO转换器
 */
@Mapper(componentModel = "spring")
public interface OrderVoAssembler {

    /**
     * API请求VO转应用服务DTO
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "addressId", source = "addressId")
    @Mapping(target = "paymentMethodId", source = "paymentMethodId")
    @Mapping(target = "couponId", source = "couponId")
    @Mapping(target = "items", source = "items")
    CreateOrderCommand toCreateOrderCommand(CreateOrderReq req);

    /**
     * API请求项VO转应用服务DTO
     */
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemCommand toOrderItemCommand(OrderItemReq itemReq);

    /**
     * 应用服务响应DTO转API响应VO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "orderNo", source = "orderNo")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createTime", source = "createTime")
    @Mapping(target = "updateTime", source = "updateTime")
    OrderResp toOrderResponse(OrderResponse resp);

    /**
     * 应用服务响应项DTO转API响应VO
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "productName", source = "productName"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "totalPrice", source = "totalAmount")
    })
    OrderItemResp toOrderItemResponse(OrderItemResponse itemResp);
}