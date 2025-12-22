package com.example.order.domain.service;

import java.util.List;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.domain.model.vo.Price;

/**
 * 售后单领域服务接口
 */
public interface AfterSaleOrderDomainService {

    /**
     * 创建售后单
     * @param afterSaleOrder 售后单
     * @return 创建后的售后单
     */
    AfterSaleOrder createAfterSaleOrder(AfterSaleOrder afterSaleOrder);

    /**
     * 创建多商品售后单
     * @param afterSaleNo 售后单号
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param type 售后类型
     * @param reason 售后原因
     * @param description 售后描述
     * @param images 售后图片
     * @param afterSaleItems 售后商品项列表
     * @return 创建后的售后单
     */
    AfterSaleOrder createAfterSaleOrder(String afterSaleNo, String orderNo, Long userId,
                                       AfterSaleType type, String reason, String description, String images,
                                       List<AfterSaleItem> afterSaleItems);

    /**
     * 创建多商品超级退款
     * @param afterSaleNo 售后单号
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param afterSaleItems 售后商品项列表
     * @return 创建后的售后单
     */
    AfterSaleOrder createSuperRefund(String afterSaleNo, String orderNo, Long userId,
                                    List<AfterSaleItem> afterSaleItems);

    /**
     * 审批售后订单（通过/拒绝）
     * @param afterSaleNo 售后单号
     * @param approved 是否通过
     * @param reason 原因
     * @return 处理后的售后订单
     */
    AfterSaleOrder approveOrRejectAfterSaleOrder(String afterSaleNo, boolean approved, String reason);

    /**
     * 审批通过售后订单
     * @param afterSaleNo 售后单号
     * @param reason 审批原因
     * @return 审批后的售后订单
     */
    AfterSaleOrder approveAfterSaleOrder(String afterSaleNo, String reason);

    /**
     * 拒绝售后订单
     * @param afterSaleNo 售后单号
     * @param reason 拒绝原因
     * @return 拒绝后的售后订单
     */
    AfterSaleOrder rejectAfterSaleOrder(String afterSaleNo, String reason);

    /**
     * 提交退货物流
     * @param afterSaleNo 售后单号
     * @param logisticsCompany 物流公司
     * @param trackingNumber 运单号
     * @return 更新后的售后单
     */
    AfterSaleOrder submitReturnLogistics(String afterSaleNo, String logisticsCompany, String trackingNumber);

    /**
     * 确认退款
     * @param afterSaleNo 售后单号
     * @param refundAmount 退款金额
     * @return 更新后的售后单
     */
    AfterSaleOrder confirmRefund(String afterSaleNo, Price refundAmount);

    /**
     * 确认收到退货
     * @param afterSaleNo 售后单号
     * @return 更新后的售后单
     */
    AfterSaleOrder confirmReturn(String afterSaleNo);

    /**
     * 更新售后单
     * @param afterSaleOrder 售后单聚合根
     * @return 更新后的售后单
     */
    AfterSaleOrder updateAfterSaleOrder(AfterSaleOrder afterSaleOrder);
}
