package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.AddOnItem;
import com.photostudio.repository.AddOnItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 加购项控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/addons")
@Tag(name = "加购项管理", description = "加购项的增删改查")
public class AddOnItemController {

    @Autowired
    private AddOnItemRepository addOnItemRepository;

    @GetMapping
    @Operation(summary = "查询所有加购项")
    public Result<List<AddOnItem>> findAll() {
        return Result.success(addOnItemRepository.findAll());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据类别查询")
    public Result<List<AddOnItem>> findByCategory(@PathVariable String category) {
        return Result.success(addOnItemRepository.findByCategory(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public Result<AddOnItem> findById(@PathVariable Long id) {
        return addOnItemRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("加购项不存在"));
    }

    @PostMapping
    @Operation(summary = "新增加购项")
    public Result<AddOnItem> add(@RequestBody AddOnItem item) {
        AddOnItem saved = addOnItemRepository.save(item);
        return Result.success("加购项创建成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新加购项")
    public Result<AddOnItem> update(@RequestBody AddOnItem item) {
        if (!addOnItemRepository.existsById(item.getId())) {
            return Result.error("加购项不存在");
        }
        AddOnItem saved = addOnItemRepository.save(item);
        return Result.success("加购项更新成功", saved);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除加购项")
    public Result<Void> delete(@PathVariable Long id) {
        if (!addOnItemRepository.existsById(id)) {
            return Result.error("加购项不存在");
        }
        addOnItemRepository.deleteById(id);
        return Result.success("加购项删除成功", null);
    }
}
