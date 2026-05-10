package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Role;
import com.websys.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 角色管理控制器
 * 提供角色管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @Operation(summary = "查询所有角色", description = "查询所有角色列表")
    public Result<List<Role>> getAll() {
        List<Role> roles = roleService.findAll();
        return Result.success(roles);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "根据条件分页查询角色列表")
    public Result<Page<Role>> getPage(
            @Parameter(description = "角色名称") @RequestParam(required = false) String name,
            @Parameter(description = "角色编码") @RequestParam(required = false) String code,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Role> rolePage = roleService.findPage(name, code, status, pageRequest);
        return Result.success(rolePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详情")
    public Result<Role> getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(Result::success).orElseGet(() -> Result.error("角色不存在"));
    }

    @PostMapping
    @Operation(summary = "新增角色", description = "新增角色信息")
    public Result<Role> save(@RequestBody Role role) {
        if (roleService.existsByCode(role.getCode())) {
            return Result.error("角色编码已存在");
        }
        Role saved = roleService.save(role);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色信息")
    public Result<Role> update(@RequestBody Role role) {
        if (role.getId() == null) {
            return Result.error("角色ID不能为空");
        }
        Role updated = roleService.save(role);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据ID删除角色")
    public Result<Void> delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        roleService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{id}/menus")
    @Operation(summary = "角色分配菜单", description = "为角色分配菜单权限")
    public Result<Role> assignMenus(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody Map<String, List<Long>> params) {
        List<Long> menuIds = params.get("menuIds");
        if (menuIds == null) {
            return Result.error("菜单ID列表不能为空");
        }
        Role role = roleService.assignMenus(id, menuIds);
        return Result.success("菜单分配成功", role);
    }

    @PostMapping("/{id}/permissions")
    @Operation(summary = "角色分配权限", description = "为角色分配操作权限")
    public Result<Role> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody Map<String, List<Long>> params) {
        List<Long> permissionIds = params.get("permissionIds");
        if (permissionIds == null) {
            return Result.error("权限ID列表不能为空");
        }
        Role role = roleService.assignPermissions(id, permissionIds);
        return Result.success("权限分配成功", role);
    }

    @GetMapping("/{id}/menus")
    @Operation(summary = "获取角色菜单", description = "获取角色已分配的菜单ID列表")
    public Result<List<Long>> getRoleMenus(@Parameter(description = "角色ID") @PathVariable Long id) {
        List<Long> menuIds = roleService.getRoleMenuIds(id);
        return Result.success(menuIds);
    }

    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取角色权限", description = "获取角色已分配的权限ID列表")
    public Result<List<Long>> getRolePermissions(@Parameter(description = "角色ID") @PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissionIds(id);
        return Result.success(permissionIds);
    }
}
