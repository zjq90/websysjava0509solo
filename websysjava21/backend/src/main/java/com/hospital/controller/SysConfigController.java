package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysConfig;
import com.hospital.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/config")
@Tag(name = "配置管理", description = "系统配置管理相关接口")
public class SysConfigController {

    @Autowired
    private SysConfigService configService;

    /**
     * 分页查询配置列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询配置列表", description = "分页查询系统配置列表")
    public Result<Page<SysConfig>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysConfig> configs = configService.findAll(pageable);
        return Result.success(configs);
    }

    /**
     * 查询所有配置
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有配置", description = "查询系统所有配置")
    public Result<List<SysConfig>> getAll() {
        List<SysConfig> configs = configService.findAll();
        return Result.success(configs);
    }

    /**
     * 根据ID查询配置
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询配置详情", description = "根据配置ID查询配置详情")
    public Result<SysConfig> getById(@PathVariable Long id) {
        Optional<SysConfig> configOptional = configService.findById(id);
        if (configOptional.isPresent()) {
            return Result.success(configOptional.get());
        }
        return Result.fail("配置不存在");
    }

    /**
     * 根据配置键查询配置
     */
    @GetMapping("/key/{configKey}")
    @Operation(summary = "根据配置键查询", description = "根据配置键查询配置详情")
    public Result<SysConfig> getByKey(@PathVariable String configKey) {
        Optional<SysConfig> configOptional = configService.findByConfigKey(configKey);
        if (configOptional.isPresent()) {
            return Result.success(configOptional.get());
        }
        return Result.fail("配置不存在");
    }

    /**
     * 新增配置
     */
    @PostMapping
    @Operation(summary = "新增配置", description = "新增系统配置")
    public Result<SysConfig> add(@RequestBody SysConfig config) {
        if (configService.existsByConfigKey(config.getConfigKey())) {
            return Result.fail("配置键已存在");
        }
        SysConfig savedConfig = configService.save(config);
        return Result.success("新增成功", savedConfig);
    }

    /**
     * 更新配置
     */
    @PutMapping
    @Operation(summary = "更新配置", description = "更新系统配置信息")
    public Result<SysConfig> update(@RequestBody SysConfig config) {
        if (!configService.findById(config.getId()).isPresent()) {
            return Result.fail("配置不存在");
        }
        if (configService.existsByConfigKeyAndIdNot(config.getConfigKey(), config.getId())) {
            return Result.fail("配置键已存在");
        }
        SysConfig updatedConfig = configService.save(config);
        return Result.success("更新成功", updatedConfig);
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置", description = "根据配置ID删除配置")
    public Result<Void> delete(@PathVariable Long id) {
        if (!configService.findById(id).isPresent()) {
            return Result.fail("配置不存在");
        }
        configService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
