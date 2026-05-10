package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.VendingMachine;
import com.vending.service.VendingMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 售货机管理控制器
 * 提供售货机设备的增删改查接口
 */
@RestController
@RequestMapping("/api/machines")
@CrossOrigin(origins = "*")
@Tag(name = "售货机管理", description = "售货机设备管理接口")
public class VendingMachineController {
    
    @Autowired
    private VendingMachineService machineService;
    
    @GetMapping
    @Operation(summary = "获取所有售货机")
    public ApiResponse<List<VendingMachine>> getAll() {
        return ApiResponse.success(machineService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取售货机")
    public ApiResponse<VendingMachine> getById(@PathVariable Long id) {
        return machineService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("设备不存在", 404));
    }
    
    @PostMapping
    @Operation(summary = "创建售货机")
    public ApiResponse<VendingMachine> create(@RequestBody VendingMachine machine) {
        try {
            return ApiResponse.success("创建成功", machineService.save(machine));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新售货机")
    public ApiResponse<VendingMachine> update(@PathVariable Long id, @RequestBody VendingMachine machine) {
        try {
            return ApiResponse.success("更新成功", machineService.update(id, machine));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除售货机")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            machineService.deleteById(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "更新设备状态")
    public ApiResponse<VendingMachine> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            return ApiResponse.success("状态更新成功", machineService.updateStatus(id, status));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询设备")
    public ApiResponse<List<VendingMachine>> getByStatus(@PathVariable String status) {
        return ApiResponse.success(machineService.findByStatus(status));
    }
}
