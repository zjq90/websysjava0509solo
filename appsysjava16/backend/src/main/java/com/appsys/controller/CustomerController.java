package com.appsys.controller;

import com.appsys.entity.Customer;
import com.appsys.entity.CustomerVisitRecord;
import com.appsys.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户管理控制器
 * 提供客户的增删改查API接口，支持临时客户注册
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/customers")
@Tag(name = "客户管理", description = "客户CRUD操作、临时客户注册、历史订单查询等")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 获取所有客户列表
     */
    @GetMapping
    @Operation(summary = "获取所有客户", description = "返回所有客户列表，按创建时间倒序排列")
    public ResponseEntity<Map<String, Object>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return successResponse(customers);
    }

    /**
     * 根据ID获取客户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取客户详情", description = "根据客户ID获取客户详细信息")
    public ResponseEntity<Map<String, Object>> getCustomerById(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return successResponse(customer);
    }

    /**
     * 创建新客户
     */
    @PostMapping
    @Operation(summary = "创建客户", description = "创建新客户，手机号和地址会自动加密存储")
    public ResponseEntity<Map<String, Object>> createCustomer(
            @RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return successResponse(createdCustomer);
    }

    /**
     * 快速注册临时客户
     * 用于展会或下乡推广时快速下单
     */
    @PostMapping("/temporary")
    @Operation(summary = "注册临时客户", description = "快速注册临时客户，仅需姓名和手机号，便于展会或下乡推广时快速下单")
    public ResponseEntity<Map<String, Object>> createTemporaryCustomer(
            @Parameter(description = "客户姓名") @RequestParam String name,
            @Parameter(description = "手机号") @RequestParam String phone) {
        Customer customer = customerService.createTemporaryCustomer(name, phone);
        return successResponse(customer);
    }

    /**
     * 更新客户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新客户", description = "根据ID更新客户信息")
    public ResponseEntity<Map<String, Object>> updateCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return successResponse(updatedCustomer);
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    public ResponseEntity<Map<String, Object>> deleteCustomer(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return successResponse("客户删除成功");
    }

    /**
     * 搜索客户（按名称）
     */
    @GetMapping("/search")
    @Operation(summary = "搜索客户", description = "根据客户名称关键字模糊搜索")
    public ResponseEntity<Map<String, Object>> searchCustomers(
            @Parameter(description = "客户名称关键字") @RequestParam String name) {
        List<Customer> customers = customerService.searchCustomersByName(name);
        return successResponse(customers);
    }

    /**
     * 获取客户历史订单
     */
    @GetMapping("/{id}/orders")
    @Operation(summary = "获取客户历史订单", description = "获取指定客户的所有历史订单")
    public ResponseEntity<Map<String, Object>> getCustomerHistoryOrders(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        List<SalesOrder> orders = customerService.getCustomerHistoryOrders(id);
        return successResponse(orders);
    }

    /**
     * 获取客户回访记录
     */
    @GetMapping("/{id}/visits")
    @Operation(summary = "获取客户回访记录", description = "获取指定客户的所有回访记录")
    public ResponseEntity<Map<String, Object>> getCustomerVisitRecords(
            @Parameter(description = "客户ID") @PathVariable Long id) {
        List<CustomerVisitRecord> records = customerService.getCustomerVisitRecords(id);
        return successResponse(records);
    }

    /**
     * 添加客户回访记录
     */
    @PostMapping("/{id}/visits")
    @Operation(summary = "添加回访记录", description = "为指定客户添加回访记录")
    public ResponseEntity<Map<String, Object>> addVisitRecord(
            @Parameter(description = "客户ID") @PathVariable Long id,
            @RequestBody CustomerVisitRecord record) {
        CustomerVisitRecord createdRecord = customerService.addVisitRecord(id, record);
        return successResponse(createdRecord);
    }

    /**
     * 获取临时客户列表
     */
    @GetMapping("/temporary")
    @Operation(summary = "获取临时客户", description = "获取所有临时客户列表")
    public ResponseEntity<Map<String, Object>> getTemporaryCustomers() {
        List<Customer> customers = customerService.getTemporaryCustomers();
        return successResponse(customers);
    }

    /**
     * 获取正式客户列表
     */
    @GetMapping("/regular")
    @Operation(summary = "获取正式客户", description = "获取所有正式客户列表")
    public ResponseEntity<Map<String, Object>> getRegularCustomers() {
        List<Customer> customers = customerService.getRegularCustomers();
        return successResponse(customers);
    }

    /**
     * 获取消费排行前N的客户
     */
    @GetMapping("/top/{limit}")
    @Operation(summary = "获取消费排行客户", description = "获取累计消费金额排行前N的客户")
    public ResponseEntity<Map<String, Object>> getTopCustomers(
            @Parameter(description = "数量限制") @PathVariable int limit) {
        List<Customer> customers = customerService.getTopCustomers(limit);
        return successResponse(customers);
    }

    /**
     * 构建成功响应
     */
    private ResponseEntity<Map<String, Object>> successResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}
