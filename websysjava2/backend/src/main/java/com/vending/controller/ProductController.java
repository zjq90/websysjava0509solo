package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.Product;
import com.vending.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 * 提供商品的增删改查、批量操作等接口
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "商品管理", description = "商品管理接口")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    @Operation(summary = "获取所有商品")
    public ApiResponse<List<Product>> getAll() {
        return ApiResponse.success(productService.findAll());
    }
    
    @GetMapping("/active")
    @Operation(summary = "获取所有上架商品")
    public ApiResponse<List<Product>> getActive() {
        return ApiResponse.success(productService.findActiveProducts());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取商品")
    public ApiResponse<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("商品不存在", 404));
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索商品")
    public ApiResponse<List<Product>> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(productService.searchProducts(keyword));
    }
    
    @PostMapping
    @Operation(summary = "创建商品")
    public ApiResponse<Product> create(@RequestBody Product product, 
                                       @RequestParam(required = false) Long categoryId) {
        try {
            return ApiResponse.success("创建成功", productService.save(product, categoryId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新商品")
    public ApiResponse<Product> update(@PathVariable Long id, 
                                       @RequestBody Product product,
                                       @RequestParam(required = false) Long categoryId) {
        try {
            return ApiResponse.success("更新成功", productService.update(id, product, categoryId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/batch-status")
    @Operation(summary = "批量更新商品状态")
    public ApiResponse<Void> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Boolean active = (Boolean) request.get("active");
            productService.batchUpdateStatus(ids, active);
            return ApiResponse.success("批量更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
