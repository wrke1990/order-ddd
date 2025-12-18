package com.example.order.infrastructure.assember;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.AfterSaleStatus;
import com.example.order.domain.model.vo.AfterSaleType;
import com.example.order.infrastructure.persistence.po.AfterSaleOrderPO;

/**
 * 售后订单对象映射器
 */
@Component
public class AfterSaleOrderAssembler {

    /**
     * 售后订单领域对象转PO
     */
    public AfterSaleOrderPO toAfterSaleOrderPO(AfterSaleOrder afterSaleOrder) {
        if (afterSaleOrder == null) {
            return null;
        }

        AfterSaleOrderPO afterSaleOrderPO = new AfterSaleOrderPO();
        afterSaleOrderPO.setId(afterSaleOrder.getId());
        afterSaleOrderPO.setAfterSaleNo(afterSaleOrder.getAfterSaleNo());
        afterSaleOrderPO.setOrderNo(afterSaleOrder.getOrderNo());
        afterSaleOrderPO.setUserId(afterSaleOrder.getUserId());
        afterSaleOrderPO.setAfterSaleType(afterSaleOrder.getType().name());
        afterSaleOrderPO.setStatus(afterSaleOrder.getStatus().name());
        afterSaleOrderPO.setReason(afterSaleOrder.getReason());
        afterSaleOrderPO.setDescription(afterSaleOrder.getDescription());
        afterSaleOrderPO.setCreateTime(afterSaleOrder.getCreateTime());
        afterSaleOrderPO.setUpdateTime(afterSaleOrder.getUpdateTime());
        afterSaleOrderPO.setVersion(afterSaleOrder.getVersion());
        afterSaleOrderPO.setCustomerServiceId(afterSaleOrder.getCustomerServiceId());
        afterSaleOrderPO.setReverseLogisticsNo(afterSaleOrder.getReverseLogisticsNo());
        afterSaleOrderPO.setReviewReason(afterSaleOrder.getReviewReason());
        afterSaleOrderPO.setRefundReason(afterSaleOrder.getRefundReason());

        return afterSaleOrderPO;
    }

    /**
     * 售后订单PO转领域对象
     */
    public AfterSaleOrder toAfterSaleOrder(AfterSaleOrderPO afterSaleOrderPO) {
        if (afterSaleOrderPO == null) {
            return null;
        }

        // 创建空的售后商品项列表
        List<AfterSaleItem> afterSaleItems = new ArrayList<>();

        // 使用工厂方法创建售后订单
        AfterSaleOrder afterSaleOrder = AfterSaleOrder.create(
                afterSaleOrderPO.getAfterSaleNo(),
                0L, // orderId, 使用默认值
                afterSaleOrderPO.getOrderNo(),
                afterSaleOrderPO.getUserId(),
                AfterSaleType.valueOf(afterSaleOrderPO.getAfterSaleType()),
                afterSaleOrderPO.getReason(),
                afterSaleOrderPO.getDescription(),
                null, // images, 使用默认值
                afterSaleItems,
                false // adminInitiated, 使用默认值
        );

        // 使用反射设置其他属性
        try {
            java.lang.reflect.Field idField = AfterSaleOrder.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(afterSaleOrder, afterSaleOrderPO.getId());

            java.lang.reflect.Field afterSaleNoField = AfterSaleOrder.class.getDeclaredField("afterSaleNo");
            afterSaleNoField.setAccessible(true);
            afterSaleNoField.set(afterSaleOrder, afterSaleOrderPO.getAfterSaleNo());

            java.lang.reflect.Field statusField = AfterSaleOrder.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(afterSaleOrder, AfterSaleStatus.valueOf(afterSaleOrderPO.getStatus()));

            java.lang.reflect.Field updateTimeField = AfterSaleOrder.class.getDeclaredField("updateTime");
            updateTimeField.setAccessible(true);
            updateTimeField.set(afterSaleOrder, afterSaleOrderPO.getUpdateTime());

            java.lang.reflect.Field versionField = AfterSaleOrder.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(afterSaleOrder, afterSaleOrderPO.getVersion());

            java.lang.reflect.Field customerServiceIdField = AfterSaleOrder.class.getDeclaredField("customerServiceId");
            customerServiceIdField.setAccessible(true);
            customerServiceIdField.set(afterSaleOrder, afterSaleOrderPO.getCustomerServiceId());

            java.lang.reflect.Field reverseLogisticsNoField = AfterSaleOrder.class.getDeclaredField("reverseLogisticsNo");
            reverseLogisticsNoField.setAccessible(true);
            reverseLogisticsNoField.set(afterSaleOrder, afterSaleOrderPO.getReverseLogisticsNo());

            java.lang.reflect.Field reviewReasonField = AfterSaleOrder.class.getDeclaredField("reviewReason");
            reviewReasonField.setAccessible(true);
            reviewReasonField.set(afterSaleOrder, afterSaleOrderPO.getReviewReason());

            java.lang.reflect.Field refundReasonField = AfterSaleOrder.class.getDeclaredField("refundReason");
            refundReasonField.setAccessible(true);
            refundReasonField.set(afterSaleOrder, afterSaleOrderPO.getRefundReason());

            return afterSaleOrder;
        } catch (Exception e) {
            throw new RuntimeException("转换售后订单PO为领域对象失败", e);
        }
    }
}
