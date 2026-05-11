package com.hospital.appointment.controller;

import com.hospital.appointment.common.Result;
import com.hospital.appointment.entity.Department;
import com.hospital.appointment.entity.Doctor;
import com.hospital.appointment.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生/科室控制器
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/doctors")
@Tag(name = "医生/科室管理", description = "医生和科室信息查询接口")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("")
    @Operation(summary = "获取医生列表", description = "按条件筛选医生列表")
    public Result<List<Doctor>> getDoctors(
            @RequestParam(required = false) @Parameter(description = "科室ID") Long deptId,
            @RequestParam(required = false) @Parameter(description = "职称级别: senior/deputy/attending/resident") String titleLevel,
            @RequestParam(required = false) @Parameter(description = "是否专家: 0-普通 1-专家") Integer isExpert) {
        return Result.success(doctorService.getDoctorsByConditions(deptId, titleLevel, isExpert));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取医生详情", description = "根据ID获取医生详情")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        return Result.success(doctorService.getDoctorById(id));
    }

    @GetMapping("/departments/{deptId}")
    @Operation(summary = "按科室获取医生", description = "根据科室ID获取该科室医生列表")
    public Result<List<Doctor>> getDoctorsByDept(@PathVariable Long deptId) {
        return Result.success(doctorService.getDoctorsByDept(deptId));
    }
}

@RestController
@RequestMapping("/departments")
@Tag(name = "科室管理", description = "科室信息查询接口")
class DepartmentController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("")
    @Operation(summary = "获取所有科室", description = "获取所有启用的科室列表")
    public Result<List<Department>> getAllDepartments() {
        return Result.success(doctorService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取科室详情", description = "根据ID获取科室详情")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return Result.success(doctorService.getDepartmentById(id));
    }
}
