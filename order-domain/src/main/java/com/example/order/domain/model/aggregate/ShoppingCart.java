package com.example.order.domain.model.aggregate;

import com.example.order.domain.model.entity.ShoppingCartItem;
import com.example.order.domain.model.vo.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 购物车聚合根
 */
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private final Long userId;
    private final List<ShoppingCartItem> items;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer version = 0;

    /**
     * 创建购物车
     */
    public static ShoppingCart create(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        ShoppingCart shoppingCart = new ShoppingCart(userId);
        return shoppingCart;
    }

    /**
     * 创建购物车（带初始商品项）
     */
    public static ShoppingCart create(Long userId, List<ShoppingCartItem> items) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        ShoppingCart shoppingCart = new ShoppingCart(userId);
        if (items != null && !items.isEmpty()) {
            shoppingCart.items.addAll(items);
        }
        return shoppingCart;
    }

    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private ShoppingCart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.version = 0;
    }

    /**
     * 添加商品到购物车
     */
    public void addItem(ShoppingCartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("购物车项不能为空");
        }

        // 检查是否已存在该商品
        Optional<ShoppingCartItem> existingItem = items.stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // 已存在则增加数量
            existingItem.get().increaseQuantity(item.getQuantity());
        } else {
            // 不存在则添加新项
            items.add(item);
        }

        this.updateTime = LocalDateTime.now();
    }

    /**
     * 删除单个商品
     */
    public void removeItem(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }

        items.removeIf(item -> item.getProductId().equals(productId));
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 删除单个商品（使用Id值对象）
     */
    public void removeItem(Id productId) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }

        removeItem(productId.getValue());
    }

    /**
     * 批量删除商品（使用Id值对象列表）
     */
    public void removeItemsByProductIds(List<Id> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return; // 没有需要删除的商品，直接返回
        }

        // 提取商品ID值列表
        List<Long> productIdValues = productIds.stream()
                .filter(Objects::nonNull)
                .map(Id::getValue)
                .toList();

        // 批量删除商品
        items.removeIf(item -> productIdValues.contains(item.getProductId()));
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 清空购物车
     */
    public void clear() {
        items.clear();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 清空所有下架商品
     */
    public void clearUnavailableItems() {
        items.removeIf(item -> !item.isAvailable());
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 增加商品数量
     */
    public void increaseItemQuantity(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("商品ID和数量必须大于0");
        }

        Optional<ShoppingCartItem> existingItem = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity);
            this.updateTime = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("商品不存在于购物车中");
        }
    }

    /**
     * 减少商品数量
     */
    public void decreaseItemQuantity(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("商品ID和数量必须大于0");
        }

        Optional<ShoppingCartItem> existingItem = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartItem item = existingItem.get();
            item.decreaseQuantity(quantity);

            // 如果数量为0，则删除该项
            if (item.getQuantity() <= 0) {
                items.remove(item);
            }

            this.updateTime = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("商品不存在于购物车中");
        }
    }

    /**
     * 获取购物车中选中的商品（用于下单）
     */
    public List<ShoppingCartItem> getSelectedItems() {
        return items.stream()
                .filter(ShoppingCartItem::isSelected)
                .collect(java.util.stream.Collectors.toList());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<ShoppingCartItem> getItems() {
        return new ArrayList<>(items); // 返回副本，避免外部修改
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                '}';
    }
}