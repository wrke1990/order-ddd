package com.example.order.infrastructure.assember;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Price;
import com.example.order.infrastructure.persistence.po.AddressPO;
import com.example.order.infrastructure.persistence.po.OrderItemPO;
import com.example.order.infrastructure.persistence.po.OrderPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单对象映射器
 */
@Component
public class OrderAssembler {

    /**
     * 订单领域对象转PO
     */
    public OrderPO toOrderPO(Order order) {
        if (order == null) {
            return null;
        }

        OrderPO orderPO = new OrderPO();
        orderPO.setId(order.getId().getValue());
        orderPO.setUserId(order.getUserId().getValue());
        orderPO.setOrderNo(order.getOrderNo());
        orderPO.setTotalAmount(order.getTotalAmount().getAmount());
        orderPO.setCurrency(order.getTotalAmount().getCurrency());
        orderPO.setStatus(order.getStatus().name());
        orderPO.setCreateTime(order.getCreateTime());
        orderPO.setUpdateTime(order.getUpdateTime());
        orderPO.setVersion(order.getVersion());

        // 转换地址信息
        if (order.getShippingAddress() != null) {
            AddressPO addressPO = toAddressPO(order.getShippingAddress(), order.getOrderNo(), "SHIPPING");
            orderPO.setShippingAddress(addressPO);
        }

        // 转换订单项
        if (order.getOrderItems() != null) {
            List<OrderItemPO> orderItemPOs = order.getOrderItems().stream()
                    .map(this::toOrderItemPO)
                    .collect(Collectors.toList());
            orderPO.setOrderItems(orderItemPOs);
        }

        return orderPO;
    }

    /**
     * 更新订单PO的版本号
     */
    public void updateVersion(OrderPO orderPO, Integer version) {
        if (orderPO != null) {
            orderPO.setVersion(version);
        }
    }

    /**
     * 订单PO转领域对象
     */
    public Order toOrder(OrderPO orderPO) {
        if (orderPO == null) {
            return null;
        }

        // 转换订单项
        List<OrderItem> orderItems = new ArrayList<>();
        if (orderPO.getOrderItems() != null) {
            orderItems = orderPO.getOrderItems().stream()
                    .map(this::toOrderItem)
                    .collect(Collectors.toList());
        }

        // 转换地址信息
        Address shippingAddress = toAddress(orderPO.getShippingAddress());

        // 使用reconstruct方法创建订单对象，替代反射
        return Order.reconstruct(
                Id.of(orderPO.getId()),
                Id.of(orderPO.getUserId()),
                orderPO.getOrderNo(),
                OrderStatus.valueOf(orderPO.getStatus()),
                new Price(orderPO.getTotalAmount(), orderPO.getCurrency()),
                orderPO.getCreateTime(),
                orderPO.getUpdateTime(),
                null, // expireTime，从PO中无法获取
                orderPO.getVersion(),
                orderItems,
                shippingAddress
        );
    }

    /**
     * 订单项领域对象转PO
     */
    public OrderItemPO toOrderItemPO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemPO orderItemPO = new OrderItemPO();
        orderItemPO.setId(orderItem.getId().getValue());
        orderItemPO.setOrderNo(orderItem.getOrderNo());
        orderItemPO.setProductId(orderItem.getProductId().getValue());
        orderItemPO.setProductName(orderItem.getProductName());
        orderItemPO.setQuantity(orderItem.getQuantity());
        orderItemPO.setPrice(orderItem.getPrice().getAmount());
        orderItemPO.setCurrency(orderItem.getPrice().getCurrency());
        orderItemPO.setTotalPrice(orderItem.getTotalPrice().getAmount());

        return orderItemPO;
    }

    /**
     * 订单项PO转领域对象
     */
    public OrderItem toOrderItem(OrderItemPO orderItemPO) {
        if (orderItemPO == null) {
            return null;
        }

        // 使用工厂方法创建订单项
        OrderItem orderItem = OrderItem.create(
                Id.of(orderItemPO.getProductId()),
                orderItemPO.getProductName(),
                orderItemPO.getQuantity(),
                new Price(orderItemPO.getPrice(), orderItemPO.getCurrency())
        );

        // 使用公共setter方法设置属性，替代反射
        orderItem.setId(Id.of(orderItemPO.getId()));
        orderItem.setOrderNo(orderItemPO.getOrderNo());

        return orderItem;
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
