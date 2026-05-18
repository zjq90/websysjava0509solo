package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户优惠券数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    /**
     * 根据用户ID查询优惠券
     */
    List<UserCoupon> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 根据用户ID和状态查询
     */
    List<UserCoupon> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);

    /**
     * 根据用户ID和优惠券ID查询
     */
    List<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
