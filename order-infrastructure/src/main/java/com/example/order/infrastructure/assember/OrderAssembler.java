package com.example.order.infrastructure.assember;

import com.example.order.domain.model.aggregate.Order;
import com.example.order.domain.model.entity.OrderItem;
import com.example.order.domain.model.vo.Id;
import com.example.order.domain.model.vo.OrderStatus;
import com.example.order.domain.model.vo.Price;
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

        // 使用反射创建订单对象
        try {
            // 获取私有构造函数
            java.lang.reflect.Constructor<Order> constructor = Order.class.getDeclaredConstructor(Id.class, List.class);
            constructor.setAccessible(true);

            // 创建订单对象
            Order order = constructor.newInstance(Id.of(orderPO.getUserId()), orderItems);

            // 使用反射设置其他属性
            java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, Id.of(orderPO.getId()));

            java.lang.reflect.Field orderNoField = Order.class.getDeclaredField("orderNo");
            orderNoField.setAccessible(true);
            orderNoField.set(order, orderPO.getOrderNo());

            java.lang.reflect.Field statusField = Order.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(order, OrderStatus.valueOf(orderPO.getStatus()));

            java.lang.reflect.Field totalAmountField = Order.class.getDeclaredField("totalAmount");
            totalAmountField.setAccessible(true);
            totalAmountField.set(order, new Price(orderPO.getTotalAmount(), orderPO.getCurrency()));

            java.lang.reflect.Field updateTimeField = Order.class.getDeclaredField("updateTime");
            updateTimeField.setAccessible(true);
            updateTimeField.set(order, orderPO.getUpdateTime());

            java.lang.reflect.Field versionField = Order.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(order, orderPO.getVersion());

            return order;
        } catch (Exception e) {
            throw new RuntimeException("转换订单PO为领域对象失败", e);
        }
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
        OrderItem orderItem = OrderItem.create(Id.of(orderItemPO.getProductId()),
                                               orderItemPO.getProductName(),
                                               orderItemPO.getQuantity(),
                                               new Price(orderItemPO.getPrice(), orderItemPO.getCurrency()));

        // 使用反射设置其他属性
        try {
            java.lang.reflect.Field idField = OrderItem.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(orderItem, Id.of(orderItemPO.getId()));

            java.lang.reflect.Field orderNoField = OrderItem.class.getDeclaredField("orderNo");
            orderNoField.setAccessible(true);
            orderNoField.set(orderItem, orderItemPO.getOrderNo());

            return orderItem;
        } catch (Exception e) {
            throw new RuntimeException("转换订单项PO为领域对象失败", e);
        }
    }
}
