package com.example.order.server.application.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.LogisticsInfo;
import com.example.order.domain.model.vo.PaymentMethod;
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
import com.example.order.server.application.service.OrderCommandService;
import com.example.order.server.application.service.ProductValidationService;
import com.example.order.server.application.service.ShoppingCartCommandService;

/**
 * 订单命令服务实现类
 * 负责处理所有订单相关的命令操作（写操作）
 */
@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCommandServiceImpl.class);

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderDtoAssembler orderDtoAssembler;
    private final PaymentClient paymentClient;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final FulfillmentClient fulfillmentClient;
    private final PromotionClient promotionClient;
    private final ProductValidationService productValidationService;
    private final ShoppingCartCommandService shoppingCartCommandService;

    // 其他防腐层客户端也可以在这里注入

    public OrderCommandServiceImpl(OrderDomainService orderDomainService,
                                   OrderRepository orderRepository,
                                   OrderDtoAssembler orderDtoAssembler,
                                   PaymentClient paymentClient,
                                   UserClient userClient,
                                   ProductClient productClient,
                                   FulfillmentClient fulfillmentClient,
                                   PromotionClient promotionClient,
                                   ProductValidationService productValidationService,
                                   ShoppingCartCommandService shoppingCartCommandService) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderDtoAssembler = orderDtoAssembler;
        this.paymentClient = paymentClient;
        this.userClient = userClient;
        this.productClient = productClient;
        this.fulfillmentClient = fulfillmentClient;
        this.promotionClient = promotionClient;
        this.productValidationService = productValidationService;
        this.shoppingCartCommandService = shoppingCartCommandService;
    }

    @Override
    @Transactional
    @CacheEvict(value = "userOrders", key = "#command.getUserId()")
    public OrderResponse createOrder(CreateOrderCommand command) {
        logger.info("创建订单，用户ID: {}, 地址ID: {}, 支付方式: {}, 优惠券ID: {}",
                command.getUserId(), command.getAddressId(), command.getPaymentMethodId(), command.getCouponId());

        // 解析命令参数
        Id userId = Id.of(command.getUserId());
        Map<Id, Integer> productQuantityMap = buildProductQuantityMap(command.getItems());
        List<Id> productIds = new ArrayList<>(productQuantityMap.keySet());

        // 验证商品和库存
        Map<Id, ProductDTO> productMap = validateProductsAndStock(productIds, productQuantityMap);

        // 批量锁定商品库存
        lockProductStocks(productQuantityMap);

        // 创建订单项
        List<OrderItem> orderItems = createOrderItems(productMap, productQuantityMap);

        // 获取地址和支付方式
        Address address = getAddressFromAddressId(userId, Id.of(command.getAddressId()));
        PaymentMethod paymentMethod = getPaymentMethodFromPaymentMethodId(Id.of(command.getPaymentMethodId()));

        // 创建订单
        Order order = createOrderEntity(userId, orderItems, address, paymentMethod);
        Order createdOrder = orderDomainService.createOrder(order);
        orderRepository.save(createdOrder);

        // 创建支付订单
        createPaymentOrder(createdOrder);

        // 订单创建成功后，购物车记录删除逻辑将通过领域事件异步处理

        logger.info("订单创建成功，订单号: {}", createdOrder.getOrderNo());
        return orderDtoAssembler.toOrderResponse(createdOrder);
    }

    /**
     * 构建商品ID到数量的映射
     */
    private Map<Id, Integer> buildProductQuantityMap(List<OrderItemCommand> itemCommands) {
        Map<Id, Integer> productQuantityMap = new HashMap<>();
        for (OrderItemCommand item : itemCommands) {
            Id productId = Id.of(item.getProductId());
            productQuantityMap.put(productId, item.getQuantity());
        }
        return productQuantityMap;
    }

    /**
     * 验证商品存在性和库存 - 复用ProductValidationService的验证逻辑
     */
    private Map<Id, ProductDTO> validateProductsAndStock(List<Id> productIds, Map<Id, Integer> productQuantityMap) {
        return productValidationService.validateProductsAndStock(productIds, productQuantityMap);
    }

    /**
     * 批量锁定商品库存
     */
    private void lockProductStocks(Map<Id, Integer> productQuantityMap) {
        logger.info("开始批量锁定商品库存");
        boolean locked = productClient.lockProductStocks(productQuantityMap);
        if (!locked) {
            logger.error("批量锁定商品库存失败");
            throw new BusinessException("批量锁定商品库存失败");
        }
        logger.info("所有商品库存批量锁定成功");
    }

    /**
     * 创建订单项
     */
    private List<OrderItem> createOrderItems(Map<Id, ProductDTO> productMap, Map<Id, Integer> productQuantityMap) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<Id, ProductDTO> entry : productMap.entrySet()) {
            Id productId = entry.getKey();
            ProductDTO product = entry.getValue();
            int quantity = productQuantityMap.get(productId);

            OrderItem orderItem = orderDtoAssembler.toOrderItem(product, quantity);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    /**
     * 根据用户ID和地址ID获取完整的地址信息
     * 使用用户服务的防腐层客户端获取真实地址信息
     */
    private Address getAddressFromAddressId(Id userId, Id addressId) {
        // 使用用户服务的防腐层客户端获取地址信息
        Address address = userClient.getAddressById(addressId);
        if (address == null) {
            // 如果指定地址不存在，尝试获取用户默认地址
            address = userClient.getDefaultAddressByUserId(userId);
            if (address == null) {
                throw new BusinessException("地址信息不存在");
            }
        }
        return address;
    }

    /**
     * 根据支付方式ID获取完整的支付方式信息
     * 使用防腐层客户端调用支付服务获取真实支付方式信息
     */
    private PaymentMethod getPaymentMethodFromPaymentMethodId(Id paymentMethodId) {
        if (paymentMethodId == null) {
            throw new BusinessException("支付方式ID不能为空");
        }

        logger.info("根据支付方式ID获取支付方式信息，支付方式ID: {}", paymentMethodId);

        // 在实际项目中，这里应该调用支付服务的防腐层客户端获取支付方式信息
        // 例如：paymentClient.getPaymentMethodById(paymentMethodId)
        // 由于当前PaymentClient接口中没有获取支付方式的方法，我们实现一个基于ID映射的简化版本
        PaymentMethod.MethodType methodType;
        String methodName;

        // 根据支付方式ID的不同值返回不同的支付方式
        Long idValue = paymentMethodId.getValue();
        switch (idValue.intValue()) {
            case 1:
                methodType = PaymentMethod.MethodType.ALIPAY;
                methodName = "支付宝";
                break;
            case 2:
                methodType = PaymentMethod.MethodType.WECHAT;
                methodName = "微信支付";
                break;
            case 3:
                methodType = PaymentMethod.MethodType.CREDIT_CARD;
                methodName = "信用卡支付";
                break;
            case 4:
                methodType = PaymentMethod.MethodType.BANK_TRANSFER;
                methodName = "银行转账";
                break;
            default:
                // 默认支付方式
                methodType = PaymentMethod.MethodType.ALIPAY;
                methodName = "支付宝";
                logger.warn("未知的支付方式ID: {}, 使用默认支付方式: {}", paymentMethodId, methodName);
        }

        try {
            PaymentMethod paymentMethod = new PaymentMethod(paymentMethodId, methodType, methodName);
            logger.info("获取支付方式信息成功，支付方式: {}", paymentMethod);
            return paymentMethod;
        } catch (IllegalArgumentException e) {
            logger.error("创建支付方式对象失败，错误信息: {}", e.getMessage());
            throw new BusinessException("获取支付方式信息失败: " + e.getMessage());
        }
    }

    /**
     * 创建订单实体
     */
    private Order createOrderEntity(Id userId, List<OrderItem> orderItems, Address address, PaymentMethod paymentMethod) {
        Order order = Order.create(userId, orderItems, address, paymentMethod);
        return order;
    }

    /**
     * 创建支付订单
     */
    private void createPaymentOrder(Order order) {
        logger.info("开始创建支付订单，订单号: {}, 支付金额: {}, 支付方式: {}",
                order.getOrderNo(), order.getActualAmount(), order.getPaymentMethod().getType().name());

        Optional<PaymentOrderDTO> paymentOrder = paymentClient.createPaymentOrder(
                order.getOrderNo(), order.getUserId(), order.getActualAmount(), order.getPaymentMethod().getType().name());

        if (paymentOrder.isPresent()) {
            logger.info("支付订单创建成功，支付订单ID: {}, 订单号: {}",
                    paymentOrder.get().getPaymentOrderId(), order.getOrderNo());
        } else {
            logger.error("支付订单创建失败，订单号: {}", order.getOrderNo());
            // 可以选择抛出异常，或者记录日志后继续，这里选择继续处理
        }
    }

    @Override
    @Transactional
    public void payOrder(String orderNo, Id userId) {
        try {
            logger.info("支付订单，订单号: {}, 用户ID: {}", orderNo, userId);
            // 验证订单是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));
            // 调用领域服务支付订单
            orderDomainService.payOrder(order);
            logger.info("订单支付成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("支付订单失败，订单号: {}, 用户ID: {}, 错误信息: {}", orderNo, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("支付订单异常，订单号: {}, 用户ID: {}", orderNo, userId, e);
            throw new BusinessException("支付订单失败");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(String orderNo, Id userId) {
        try {
            logger.info("取消订单，订单号: {}, 用户ID: {}", orderNo, userId);

            // 获取订单信息并验证是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));

            // 取消订单
            orderDomainService.cancelOrder(order);

            // 批量解锁已锁定的商品库存
            Map<Id, Integer> productQuantityMap = new HashMap<>();
            for (OrderItem item : order.getOrderItems()) {
                productQuantityMap.put(item.getProductId(), item.getQuantity());
            }
            boolean unlockSuccess = productClient.unlockProductStock(productQuantityMap);
            if (!unlockSuccess) {
                logger.error("批量解锁商品库存失败");
            } else {
                logger.info("批量解锁商品库存成功");
            }

            logger.info("订单取消成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("取消订单失败，订单号: {}, 用户ID: {}, 错误信息: {}", orderNo, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("取消订单异常，订单号: {}, 用户ID: {}", orderNo, userId, e);
            throw new BusinessException("取消订单失败");
        }
    }

    @Override
    @Transactional
    public void shipOrder(String orderNo, Id userId) {
        try {
            logger.info("发货，订单号: {}, 用户ID: {}", orderNo, userId);
            // 验证订单是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));
            // 调用领域服务发货
            orderDomainService.shipOrder(order);

            // 调用履约系统创建物流信息（简化实现，实际项目中应该创建完整的LogisticsInfo对象）
            LogisticsInfo logisticsInfo = new LogisticsInfo("DefaultCourier", "");
            // 使用订单ID而不是订单号调用createDeliveryOrder方法
            String deliveryOrderId = fulfillmentClient.createDeliveryOrder(order.getId(), order.getUserId(), logisticsInfo);
            logger.info("为订单创建物流信息，订单号: {}, 发货单ID: {}", orderNo, deliveryOrderId);

            logger.info("订单发货成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("发货失败，订单号: {}, 用户ID: {}, 错误信息: {}", orderNo, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("发货异常，订单号: {}, 用户ID: {}", orderNo, userId, e);
            throw new BusinessException("发货失败");
        }
    }

    @Override
    @Transactional
    public void confirmReceipt(String orderNo, Id userId) {
        try {
            logger.info("确认收货，订单号: {}, 用户ID: {}", orderNo, userId);
            // 验证订单是否属于当前用户
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));

            // 调用领域服务确认收货
            orderDomainService.confirmReceipt(order);

            logger.info("订单确认收货成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("确认收货失败，订单号: {}, 用户ID: {}, 错误信息: {}", orderNo, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("确认收货异常，订单号: {}, 用户ID: {}", orderNo, userId, e);
            throw new BusinessException("确认收货失败");
        }
    }

    @Override
    @Transactional
    public void changeShippingAddress(String orderNo, Id addressId, Id userId) {
        try {
            logger.info("修改订单地址，订单号: {}, 新地址ID: {}, 用户ID: {}", orderNo, addressId, userId);
            // 验证订单是否属于当前用户
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));
            // 调用领域服务修改地址
            orderDomainService.changeAddress(order, addressId);
            logger.info("订单地址修改成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("修改订单地址失败，订单号: {}, 地址ID: {}, 用户ID: {}, 错误信息: {}", orderNo, addressId, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("修改订单地址异常，订单号: {}, 地址ID: {}, 用户ID: {}", orderNo, addressId, userId, e);
            throw new BusinessException("修改订单地址失败");
        }
    }

    @Override
    @Transactional
    public void changePaymentMethod(String orderNo, Id paymentMethodId, Id userId) {
        try {
            logger.info("修改订单支付方式，订单号: {}, 新支付方式ID: {}, 用户ID: {}", orderNo, paymentMethodId, userId);
            // 验证订单是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));
            // 调用领域服务修改支付方式
            orderDomainService.changePaymentMethod(order, paymentMethodId);
            logger.info("订单支付方式修改成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("修改支付方式失败，订单号: {}, 支付方式ID: {}, 用户ID: {}, 错误信息: {}", orderNo, paymentMethodId, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("修改支付方式异常，订单号: {}, 支付方式ID: {}, 用户ID: {}", orderNo, paymentMethodId, userId, e);
            throw new BusinessException("修改支付方式失败");
        }
    }

    @Override
    @Transactional
    public void applyCoupon(String orderNo, Id couponId, Id userId) {
        try {
            logger.info("应用优惠券，订单号: {}, 优惠券ID: {}, 用户ID: {}", orderNo, couponId, userId);
            // 验证订单是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));
            // 调用领域服务应用优惠券
            orderDomainService.applyCoupon(order, couponId);
            logger.info("优惠券应用成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("应用优惠券失败，订单号: {}, 优惠券ID: {}, 用户ID: {}, 错误信息: {}", orderNo, couponId, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("应用优惠券异常，订单号: {}, 优惠券ID: {}, 用户ID: {}", orderNo, couponId, userId, e);
            throw new BusinessException("应用优惠券失败");
        }
    }

    @Override
    @Transactional
    public void completeOrder(String orderNo, Id userId) {
        try {
            logger.info("完成订单，订单号: {}, 用户ID: {}", orderNo, userId);

            // 验证订单是否属于当前用户，使用findByUserIdAndOrderNo方法自动验证用户权限
            Order order = orderRepository.findByUserIdAndOrderNo(userId, orderNo)
                    .orElseThrow(() -> new BusinessException("订单不存在或无权操作"));

            // 完成订单
            orderDomainService.completeOrder(order);

            // 批量扣减商品库存
            Map<Id, Integer> productQuantityMap = new HashMap<>();
            for (OrderItem item : order.getOrderItems()) {
                productQuantityMap.put(item.getProductId(), item.getQuantity());
            }
            boolean deductSuccess = productClient.deductProductStock(productQuantityMap);
            if (!deductSuccess) {
                logger.error("批量扣减商品库存失败");
            } else {
                logger.info("批量扣减商品库存成功");
            }

            logger.info("订单完成成功，订单号: {}", orderNo);
        } catch (BusinessException e) {
            logger.error("完成订单失败，订单号: {}, 用户ID: {}, 错误信息: {}", orderNo, userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("完成订单异常，订单号: {}, 用户ID: {}", orderNo, userId, e);
            throw new BusinessException("完成订单失败");
        }
    }


}