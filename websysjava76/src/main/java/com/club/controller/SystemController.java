package com.club.controller;

import com.club.common.Result;
import com.club.dto.PageQuery;
import com.club.entity.MessageTemplate;
import com.club.entity.DataBackup;
import com.club.entity.User;
import com.club.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统设置控制器
 * 提供权限管理、消息模板、数据备份等API接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system")
@Tag(name = "系统设置", description = "权限管理、消息模板、数据备份相关接口")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    // ==================== 用户/权限管理 ====================

    @GetMapping("/user/list")
    @Operation(summary = "分页查询用户列表")
    public Result<Page<User>> getUserList(PageQuery query) {
        return Result.success(systemService.getUserList(query));
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "根据ID查询用户详情")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(systemService.getUserById(id));
    }

    @PostMapping("/user")
    @Operation(summary = "新增用户")
    public Result<User> createUser(@RequestBody User user) {
        try {
            return Result.success(systemService.createUser(user));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    @Operation(summary = "更新用户信息")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            return Result.success(systemService.updateUser(id, user));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> deleteUser(@PathVariable Long id) {
        systemService.deleteUser(id);
        return Result.success();
    }

    @PostMapping("/user/{userId}/role")
    @Operation(summary = "分配用户角色")
    public Result<User> assignRole(
            @PathVariable Long userId,
            @Parameter(description = "角色") @RequestParam String role,
            @Parameter(description = "负责社团ID") @RequestParam(required = false) Long clubId) {
        try {
            return Result.success(systemService.assignRole(userId, role, clubId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 消息模板管理 ====================

    @GetMapping("/template/list")
    @Operation(summary = "分页查询消息模板列表")
    public Result<Page<MessageTemplate>> getMessageTemplateList(PageQuery query) {
        return Result.success(systemService.getMessageTemplateList(query));
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "根据ID查询消息模板详情")
    public Result<MessageTemplate> getMessageTemplateById(@PathVariable Long id) {
        return Result.success(systemService.getMessageTemplateById(id));
    }

    @PostMapping("/template")
    @Operation(summary = "新增消息模板")
    public Result<MessageTemplate> createMessageTemplate(@RequestBody MessageTemplate template) {
        try {
            return Result.success(systemService.createMessageTemplate(template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/template/{id}")
    @Operation(summary = "更新消息模板")
    public Result<MessageTemplate> updateMessageTemplate(@PathVariable Long id, @RequestBody MessageTemplate template) {
        try {
            return Result.success(systemService.updateMessageTemplate(id, template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除消息模板")
    public Result<Void> deleteMessageTemplate(@PathVariable Long id) {
        systemService.deleteMessageTemplate(id);
        return Result.success();
    }

    // ==================== 数据备份管理 ====================

    @GetMapping("/backup/list")
    @Operation(summary = "分页查询备份记录列表")
    public Result<Page<DataBackup>> getDataBackupList(PageQuery query) {
        return Result.success(systemService.getDataBackupList(query));
    }

    @PostMapping("/backup/create")
    @Operation(summary = "创建数据备份")
    public Result<DataBackup> createBackup(
            @Parameter(description = "备份名称") @RequestParam String name,
            @Parameter(description = "备份描述") @RequestParam(required = false) String description,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName) {
        return Result.success(systemService.createBackup(name, description, operatorId, operatorName));
    }

    @PostMapping("/backup/{id}/restore")
    @Operation(summary = "恢复数据备份")
    public Result<Boolean> restoreBackup(@PathVariable Long id) {
        return Result.success(systemService.restoreBackup(id));
    }

    @DeleteMapping("/backup/{id}")
    @Operation(summary = "删除备份记录")
    public Result<Void> deleteBackup(@PathVariable Long id) {
        systemService.deleteBackup(id);
        return Result.success();
    }

    @GetMapping("/overview")
    @Operation(summary = "获取系统概览统计")
    public Result<Map<String, Object>> getSystemOverview() {
        return Result.success(systemService.getSystemOverview());
    }
}
