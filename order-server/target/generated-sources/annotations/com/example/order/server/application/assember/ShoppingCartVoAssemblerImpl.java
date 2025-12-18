package com.example.order.server.application.assember;

import com.example.order.api.vo.req.ShoppingCartReq;
import com.example.order.api.vo.resp.ShoppingCartResp;
import com.example.order.server.application.dto.ShoppingCartCommand;
import com.example.order.server.application.dto.ShoppingCartResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T16:47:34+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (GraalVM Community)"
)
@Component
public class ShoppingCartVoAssemblerImpl implements ShoppingCartVoAssembler {

    @Override
    public ShoppingCartCommand toShoppingCartCommand(ShoppingCartReq req) {
        if ( req == null ) {
            return null;
        }

        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();

        shoppingCartCommand.setUserId( req.getUserId() );
        shoppingCartCommand.setItem( toShoppingCartItemCommand( req.getItem() ) );

        return shoppingCartCommand;
    }

    @Override
    public ShoppingCartCommand.ItemCommand toShoppingCartItemCommand(ShoppingCartReq.ItemReq itemReq) {
        if ( itemReq == null ) {
            return null;
        }

        ShoppingCartCommand.ItemCommand itemCommand = new ShoppingCartCommand.ItemCommand();

        itemCommand.setProductId( itemReq.getProductId() );
        itemCommand.setProductName( itemReq.getProductName() );
        itemCommand.setProductImage( itemReq.getProductImage() );
        itemCommand.setPrice( itemReq.getPrice() );
        itemCommand.setCurrency( itemReq.getCurrency() );
        itemCommand.setQuantity( itemReq.getQuantity() );
        itemCommand.setSelected( itemReq.getSelected() );

        return itemCommand;
    }

    @Override
    public ShoppingCartResp toShoppingCartResponse(ShoppingCartResponse resp) {
        if ( resp == null ) {
            return null;
        }

        ShoppingCartResp shoppingCartResp = new ShoppingCartResp();

        shoppingCartResp.setItems( itemResponseListToShoppingCartItemRespList( resp.getItems() ) );
        shoppingCartResp.setUserId( resp.getUserId() );

        return shoppingCartResp;
    }

    @Override
    public ShoppingCartResp.ShoppingCartItemResp toShoppingCartItemResponse(ShoppingCartResponse.ItemResponse itemResp) {
        if ( itemResp == null ) {
            return null;
        }

        ShoppingCartResp.ShoppingCartItemResp shoppingCartItemResp = new ShoppingCartResp.ShoppingCartItemResp();

        shoppingCartItemResp.setId( itemResp.getId() );
        shoppingCartItemResp.setProductId( itemResp.getProductId() );
        shoppingCartItemResp.setProductName( itemResp.getProductName() );
        shoppingCartItemResp.setProductImage( itemResp.getProductImage() );
        if ( itemResp.getPrice() != null ) {
            shoppingCartItemResp.setPrice( itemResp.getPrice().intValue() );
        }
        shoppingCartItemResp.setCurrency( itemResp.getCurrency() );
        shoppingCartItemResp.setQuantity( itemResp.getQuantity() );

        return shoppingCartItemResp;
    }

    protected List<ShoppingCartResp.ShoppingCartItemResp> itemResponseListToShoppingCartItemRespList(List<ShoppingCartResponse.ItemResponse> list) {
        if ( list == null ) {
            return null;
        }

        List<ShoppingCartResp.ShoppingCartItemResp> list1 = new ArrayList<ShoppingCartResp.ShoppingCartItemResp>( list.size() );
        for ( ShoppingCartResponse.ItemResponse itemResponse : list ) {
            list1.add( toShoppingCartItemResponse( itemResponse ) );
        }

        return list1;
    }
}
