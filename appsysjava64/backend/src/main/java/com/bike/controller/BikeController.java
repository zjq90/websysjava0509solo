package com.bike.controller;

import com.bike.common.Result;
import com.bike.entity.Bike;
import com.bike.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 车辆管理控制器
 * 
 * @author bike-sharing
 */
@Tag(name = "车辆管理", description = "车辆信息、详情、统计等接口")
@RestController
@RequestMapping("/api/bike")
@CrossOrigin
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @Operation(summary = "获取所有车辆")
    @GetMapping("/list")
    public Result<List<Bike>> getAllBikes() {
        return Result.success(bikeService.getAllBikes());
    }

    @Operation(summary = "按状态获取车辆")
    @GetMapping("/list/status/{status}")
    public Result<List<Bike>> getBikesByStatus(
            @Parameter(description = "状态") @PathVariable String status) {
        return Result.success(bikeService.getBikesByStatus(status));
    }

    @Operation(summary = "获取低电量车辆")
    @GetMapping("/list/low-battery")
    public Result<List<Bike>> getLowBatteryBikes() {
        return Result.success(bikeService.getLowBatteryBikes());
    }

    @Operation(summary = "获取车辆详情")
    @GetMapping("/{id}")
    public Result<Bike> getBikeById(@Parameter(description = "车辆ID") @PathVariable Long id) {
        return Result.success(bikeService.getBikeById(id));
    }

    @Operation(summary = "通过二维码获取车辆")
    @GetMapping("/qrcode/{qrCode}")
    public Result<Bike> getBikeByQrCode(
            @Parameter(description = "二维码") @PathVariable String qrCode) {
        return Result.success(bikeService.getBikeByQrCode(qrCode));
    }

    @Operation(summary = "获取车辆完整详情（含维修记录）")
    @GetMapping("/{id}/detail")
    public Result<Map<String, Object>> getBikeDetail(
            @Parameter(description = "车辆ID") @PathVariable Long id) {
        return Result.success(bikeService.getBikeDetail(id));
    }

    @Operation(summary = "创建车辆")
    @PostMapping("/create")
    public Result<Bike> createBike(@RequestBody Bike bike) {
        return Result.success(bikeService.createBike(bike));
    }

    @Operation(summary = "更新车辆")
    @PutMapping("/{id}")
    public Result<Bike> updateBike(
            @Parameter(description = "车辆ID") @PathVariable Long id,
            @RequestBody Bike bike) {
        return Result.success(bikeService.updateBike(id, bike));
    }

    @Operation(summary = "删除车辆")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBike(@Parameter(description = "车辆ID") @PathVariable Long id) {
        bikeService.deleteBike(id);
        return Result.success();
    }

    @Operation(summary = "获取车辆统计")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getBikeStats() {
        return Result.success(bikeService.getBikeStats());
    }
}
