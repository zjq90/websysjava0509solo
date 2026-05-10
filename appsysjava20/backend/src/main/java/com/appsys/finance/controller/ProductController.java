package com.appsys.finance.controller;

import com.appsys.finance.entity.Product;
import com.appsys.finance.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "产品管理", description = "产品的增删改查接口，包含批次编号、保质期、发芽率等业务规则校验")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "创建产品", description = "创建新产品，包含批次编号唯一性、保质期、发芽率等校验")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product saved = productService.createProduct(product);
            response.put("success", true);
            response.put("message", "创建成功");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取产品详情", description = "根据ID获取产品信息")
    public ResponseEntity<Map<String, Object>> getProductById(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return productService.getProductById(id)
            .map(product -> {
                response.put("success", true);
                response.put("data", product);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "产品不存在");
                return ResponseEntity.notFound().build();
            });
    }

    @GetMapping("/batch/{batchNumber}")
    @Operation(summary = "根据批次编号查询产品", description = "根据唯一的批次编号查询产品")
    public ResponseEntity<Map<String, Object>> getProductByBatchNumber(
            @Parameter(description = "批次编号") @PathVariable String batchNumber) {
        Map<String, Object> response = new HashMap<>();
        return productService.getProductByBatchNumber(batchNumber)
            .map(product -> {
                response.put("success", true);
                response.put("data", product);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "产品不存在");
                return ResponseEntity.notFound().build();
            });
    }

    @GetMapping
    @Operation(summary = "获取产品列表", description = "获取所有产品列表")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getAllProducts();
        response.put("success", true);
        response.put("data", products);
        response.put("total", products.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新产品", description = "更新产品信息")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @RequestBody Product productDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product updated = productService.updateProduct(id, productDetails);
            response.put("success", true);
            response.put("message", "更新成功");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品", description = "根据ID删除产品")
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        productService.deleteProduct(id);
        response.put("success", true);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
