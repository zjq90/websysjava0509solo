package com.appsys.inventory.controller;

import com.appsys.common.result.PageResult;
import com.appsys.common.result.Result;
import com.appsys.inventory.dto.InventoryDTO;
import com.appsys.inventory.entity.Inventory;
import com.appsys.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 库存管理控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "库存管理", description = "库存增删改查接口")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 分页查询库存列表
     */
    @Operation(summary = "分页查询库存列表", description = "根据关键字分页查询库存信息")
    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<PageResult<Inventory>> list(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键字（批次号或种子名称）") @RequestParam(required = false) String keyword) {
        PageResult<Inventory> result = inventoryService.list(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 根据ID查询库存详情
     */
    @Operation(summary = "查询库存详情", description = "根据ID查询库存详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Inventory> getById(@Parameter(description = "库存ID") @PathVariable Long id) {
        Inventory inventory = inventoryService.getById(id);
        return Result.success(inventory);
    }

    /**
     * 根据批次号查询库存
     */
    @Operation(summary = "根据批次号查询", description = "根据批次号查询库存信息")
    @GetMapping("/batch/{batchNo}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Inventory> getByBatchNo(@Parameter(description = "批次号") @PathVariable String batchNo) {
        Inventory inventory = inventoryService.getByBatchNo(batchNo);
        return Result.success(inventory);
    }

    /**
     * 新增库存入库
     */
    @Operation(summary = "新增库存", description = "新增库存入库记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Inventory> create(@Validated @RequestBody InventoryDTO dto) {
        Inventory inventory = inventoryService.create(dto);
        return Result.success("库存入库成功", inventory);
    }

    /**
     * 更新库存信息
     */
    @Operation(summary = "更新库存", description = "更新库存信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Inventory> update(
            @Parameter(description = "库存ID") @PathVariable Long id,
            @Validated @RequestBody InventoryDTO dto) {
        Inventory inventory = inventoryService.update(id, dto);
        return Result.success("库存更新成功", inventory);
    }

    /**
     * 删除库存记录
     */
    @Operation(summary = "删除库存", description = "删除库存记录（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Void> delete(@Parameter(description = "库存ID") @PathVariable Long id) {
        inventoryService.delete(id);
        return Result.success("库存删除成功", null);
    }
}
