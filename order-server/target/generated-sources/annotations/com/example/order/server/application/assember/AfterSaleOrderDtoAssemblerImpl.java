package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T16:47:34+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (GraalVM Community)"
)
@Component
public class AfterSaleOrderDtoAssemblerImpl implements AfterSaleOrderDtoAssembler {

    @Override
    public AfterSaleOrderResponse toAfterSaleOrderResponse(AfterSaleOrder afterSaleOrder) {
        if ( afterSaleOrder == null ) {
            return null;
        }

        AfterSaleOrderResponse afterSaleOrderResponse = new AfterSaleOrderResponse();

        afterSaleOrderResponse.setRefundAmount( afterSaleOrderTotalRefundAmountAmount( afterSaleOrder ) );
        afterSaleOrderResponse.setId( afterSaleOrder.getId() );
        afterSaleOrderResponse.setAfterSaleNo( afterSaleOrder.getAfterSaleNo() );
        afterSaleOrderResponse.setOrderNo( afterSaleOrder.getOrderNo() );
        afterSaleOrderResponse.setUserId( afterSaleOrder.getUserId() );
        if ( afterSaleOrder.getStatus() != null ) {
            afterSaleOrderResponse.setStatus( afterSaleOrder.getStatus().name() );
        }
        afterSaleOrderResponse.setReason( afterSaleOrder.getReason() );
        afterSaleOrderResponse.setDescription( afterSaleOrder.getDescription() );
        if ( afterSaleOrder.getCustomerServiceId() != null ) {
            afterSaleOrderResponse.setCustomerServiceId( String.valueOf( afterSaleOrder.getCustomerServiceId() ) );
        }
        afterSaleOrderResponse.setReverseLogisticsNo( afterSaleOrder.getReverseLogisticsNo() );
        afterSaleOrderResponse.setCreateTime( afterSaleOrder.getCreateTime() );
        afterSaleOrderResponse.setUpdateTime( afterSaleOrder.getUpdateTime() );

        return afterSaleOrderResponse;
    }

    private Long afterSaleOrderTotalRefundAmountAmount(AfterSaleOrder afterSaleOrder) {
        if ( afterSaleOrder == null ) {
            return null;
        }
        Price totalRefundAmount = afterSaleOrder.getTotalRefundAmount();
        if ( totalRefundAmount == null ) {
            return null;
        }
        Long amount = totalRefundAmount.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }
}
