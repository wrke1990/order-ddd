package com.example.order.domain.model.entity;

import com.example.order.domain.model.vo.Price;

import java.io.Serializable;

/**
 * 购物车项实体
 */
public class ShoppingCartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_QUANTITY = 99;

    private Long id;
    private String shoppingCartNo;
    private Long cartId;
    private Long userId;
    private final Long productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Price price;
    private boolean selected;
    private boolean available;
    private String unavailableReason;

    /**
     * 创建购物车项
     */
    public static ShoppingCartItem create(Long productId, String productName, String productImage,
                                         Integer quantity, Price price, boolean available) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (quantity == null || quantity <= 0 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("商品数量必须在1到" + MAX_QUANTITY + "之间");
        }
        if (price == null || price.getAmount() <= 0) {
            throw new IllegalArgumentException("商品价格必须大于0");
        }

        ShoppingCartItem item = new ShoppingCartItem(productId, productName, productImage,
                                                   quantity, price, available);
        return item;
    }

    /**
     * 私有构造函数，确保通过工厂方法创建
     */
    private ShoppingCartItem(Long productId, String productName, String productImage,
                           Integer quantity, Price price, boolean available) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
        this.selected = true; // 默认选中
        this.available = available;
    }

    /**
     * 增加数量
     */
    public void increaseQuantity(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("增加数量必须大于0");
        }
        if (this.quantity + amount > MAX_QUANTITY) {
            throw new IllegalArgumentException("商品数量不能超过" + MAX_QUANTITY);
        }
        this.quantity += amount;
    }

    /**
     * 减少数量
     */
    public void decreaseQuantity(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("减少数量必须大于0");
        }
        this.quantity -= amount;
    }

    /**
     * 更新商品信息（价格、可用性等）
     */
    public void updateProductInfo(Price newPrice, boolean newAvailable, String unavailableReason) {
        if (newPrice != null) {
            this.price = newPrice;
        }
        this.available = newAvailable;
        this.unavailableReason = unavailableReason;
        if (!newAvailable) {
            this.selected = false; // 下架商品自动取消选中
        }
    }

    /**
     * 更新商品基本信息（名称、图片、价格）
     */
    public void updateBasicInfo(String newProductName, String newProductImage, Long newPriceAmount) {
        if (newProductName != null && !newProductName.isEmpty()) {
            this.productName = newProductName;
        }
        if (newProductImage != null) {
            this.productImage = newProductImage;
        }
        if (newPriceAmount != null && newPriceAmount > 0) {
            this.price = new Price(newPriceAmount, this.price.getCurrency());
        }
    }

    /**
     * 切换选中状态
     */
    public void toggleSelected() {
        if (this.available) { // 只有可用商品可以切换选中状态
            this.selected = !this.selected;
        }
    }

    /**
     * 计算商品小计金额
     */
    public Price getSubtotal() {
        return new Price(this.price.getAmount() * this.quantity, this.price.getCurrency());
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getShoppingCartNo() {
        return shoppingCartNo;
    }

    public void setShoppingCartNo(String shoppingCartNo) {
        this.shoppingCartNo = shoppingCartNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.available) { // 只有可用商品可以设置选中状态
            this.selected = selected;
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public String getUnavailableReason() {
        return unavailableReason;
    }

    /**
     * 重建购物车项对象，用于从持久化数据重建领域对象
     */
    public static ShoppingCartItem reconstruct(
            Long id,
            String shoppingCartNo,
            Long cartId,
            Long userId,
            Long productId,
            String productName,
            String productImage,
            Integer quantity,
            Price price,
            boolean selected,
            boolean available,
            String unavailableReason) {
        ShoppingCartItem item = new ShoppingCartItem(productId, productName, productImage, quantity, price, available);
        item.id = id;
        item.shoppingCartNo = shoppingCartNo;
        item.cartId = cartId;
        item.userId = userId;
        item.selected = selected;
        item.unavailableReason = unavailableReason;
        return item;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", productName='" + productName + "'" +
                ", quantity=" + quantity +
                ", price=" + price +
                ", selected=" + selected +
                ", available=" + available +
                '}';
    }
}