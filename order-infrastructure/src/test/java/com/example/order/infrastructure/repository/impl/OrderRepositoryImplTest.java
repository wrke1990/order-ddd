package com.example.order.infrastructure.repository.impl;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.event.DomainEventPublisher;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.PaymentMethod;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.assember.OrderAssembler;
import com.example.order.infrastructure.persistence.po.OrderItemPO;
import com.example.order.infrastructure.persistence.po.OrderPO;
import com.example.order.infrastructure.persistence.repository.JpaOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * OrderRepositoryImpl测试
 */
@ExtendWith(MockitoExtension.class)
public class OrderRepositoryImplTest {

    @Mock
    private JpaOrderRepository jpaOrderRepository;

    @Mock
    private OrderAssembler orderAssembler;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    private Order testOrder;
    private OrderPO testOrderPO;

    @BeforeEach
    public void setUp() {
        // 准备测试数据
        testOrder = createTestOrder();
        testOrderPO = createTestOrderPO();

        // 设置mock行为
        lenient().when(orderAssembler.toOrderPO(any(Order.class))).thenReturn(testOrderPO);
        lenient().when(orderAssembler.toOrder(any(OrderPO.class))).thenReturn(testOrder);
        lenient().when(jpaOrderRepository.save(any(OrderPO.class))).thenReturn(testOrderPO);
        lenient().when(jpaOrderRepository.findById(1L)).thenReturn(Optional.of(testOrderPO));
        lenient().when(jpaOrderRepository.findByOrderNo("ORDER-001")).thenReturn(Optional.of(testOrderPO));

        List<OrderPO> orderPOList = new ArrayList<>();
        orderPOList.add(testOrderPO);
        lenient().when(jpaOrderRepository.findByUserId(1L)).thenReturn(orderPOList);
    }

    @Test
    public void testSaveOrder() {
        // 调用save方法
        Order savedOrder = orderRepository.save(testOrder);

        // 验证结果
        Assertions.assertNotNull(savedOrder);
        Assertions.assertEquals(testOrder.getId(), savedOrder.getId());

        // 验证mock调用
        verify(orderAssembler, times(1)).toOrderPO(testOrder);
        verify(jpaOrderRepository, times(1)).save(testOrderPO);
        verify(orderAssembler, times(1)).toOrder(testOrderPO);
    }

    @Test
    public void testFindById() {
        // 调用findById方法
        Optional<Order> orderOptional = orderRepository.findById(Id.of(1L));

        // 验证结果
        Assertions.assertTrue(orderOptional.isPresent());
        Assertions.assertEquals(testOrder.getId(), orderOptional.get().getId());

        // 验证mock调用
        verify(jpaOrderRepository, times(1)).findById(1L);
        verify(orderAssembler, times(1)).toOrder(testOrderPO);
    }

    @Test
    public void testFindByOrderNo() {
        // 调用findByOrderNo方法
        Optional<Order> orderOptional = orderRepository.findByOrderNo("ORDER-001");

        // 验证结果
        Assertions.assertTrue(orderOptional.isPresent());
        Assertions.assertEquals(testOrder.getOrderNo(), orderOptional.get().getOrderNo());

        // 验证mock调用
        verify(jpaOrderRepository, times(1)).findByOrderNo("ORDER-001");
        verify(orderAssembler, times(1)).toOrder(testOrderPO);
    }

    @Test
    public void testFindByUserId() {
        // 调用findByUserId方法
        List<Order> orderList = orderRepository.findByUserId(Id.of(1L));

        // 验证结果
        Assertions.assertNotNull(orderList);
        Assertions.assertEquals(1, orderList.size());
        Assertions.assertEquals(testOrder.getUserId(), orderList.get(0).getUserId());

        // 验证mock调用
        verify(jpaOrderRepository, times(1)).findByUserId(1L);
        verify(orderAssembler, times(1)).toOrder(testOrderPO);
    }

    @Test
    public void testDeleteById() {
        // 调用deleteById方法
        orderRepository.deleteById(Id.of(1L));

        // 验证mock调用
        verify(jpaOrderRepository, times(1)).deleteById(1L);
    }

    /**
     * 创建测试订单
     */
    private Order createTestOrder() {
        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem item1 = OrderItem.create(Id.of(1L), "Product 1", 2, Price.ofCNY(50L));
        orderItems.add(item1);

        // 创建地址和支付方式对象
        Address address = new Address(Id.of(1L), "张三", "13800138000", "北京市", "朝阳区", "朝阳区", "xxx路123号", "100000");
        PaymentMethod paymentMethod = new PaymentMethod(Id.of(1L), PaymentMethod.MethodType.ALIPAY, "支付宝");

        Order order = Order.create(Id.of(1L), orderItems, address, paymentMethod);
        order.setOrderNo("ORDER-001");
        order.setId(Id.of(1L));
        return order;
    }

    /**
     * 创建测试订单PO
     */
    private OrderPO createTestOrderPO() {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(1L);
        orderPO.setUserId(1L);
        orderPO.setOrderNo("ORDER-001");
        orderPO.setTotalAmount(100L);
        orderPO.setCurrency("CNY");
        orderPO.setStatus("PENDING_PAYMENT");

        List<OrderItemPO> orderItemPOs = new ArrayList<>();
        OrderItemPO orderItemPO = new OrderItemPO();
        orderItemPO.setId(1L);
        orderItemPO.setOrderNo("ORDER-001");
        orderItemPO.setProductId(1L);
        orderItemPO.setProductName("Product 1");
        orderItemPO.setQuantity(2);
        orderItemPO.setPrice(50L);
        orderItemPO.setCurrency("CNY");
        orderItemPO.setTotalPrice(100L);
        orderItemPOs.add(orderItemPO);

        orderPO.setOrderItems(orderItemPOs);

        return orderPO;
    }
}
