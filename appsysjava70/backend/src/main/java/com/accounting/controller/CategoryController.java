package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.entity.Category;
import com.accounting.enums.BillType;
import com.accounting.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "收支分类的增删改查接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取全部分类", description = "获取所有分类，按排序号排列")
    public ApiResponse<List<Category>> getAllCategories() {
        log.info("API: 获取全部分类");
        return ApiResponse.success(categoryService.getAllCategories());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "按类型获取分类", description = "根据账单类型（支出/收入）获取分类列表")
    public ApiResponse<List<Category>> getCategoriesByType(
            @Parameter(description = "账单类型: EXPENSE-支出, INCOME-收入", required = true)
            @PathVariable BillType type) {
        log.info("API: 按类型获取分类: {}", type);
        return ApiResponse.success(categoryService.getCategoriesByType(type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取分类", description = "根据分类ID获取分类详情")
    public ApiResponse<Category> getCategoryById(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id) {
        log.info("API: 根据ID获取分类: {}", id);
        return ApiResponse.success(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "创建新的收支分类")
    public ApiResponse<Category> createCategory(
            @RequestBody Category category) {
        log.info("API: 创建分类: {}", category.getName());
        return ApiResponse.success("分类创建成功", categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新分类信息")
    public ApiResponse<Category> updateCategory(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id,
            @RequestBody Category category) {
        log.info("API: 更新分类: {}", id);
        return ApiResponse.success("分类更新成功", categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定分类")
    public ApiResponse<Void> deleteCategory(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id) {
        log.info("API: 删除分类: {}", id);
        categoryService.deleteCategory(id);
        return ApiResponse.success("分类删除成功", null);
    }
}
