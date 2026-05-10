package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.Category;
import com.vending.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理控制器
 * 提供商品分类的增删改查接口
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
@Tag(name = "分类管理", description = "商品分类管理接口")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    @Operation(summary = "获取所有分类")
    public ApiResponse<List<Category>> getAll() {
        return ApiResponse.success(categoryService.findAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取分类")
    public ApiResponse<Category> getById(@PathVariable Long id) {
        return categoryService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("分类不存在", 404));
    }
    
    @PostMapping
    @Operation(summary = "创建分类")
    public ApiResponse<Category> create(@RequestBody Category category) {
        try {
            return ApiResponse.success("创建成功", categoryService.save(category));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新分类")
    public ApiResponse<Category> update(@PathVariable Long id, @RequestBody Category category) {
        try {
            return ApiResponse.success("更新成功", categoryService.update(id, category));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            categoryService.deleteById(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
