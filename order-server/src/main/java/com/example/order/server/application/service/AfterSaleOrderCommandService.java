package com.example.order.server.application.service;

import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;

/**
 * 售后订单命令服务接口
 */
public interface AfterSaleOrderCommandService {

    /**
     * 创建售后订单
     */
    AfterSaleOrderResponse createAfterSaleOrder(CreateAfterSaleOrderCommand request);

    /**
     * 取消售后订单
     */
    AfterSaleOrderResponse cancelAfterSaleOrder(String afterSaleNo);

    /**
     * 审核售后订单
     */
    AfterSaleOrderResponse approveAfterSaleOrder(String afterSaleNo, String reason);

    /**
     * 拒绝售后订单
     */
    AfterSaleOrderResponse rejectAfterSaleOrder(String afterSaleNo, String reason);

    /**
     * 完成售后退款
     */
    AfterSaleOrderResponse completeRefund(String afterSaleNo, Double refundAmount);
}
