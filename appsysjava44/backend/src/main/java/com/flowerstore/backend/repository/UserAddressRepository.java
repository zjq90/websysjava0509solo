package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户收货地址数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    /**
     * 根据用户ID查询地址列表
     */
    List<UserAddress> findByUserIdOrderByIsDefaultDescAndCreateTimeDesc(Long userId);

    /**
     * 根据用户ID查询默认地址
     */
    Optional<UserAddress> findByUserIdAndIsDefault(Long userId, Integer isDefault);

    /**
     * 根据用户ID和地址ID查询
     */
    Optional<UserAddress> findByUserIdAndId(Long userId, Long id);
}
