package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Package;
import com.photostudio.repository.PackageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 套餐控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/packages")
@Tag(name = "套餐管理", description = "套餐的增删改查")
public class PackageController {

    @Autowired
    private PackageRepository packageRepository;

    @GetMapping
    @Operation(summary = "查询所有套餐")
    public Result<List<Package>> findAll() {
        return Result.success(packageRepository.findAll());
    }

    @GetMapping("/available")
    @Operation(summary = "查询所有上架套餐")
    public Result<List<Package>> findAvailable() {
        return Result.success(packageRepository.findByStatusOrderBySortOrderAsc(1));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型查询套餐")
    public Result<List<Package>> findByType(@Parameter(description = "套餐类型") @PathVariable String type) {
        return Result.success(packageRepository.findByType(type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询套餐")
    public Result<Package> findById(@Parameter(description = "套餐ID") @PathVariable Long id) {
        Optional<Package> optional = packageRepository.findById(id);
        return optional.map(Result::success).orElse(Result.error("套餐不存在"));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索套餐")
    public Result<List<Package>> search(@Parameter(description = "关键词") @RequestParam String keyword) {
        return Result.success(packageRepository.findByNameContaining(keyword));
    }

    @PostMapping
    @Operation(summary = "新增套餐")
    public Result<Package> add(@RequestBody Package pkg) {
        Package saved = packageRepository.save(pkg);
        return Result.success("套餐创建成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新套餐")
    public Result<Package> update(@RequestBody Package pkg) {
        if (pkg.getId() == null) {
            return Result.error("套餐ID不能为空");
        }
        Package saved = packageRepository.save(pkg);
        return Result.success("套餐更新成功", saved);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除套餐")
    public Result<Void> delete(@Parameter(description = "套餐ID") @PathVariable Long id) {
        if (!packageRepository.existsById(id)) {
            return Result.error("套餐不存在");
        }
        packageRepository.deleteById(id);
        return Result.success("套餐删除成功", null);
    }
}
