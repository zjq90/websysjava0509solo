package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysRole;
import com.hospital.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统角色控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理", description = "系统角色管理相关接口")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询角色列表", description = "分页查询系统角色列表")
    public Result<Page<SysRole>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysRole> roles = roleService.findAll(pageable);
        return Result.success(roles);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有角色", description = "查询系统所有角色")
    public Result<List<SysRole>> getAll() {
        List<SysRole> roles = roleService.findAll();
        return Result.success(roles);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情", description = "根据角色ID查询角色详情")
    public Result<SysRole> getById(@PathVariable Long id) {
        Optional<SysRole> roleOptional = roleService.findById(id);
        if (roleOptional.isPresent()) {
            return Result.success(roleOptional.get());
        }
        return Result.fail("角色不存在");
    }

    /**
     * 新增角色
     */
    @PostMapping
    @Operation(summary = "新增角色", description = "新增系统角色")
    public Result<SysRole> add(@RequestBody SysRole role) {
        if (roleService.existsByRoleCode(role.getRoleCode())) {
            return Result.fail("角色编码已存在");
        }
        SysRole savedRole = roleService.save(role);
        return Result.success("新增成功", savedRole);
    }

    /**
     * 更新角色
     */
    @PutMapping
    @Operation(summary = "更新角色", description = "更新系统角色信息")
    public Result<SysRole> update(@RequestBody SysRole role) {
        if (!roleService.findById(role.getId()).isPresent()) {
            return Result.fail("角色不存在");
        }
        if (roleService.existsByRoleCodeAndIdNot(role.getRoleCode(), role.getId())) {
            return Result.fail("角色编码已存在");
        }
        SysRole updatedRole = roleService.save(role);
        return Result.success("更新成功", updatedRole);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        if (!roleService.findById(id).isPresent()) {
            return Result.fail("角色不存在");
        }
        roleService.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 分配角色权限
     */
    @PostMapping("/{id}/permissions")
    @Operation(summary = "分配角色权限", description = "为角色分配权限")
    public Result<SysRole> assignPermissions(
            @PathVariable Long id,
            @RequestBody Map<String, List<Long>> request) {
        List<Long> permissionIds = request.get("permissionIds");
        SysRole role = roleService.assignPermissions(id, permissionIds);
        return Result.success("权限分配成功", role);
    }

    /**
     * 获取角色权限ID列表
     */
    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取角色权限", description = "获取角色的权限ID列表")
    public Result<List<Long>> getRolePermissions(@PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissionIds(id);
        return Result.success(permissionIds);
    }
}
