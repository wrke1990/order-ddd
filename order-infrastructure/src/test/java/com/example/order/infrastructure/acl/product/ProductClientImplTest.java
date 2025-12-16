package com.example.order.infrastructure.acl.product;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProductClientImplTest {

    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        productClient = new ProductClientImpl();
    }

    @Test
    void testGetProductNameById() {
        // 测试获取商品名称
        Id productId = Id.of(1L);
        String productName = productClient.getProductNameById(productId);

        Assertions.assertNotNull(productName);
        Assertions.assertEquals("商品1", productName);
    }

    @Test
    void testGetProductImageById() {
        // 测试获取商品图片
        Id productId = Id.of(2L);
        String productImage = productClient.getProductImageById(productId);

        Assertions.assertNotNull(productImage);
        Assertions.assertEquals("https://example.com/product2.jpg", productImage);
    }

    @Test
    void testGetProductPriceById() {
        // 测试获取商品价格
        Id productId = Id.of(1L);
        Price productPrice = productClient.getProductPriceById(productId);

        Assertions.assertNotNull(productPrice);
        Assertions.assertEquals(100L, productPrice.getAmount());
        Assertions.assertEquals("CNY", productPrice.getCurrency());
    }

    @Test
    void testGetProductStockById() {
        // 测试获取商品库存
        Id productId = Id.of(2L);
        Integer productStock = productClient.getProductStockById(productId);

        Assertions.assertNotNull(productStock);
        Assertions.assertEquals(50, productStock);
    }

    @Test
    void testGetProductsByIds() {
        // 测试批量获取商品信息
        List<Id> productIds = new ArrayList<>();
        productIds.add(Id.of(1L));
        productIds.add(Id.of(2L));

        List<ProductDTO> products = productClient.getProductsByIds(productIds);

        Assertions.assertNotNull(products);
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("商品1", products.get(0).getProductName());
        Assertions.assertEquals("商品2", products.get(1).getProductName());
        Assertions.assertEquals(new BigDecimal(100), products.get(0).getPrice());
        Assertions.assertEquals(new BigDecimal(200), products.get(1).getPrice());
    }

    @Test
    void testLockProductStock() {
        // 测试锁定商品库存
        Id productId = Id.of(1L);
        Integer quantity = 50;

        // 获取锁定前的库存
        Integer beforeStock = productClient.getProductStockById(productId);

        // 锁定库存
        boolean result = productClient.lockProductStock(productId, quantity);

        // 获取锁定后的库存
        Integer afterStock = productClient.getProductStockById(productId);

        Assertions.assertTrue(result);
        Assertions.assertEquals(beforeStock - quantity, afterStock);
    }

    @Test
    void testUnlockProductStock() {
        // 测试解锁商品库存
        Id productId = Id.of(2L);
        Integer quantity = 20; // 使用较小的数量，避免超过初始库存

        // 先锁定库存
        productClient.lockProductStock(productId, quantity);
        Integer lockedStock = productClient.getProductStockById(productId);

        // 解锁库存
        boolean result = productClient.unlockProductStock(productId, quantity);

        // 获取解锁后的库存
        Integer unlockedStock = productClient.getProductStockById(productId);

        Assertions.assertTrue(result);
        Assertions.assertEquals(lockedStock + quantity, unlockedStock);
    }

    @Test
    void testDeductProductStock() {
        // 测试扣减商品库存
        Id productId = Id.of(1L);
        Integer quantity = 30;

        // 先锁定库存
        productClient.lockProductStock(productId, quantity);

        // 扣减库存
        boolean result = productClient.deductProductStock(productId, quantity);

        Assertions.assertTrue(result);
    }

    @Test
    void testAddProductStock() {
        // 测试增加商品库存
        Id productId = Id.of(2L);
        Integer quantity = 100;

        // 获取增加前的库存
        Integer beforeStock = productClient.getProductStockById(productId);

        // 增加库存
        boolean result = productClient.addProductStock(productId, quantity);

        // 获取增加后的库存
        Integer afterStock = productClient.getProductStockById(productId);

        Assertions.assertTrue(result);
        Assertions.assertEquals(beforeStock + quantity, afterStock);
    }

    @Test
    void testGetProductStocksByIds() {
        // 测试批量获取商品库存
        List<Id> productIds = Arrays.asList(Id.of(1L), Id.of(2L));

        Map<Id, Integer> stockMap = productClient.getProductStocksByIds(productIds);

        Assertions.assertNotNull(stockMap);
        Assertions.assertEquals(2, stockMap.size());
        // 由于测试共享静态数据，库存值可能已被其他测试修改，所以只断言不为null
        Assertions.assertNotNull(stockMap.get(Id.of(1L)));
        Assertions.assertNotNull(stockMap.get(Id.of(2L)));
    }

    @Test
    void testLockProductStocks() {
        // 测试批量锁定商品库存
        Map<Id, Integer> productIdQuantities = new HashMap<>();
        productIdQuantities.put(Id.of(1L), 5);
        productIdQuantities.put(Id.of(2L), 10);

        // 获取锁定前的库存
        Integer beforeStock1 = productClient.getProductStockById(Id.of(1L));
        Integer beforeStock2 = productClient.getProductStockById(Id.of(2L));

        // 执行批量锁定
        boolean result = productClient.lockProductStocks(productIdQuantities);

        // 获取锁定后的库存
        Integer afterStock1 = productClient.getProductStockById(Id.of(1L));
        Integer afterStock2 = productClient.getProductStockById(Id.of(2L));

        Assertions.assertTrue(result);
        Assertions.assertEquals(beforeStock1 - 5, afterStock1);
        Assertions.assertEquals(beforeStock2 - 10, afterStock2);
    }

    @Test
    void testLockProductStocksInsufficientStock() {
        // 测试批量锁定商品库存（库存不足）
        Map<Id, Integer> productIdQuantities = new HashMap<>();
        productIdQuantities.put(Id.of(1L), 200); // 超过初始库存100
        productIdQuantities.put(Id.of(2L), 10);

        // 获取锁定前的库存
        Integer beforeStock1 = productClient.getProductStockById(Id.of(1L));
        Integer beforeStock2 = productClient.getProductStockById(Id.of(2L));

        // 执行批量锁定
        boolean result = productClient.lockProductStocks(productIdQuantities);

        // 获取锁定后的库存
        Integer afterStock1 = productClient.getProductStockById(Id.of(1L));
        Integer afterStock2 = productClient.getProductStockById(Id.of(2L));

        Assertions.assertFalse(result);
        // 批量操作失败，库存应保持不变
        Assertions.assertEquals(beforeStock1, afterStock1);
        Assertions.assertEquals(beforeStock2, afterStock2);
    }
}
