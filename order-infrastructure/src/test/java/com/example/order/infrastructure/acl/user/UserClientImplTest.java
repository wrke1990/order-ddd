package com.example.order.infrastructure.acl.user;

import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.user.impl.UserClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 用户系统客户端实现测试类
 */
class UserClientImplTest {

    private UserClient userClient;

    @BeforeEach
    void setUp() {
        // 手动创建测试对象
        userClient = new UserClientImpl();
    }

    @Test
    void testGetUserNameById() {
        // 测试获取用户名称
        Id userId = Id.of(1001L);
        String userName = userClient.getUserNameById(userId);

        assertNotNull(userName);
        assertEquals("测试用户1001", userName);
    }

    @Test
    void testGetAddressById() {
        // 测试获取地址信息
        Id addressId = Id.of(10001L);
        Address address = userClient.getAddressById(addressId);

        assertNotNull(address);
        assertEquals(Id.of(10001L), address.getAddressId());
        assertEquals("测试收货人", address.getReceiverName());
        assertEquals("13800138000", address.getReceiverPhone());
        assertEquals("北京市", address.getProvince());
        assertEquals("北京市", address.getCity());
        assertEquals("朝阳区", address.getDistrict());
        assertEquals("测试街道123号", address.getDetailAddress());
        assertEquals("100000", address.getPostalCode());
    }

    @Test
    void testGetDefaultAddressByUserId() {
        // 测试获取默认地址
        Id userId = Id.of(1001L);
        Address defaultAddress = userClient.getDefaultAddressByUserId(userId);

        assertNotNull(defaultAddress);
        assertEquals(Id.of(10001L), defaultAddress.getAddressId());
        assertEquals("默认收货人", defaultAddress.getReceiverName());
        assertEquals("上海市", defaultAddress.getProvince());
        assertEquals("上海市", defaultAddress.getCity());
        assertEquals("浦东新区", defaultAddress.getDistrict());
        assertEquals("默认街道456号", defaultAddress.getDetailAddress());
        assertEquals("200000", defaultAddress.getPostalCode());
    }
}
