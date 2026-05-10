package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Product;
import com.websys.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 商品管理控制器
 * 提供商品管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/product")
@Tag(name = "商品管理", description = "商品管理相关接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @Operation(summary = "查询所有商品", description = "查询所有商品列表")
    public Result<List<Product>> getAll() {
        List<Product> products = productService.findAll();
        return Result.success(products);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询商品列表", description = "根据条件分页查询商品列表")
    public Result<Page<Product>> getPage(
            @Parameter(description = "商品编码") @RequestParam(required = false) String productCode,
            @Parameter(description = "商品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "商品类别") @RequestParam(required = false) String category,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Product> productPage = productService.findPage(productCode, productName, category, status, pageRequest);
        return Result.success(productPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询商品", description = "根据商品ID查询商品详情")
    public Result<Product> getById(@Parameter(description = "商品ID") @PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(Result::success).orElseGet(() -> Result.error("商品不存在"));
    }

    @GetMapping("/code/{productCode}")
    @Operation(summary = "根据商品编码查询商品", description = "根据商品编码查询商品详情")
    public Result<Product> getByProductCode(@Parameter(description = "商品编码") @PathVariable String productCode) {
        Optional<Product> product = productService.findByProductCode(productCode);
        return product.map(Result::success).orElseGet(() -> Result.error("商品不存在"));
    }

    @PostMapping
    @Operation(summary = "新增商品", description = "新增商品信息")
    public Result<Product> save(@RequestBody Product product) {
        if (productService.existsByProductCode(product.getProductCode())) {
            return Result.error("商品编码已存在");
        }
        Product saved = productService.save(product);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新商品", description = "更新商品信息")
    public Result<Product> update(@RequestBody Product product) {
        if (product.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        Product updated = productService.save(product);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品")
    public Result<Void> delete(@Parameter(description = "商品ID") @PathVariable Long id) {
        productService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "更新商品状态", description = "更新商品上架/下架状态")
    public Result<Product> updateStatus(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "状态：1上架，0下架") @PathVariable Integer status) {
        Product product = productService.updateStatus(id, status);
        return Result.success("状态更新成功", product);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据类别查询商品", description = "根据商品类别查询商品列表")
    public Result<List<Product>> getByCategory(@Parameter(description = "商品类别") @PathVariable String category) {
        List<Product> products = productService.findByCategory(category);
        return Result.success(products);
    }
}
