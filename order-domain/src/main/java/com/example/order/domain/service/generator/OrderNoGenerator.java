package com.example.order.domain.service.generator;

/**
 * 订单号生成器接口
 */
public interface OrderNoGenerator {
    
    /**
     * 生成唯一订单号
     * @return 订单号
     */
    String generate();
}
