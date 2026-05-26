package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.CategoryRuleDTO;
import com.accounting.system.entity.CategoryRule;
import com.accounting.system.service.CategoryRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 分类规则管理Controller
 * 提供自定义匹配规则管理API
 * 支持正则表达式精准识别复杂商家名称
 * 如"含'超市'关键词的支出归为'购物'"
 */
@Api(tags = "分类规则管理")
@RestController
@RequestMapping("/category-rules")
public class CategoryRuleController {

    @Autowired
    private CategoryRuleService categoryRuleService;

    @ApiOperation("查询所有规则列表")
    @GetMapping
    public Result<List<CategoryRule>> listAll() {
        return Result.success(categoryRuleService.listAll());
    }

    @ApiOperation("查询所有启用的规则（按优先级降序排列）")
    @GetMapping("/enabled")
    public Result<List<CategoryRule>> listEnabledRules() {
        return Result.success(categoryRuleService.listEnabledRules());
    }

    @ApiOperation("根据ID获取规则详情")
    @GetMapping("/{id}")
    public Result<CategoryRule> getDetailById(
            @ApiParam(value = "规则ID", required = true)
            @PathVariable Long id) {
        return Result.success(categoryRuleService.getDetailById(id));
    }

    @ApiOperation("新增规则")
    @PostMapping
    public Result<CategoryRule> addRule(
            @ApiParam(value = "规则信息", required = true)
            @Valid @RequestBody CategoryRuleDTO dto) {
        return Result.success("新增规则成功", categoryRuleService.addRule(dto));
    }

    @ApiOperation("更新规则")
    @PutMapping
    public Result<CategoryRule> updateRule(
            @ApiParam(value = "规则信息", required = true)
            @Valid @RequestBody CategoryRuleDTO dto) {
        return Result.success("更新规则成功", categoryRuleService.updateRule(dto));
    }

    @ApiOperation("删除规则")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRule(
            @ApiParam(value = "规则ID", required = true)
            @PathVariable Long id) {
        categoryRuleService.deleteRule(id);
        return Result.success("删除规则成功", null);
    }

    @ApiOperation("测试规则匹配")
    @PostMapping("/test")
    public Result<Boolean> testRule(
            @ApiParam(value = "规则信息（matchValue为要匹配的内容，remark为测试文本）", required = true)
            @RequestBody CategoryRuleDTO dto) {
        boolean matched = categoryRuleService.testRule(dto);
        if (matched) {
            return Result.success("匹配成功", true);
        }
        return Result.success("匹配失败", false);
    }
}
