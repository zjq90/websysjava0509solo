package com.ops.controller;

import com.ops.common.Result;
import com.ops.entity.Coupon;
import com.ops.entity.Promotion;
import com.ops.service.MarketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 营销管理控制器
 * 提供优惠券和营销活动的API接口
 * 
 * @author ops-admin
 */
@RestController
@RequestMapping("/api/marketing")
@Tag(name = "营销管理", description = "优惠券管理、营销活动管理")
@CrossOrigin(origins = "*")
public class MarketingController {

    @Autowired
    private MarketingService marketingService;

    @GetMapping("/coupons")
    @Operation(summary = "获取所有优惠券")
    public Result<List<Coupon>> getAllCoupons() {
        return Result.success(marketingService.getAllCoupons());
    }

    @PostMapping("/coupons")
    @Operation(summary = "创建优惠券")
    public Result<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return Result.success(marketingService.createCoupon(coupon));
    }

    @PutMapping("/coupons/{id}")
    @Operation(summary = "更新优惠券")
    public Result<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        coupon.setId(id);
        return Result.success(marketingService.updateCoupon(coupon));
    }

    @DeleteMapping("/coupons/{id}")
    @Operation(summary = "删除优惠券")
    public Result<Void> deleteCoupon(@PathVariable Long id) {
        marketingService.deleteCoupon(id);
        return Result.success();
    }

    @PostMapping("/coupons/{id}/distribute")
    @Operation(summary = "发放优惠券")
    public Result<String> distributeCoupons(@PathVariable Long id, @RequestBody Map<String, String> params) {
        try {
            String targetType = params.get("targetType");
            return Result.success(marketingService.distributeCoupons(id, targetType));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/promotions")
    @Operation(summary = "获取所有营销活动")
    public Result<List<Promotion>> getAllPromotions() {
        return Result.success(marketingService.getAllPromotions());
    }

    @PostMapping("/promotions")
    @Operation(summary = "创建营销活动")
    public Result<Promotion> createPromotion(@RequestBody Promotion promotion) {
        return Result.success(marketingService.createPromotion(promotion));
    }

    @PutMapping("/promotions/{id}")
    @Operation(summary = "更新营销活动")
    public Result<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        promotion.setId(id);
        return Result.success(marketingService.updatePromotion(promotion));
    }

    @DeleteMapping("/promotions/{id}")
    @Operation(summary = "删除营销活动")
    public Result<Void> deletePromotion(@PathVariable Long id) {
        marketingService.deletePromotion(id);
        return Result.success();
    }

    @PostMapping("/promotions/{id}/start")
    @Operation(summary = "启动营销活动")
    public Result<Promotion> startPromotion(@PathVariable Long id) {
        try {
            return Result.success(marketingService.startPromotion(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/promotions/{id}/end")
    @Operation(summary = "结束营销活动")
    public Result<Promotion> endPromotion(@PathVariable Long id) {
        try {
            return Result.success(marketingService.endPromotion(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
