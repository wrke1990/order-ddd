package com.example.order.server.application.assember;

import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.resp.AfterSaleResp;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T16:47:34+0800",
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
        createAfterSaleOrderCommand.setProductId( req.getProductId() );
        createAfterSaleOrderCommand.setProductName( req.getProductName() );
        createAfterSaleOrderCommand.setProductImage( req.getProductImage() );
        createAfterSaleOrderCommand.setQuantity( req.getQuantity() );
        createAfterSaleOrderCommand.setApplyAmount( req.getApplyAmount() );
        createAfterSaleOrderCommand.setCurrency( req.getCurrency() );
        createAfterSaleOrderCommand.setAfterSaleType( req.getAfterSaleType() );
        createAfterSaleOrderCommand.setReason( req.getReason() );
        createAfterSaleOrderCommand.setDescription( req.getDescription() );

        return createAfterSaleOrderCommand;
    }

    @Override
    public AfterSaleResp toAfterSaleResponse(AfterSaleOrderResponse resp) {
        if ( resp == null ) {
            return null;
        }

        AfterSaleResp afterSaleResp = new AfterSaleResp();

        afterSaleResp.setApplyAmount( resp.getApplyAmount() );
        afterSaleResp.setRefundAmount( resp.getRefundAmount() );
        afterSaleResp.setCurrency( resp.getCurrency() );
        afterSaleResp.setId( resp.getId() );
        afterSaleResp.setAfterSaleNo( resp.getAfterSaleNo() );
        afterSaleResp.setOrderNo( resp.getOrderNo() );
        afterSaleResp.setUserId( resp.getUserId() );
        afterSaleResp.setProductId( resp.getProductId() );
        afterSaleResp.setProductName( resp.getProductName() );
        afterSaleResp.setProductImage( resp.getProductImage() );
        afterSaleResp.setQuantity( resp.getQuantity() );
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
}
