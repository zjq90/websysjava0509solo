package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Customer;
import com.photostudio.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "客户管理", description = "客户的增删改查")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @Operation(summary = "查询所有客户")
    public Result<List<Customer>> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询客户")
    public Result<Page<Customer>> findPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return customerService.findPage(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询客户")
    public Result<Customer> findById(@Parameter(description = "客户ID") @PathVariable Long id) {
        return customerService.findById(id);
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "根据手机号查询客户")
    public Result<Customer> findByPhone(@Parameter(description = "手机号") @PathVariable String phone) {
        return customerService.findByPhone(phone);
    }

    @GetMapping("/source/{source}")
    @Operation(summary = "根据来源查询客户")
    public Result<List<Customer>> findBySource(@Parameter(description = "来源") @PathVariable String source) {
        return customerService.findBySource(source);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索客户")
    public Result<List<Customer>> search(@Parameter(description = "关键词") @RequestParam String keyword) {
        return customerService.search(keyword);
    }

    @PostMapping
    @Operation(summary = "新增客户")
    public Result<Customer> add(@RequestBody Customer customer) {
        return customerService.add(customer);
    }

    @PutMapping
    @Operation(summary = "更新客户")
    public Result<Customer> update(@RequestBody Customer customer) {
        return customerService.update(customer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户")
    public Result<Void> delete(@Parameter(description = "客户ID") @PathVariable Long id) {
        return customerService.delete(id);
    }
}
