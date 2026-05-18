package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.Product;
import com.flowerstore.backend.entity.ProductCategory;
import com.flowerstore.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
@Tag(name = "产品接口", description = "产品查询、分类、搜索等")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取首页数据
     */
    @GetMapping("/home")
    @Operation(summary = "获取首页数据", description = "包含热门、新品、推荐产品和分类")
    public Result<Map<String, Object>> getHomeData() {
        return productService.getHomeData();
    }

    /**
     * 获取产品分类列表
     */
    @GetMapping("/categories")
    @Operation(summary = "获取产品分类", description = "scene-场景分类，material-花材分类，price-价格区间")
    public Result<List<ProductCategory>> getCategories(
            @RequestParam(required = false) String categoryType) {
        return productService.getCategories(categoryType);
    }

    /**
     * 根据分类ID获取产品列表
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取产品", description = "根据分类ID获取该分类下的产品")
    public Result<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * 搜索产品
     */
    @GetMapping("/search")
    @Operation(summary = "搜索产品", description = "根据关键词搜索产品")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    /**
     * 获取搜索建议
     */
    @GetMapping("/search-suggestions")
    @Operation(summary = "获取搜索建议", description = "根据输入关键词联想搜索建议")
    public Result<List<String>> getSearchSuggestions(@RequestParam String keyword) {
        return productService.getSearchSuggestions(keyword);
    }

    /**
     * 获取产品详情
     */
    @GetMapping("/{productId}")
    @Operation(summary = "获取产品详情", description = "包含产品信息、图片、评价等")
    public Result<Map<String, Object>> getProductDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }

    /**
     * 根据价格区间筛选产品
     */
    @GetMapping("/price-range")
    @Operation(summary = "按价格区间筛选", description = "根据价格区间筛选产品")
    public Result<List<Product>> getProductsByPriceRange(
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    /**
     * 获取所有产品
     */
    @GetMapping("/all")
    @Operation(summary = "获取所有产品", description = "获取所有上架的产品")
    public Result<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }
}
