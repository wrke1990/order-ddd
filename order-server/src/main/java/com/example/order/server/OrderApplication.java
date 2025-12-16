package com.example.order.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 订单系统应用启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.example.order")
@EntityScan(basePackages = "com.example.order.infrastructure.persistence.po")
@EnableJpaRepositories(basePackages = "com.example.order.infrastructure.persistence.repository")
@EnableCaching
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
