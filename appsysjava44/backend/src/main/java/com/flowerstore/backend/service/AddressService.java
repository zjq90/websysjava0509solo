package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.UserAddress;
import com.flowerstore.backend.repository.UserAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 收货地址服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class AddressService {

    @Autowired
    private UserAddressRepository addressRepository;

    /**
     * 获取用户地址列表
     */
    public Result<List<UserAddress>> getUserAddresses(Long userId) {
        List<UserAddress> addresses = addressRepository.findByUserIdOrderByIsDefaultDescAndCreateTimeDesc(userId);
        return Result.success(addresses);
    }

    /**
     * 获取地址详情
     */
    public Result<UserAddress> getAddressDetail(Long userId, Long addressId) {
        Optional<UserAddress> addressOpt = addressRepository.findByUserIdAndId(userId, addressId);
        if (addressOpt.isEmpty()) {
            return Result.error("地址不存在");
        }
        return Result.success(addressOpt.get());
    }

    /**
     * 添加地址
     */
    @Transactional
    public Result<UserAddress> addAddress(Long userId, UserAddress address) {
        address.setUserId(userId);
        
        if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        
        if (address.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        
        address = addressRepository.save(address);
        return Result.success("地址添加成功", address);
    }

    /**
     * 更新地址
     */
    @Transactional
    public Result<UserAddress> updateAddress(Long userId, Long addressId, UserAddress address) {
        Optional<UserAddress> existOpt = addressRepository.findByUserIdAndId(userId, addressId);
        if (existOpt.isEmpty()) {
            return Result.error("地址不存在");
        }
        
        UserAddress existAddress = existOpt.get();
        
        if (address.getReceiverName() != null) existAddress.setReceiverName(address.getReceiverName());
        if (address.getReceiverPhone() != null) existAddress.setReceiverPhone(address.getReceiverPhone());
        if (address.getProvince() != null) existAddress.setProvince(address.getProvince());
        if (address.getCity() != null) existAddress.setCity(address.getCity());
        if (address.getDistrict() != null) existAddress.setDistrict(address.getDistrict());
        if (address.getDetailAddress() != null) existAddress.setDetailAddress(address.getDetailAddress());
        if (address.getIsDefault() != null) {
            if (address.getIsDefault() == 1) {
                clearDefaultAddress(userId);
            }
            existAddress.setIsDefault(address.getIsDefault());
        }
        
        addressRepository.save(existAddress);
        return Result.success("地址更新成功", existAddress);
    }

    /**
     * 删除地址
     */
    @Transactional
    public Result<String> deleteAddress(Long userId, Long addressId) {
        Optional<UserAddress> addressOpt = addressRepository.findByUserIdAndId(userId, addressId);
        if (addressOpt.isEmpty()) {
            return Result.error("地址不存在");
        }
        addressRepository.deleteById(addressId);
        return Result.success("地址删除成功");
    }

    /**
     * 设置默认地址
     */
    @Transactional
    public Result<String> setDefaultAddress(Long userId, Long addressId) {
        Optional<UserAddress> addressOpt = addressRepository.findByUserIdAndId(userId, addressId);
        if (addressOpt.isEmpty()) {
            return Result.error("地址不存在");
        }
        
        clearDefaultAddress(userId);
        
        UserAddress address = addressOpt.get();
        address.setIsDefault(1);
        addressRepository.save(address);
        
        return Result.success("设置默认地址成功");
    }

    /**
     * 清除用户的默认地址标记
     */
    private void clearDefaultAddress(Long userId) {
        Optional<UserAddress> defaultOpt = addressRepository.findByUserIdAndIsDefault(userId, 1);
        if (defaultOpt.isPresent()) {
            UserAddress defaultAddress = defaultOpt.get();
            defaultAddress.setIsDefault(0);
            addressRepository.save(defaultAddress);
        }
    }
}
