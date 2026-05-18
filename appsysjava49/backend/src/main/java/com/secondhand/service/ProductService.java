package com.secondhand.service;

import com.secondhand.common.PageResult;
import com.secondhand.common.Result;
import com.secondhand.entity.Product;
import com.secondhand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Result<Product> create(Product product) {
        product.setStatus(1);
        product.setViewCount(0);
        product.setFavoriteCount(0);
        product.setShareCount(0);
        product.setRefreshTime(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return Result.success("发布成功", savedProduct);
    }

    public Result<Product> update(Long id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return Result.error("商品不存在");
        }

        Product existProduct = productOptional.get();
        if (product.getTitle() != null) existProduct.setTitle(product.getTitle());
        if (product.getDescription() != null) existProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) existProduct.setPrice(product.getPrice());
        if (product.getOriginalPrice() != null) existProduct.setOriginalPrice(product.getOriginalPrice());
        if (product.getCategoryId() != null) existProduct.setCategoryId(product.getCategoryId());
        if (product.getBrand() != null) existProduct.setBrand(product.getBrand());
        if (product.getCondition() != null) existProduct.setCondition(product.getCondition());
        if (product.getLocation() != null) existProduct.setLocation(product.getLocation());
        if (product.getImages() != null) existProduct.setImages(product.getImages());
        if (product.getCoverImage() != null) existProduct.setCoverImage(product.getCoverImage());
        if (product.getIsNegotiable() != null) existProduct.setIsNegotiable(product.getIsNegotiable());
        if (product.getIsDelivery() != null) existProduct.setIsDelivery(product.getIsDelivery());
        if (product.getIsPickup() != null) existProduct.setIsPickup(product.getIsPickup());

        Product savedProduct = productRepository.save(existProduct);
        return Result.success("更新成功", savedProduct);
    }

    public Result<Product> getById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return Result.error("商品不存在");
        }

        Product product = productOptional.get();
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);

        return Result.success(product);
    }

    public Result<PageResult<Product>> search(String keyword, Long categoryId, BigDecimal minPrice,
                                               BigDecimal maxPrice, String condition, String brand,
                                               String sortBy, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), 1));

            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.or(
                    cb.like(root.get("title"), "%" + keyword + "%"),
                    cb.like(root.get("description"), "%" + keyword + "%")
                ));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (StringUtils.hasText(condition)) {
                predicates.add(cb.equal(root.get("condition"), condition));
            }

            if (StringUtils.hasText(brand)) {
                predicates.add(cb.equal(root.get("brand"), brand));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> page = productRepository.findAll(spec, pageable);

        if ("viewCount".equals(sortBy)) {
            pageable = PageRequest.of(pageNum - 1, pageSize, org.springframework.data.domain.Sort.by("viewCount").descending());
            page = productRepository.findAll(spec, pageable);
        } else if ("priceAsc".equals(sortBy)) {
            pageable = PageRequest.of(pageNum - 1, pageSize, org.springframework.data.domain.Sort.by("price").ascending());
            page = productRepository.findAll(spec, pageable);
        } else if ("priceDesc".equals(sortBy)) {
            pageable = PageRequest.of(pageNum - 1, pageSize, org.springframework.data.domain.Sort.by("price").descending());
            page = productRepository.findAll(spec, pageable);
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize, org.springframework.data.domain.Sort.by("refreshTime").descending());
            page = productRepository.findAll(spec, pageable);
        }

        PageResult<Product> pageResult = PageResult.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    public Result<List<Product>> getMyProducts(Long userId) {
        List<Product> products = productRepository.findByUserIdAndStatus(userId, 1);
        return Result.success(products);
    }

    public Result<Void> delete(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return Result.error("商品不存在");
        }

        Product product = productOptional.get();
        product.setStatus(0);
        productRepository.save(product);

        return Result.success("删除成功");
    }

    public Result<List<String>> getSuggestions(String keyword) {
        List<Product> products = productRepository.searchByKeyword(keyword);
        List<String> suggestions = new ArrayList<>();
        for (Product product : products) {
            if (!suggestions.contains(product.getTitle())) {
                suggestions.add(product.getTitle());
            }
            if (suggestions.size() >= 10) break;
        }
        return Result.success(suggestions);
    }
}
