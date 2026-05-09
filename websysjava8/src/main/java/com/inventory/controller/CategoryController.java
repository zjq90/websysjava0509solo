package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.Category;
import com.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品类管理Controller
 * 处理种子品类的增删改查
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 品类列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "category/list";
    }

    /**
     * 新增品类页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    /**
     * 编辑品类页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/form";
    }

    /**
     * 保存品类（新增或更新）
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<Category> save(@RequestBody Category category) {
        try {
            // 检查名称重复
            if (category.getId() == null && categoryService.existsByName(category.getName())) {
                return Result.error("品类名称已存在");
            }
            if (category.getCode() != null && !category.getCode().isEmpty()) {
                if (category.getId() == null && categoryService.existsByCode(category.getCode())) {
                    return Result.error("品类编码已存在");
                }
            }
            Category saved = categoryService.save(category);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除品类
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有品类（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<List<Category>> getList() {
        List<Category> categories = categoryService.findAll();
        return Result.success(categories);
    }

    /**
     * 根据ID获取品类详情（API）
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<Category> getById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return Result.error("品类不存在");
        }
        return Result.success(category);
    }

    /**
     * 搜索品类（API）
     */
    @GetMapping("/api/search")
    @ResponseBody
    public Result<List<Category>> search(@RequestParam String keyword) {
        List<Category> categories = categoryService.searchByName(keyword);
        return Result.success(categories);
    }
}
