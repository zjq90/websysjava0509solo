package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.IotDevice;
import com.culturalrelic.service.IotDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 物联网设备管理控制器
 */
@RestController
@RequestMapping("/iot-device")
@Tag(name = "物联网设备管理", description = "温湿度传感器设备的增删改查，支持NFC近场通信绑定")
public class IotDeviceController {

    @Autowired
    private IotDeviceService iotDeviceService;

    /**
     * 新增设备
     */
    @PostMapping
    @Operation(summary = "新增设备", description = "添加新的温湿度传感器设备")
    public Result<IotDevice> save(@RequestBody IotDevice device) {
        IotDevice saved = iotDeviceService.save(device);
        return Result.success("新增成功", saved);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询设备详情", description = "根据ID查询设备详细信息")
    public Result<IotDevice> findById(@Parameter(description = "设备ID") @PathVariable Long id) {
        Optional<IotDevice> device = iotDeviceService.findById(id);
        if (device.isPresent()) {
            return Result.success(device.get());
        }
        return Result.error("设备不存在");
    }

    /**
     * 分页查询所有
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询设备", description = "分页查询所有设备列表")
    public Result<Page<IotDevice>> findAll(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<IotDevice> result = iotDeviceService.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 查询所有
     */
    @GetMapping
    @Operation(summary = "查询所有设备", description = "获取所有设备列表")
    public Result<List<IotDevice>> findAll() {
        List<IotDevice> list = iotDeviceService.findAll();
        return Result.success(list);
    }

    /**
     * 更新设备
     */
    @PutMapping
    @Operation(summary = "更新设备", description = "更新设备信息")
    public Result<IotDevice> update(@RequestBody IotDevice device) {
        IotDevice updated = iotDeviceService.update(device);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备", description = "根据ID删除设备（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "设备ID") @PathVariable Long id) {
        boolean success = iotDeviceService.delete(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * NFC绑定文物
     */
    @PostMapping("/bind/{deviceId}/{relicId}")
    @Operation(summary = "NFC绑定文物", description = "通过NFC近场通信将设备绑定到文物")
    public Result<IotDevice> bindToRelic(
            @Parameter(description = "设备ID") @PathVariable Long deviceId,
            @Parameter(description = "文物ID") @PathVariable Long relicId,
            @Parameter(description = "文物名称") @RequestParam String relicName) {
        IotDevice device = iotDeviceService.bindToRelic(deviceId, relicId, relicName);
        return device != null ? Result.success("绑定成功", device) : Result.error("绑定失败");
    }

    /**
     * 解绑文物
     */
    @PostMapping("/unbind/{deviceId}")
    @Operation(summary = "解绑文物", description = "将设备与文物解绑")
    public Result<IotDevice> unbind(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        IotDevice device = iotDeviceService.unbind(deviceId);
        return device != null ? Result.success("解绑成功", device) : Result.error("解绑失败");
    }

    /**
     * 根据文物ID查询绑定设备
     */
    @GetMapping("/relic/{relicId}")
    @Operation(summary = "查询文物绑定设备", description = "根据文物ID查询已绑定的设备列表")
    public Result<List<IotDevice>> findByRelicId(@Parameter(description = "文物ID") @PathVariable Long relicId) {
        List<IotDevice> list = iotDeviceService.findByRelicId(relicId);
        return Result.success(list);
    }

    /**
     * 查询告警设备
     */
    @GetMapping("/alarm")
    @Operation(summary = "查询告警设备", description = "获取所有处于告警状态的设备列表")
    public Result<List<IotDevice>> findByAlarmStatus() {
        List<IotDevice> list = iotDeviceService.findByAlarmStatus(1);
        return Result.success(list);
    }
}
