package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.CheckInRecord;
import com.appsys.entity.PointGoods;
import com.appsys.entity.PointRecord;
import com.appsys.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@Tag(name = "积分管理", description = "积分商品、签到、积分记录相关接口")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/goods")
    @Operation(summary = "获取所有积分商品")
    public Result<List<PointGoods>> getAllPointGoods() {
        return Result.success(pointService.getAllPointGoods());
    }

    @GetMapping("/goods/type/{type}")
    @Operation(summary = "根据类型获取积分商品")
    public Result<List<PointGoods>> getPointGoodsByType(@PathVariable Integer type) {
        return Result.success(pointService.getPointGoodsByType(type));
    }

    @GetMapping("/records/{userId}")
    @Operation(summary = "获取用户积分记录")
    public Result<List<PointRecord>> getUserPointRecords(@PathVariable Long userId) {
        return Result.success(pointService.getUserPointRecords(userId));
    }

    @PostMapping("/checkin")
    @Operation(summary = "签到")
    public Result<CheckInRecord> checkIn(@RequestParam Long userId) {
        CheckInRecord record = pointService.checkIn(userId);
        if (record != null) {
            return Result.success("签到成功", record);
        }
        return Result.error("今日已签到");
    }

    @GetMapping("/checkin/status")
    @Operation(summary = "检查今日签到状态")
    public Result<Boolean> hasCheckedInToday(@RequestParam Long userId) {
        return Result.success(pointService.hasCheckedInToday(userId));
    }

    @PostMapping("/exchange")
    @Operation(summary = "积分兑换商品")
    public Result<Boolean> exchangeGoods(@RequestParam Long userId, @RequestParam Long goodsId) {
        boolean success = pointService.exchangeGoods(userId, goodsId);
        if (success) {
            return Result.success("兑换成功", true);
        }
        return Result.error("兑换失败，积分不足或库存不足");
    }

    @PostMapping("/business")
    @Operation(summary = "办理业务获得积分")
    public Result<Void> addPointsForBusiness(@RequestParam Long userId,
                                              @RequestParam Integer basePoints,
                                              @RequestParam String businessName,
                                              @RequestParam String bizId) {
        pointService.addPointsForBusiness(userId, basePoints, businessName, bizId);
        return Result.success("积分已添加");
    }

    @PostMapping("/review")
    @Operation(summary = "评价服务获得积分")
    public Result<Void> addPointsForReview(@RequestParam Long userId, @RequestParam String bizId) {
        pointService.addPointsForReview(userId, bizId);
        return Result.success("评价积分已添加");
    }
}
