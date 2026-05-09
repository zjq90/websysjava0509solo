package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.dto.UserDTO;
import com.appsys.production.entity.User;
import com.appsys.production.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户增删改查接口")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "查询所有用户", description = "获取系统所有用户列表")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID获取用户详情")
    public Result<User> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名获取用户详情")
    public Result<User> getByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        return Result.success(userService.getByUsername(username));
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户，手机号加密存储")
    public Result<User> create(@Valid @RequestBody UserDTO dto) {
        return Result.success(userService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<User> update(@Parameter(description = "用户ID") @PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return Result.success(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "逻辑删除用户（禁用）")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
