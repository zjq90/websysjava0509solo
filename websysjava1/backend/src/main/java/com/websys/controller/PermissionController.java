package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Permission;
import com.websys.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permission")
@Tag(name = "权限管理", description = "权限管理相关接口")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    @Operation(summary = "查询所有权限", description = "查询所有权限列表")
    public Result<List<Permission>> getAll() {
        List<Permission> permissions = permissionService.findAll();
        return Result.success(permissions);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询权限列表", description = "根据条件分页查询权限列表")
    public Result<Page<Permission>> getPage(
            @Parameter(description = "权限名称") @RequestParam(required = false) String name,
            @Parameter(description = "权限编码") @RequestParam(required = false) String code,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Permission> permissionPage = permissionService.findPage(name, code, status, pageRequest);
        return Result.success(permissionPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询权限", description = "根据权限ID查询权限详情")
    public Result<Permission> getById(@Parameter(description = "权限ID") @PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(Result::success).orElseGet(() -> Result.error("权限不存在"));
    }

    @PostMapping
    @Operation(summary = "新增权限", description = "新增权限信息")
    public Result<Permission> save(@RequestBody Permission permission) {
        if (permissionService.existsByCode(permission.getCode())) {
            return Result.error("权限编码已存在");
        }
        Permission saved = permissionService.save(permission);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新权限", description = "更新权限信息")
    public Result<Permission> update(@RequestBody Permission permission) {
        if (permission.getId() == null) {
            return Result.error("权限ID不能为空");
        }
        Permission updated = permissionService.save(permission);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限", description = "根据ID删除权限")
    public Result<Void> delete(@Parameter(description = "权限ID") @PathVariable Long id) {
        permissionService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
