package com.appsys.controller;

import com.appsys.entity.Product;
import com.appsys.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品管理控制�? * 提供产品的增删改查API接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/products")
@Tag(name = "产品管理", description = "产品CRUD操作及相关功")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取所有产品列�?     */
    @GetMapping
    @Operation(summary = "获取所有产�?, description = "返回所有产品列表，按创建时间倒序排列")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return successResponse(products);
    }

    /**
     * 根据ID获取产品详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取产品详情", description = "根据产品ID获取产品详细信息")
    public ResponseEntity<Map<String, Object>> getProductById(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        Product product = productService.getProductById(id);
        return successResponse(product);
    }

    /**
     * 根据批次编号获取产品
     */
    @GetMapping("/batch/{batchNumber}")
    @Operation(summary = "根据批次编号获取产品", description = "根据8位批次编号获取产品信�?)
    public ResponseEntity<Map<String, Object>> getProductByBatchNumber(
            @Parameter(description = "批次编号") @PathVariable String batchNumber) {
        Product product = productService.getProductByBatchNumber(batchNumber);
        return successResponse(product);
    }

    /**
     * 创建新产�?     */
    @PostMapping
    @Operation(summary = "创建产品", description = "创建新的产品，批次编号必须唯一且符合格式要�?)
    public ResponseEntity<Map<String, Object>> createProduct(
            @Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return successResponse(createdProduct);
    }

    /**
     * 更新产品信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新产品", description = "根据ID更新产品信息")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return successResponse(updatedProduct);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品", description = "根据ID删除产品")
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return successResponse("产品删除成功");
    }

    /**
     * 搜索产品（按名称�?     */
    @GetMapping("/search")
    @Operation(summary = "搜索产品", description = "根据产品名称关键字模糊搜�?)
    public ResponseEntity<Map<String, Object>> searchProducts(
            @Parameter(description = "产品名称关键�?) @RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return successResponse(products);
    }

    /**
     * 获取可用产品（库�?0�?     */
    @GetMapping("/available")
    @Operation(summary = "获取可用产品", description = "获取库存大于0的产品列�?)
    public ResponseEntity<Map<String, Object>> getAvailableProducts() {
        List<Product> products = productService.getAvailableProducts();
        return successResponse(products);
    }

    /**
     * 根据分类获取产品
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "按分类获取产�?, description = "根据产品分类获取产品列表")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @Parameter(description = "产品分类") @PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return successResponse(products);
    }

    /**
     * 更新产品库存
     */
    @PutMapping("/{id}/stock")
    @Operation(summary = "更新库存", description = "更新产品库存数量，正数增加，负数减少")
    public ResponseEntity<Map<String, Object>> updateStock(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @Parameter(description = "库存变化�?) @RequestParam Integer quantity) {
        Product product = productService.updateStock(id, quantity);
        return successResponse(product);
    }

    /**
     * 构建成功响应
     */
    private ResponseEntity<Map<String, Object>> successResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}
