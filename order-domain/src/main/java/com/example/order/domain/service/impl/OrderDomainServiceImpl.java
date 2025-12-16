package com.example.order.domain.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.vo.AfterSaleStatus;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Coupon;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.PaymentMethod;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.OrderDomainService;
import com.example.order.domain.service.generator.OrderNoGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单领域服务实现
 */
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final Logger logger = LoggerFactory.getLogger(OrderDomainServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderNoGenerator orderNoGenerator;
    private final AfterSaleOrderRepository afterSaleOrderRepository;

    public OrderDomainServiceImpl(OrderRepository orderRepository, OrderNoGenerator orderNoGenerator, AfterSaleOrderRepository afterSaleOrderRepository) {
        this.orderRepository = orderRepository;
        this.orderNoGenerator = orderNoGenerator;
        this.afterSaleOrderRepository = afterSaleOrderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        if (order == null) {
            throw new BusinessException("订单不能为空");
        }
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            // 如果订单号为空，生成一个新的
            order.setOrderNo(orderNoGenerator.generate());
        }

        // 保存订单
        return orderRepository.save(order);
    }



    @Override
    public Order payOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        // TODO: 调用支付服务进行支付处理，获取支付单号
        String paymentNo = "PAY" + System.currentTimeMillis(); // 临时生成支付单号
        order.pay(paymentNo);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        order.cancel();
        Order savedOrder = orderRepository.save(order);

        logger.info("订单已取消，订单号: {}", savedOrder.getOrderNo());
        return savedOrder;
    }



    @Override
    public Order shipOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        // TODO: 调用物流服务生成运单，获取物流公司和运单号
        String logisticsCompany = "顺丰快递"; // 临时使用物流公司
        String trackingNumber = "SF" + System.currentTimeMillis(); // 临时生成运单号
        order.ship(logisticsCompany, trackingNumber);
        return orderRepository.save(order);
    }

    @Override
    public Order completeOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        order.complete();
        Order savedOrder = orderRepository.save(order);
        logger.info("订单已完成，订单号: {}", savedOrder.getOrderNo());
        return savedOrder;
    }

    @Override
    public Order applyCoupon(String orderNo, Id couponId) {
        if (couponId == null) {
            throw new BusinessException("优惠券ID不能为空");
        }
        logger.info("应用优惠券，订单号: {}, 优惠券ID: {}", orderNo, couponId.getValue());
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 创建优惠券对象（实际项目中应通过优惠券服务获取）
        Coupon coupon = new Coupon(
                couponId.getValue(),
                1L, // 优惠券模板ID
                "测试优惠券",
                "测试优惠券描述",
                new Price(100L, "CNY"), // 优惠金额100元
                new Price(1000L, "CNY"), // 最低订单金额1000元
                LocalDateTime.now().minusDays(1), // 开始时间
                LocalDateTime.now().plusDays(30) // 结束时间
        );

        // 应用优惠券逻辑
        order.applyCoupon(coupon);
        return orderRepository.save(order);
    }

    @Override
    public Order changePaymentMethod(String orderNo, Id paymentMethodId) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 这里需要调用支付服务创建新的支付单和获取支付方式，后续实现
        // TODO: 调用支付服务创建新的支付单和获取支付方式

        // 当前实现：创建一个测试支付方式对象
        PaymentMethod newPaymentMethod = new PaymentMethod(paymentMethodId, PaymentMethod.MethodType.ALIPAY, "支付宝");

        // 调用订单的修改支付方式方法
        order.changePaymentMethod(newPaymentMethod);

        return orderRepository.save(order);
    }

    @Override
    public Order changeAddress(String orderNo, Id addressId) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        // 这里需要调用地址服务获取地址信息，后续实现
        // TODO: 调用地址服务获取地址信息

        // 当前实现：创建一个测试地址对象
        Address newAddress = new Address(
                addressId,
                "测试收货人",
                "13800138000",
                "北京市",
                "北京市",
                "朝阳区",
                "测试街道",
                "100000"
        );

        // 调用订单的修改地址方法
        order.changeShippingAddress(newAddress);

        return orderRepository.save(order);
    }
}
