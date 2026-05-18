package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.service.ProductService;
import com.secondhand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 *
 * @author secondhand
 * @version 1.0.0
 */
@RestController
@RequestMapping("/product")
@Tag(name = "商品管理", description = "商品CRUD、附近商品推荐等接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "发布商品", description = "发布新的二手商品")
    public Result<Product> createProduct(@RequestHeader("Authorization") String token, @RequestBody Product product) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        product.setUserId(user.getId());
        Product created = productService.createProduct(product);
        return Result.success("发布成功", created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据ID获取商品详细信息")
    public Result<Product> getProductById(@PathVariable Long id, @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
        Product product = productService.getProductById(id, lat, lon);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品信息", description = "更新已发布商品的信息")
    public Result<Product> updateProduct(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Product product) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        Product existProduct = productService.getProductById(id, null, null);
        if (existProduct == null) {
            return Result.error("商品不存在");
        }
        if (!existProduct.getUserId().equals(user.getId())) {
            return Result.error(403, "无权限修改此商品");
        }
        Product updated = productService.updateProduct(id, product);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "删除已发布的商品")
    public Result<Void> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        Product existProduct = productService.getProductById(id, null, null);
        if (existProduct == null) {
            return Result.error("商品不存在");
        }
        if (!existProduct.getUserId().equals(user.getId())) {
            return Result.error(403, "无权限删除此商品");
        }
        productService.deleteProduct(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/nearby")
    @Operation(summary = "附近商品", description = "根据地理位置获取附近的商品列表")
    public Result<List<Product>> getNearbyProducts(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon, @RequestParam(required = false) Double maxDistance) {
        List<Product> products = productService.getNearbyProducts(lat, lon, maxDistance);
        return Result.success(products);
    }

    @GetMapping("/my")
    @Operation(summary = "我的商品", description = "获取当前用户发布的商品列表")
    public Result<List<Product>> getUserProducts(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        List<Product> products = productService.getUserProducts(user.getId());
        return Result.success(products);
    }

    @GetMapping("/latest")
    @Operation(summary = "最新商品", description = "获取最新发布的商品列表")
    public Result<List<Product>> getLatestProducts() {
        List<Product> products = productService.getLatestProducts();
        return Result.success(products);
    }

    @GetMapping("/hot")
    @Operation(summary = "热门商品", description = "获取热门商品列表")
    public Result<List<Product>> getHotProducts() {
        List<Product> products = productService.getHotProducts();
        return Result.success(products);
    }

}
