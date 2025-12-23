package com.example.order.api.facade;

import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.req.AfterSaleStatus;
import com.example.order.api.vo.resp.AfterSaleResp;
import com.example.order.common.response.CommonResponse;

import java.util.List;

/**
 * 售后API接口
 */
public interface AfterSaleApiFacade {

    /**
     * 创建售后订单
     */
    CommonResponse<AfterSaleResp> createAfterSale(CreateAfterSaleReq req);

    /**
     * 查询售后订单详情
     */
    CommonResponse<AfterSaleResp> getAfterSale(Long afterSaleId);

    /**
     * 查询用户售后订单列表
     */
    CommonResponse<List<AfterSaleResp>> getAfterSalesByUserId(Long userId, Integer page, Integer size);

    /**
     * 按状态查询售后订单
     */
    CommonResponse<List<AfterSaleResp>> getAfterSalesByUserIdAndStatus(Long userId, AfterSaleStatus status, Integer page, Integer size);

    /**
     * 取消售后订单
     */
    CommonResponse<Void> cancelAfterSale(Long afterSaleId);

    /**
     * 审批售后订单
     */
    CommonResponse<Void> approveAfterSale(Long afterSaleId, String reason);

    /**
     * 拒绝售后订单
     */
    CommonResponse<Void> rejectAfterSale(Long afterSaleId, String reason);

    /**
     * 完成退款
     */
    CommonResponse<Void> completeRefund(Long afterSaleId, Double refundAmount);

}