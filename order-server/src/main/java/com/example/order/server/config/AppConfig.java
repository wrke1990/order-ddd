package com.example.order.server.config;

import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.repository.ShoppingCartRepository;
import com.example.order.domain.service.AfterSaleOrderDomainService;
import com.example.order.domain.service.OrderDomainService;
import com.example.order.domain.service.ShoppingCartDomainService;
import com.example.order.domain.service.generator.OrderNoGenerator;
import com.example.order.domain.service.generator.impl.OrderNoGeneratorImpl;
import com.example.order.domain.service.impl.AfterSaleOrderDomainServiceImpl;
import com.example.order.domain.service.impl.OrderDomainServiceImpl;
import com.example.order.domain.service.impl.ShoppingCartDomainServiceImpl;
import com.example.order.infrastructure.acl.fulfillment.FulfillmentClient;
import com.example.order.infrastructure.acl.fulfillment.FulfillmentClientImpl;
import com.example.order.infrastructure.acl.payment.PaymentClient;
import com.example.order.infrastructure.acl.payment.PaymentClientImpl;
import com.example.order.infrastructure.acl.product.ProductClient;
import com.example.order.infrastructure.acl.product.ProductClientImpl;
import com.example.order.infrastructure.acl.promotion.PromotionClient;
import com.example.order.infrastructure.acl.promotion.PromotionClientImpl;
import com.example.order.infrastructure.acl.user.UserClient;
import com.example.order.infrastructure.acl.user.impl.UserClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 */
@Configuration
@ComponentScan({
        "com.example.order.server",
        "com.example.order.domain",
        "com.example.order.infrastructure"
})
public class AppConfig {

    @Bean
    public OrderNoGenerator orderNoGenerator() {
        return new OrderNoGeneratorImpl();
    }

    @Bean
    public PaymentClient paymentClient() {
        return new PaymentClientImpl();
    }

    @Bean
    public UserClient userClient() {
        return new UserClientImpl();
    }

    @Bean
    public ProductClient productClient() {
        return new ProductClientImpl();
    }

    @Bean
    public FulfillmentClient fulfillmentClient() {
        return new FulfillmentClientImpl();
    }

    @Bean
    public PromotionClient promotionClient() {
        return new PromotionClientImpl();
    }

    @Bean
    public OrderDomainService orderDomainService(OrderRepository orderRepository,
                                                OrderNoGenerator orderNoGenerator) {
        return new OrderDomainServiceImpl(orderRepository, orderNoGenerator);
    }

    @Bean
    public ShoppingCartDomainService shoppingCartDomainService(ShoppingCartRepository shoppingCartRepository) {
        return new ShoppingCartDomainServiceImpl(shoppingCartRepository);
    }

    @Bean
    public AfterSaleOrderDomainService afterSaleOrderDomainService(AfterSaleOrderRepository afterSaleOrderRepository,
                                                                OrderRepository orderRepository) {
        return new AfterSaleOrderDomainServiceImpl(afterSaleOrderRepository, orderRepository);
    }
}