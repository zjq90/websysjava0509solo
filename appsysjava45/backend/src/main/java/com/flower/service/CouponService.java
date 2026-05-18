package com.flower.service;

import com.flower.entity.Coupon;
import com.flower.entity.User;
import com.flower.entity.UserCoupon;
import com.flower.repository.CouponRepository;
import com.flower.repository.UserCouponRepository;
import com.flower.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务类
 */
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Coupon> getCouponList(Pageable pageable) {
        return couponRepository.findByEnabled(true, pageable);
    }

    public List<Coupon> getNewUserCoupons() {
        return couponRepository.findByIsNewUserOnlyTrueAndEnabled(true);
    }

    public List<UserCoupon> getUserCoupons(Long userId, String status) {
        if (status != null && !status.isEmpty()) {
            return userCouponRepository.findByUserIdAndStatus(userId, status);
        }
        return userCouponRepository.findByUserId(userId);
    }

    @Transactional
    public UserCoupon receiveCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));

        if (!coupon.getEnabled()) {
            throw new RuntimeException("优惠券已失效");
        }

        if (coupon.getIsNewUserOnly() && !user.getIsNewUser()) {
            throw new RuntimeException("仅限新用户领取");
        }

        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new RuntimeException("已领取过该优惠券");
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus("unused");

        if (coupon.getValidDays() != null) {
            userCoupon.setExpireTime(LocalDateTime.now().plusDays(coupon.getValidDays()));
        } else if (coupon.getEndTime() != null) {
            userCoupon.setExpireTime(coupon.getEndTime());
        }

        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        couponRepository.save(coupon);

        return userCouponRepository.save(userCoupon);
    }

    @Transactional
    public void grantNewUserCoupons(Long userId) {
        List<Coupon> newUserCoupons = getNewUserCoupons();
        for (Coupon coupon : newUserCoupons) {
            if (!userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setUserId(userId);
                userCoupon.setCouponId(coupon.getId());
                userCoupon.setStatus("unused");
                if (coupon.getValidDays() != null) {
                    userCoupon.setExpireTime(LocalDateTime.now().plusDays(coupon.getValidDays()));
                } else if (coupon.getEndTime() != null) {
                    userCoupon.setExpireTime(coupon.getEndTime());
                }
                userCouponRepository.save(userCoupon);
            }
        }
    }
}