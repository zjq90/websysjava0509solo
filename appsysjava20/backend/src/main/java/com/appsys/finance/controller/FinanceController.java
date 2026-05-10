package com.appsys.finance.controller;

import com.appsys.finance.entity.SalesOrder;
import com.appsys.finance.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@CrossOrigin(origins = "*")
@Tag(name = "财务与报表", description = "个人业绩、回款情况、提成计算等财务报表接口")
public class FinanceController {

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping("/performance/{employeeId}")
    @Operation(summary = "获取个人业绩", description = "根据员工ID和月份获取个人销售业绩")
    public ResponseEntity<Map<String, Object>> getPersonalPerformance(
            @Parameter(description = "员工ID") @PathVariable Long employeeId,
            @Parameter(description = "年份") @RequestParam(required = false) Integer year,
            @Parameter(description = "月份") @RequestParam(required = false) Integer month) {
        
        if (year == null) year = LocalDate.now().getYear();
        if (month == null) month = LocalDate.now().getMonthValue();

        Map<String, Object> response = new HashMap<>();
        try {
            BigDecimal sales = salesOrderService.getPersonalSalesPerformance(employeeId, year, month);
            BigDecimal received = salesOrderService.getPersonalReceivedAmount(employeeId, year, month);
            BigDecimal commission = salesOrderService.getPersonalCommission(employeeId, year, month);
            List<SalesOrder> orders = salesOrderService.getOrdersByEmployeeId(employeeId);

            Map<String, Object> data = new HashMap<>();
            data.put("employeeId", employeeId);
            data.put("year", year);
            data.put("month", month);
            data.put("totalSalesAmount", sales != null ? sales : BigDecimal.ZERO);
            data.put("totalReceivedAmount", received != null ? received : BigDecimal.ZERO);
            data.put("commissionAmount", commission != null ? commission : BigDecimal.ZERO);
            data.put("pendingAmount", (sales != null ? sales : BigDecimal.ZERO)
                .subtract(received != null ? received : BigDecimal.ZERO));
            data.put("orders", orders);

            response.put("success", true);
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/orders")
    @Operation(summary = "获取销售订单列表", description = "获取所有销售订单")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SalesOrder> orders = salesOrderService.getAllOrders();
            response.put("success", true);
            response.put("data", orders);
            response.put("total", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/orders")
    @Operation(summary = "创建销售订单", description = "创建新的销售订单，自动计算总额和提成")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody SalesOrder order) {
        Map<String, Object> response = new HashMap<>();
        try {
            SalesOrder saved = salesOrderService.createOrder(order);
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

    @GetMapping("/orders/{id}")
    @Operation(summary = "获取订单详情", description = "根据ID获取订单详情")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return salesOrderService.getOrderById(id)
            .map(order -> {
                response.put("success", true);
                response.put("data", order);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            });
    }

    @PutMapping("/orders/{id}")
    @Operation(summary = "更新订单", description = "更新销售订单信息")
    public ResponseEntity<Map<String, Object>> updateOrder(
            @PathVariable Long id,
            @RequestBody SalesOrder orderDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            SalesOrder updated = salesOrderService.updateOrder(id, orderDetails);
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

    @DeleteMapping("/orders/{id}")
    @Operation(summary = "删除订单", description = "根据ID删除订单")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        salesOrderService.deleteOrder(id);
        response.put("success", true);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
