package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.AuditRule;
import com.culturalrelic.service.AuditRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 审核规则管理控制器
 */
@RestController
@RequestMapping("/audit-rule")
@Tag(name = "审核规则管理", description = "审核规则的增删改查维护")
public class AuditRuleController {

    @Autowired
    private AuditRuleService auditRuleService;

    @PostMapping
    @Operation(summary = "新增规则", description = "创建新的审核规则")
    public Result<AuditRule> save(@RequestBody AuditRule rule) {
        AuditRule saved = auditRuleService.save(rule);
        return Result.success("新增成功", saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询规则详情", description = "根据ID查询规则详细信息")
    public Result<AuditRule> findById(@Parameter(description = "规则ID") @PathVariable Long id) {
        Optional<AuditRule> rule = auditRuleService.findById(id);
        if (rule.isPresent()) {
            return Result.success(rule.get());
        }
        return Result.error("规则不存在");
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询规则", description = "分页查询所有规则列表")
    public Result<Page<AuditRule>> findAll(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<AuditRule> result = auditRuleService.findAll(pageable);
        return Result.success(result);
    }

    @GetMapping
    @Operation(summary = "查询所有规则", description = "获取所有规则列表")
    public Result<List<AuditRule>> findAll() {
        List<AuditRule> list = auditRuleService.findAll();
        return Result.success(list);
    }

    @PutMapping
    @Operation(summary = "更新规则", description = "更新规则信息")
    public Result<AuditRule> update(@RequestBody AuditRule rule) {
        AuditRule updated = auditRuleService.update(rule);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除规则", description = "根据ID删除规则（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "规则ID") @PathVariable Long id) {
        boolean success = auditRuleService.delete(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
