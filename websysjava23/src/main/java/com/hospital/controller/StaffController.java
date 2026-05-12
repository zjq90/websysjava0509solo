package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Staff;
import com.hospital.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医护人员Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/staff")
@CrossOrigin
@Tag(name = "医护人员管理", description = "医生、护士等工作人员信息管理")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    @Operation(summary = "查询所有医护人员")
    public Result<List<Staff>> findAll() {
        return Result.success(staffService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询医护人员")
    public Result<Staff> findById(@PathVariable Long id) {
        return staffService.findById(id)
                .map(Result::success)
                .orElse(Result.error("人员不存在"));
    }

    @GetMapping("/doctors")
    @Operation(summary = "查询所有医生")
    public Result<List<Staff>> findAllDoctors() {
        return Result.success(staffService.findAllDoctors());
    }

    @GetMapping("/nurses")
    @Operation(summary = "查询所有护士")
    public Result<List<Staff>> findAllNurses() {
        return Result.success(staffService.findAllNurses());
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "根据角色查询人员")
    public Result<List<Staff>> findByRole(@PathVariable String role) {
        return Result.success(staffService.findByRole(role));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "根据科室查询人员")
    public Result<List<Staff>> findByDepartment(@PathVariable String department) {
        return Result.success(staffService.findByDepartment(department));
    }

    @PostMapping
    @Operation(summary = "新增医护人员")
    public Result<Staff> save(@RequestBody Staff staff) {
        return Result.success(staffService.save(staff));
    }

    @PutMapping
    @Operation(summary = "修改医护人员")
    public Result<Staff> update(@RequestBody Staff staff) {
        return Result.success(staffService.save(staff));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除医护人员")
    public Result<Void> deleteById(@PathVariable Long id) {
        staffService.deleteById(id);
        return Result.success();
    }
}
