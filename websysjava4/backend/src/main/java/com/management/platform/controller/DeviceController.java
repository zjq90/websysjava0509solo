package com.management.platform.controller;

import com.management.platform.entity.Device;
import com.management.platform.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备控制器
 * 提供设备CRUD和统计分析的REST API
 */
@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
@Tag(name = "设备管理", description = "设备的增删改查及统计分析接口")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 创建新设备
     * @param device 设备信息
     * @return 创建后的设备
     */
    @PostMapping
    @Operation(summary = "创建设备", description = "创建一个新的设备")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device created = deviceService.createDevice(device);
        return ResponseEntity.ok(created);
    }

    /**
     * 根据ID获取设备详情
     * @param id 设备ID
     * @return 设备详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取设备详情", description = "根据ID获取设备详细信息")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Device device = deviceService.getDeviceById(id);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }

    /**
     * 分页获取设备列表
     * @param page 页码，从0开始
     * @param size 每页大小
     * @return 设备分页列表
     */
    @GetMapping
    @Operation(summary = "获取设备列表", description = "分页获取所有设备列表")
    public ResponseEntity<Page<Device>> getAllDevices(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(deviceService.getAllDevices(page, size));
    }

    /**
     * 更新设备信息
     * @param id 设备ID
     * @param device 更新的设备信息
     * @return 更新后的设备
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新设备", description = "更新指定设备的信息")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        Device updated = deviceService.updateDevice(id, device);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * 删除设备
     * @param id 设备ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备", description = "删除指定设备")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        boolean deleted = deviceService.deleteDevice(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 获取单机产出统计
     * @param limit 返回数量限制
     * @return 单机产出数据
     */
    @GetMapping("/stats/single-output")
    @Operation(summary = "单机产出统计", description = "获取单机产出统计数据")
    public ResponseEntity<List<Map<String, Object>>> getSingleMachineOutput(
            @Parameter(description = "返回数量限制") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(deviceService.getSingleMachineOutput(limit));
    }

    /**
     * 获取故障率统计
     * @return 故障率统计数据
     */
    @GetMapping("/stats/fault-rate")
    @Operation(summary = "故障率统计", description = "获取设备故障率统计数据")
    public ResponseEntity<Map<String, Object>> getFaultRateStatistics() {
        return ResponseEntity.ok(deviceService.getFaultRateStatistics());
    }

    /**
     * 获取运维成本分析
     * @return 运维成本统计数据
     */
    @GetMapping("/stats/maintenance-cost")
    @Operation(summary = "运维成本分析", description = "获取设备运维成本分析数据")
    public ResponseEntity<Map<String, Object>> getMaintenanceCostAnalysis() {
        return ResponseEntity.ok(deviceService.getMaintenanceCostAnalysis());
    }
}
