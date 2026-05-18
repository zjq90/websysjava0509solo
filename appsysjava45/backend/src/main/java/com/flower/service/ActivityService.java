package com.flower.service;

import com.flower.entity.*;
import com.flower.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private FlashSaleRepository flashSaleRepository;

    @Autowired
    private GroupBuyRepository groupBuyRepository;

    @Autowired
    private PickupPointRepository pickupPointRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findByEnabled(true);
    }

    public List<Coupon> getNewUserCoupons() {
        return couponRepository.findByIsNewUserOnlyTrueAndEnabled(true);
    }

    public UserCoupon receiveCoupon(Long userId, Long couponId) {
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new RuntimeException("已领取该优惠券");
        }
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus("AVAILABLE");
        userCoupon.setExpireTime(coupon.getEndTime());
        return userCouponRepository.save(userCoupon);
    }

    public List<UserCoupon> getUserCoupons(Long userId) {
        return userCouponRepository.findByUserId(userId);
    }

    public List<UserCoupon> getUserCouponsByStatus(Long userId, String status) {
        return userCouponRepository.findByUserIdAndStatus(userId, status);
    }

    public List<FlashSale> getActiveFlashSales() {
        return flashSaleRepository.findActiveFlashSales(LocalDateTime.now());
    }

    public List<GroupBuy> getActiveGroupBuys() {
        return groupBuyRepository.findActiveGroupBuys(LocalDateTime.now());
    }

    public List<PickupPoint> getAllPickupPoints() {
        return pickupPointRepository.findByEnabled(true);
    }
}