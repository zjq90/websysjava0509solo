package com.bike.controller;

import com.bike.common.Result;
import com.bike.entity.MaintenanceStaff;
import com.bike.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运维人员控制器
 * 
 * @author bike-sharing
 */
@Tag(name = "运维人员", description = "运维人员管理、登录等接口")
@RestController
@RequestMapping("/api/staff")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Operation(summary = "获取所有运维人员")
    @GetMapping("/list")
    public Result<List<MaintenanceStaff>> getAllStaffs() {
        return Result.success(staffService.getAllStaffs());
    }

    @Operation(summary = "按状态获取运维人员")
    @GetMapping("/list/status/{status}")
    public Result<List<MaintenanceStaff>> getStaffsByStatus(
            @Parameter(description = "状态") @PathVariable String status) {
        return Result.success(staffService.getStaffsByStatus(status));
    }

    @Operation(summary = "获取运维人员详情")
    @GetMapping("/{id}")
    public Result<MaintenanceStaff> getStaffById(@Parameter(description = "人员ID") @PathVariable Long id) {
        return Result.success(staffService.getStaffById(id));
    }

    @Operation(summary = "创建运维人员")
    @PostMapping("/create")
    public Result<MaintenanceStaff> createStaff(@RequestBody MaintenanceStaff staff) {
        return Result.success(staffService.createStaff(staff));
    }

    @Operation(summary = "更新运维人员")
    @PutMapping("/{id}")
    public Result<MaintenanceStaff> updateStaff(
            @Parameter(description = "人员ID") @PathVariable Long id,
            @RequestBody MaintenanceStaff staff) {
        return Result.success(staffService.updateStaff(id, staff));
    }

    @Operation(summary = "删除运维人员")
    @DeleteMapping("/{id}")
    public Result<Void> deleteStaff(@Parameter(description = "人员ID") @PathVariable Long id) {
        staffService.deleteStaff(id);
        return Result.success();
    }

    @Operation(summary = "运维人员登录")
    @PostMapping("/login")
    public Result<MaintenanceStaff> login(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "密码") @RequestParam String password) {
        MaintenanceStaff staff = staffService.login(phone, password);
        if (staff != null) {
            return Result.success("登录成功", staff);
        } else {
            return Result.error(401, "手机号或密码错误");
        }
    }
}
