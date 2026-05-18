package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Category;
import com.secondhand.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@Tag(name = "分类管理", description = "商品分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/tree")
    @Operation(summary = "获取分类树形结构")
    public Result<List<Category>> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findByLevelAndStatus(1, 1);
        allCategories.addAll(categoryRepository.findByLevelAndStatus(2, 1));
        
        Map<Long, List<Category>> childrenMap = allCategories.stream()
            .filter(c -> c.getParentId() != null && c.getParentId() != 0)
            .collect(Collectors.groupingBy(Category::getParentId));
        
        List<Category> rootCategories = allCategories.stream()
            .filter(c -> c.getParentId() == null || c.getParentId() == 0)
            .sorted((a, b) -> a.getSort() - b.getSort())
            .collect(Collectors.toList());
        
        for (Category category : rootCategories) {
            List<Category> children = childrenMap.getOrDefault(category.getId(), new ArrayList<>());
            children.sort((a, b) -> a.getSort() - b.getSort());
            category.setChildren(children);
        }
        
        return Result.success(rootCategories);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索分类")
    public Result<List<Category>> search(@RequestParam String keyword) {
        List<Category> categories = categoryRepository.findByNameContainingAndStatus(keyword, 1);
        return Result.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情")
    public Result<Category> getById(@PathVariable Long id) {
        return categoryRepository.findById(id)
            .map(Result::success)
            .orElse(Result.error("分类不存在"));
    }
}
