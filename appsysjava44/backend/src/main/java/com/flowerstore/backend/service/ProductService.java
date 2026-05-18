package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.*;
import com.flowerstore.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryRelationRepository productCategoryRelationRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    /**
     * 获取首页数据
     */
    @Cacheable(value = "homeData", key = "'home'")
    public Result<Map<String, Object>> getHomeData() {
        Map<String, Object> data = new HashMap<>();

        List<Product> hotProducts = productRepository.findByIsHotAndStatus(1, 1);
        List<Product> newProducts = productRepository.findByIsNewAndStatus(1, 1);
        List<Product> recommendProducts = productRepository.findByIsRecommendAndStatus(1, 1);
        List<ProductCategory> categories = productCategoryRepository.findByStatusOrderBySortOrderAsc(1);

        data.put("hotProducts", hotProducts);
        data.put("newProducts", newProducts);
        data.put("recommendProducts", recommendProducts);
        data.put("categories", categories);

        return Result.success(data);
    }

    /**
     * 获取产品分类列表
     */
    public Result<List<ProductCategory>> getCategories(String categoryType) {
        List<ProductCategory> categories;
        if (categoryType != null && !categoryType.isEmpty()) {
            categories = productCategoryRepository.findByCategoryTypeAndStatusOrderBySortOrderAsc(categoryType, 1);
        } else {
            categories = productCategoryRepository.findByStatusOrderBySortOrderAsc(1);
        }
        return Result.success(categories);
    }

    /**
     * 根据分类ID获取产品列表
     */
    public Result<List<Product>> getProductsByCategory(Long categoryId) {
        List<ProductCategoryRelation> relations = productCategoryRelationRepository.findByCategoryId(categoryId);
        List<Long> productIds = relations.stream()
                .map(ProductCategoryRelation::getProductId)
                .collect(Collectors.toList());

        if (productIds.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        List<Product> products = productRepository.findAllById(productIds)
                .stream()
                .filter(p -> p.getStatus() == 1)
                .collect(Collectors.toList());

        return Result.success(products);
    }

    /**
     * 搜索产品
     */
    public Result<List<Product>> searchProducts(String keyword) {
        List<Product> products = productRepository.searchByKeyword(keyword);
        return Result.success(products);
    }

    /**
     * 获取搜索建议
     */
    public Result<List<String>> getSearchSuggestions(String keyword) {
        List<Product> products = productRepository.searchByKeyword(keyword);
        List<String> suggestions = products.stream()
                .map(Product::getName)
                .limit(10)
                .collect(Collectors.toList());
        return Result.success(suggestions);
    }

    /**
     * 获取产品详情
     */
    public Result<Map<String, Object>> getProductDetail(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return Result.error("产品不存在");
        }

        Product product = productOpt.get();
        List<ProductImage> images = productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
        List<ProductReview> reviews = productReviewRepository.findByProductIdAndStatusOrderByCreateTimeDesc(productId, 1);

        Map<String, Object> data = new HashMap<>();
        data.put("product", product);
        data.put("images", images);
        data.put("reviews", reviews);
        data.put("reviewCount", reviews.size());
        data.put("avgRating", reviews.isEmpty() ? 5.0 : reviews.stream()
                .mapToInt(ProductReview::getRating)
                .average()
                .orElse(5.0));

        return Result.success(data);
    }

    /**
     * 根据价格区间筛选产品
     */
    public Result<List<Product>> getProductsByPriceRange(Long minPrice, Long maxPrice) {
        List<Product> products = productRepository.findByPriceBetweenAndStatus(minPrice, maxPrice, 1);
        return Result.success(products);
    }

    /**
     * 获取所有产品
     */
    public Result<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findByStatus(1);
        return Result.success(products);
    }
}
