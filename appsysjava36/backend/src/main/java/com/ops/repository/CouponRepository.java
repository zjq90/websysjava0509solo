package com.ops.repository;

import com.ops.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券数据访问接口
 * 
 * @author ops-admin
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    /**
     * 根据优惠券编码查找
     */
    Coupon findByCouponCode(String couponCode);

    /**
     * 根据目标用户类型查找有效的优惠券
     */
    List<Coupon> findByTargetUserTypeAndStatus(String targetUserType, String status);
}
