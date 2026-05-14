package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.ChildGuard;
import com.appsys.entity.NetworkDevice;
import com.appsys.entity.WiFiSetting;
import com.appsys.service.NetworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/network")
@Tag(name = "网络管理", description = "网络管理相关接口")
public class NetworkController {

    @Autowired
    private NetworkService networkService;

    @GetMapping("/devices/{userId}")
    @Operation(summary = "获取用户设备列表")
    public Result<List<NetworkDevice>> listDevices(@PathVariable Long userId) {
        return Result.success(networkService.findDevicesByUserId(userId));
    }

    @GetMapping("/device/{id}")
    @Operation(summary = "获取设备详情")
    public Result<NetworkDevice> getDevice(@PathVariable Long id) {
        Optional<NetworkDevice> device = networkService.findDeviceById(id);
        return device.map(Result::success).orElseGet(() -> Result.error("设备不存在"));
    }

    @PostMapping("/device")
    @Operation(summary = "创建设备")
    public Result<NetworkDevice> createDevice(@RequestBody NetworkDevice device) {
        return Result.success(networkService.saveDevice(device));
    }

    @PutMapping("/device/{id}/block")
    @Operation(summary = "拉黑/取消拉黑设备")
    public Result<NetworkDevice> toggleDeviceBlock(@PathVariable Long id) {
        NetworkDevice device = networkService.toggleDeviceBlock(id);
        if (device != null) {
            return Result.success(device);
        }
        return Result.error("设备不存在");
    }

    @PutMapping("/device/{id}/speed-limit")
    @Operation(summary = "设置设备限速")
    public Result<NetworkDevice> setDeviceSpeedLimit(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Integer limit = Integer.valueOf(request.get("limit").toString());
        NetworkDevice device = networkService.setDeviceSpeedLimit(id, limit);
        if (device != null) {
            return Result.success(device);
        }
        return Result.error("设备不存在");
    }

    @DeleteMapping("/device/{id}")
    @Operation(summary = "删除设备")
    public Result<Void> deleteDevice(@PathVariable Long id) {
        networkService.deleteDevice(id);
        return Result.success();
    }

    @GetMapping("/wifi/{userId}")
    @Operation(summary = "获取WiFi设置")
    public Result<WiFiSetting> getWiFiSetting(@PathVariable Long userId) {
        Optional<WiFiSetting> setting = networkService.findWiFiSettingByUserId(userId);
        return setting.map(Result::success).orElseGet(() -> Result.error("WiFi设置不存在"));
    }

    @PostMapping("/wifi")
    @Operation(summary = "保存WiFi设置")
    public Result<WiFiSetting> saveWiFiSetting(@RequestBody WiFiSetting setting) {
        return Result.success(networkService.saveWiFiSetting(setting));
    }

    @PutMapping("/wifi/{userId}/password")
    @Operation(summary = "修改WiFi密码")
    public Result<WiFiSetting> updateWiFiPassword(@PathVariable Long userId, @RequestBody Map<String, Object> request) {
        String password = (String) request.get("password");
        WiFiSetting setting = networkService.updateWiFiPassword(userId, password);
        if (setting != null) {
            return Result.success(setting);
        }
        return Result.error("WiFi设置不存在");
    }

    @PutMapping("/wifi/{userId}/visibility")
    @Operation(summary = "切换WiFi可见性")
    public Result<WiFiSetting> toggleWiFiVisibility(@PathVariable Long userId) {
        WiFiSetting setting = networkService.toggleWiFiVisibility(userId);
        if (setting != null) {
            return Result.success(setting);
        }
        return Result.error("WiFi设置不存在");
    }

    @GetMapping("/child-guard/{userId}")
    @Operation(summary = "获取儿童守护列表")
    public Result<List<ChildGuard>> listChildGuards(@PathVariable Long userId) {
        return Result.success(networkService.findChildGuardsByUserId(userId));
    }

    @PostMapping("/child-guard")
    @Operation(summary = "创建儿童守护")
    public Result<ChildGuard> createChildGuard(@RequestBody ChildGuard childGuard) {
        return Result.success(networkService.saveChildGuard(childGuard));
    }

    @PutMapping("/child-guard/{id}/toggle")
    @Operation(summary = "切换儿童守护开关")
    public Result<ChildGuard> toggleChildGuard(@PathVariable Long id) {
        ChildGuard guard = networkService.toggleChildGuard(id);
        if (guard != null) {
            return Result.success(guard);
        }
        return Result.error("儿童守护不存在");
    }

    @DeleteMapping("/child-guard/{id}")
    @Operation(summary = "删除儿童守护")
    public Result<Void> deleteChildGuard(@PathVariable Long id) {
        networkService.deleteChildGuard(id);
        return Result.success();
    }

    @PostMapping("/router/{userId}/restart")
    @Operation(summary = "重启路由器")
    public Result<Void> restartRouter(@PathVariable Long userId) {
        networkService.restartRouter(userId);
        return Result.success();
    }

    @GetMapping("/topology/{userId}")
    @Operation(summary = "获取网络拓扑")
    public Result<List<NetworkDevice>> getNetworkTopology(@PathVariable Long userId) {
        return Result.success(networkService.getNetworkTopology(userId));
    }
}
