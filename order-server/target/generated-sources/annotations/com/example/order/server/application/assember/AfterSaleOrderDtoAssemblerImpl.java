package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.AfterSaleItemResponse;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-19T17:54:38+0800",
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
        afterSaleOrderResponse.setAfterSaleItems( afterSaleItemListToAfterSaleItemResponseList( afterSaleOrder.getAfterSaleItems() ) );
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

    protected List<AfterSaleItemResponse> afterSaleItemListToAfterSaleItemResponseList(List<AfterSaleItem> list) {
        if ( list == null ) {
            return null;
        }

        List<AfterSaleItemResponse> list1 = new ArrayList<AfterSaleItemResponse>( list.size() );
        for ( AfterSaleItem afterSaleItem : list ) {
            list1.add( toAfterSaleItemResponse( afterSaleItem ) );
        }

        return list1;
    }
}
