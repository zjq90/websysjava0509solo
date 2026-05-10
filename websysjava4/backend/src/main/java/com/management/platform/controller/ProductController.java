package com.management.platform.controller;

import com.management.platform.entity.Product;
import com.management.platform.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 * 提供商品CRUD和统计分析的REST API
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "商品管理", description = "商品的增删改查及统计分析接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 创建新商品
     * @param product 商品信息
     * @return 创建后的商品
     */
    @PostMapping
    @Operation(summary = "创建商品", description = "创建一个新的商品")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品详细信息")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    /**
     * 分页获取商品列表
     * @param page 页码，从0开始
     * @param size 每页大小
     * @return 商品分页列表
     */
    @GetMapping
    @Operation(summary = "获取商品列表", description = "分页获取所有商品列表")
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    /**
     * 更新商品信息
     * @param id 商品ID
     * @param product 更新的商品信息
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "更新指定商品的信息")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "删除指定商品")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 获取热销商品排行
     * @param limit 返回数量限制
     * @return 热销商品列表
     */
    @GetMapping("/stats/hot-selling")
    @Operation(summary = "热销商品排行", description = "获取销售数量最高的商品排行")
    public ResponseEntity<List<Map<String, Object>>> getHotSellingProducts(
            @Parameter(description = "返回数量限制") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(productService.getHotSellingProducts(limit));
    }

    /**
     * 获取滞销商品分析
     * @param limit 返回数量限制
     * @return 滞销商品列表
     */
    @GetMapping("/stats/unsold")
    @Operation(summary = "滞销商品分析", description = "获取滞销商品分析数据")
    public ResponseEntity<List<Map<String, Object>>> getUnsoldProducts(
            @Parameter(description = "返回数量限制") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(productService.getUnsoldProducts(limit));
    }

    /**
     * 获取毛利率分析
     * @return 毛利率统计数据
     */
    @GetMapping("/stats/gross-margin")
    @Operation(summary = "毛利率分析", description = "获取商品毛利率分析数据")
    public ResponseEntity<Map<String, Object>> getGrossMarginAnalysis() {
        return ResponseEntity.ok(productService.getGrossMarginAnalysis());
    }
}
