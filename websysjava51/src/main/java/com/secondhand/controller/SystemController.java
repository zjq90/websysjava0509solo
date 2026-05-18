package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.OperationLog;
import com.secondhand.entity.Role;
import com.secondhand.entity.User;
import com.secondhand.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/system")
@Tag(name = "系统管理", description = "角色管理、用户管理、操作日志等接口")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/roles")
    @Operation(summary = "分页查询角色列表")
    public Result<Page<Role>> listRoles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(systemService.findAllRoles(name, code, pageable));
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "根据ID查询角色")
    public Result<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = systemService.findRoleById(id);
        return role.map(Result::success).orElse(Result.error("角色不存在"));
    }

    @PostMapping("/roles")
    @Operation(summary = "新增角色")
    public Result<Role> createRole(@RequestBody Role role) {
        return Result.success(systemService.saveRole(role));
    }

    @PutMapping("/roles/{id}")
    @Operation(summary = "更新角色")
    public Result<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        return Result.success(systemService.saveRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> deleteRole(@PathVariable Long id) {
        systemService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/users")
    @Operation(summary = "分页查询用户列表")
    public Result<Page<User>> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(systemService.findAllUsers(username, nickname, roleId, status, pageable));
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "根据ID查询用户")
    public Result<User> getUserById(@PathVariable Long id) {
        Optional<User> user = systemService.findUserById(id);
        return user.map(Result::success).orElse(Result.error("用户不存在"));
    }

    @PostMapping("/users")
    @Operation(summary = "新增用户")
    public Result<User> createUser(@RequestBody User user) {
        return Result.success(systemService.saveUser(user));
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "更新用户")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return Result.success(systemService.saveUser(user));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> deleteUser(@PathVariable Long id) {
        systemService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/operation-logs")
    @Operation(summary = "分页查询操作日志")
    public Result<Page<OperationLog>> listOperationLogs(
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return Result.success(systemService.findAllOperationLogs(operatorId, module, type, startTime, endTime, pageable));
    }

    @GetMapping("/operation-logs/{id}")
    @Operation(summary = "根据ID查询操作日志")
    public Result<OperationLog> getOperationLogById(@PathVariable Long id) {
        Optional<OperationLog> log = systemService.findOperationLogById(id);
        return log.map(Result::success).orElse(Result.error("操作日志不存在"));
    }

}