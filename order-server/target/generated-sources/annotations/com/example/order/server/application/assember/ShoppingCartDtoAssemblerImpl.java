package com.example.order.server.application.assember;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.entity.ShoppingCartItem;
import com.example.order.domain.model.vo.Price;
import com.example.order.server.application.dto.ShoppingCartResponse;
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
public class ShoppingCartDtoAssemblerImpl implements ShoppingCartDtoAssembler {

    @Override
    public ShoppingCartResponse toShoppingCartResponse(ShoppingCart shoppingCart) {
        if ( shoppingCart == null ) {
            return null;
        }

        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();

        shoppingCartResponse.setItems( shoppingCartItemListToItemResponseList( shoppingCart.getItems() ) );
        shoppingCartResponse.setUserId( shoppingCart.getUserId() );

        shoppingCartResponse.setTotalAmount( shoppingCart.getItems().stream().mapToLong(item -> item.getSubtotal().getAmount()).sum() );
        shoppingCartResponse.setCurrency( shoppingCart.getItems().isEmpty() ? null : shoppingCart.getItems().get(0).getPrice().getCurrency() );
        shoppingCartResponse.setItemCount( shoppingCart.getItems().size() );

        return shoppingCartResponse;
    }

    @Override
    public ShoppingCartResponse.ItemResponse toShoppingCartItemResponse(ShoppingCartItem item) {
        if ( item == null ) {
            return null;
        }

        ShoppingCartResponse.ItemResponse itemResponse = new ShoppingCartResponse.ItemResponse();

        itemResponse.setPrice( itemPriceAmount( item ) );
        itemResponse.setCurrency( itemPriceCurrency( item ) );
        itemResponse.setTotalPrice( itemSubtotalAmount( item ) );
        itemResponse.setId( item.getId() );
        itemResponse.setProductId( item.getProductId() );
        itemResponse.setProductName( item.getProductName() );
        itemResponse.setProductImage( item.getProductImage() );
        itemResponse.setQuantity( item.getQuantity() );
        itemResponse.setSelected( item.isSelected() );

        return itemResponse;
    }

    protected List<ShoppingCartResponse.ItemResponse> shoppingCartItemListToItemResponseList(List<ShoppingCartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<ShoppingCartResponse.ItemResponse> list1 = new ArrayList<ShoppingCartResponse.ItemResponse>( list.size() );
        for ( ShoppingCartItem shoppingCartItem : list ) {
            list1.add( toShoppingCartItemResponse( shoppingCartItem ) );
        }

        return list1;
    }

    private Long itemPriceAmount(ShoppingCartItem shoppingCartItem) {
        if ( shoppingCartItem == null ) {
            return null;
        }
        Price price = shoppingCartItem.getPrice();
        if ( price == null ) {
            return null;
        }
        Long amount = price.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }

    private String itemPriceCurrency(ShoppingCartItem shoppingCartItem) {
        if ( shoppingCartItem == null ) {
            return null;
        }
        Price price = shoppingCartItem.getPrice();
        if ( price == null ) {
            return null;
        }
        String currency = price.getCurrency();
        if ( currency == null ) {
            return null;
        }
        return currency;
    }

    private Long itemSubtotalAmount(ShoppingCartItem shoppingCartItem) {
        if ( shoppingCartItem == null ) {
            return null;
        }
        Price subtotal = shoppingCartItem.getSubtotal();
        if ( subtotal == null ) {
            return null;
        }
        Long amount = subtotal.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }
}
