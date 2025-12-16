package com.example.order.server.application.assember;

import com.example.order.api.vo.req.CreateOrderReq;
import com.example.order.api.vo.resp.OrderResp;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-16T10:32:52+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class OrderVoAssemblerImpl implements OrderVoAssembler {

    @Override
    public CreateOrderCommand toCreateOrderCommand(CreateOrderReq req) {
        if ( req == null ) {
            return null;
        }

        CreateOrderCommand createOrderCommand = new CreateOrderCommand();

        createOrderCommand.setUserId( req.getUserId() );
        createOrderCommand.setAddressId( req.getAddressId() );
        createOrderCommand.setPaymentMethodId( req.getPaymentMethodId() );
        createOrderCommand.setCouponId( req.getCouponId() );
        createOrderCommand.setItems( orderItemReqListToOrderItemCommandList( req.getItems() ) );

        return createOrderCommand;
    }

    @Override
    public CreateOrderCommand.OrderItemCommand toOrderItemCommand(CreateOrderReq.OrderItemReq itemReq) {
        if ( itemReq == null ) {
            return null;
        }

        CreateOrderCommand.OrderItemCommand orderItemCommand = new CreateOrderCommand.OrderItemCommand();

        orderItemCommand.setProductId( itemReq.getProductId() );
        orderItemCommand.setProductName( itemReq.getProductName() );
        orderItemCommand.setPrice( itemReq.getPrice() );
        orderItemCommand.setCurrency( itemReq.getCurrency() );
        orderItemCommand.setQuantity( itemReq.getQuantity() );

        return orderItemCommand;
    }

    @Override
    public OrderResp toOrderResponse(OrderResponse resp) {
        if ( resp == null ) {
            return null;
        }

        OrderResp orderResp = new OrderResp();

        orderResp.setId( resp.getId() );
        orderResp.setUserId( resp.getUserId() );
        orderResp.setOrderNo( resp.getOrderNo() );
        orderResp.setTotalAmount( resp.getTotalAmount() );
        orderResp.setCurrency( resp.getCurrency() );
        orderResp.setStatus( resp.getStatus() );
        orderResp.setItems( orderItemResponseListToOrderItemRespList( resp.getItems() ) );
        if ( resp.getCreateTime() != null ) {
            orderResp.setCreateTime( LocalDateTime.parse( resp.getCreateTime() ) );
        }
        if ( resp.getUpdateTime() != null ) {
            orderResp.setUpdateTime( LocalDateTime.parse( resp.getUpdateTime() ) );
        }

        return orderResp;
    }

    @Override
    public OrderResp.OrderItemResp toOrderItemResponse(OrderResponse.OrderItemResponse itemResp) {
        if ( itemResp == null ) {
            return null;
        }

        OrderResp.OrderItemResp orderItemResp = new OrderResp.OrderItemResp();

        orderItemResp.setId( itemResp.getId() );
        orderItemResp.setProductId( itemResp.getProductId() );
        orderItemResp.setProductName( itemResp.getProductName() );
        orderItemResp.setPrice( itemResp.getPrice() );
        orderItemResp.setCurrency( itemResp.getCurrency() );
        orderItemResp.setQuantity( itemResp.getQuantity() );
        orderItemResp.setTotalPrice( itemResp.getTotalAmount() );

        return orderItemResp;
    }

    protected List<CreateOrderCommand.OrderItemCommand> orderItemReqListToOrderItemCommandList(List<CreateOrderReq.OrderItemReq> list) {
        if ( list == null ) {
            return null;
        }

        List<CreateOrderCommand.OrderItemCommand> list1 = new ArrayList<CreateOrderCommand.OrderItemCommand>( list.size() );
        for ( CreateOrderReq.OrderItemReq orderItemReq : list ) {
            list1.add( toOrderItemCommand( orderItemReq ) );
        }

        return list1;
    }

    protected List<OrderResp.OrderItemResp> orderItemResponseListToOrderItemRespList(List<OrderResponse.OrderItemResponse> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResp.OrderItemResp> list1 = new ArrayList<OrderResp.OrderItemResp>( list.size() );
        for ( OrderResponse.OrderItemResponse orderItemResponse : list ) {
            list1.add( toOrderItemResponse( orderItemResponse ) );
        }

        return list1;
    }
}
