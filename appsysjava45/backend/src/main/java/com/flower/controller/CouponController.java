package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.Coupon;
import com.flower.entity.UserCoupon;
import com.flower.service.CouponService;
import com.flower.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 优惠券控制器
 */
@RestController
@RequestMapping("/api/coupon")
@Tag(name = "优惠券管理", description = "优惠券相关接口")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @Operation(summary = "获取优惠券列表")
    public Result<Page<Coupon>> getCouponList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Coupon> coupons = couponService.getCouponList(pageable);
            return Result.success(coupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的优惠券")
    public Result<List<UserCoupon>> getUserCoupons(
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<UserCoupon> userCoupons = couponService.getUserCoupons(userId, status);
            return Result.success(userCoupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/receive/{id}")
    @Operation(summary = "领取优惠券")
    public Result<UserCoupon> receiveCoupon(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            UserCoupon userCoupon = couponService.receiveCoupon(userId, id);
            return Result.success(userCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}