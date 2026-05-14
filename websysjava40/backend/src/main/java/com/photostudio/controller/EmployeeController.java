package com.photostudio.controller;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Employee.EmployeePosition;
import com.photostudio.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理控制器
 * 提供员工管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/employees")
@Tag(name = "员工管理", description = "员工信息的增删改查")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "获取所有员工", description = "获取系统中所有员工信息")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/active")
    @Operation(summary = "获取在职员工", description = "获取所有在职员工信息")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取员工", description = "根据员工ID获取员工详细信息")
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "员工ID") @PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/position/{position}")
    @Operation(summary = "根据职位获取员工", description = "根据职位类型获取员工列表")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(
            @Parameter(description = "职位类型") @PathVariable EmployeePosition position) {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(position));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索员工", description = "根据姓名关键词搜索员工")
    public ResponseEntity<List<Employee>> searchEmployees(
            @Parameter(description = "姓名关键词") @RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }

    @PostMapping
    @Operation(summary = "创建员工", description = "创建新员工信息")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新员工", description = "更新员工信息")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "员工ID") @PathVariable Long id,
            @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工", description = "删除员工信息")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "员工ID") @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "禁用员工", description = "将员工标记为离职状态")
    public ResponseEntity<Employee> deactivateEmployee(
            @Parameter(description = "员工ID") @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.deactivateEmployee(id));
    }
}
