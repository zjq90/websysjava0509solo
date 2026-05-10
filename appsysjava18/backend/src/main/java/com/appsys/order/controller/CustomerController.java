package com.appsys.order.controller;

import com.appsys.common.result.Result;
import com.appsys.order.dto.CustomerDTO;
import com.appsys.order.entity.Customer;
import com.appsys.order.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "客户管理", description = "客户增删改查接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询所有客户列表
     */
    @Operation(summary = "查询客户列表", description = "查询所有客户信息")
    @GetMapping
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<List<Customer>> list() {
        List<Customer> customers = customerService.list();
        return Result.success(customers);
    }

    /**
     * 根据ID查询客户详情
     */
    @Operation(summary = "查询客户详情", description = "根据ID查询客户详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Customer> getById(@Parameter(description = "客户ID") @PathVariable Long id) {
        Customer customer = customerService.getById(id);
        return Result.success(customer);
    }

    /**
     * 新增客户
     */
    @Operation(summary = "新增客户", description = "新增客户信息")
    @PostMapping
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Customer> create(@Validated @RequestBody CustomerDTO dto) {
        Customer customer = customerService.create(dto);
        return Result.success("客户新增成功", customer);
    }

    /**
     * 更新客户信息
     */
    @Operation(summary = "更新客户", description = "更新客户信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Customer> update(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Validated @RequestBody CustomerDTO dto) {
        Customer customer = customerService.update(id, dto);
        return Result.success("客户更新成功", customer);
    }

    /**
     * 删除客户
     */
    @Operation(summary = "删除客户", description = "删除客户信息（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Void> delete(@Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.delete(id);
        return Result.success("客户删除成功", null);
    }
}
