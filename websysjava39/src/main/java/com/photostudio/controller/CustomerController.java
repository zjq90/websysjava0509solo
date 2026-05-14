package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Customer;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器
 * 提供客户的增删改查API接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "客户管理", description = "客户CRUD API")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 获取所有客户
     */
    @GetMapping
    @Operation(summary = "获取所有客户", description = "获取所有客户列表")
    public Result<List<Customer>> getAllCustomers() {
        return Result.success(customerRepository.findAll());
    }

    /**
     * 根据ID获取客户
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取客户详情", description = "根据ID获取客户详情")
    public Result<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("客户不存在: " + id));
        return Result.success(customer);
    }

    /**
     * 创建客户
     */
    @PostMapping
    @Operation(summary = "创建客户", description = "创建新的客户")
    public Result<Customer> createCustomer(@RequestBody Customer customer) {
        return Result.success(customerRepository.save(customer));
    }

    /**
     * 更新客户
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新客户", description = "根据ID更新客户信息")
    public Result<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("客户不存在: " + id));
        
        customer.setName(customerDetails.getName());
        customer.setPhone(customerDetails.getPhone());
        customer.setGender(customerDetails.getGender());
        customer.setBirthDate(customerDetails.getBirthDate());
        customer.setAddress(customerDetails.getAddress());
        customer.setWechat(customerDetails.getWechat());
        customer.setOldCustomer(customerDetails.getOldCustomer());
        
        return Result.success(customerRepository.save(customer));
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("客户不存在: " + id));
        
        customerRepository.delete(customer);
        return Result.success();
    }
}
