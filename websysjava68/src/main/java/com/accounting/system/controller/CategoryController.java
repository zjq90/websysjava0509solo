package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.CategoryDTO;
import com.accounting.system.entity.Category;
import com.accounting.system.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 分类管理Controller
 * 提供收入/支出分类管理API
 * 收入分类：工资、奖金、投资收益、兼职等
 * 支出分类：餐饮、交通、购物、娱乐、房贷/租金等
 */
@Api(tags = "分类管理")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询所有分类列表")
    @GetMapping
    public Result<List<Category>> listAll() {
        return Result.success(categoryService.listAll());
    }

    @ApiOperation("根据类型查询分类列表")
    @GetMapping("/type/{categoryType}")
    public Result<List<Category>> listByType(
            @ApiParam(value = "分类类型：INCOME-收入，EXPENSE-支出", required = true)
            @PathVariable String categoryType) {
        return Result.success(categoryService.listByType(categoryType));
    }

    @ApiOperation("根据ID获取分类详情")
    @GetMapping("/{id}")
    public Result<Category> getDetailById(
            @ApiParam(value = "分类ID", required = true)
            @PathVariable Long id) {
        return Result.success(categoryService.getDetailById(id));
    }

    @ApiOperation("新增分类")
    @PostMapping
    public Result<Category> addCategory(
            @ApiParam(value = "分类信息", required = true)
            @Valid @RequestBody CategoryDTO dto) {
        return Result.success("新增分类成功", categoryService.addCategory(dto));
    }

    @ApiOperation("更新分类")
    @PutMapping
    public Result<Category> updateCategory(
            @ApiParam(value = "分类信息", required = true)
            @Valid @RequestBody CategoryDTO dto) {
        return Result.success("更新分类成功", categoryService.updateCategory(dto));
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @ApiParam(value = "分类ID", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除分类成功", null);
    }
}
