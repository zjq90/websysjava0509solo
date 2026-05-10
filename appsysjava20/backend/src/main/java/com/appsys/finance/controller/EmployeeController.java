package com.appsys.finance.controller;

import com.appsys.finance.entity.Employee;
import com.appsys.finance.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Tag(name = "员工管理", description = "员工的增删改查接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "创建员工", description = "创建新员工")
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee saved = employeeService.createEmployee(employee);
            response.put("success", true);
            response.put("message", "创建成功");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取员工详情", description = "根据ID获取员工信息")
    public ResponseEntity<Map<String, Object>> getEmployeeById(
            @Parameter(description = "员工ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return employeeService.getEmployeeById(id)
            .map(employee -> {
                response.put("success", true);
                response.put("data", employee);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "员工不存在");
                return ResponseEntity.notFound().build();
            });
    }

    @GetMapping
    @Operation(summary = "获取员工列表", description = "获取所有员工列表")
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        Map<String, Object> response = new HashMap<>();
        List<Employee> employees = employeeService.getAllEmployees();
        response.put("success", true);
        response.put("data", employees);
        response.put("total", employees.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新员工", description = "更新员工信息")
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @Parameter(description = "员工ID") @PathVariable Long id,
            @RequestBody Employee employeeDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee updated = employeeService.updateEmployee(id, employeeDetails);
            response.put("success", true);
            response.put("message", "更新成功");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工", description = "根据ID删除员工")
    public ResponseEntity<Map<String, Object>> deleteEmployee(
            @Parameter(description = "员工ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        employeeService.deleteEmployee(id);
        response.put("success", true);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
