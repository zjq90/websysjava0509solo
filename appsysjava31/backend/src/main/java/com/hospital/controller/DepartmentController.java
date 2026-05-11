package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Department;
import com.hospital.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室控制器
 * 处理科室相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/public/departments")
@Tag(name = "科室管理", description = "科室相关接口")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取所有科室
     */
    @GetMapping
    @Operation(summary = "获取所有科室", description = "获取所有启用的科室列表")
    public Result<List<Department>> getAllDepartments() {
        return Result.success(departmentService.getAllDepartments());
    }

    /**
     * 获取一级科室
     */
    @GetMapping("/parent")
    @Operation(summary = "获取一级科室", description = "获取所有顶级科室")
    public Result<List<Department>> getParentDepartments() {
        return Result.success(departmentService.getParentDepartments());
    }

    /**
     * 获取子科室
     */
    @GetMapping("/{parentId}/children")
    @Operation(summary = "获取子科室", description = "获取指定父科室下的所有子科室")
    public Result<List<Department>> getChildDepartments(@PathVariable Long parentId) {
        return Result.success(departmentService.getChildDepartments(parentId));
    }

    /**
     * 获取科室详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取科室详情", description = "根据ID获取科室详细信息")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return Result.success(departmentService.getDepartmentById(id));
    }
}
