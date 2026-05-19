package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.entity.PetDailyData;
import com.petclinic.entity.SmartDevice;
import com.petclinic.service.SmartDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 智能设备Controller
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/smart-device")
@Tag(name = "智能设备管理", description = "智能喂食器、饮水机及宠物日常数据监控")
public class SmartDeviceController {

    @Autowired
    private SmartDeviceService smartDeviceService;

    // ==================== 智能设备管理 ====================

    @GetMapping("/devices")
    @Operation(summary = "查询所有智能设备")
    public Result<List<SmartDevice>> findAllDevices() {
        return Result.success(smartDeviceService.findAllDevices());
    }

    @GetMapping("/devices/{id}")
    @Operation(summary = "根据ID查询设备")
    public Result<SmartDevice> findDeviceById(@PathVariable Long id) {
        Optional<SmartDevice> device = smartDeviceService.findDeviceById(id);
        return device.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping("/devices")
    @Operation(summary = "新增智能设备")
    public Result<SmartDevice> saveDevice(@RequestBody SmartDevice device) {
        return Result.success(smartDeviceService.saveDevice(device));
    }

    @PutMapping("/devices/{id}")
    @Operation(summary = "更新智能设备")
    public Result<SmartDevice> updateDevice(@PathVariable Long id, @RequestBody SmartDevice device) {
        SmartDevice updated = smartDeviceService.updateDevice(id, device);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @DeleteMapping("/devices/{id}")
    @Operation(summary = "删除智能设备")
    public Result<Void> deleteDeviceById(@PathVariable Long id) {
        smartDeviceService.deleteDeviceById(id);
        return Result.success();
    }

    @GetMapping("/devices/status/{status}")
    @Operation(summary = "根据状态查询设备")
    public Result<List<SmartDevice>> findDevicesByStatus(@PathVariable String status) {
        return Result.success(smartDeviceService.findDevicesByStatus(status));
    }

    @GetMapping("/devices/type/{deviceType}")
    @Operation(summary = "根据设备类型查询")
    public Result<List<SmartDevice>> findDevicesByType(@PathVariable String deviceType) {
        return Result.success(smartDeviceService.findDevicesByType(deviceType));
    }

    @PutMapping("/devices/{id}/status")
    @Operation(summary = "更新设备状态")
    public Result<SmartDevice> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) {
        SmartDevice updated = smartDeviceService.updateDeviceStatus(id, status);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    // ==================== 宠物日常数据管理 ====================

    @GetMapping("/data")
    @Operation(summary = "查询所有日常数据")
    public Result<List<PetDailyData>> findAllData() {
        return Result.success(smartDeviceService.findAllData());
    }

    @GetMapping("/data/{id}")
    @Operation(summary = "根据ID查询数据")
    public Result<PetDailyData> findDataById(@PathVariable Long id) {
        Optional<PetDailyData> data = smartDeviceService.findDataById(id);
        return data.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping("/data")
    @Operation(summary = "新增日常数据")
    public Result<PetDailyData> saveData(@RequestBody PetDailyData data) {
        return Result.success(smartDeviceService.saveData(data));
    }

    @DeleteMapping("/data/{id}")
    @Operation(summary = "删除日常数据")
    public Result<Void> deleteDataById(@PathVariable Long id) {
        smartDeviceService.deleteDataById(id);
        return Result.success();
    }

    @GetMapping("/data/device/{deviceId}")
    @Operation(summary = "根据设备ID查询数据")
    public Result<List<PetDailyData>> findDataByDeviceId(@PathVariable Long deviceId) {
        return Result.success(smartDeviceService.findDataByDeviceId(deviceId));
    }

    @GetMapping("/data/time-range")
    @Operation(summary = "根据时间范围查询数据")
    public Result<List<PetDailyData>> findDataByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(smartDeviceService.findDataByTimeRange(startTime, endTime));
    }

    @GetMapping("/data/recent")
    @Operation(summary = "查询最近N条数据")
    public Result<List<PetDailyData>> findRecentData(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(smartDeviceService.findRecentData(limit));
    }

    @PostMapping("/data/mock/{deviceId}")
    @Operation(summary = "生成模拟数据")
    public Result<PetDailyData> generateMockData(@PathVariable Long deviceId) {
        PetDailyData data = smartDeviceService.generateMockData(deviceId);
        return data != null ? Result.success(data) : Result.error("生成失败");
    }
}
