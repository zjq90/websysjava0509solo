package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.Product;
import com.flower.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/api/products")
@Api(tags = "商品管理接口")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ApiOperation("分页查询商品列表")
    public Result<Page<Product>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询商品")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return product != null ? Result.success(product) : Result.error("商品不存在");
    }

    @PostMapping
    @ApiOperation("新增商品")
    public Result<Product> create(@RequestBody Product product) {
        return Result.success(productService.save(product));
    }

    @PutMapping("/{id}")
    @ApiOperation("更新商品")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success(productService.save(product));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @ApiOperation("商品上下架")
    public Result<Product> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Product product = productService.updateStatus(id, status);
        return product != null ? Result.success(product) : Result.error("商品不存在");
    }

    @GetMapping("/warning")
    @ApiOperation("获取库存预警商品列表")
    public Result<List<Product>> getStockWarning() {
        return Result.success(productService.getStockWarningProducts());
    }

    @GetMapping("/template")
    @ApiOperation("下载商品导入模板")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        byte[] content = productService.generateTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "product_template.xlsx");
        return ResponseEntity.ok().headers(headers).body(content);
    }

    @PostMapping("/import")
    @ApiOperation("批量导入商品")
    public Result<List<Product>> importProducts(@RequestParam("file") MultipartFile file) throws IOException {
        List<Product> products = productService.importProducts(file);
        return Result.success("成功导入 " + products.size() + " 个商品", products);
    }
}
