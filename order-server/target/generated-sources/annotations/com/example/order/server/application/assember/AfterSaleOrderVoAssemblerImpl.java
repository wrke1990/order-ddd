package com.example.order.server.application.assember;

import com.example.order.api.vo.req.AfterSaleItemReq;
import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.resp.AfterSaleItemResp;
import com.example.order.api.vo.resp.AfterSaleResp;
import com.example.order.server.application.dto.AfterSaleItemRequest;
import com.example.order.server.application.dto.AfterSaleItemResponse;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
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
public class AfterSaleOrderVoAssemblerImpl implements AfterSaleOrderVoAssembler {

    @Override
    public CreateAfterSaleOrderCommand toCreateAfterSaleOrderCommand(CreateAfterSaleReq req) {
        if ( req == null ) {
            return null;
        }

        CreateAfterSaleOrderCommand createAfterSaleOrderCommand = new CreateAfterSaleOrderCommand();

        createAfterSaleOrderCommand.setUserId( req.getUserId() );
        createAfterSaleOrderCommand.setOrderNo( req.getOrderNo() );
        createAfterSaleOrderCommand.setAfterSaleType( req.getAfterSaleType() );
        createAfterSaleOrderCommand.setReason( req.getReason() );
        createAfterSaleOrderCommand.setDescription( req.getDescription() );
        createAfterSaleOrderCommand.setAfterSaleItems( afterSaleItemReqListToAfterSaleItemRequestList( req.getAfterSaleItems() ) );

        return createAfterSaleOrderCommand;
    }

    @Override
    public AfterSaleItemRequest toAfterSaleItemRequest(AfterSaleItemReq req) {
        if ( req == null ) {
            return null;
        }

        AfterSaleItemRequest afterSaleItemRequest = new AfterSaleItemRequest();

        afterSaleItemRequest.setProductId( req.getProductId() );
        afterSaleItemRequest.setProductName( req.getProductName() );
        afterSaleItemRequest.setProductImage( req.getProductImage() );
        afterSaleItemRequest.setQuantity( req.getQuantity() );
        afterSaleItemRequest.setApplyAmount( req.getApplyAmount() );
        afterSaleItemRequest.setCurrency( req.getCurrency() );

        return afterSaleItemRequest;
    }

    @Override
    public AfterSaleItemResp toAfterSaleItemResp(AfterSaleItemResponse itemResponse) {
        if ( itemResponse == null ) {
            return null;
        }

        AfterSaleItemResp afterSaleItemResp = new AfterSaleItemResp();

        afterSaleItemResp.setRefundAmount( itemResponse.getApplyAmount() );
        afterSaleItemResp.setProductId( itemResponse.getProductId() );
        afterSaleItemResp.setProductName( itemResponse.getProductName() );
        afterSaleItemResp.setProductImage( itemResponse.getProductImage() );
        afterSaleItemResp.setQuantity( itemResponse.getQuantity() );
        afterSaleItemResp.setApplyAmount( itemResponse.getApplyAmount() );
        afterSaleItemResp.setCurrency( itemResponse.getCurrency() );

        return afterSaleItemResp;
    }

    @Override
    public AfterSaleResp toAfterSaleResponse(AfterSaleOrderResponse resp) {
        if ( resp == null ) {
            return null;
        }

        AfterSaleResp afterSaleResp = new AfterSaleResp();

        afterSaleResp.setRefundAmount( resp.getRefundAmount() );
        afterSaleResp.setAfterSaleItems( afterSaleItemResponseListToAfterSaleItemRespList( resp.getAfterSaleItems() ) );
        afterSaleResp.setId( resp.getId() );
        afterSaleResp.setAfterSaleNo( resp.getAfterSaleNo() );
        afterSaleResp.setOrderNo( resp.getOrderNo() );
        afterSaleResp.setUserId( resp.getUserId() );
        afterSaleResp.setAfterSaleType( resp.getAfterSaleType() );
        afterSaleResp.setStatus( resp.getStatus() );
        afterSaleResp.setReason( resp.getReason() );
        afterSaleResp.setDescription( resp.getDescription() );
        afterSaleResp.setCustomerRemark( resp.getCustomerRemark() );
        afterSaleResp.setCustomerServiceId( resp.getCustomerServiceId() );
        afterSaleResp.setReverseLogisticsNo( resp.getReverseLogisticsNo() );
        afterSaleResp.setCreateTime( resp.getCreateTime() );
        afterSaleResp.setUpdateTime( resp.getUpdateTime() );

        return afterSaleResp;
    }

    protected List<AfterSaleItemRequest> afterSaleItemReqListToAfterSaleItemRequestList(List<AfterSaleItemReq> list) {
        if ( list == null ) {
            return null;
        }

        List<AfterSaleItemRequest> list1 = new ArrayList<AfterSaleItemRequest>( list.size() );
        for ( AfterSaleItemReq afterSaleItemReq : list ) {
            list1.add( toAfterSaleItemRequest( afterSaleItemReq ) );
        }

        return list1;
    }

    protected List<AfterSaleItemResp> afterSaleItemResponseListToAfterSaleItemRespList(List<AfterSaleItemResponse> list) {
        if ( list == null ) {
            return null;
        }

        List<AfterSaleItemResp> list1 = new ArrayList<AfterSaleItemResp>( list.size() );
        for ( AfterSaleItemResponse afterSaleItemResponse : list ) {
            list1.add( toAfterSaleItemResp( afterSaleItemResponse ) );
        }

        return list1;
    }
}
