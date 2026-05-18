package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.Coupon;
import com.flowerstore.backend.entity.UserCoupon;
import com.flowerstore.backend.service.CouponService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 优惠券控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/coupon")
@Tag(name = "优惠券接口", description = "优惠券领取、查询等")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 获取可用优惠券列表
     */
    @GetMapping("/available")
    @Operation(summary = "获取可用优惠券", description = "获取所有可领取的优惠券列表")
    public Result<List<Coupon>> getAvailableCoupons() {
        return couponService.getAvailableCoupons();
    }

    /**
     * 获取用户优惠券列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的优惠券", description = "获取当前用户的优惠券列表")
    public Result<List<UserCoupon>> getUserCoupons(
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return couponService.getUserCoupons(userId, status);
    }

    /**
     * 领取优惠券
     */
    @PostMapping("/receive/{couponId}")
    @Operation(summary = "领取优惠券", description = "领取指定优惠券")
    public Result<UserCoupon> receiveCoupon(@PathVariable Long couponId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return couponService.receiveCoupon(userId, couponId);
    }

    /**
     * 积分兑换优惠券
     */
    @PostMapping("/exchange/{couponId}")
    @Operation(summary = "积分兑换优惠券", description = "使用积分兑换优惠券")
    public Result<UserCoupon> exchangeCoupon(@PathVariable Long couponId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return couponService.exchangeCoupon(userId, couponId);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
