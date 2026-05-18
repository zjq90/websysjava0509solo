package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByStatusOrderByCreateTimeDesc(Integer status);

    List<Coupon> findByStatusOrderBySortOrderAsc(Integer status);

    List<Coupon> findByPointExchangeAndStatus(Integer pointExchange, Integer status);
}
