package com.bikesystem.controller;

import com.bikesystem.common.Result;
import com.bikesystem.entity.Bike;
import com.bikesystem.entity.Reservation;
import com.bikesystem.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 车辆控制器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/bike")
@Tag(name = "车辆模块", description = "车辆查找、预约等接口")
public class BikeController {

    @Resource
    private BikeService bikeService;

    @GetMapping("/nearby")
    @Operation(summary = "获取附近车辆", description = "根据位置获取附近可用车辆")
    public Result<List<Bike>> getNearbyBikes(
            @Parameter(description = "纬度", required = true) @RequestParam BigDecimal lat,
            @Parameter(description = "经度", required = true) @RequestParam BigDecimal lng,
            @Parameter(description = "车型: STANDARD普通单车/ELECTRIC电动车/ASSIST助力车") @RequestParam(required = false) String bikeType,
            @Parameter(description = "最低电量") @RequestParam(required = false) Integer minBattery,
            @Parameter(description = "返回数量限制") @RequestParam(required = false, defaultValue = "50") Integer limit) {
        return Result.success(bikeService.getNearbyBikes(lat, lng, bikeType, minBattery, limit));
    }

    @GetMapping("/{bikeId}")
    @Operation(summary = "获取车辆详情", description = "根据车辆ID获取车辆详细信息")
    public Result<Bike> getBikeDetail(
            @Parameter(description = "车辆ID", required = true) @PathVariable Long bikeId) {
        return Result.success(bikeService.getBikeDetail(bikeId));
    }

    @GetMapping("/qrcode")
    @Operation(summary = "根据二维码获取车辆", description = "扫描二维码后获取车辆信息")
    public Result<Bike> getBikeByQrCode(
            @Parameter(description = "二维码内容", required = true) @RequestParam String qrCode) {
        return Result.success(bikeService.getBikeByQrCode(qrCode));
    }

    @PostMapping("/reserve")
    @Operation(summary = "预约车辆", description = "预约指定车辆，保留15分钟")
    public Result<Map<String, Object>> reserveBike(
            @Parameter(description = "车辆ID", required = true) @RequestParam Long bikeId) {
        return Result.success(bikeService.reserveBike(bikeId));
    }

    @PostMapping("/reserve/cancel")
    @Operation(summary = "取消预约", description = "取消当前车辆预约")
    public Result<Void> cancelReservation(
            @Parameter(description = "预约ID", required = true) @RequestParam Long reservationId) {
        bikeService.cancelReservation(reservationId);
        return Result.success();
    }

    @GetMapping("/reserve/current")
    @Operation(summary = "获取当前预约", description = "获取用户当前有效的预约")
    public Result<Reservation> getCurrentReservation() {
        return Result.success(bikeService.getCurrentReservation());
    }

    @GetMapping("/{bikeId}/dynamic-qrcode")
    @Operation(summary = "生成动态二维码", description = "生成每分钟刷新的动态二维码")
    public Result<Map<String, Object>> generateDynamicQrCode(
            @Parameter(description = "车辆ID", required = true) @PathVariable Long bikeId) {
        return Result.success(bikeService.generateDynamicQrCode(bikeId));
    }
}
