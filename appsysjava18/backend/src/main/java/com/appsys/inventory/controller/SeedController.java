package com.appsys.inventory.controller;

import com.appsys.common.result.Result;
import com.appsys.inventory.dto.SeedDTO;
import com.appsys.inventory.entity.Seed;
import com.appsys.inventory.service.SeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 种子管理控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "种子管理", description = "种子增删改查接口")
@RestController
@RequestMapping("/api/seed")
public class SeedController {

    @Autowired
    private SeedService seedService;

    /**
     * 查询所有种子列表
     */
    @Operation(summary = "查询种子列表", description = "查询所有种子信息")
    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<List<Seed>> list() {
        List<Seed> seeds = seedService.list();
        return Result.success(seeds);
    }

    /**
     * 根据ID查询种子详情
     */
    @Operation(summary = "查询种子详情", description = "根据ID查询种子详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Seed> getById(@Parameter(description = "种子ID") @PathVariable Long id) {
        Seed seed = seedService.getById(id);
        return Result.success(seed);
    }

    /**
     * 新增种子
     */
    @Operation(summary = "新增种子", description = "新增种子信息")
    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Seed> create(@Validated @RequestBody SeedDTO dto) {
        Seed seed = seedService.create(dto);
        return Result.success("种子新增成功", seed);
    }

    /**
     * 更新种子信息
     */
    @Operation(summary = "更新种子", description = "更新种子信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Seed> update(
            @Parameter(description = "种子ID") @PathVariable Long id,
            @Validated @RequestBody SeedDTO dto) {
        Seed seed = seedService.update(id, dto);
        return Result.success("种子更新成功", seed);
    }

    /**
     * 删除种子
     */
    @Operation(summary = "删除种子", description = "删除种子信息（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'MANAGER', 'ADMIN')")
    public Result<Void> delete(@Parameter(description = "种子ID") @PathVariable Long id) {
        seedService.delete(id);
        return Result.success("种子删除成功", null);
    }
}
