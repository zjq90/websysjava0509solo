package com.secondhand.repository;

import com.secondhand.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByStatusOrderByCreateTimeDesc(String status);

}
