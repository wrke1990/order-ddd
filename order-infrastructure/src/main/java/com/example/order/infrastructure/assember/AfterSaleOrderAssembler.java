package com.example.order.infrastructure.assember;

import com.example.order.domain.model.aggregate.AfterSaleOrder;
import com.example.order.domain.model.entity.AfterSaleItem;
import com.example.order.domain.model.vo.*;
import com.example.order.infrastructure.persistence.po.AddressPO;
import com.example.order.infrastructure.persistence.po.AfterSaleItemPO;
import com.example.order.infrastructure.persistence.po.AfterSaleOrderPO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // 设置总退款金额和货币
        afterSaleOrderPO.setTotalRefundAmount(afterSaleOrder.getTotalRefundAmount().getAmount());
        afterSaleOrderPO.setTotalCurrency(afterSaleOrder.getTotalRefundAmount().getCurrency());

        // 设置退货地址信息
        if (afterSaleOrder.getReturnAddress() != null) {
            AddressPO addressPO = toAddressPO(afterSaleOrder.getReturnAddress(), afterSaleOrder.getAfterSaleNo(), "RETURN");
            afterSaleOrderPO.setReturnAddress(addressPO);
        }

        // 转换售后商品项
        if (afterSaleOrder.getAfterSaleItems() != null && !afterSaleOrder.getAfterSaleItems().isEmpty()) {
            List<AfterSaleItemPO> afterSaleItemPOs = afterSaleOrder.getAfterSaleItems().stream()
                    .map(this::toAfterSaleItemPO)
                    .collect(Collectors.toList());
            afterSaleOrderPO.setAfterSaleItems(afterSaleItemPOs);
        }

        return afterSaleOrderPO;
    }

    /**
     * 售后商品项领域对象转PO
     */
    private AfterSaleItemPO toAfterSaleItemPO(AfterSaleItem afterSaleItem) {
        if (afterSaleItem == null) {
            return null;
        }

        AfterSaleItemPO afterSaleItemPO = new AfterSaleItemPO();
        afterSaleItemPO.setAfterSaleNo(afterSaleItem.getAfterSaleNo());
        afterSaleItemPO.setProductId(afterSaleItem.getProductId());
        afterSaleItemPO.setProductName(afterSaleItem.getProductName());
        afterSaleItemPO.setProductImage(afterSaleItem.getProductImage());
        afterSaleItemPO.setQuantity(afterSaleItem.getQuantity());
        // 使用productPrice代替applyAmount，因为AfterSaleItem类中没有applyAmount方法
        afterSaleItemPO.setApplyAmount(afterSaleItem.getProductPrice().getAmount());
        afterSaleItemPO.setCurrency(afterSaleItem.getProductPrice().getCurrency());
        afterSaleItemPO.setRefundQuantity(afterSaleItem.getRefundQuantity());
        afterSaleItemPO.setRefundAmount(afterSaleItem.getRefundAmount().getAmount());
        // 设置默认状态为"PENDING_PROCESS"
        afterSaleItemPO.setStatus(AfterSaleStatus.PENDING_PROCESS.name());
        // 设置当前时间为创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        afterSaleItemPO.setCreateTime(now);
        afterSaleItemPO.setUpdateTime(now);

        return afterSaleItemPO;
    }

    /**
     * 售后订单PO转领域对象
     */
    public AfterSaleOrder toAfterSaleOrder(AfterSaleOrderPO afterSaleOrderPO) {
        if (afterSaleOrderPO == null) {
            return null;
        }

        // 转换售后商品项
        List<AfterSaleItem> afterSaleItems = new ArrayList<>();
        if (afterSaleOrderPO.getAfterSaleItems() != null && !afterSaleOrderPO.getAfterSaleItems().isEmpty()) {
            afterSaleItems = afterSaleOrderPO.getAfterSaleItems().stream()
                    .map(this::toAfterSaleItem)
                    .collect(Collectors.toList());
        }

        // 创建总退款金额对象
        Price totalRefundAmount = Price.ofCNY(afterSaleOrderPO.getTotalRefundAmount());

        // 构建退货地址
        Address returnAddress = toAddress(afterSaleOrderPO.getReturnAddress());

        // 使用reconstruct方法创建售后订单对象，替代反射
        return AfterSaleOrder.reconstruct(
                afterSaleOrderPO.getId(),
                afterSaleOrderPO.getAfterSaleNo(),
                afterSaleOrderPO.getOrderNo(),
                afterSaleOrderPO.getUserId(),
                AfterSaleType.valueOf(afterSaleOrderPO.getAfterSaleType()),
                AfterSaleStatus.valueOf(afterSaleOrderPO.getStatus()),
                afterSaleOrderPO.getReason(),
                afterSaleOrderPO.getDescription(),
                null, // images, 从PO中无法获取
                false, // adminInitiated, 从PO中无法获取
                afterSaleOrderPO.getCreateTime(),
                afterSaleOrderPO.getUpdateTime(),
                afterSaleOrderPO.getVersion(),
                afterSaleOrderPO.getCustomerServiceId(),
                afterSaleOrderPO.getReverseLogisticsNo(),
                afterSaleOrderPO.getReviewReason(),
                afterSaleOrderPO.getRefundReason(),
                returnAddress, // 从PO中构建退货地址
                totalRefundAmount,
                afterSaleItems
        );
    }

    /**
     * 售后商品项PO转领域对象
     */
    private AfterSaleItem toAfterSaleItem(AfterSaleItemPO afterSaleItemPO) {
        if (afterSaleItemPO == null) {
            return null;
        }

        // 使用applyAmount作为商品价格（因为AfterSaleItemPO中没有productPrice字段）
        Price productPrice = Price.ofCNY(afterSaleItemPO.getApplyAmount());

        // 创建售后商品项
        AfterSaleItem item = AfterSaleItem.create(
                afterSaleItemPO.getProductId(),
                afterSaleItemPO.getProductName(),
                afterSaleItemPO.getProductImage(),
                afterSaleItemPO.getQuantity(),
                productPrice,
                afterSaleItemPO.getRefundQuantity()
        );

        // 设置其他属性
        item.setId(afterSaleItemPO.getId());
        item.setAfterSaleNo(afterSaleItemPO.getAfterSaleNo());
        if (afterSaleItemPO.getRefundAmount() != null) {
            item.setRefundAmount(Price.ofCNY(afterSaleItemPO.getRefundAmount()));
        }

        return item;
    }

    /**
     * 地址领域对象转PO
     */
    private AddressPO toAddressPO(Address address, String relatedNo, String addressType) {
        if (address == null) {
            return null;
        }

        AddressPO addressPO = new AddressPO();
        if (address.getAddressId() != null) {
            addressPO.setId(address.getAddressId().getValue());
        }
        addressPO.setReceiverName(address.getReceiverName());
        addressPO.setReceiverPhone(address.getReceiverPhone());
        addressPO.setProvince(address.getProvince());
        addressPO.setCity(address.getCity());
        addressPO.setDistrict(address.getDistrict());
        addressPO.setDetailAddress(address.getDetailAddress());
        addressPO.setPostalCode(address.getPostalCode());
        addressPO.setRelatedNo(relatedNo);
        addressPO.setAddressType(addressType);

        return addressPO;
    }

    /**
     * 地址PO转领域对象
     */
    private Address toAddress(AddressPO addressPO) {
        if (addressPO == null) {
            return null;
        }

        return new Address(
                addressPO.getId() != null ? Id.of(addressPO.getId()) : null,
                addressPO.getReceiverName(),
                addressPO.getReceiverPhone(),
                addressPO.getProvince(),
                addressPO.getCity(),
                addressPO.getDistrict(),
                addressPO.getDetailAddress(),
                addressPO.getPostalCode()
        );
    }
}
