package com.example.order.infrastructure.acl.user.impl;

import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;
import com.example.order.infrastructure.acl.user.UserClient;
import com.example.order.infrastructure.acl.user.dto.AddressDTO;
import com.example.order.infrastructure.acl.user.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户系统客户端实现（反腐层）
 * 负责将外部用户系统的数据转换为订单系统的领域模型
 */
@Component
public class UserClientImpl implements UserClient {

    private static final Logger logger = LoggerFactory.getLogger(UserClientImpl.class);

    /**
     * 模拟调用外部用户系统获取用户名称
     * 实际项目中应该通过HTTP、RPC等方式调用外部系统
     */
    @Override
    public String getUserNameById(Id userId) {
        logger.info("调用外部用户系统获取用户名称，用户ID: {}", userId.getValue());
        
        // 模拟外部系统调用
        UserDTO userDTO = mockGetUserById(userId.getValue());
        return userDTO != null ? userDTO.getUserName() : null;
    }

    /**
     * 模拟调用外部用户系统获取地址信息
     * 实际项目中应该通过HTTP、RPC等方式调用外部系统
     */
    @Override
    public Address getAddressById(Id addressId) {
        logger.info("调用外部用户系统获取地址信息，地址ID: {}", addressId.getValue());
        
        // 模拟外部系统调用
        AddressDTO addressDTO = mockGetAddressById(addressId.getValue());
        
        // 将外部系统的AddressDTO转换为订单系统的Address领域模型
        return convertToAddress(addressDTO);
    }

    /**
     * 模拟调用外部用户系统获取默认地址
     * 实际项目中应该通过HTTP、RPC等方式调用外部系统
     */
    @Override
    public Address getDefaultAddressByUserId(Id userId) {
        logger.info("调用外部用户系统获取默认地址，用户ID: {}", userId.getValue());
        
        // 模拟外部系统调用
        AddressDTO addressDTO = mockGetDefaultAddressByUserId(userId.getValue());
        
        // 将外部系统的AddressDTO转换为订单系统的Address领域模型
        return convertToAddress(addressDTO);
    }

    /**
     * 将外部系统的AddressDTO转换为订单系统的Address领域模型
     */
    private Address convertToAddress(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }
        
        return new Address(
                Id.of(addressDTO.getAddressId()),
                addressDTO.getReceiverName(),
                addressDTO.getReceiverPhone(),
                addressDTO.getProvince(),
                addressDTO.getCity(),
                addressDTO.getDistrict(),
                addressDTO.getDetailAddress(),
                addressDTO.getPostalCode()
        );
    }

    // ==================== 模拟外部系统调用 ====================
    
    private UserDTO mockGetUserById(Long userId) {
        // 模拟外部系统返回数据
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUserName("测试用户" + userId);
        userDTO.setPhone("13800138000");
        userDTO.setEmail("test" + userId + "@example.com");
        userDTO.setNickname("昵称" + userId);
        userDTO.setGender(1);
        userDTO.setStatus("ACTIVE");
        return userDTO;
    }

    private AddressDTO mockGetAddressById(Long addressId) {
        // 模拟外部系统返回数据
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(addressId);
        addressDTO.setUserId(1001L);
        addressDTO.setReceiverName("测试收货人");
        addressDTO.setReceiverPhone("13800138000");
        addressDTO.setProvince("北京市");
        addressDTO.setCity("北京市");
        addressDTO.setDistrict("朝阳区");
        addressDTO.setDetailAddress("测试街道123号");
        addressDTO.setPostalCode("100000");
        addressDTO.setIsDefault(false);
        addressDTO.setStatus(1);
        addressDTO.setTag("家");
        return addressDTO;
    }

    private AddressDTO mockGetDefaultAddressByUserId(Long userId) {
        // 模拟外部系统返回数据
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(10001L);
        addressDTO.setUserId(userId);
        addressDTO.setReceiverName("默认收货人");
        addressDTO.setReceiverPhone("13800138000");
        addressDTO.setProvince("上海市");
        addressDTO.setCity("上海市");
        addressDTO.setDistrict("浦东新区");
        addressDTO.setDetailAddress("默认街道456号");
        addressDTO.setPostalCode("200000");
        addressDTO.setIsDefault(true);
        addressDTO.setStatus(1);
        addressDTO.setTag("公司");
        return addressDTO;
    }
}
