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
        shoppingCartPO.setUserId(shoppingCart.getUserId());
        shoppingCartPO.setCreateTime(shoppingCart.getCreateTime());
        shoppingCartPO.setUpdateTime(shoppingCart.getUpdateTime());
        shoppingCartPO.setVersion(shoppingCart.getVersion());

        // 转换购物车项
        List<ShoppingCartItemPO> items = new ArrayList<>();
        if (shoppingCart.getItems() != null) {
            items = shoppingCart.getItems().stream()
                    .map(this::toShoppingCartItemPO)
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

        // 使用工厂方法创建购物车
        ShoppingCart shoppingCart = ShoppingCart.create(shoppingCartPO.getUserId(), items);

        // 设置创建和更新时间
        try {
            java.lang.reflect.Field createTimeField = ShoppingCart.class.getDeclaredField("createTime");
            createTimeField.setAccessible(true);
            createTimeField.set(shoppingCart, shoppingCartPO.getCreateTime());

            java.lang.reflect.Field updateTimeField = ShoppingCart.class.getDeclaredField("updateTime");
            updateTimeField.setAccessible(true);
            updateTimeField.set(shoppingCart, shoppingCartPO.getUpdateTime());

            java.lang.reflect.Field versionField = ShoppingCart.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(shoppingCart, shoppingCartPO.getVersion());

            return shoppingCart;
        } catch (Exception e) {
            throw new RuntimeException("转换购物车PO为领域对象失败", e);
        }
    }

    /**
     * 购物车项领域对象转PO
     */
    public ShoppingCartItemPO toShoppingCartItemPO(ShoppingCartItem shoppingCartItem) {
        if (shoppingCartItem == null) {
            return null;
        }

        ShoppingCartItemPO itemPO = new ShoppingCartItemPO();
        itemPO.setId(shoppingCartItem.getId());
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

        // 使用工厂方法创建购物车项
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.create(
                itemPO.getProductId(),
                itemPO.getProductName(),
                itemPO.getProductImage(),
                itemPO.getQuantity(),
                new Price(itemPO.getPrice(), itemPO.getCurrency()),
                true  // 默认商品可用
        );

        // 设置ID
        try {
            java.lang.reflect.Field idField = ShoppingCartItem.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(shoppingCartItem, itemPO.getId());

            return shoppingCartItem;
        } catch (Exception e) {
            throw new RuntimeException("转换购物车项PO为领域对象失败", e);
        }
    }
}
