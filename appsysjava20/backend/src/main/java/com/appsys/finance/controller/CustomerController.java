package com.appsys.finance.controller;

import com.appsys.finance.entity.Customer;
import com.appsys.finance.service.CustomerService;
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
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "客户管理", description = "客户的增删改查接口")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @Operation(summary = "创建客户", description = "创建新客户，手机号和邮箱会自动AES-256加密存储")
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!isValidPhone(customer.getPhone())) {
                response.put("success", false);
                response.put("message", "手机号格式不正确，应为1开头的11位数字");
                return ResponseEntity.badRequest().body(response);
            }
            Customer saved = customerService.createCustomer(customer);
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
    @Operation(summary = "获取客户详情", description = "根据ID获取客户信息，敏感信息会自动解密")
    public ResponseEntity<Map<String, Object>> getCustomerById(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return customerService.getCustomerById(id)
            .map(customer -> {
                response.put("success", true);
                response.put("data", customer);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "客户不存在");
                return ResponseEntity.notFound().build();
            });
    }

    @GetMapping
    @Operation(summary = "获取客户列表", description = "获取所有客户列表")
    public ResponseEntity<Map<String, Object>> getAllCustomers() {
        Map<String, Object> response = new HashMap<>();
        List<Customer> customers = customerService.getAllCustomers();
        response.put("success", true);
        response.put("data", customers);
        response.put("total", customers.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新客户", description = "更新客户信息")
    public ResponseEntity<Map<String, Object>> updateCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @RequestBody Customer customerDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Customer updated = customerService.updateCustomer(id, customerDetails);
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
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    public ResponseEntity<Map<String, Object>> deleteCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        customerService.deleteCustomer(id);
        response.put("success", true);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return true;
        }
        return phone.matches("^1\\d{10}$");
    }
}
