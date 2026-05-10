package com.appsys.controller;

import com.appsys.dto.OrderCreateRequest;
import com.appsys.entity.*;
import com.appsys.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "系统功能测试接口")
public class TestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private LogisticsService logisticsService;

    @GetMapping("/demo-data")
    @Operation(summary = "获取演示数据", description = "获取所有测试数据用于功能测试")
    public ResponseEntity<Map<String, Object>> getDemoData() {
        Map<String, Object> data = new HashMap<>();
        data.put("products", productService.getAllProducts());
        data.put("customers", customerService.getAllCustomers());
        data.put("orders", salesOrderService.getAllOrders());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/create-test-order")
    @Operation(summary = "创建测试订单", description = "快速创建一个完整的测试订单用于演示")
    public ResponseEntity<SalesOrder> createTestOrder(@RequestParam Long customerId, 
                                                     @RequestParam Long productId, 
                                                     @RequestParam(defaultValue = "10") int quantity) {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(customerId);
        request.setNotes("测试订单 - 功能演示");
        
        OrderCreateRequest.OrderItemRequest item = new OrderCreateRequest.OrderItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);
        request.setOrderItems(Collections.singletonList(item));
        
        SalesOrder order = salesOrderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/full-order-flow/{orderId}")
    @Operation(summary = "执行完整订单流程", description = "自动执行从确认到完成的完整订单流程")
    public ResponseEntity<SalesOrder> runFullOrderFlow(@PathVariable Long orderId) {
        SalesOrder order = salesOrderService.getOrderById(orderId);
        
        if (order.getStatus() == OrderStatus.PENDING_CONFIRMATION) {
            order = salesOrderService.confirmOrder(orderId);
        }
        
        if (order.getStatus() == OrderStatus.PENDING_SIGNATURE) {
            order = salesOrderService.signContract(orderId);
        }
        
        if (order.getStatus() == OrderStatus.SIGNED) {
            order = salesOrderService.syncToErp(orderId);
        }
        
        if (order.getStatus() == OrderStatus.STOCK_PREPARING) {
            order = salesOrderService.shipOrder(orderId);
            
            logisticsService.createTracking(orderId, LogisticsStatus.IN_TRANSIT, 
                "SF" + System.currentTimeMillis(), "顺丰速运");
        }
        
        if (order.getStatus() == OrderStatus.SHIPPED) {
            order = salesOrderService.deliverOrder(orderId);
        }
        
        if (order.getStatus() == OrderStatus.DELIVERED) {
            order = salesOrderService.completeOrder(orderId);
        }
        
        return ResponseEntity.ok(order);
    }

    @GetMapping("/validation-test")
    @Operation(summary = "获取验证测试数据", description = "获取用于验证功能测试的数据参数")
    public ResponseEntity<Map<String, Object>> getValidationTestData() {
        Map<String, Object> data = new HashMap<>();
        
        Map<String, String> batchNumberTest = new HashMap<>();
        batchNumberTest.put("valid", "ABC12345");
        batchNumberTest.put("invalid_short", "AB123");
        batchNumberTest.put("invalid_long", "ABCDEFGHI12345");
        batchNumberTest.put("invalid_special", "AB@12345");
        data.put("batchNumber", batchNumberTest);
        
        Map<String, String> phoneTest = new HashMap<>();
        phoneTest.put("valid", "13812345678");
        phoneTest.put("invalid_start", "23812345678");
        phoneTest.put("invalid_length", "1381234567");
        phoneTest.put("invalid_format", "1381234567a");
        data.put("phone", phoneTest);
        
        Map<String, String> expiryDateTest = new HashMap<>();
        LocalDate today = LocalDate.now();
        expiryDateTest.put("valid", today.plusMonths(7).toString());
        expiryDateTest.put("invalid_past", today.minusDays(1).toString());
        expiryDateTest.put("invalid_soon", today.plusMonths(3).toString());
        data.put("expiryDate", expiryDateTest);
        
        Map<String, String> germinationRateTest = new HashMap<>();
        germinationRateTest.put("valid_min", "0.0");
        germinationRateTest.put("valid_max", "100.0");
        germinationRateTest.put("valid_normal", "85.5");
        germinationRateTest.put("invalid_negative", "-1.0");
        germinationRateTest.put("invalid_over", "100.1");
        data.put("germinationRate", germinationRateTest);
        
        return ResponseEntity.ok(data);
    }

    @GetMapping("/price-strategy")
    @Operation(summary = "测试价格策略", description = "获取不同客户等级的折扣信息")
    public ResponseEntity<List<Map<String, Object>>> getPriceStrategy() {
        List<Map<String, Object>> strategy = new ArrayList<>();
        
        for (CustomerLevel level : CustomerLevel.values()) {
            Map<String, Object> info = new HashMap<>();
            info.put("level", level.name());
            info.put("levelName", getLevelName(level));
            info.put("discountRate", level.getDiscountRate());
            info.put("examplePrice", new BigDecimal("100.00")
                .multiply(BigDecimal.valueOf(level.getDiscountRate()))
                .setScale(2, BigDecimal.ROUND_HALF_UP));
            strategy.add(info);
        }
        
        return ResponseEntity.ok(strategy);
    }

    private String getLevelName(CustomerLevel level) {
        switch (level) {
            case TEMPORARY: return "临时客户";
            case NORMAL: return "普通客户";
            case VIP: return "VIP客户";
            case SVIP: return "SVIP客户";
            case DIAMOND: return "钻石客户";
            default: return level.name();
        }
    }

    @PostMapping("/add-visit/{customerId}")
    @Operation(summary = "添加客户回访记录", description = "测试回访记录功能")
    public ResponseEntity<CustomerVisitRecord> addVisitRecord(
            @PathVariable Long customerId,
            @RequestParam String visitType,
            @RequestParam String content) {
        CustomerVisitRecord record = new CustomerVisitRecord();
        record.setCustomerId(customerId);
        record.setVisitType(visitType);
        record.setContent(content);
        record = customerService.addVisitRecord(record);
        return ResponseEntity.ok(record);
    }
}
