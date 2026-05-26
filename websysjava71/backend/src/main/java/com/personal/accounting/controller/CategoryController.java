package com.personal.accounting.controller;

import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.enums.CategoryType;
import com.personal.accounting.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * 提供分类管理API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "收支分类的增删改查API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有启用的分类列表")
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "按类型获取分类", description = "根据类型（收入/支出）获取分类列表")
    public ResponseEntity<List<Category>> findByType(@PathVariable CategoryType type) {
        return ResponseEntity.ok(categoryService.findByType(type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取分类", description = "根据分类ID获取详细信息")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "创建新的分类")
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.create(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "根据ID更新分类信息")
    public ResponseEntity<Category> update(
            @PathVariable Long id,
            @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "根据ID删除分类（逻辑删除）")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
