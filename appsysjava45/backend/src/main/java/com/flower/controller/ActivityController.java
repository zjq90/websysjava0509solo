package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.*;
import com.flower.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@Tag(name = "活动管理", description = "优惠券、秒杀、拼团、自提点等接口")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/coupons")
    @Operation(summary = "获取所有优惠券", description = "获取所有可用的优惠券列表")
    public Result<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = activityService.getAllCoupons();
        return Result.success(coupons);
    }

    @GetMapping("/coupons/new-user")
    @Operation(summary = "获取新人专享优惠券", description = "获取新用户专享的优惠券")
    public Result<List<Coupon>> getNewUserCoupons() {
        List<Coupon> coupons = activityService.getNewUserCoupons();
        return Result.success(coupons);
    }

    @PostMapping("/coupon/receive")
    @Operation(summary = "领取优惠券", description = "用户领取优惠券")
    public Result<UserCoupon> receiveCoupon(@RequestParam Long userId, @RequestParam Long couponId) {
        try {
            UserCoupon userCoupon = activityService.receiveCoupon(userId, couponId);
            return Result.success("领取成功", userCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/coupon/user/{userId}")
    @Operation(summary = "获取用户优惠券", description = "获取用户的所有优惠券")
    public Result<List<UserCoupon>> getUserCoupons(@PathVariable Long userId) {
        List<UserCoupon> coupons = activityService.getUserCoupons(userId);
        return Result.success(coupons);
    }

    @GetMapping("/coupon/user/{userId}/{status}")
    @Operation(summary = "按状态获取用户优惠券", description = "获取用户指定状态的优惠券")
    public Result<List<UserCoupon>> getUserCouponsByStatus(@PathVariable Long userId, @PathVariable String status) {
        List<UserCoupon> coupons = activityService.getUserCouponsByStatus(userId, status);
        return Result.success(coupons);
    }

    @GetMapping("/flashsale")
    @Operation(summary = "获取秒杀活动", description = "获取当前正在进行的秒杀活动")
    public Result<List<FlashSale>> getActiveFlashSales() {
        List<FlashSale> flashSales = activityService.getActiveFlashSales();
        return Result.success(flashSales);
    }

    @GetMapping("/groupbuy")
    @Operation(summary = "获取拼团活动", description = "获取当前正在进行的拼团活动")
    public Result<List<GroupBuy>> getActiveGroupBuys() {
        List<GroupBuy> groupBuys = activityService.getActiveGroupBuys();
        return Result.success(groupBuys);
    }

    @GetMapping("/pickup-points")
    @Operation(summary = "获取自提点列表", description = "获取所有可用的自提点")
    public Result<List<PickupPoint>> getAllPickupPoints() {
        List<PickupPoint> points = activityService.getAllPickupPoints();
        return Result.success(points);
    }
}