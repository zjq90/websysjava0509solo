package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Employee;
import com.photostudio.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/employees")
@Tag(name = "员工管理", description = "员工的增删改查")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    @Operation(summary = "查询所有员工")
    public Result<List<Employee>> findAll() {
        return Result.success(employeeRepository.findAll());
    }

    @GetMapping("/position/{position}")
    @Operation(summary = "根据职位查询")
    public Result<List<Employee>> findByPosition(@PathVariable String position) {
        return Result.success(employeeRepository.findByPosition(position));
    }

    @GetMapping("/specialty/{specialty}")
    @Operation(summary = "根据专长查询")
    public Result<List<Employee>> findBySpecialty(@PathVariable String specialty) {
        return Result.success(employeeRepository.findBySpecialtyContaining(specialty));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public Result<Employee> findById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("员工不存在"));
    }

    @PostMapping
    @Operation(summary = "新增员工")
    public Result<Employee> add(@RequestBody Employee employee) {
        Employee saved = employeeRepository.save(employee);
        return Result.success("员工创建成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新员工")
    public Result<Employee> update(@RequestBody Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            return Result.error("员工不存在");
        }
        Employee saved = employeeRepository.save(employee);
        return Result.success("员工更新成功", saved);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工")
    public Result<Void> delete(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            return Result.error("员工不存在");
        }
        employeeRepository.deleteById(id);
        return Result.success("员工删除成功", null);
    }
}
