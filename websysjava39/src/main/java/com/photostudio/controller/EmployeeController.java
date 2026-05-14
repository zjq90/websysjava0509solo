package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Employee;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 * 提供员工的增删改查API接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Tag(name = "员工管理", description = "员工CRUD API")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 获取所有员工
     */
    @GetMapping
    @Operation(summary = "获取所有员工", description = "获取所有员工列表")
    public Result<List<Employee>> getAllEmployees() {
        return Result.success(employeeRepository.findAll());
    }

    /**
     * 根据岗位获取员工
     */
    @GetMapping("/position/{position}")
    @Operation(summary = "根据岗位获取员工", description = "根据岗位类型获取员工列表")
    public Result<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        return Result.success(employeeRepository.findByPositionAndActive(position, true));
    }

    /**
     * 根据ID获取员工
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取员工详情", description = "根据ID获取员工详情")
    public Result<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("员工不存在: " + id));
        return Result.success(employee);
    }

    /**
     * 创建员工
     */
    @PostMapping
    @Operation(summary = "创建员工", description = "创建新的员工")
    public Result<Employee> createEmployee(@RequestBody Employee employee) {
        return Result.success(employeeRepository.save(employee));
    }

    /**
     * 更新员工
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新员工", description = "根据ID更新员工信息")
    public Result<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("员工不存在: " + id));
        
        employee.setName(employeeDetails.getName());
        employee.setPhone(employeeDetails.getPhone());
        employee.setPosition(employeeDetails.getPosition());
        employee.setStore(employeeDetails.getStore());
        employee.setHireDate(employeeDetails.getHireDate());
        employee.setBaseSalary(employeeDetails.getBaseSalary());
        employee.setActive(employeeDetails.getActive());
        
        return Result.success(employeeRepository.save(employee));
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工", description = "根据ID删除员工")
    public Result<Void> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("员工不存在: " + id));
        
        employeeRepository.delete(employee);
        return Result.success();
    }
}
