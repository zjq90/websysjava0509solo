package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.entity.Package;
import com.broadband.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐控制器
 * 处理套餐管理相关接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/package")
@Tag(name = "套餐管理", description = "套餐查询、管理等接口")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Operation(summary = "获取所有套餐", description = "获取所有上架的套餐列表")
    @GetMapping
    public Result<List<Package>> getAllPackages() {
        try {
            List<Package> packages = packageService.getAllPackages();
            return Result.success(packages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "按类型获取套餐", description = "1-基础套餐 2-提速包 3-安全防护包")
    @GetMapping("/type/{type}")
    public Result<List<Package>> getPackagesByType(@PathVariable Integer type) {
        try {
            List<Package> packages = packageService.getPackagesByType(type);
            return Result.success(packages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取套餐详情", description = "根据套餐ID获取套餐详细信息")
    @GetMapping("/{id}")
    public Result<Package> getPackageById(@PathVariable Long id) {
        try {
            Package pkg = packageService.getPackageById(id);
            return Result.success(pkg);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取增值服务包", description = "根据父套餐ID获取关联的增值服务包")
    @GetMapping("/extra/{parentId}")
    public Result<List<Package>> getExtraPackages(@PathVariable Long parentId) {
        try {
            List<Package> packages = packageService.getExtraPackages(parentId);
            return Result.success(packages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
