package com.gamesys.controller;

import com.gamesys.common.Result;
import com.gamesys.entity.Category;
import com.gamesys.entity.Tag;
import com.gamesys.service.CategoryTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类标签管理")
@RestController
@RequestMapping("/category-tag")
public class CategoryTagController {

    @Autowired
    private CategoryTagService categoryTagService;

    @ApiOperation("获取所有分类")
    @GetMapping("/categories")
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryTagService.getAllCategories());
    }

    @ApiOperation("新增分类")
    @PostMapping("/categories")
    public Result<Void> saveCategory(@RequestBody Category category) {
        categoryTagService.saveCategory(category);
        return Result.success();
    }

    @ApiOperation("更新分类")
    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryTagService.updateCategory(category);
        return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryTagService.deleteCategory(id);
        return Result.success();
    }

    @ApiOperation("获取所有标签")
    @GetMapping("/tags")
    public Result<List<Tag>> getAllTags() {
        return Result.success(categoryTagService.getAllTags());
    }

    @ApiOperation("新增标签")
    @PostMapping("/tags")
    public Result<Void> saveTag(@RequestBody Tag tag) {
        categoryTagService.saveTag(tag);
        return Result.success();
    }

    @ApiOperation("更新标签")
    @PutMapping("/tags/{id}")
    public Result<Void> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        categoryTagService.updateTag(tag);
        return Result.success();
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/tags/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        categoryTagService.deleteTag(id);
        return Result.success();
    }

    @ApiOperation("合并标签")
    @PostMapping("/tags/merge")
    public Result<Void> mergeTags(
            @RequestParam Long targetId,
            @RequestParam List<Long> sourceIds) {
        categoryTagService.mergeTags(targetId, sourceIds);
        return Result.success();
    }

    @ApiOperation("清理低使用率标签")
    @PostMapping("/tags/clean")
    public Result<Void> cleanLowUsageTags(@RequestParam(defaultValue = "5") Integer minGameCount) {
        categoryTagService.cleanLowUsageTags(minGameCount);
        return Result.success();
    }
}
