package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.DeviceStatus;
import com.websys.service.DeviceStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 设备状态与远程控制控制器
 * 提供设备状态查询和远程控制相关的REST API接口
 */
@RestController
@RequestMapping("/api/device-status")
@Tag(name = "设备状态与远程控制", description = "设备状态查询和远程控制相关接口")
public class DeviceStatusController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @GetMapping("/latest/device/{deviceId}")
    @Operation(summary = "查询设备最新状态", description = "根据设备ID查询设备最新状态")
    public Result<DeviceStatus> getLatestByDeviceId(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        Optional<DeviceStatus> status = deviceStatusService.findLatestByDeviceId(deviceId);
        return status.map(Result::success).orElseGet(() -> Result.error("设备状态不存在"));
    }

    @GetMapping("/latest/code/{deviceCode}")
    @Operation(summary = "根据设备编号查询最新状态", description = "根据设备编号查询设备最新状态")
    public Result<DeviceStatus> getLatestByDeviceCode(@Parameter(description = "设备编号") @PathVariable String deviceCode) {
        Optional<DeviceStatus> status = deviceStatusService.findLatestByDeviceCode(deviceCode);
        return status.map(Result::success).orElseGet(() -> Result.error("设备状态不存在"));
    }

    @GetMapping("/history/device/{deviceId}")
    @Operation(summary = "查询设备历史状态", description = "根据设备ID查询设备历史状态记录")
    public Result<List<DeviceStatus>> getHistoryByDeviceId(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        List<DeviceStatus> statuses = deviceStatusService.findHistoryByDeviceId(deviceId);
        return Result.success(statuses);
    }

    @PostMapping
    @Operation(summary = "保存设备状态", description = "保存设备上报的状态信息")
    public Result<DeviceStatus> save(@RequestBody DeviceStatus deviceStatus) {
        DeviceStatus saved = deviceStatusService.save(deviceStatus);
        return Result.success("保存成功", saved);
    }

    @PostMapping("/{deviceId}/restart")
    @Operation(summary = "远程重启设备", description = "发送远程重启指令到设备")
    public Result<String> restartDevice(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        String result = deviceStatusService.restartDevice(deviceId);
        return Result.success(result);
    }

    @PostMapping("/{deviceId}/unlock")
    @Operation(summary = "远程开锁", description = "发送远程开锁指令到设备")
    public Result<String> unlockDevice(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        String result = deviceStatusService.unlockDevice(deviceId);
        return Result.success(result);
    }

    @PostMapping("/{deviceId}/temperature")
    @Operation(summary = "调整温度", description = "发送温度调整指令到设备")
    public Result<String> adjustTemperature(
            @Parameter(description = "设备ID") @PathVariable Long deviceId,
            @RequestBody Map<String, BigDecimal> params) {
        BigDecimal targetTemperature = params.get("targetTemperature");
        String result = deviceStatusService.adjustTemperature(deviceId, targetTemperature);
        return Result.success(result);
    }

    @PostMapping("/{deviceId}/advert")
    @Operation(summary = "设置屏幕广告", description = "设置设备屏幕广告内容")
    public Result<String> setAdvertContent(
            @Parameter(description = "设备ID") @PathVariable Long deviceId,
            @RequestBody Map<String, String> params) {
        String advertContent = params.get("advertContent");
        String result = deviceStatusService.setAdvertContent(deviceId, advertContent);
        return Result.success(result);
    }

    @PostMapping("/firmware-upgrade")
    @Operation(summary = "固件升级", description = "批量或单个设备的固件远程升级")
    public Result<String> firmwareUpgrade(@RequestBody Map<String, List<Long>> params) {
        List<Long> deviceIds = params.get("deviceIds");
        String result = deviceStatusService.firmwareUpgrade(deviceIds);
        return Result.success(result);
    }
}
