package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Device;
import com.websys.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 设备管理控制器
 * 提供设备管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/device")
@Tag(name = "设备管理", description = "设备管理相关接口")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/list")
    @Operation(summary = "查询所有设备", description = "查询所有设备列表")
    public Result<List<Device>> getAll() {
        List<Device> devices = deviceService.findAll();
        return Result.success(devices);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询设备列表", description = "根据条件分页查询设备列表")
    public Result<Page<Device>> getPage(
            @Parameter(description = "设备编号") @RequestParam(required = false) String deviceCode,
            @Parameter(description = "设备名称") @RequestParam(required = false) String deviceName,
            @Parameter(description = "投放位置") @RequestParam(required = false) String location,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Device> devicePage = deviceService.findPage(deviceCode, deviceName, location, status, pageRequest);
        return Result.success(devicePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询设备", description = "根据设备ID查询设备详情")
    public Result<Device> getById(@Parameter(description = "设备ID") @PathVariable Long id) {
        Optional<Device> device = deviceService.findById(id);
        return device.map(Result::success).orElseGet(() -> Result.error("设备不存在"));
    }

    @GetMapping("/code/{deviceCode}")
    @Operation(summary = "根据设备编号查询设备", description = "根据设备编号查询设备详情")
    public Result<Device> getByDeviceCode(@Parameter(description = "设备编号") @PathVariable String deviceCode) {
        Optional<Device> device = deviceService.findByDeviceCode(deviceCode);
        return device.map(Result::success).orElseGet(() -> Result.error("设备不存在"));
    }

    @PostMapping
    @Operation(summary = "新增设备", description = "新增设备信息")
    public Result<Device> save(@RequestBody Device device) {
        if (deviceService.existsByDeviceCode(device.getDeviceCode())) {
            return Result.error("设备编号已存在");
        }
        Device saved = deviceService.save(device);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新设备", description = "更新设备信息")
    public Result<Device> update(@RequestBody Device device) {
        if (device.getId() == null) {
            return Result.error("设备ID不能为空");
        }
        Device updated = deviceService.save(device);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备", description = "根据ID删除设备")
    public Result<Void> delete(@Parameter(description = "设备ID") @PathVariable Long id) {
        deviceService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "更新设备状态", description = "更新设备在线/离线/故障状态")
    public Result<Device> updateStatus(
            @Parameter(description = "设备ID") @PathVariable Long id,
            @Parameter(description = "状态：1在线，2离线，3故障") @PathVariable Integer status) {
        Device device = deviceService.updateStatus(id, status);
        return Result.success("状态更新成功", device);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询设备", description = "根据设备状态查询设备列表")
    public Result<List<Device>> getByStatus(@Parameter(description = "状态：1在线，2离线，3故障") @PathVariable Integer status) {
        List<Device> devices = deviceService.findByStatus(status);
        return Result.success(devices);
    }

    @GetMapping("/statistics")
    @Operation(summary = "设备统计", description = "统计各状态设备数量")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("online", deviceService.countByStatus(1));
        statistics.put("offline", deviceService.countByStatus(2));
        statistics.put("fault", deviceService.countByStatus(3));
        statistics.put("total", deviceService.findAll().size());
        return Result.success(statistics);
    }

    @PostMapping("/batch/status")
    @Operation(summary = "批量更新设备状态", description = "批量更新设备状态")
    public Result<Void> batchUpdateStatus(
            @RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) params.get("ids");
        Integer status = (Integer) params.get("status");
        deviceService.batchUpdateStatus(ids, status);
        return Result.success("批量更新成功", null);
    }
}
