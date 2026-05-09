package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.Plot;
import com.agriculture.service.PlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 地块管理控制器
 * 处理地块的增删改查操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "地块管理", description = "试验地块的增删改查操作")
@RestController
@RequestMapping("/plots")
@CrossOrigin(origins = "*")
public class PlotController {

    @Autowired
    private PlotService plotService;

    /**
     * 查询所有地块
     */
    @Operation(summary = "查询所有地块", description = "获取系统中所有试验地块列表")
    @GetMapping
    public Result<List<Plot>> list() {
        return Result.success(plotService.findAll());
    }

    /**
     * 查询启用状态的地块
     */
    @Operation(summary = "查询启用的地块", description = "获取所有状态为启用的地块")
    @GetMapping("/active")
    public Result<List<Plot>> listActive() {
        return Result.success(plotService.findActivePlots());
    }

    /**
     * 根据ID获取地块详情
     */
    @Operation(summary = "获取地块详情", description = "根据ID获取地块详细信息")
    @GetMapping("/{id}")
    public Result<Plot> getById(@Parameter(description = "地块ID") @PathVariable Long id) {
        Optional<Plot> plotOpt = plotService.findById(id);
        if (plotOpt.isPresent()) {
            return Result.success(plotOpt.get());
        } else {
            return Result.notFound("地块不存在");
        }
    }

    /**
     * 根据地块编号查询
     */
    @Operation(summary = "根据编号查询地块", description = "通过地块编号查询地块信息")
    @GetMapping("/code/{code}")
    public Result<Plot> getByCode(@Parameter(description = "地块编号") @PathVariable String code) {
        Optional<Plot> plotOpt = plotService.findByPlotCode(code);
        if (plotOpt.isPresent()) {
            return Result.success(plotOpt.get());
        } else {
            return Result.notFound("地块不存在");
        }
    }

    /**
     * 根据城市查询地块
     */
    @Operation(summary = "根据城市查询地块", description = "根据城市名称筛选地块")
    @GetMapping("/city/{city}")
    public Result<List<Plot>> getByCity(@Parameter(description = "城市名称") @PathVariable String city) {
        return Result.success(plotService.findByCity(city));
    }

    /**
     * 创建地块
     */
    @Operation(summary = "创建地块", description = "新增试验地块信息")
    @PostMapping
    public Result<Plot> create(@RequestBody Plot plot) {
        try {
            Plot created = plotService.create(plot);
            return Result.success("创建成功", created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新地块
     */
    @Operation(summary = "更新地块", description = "修改已有的地块信息")
    @PutMapping
    public Result<Plot> update(@RequestBody Plot plot) {
        try {
            Plot updated = plotService.update(plot);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除地块
     */
    @Operation(summary = "删除地块", description = "根据ID删除地块")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "地块ID") @PathVariable Long id) {
        plotService.deleteById(id);
        return Result.success();
    }
}
