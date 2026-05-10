package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Menu;
import com.websys.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 菜单管理控制器
 * 提供菜单管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/menu")
@Tag(name = "菜单管理", description = "菜单管理相关接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @Operation(summary = "查询所有菜单", description = "查询所有菜单列表")
    public Result<List<Menu>> getAll() {
        List<Menu> menus = menuService.findAll();
        return Result.success(menus);
    }

    @GetMapping("/root")
    @Operation(summary = "查询一级菜单", description = "查询所有一级菜单")
    public Result<List<Menu>> getRootMenus() {
        List<Menu> menus = menuService.findRootMenus();
        return Result.success(menus);
    }

    @GetMapping("/children/{parentId}")
    @Operation(summary = "查询子菜单", description = "根据父菜单ID查询子菜单")
    public Result<List<Menu>> getChildMenus(@Parameter(description = "父菜单ID") @PathVariable Long parentId) {
        List<Menu> menus = menuService.findChildMenus(parentId);
        return Result.success(menus);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询菜单", description = "根据菜单ID查询菜单详情")
    public Result<Menu> getById(@Parameter(description = "菜单ID") @PathVariable Long id) {
        Optional<Menu> menu = menuService.findById(id);
        return menu.map(Result::success).orElseGet(() -> Result.error("菜单不存在"));
    }

    @PostMapping
    @Operation(summary = "新增菜单", description = "新增菜单信息")
    public Result<Menu> save(@RequestBody Menu menu) {
        Menu saved = menuService.save(menu);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新菜单", description = "更新菜单信息")
    public Result<Menu> update(@RequestBody Menu menu) {
        if (menu.getId() == null) {
            return Result.error("菜单ID不能为空");
        }
        Menu updated = menuService.save(menu);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单", description = "根据ID删除菜单")
    public Result<Void> delete(@Parameter(description = "菜单ID") @PathVariable Long id) {
        menuService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型查询菜单", description = "根据菜单类型查询菜单列表")
    public Result<List<Menu>> getByType(@Parameter(description = "菜单类型：1目录，2菜单，3按钮") @PathVariable Integer type) {
        List<Menu> menus = menuService.findByType(type);
        return Result.success(menus);
    }
}
