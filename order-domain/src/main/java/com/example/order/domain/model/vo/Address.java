package com.example.order.domain.model.vo;

import java.io.Serializable;

/**
 * 地址值对象
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private Id addressId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String postalCode;

    public Address(Id addressId, String receiverName, String receiverPhone,
                  String province, String city, String district,
                  String detailAddress, String postalCode) {
        if (receiverName == null || receiverName.isEmpty()) {
            throw new IllegalArgumentException("收货人姓名不能为空");
        }
        if (receiverPhone == null || receiverPhone.isEmpty()) {
            throw new IllegalArgumentException("收货人电话不能为空");
        }
        if (province == null || province.isEmpty() || city == null || city.isEmpty() ||
            district == null || district.isEmpty() || detailAddress == null || detailAddress.isEmpty()) {
            throw new IllegalArgumentException("收货地址信息不完整");
        }
        this.addressId = addressId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
    }

    // Getters
    public Id getAddressId() {
        return addressId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}