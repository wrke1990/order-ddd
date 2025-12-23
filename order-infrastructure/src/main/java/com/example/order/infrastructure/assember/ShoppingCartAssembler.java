package com.example.order.infrastructure.assember;

import com.example.order.domain.model.aggregate.ShoppingCart;
import com.example.order.domain.model.entity.ShoppingCartItem;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.persistence.po.ShoppingCartItemPO;
import com.example.order.infrastructure.persistence.po.ShoppingCartPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车对象映射器
 */
@Component
public class ShoppingCartAssembler {

    /**
     * 购物车领域对象转PO
     */
    public ShoppingCartPO toShoppingCartPO(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }

        ShoppingCartPO shoppingCartPO = new ShoppingCartPO();
        shoppingCartPO.setShoppingCartNo(shoppingCart.getShoppingCartNo());
        shoppingCartPO.setUserId(shoppingCart.getUserId());
        shoppingCartPO.setCreateTime(shoppingCart.getCreateTime());
        shoppingCartPO.setUpdateTime(shoppingCart.getUpdateTime());
        shoppingCartPO.setVersion(shoppingCart.getVersion());

        // 转换购物车项
        List<ShoppingCartItemPO> items = new ArrayList<>();
        if (shoppingCart.getItems() != null) {
            items = shoppingCart.getItems().stream()
                    .map(item -> toShoppingCartItemPO(item, shoppingCart.getShoppingCartNo()))
                    .collect(Collectors.toList());
        }
        shoppingCartPO.setItems(items);

        return shoppingCartPO;
    }

    /**
     * 购物车PO转领域对象
     */
    public ShoppingCart toShoppingCart(ShoppingCartPO shoppingCartPO) {
        if (shoppingCartPO == null) {
            return null;
        }

        // 转换购物车项
        List<ShoppingCartItem> items = new ArrayList<>();
        if (shoppingCartPO.getItems() != null) {
            items = shoppingCartPO.getItems().stream()
                    .map(this::toShoppingCartItem)
                    .collect(Collectors.toList());
        }

        // 使用reconstruct方法创建购物车对象，替代反射
        return ShoppingCart.reconstruct(
                shoppingCartPO.getId(),
                shoppingCartPO.getShoppingCartNo(),
                shoppingCartPO.getUserId(),
                items,
                shoppingCartPO.getCreateTime(),
                shoppingCartPO.getUpdateTime(),
                shoppingCartPO.getVersion()
        );
    }

    /**
     * 购物车项领域对象转PO
     */
    public ShoppingCartItemPO toShoppingCartItemPO(ShoppingCartItem shoppingCartItem, String shoppingCartNo) {
        if (shoppingCartItem == null) {
            return null;
        }

        ShoppingCartItemPO itemPO = new ShoppingCartItemPO();
        itemPO.setId(shoppingCartItem.getId());
        itemPO.setShoppingCartNo(shoppingCartNo);
        itemPO.setUserId(shoppingCartItem.getUserId());
        itemPO.setProductId(shoppingCartItem.getProductId());
        itemPO.setProductName(shoppingCartItem.getProductName());
        itemPO.setProductImage(shoppingCartItem.getProductImage());
        itemPO.setPrice(shoppingCartItem.getPrice().getAmount());
        itemPO.setCurrency(shoppingCartItem.getPrice().getCurrency());
        itemPO.setQuantity(shoppingCartItem.getQuantity());
        itemPO.setSelected(shoppingCartItem.isSelected());

        return itemPO;
    }

    /**
     * 购物车项PO转领域对象
     */
    public ShoppingCartItem toShoppingCartItem(ShoppingCartItemPO itemPO) {
        if (itemPO == null) {
            return null;
        }

        // 使用reconstruct方法创建购物车项对象，替代反射
        return ShoppingCartItem.reconstruct(
                itemPO.getId(),
                itemPO.getShoppingCartNo(),
                null, // cartId, 从PO中无法获取
                itemPO.getUserId(),
                itemPO.getProductId(),
                itemPO.getProductName(),
                itemPO.getProductImage(),
                itemPO.getQuantity(),
                new Price(itemPO.getPrice(), itemPO.getCurrency()),
                itemPO.getSelected() != null ? itemPO.getSelected() : true, // 使用getSelected()方法并处理null值
                true, // 默认商品可用
                null  // 不可用原因，从PO中无法获取
        );
    }
}
