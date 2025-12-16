package com.example.order.infrastructure.acl.user;

import com.example.order.domain.model.vo.Address;
import com.example.order.domain.model.vo.Id;

/**
 * 用户系统客户端接口（反腐层）
 * 用于与外部用户系统交互，获取用户和地址信息
 */
public interface UserClient {

    /**
     * 根据用户ID获取用户名称
     * @param userId 用户ID
     * @return 用户名称
     */
    String getUserNameById(Id userId);

    /**
     * 根据地址ID获取地址信息
     * @param addressId 地址ID
     * @return 地址信息
     */
    Address getAddressById(Id addressId);

    /**
     * 根据用户ID获取默认地址
     * @param userId 用户ID
     * @return 默认地址
     */
    Address getDefaultAddressByUserId(Id userId);
}
