package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.Crop;
import com.agriculture.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 作物品种管理控制器
 * 处理作物品种的增删改查操作，包含数据验证
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "作物品种管理", description = "育种作物品种的增删改查操作")
@RestController
@RequestMapping("/crops")
@CrossOrigin(origins = "*")
public class CropController {

    @Autowired
    private CropService cropService;

    /**
     * 查询所有作物品种
     */
    @Operation(summary = "查询所有品种", description = "获取系统中所有作物品种列表")
    @GetMapping
    public Result<List<Crop>> list() {
        return Result.success(cropService.findAll());
    }

    /**
     * 查询启用状态的品种
     */
    @Operation(summary = "查询启用的品种", description = "获取所有状态为启用的作物品种")
    @GetMapping("/active")
    public Result<List<Crop>> listActive() {
        return Result.success(cropService.findActiveCrops());
    }

    /**
     * 根据ID获取品种详情
     */
    @Operation(summary = "获取品种详情", description = "根据ID获取作物品种详细信息")
    @GetMapping("/{id}")
    public Result<Crop> getById(@Parameter(description = "品种ID") @PathVariable Long id) {
        Optional<Crop> cropOpt = cropService.findById(id);
        if (cropOpt.isPresent()) {
            return Result.success(cropOpt.get());
        } else {
            return Result.notFound("作物品种不存在");
        }
    }

    /**
     * 根据批次编号查询
     */
    @Operation(summary = "根据批次编号查询", description = "通过8位批次编号查询作物品种")
    @GetMapping("/batch/{code}")
    public Result<Crop> getByBatchCode(@Parameter(description = "批次编号（8位）") @PathVariable String code) {
        Optional<Crop> cropOpt = cropService.findByBatchCode(code);
        if (cropOpt.isPresent()) {
            return Result.success(cropOpt.get());
        } else {
            return Result.notFound("作物品种不存在");
        }
    }

    /**
     * 根据作物类型查询
     */
    @Operation(summary = "根据类型查询品种", description = "根据作物类型（玉米、小麦等）筛选品种")
    @GetMapping("/type/{type}")
    public Result<List<Crop>> getByType(@Parameter(description = "作物类型：CORN/WHEAT/RICE等") @PathVariable String type) {
        return Result.success(cropService.findByCropType(type));
    }

    /**
     * 创建作物品种
     */
    @Operation(summary = "创建品种", description = "新增作物品种，包含数据验证（批次编号、保质期、发芽率等）")
    @PostMapping
    public Result<Crop> create(@RequestBody Crop crop) {
        try {
            Crop created = cropService.create(crop);
            return Result.success("创建成功", created);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 更新作物品种
     */
    @Operation(summary = "更新品种", description = "修改已有的作物品种信息")
    @PutMapping
    public Result<Crop> update(@RequestBody Crop crop) {
        try {
            Crop updated = cropService.update(crop);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 删除作物品种
     */
    @Operation(summary = "删除品种", description = "根据ID删除作物品种")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "品种ID") @PathVariable Long id) {
        cropService.deleteById(id);
        return Result.success();
    }
}
