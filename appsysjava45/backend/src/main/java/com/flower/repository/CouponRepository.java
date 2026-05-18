package com.flower.repository;

import com.flower.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券数据访问接口
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Page<Coupon> findByEnabled(Boolean enabled, Pageable pageable);

    List<Coupon> findByEnabled(Boolean enabled);

    List<Coupon> findByIsNewUserOnlyTrueAndEnabled(Boolean enabled);
}