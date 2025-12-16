package com.example.order.domain.service.generator.impl;

import com.example.order.domain.service.generator.OrderNoGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单号生成器实现
 * 基于时间戳和随机数的订单号生成策略
 */
public class OrderNoGeneratorImpl implements OrderNoGenerator {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final Random RANDOM = new Random();
    private static final int RANDOM_DIGITS = 3;
    private static final int MAX_RANDOM_NUMBER = (int) Math.pow(10, RANDOM_DIGITS);
    
    @Override
    public String generate() {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        int random = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        String randomStr = String.format("%0" + RANDOM_DIGITS + "d", random);
        
        return String.format("ORDER-%s-%s", timestamp, randomStr);
    }
}
