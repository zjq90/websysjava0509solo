package com.websys.controller;

import com.websys.common.PageResult;
import com.websys.common.Result;
import com.websys.entity.OperationLog;
import com.websys.entity.User;
import com.websys.service.AuthService;
import com.websys.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志控制器
 * 
 * @author websys
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/operation-logs")
@Tag(name = "操作日志", description = "操作日志相关接口")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private AuthService authService;

    /**
     * 分页查询操作日志（支持数据隔离）
     * 
     * @param username 操作用户名
     * @param module 操作模块
     * @param operationType 操作类型
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param current 当前页
     * @param size 每页大小
     * @param userDetails 用户详情（用于获取当前登录用户信息）
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询操作日志", description = "支持多条件筛选查询操作日志（支持数据隔离）")
    public Result<PageResult<OperationLog>> getLogPage(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        PageResult<OperationLog> result = operationLogService.getLogPageWithPermission(
            username, module, operationType, status, startTime, endTime, current, size,
            currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(result);
    }

    /**
     * 根据ID查询日志详情（支持数据隔离校验）
     * 
     * @param id 日志ID
     * @param userDetails 用户详情
     * @return 日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询日志详情", description = "根据ID获取操作日志详细信息（支持权限校验）")
    public Result<OperationLog> getLogById(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        OperationLog log = operationLogService.getLogByIdWithPermission(
            id, currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(log);
    }

    /**
     * 获取所有模块列表
     * 
     * @return 模块列表
     */
    @GetMapping("/modules")
    @Operation(summary = "获取模块列表", description = "获取所有可用的操作模块")
    public Result<List<String>> getAllModules() {
        List<String> modules = operationLogService.getAllModules();
        return Result.success(modules);
    }

    /**
     * 获取所有操作类型列表
     * 
     * @return 操作类型列表
     */
    @GetMapping("/operation-types")
    @Operation(summary = "获取操作类型列表", description = "获取所有可用的操作类型")
    public Result<List<String>> getAllOperationTypes() {
        List<String> types = operationLogService.getAllOperationTypes();
        return Result.success(types);
    }
}
