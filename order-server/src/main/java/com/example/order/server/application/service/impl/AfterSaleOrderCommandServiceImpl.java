package com.example.order.server.application.service.impl;

import com.example.order.common.exception.BusinessException;
import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.Price;
import com.example.order.domain.repository.AfterSaleOrderRepository;
import com.example.order.infrastructure.acl.payment.PaymentClient;
import com.example.order.server.application.assember.AfterSaleOrderDtoAssembler;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
import com.example.order.server.application.service.AfterSaleOrderCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后订单命令服务实现类
 */
@Service
public class AfterSaleOrderCommandServiceImpl implements AfterSaleOrderCommandService {

    private static final Logger log = LoggerFactory.getLogger(AfterSaleOrderCommandServiceImpl.class);

    private final AfterSaleOrderRepository afterSaleOrderRepository;
    private final AfterSaleOrderDtoAssembler afterSaleOrderDtoAssembler;
    private final PaymentClient paymentClient;

    public AfterSaleOrderCommandServiceImpl(AfterSaleOrderRepository afterSaleOrderRepository,
                                            AfterSaleOrderDtoAssembler afterSaleOrderDtoAssembler,
                                            PaymentClient paymentClient) {
        this.afterSaleOrderRepository = afterSaleOrderRepository;
        this.afterSaleOrderDtoAssembler = afterSaleOrderDtoAssembler;
        this.paymentClient = paymentClient;
    }

    @Override
    @Transactional
    public AfterSaleOrderResponse createAfterSaleOrder(CreateAfterSaleOrderCommand request) {
        log.info("创建售后订单，用户ID: {}, 订单号: {}, 售后类型: {}, 申请金额: {}",
                request.getUserId(), request.getOrderNo(), request.getAfterSaleType(), request.getApplyAmount());

        // 生成售后单号（可以使用UUID或雪花算法）
        String afterSaleNo = "ASO" + System.currentTimeMillis();

        // 创建售后商品项
        AfterSaleItem afterSaleItem = AfterSaleItem.create(
                request.getProductId(),
                request.getProductName(),
                request.getProductImage(),
                request.getQuantity(),
                new Price(request.getApplyAmount(), request.getCurrency()),
                request.getQuantity() // 退款数量默认为申请数量
        );
        List<AfterSaleItem> afterSaleItems = new ArrayList<>();
        afterSaleItems.add(afterSaleItem);

        // 创建售后单聚合根
        AfterSaleOrder afterSaleOrder = AfterSaleOrder.create(
                afterSaleNo,
                null, // orderId由订单服务后续填充
                request.getOrderNo(),
                request.getUserId(),
                AfterSaleType.valueOf(request.getAfterSaleType()),
                request.getReason(),
                request.getDescription(),
                null, // 图片字段由前端可选上传
                afterSaleItems,
                false // 不是管理员发起的
        );

        // 保存到仓储
        AfterSaleOrder savedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        log.info("售后订单创建成功，售后单号: {}", savedAfterSaleOrder.getAfterSaleNo());

        // 转换为响应DTO
        return afterSaleOrderDtoAssembler.toAfterSaleOrderResponse(savedAfterSaleOrder);
    }

    @Override
    @Transactional
    public AfterSaleOrderResponse cancelAfterSaleOrder(Long afterSaleId) {
        log.info("取消售后订单，售后ID: {}", afterSaleId);

        // 查找售后订单
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后订单不存在: " + afterSaleId));

        // 执行取消操作（在聚合根内部实现业务规则）
        afterSaleOrder.cancel();

        // 保存更新后的聚合根
        AfterSaleOrder cancelledAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        log.info("售后订单取消成功，售后单号: {}", cancelledAfterSaleOrder.getAfterSaleNo());

        // 转换为响应DTO
        return afterSaleOrderDtoAssembler.toAfterSaleOrderResponse(cancelledAfterSaleOrder);
    }

    @Override
    @Transactional
    public AfterSaleOrderResponse approveAfterSaleOrder(Long afterSaleId, String reason) {
        log.info("审批通过售后订单，售后ID: {}, 原因: {}", afterSaleId, reason);

        // 查找售后订单
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后订单不存在: " + afterSaleId));

        // 执行审批通过操作
        afterSaleOrder.approve(reason);

        // 保存更新后的聚合根
        AfterSaleOrder approvedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        log.info("售后订单审批通过，售后单号: {}", approvedAfterSaleOrder.getAfterSaleNo());

        // 转换为响应DTO
        return afterSaleOrderDtoAssembler.toAfterSaleOrderResponse(approvedAfterSaleOrder);
    }

    @Override
    @Transactional
    public AfterSaleOrderResponse rejectAfterSaleOrder(Long afterSaleId, String reason) {
        log.info("拒绝售后订单，售后ID: {}, 原因: {}", afterSaleId, reason);

        // 查找售后订单
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后订单不存在: " + afterSaleId));

        // 执行拒绝操作
        afterSaleOrder.reject(reason);

        // 保存更新后的聚合根
        AfterSaleOrder rejectedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        log.info("售后订单拒绝成功，售后单号: {}", rejectedAfterSaleOrder.getAfterSaleNo());

        // 转换为响应DTO
        return afterSaleOrderDtoAssembler.toAfterSaleOrderResponse(rejectedAfterSaleOrder);
    }

    @Override
    @Transactional
    public AfterSaleOrderResponse completeRefund(Long afterSaleId, Double refundAmount) {
        log.info("完成售后退款，售后ID: {}, 退款金额: {}", afterSaleId, refundAmount);

        // 查找售后订单
        AfterSaleOrder afterSaleOrder = afterSaleOrderRepository.findById(afterSaleId)
                .orElseThrow(() -> new BusinessException("售后订单不存在: " + afterSaleId));

        // 执行退款完成操作
        afterSaleOrder.confirmRefund(new Price(refundAmount.longValue(), "CNY"));

        // 调用支付系统执行实际退款
        String orderNo = afterSaleOrder.getOrderNo();
        log.info("调用支付系统退款，订单号: {}, 退款金额: {}", orderNo, refundAmount);

        // 这里假设支付单ID可以通过订单号获取
        // 在实际项目中，可能需要从订单或售后订单中获取支付单ID
        paymentClient.getPaymentOrderByOrderNo(orderNo)
                .ifPresent(paymentOrder -> {
                    boolean refundSuccess = paymentClient.refundPayment(
                            Id.of(paymentOrder.getPaymentOrderId()),
                            new Price(refundAmount.longValue(), "CNY"),
                            "售后退款: " + afterSaleOrder.getReason()
                    );
                    if (refundSuccess) {
                        log.info("支付系统退款成功，订单号: {}, 退款金额: {}", orderNo, refundAmount);
                    } else {
                        log.error("支付系统退款失败，订单号: {}, 退款金额: {}", orderNo, refundAmount);
                        // 在实际项目中，这里可能需要处理退款失败的情况
                        // 例如抛出异常或记录错误状态
                    }
                });

        // 保存更新后的聚合根
        AfterSaleOrder completedAfterSaleOrder = afterSaleOrderRepository.save(afterSaleOrder);

        log.info("售后退款完成，售后单号: {}", completedAfterSaleOrder.getAfterSaleNo());

        // 转换为响应DTO
        return afterSaleOrderDtoAssembler.toAfterSaleOrderResponse(completedAfterSaleOrder);
    }
}
