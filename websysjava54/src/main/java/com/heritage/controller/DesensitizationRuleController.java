package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.DesensitizationRule;
import com.heritage.service.DesensitizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desensitization")
@CrossOrigin(origins = "*")
@Tag(name = "脱敏规则管理", description = "脱敏规则相关接口")
public class DesensitizationRuleController {

    @Autowired
    private DesensitizationService desensitizationService;

    @GetMapping
    @Operation(summary = "获取所有脱敏规则")
    public Result<List<DesensitizationRule>> findAll() {
        return Result.success(desensitizationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取脱敏规则")
    public Result<DesensitizationRule> findById(@PathVariable Long id) {
        return desensitizationService.findById(id)
                .map(Result::success)
                .orElse(Result.error("脱敏规则不存在"));
    }

    @PostMapping
    @Operation(summary = "创建脱敏规则")
    public Result<DesensitizationRule> create(@RequestBody DesensitizationRule rule) {
        return Result.success(desensitizationService.save(rule));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新脱敏规则")
    public Result<DesensitizationRule> update(@PathVariable Long id, @RequestBody DesensitizationRule rule) {
        rule.setId(id);
        return Result.success(desensitizationService.save(rule));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除脱敏规则")
    public Result<Void> delete(@PathVariable Long id) {
        desensitizationService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/enabled")
    @Operation(summary = "获取所有启用的脱敏规则")
    public Result<List<DesensitizationRule>> getEnabledRules() {
        return Result.success(desensitizationService.findEnabledRules());
    }
}
