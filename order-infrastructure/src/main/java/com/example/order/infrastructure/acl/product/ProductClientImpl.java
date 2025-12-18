package com.example.order.infrastructure.acl.product;

import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import com.example.order.infrastructure.acl.product.dto.StockDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品系统客户端实现类
 * 模拟与外部商品系统的交互
 */
@Component
public class ProductClientImpl implements ProductClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClientImpl.class);

    // 模拟商品数据
    private static final Map<Long, ProductDTO> PRODUCT_MAP = new ConcurrentHashMap<>();
    // 模拟库存数据
    private static final Map<Long, StockDTO> STOCK_MAP = new ConcurrentHashMap<>();

    // 初始化模拟数据
    static {
        // 初始化商品数据
        ProductDTO product1 = new ProductDTO();
        product1.setProductId(1L);
        product1.setProductName("商品1");
        product1.setProductImage("https://example.com/product1.jpg");
        product1.setPrice(new BigDecimal(100));
        PRODUCT_MAP.put(1L, product1);

        ProductDTO product2 = new ProductDTO();
        product2.setProductId(2L);
        product2.setProductName("商品2");
        product2.setProductImage("https://example.com/product2.jpg");
        product2.setPrice(new BigDecimal(200));
        PRODUCT_MAP.put(2L, product2);

        ProductDTO product3 = new ProductDTO();
        product3.setProductId(3L);
        product3.setProductName("商品3");
        product3.setProductImage("https://example.com/product3.jpg");
        product3.setPrice(new BigDecimal(300));
        PRODUCT_MAP.put(3L, product3);

        // 初始化库存数据
        StockDTO stock1 = new StockDTO();
        stock1.setProductId(1L);
        stock1.setAvailableStock(100);
        stock1.setLockedStock(0);
        STOCK_MAP.put(1L, stock1);

        StockDTO stock2 = new StockDTO();
        stock2.setProductId(2L);
        stock2.setAvailableStock(50);
        stock2.setLockedStock(0);
        STOCK_MAP.put(2L, stock2);

        StockDTO stock3 = new StockDTO();
        stock3.setProductId(3L);
        stock3.setAvailableStock(200);
        stock3.setLockedStock(0);
        STOCK_MAP.put(3L, stock3);
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Id> productIds) {
        LOGGER.info("Getting products by ids: {}", productIds);
        List<ProductDTO> products = new ArrayList<>();

        for (Id productId : productIds) {
            ProductDTO product = PRODUCT_MAP.get(productId.getValue());
            if (product != null) {
                products.add(product);
            }
        }

        LOGGER.info("Got {} products", products.size());
        return products;
    }

    @Override
    public boolean lockProductStocks(Map<Id, Integer> productIdQuantities) {
        LOGGER.info("Locking product stocks for ids and quantities: {}", productIdQuantities);

        // 首先验证所有商品都有足够的库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            if (stock == null) {
                LOGGER.error("Product not found: {}", productId);
                return false;
            }

            if (stock.getAvailableStock() < quantity) {
                LOGGER.error("Insufficient stock for product: {}, available: {}, requested: {}",
                        productId, stock.getAvailableStock(), quantity);
                return false;
            }
        }

        // 批量锁定库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            // 锁定库存
            stock.setAvailableStock(stock.getAvailableStock() - quantity);
            stock.setLockedStock(stock.getLockedStock() + quantity);
        }

        LOGGER.info("Locked stocks successfully for all products");
        return true;
    }

    @Override
    public boolean unlockProductStock(Map<Id, Integer> productIdQuantities) {
        LOGGER.info("Unlocking product stocks for ids and quantities: {}", productIdQuantities);

        // 验证所有商品都有足够的锁定库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            if (stock == null) {
                LOGGER.error("Product not found: {}", productId);
                return false;
            }

            if (stock.getLockedStock() < quantity) {
                LOGGER.error("Insufficient locked stock for product: {}, locked: {}, requested: {}",
                        productId, stock.getLockedStock(), quantity);
                return false;
            }
        }

        // 批量解锁库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            // 解锁库存
            stock.setAvailableStock(stock.getAvailableStock() + quantity);
            stock.setLockedStock(stock.getLockedStock() - quantity);
        }

        LOGGER.info("Unlocked stocks successfully for all products");
        return true;
    }

    @Override
    public boolean deductProductStock(Map<Id, Integer> productIdQuantities) {
        LOGGER.info("Deducting product stocks for ids and quantities: {}", productIdQuantities);

        // 验证所有商品都有足够的锁定库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            if (stock == null) {
                LOGGER.error("Product not found: {}", productId);
                return false;
            }

            if (stock.getLockedStock() < quantity) {
                LOGGER.error("Insufficient locked stock for product: {}, locked: {}, requested: {}",
                        productId, stock.getLockedStock(), quantity);
                return false;
            }
        }

        // 批量扣减库存
        for (Map.Entry<Id, Integer> entry : productIdQuantities.entrySet()) {
            Id productId = entry.getKey();
            Integer quantity = entry.getValue();
            StockDTO stock = STOCK_MAP.get(productId.getValue());

            // 扣减库存（从锁定库存中扣减）
            stock.setLockedStock(stock.getLockedStock() - quantity);
            // 这里可以考虑更新总库存，如果需要的话
            // stock.setTotalStock(stock.getTotalStock() - quantity);
        }

        LOGGER.info("Deducted stocks successfully for all products");
        return true;
    }


}