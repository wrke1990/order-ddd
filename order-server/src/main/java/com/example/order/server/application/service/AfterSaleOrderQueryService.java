package com.example.order.server.application.service;

import com.example.order.server.application.dto.AfterSaleOrderResponse;

import java.util.List;

/**
 * 售后订单查询服务接口
 */
public interface AfterSaleOrderQueryService {

    /**
     * 根据售后单号获取售后订单
     */
    AfterSaleOrderResponse getAfterSaleOrderByNo(String afterSaleNo);



    /**
     * 根据用户ID获取售后订单列表
     */
    List<AfterSaleOrderResponse> getAfterSaleOrdersByUserId(Long userId);

    /**
     * 根据用户ID获取售后订单列表（分页）
     */
    List<AfterSaleOrderResponse> getAfterSaleOrdersByUserId(Long userId, Integer page, Integer size);
}
