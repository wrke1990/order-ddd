package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.OrderResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-16T10:32:53+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class OrderDtoAssemblerImpl implements OrderDtoAssembler {

    @Override
    public OrderResponse toOrderResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setTotalAmount( orderTotalAmountAmount( order ) );
        orderResponse.setCurrency( orderTotalAmountCurrency( order ) );
        orderResponse.setItems( orderItemListToOrderItemResponseList( order.getOrderItems() ) );
        orderResponse.setOrderNo( order.getOrderNo() );
        if ( order.getStatus() != null ) {
            orderResponse.setStatus( order.getStatus().name() );
        }

        orderResponse.setId( order.getId().getValue() );
        orderResponse.setUserId( order.getUserId().getValue() );
        orderResponse.setCreateTime( order.getCreateTime().toString() );
        orderResponse.setUpdateTime( order.getUpdateTime().toString() );

        return orderResponse;
    }

    @Override
    public OrderResponse.OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderResponse.OrderItemResponse orderItemResponse = new OrderResponse.OrderItemResponse();

        orderItemResponse.setPrice( orderItemPriceAmount( orderItem ) );
        orderItemResponse.setCurrency( orderItemPriceCurrency( orderItem ) );
        orderItemResponse.setTotalAmount( orderItemTotalPriceAmount( orderItem ) );
        orderItemResponse.setProductName( orderItem.getProductName() );
        orderItemResponse.setQuantity( orderItem.getQuantity() );

        orderItemResponse.setId( orderItem.getId().getValue() );
        orderItemResponse.setProductId( orderItem.getProductId().getValue() );

        return orderItemResponse;
    }

    private Long orderTotalAmountAmount(Order order) {
        if ( order == null ) {
            return null;
        }
        Price totalAmount = order.getTotalAmount();
        if ( totalAmount == null ) {
            return null;
        }
        Long amount = totalAmount.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }

    private String orderTotalAmountCurrency(Order order) {
        if ( order == null ) {
            return null;
        }
        Price totalAmount = order.getTotalAmount();
        if ( totalAmount == null ) {
            return null;
        }
        String currency = totalAmount.getCurrency();
        if ( currency == null ) {
            return null;
        }
        return currency;
    }

    protected List<OrderResponse.OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResponse.OrderItemResponse> list1 = new ArrayList<OrderResponse.OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toOrderItemResponse( orderItem ) );
        }

        return list1;
    }

    private Long orderItemPriceAmount(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Price price = orderItem.getPrice();
        if ( price == null ) {
            return null;
        }
        Long amount = price.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }

    private String orderItemPriceCurrency(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Price price = orderItem.getPrice();
        if ( price == null ) {
            return null;
        }
        String currency = price.getCurrency();
        if ( currency == null ) {
            return null;
        }
        return currency;
    }

    private Long orderItemTotalPriceAmount(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Price totalPrice = orderItem.getTotalPrice();
        if ( totalPrice == null ) {
            return null;
        }
        Long amount = totalPrice.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }
}
