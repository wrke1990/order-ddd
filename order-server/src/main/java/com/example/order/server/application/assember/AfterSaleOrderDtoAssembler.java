package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后订单DTO映射器
 */
@Mapper(componentModel = "spring")
public interface AfterSaleOrderDtoAssembler {

    AfterSaleOrderDtoAssembler INSTANCE = Mappers.getMapper(AfterSaleOrderDtoAssembler.class);

    /**
     * 售后订单命令转领域实体
     */
    default AfterSaleOrder toAfterSaleOrder(CreateAfterSaleOrderCommand command) {
        if (command == null) {
            return null;
        }

        // 这里需要注意：afterSaleNo应该由领域服务生成，orderId可能需要通过orderNo查询获取
        // 暂时使用临时值，实际应该由上层服务提供完整信息
        String afterSaleNo = "TEMP-" + System.currentTimeMillis();
        Long orderId = null; // 实际应用中应该通过orderNo查询获取

        // 创建售后商品项列表
        List<AfterSaleItem> afterSaleItems = new ArrayList<>();
        AfterSaleItem item = AfterSaleItem.create(
                command.getProductId(),
                command.getProductName(),
                command.getProductImage(),
                command.getQuantity(),
                new Price(command.getApplyAmount(), command.getCurrency()),
                command.getQuantity() // 使用全部数量作为退款数量
        );
        afterSaleItems.add(item);

        // 使用工厂方法创建售后订单
        return AfterSaleOrder.create(
                afterSaleNo,
                orderId,
                command.getOrderNo(),
                command.getUserId(),
                AfterSaleType.valueOf(command.getAfterSaleType()),
                command.getReason(),
                command.getDescription(),
                null, // 图片暂时为null
                afterSaleItems,
                false // 不是管理员发起
        );
    }

    /**
     * 领域售后订单转响应DTO
     */
    @Mappings({
            @Mapping(target = "refundAmount", source = "totalRefundAmount.amount")
    })
    AfterSaleOrderResponse toAfterSaleOrderResponse(AfterSaleOrder afterSaleOrder);

    /**
     * 自定义映射方法：设置申请金额和货币
     */
    default void customMapping(AfterSaleOrder afterSaleOrder, AfterSaleOrderResponse response) {
        if (afterSaleOrder != null && afterSaleOrder.getAfterSaleItems() != null && !afterSaleOrder.getAfterSaleItems().isEmpty()) {
            AfterSaleItem firstItem = afterSaleOrder.getAfterSaleItems().get(0);
            if (firstItem != null && firstItem.getProductPrice() != null) {
                response.setApplyAmount(firstItem.getProductPrice().getAmount());
                response.setCurrency(firstItem.getProductPrice().getCurrency());
            }
        }
    }
}
