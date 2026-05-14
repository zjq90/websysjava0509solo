package com.photostudio.controller;

import com.photostudio.entity.Customer;
import com.photostudio.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户管理控制器
 * 提供客户相关的REST API
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "客户管理", description = "客户信息的增删改查")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 创建客户
     */
    @PostMapping
    @Operation(summary = "创建客户", description = "创建新的客户信息")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    /**
     * 更新客户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新客户", description = "根据ID更新客户信息")
    public ResponseEntity<Customer> updateCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Valid @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户", description = "根据ID删除客户（软删除）")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据ID查询客户
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询客户", description = "根据ID查询客户信息")
    public ResponseEntity<Customer> getCustomerById(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询所有客户
     */
    @GetMapping
    @Operation(summary = "查询所有客户", description = "获取所有客户列表")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据生命周期阶段查询客户
     */
    @GetMapping("/lifecycle/{lifecycle}")
    @Operation(summary = "按生命周期查询", description = "根据客户生命周期阶段查询客户")
    public ResponseEntity<List<Customer>> getCustomersByLifecycle(
            @Parameter(description = "生命周期阶段") @PathVariable Customer.CustomerLifecycle lifecycle) {
        List<Customer> customers = customerService.getCustomersByLifecycle(lifecycle);
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据姓名搜索客户
     */
    @GetMapping("/search")
    @Operation(summary = "搜索客户", description = "根据姓名模糊搜索客户")
    public ResponseEntity<List<Customer>> searchCustomers(
            @Parameter(description = "客户姓名") @RequestParam String name) {
        List<Customer> customers = customerService.searchCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据手机号查询客户
     */
    @GetMapping("/phone/{phone}")
    @Operation(summary = "按手机号查询", description = "根据手机号查询客户信息")
    public ResponseEntity<Customer> getCustomerByPhone(
            @Parameter(description = "手机号") @PathVariable String phone) {
        return customerService.getCustomerByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 更新客户生命周期阶段
     */
    @PatchMapping("/{id}/lifecycle")
    @Operation(summary = "更新生命周期", description = "更新客户的生命周期阶段")
    public ResponseEntity<Customer> updateLifecycle(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @Parameter(description = "生命周期阶段") @RequestParam Customer.CustomerLifecycle lifecycle) {
        Customer customer = customerService.updateLifecycle(id, lifecycle);
        return ResponseEntity.ok(customer);
    }

    /**
     * 根据拍摄类型查询客户
     */
    @GetMapping("/photo-type/{photoType}")
    @Operation(summary = "按拍摄类型查询", description = "根据拍摄类型查询客户")
    public ResponseEntity<List<Customer>> getCustomersByPhotoType(
            @Parameter(description = "拍摄类型") @PathVariable Customer.PhotoType photoType) {
        List<Customer> customers = customerService.getCustomersByPhotoType(photoType);
        return ResponseEntity.ok(customers);
    }
}
