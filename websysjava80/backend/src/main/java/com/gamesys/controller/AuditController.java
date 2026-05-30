package com.gamesys.controller;

import com.gamesys.common.PageResult;
import com.gamesys.common.Result;
import com.gamesys.dto.AuditDTO;
import com.gamesys.entity.AuditLog;
import com.gamesys.entity.AutoAuditRule;
import com.gamesys.service.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "审核管理")
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @ApiOperation("审核游戏")
    @PostMapping
    public Result<Void> audit(@Valid @RequestBody AuditDTO dto) {
        if (dto.getAuditResult() == 0 && (dto.getRejectReason() == null || dto.getRejectReason().trim().isEmpty())) {
            return Result.error("拒绝时必须填写拒绝原因");
        }
        auditService.audit(dto);
        return Result.success();
    }

    @ApiOperation("触发自动审核")
    @PostMapping("/auto/{gameId}")
    public Result<Void> autoAudit(@PathVariable Long gameId) {
        auditService.autoAudit(gameId);
        return Result.success();
    }

    @ApiOperation("获取审核日志")
    @GetMapping("/logs")
    public Result<PageResult<AuditLog>> getAuditLogs(
            @RequestParam(required = false) Long gameId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(auditService.getAuditLogs(gameId, pageNum, pageSize));
    }

    @ApiOperation("获取自动审核规则列表")
    @GetMapping("/rules")
    public Result<List<AutoAuditRule>> getAutoAuditRules() {
        return Result.success(auditService.getAutoAuditRules());
    }

    @ApiOperation("保存自动审核规则")
    @PostMapping("/rules")
    public Result<Void> saveAutoAuditRule(@RequestBody AutoAuditRule rule) {
        auditService.saveAutoAuditRule(rule);
        return Result.success();
    }

    @ApiOperation("删除自动审核规则")
    @DeleteMapping("/rules/{id}")
    public Result<Void> deleteAutoAuditRule(@PathVariable Long id) {
        auditService.deleteAutoAuditRule(id);
        return Result.success();
    }

    @ApiOperation("更新自动审核规则状态")
    @PutMapping("/rules/{id}/status")
    public Result<Void> updateAutoAuditRuleStatus(@PathVariable Long id, @RequestParam Integer status) {
        auditService.updateAutoAuditRuleStatus(id, status);
        return Result.success();
    }
}
