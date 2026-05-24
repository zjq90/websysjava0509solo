package com.bikesystem.controller;

import com.bikesystem.common.Result;
import com.bikesystem.entity.Coupon;
import com.bikesystem.entity.CreditRecord;
import com.bikesystem.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 优惠券控制器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/coupon")
@Tag(name = "优惠券模块", description = "优惠券和信用体系相关接口")
public class CouponController {

    @Resource
    private CouponService couponService;

    @GetMapping("/my-coupons")
    @Operation(summary = "我的优惠券", description = "获取用户优惠券列表")
    public Result<List<Coupon>> getMyCoupons(
            @Parameter(description = "状态: UNUSED/USED/EXPIRED") @RequestParam(required = false) String status) {
        return Result.success(couponService.getMyCoupons(status));
    }

    @GetMapping("/available-count")
    @Operation(summary = "可用优惠券数量", description = "获取用户可用优惠券数量")
    public Result<Long> getAvailableCouponCount() {
        return Result.success(couponService.getAvailableCouponCount());
    }

    @GetMapping("/credit-records")
    @Operation(summary = "信用分记录", description = "获取用户信用分变动记录")
    public Result<List<CreditRecord>> getCreditRecords(
            @Parameter(description = "页码") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer pageSize) {
        return Result.success(couponService.getCreditRecords(pageNum, pageSize));
    }
}
