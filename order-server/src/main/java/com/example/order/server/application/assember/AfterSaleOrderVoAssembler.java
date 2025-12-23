package com.example.order.server.application.assember;

import com.example.order.api.vo.req.AfterSaleItemReq;
import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.resp.AfterSaleItemResp;
import com.example.order.api.vo.resp.AfterSaleResp;
import com.example.order.server.application.dto.AfterSaleItemRequest;
import com.example.order.server.application.dto.AfterSaleItemResponse;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 售后订单VO转换器
 */
@Mapper(componentModel = "spring")
public interface AfterSaleOrderVoAssembler {

    AfterSaleOrderVoAssembler INSTANCE = Mappers.getMapper(AfterSaleOrderVoAssembler.class);

    /**
     * API请求VO转应用服务命令
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "orderNo", source = "orderNo")
    @Mapping(target = "afterSaleType", source = "afterSaleType")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "afterSaleItems", source = "afterSaleItems")
    CreateAfterSaleOrderCommand toCreateAfterSaleOrderCommand(CreateAfterSaleReq req);

    /**
     * 将AfterSaleItemReq转换为AfterSaleItemRequest
     */
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "productImage", source = "productImage")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "applyAmount", source = "applyAmount")
    @Mapping(target = "currency", source = "currency")
    AfterSaleItemRequest toAfterSaleItemRequest(AfterSaleItemReq req);

    /**
     * 将AfterSaleItemResponse转换为AfterSaleItemResp
     */
    @Mapping(target = "refundAmount", source = "applyAmount")
    AfterSaleItemResp toAfterSaleItemResp(AfterSaleItemResponse itemResponse);

    /**
     * 应用服务响应DTO转API响应VO
     */
    @Mappings({
            @Mapping(target = "refundAmount", source = "refundAmount"),
            @Mapping(target = "afterSaleItems", source = "afterSaleItems")
    })
    AfterSaleResp toAfterSaleResponse(AfterSaleOrderResponse resp);
}