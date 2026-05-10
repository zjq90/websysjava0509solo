package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Menu;
import com.websys.entity.Permission;
import com.websys.entity.User;
import com.websys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户管理控制器
 * 提供用户管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    public Result<Page<User>> getPage(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<User> userPage = userService.findPage(username, realName, status, pageRequest);
        return Result.success(userPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详情")
    public Result<User> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(Result::success).orElseGet(() -> Result.error("用户不存在"));
    }

    @PostMapping
    @Operation(summary = "新增用户", description = "新增用户信息")
    public Result<User> save(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername(), null)) {
            return Result.error("用户名已存在");
        }
        User saved = userService.save(user);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<User> update(@RequestBody User user) {
        if (user.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        if (userService.existsByUsername(user.getUsername(), user.getId())) {
            return Result.error("用户名已存在");
        }
        User updated = userService.save(user);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "更新用户状态", description = "更新用户启用/禁用状态")
    public Result<User> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态：1启用，0禁用") @PathVariable Integer status) {
        User user = userService.updateStatus(id, status);
        return Result.success("状态更新成功", user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户详情")
    public Result<User> getByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(Result::success).orElseGet(() -> Result.error("用户不存在"));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录验证")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return Result.error("用户不存在");
        }
        
        User user = userOpt.get();
        if (!password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        
        if (user.getStatus() != 1) {
            return Result.error("用户已被禁用");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("token", "mock-token-" + System.currentTimeMillis());
        
        return Result.success("登录成功", result);
    }

    @GetMapping("/{id}/menus")
    @Operation(summary = "获取用户菜单", description = "根据用户ID获取用户有权限访问的菜单列表")
    public Result<List<Menu>> getUserMenus(@Parameter(description = "用户ID") @PathVariable Long id) {
        List<Menu> menus = userService.getUserMenus(id);
        return Result.success(menus);
    }

    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取用户权限", description = "根据用户ID获取用户拥有的操作权限列表")
    public Result<List<Permission>> getUserPermissions(@Parameter(description = "用户ID") @PathVariable Long id) {
        List<Permission> permissions = userService.getUserPermissions(id);
        return Result.success(permissions);
    }

    @GetMapping("/{id}/has-permission/{permissionCode}")
    @Operation(summary = "检查用户权限", description = "检查用户是否拥有指定权限编码的权限")
    public Result<Boolean> hasPermission(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "权限编码") @PathVariable String permissionCode) {
        boolean hasPermission = userService.hasPermission(id, permissionCode);
        return Result.success(hasPermission);
    }
}
