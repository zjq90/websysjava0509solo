package com.seedinventory.controller;

import com.seedinventory.common.Result;
import com.seedinventory.entity.Customer;
import com.seedinventory.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 客户管理控制器
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "客户管理", description = "客户信息的增删改查")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    @Operation(summary = "新增客户", description = "创建新客户，敏感数据自动加密")
    public Result<Customer> create(@RequestBody Customer customer) {
        try {
            Customer result = customerService.create(customer);
            return Result.success("创建成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping
    @Operation(summary = "更新客户", description = "更新客户信息")
    public Result<Customer> update(@RequestBody Customer customer) {
        try {
            Customer result = customerService.update(customer);
            return Result.success("更新成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "查询客户详情", description = "根据ID查询客户信息（已解密）")
    public Result<Customer> getById(@PathVariable Long id) {
        Optional<Customer> opt = customerService.findById(id);
        if (opt.isPresent()) {
            return Result.success(opt.get());
        }
        return Result.error(404, "客户不存在");
    }
    
    @GetMapping
    @Operation(summary = "查询客户列表", description = "查询所有客户")
    public Result<List<Customer>> list() {
        return Result.success(customerService.findAll());
    }
    
    @GetMapping("/code/{customerCode}")
    @Operation(summary = "根据编号查询", description = "根据客户编号查询")
    public Result<Customer> getByCode(@PathVariable String customerCode) {
        Optional<Customer> opt = customerService.findByCustomerCode(customerCode);
        if (opt.isPresent()) {
            return Result.success(opt.get());
        }
        return Result.error(404, "客户不存在");
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            customerService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
