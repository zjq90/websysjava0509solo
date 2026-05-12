package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysUser;
import com.hospital.service.SysUserService;
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
 * 系统用户控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/user")
@Tag(name = "用户管理", description = "系统用户管理相关接口")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询用户列表", description = "分页查询系统用户列表")
    public Result<Page<SysUser>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysUser> users = userService.findAll(pageable);
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户ID查询用户详情")
    public Result<SysUser> getById(@PathVariable Long id) {
        Optional<SysUser> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            SysUser user = userOptional.get();
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.fail("用户不存在");
    }

    /**
     * 新增用户
     */
    @PostMapping
    @Operation(summary = "新增用户", description = "新增系统用户")
    public Result<SysUser> add(@RequestBody SysUser user) {
        if (userService.existsByUsername(user.getUsername())) {
            return Result.fail("用户名已存在");
        }
        SysUser savedUser = userService.save(user);
        savedUser.setPassword(null);
        return Result.success("新增成功", savedUser);
    }

    /**
     * 更新用户
     */
    @PutMapping
    @Operation(summary = "更新用户", description = "更新系统用户信息")
    public Result<SysUser> update(@RequestBody SysUser user) {
        if (!userService.findById(user.getId()).isPresent()) {
            return Result.fail("用户不存在");
        }
        if (userService.existsByUsernameAndIdNot(user.getUsername(), user.getId())) {
            return Result.fail("用户名已存在");
        }
        SysUser updatedUser = userService.save(user);
        updatedUser.setPassword(null);
        return Result.success("更新成功", updatedUser);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            return Result.fail("用户不存在");
        }
        userService.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 分配用户角色
     */
    @PostMapping("/{id}/roles")
    @Operation(summary = "分配用户角色", description = "为用户分配角色")
    public Result<SysUser> assignRoles(
            @PathVariable Long id,
            @RequestBody Map<String, List<Long>> request) {
        List<Long> roleIds = request.get("roleIds");
        SysUser user = userService.assignRoles(id, roleIds);
        user.setPassword(null);
        return Result.success("角色分配成功", user);
    }

    /**
     * 获取用户角色ID列表
     */
    @GetMapping("/{id}/roles")
    @Operation(summary = "获取用户角色", description = "获取用户的角色ID列表")
    public Result<List<Long>> getUserRoles(@PathVariable Long id) {
        List<Long> roleIds = userService.getUserRoleIds(id);
        return Result.success(roleIds);
    }
}
