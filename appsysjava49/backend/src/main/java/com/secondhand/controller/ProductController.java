package com.secondhand.controller;

import com.secondhand.common.PageResult;
import com.secondhand.common.Result;
import com.secondhand.entity.Product;
import com.secondhand.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "商品管理", description = "商品相关接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "发布商品")
    public Result<Product> create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情")
    public Result<Product> getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索商品")
    public Result<PageResult<Product>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return productService.search(keyword, categoryId, minPrice, maxPrice,
                condition, brand, sortBy, pageNum, pageSize);
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的商品")
    public Result<List<Product>> getMyProducts(@RequestParam Long userId) {
        return productService.getMyProducts(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        return productService.delete(id);
    }

    @GetMapping("/suggestions")
    @Operation(summary = "获取搜索建议")
    public Result<List<String>> getSuggestions(@RequestParam String keyword) {
        return productService.getSuggestions(keyword);
    }
}
