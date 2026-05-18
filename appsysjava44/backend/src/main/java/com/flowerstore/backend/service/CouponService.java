package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.Coupon;
import com.flowerstore.backend.entity.UserCoupon;
import com.flowerstore.backend.repository.CouponRepository;
import com.flowerstore.backend.repository.UserCouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 优惠券服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    /**
     * 获取可用优惠券列表
     */
    public Result<List<Coupon>> getAvailableCoupons() {
        List<Coupon> coupons = couponRepository.findByStatusOrderBySortOrderAsc(1);
        return Result.success(coupons);
    }

    /**
     * 获取用户优惠券列表
     */
    public Result<List<UserCoupon>> getUserCoupons(Long userId, Integer status) {
        List<UserCoupon> userCoupons;
        if (status != null) {
            userCoupons = userCouponRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
        } else {
            userCoupons = userCouponRepository.findByUserIdOrderByCreateTimeDesc(userId);
        }
        return Result.success(userCoupons);
    }

    /**
     * 领取优惠券
     */
    @Transactional
    public Result<UserCoupon> receiveCoupon(Long userId, Long couponId) {
        Optional<Coupon> couponOpt = couponRepository.findById(couponId);
        if (couponOpt.isEmpty()) {
            return Result.error("优惠券不存在");
        }
        Coupon coupon = couponOpt.get();
        
        if (coupon.getStatus() != 1) {
            return Result.error("优惠券已失效");
        }
        
        List<UserCoupon> received = userCouponRepository.findByUserIdAndCouponId(userId, couponId);
        if (coupon.getPerLimit() != null && received.size() >= coupon.getPerLimit()) {
            return Result.error("已达到领取上限");
        }
        
        if (coupon.getReceiveCount() >= coupon.getTotalCount()) {
            return Result.error("优惠券已领完");
        }
        
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0);
        userCoupon.setExpireTime(LocalDateTime.now().plusDays(30));
        userCoupon = userCouponRepository.save(userCoupon);
        
        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        couponRepository.save(coupon);
        
        return Result.success("领取成功", userCoupon);
    }

    /**
     * 积分兑换优惠券
     */
    @Transactional
    public Result<UserCoupon> exchangeCoupon(Long userId, Long couponId) {
        Optional<Coupon> couponOpt = couponRepository.findById(couponId);
        if (couponOpt.isEmpty()) {
            return Result.error("优惠券不存在");
        }
        Coupon coupon = couponOpt.get();
        
        if (coupon.getPointExchange() == null || coupon.getPointExchange() == 0) {
            return Result.error("该优惠券不支持积分兑换");
        }
        
        return receiveCoupon(userId, couponId);
    }
}
