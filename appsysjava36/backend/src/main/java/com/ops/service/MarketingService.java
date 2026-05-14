package com.ops.service;

import com.ops.entity.Coupon;
import com.ops.entity.Promotion;
import com.ops.entity.User;
import com.ops.repository.CouponRepository;
import com.ops.repository.PromotionRepository;
import com.ops.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 营销服务类
 * 提供优惠券发放、营销活动管理功能
 * 
 * @author ops-admin
 */
@Service
public class MarketingService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建优惠券
     */
    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        String couponCode = "CP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) 
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        coupon.setCouponCode(couponCode);
        return couponRepository.save(coupon);
    }

    /**
     * 定向发放优惠券给特定用户群体
     */
    @Transactional
    public String distributeCoupons(Long couponId, String targetUserType) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("优惠券不存在"));
        
        List<User> targetUsers;
        
        switch (targetUserType) {
            case "EXPIRING":
                LocalDateTime sevenDaysLater = LocalDateTime.now().plusDays(7);
                targetUsers = userRepository.findByExpireDateBeforeAndIsActive(sevenDaysLater, true);
                break;
            case "INACTIVE":
                LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
                targetUsers = userRepository.findByLastLoginTimeBeforeAndIsActive(thirtyDaysAgo, true);
                break;
            default:
                throw new RuntimeException("不支持的用户类型");
        }
        
        return "已向 " + targetUsers.size() + " 名用户发放了优惠券";
    }

    /**
     * 创建营销活动
     */
    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    /**
     * 启动营销活动
     */
    @Transactional
    public Promotion startPromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        promotion.setStatus("ACTIVE");
        return promotionRepository.save(promotion);
    }

    /**
     * 结束营销活动
     */
    @Transactional
    public Promotion endPromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        promotion.setStatus("ENDED");
        return promotionRepository.save(promotion);
    }

    /**
     * 获取所有优惠券
     */
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    /**
     * 获取所有营销活动
     */
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    /**
     * 更新优惠券
     */
    @Transactional
    public Coupon updateCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    /**
     * 更新营销活动
     */
    @Transactional
    public Promotion updatePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    /**
     * 删除优惠券
     */
    @Transactional
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    /**
     * 删除营销活动
     */
    @Transactional
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }
}
