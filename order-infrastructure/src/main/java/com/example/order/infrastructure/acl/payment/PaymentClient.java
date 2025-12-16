package com.example.order.infrastructure.acl.payment;

import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.acl.payment.dto.PaymentOrderDTO;

import java.util.Optional;

/**
 * 支付系统客户端接口
 * 定义与外部支付系统的交互方法
 */
public interface PaymentClient {

    /**
     * 创建支付单
     *
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式
     * @return 支付单DTO
     */
    Optional<PaymentOrderDTO> createPaymentOrder(String orderNo, Id userId, Price amount, String paymentMethod);

    /**
     * 获取支付单信息
     *
     * @param paymentOrderId 支付单ID
     * @return 支付单DTO
     */
    Optional<PaymentOrderDTO> getPaymentOrderById(Id paymentOrderId);

    /**
     * 根据订单号获取支付单信息
     *
     * @param orderNo 订单号
     * @return 支付单DTO
     */
    Optional<PaymentOrderDTO> getPaymentOrderByOrderNo(String orderNo);

    /**
     * 支付单确认
     *
     * @param paymentOrderId 支付单ID
     * @return 是否确认成功
     */
    boolean confirmPayment(Id paymentOrderId);

    /**
     * 支付单取消
     *
     * @param paymentOrderId 支付单ID
     * @return 是否取消成功
     */
    boolean cancelPayment(Id paymentOrderId);

    /**
     * 支付单退款
     *
     * @param paymentOrderId 支付单ID
     * @param refundAmount 退款金额
     * @param refundReason 退款原因
     * @return 是否退款成功
     */
    boolean refundPayment(Id paymentOrderId, Price refundAmount, String refundReason);

    /**
     * 查询支付单状态
     *
     * @param paymentOrderId 支付单ID
     * @return 支付单状态
     */
    String getPaymentStatus(Id paymentOrderId);
}
