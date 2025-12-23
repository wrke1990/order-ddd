package com.example.order.infrastructure.acl.product;

import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class ProductClientImplTest {

    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        productClient = new ProductClientImpl();
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
    void testLockProductStocks() {
        // 测试批量锁定商品库存
        Map<Id, Integer> productIdQuantities = new HashMap<>();
        productIdQuantities.put(Id.of(1L), 5);
        productIdQuantities.put(Id.of(2L), 10);

        // 执行批量锁定
        boolean result = productClient.lockProductStocks(productIdQuantities);

        Assertions.assertTrue(result);
    }

    @Test
    void testUnlockProductStock() {
        // 测试批量解锁商品库存
        Map<Id, Integer> productIdQuantities = new HashMap<>();
        productIdQuantities.put(Id.of(1L), 5);
        productIdQuantities.put(Id.of(2L), 10);

        // 先锁定库存
        productClient.lockProductStocks(productIdQuantities);

        // 解锁库存
        boolean result = productClient.unlockProductStock(productIdQuantities);

        Assertions.assertTrue(result);
    }

    @Test
    void testDeductProductStock() {
        // 测试批量扣减商品库存
        Map<Id, Integer> productIdQuantities = new HashMap<>();
        productIdQuantities.put(Id.of(1L), 5);
        productIdQuantities.put(Id.of(2L), 10);

        // 先锁定库存
        productClient.lockProductStocks(productIdQuantities);

        // 扣减库存
        boolean result = productClient.deductProductStock(productIdQuantities);

        Assertions.assertTrue(result);
    }
}
