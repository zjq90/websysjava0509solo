package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysPermission;
import com.hospital.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统权限控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/permission")
@Tag(name = "权限管理", description = "系统权限管理相关接口")
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 查询所有权限
     */
    @GetMapping("/list")
    @Operation(summary = "查询权限列表", description = "查询系统所有权限列表")
    public Result<List<SysPermission>> list() {
        List<SysPermission> permissions = permissionService.findAll();
        return Result.success(permissions);
    }

    /**
     * 查询权限树
     */
    @GetMapping("/tree")
    @Operation(summary = "查询权限树", description = "查询系统权限树结构")
    public Result<List<Map<String, Object>>> tree() {
        List<Map<String, Object>> permissionTree = permissionService.buildPermissionTree();
        return Result.success(permissionTree);
    }

    /**
     * 根据ID查询权限
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询权限详情", description = "根据权限ID查询权限详情")
    public Result<SysPermission> getById(@PathVariable Long id) {
        Optional<SysPermission> permissionOptional = permissionService.findById(id);
        if (permissionOptional.isPresent()) {
            return Result.success(permissionOptional.get());
        }
        return Result.fail("权限不存在");
    }

    /**
     * 新增权限
     */
    @PostMapping
    @Operation(summary = "新增权限", description = "新增系统权限")
    public Result<SysPermission> add(@RequestBody SysPermission permission) {
        if (permissionService.existsByPermissionCode(permission.getPermissionCode())) {
            return Result.fail("权限编码已存在");
        }
        SysPermission savedPermission = permissionService.save(permission);
        return Result.success("新增成功", savedPermission);
    }

    /**
     * 更新权限
     */
    @PutMapping
    @Operation(summary = "更新权限", description = "更新系统权限信息")
    public Result<SysPermission> update(@RequestBody SysPermission permission) {
        if (!permissionService.findById(permission.getId()).isPresent()) {
            return Result.fail("权限不存在");
        }
        if (permissionService.existsByPermissionCodeAndIdNot(permission.getPermissionCode(), permission.getId())) {
            return Result.fail("权限编码已存在");
        }
        SysPermission updatedPermission = permissionService.save(permission);
        return Result.success("更新成功", updatedPermission);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限", description = "根据权限ID删除权限")
    public Result<Void> delete(@PathVariable Long id) {
        if (!permissionService.findById(id).isPresent()) {
            return Result.fail("权限不存在");
        }
        permissionService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
