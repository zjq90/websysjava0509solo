package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Department;
import com.hospital.repository.DepartmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "科室管理", description = "医院科室相关接口")
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Operation(summary = "获取所有启用的科室列表")
    @GetMapping("/list")
    public Result<List<Department>> listDepartments() {
        List<Department> departments = departmentRepository.findByStatusOrderBySortOrderAsc(1);
        return Result.success(departments);
    }

    @Operation(summary = "获取所有科室列表（含停用）")
    @GetMapping("/all")
    public Result<List<Department>> listAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return Result.success(departments);
    }

    @Operation(summary = "获取科室详情")
    @GetMapping("/{id}")
    public Result<Department> getDepartment(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .<Result<Department>>map(Result::success)
                .orElse(Result.<Department>notFound());
    }

    @Operation(summary = "按科室编码获取")
    @GetMapping("/code/{deptCode}")
    public Result<Department> getByCode(@PathVariable String deptCode) {
        return departmentRepository.findByDeptCode(deptCode)
                .<Result<Department>>map(Result::success)
                .orElse(Result.<Department>notFound());
    }
}
