package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysLog;
import com.hospital.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 系统日志控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/log")
@Tag(name = "日志管理", description = "系统日志管理相关接口")
public class SysLogController {

    @Autowired
    private SysLogService logService;

    /**
     * 分页查询日志列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询日志列表", description = "分页查询系统日志列表")
    public Result<Page<SysLog>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysLog> logs = logService.findAll(pageable);
        return Result.success(logs);
    }

    /**
     * 根据ID查询日志
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询日志详情", description = "根据日志ID查询日志详情")
    public Result<SysLog> getById(@PathVariable Long id) {
        Optional<SysLog> logOptional = logService.findById(id);
        if (logOptional.isPresent()) {
            return Result.success(logOptional.get());
        }
        return Result.fail("日志不存在");
    }

    /**
     * 删除日志
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除日志", description = "根据日志ID删除日志")
    public Result<Void> delete(@PathVariable Long id) {
        if (!logService.findById(id).isPresent()) {
            return Result.fail("日志不存在");
        }
        logService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
