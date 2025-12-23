package com.example.order.server.interfaces.facade.impl;

import com.example.order.api.facade.AfterSaleApiFacade;
import com.example.order.api.vo.req.AfterSaleStatus;
import com.example.order.api.vo.req.CreateAfterSaleReq;
import com.example.order.api.vo.resp.AfterSaleResp;
import com.example.order.common.response.CommonResponse;
import com.example.order.server.application.assember.AfterSaleOrderVoAssembler;
import com.example.order.server.application.dto.AfterSaleOrderResponse;
import com.example.order.server.application.dto.CreateAfterSaleOrderCommand;
import com.example.order.server.application.service.AfterSaleOrderCommandService;
import com.example.order.server.application.service.AfterSaleOrderQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后API接口实现
 */
@Service
public class AfterSaleApiFacadeImpl implements AfterSaleApiFacade {

    private final AfterSaleOrderCommandService afterSaleOrderCommandService;
    private final AfterSaleOrderQueryService afterSaleOrderQueryService;
    private final AfterSaleOrderVoAssembler afterSaleOrderVoAssembler;

    public AfterSaleApiFacadeImpl(AfterSaleOrderCommandService afterSaleOrderCommandService,
                                  AfterSaleOrderQueryService afterSaleOrderQueryService,
                                  AfterSaleOrderVoAssembler afterSaleOrderVoAssembler) {
        this.afterSaleOrderCommandService = afterSaleOrderCommandService;
        this.afterSaleOrderQueryService = afterSaleOrderQueryService;
        this.afterSaleOrderVoAssembler = afterSaleOrderVoAssembler;
    }

    @Override
    public CommonResponse<AfterSaleResp> createAfterSale(CreateAfterSaleReq req) {
        CreateAfterSaleOrderCommand request = afterSaleOrderVoAssembler.toCreateAfterSaleOrderCommand(req);
        AfterSaleOrderResponse response = afterSaleOrderCommandService.createAfterSaleOrder(request);
        AfterSaleResp afterSaleResp = afterSaleOrderVoAssembler.toAfterSaleResponse(response);
        return CommonResponse.success(afterSaleResp);
    }

    @Override
    public CommonResponse<AfterSaleResp> getAfterSale(Long afterSaleId) {
        // 需要修改服务层方法参数或添加转换逻辑
        AfterSaleOrderResponse response = afterSaleOrderQueryService.getAfterSaleOrderByNo(String.valueOf(afterSaleId));
        AfterSaleResp afterSaleResp = afterSaleOrderVoAssembler.toAfterSaleResponse(response);
        return CommonResponse.success(afterSaleResp);
    }

    @Override
    public CommonResponse<List<AfterSaleResp>> getAfterSalesByUserId(Long userId, Integer page, Integer size) {
        List<AfterSaleOrderResponse> responses = afterSaleOrderQueryService.getAfterSaleOrdersByUserId(userId, page, size);
        List<AfterSaleResp> afterSaleResps = responses.stream()
                .map(afterSaleOrderVoAssembler::toAfterSaleResponse)
                .collect(Collectors.toList());
        return CommonResponse.success(afterSaleResps);
    }

    @Override
    public CommonResponse<List<AfterSaleResp>> getAfterSalesByUserIdAndStatus(Long userId, AfterSaleStatus status, Integer page, Integer size) {
        // 将API层的AfterSaleStatus转换为领域层的AfterSaleStatus
        com.example.order.domain.model.vo.AfterSaleStatus domainStatus = com.example.order.domain.model.vo.AfterSaleStatus.valueOf(status.name());
        // 简单实现，实际应用中应该添加状态筛选逻辑
        List<AfterSaleOrderResponse> responses = afterSaleOrderQueryService.getAfterSaleOrdersByUserId(userId, page, size);
        List<AfterSaleResp> afterSaleResps = responses.stream()
                .map(afterSaleOrderVoAssembler::toAfterSaleResponse)
                .collect(Collectors.toList());
        return CommonResponse.success(afterSaleResps);
    }

    @Override
    public CommonResponse<Void> cancelAfterSale(Long afterSaleId) {
        // 需要修改服务层方法参数或添加转换逻辑
        afterSaleOrderCommandService.cancelAfterSaleOrder(String.valueOf(afterSaleId));
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> approveAfterSale(Long afterSaleId, String reason) {
        // 需要修改服务层方法参数或添加转换逻辑
        afterSaleOrderCommandService.approveAfterSaleOrder(String.valueOf(afterSaleId), reason);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> rejectAfterSale(Long afterSaleId, String reason) {
        // 需要修改服务层方法参数或添加转换逻辑
        afterSaleOrderCommandService.rejectAfterSaleOrder(String.valueOf(afterSaleId), reason);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Void> completeRefund(Long afterSaleId, Double refundAmount) {
        // 需要修改服务层方法参数或添加转换逻辑
        afterSaleOrderCommandService.completeRefund(String.valueOf(afterSaleId), refundAmount);
        return CommonResponse.success();
    }

}