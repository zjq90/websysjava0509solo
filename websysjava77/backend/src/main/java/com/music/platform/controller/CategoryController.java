package com.music.platform.controller;

import com.music.platform.common.Result;
import com.music.platform.entity.Category;
import com.music.platform.entity.Music;
import com.music.platform.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> findAll() {
        return Result.success(categoryService.findAll());
    }

    @GetMapping("/type/{type}")
    public Result<List<Category>> findByType(@PathVariable String type) {
        return Result.success(categoryService.findByType(type));
    }

    @PostMapping
    public Result<Category> create(@RequestBody Map<String, String> body) {
        Category category = categoryService.create(
                body.get("name"),
                body.get("type"),
                body.get("icon")
        );
        return Result.success(category);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/music")
    public Result<List<Music>> getMusic(@PathVariable Long id) {
        return Result.success(categoryService.getMusicByCategory(id));
    }
}
