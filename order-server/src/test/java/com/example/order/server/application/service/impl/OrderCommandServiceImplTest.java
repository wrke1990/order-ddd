package com.example.order.server.application.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.PaymentMethod;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.OrderDomainService;
import com.example.order.infrastructure.acl.fulfillment.FulfillmentClient;
import com.example.order.infrastructure.acl.payment.PaymentClient;
import com.example.order.infrastructure.acl.payment.dto.PaymentOrderDTO;
import com.example.order.infrastructure.acl.product.ProductClient;
import com.example.order.infrastructure.acl.product.dto.ProductDTO;
import com.example.order.infrastructure.acl.promotion.PromotionClient;
import com.example.order.infrastructure.acl.user.UserClient;
import com.example.order.server.application.assember.OrderDtoAssembler;
import com.example.order.server.application.dto.CreateOrderCommand;
import com.example.order.server.application.dto.OrderItemCommand;
import com.example.order.server.application.dto.OrderResponse;
import com.example.order.server.application.service.ProductValidationService;
import com.example.order.server.application.service.ShoppingCartCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCommandServiceImplTest {

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductValidationService productValidationService;

    @Mock
    private ProductClient productClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private UserClient userClient;

    @Mock
    private PromotionClient promotionClient;

    @Mock
    private FulfillmentClient fulfillmentClient;

    @Mock
    private OrderDtoAssembler orderDtoAssembler;

    @Mock
    private ShoppingCartCommandService shoppingCartCommandService;

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    private CreateOrderCommand createOrderCommand;
    private Map<Id, ProductDTO> productMap;
    private ProductDTO productDTO;
    private Order order;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        createOrderCommand = new CreateOrderCommand();
        createOrderCommand.setUserId(1L);
        createOrderCommand.setAddressId(1001L);
        createOrderCommand.setPaymentMethodId(1L);

        List<OrderItemCommand> items = new ArrayList<>();
        OrderItemCommand item = new OrderItemCommand();
        item.setProductId(2001L);
        item.setProductName("测试商品");
        item.setQuantity(2);
        item.setPrice(100L);
        items.add(item);
        createOrderCommand.setItems(items);
    }

    @Test
    void createOrder_Success() {
        // 初始化订单数据
        order = mock(Order.class);
        when(order.getOrderNo()).thenReturn("ORDER_20230101_00001");
        when(order.getUserId()).thenReturn(Id.of(1L));
        when(order.getActualAmount()).thenReturn(new Price(200L, "CNY"));
        when(order.getPaymentMethod()).thenReturn(new PaymentMethod(Id.of(1L), PaymentMethod.MethodType.ALIPAY, "支付宝"));
        when(order.getOrderNo()).thenReturn("TEST_ORDER_NO");

        // 模拟商品验证服务的行为
        Map<Id, ProductDTO> mockProductMap = new HashMap<>();
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setProductId(2001L);
        mockProduct.setProductName("测试商品");
        mockProduct.setPrice(new BigDecimal(100));
        mockProductMap.put(Id.of(2001L), mockProduct);

        when(productValidationService.validateProductsAndStock(anyList(), anyMap())).thenReturn(mockProductMap);

        // 模拟商品库存锁定（使用类型转换来解决类型匹配问题）
        doAnswer((invocation) -> {
            Map<Id, Integer> productIdQuantities = (Map<Id, Integer>) invocation.getArgument(0);
            System.out.println("Mock lockProductStocks called with: " + productIdQuantities);
            return true;
        }).when(productClient).lockProductStocks((Map<Id, Integer>) any());

        // 模拟地址获取
        Address address = new Address(Id.of(1001L), "张三", "13800138000", "北京市", "北京市", "朝阳区", "测试街道1号", "100000");
        when(userClient.getAddressById(any(Id.class))).thenReturn(address);

        // 模拟订单创建
        when(orderDomainService.createOrder(any(Order.class))).thenReturn(order);
        when(paymentClient.createPaymentOrder(anyString(), any(Id.class), any(Price.class), anyString()))
                .thenReturn(Optional.of(mock(PaymentOrderDTO.class)));

        // 模拟DTO转换
        OrderItem mockOrderItem = mock(OrderItem.class);
        when(mockOrderItem.getProductId()).thenReturn(Id.of(2001L));
        when(mockOrderItem.getProductName()).thenReturn("测试商品");
        when(mockOrderItem.getQuantity()).thenReturn(2);
        when(mockOrderItem.getPrice()).thenReturn(new Price(100L, "CNY"));
        when(mockOrderItem.getTotalPrice()).thenReturn(new Price(200L, "CNY"));

        when(orderDtoAssembler.toOrderItem(any(ProductDTO.class), anyInt())).thenReturn(mockOrderItem);
        when(orderDtoAssembler.toOrderResponse(any(Order.class))).thenReturn(new OrderResponse());

        // 执行测试
        OrderResponse result = orderCommandService.createOrder(createOrderCommand);

        // 验证结果
        assertNotNull(result);

        // 验证调用次数
        verify(productValidationService, times(1)).validateProductsAndStock(anyList(), anyMap());
        verify(productClient, times(1)).lockProductStocks((Map<Id, Integer>) any());
        verify(orderDomainService, times(1)).createOrder(any(Order.class));
        verify(paymentClient, times(1)).createPaymentOrder(anyString(), any(Id.class), any(Price.class), anyString());
    }

    @Test
    void createOrder_ProductValidationFailed() {
        // 模拟商品验证失败
        when(productValidationService.validateProductsAndStock(anyList(), anyMap()))
                .thenThrow(new BusinessException("商品验证失败"));

        // 执行测试并验证异常
        assertThrows(BusinessException.class, () -> {
            orderCommandService.createOrder(createOrderCommand);
        });

        // 验证没有调用后续方法
        verify(productClient, never()).lockProductStocks((Map<Id, Integer>) any());
        verify(orderDomainService, never()).createOrder(any(Order.class));
    }

    @Test
    void createOrder_StockLockFailed() {
        // 模拟商品验证服务的行为
        Map<Id, ProductDTO> mockProductMap = new HashMap<>();
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setProductId(2001L);
        mockProduct.setProductName("测试商品");
        mockProduct.setPrice(new BigDecimal(100));
        mockProductMap.put(Id.of(2001L), mockProduct);

        when(productValidationService.validateProductsAndStock(anyList(), anyMap())).thenReturn(mockProductMap);

        // 模拟商品库存锁定失败（使用类型转换来解决类型匹配问题）
        doAnswer((invocation) -> {
            Map<Id, Integer> productIdQuantities = (Map<Id, Integer>) invocation.getArgument(0);
            System.out.println("Mock lockProductStocks called with: " + productIdQuantities);
            return false;
        }).when(productClient).lockProductStocks((Map<Id, Integer>) any());

        // 注意：在库存锁定失败的情况下，不会执行到获取地址信息的步骤，所以不需要模拟userClient.getAddressById

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderCommandService.createOrder(createOrderCommand);
        });
        assertEquals("批量锁定商品库存失败", exception.getMessage());

        // 验证调用
        verify(productClient).lockProductStocks((Map<Id, Integer>) any());
        verify(orderDomainService, never()).createOrder(any(Order.class));
        verify(paymentClient, never()).createPaymentOrder(anyString(), any(Id.class), any(Price.class), anyString());
    }
}
