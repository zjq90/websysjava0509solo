package com.hospital.controller;

import com.hospital.common.PageResult;
import com.hospital.common.Result;
import com.hospital.entity.AuditLog;
import com.hospital.repository.AuditLogRepository;
import com.hospital.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "审计日志", description = "操作日志查询相关接口")
@RestController
@RequestMapping("/audit-log")
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取当前用户操作日志")
    @GetMapping("/my")
    public Result<PageResult<AuditLog>> getMyLogs(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").descending());
        Page<AuditLog> page = auditLogRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);

        PageResult<AuditLog> pageResult = PageResult.of(
                page.getContent(),
                page.getTotalElements(),
                pageNum,
                pageSize
        );
        return Result.success(pageResult);
    }

    @Operation(summary = "获取所有操作日志（管理员）")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<AuditLog>> getAllLogs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").descending());
        Page<AuditLog> page = auditLogRepository.findByOrderByCreateTimeDesc(pageable);

        PageResult<AuditLog> pageResult = PageResult.of(
                page.getContent(),
                page.getTotalElements(),
                pageNum,
                pageSize
        );
        return Result.success(pageResult);
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }
}
