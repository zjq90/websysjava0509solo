package com.vending.service;

import com.vending.entity.Category;
import com.vending.entity.Product;
import com.vending.repository.CategoryRepository;
import com.vending.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 商品服务类
 * 提供商品的增删改查功能
 */
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 获取所有商品
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    /**
     * 获取所有上架商品
     */
    public List<Product> findActiveProducts() {
        return productRepository.findByActiveTrue();
    }
    
    /**
     * 根据ID查询商品
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * 保存商品
     */
    @Transactional
    public Product save(Product product, Long categoryId) {
        if (product.getBarcode() != null && productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("条形码已存在");
        }
        
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }
    
    /**
     * 更新商品
     */
    @Transactional
    public Product update(Long id, Product product, Long categoryId) {
        Product existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        if (product.getBarcode() != null 
            && !product.getBarcode().equals(existing.getBarcode())
            && productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("条形码已存在");
        }
        
        existing.setName(product.getName());
        existing.setImageUrl(product.getImageUrl());
        existing.setSpecification(product.getSpecification());
        existing.setBarcode(product.getBarcode());
        existing.setCostPrice(product.getCostPrice());
        existing.setRetailPrice(product.getRetailPrice());
        existing.setStockThreshold(product.getStockThreshold() != null ? product.getStockThreshold() : 5);
        existing.setDescription(product.getDescription());
        existing.setActive(product.getActive() != null ? product.getActive() : true);
        
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
            existing.setCategory(category);
        } else {
            existing.setCategory(null);
        }
        
        return productRepository.save(existing);
    }
    
    /**
     * 删除商品
     */
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("商品不存在");
        }
        productRepository.deleteById(id);
    }
    
    /**
     * 搜索商品
     */
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchProducts(keyword);
    }
    
    /**
     * 批量更新商品状态
     */
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Boolean active) {
        for (Long id : ids) {
            productRepository.findById(id).ifPresent(product -> {
                product.setActive(active);
                productRepository.save(product);
            });
        }
    }
}
