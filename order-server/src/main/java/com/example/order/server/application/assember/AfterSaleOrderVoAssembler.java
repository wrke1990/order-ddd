package com.example.order.server.application.assember;

import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.resp.AfterSaleResp;
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
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "productImage", source = "productImage")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "applyAmount", source = "applyAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "afterSaleType", source = "afterSaleType")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "description", source = "description")
    CreateAfterSaleOrderCommand toCreateAfterSaleOrderCommand(CreateAfterSaleReq req);

    /**
     * 应用服务响应DTO转API响应VO
     */
    @Mappings({
            @Mapping(target = "applyAmount", source = "applyAmount"),
            @Mapping(target = "refundAmount", source = "refundAmount"),
            @Mapping(target = "currency", source = "currency")
    })
    AfterSaleResp toAfterSaleResponse(AfterSaleOrderResponse resp);
}