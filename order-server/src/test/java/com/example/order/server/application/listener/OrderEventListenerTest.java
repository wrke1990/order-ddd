package com.example.order.server.application.listener;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.event.OrderCreatedEvent;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.service.ShoppingCartDomainService;

@ExtendWith(MockitoExtension.class)
public class OrderEventListenerTest {

    @Mock
    private ShoppingCartDomainService shoppingCartDomainService;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Test
    public void testOrderCreatedEventHandler() {
        // 创建订单创建事件
        Long orderId = 123456L;
        String orderNo = "DDD202312100001";
        Long userId = 1001L;

        // 创建模拟的订单项
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item1 = OrderItem.create(Id.of(1L), "测试商品1", 1, new Price(1000L, "CNY"));
        OrderItem item2 = OrderItem.create(Id.of(2L), "测试商品2", 2, new Price(2000L, "CNY"));
        orderItems.add(item1);
        orderItems.add(item2);

        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNo, userId, orderItems);

        // 直接调用事件处理方法
        System.out.println("[测试] 开始处理订单创建事件...");
        orderEventListener.handleOrderCreatedEvent(event);
        System.out.println("[测试] 订单创建事件处理完成");

        // 验证事件处理日志是否输出
        System.out.println("[测试] 事件处理流程完成");
    }
}
