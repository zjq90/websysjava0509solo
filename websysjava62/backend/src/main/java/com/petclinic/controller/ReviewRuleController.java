package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.dto.ReviewResult;
import com.petclinic.entity.ReviewRule;
import com.petclinic.service.ReviewRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 审核规则Controller
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/review-rules")
@Tag(name = "审核规则管理", description = "动态配置审核规则，如敏感词列表等")
public class ReviewRuleController {

    @Autowired
    private ReviewRuleService reviewRuleService;

    @GetMapping
    @Operation(summary = "查询所有审核规则")
    public Result<List<ReviewRule>> findAll() {
        return Result.success(reviewRuleService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询审核规则")
    public Result<ReviewRule> findById(@PathVariable Long id) {
        Optional<ReviewRule> rule = reviewRuleService.findById(id);
        return rule.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping
    @Operation(summary = "新增审核规则")
    public Result<ReviewRule> save(@RequestBody ReviewRule rule) {
        return Result.success(reviewRuleService.save(rule));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新审核规则")
    public Result<ReviewRule> update(@PathVariable Long id, @RequestBody ReviewRule rule) {
        ReviewRule updated = reviewRuleService.update(id, rule);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除审核规则")
    public Result<Void> deleteById(@PathVariable Long id) {
        reviewRuleService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/type/{ruleType}")
    @Operation(summary = "根据规则类型查询")
    public Result<List<ReviewRule>> findByRuleType(@PathVariable String ruleType) {
        return Result.success(reviewRuleService.findByRuleType(ruleType));
    }

    @GetMapping("/enabled/{enabled}")
    @Operation(summary = "查询启用/禁用的规则")
    public Result<List<ReviewRule>> findByEnabled(@PathVariable Boolean enabled) {
        return Result.success(reviewRuleService.findByEnabled(enabled));
    }

    @GetMapping("/search")
    @Operation(summary = "根据名称搜索规则")
    public Result<List<ReviewRule>> searchByName(@RequestParam String name) {
        return Result.success(reviewRuleService.searchByName(name));
    }

    @PostMapping("/init-default")
    @Operation(summary = "初始化默认敏感词规则")
    public Result<ReviewRule> initDefaultSensitiveWords() {
        return Result.success(reviewRuleService.initDefaultSensitiveWords());
    }

    @PostMapping("/{id}/add-word")
    @Operation(summary = "添加敏感词")
    public Result<ReviewRule> addSensitiveWord(@PathVariable Long id, @RequestParam String word) {
        ReviewRule updated = reviewRuleService.addSensitiveWord(id, word);
        return updated != null ? Result.success(updated) : Result.error("添加失败");
    }

    @GetMapping("/check")
    @Operation(summary = "检查文本是否包含敏感词")
    public Result<Boolean> containsSensitiveWord(@RequestParam String text) {
        return Result.success(reviewRuleService.containsSensitiveWord(text));
    }

    @PostMapping("/review")
    @Operation(summary = "完整内容审核，返回审核结果详情")
    public Result<ReviewResult> reviewContent(@RequestParam String content) {
        return Result.success(reviewRuleService.reviewContent(content));
    }

    @PostMapping("/review-block")
    @Operation(summary = "审核并阻止违规内容（中高级违规直接阻止）")
    public Result<String> reviewAndBlock(@RequestParam String content) {
        ReviewResult result = reviewRuleService.reviewAndBlock(content);
        if (result != null) {
            return Result.error("内容审核不通过：" + result.getMessage());
        }
        return Result.success("内容审核通过");
    }

    @GetMapping("/sensitive-words")
    @Operation(summary = "获取所有启用的敏感词列表")
    public Result<Set<String>> getAllEnabledSensitiveWords() {
        return Result.success(reviewRuleService.getAllEnabledSensitiveWords());
    }

    @PostMapping("/{id}/remove-word")
    @Operation(summary = "移除敏感词")
    public Result<ReviewRule> removeSensitiveWord(@PathVariable Long id, @RequestParam String word) {
        ReviewRule updated = reviewRuleService.removeSensitiveWord(id, word);
        return updated != null ? Result.success(updated) : Result.error("移除失败");
    }

    @PostMapping("/clear-cache")
    @Operation(summary = "清除敏感词缓存")
    public Result<Void> clearCache() {
        reviewRuleService.clearCache();
        return Result.success();
    }
}
