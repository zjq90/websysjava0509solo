package com.bike.controller;

import com.bike.common.Result;
import com.bike.entity.Battery;
import com.bike.entity.BatteryLog;
import com.bike.entity.SwapStation;
import com.bike.service.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 电池管理控制器
 * 
 * @author bike-sharing
 */
@Tag(name = "电池管理", description = "电池监控、换电站、电池生命周期等接口")
@RestController
@RequestMapping("/api/battery")
@CrossOrigin
public class BatteryController {

    @Autowired
    private BatteryService batteryService;

    @Operation(summary = "获取所有电池")
    @GetMapping("/batteries")
    public Result<List<Battery>> getAllBatteries() {
        return Result.success(batteryService.getAllBatteries());
    }

    @Operation(summary = "按状态获取电池")
    @GetMapping("/batteries/status/{status}")
    public Result<List<Battery>> getBatteriesByStatus(
            @Parameter(description = "状态") @PathVariable String status) {
        return Result.success(batteryService.getBatteriesByStatus(status));
    }

    @Operation(summary = "获取低电量电池")
    @GetMapping("/batteries/low")
    public Result<List<Battery>> getLowBatteries(
            @Parameter(description = "电量阈值") @RequestParam(defaultValue = "20") Integer level) {
        return Result.success(batteryService.getLowBatteries(level));
    }

    @Operation(summary = "获取车辆的电池")
    @GetMapping("/batteries/bike/{bikeId}")
    public Result<List<Battery>> getBatteriesByBikeId(
            @Parameter(description = "车辆ID") @PathVariable Long bikeId) {
        return Result.success(batteryService.getBatteriesByBikeId(bikeId));
    }

    @Operation(summary = "获取电池详情")
    @GetMapping("/batteries/{id}")
    public Result<Battery> getBatteryById(@Parameter(description = "电池ID") @PathVariable Long id) {
        return Result.success(batteryService.getBatteryById(id));
    }

    @Operation(summary = "创建电池")
    @PostMapping("/batteries")
    public Result<Battery> createBattery(@RequestBody Battery battery) {
        return Result.success(batteryService.createBattery(battery));
    }

    @Operation(summary = "更新电池")
    @PutMapping("/batteries/{id}")
    public Result<Battery> updateBattery(
            @Parameter(description = "电池ID") @PathVariable Long id,
            @RequestBody Battery battery) {
        return Result.success(batteryService.updateBattery(id, battery));
    }

    @Operation(summary = "更新电池电量")
    @PostMapping("/batteries/{id}/level")
    public Result<Battery> updateBatteryLevel(
            @Parameter(description = "电池ID") @PathVariable Long id,
            @Parameter(description = "新电量") @RequestParam Integer newLevel,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName) {
        return Result.success(batteryService.updateBatteryLevel(id, newLevel, operatorId, operatorName));
    }

    @Operation(summary = "换电")
    @PostMapping("/swap")
    public Result<Map<String, Object>> swapBattery(
            @Parameter(description = "车辆ID") @RequestParam Long bikeId,
            @Parameter(description = "换电站ID") @RequestParam Long stationId,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName) {
        return Result.success(batteryService.swapBattery(bikeId, stationId, operatorId, operatorName));
    }

    @Operation(summary = "获取所有换电站")
    @GetMapping("/stations")
    public Result<List<SwapStation>> getAllSwapStations() {
        return Result.success(batteryService.getAllSwapStations());
    }

    @Operation(summary = "获取有可用电池的换电站")
    @GetMapping("/stations/available")
    public Result<List<SwapStation>> getSwapStationsWithAvailableBatteries() {
        return Result.success(batteryService.getSwapStationsWithAvailableBatteries());
    }

    @Operation(summary = "获取换电站详情")
    @GetMapping("/stations/{id}")
    public Result<SwapStation> getSwapStationById(@Parameter(description = "换电站ID") @PathVariable Long id) {
        return Result.success(batteryService.getSwapStationById(id));
    }

    @Operation(summary = "创建换电站")
    @PostMapping("/stations")
    public Result<SwapStation> createSwapStation(@RequestBody SwapStation station) {
        return Result.success(batteryService.createSwapStation(station));
    }

    @Operation(summary = "更新换电站")
    @PutMapping("/stations/{id}")
    public Result<SwapStation> updateSwapStation(
            @Parameter(description = "换电站ID") @PathVariable Long id,
            @RequestBody SwapStation station) {
        return Result.success(batteryService.updateSwapStation(id, station));
    }

    @Operation(summary = "获取电池操作日志")
    @GetMapping("/logs")
    public Result<List<BatteryLog>> getBatteryLogs(
            @Parameter(description = "电池ID") @RequestParam(required = false) Long batteryId) {
        return Result.success(batteryService.getBatteryLogs(batteryId));
    }

    @Operation(summary = "获取低电量预警")
    @GetMapping("/warnings")
    public Result<List<Battery>> getLowBatteryWarning() {
        return Result.success(batteryService.getLowBatteryWarning());
    }

    @Operation(summary = "获取电池生命周期统计")
    @GetMapping("/lifecycle-stats")
    public Result<List<Map<String, Object>>> getBatteryLifeCycleStats() {
        return Result.success(batteryService.getBatteryLifeCycleStats());
    }
}
