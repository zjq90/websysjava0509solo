package com.bike.controller;

import com.bike.common.Result;
import com.bike.entity.Area;
import com.bike.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域管理控制器
 * 
 * @author bike-sharing
 */
@Tag(name = "区域管理", description = "区域信息、热力区域等接口")
@RestController
@RequestMapping("/api/area")
@CrossOrigin
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Operation(summary = "获取所有区域")
    @GetMapping("/list")
    public Result<List<Area>> getAllAreas() {
        return Result.success(areaService.getAllAreas());
    }

    @Operation(summary = "按类型获取区域")
    @GetMapping("/list/type/{type}")
    public Result<List<Area>> getAreasByType(
            @Parameter(description = "类型") @PathVariable String type) {
        return Result.success(areaService.getAreasByType(type));
    }

    @Operation(summary = "获取高需求区域")
    @GetMapping("/list/high-demand")
    public Result<List<Area>> getHighDemandAreas(
            @Parameter(description = "最低需求等级") @RequestParam(defaultValue = "7") Integer minLevel) {
        return Result.success(areaService.getHighDemandAreas(minLevel));
    }

    @Operation(summary = "获取区域详情")
    @GetMapping("/{id}")
    public Result<Area> getAreaById(@Parameter(description = "区域ID") @PathVariable Long id) {
        return Result.success(areaService.getAreaById(id));
    }

    @Operation(summary = "创建区域")
    @PostMapping("/create")
    public Result<Area> createArea(@RequestBody Area area) {
        return Result.success(areaService.createArea(area));
    }

    @Operation(summary = "更新区域")
    @PutMapping("/{id}")
    public Result<Area> updateArea(
            @Parameter(description = "区域ID") @PathVariable Long id,
            @RequestBody Area area) {
        return Result.success(areaService.updateArea(id, area));
    }

    @Operation(summary = "删除区域")
    @DeleteMapping("/{id}")
    public Result<Void> deleteArea(@Parameter(description = "区域ID") @PathVariable Long id) {
        areaService.deleteArea(id);
        return Result.success();
    }
}
