package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.Customer;
import com.agriculture.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 客户管理控制器
 * 处理客户信息的增删改查，敏感信息加密存储
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "客户管理", description = "客户信息的增删改查，敏感信息AES-256加密")
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询所有客户
     */
    @Operation(summary = "查询所有客户", description = "获取所有客户列表（敏感字段已解密）")
    @GetMapping
    public Result<List<Customer>> list() {
        return Result.success(customerService.findAll());
    }

    /**
     * 查询启用状态的客户
     */
    @Operation(summary = "查询启用客户", description = "获取所有状态为启用的客户")
    @GetMapping("/active")
    public Result<List<Customer>> listActive() {
        return Result.success(customerService.findActiveCustomers());
    }

    /**
     * 根据ID获取客户详情
     */
    @Operation(summary = "获取客户详情", description = "根据ID获取客户详细信息")
    @GetMapping("/{id}")
    public Result<Customer> getById(@Parameter(description = "客户ID") @PathVariable Long id) {
        Optional<Customer> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            return Result.success(customerOpt.get());
        } else {
            return Result.notFound("客户不存在");
        }
    }

    /**
     * 根据客户编号查询
     */
    @Operation(summary = "根据编号查询客户", description = "通过客户编号查询客户信息")
    @GetMapping("/code/{code}")
    public Result<Customer> getByCode(@Parameter(description = "客户编号") @PathVariable String code) {
        Optional<Customer> customerOpt = customerService.findByCustomerCode(code);
        if (customerOpt.isPresent()) {
            return Result.success(customerOpt.get());
        } else {
            return Result.notFound("客户不存在");
        }
    }

    /**
     * 根据客户类型查询
     */
    @Operation(summary = "按类型查询客户", description = "根据客户类型（企业、个人、科研单位）筛选")
    @GetMapping("/type/{type}")
    public Result<List<Customer>> getByType(
            @Parameter(description = "客户类型：COMPANY/INDIVIDUAL/RESEARCH") @PathVariable String type) {
        return Result.success(customerService.findByCustomerType(type));
    }

    /**
     * 根据客户等级查询
     */
    @Operation(summary = "按等级查询客户", description = "根据客户等级（VIP、普通）筛选")
    @GetMapping("/level/{level}")
    public Result<List<Customer>> getByLevel(
            @Parameter(description = "客户等级：VIP/NORMAL") @PathVariable String level) {
        return Result.success(customerService.findByCustomerLevel(level));
    }

    /**
     * 创建客户
     */
    @Operation(summary = "创建客户", description = "新增客户信息，敏感字段自动加密存储")
    @PostMapping
    public Result<Customer> create(@RequestBody Customer customer) {
        try {
            Customer created = customerService.create(customer);
            return Result.success("创建成功", created);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 更新客户
     */
    @Operation(summary = "更新客户", description = "修改客户信息")
    @PutMapping
    public Result<Customer> update(@RequestBody Customer customer) {
        try {
            Customer updated = customerService.update(customer);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 删除客户
     */
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.deleteById(id);
        return Result.success();
    }
}
