package com.seedinventory.controller;

import com.seedinventory.common.Result;
import com.seedinventory.entity.Warehouse;
import com.seedinventory.repository.WarehouseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 仓库管理控制器
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "*")
@Tag(name = "仓库管理", description = "仓库信息的增删改查")
public class WarehouseController {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @PostMapping
    @Operation(summary = "新增仓库")
    public Result<Warehouse> create(@RequestBody Warehouse warehouse) {
        if (warehouseRepository.existsByWarehouseCode(warehouse.getWarehouseCode())) {
            return Result.error("仓库编号已存在");
        }
        Warehouse result = warehouseRepository.save(warehouse);
        return Result.success("创建成功", result);
    }
    
    @PutMapping
    @Operation(summary = "更新仓库")
    public Result<Warehouse> update(@RequestBody Warehouse warehouse) {
        if (!warehouseRepository.existsById(warehouse.getId())) {
            return Result.error(404, "仓库不存在");
        }
        Warehouse result = warehouseRepository.save(warehouse);
        return Result.success("更新成功", result);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "查询仓库详情")
    public Result<Warehouse> getById(@PathVariable Long id) {
        Optional<Warehouse> opt = warehouseRepository.findById(id);
        if (opt.isPresent()) {
            return Result.success(opt.get());
        }
        return Result.error(404, "仓库不存在");
    }
    
    @GetMapping
    @Operation(summary = "查询仓库列表")
    public Result<List<Warehouse>> list() {
        return Result.success(warehouseRepository.findAll());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除仓库")
    public Result<Void> delete(@PathVariable Long id) {
        if (!warehouseRepository.existsById(id)) {
            return Result.error(404, "仓库不存在");
        }
        warehouseRepository.deleteById(id);
        return Result.success();
    }
}
