package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.Slot;
import com.vending.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 货道管理控制器
 * 提供货道绑定商品、库存监控等接口
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "*")
@Tag(name = "货道管理", description = "货道与库存管理接口")
public class SlotController {
    
    @Autowired
    private SlotService slotService;
    
    @GetMapping("/machine/{machineId}")
    @Operation(summary = "获取指定设备的所有货道")
    public ApiResponse<List<Slot>> getByMachineId(@PathVariable Long machineId) {
        return ApiResponse.success(slotService.findByMachineId(machineId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取货道")
    public ApiResponse<Slot> getById(@PathVariable Long id) {
        return slotService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("货道不存在", 404));
    }
    
    @PutMapping("/{slotId}/bind-product")
    @Operation(summary = "绑定商品到货道")
    public ApiResponse<Slot> bindProduct(@PathVariable Long slotId, 
                                         @RequestParam(required = false) Long productId) {
        try {
            return ApiResponse.success("绑定成功", slotService.bindProduct(slotId, productId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/batch-bind")
    @Operation(summary = "批量绑定商品到货道")
    public ApiResponse<Void> batchBindProduct(@RequestBody Map<String, Object> request) {
        try {
            Long machineId = Long.valueOf(request.get("machineId").toString());
            @SuppressWarnings("unchecked")
            List<Integer> slotNumbers = (List<Integer>) request.get("slotNumbers");
            Long productId = request.get("productId") != null ? 
                Long.valueOf(request.get("productId").toString()) : null;
            slotService.batchBindProduct(machineId, slotNumbers, productId);
            return ApiResponse.success("批量绑定成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{slotId}/stock")
    @Operation(summary = "更新货道库存")
    public ApiResponse<Slot> updateStock(@PathVariable Long slotId, 
                                         @RequestBody Map<String, Integer> request) {
        try {
            Integer stock = request.get("stock");
            return ApiResponse.success("库存更新成功", slotService.updateStock(slotId, stock));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{slotId}/deduct-stock")
    @Operation(summary = "扣减库存（销售扣减）")
    public ApiResponse<Slot> deductStock(@PathVariable Long slotId, 
                                         @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            return ApiResponse.success("扣减成功", slotService.deductStock(slotId, quantity));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{slotId}/refill")
    @Operation(summary = "补货")
    public ApiResponse<Slot> refillStock(@PathVariable Long slotId, 
                                         @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            return ApiResponse.success("补货成功", slotService.refillStock(slotId, quantity));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "获取所有低库存货道")
    public ApiResponse<List<Slot>> getLowStockSlots() {
        return ApiResponse.success(slotService.findLowStockSlots());
    }
    
    @GetMapping("/low-stock/machine/{machineId}")
    @Operation(summary = "获取指定设备的低库存货道")
    public ApiResponse<List<Slot>> getLowStockSlotsByMachine(@PathVariable Long machineId) {
        return ApiResponse.success(slotService.findLowStockSlotsByMachine(machineId));
    }
    
    @GetMapping("/product-total-stock/{productId}")
    @Operation(summary = "获取商品总库存")
    public ApiResponse<Integer> getTotalStockByProduct(@PathVariable Long productId) {
        return ApiResponse.success(slotService.getTotalStockByProduct(productId));
    }
}
