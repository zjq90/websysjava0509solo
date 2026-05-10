package com.seedinventory.controller;

import com.seedinventory.common.Result;
import com.seedinventory.entity.Inventory;
import com.seedinventory.entity.InventoryRecord;
import com.seedinventory.entity.Seed;
import com.seedinventory.entity.Warehouse;
import com.seedinventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 库存管理控制器
 * 提供入库、出库、库存查询等API接口
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
@Tag(name = "库存管理", description = "库存相关的增删改查及出入库操作")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @PostMapping("/inbound")
    @Operation(summary = "入库操作", description = "扫码或手动录入完成入库")
    public Result<Inventory> inbound(
            @RequestBody Inventory inventory,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String remark) {
        try {
            Inventory result = inventoryService.inbound(inventory, operator, remark);
            return Result.success("入库成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/outbound")
    @Operation(summary = "出库操作", description = "扫码批次号完成出库")
    public Result<Inventory> outbound(
            @Parameter(description = "批次号") @RequestParam String batchNo,
            @Parameter(description = "出库数量") @RequestParam BigDecimal quantity,
            @Parameter(description = "操作人") @RequestParam(required = false) String operator,
            @Parameter(description = "客户ID") @RequestParam(required = false) Long customerId,
            @Parameter(description = "关联单号") @RequestParam(required = false) String relatedNo,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        try {
            Inventory result = inventoryService.outbound(batchNo, quantity, operator, customerId, relatedNo, remark);
            return Result.success("出库成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/scan/{batchNo}")
    @Operation(summary = "扫码查询库存", description = "根据批次号查询库存信息")
    public Result<Map<String, Object>> scan(@PathVariable String batchNo) {
        Optional<Inventory> opt = inventoryService.findByBatchNo(batchNo);
        if (opt.isPresent()) {
            Inventory inventory = opt.get();
            Map<String, Object> result = new HashMap<>();
            result.put("inventory", inventory);
            result.put("records", inventoryService.getRecordsByBatchNo(batchNo));
            return Result.success(result);
        }
        return Result.error(404, "未找到该批次的库存记录");
    }
    
    @GetMapping("/list")
    @Operation(summary = "查询库存列表", description = "查询所有或指定仓库的库存")
    public Result<List<Inventory>> list(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId) {
        List<Inventory> list;
        if (warehouseId != null) {
            list = inventoryService.findByWarehouseId(warehouseId);
        } else {
            list = inventoryService.findAll();
        }
        return Result.success(list);
    }
    
    @GetMapping("/warehouses")
    @Operation(summary = "获取仓库列表", description = "获取所有活跃仓库")
    public Result<List<Warehouse>> getWarehouses() {
        return Result.success(inventoryService.getAllWarehouses());
    }
    
    @GetMapping("/seeds")
    @Operation(summary = "获取种子列表", description = "获取所有在售种子")
    public Result<List<Seed>> getSeeds() {
        return Result.success(inventoryService.getAllSeeds());
    }
    
    @GetMapping("/records/{batchNo}")
    @Operation(summary = "查询出入库记录", description = "根据批次号查询历史记录")
    public Result<List<InventoryRecord>> getRecords(@PathVariable String batchNo) {
        return Result.success(inventoryService.getRecordsByBatchNo(batchNo));
    }
    
    @PostMapping("/check-expiry")
    @Operation(summary = "检测近效期", description = "手动触发近效期检测")
    public Result<Integer> checkNearExpiry() {
        int count = inventoryService.checkNearExpiry();
        return Result.success("检测完成，发现 " + count + " 条预警记录", count);
    }
}
