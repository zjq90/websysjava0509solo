package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Product;
import com.secondhand.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Tag(name = "商品管理", description = "商品的增删改查、批量操作、导入导出等接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "分页查询商品列表")
    public Result<Page<Product>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(productService.findAll(name, category, status, sellerId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询商品")
    public Result<Product> getById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(Result::success).orElse(Result.error("商品不存在"));
    }

    @PostMapping
    @Operation(summary = "新增商品")
    public Result<Product> create(@RequestBody Product product) {
        return Result.success(productService.save(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success(productService.save(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/batch/off-shelf")
    @Operation(summary = "批量下架商品")
    public Result<Integer> batchOffShelf(@RequestBody List<Long> ids) {
        int count = productService.batchOffShelf(ids);
        return Result.success("成功下架" + count + "件商品", count);
    }

    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除商品")
    public Result<Integer> batchDelete(@RequestBody List<Long> ids) {
        int count = productService.batchDelete(ids);
        return Result.success("成功删除" + count + "件商品", count);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "查询库存预警商品")
    public Result<List<Product>> getLowStockProducts() {
        return Result.success(productService.findLowStockProducts());
    }

    @PostMapping("/import")
    @Operation(summary = "批量导入商品")
    public Result<List<Product>> importProducts(@RequestParam("file") MultipartFile file, @RequestParam Long sellerId) throws IOException {
        return Result.success(productService.importFromExcel(file, sellerId));
    }

    @PostMapping("/export")
    @Operation(summary = "导出商品列表")
    public ResponseEntity<byte[]> exportProducts(@RequestBody List<Product> products) throws IOException {
        byte[] excelBytes = productService.exportToExcel(products);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "products.xlsx");
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }

}