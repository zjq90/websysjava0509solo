package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.RestockOrder;
import com.vending.service.RestockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 补货管理控制器
 * 提供智能补货、补货单管理等接口
 */
@RestController
@RequestMapping("/api/restocks")
@CrossOrigin(origins = "*")
@Tag(name = "智能补货", description = "智能补货与补货单管理接口")
public class RestockController {
    
    @Autowired
    private RestockService restockService;
    
    @GetMapping
    @Operation(summary = "获取所有补货单")
    public ApiResponse<List<RestockOrder>> getAll() {
        return ApiResponse.success(restockService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取补货单")
    public ApiResponse<RestockOrder> getById(@PathVariable Long id) {
        return restockService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("补货单不存在", 404));
    }
    
    @PostMapping("/auto-generate")
    @Operation(summary = "自动生成补货单（智能补货）")
    public ApiResponse<Integer> autoGenerate() {
        try {
            int count = restockService.autoGenerateRestockOrders();
            return ApiResponse.success("自动生成了 " + count + " 个补货单", count);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/manual")
    @Operation(summary = "手动创建补货单")
    public ApiResponse<RestockOrder> createManual(@RequestBody Map<String, Object> request) {
        try {
            Long machineId = Long.valueOf(request.get("machineId").toString());
            @SuppressWarnings("unchecked")
            List<Long> slotIds = (List<Long>) request.get("slotIds");
            String operator = (String) request.get("operator");
            String remark = (String) request.get("remark");
            
            return ApiResponse.success("创建成功", 
                restockService.createManualRestock(machineId, slotIds, operator, remark));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{restockId}/complete")
    @Operation(summary = "完成补货")
    public ApiResponse<RestockOrder> complete(@PathVariable Long restockId, 
                                              @RequestBody(required = false) Map<String, String> request) {
        try {
            String operator = request != null ? request.get("operator") : null;
            return ApiResponse.success("补货完成", 
                restockService.completeRestock(restockId, operator));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{restockId}/cancel")
    @Operation(summary = "取消补货单")
    public ApiResponse<RestockOrder> cancel(@PathVariable Long restockId, 
                                            @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            return ApiResponse.success("取消成功", 
                restockService.cancelRestock(restockId, reason));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{restockId}/mark-notified")
    @Operation(summary = "标记已提醒")
    public ApiResponse<RestockOrder> markNotified(@PathVariable Long restockId) {
        try {
            return ApiResponse.success("标记成功", restockService.markNotified(restockId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/unnotified")
    @Operation(summary = "获取未提醒的补货单")
    public ApiResponse<List<RestockOrder>> getUnnotified() {
        return ApiResponse.success(restockService.getUnnotifiedOrders());
    }
    
    @GetMapping("/pending-count")
    @Operation(summary = "统计待处理补货单数量")
    public ApiResponse<Long> getPendingCount() {
        return ApiResponse.success(restockService.countPendingOrders());
    }
}
