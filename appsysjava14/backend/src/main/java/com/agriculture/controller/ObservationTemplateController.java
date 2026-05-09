package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.ObservationTemplate;
import com.agriculture.service.ObservationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 观测模板控制器
 * 支持模板预设（如玉米、小麦标准观测项），减少重复输入
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "观测模板管理", description = "观测模板的增删改查，支持系统预设和用户自定义")
@RestController
@RequestMapping("/templates")
@CrossOrigin(origins = "*")
public class ObservationTemplateController {

    @Autowired
    private ObservationTemplateService templateService;

    /**
     * 查询所有模板
     */
    @Operation(summary = "查询所有模板", description = "获取系统中所有观测模板")
    @GetMapping
    public Result<List<ObservationTemplate>> list() {
        return Result.success(templateService.findAll());
    }

    /**
     * 查询系统预设模板
     */
    @Operation(summary = "查询系统模板", description = "获取所有系统预设的观测模板")
    @GetMapping("/system")
    public Result<List<ObservationTemplate>> listSystem() {
        return Result.success(templateService.findSystemTemplates());
    }

    /**
     * 根据ID获取模板详情
     */
    @Operation(summary = "获取模板详情", description = "根据ID获取观测模板详细信息")
    @GetMapping("/{id}")
    public Result<ObservationTemplate> getById(@Parameter(description = "模板ID") @PathVariable Long id) {
        Optional<ObservationTemplate> templateOpt = templateService.findById(id);
        if (templateOpt.isPresent()) {
            return Result.success(templateOpt.get());
        } else {
            return Result.notFound("模板不存在");
        }
    }

    /**
     * 根据作物类型查询模板
     */
    @Operation(summary = "按作物类型查询模板", description = "获取某类作物的观测模板（系统模板优先）")
    @GetMapping("/crop/{cropType}")
    public Result<List<ObservationTemplate>> getByCropType(
            @Parameter(description = "作物类型：CORN/WHEAT/RICE等") @PathVariable String cropType) {
        return Result.success(templateService.findByCropType(cropType));
    }

    /**
     * 查询用户自定义模板
     */
    @Operation(summary = "查询用户模板", description = "获取某个用户创建的自定义模板")
    @GetMapping("/user/{userId}")
    public Result<List<ObservationTemplate>> getUserTemplates(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(templateService.findUserTemplates(userId));
    }

    /**
     * 创建自定义模板
     */
    @Operation(summary = "创建模板", description = "创建用户自定义观测模板")
    @PostMapping
    public Result<ObservationTemplate> create(@RequestBody ObservationTemplate template) {
        try {
            ObservationTemplate created = templateService.create(template);
            return Result.success("创建成功", created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新模板
     */
    @Operation(summary = "更新模板", description = "修改自定义模板（系统模板不可修改）")
    @PutMapping
    public Result<ObservationTemplate> update(@RequestBody ObservationTemplate template) {
        try {
            ObservationTemplate updated = templateService.update(template);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除模板
     */
    @Operation(summary = "删除模板", description = "删除自定义模板（系统模板不可删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "模板ID") @PathVariable Long id) {
        try {
            templateService.deleteById(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
