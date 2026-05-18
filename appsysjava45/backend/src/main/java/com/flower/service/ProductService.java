package com.flower.service;

import com.flower.entity.Product;
import com.flower.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务类
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProductList(String category, String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingAndEnabled(keyword, true, pageable);
        }
        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategoryAndEnabled(category, true, pageable);
        }
        return productRepository.findByEnabled(true, pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    public List<Product> getHotProducts() {
        return productRepository.findByIsHotTrueAndEnabled(true);
    }

    public List<Product> getNewProducts() {
        return productRepository.findByIsNewTrueAndEnabled(true);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        if (product.getName() != null) existing.setName(product.getName());
        if (product.getDescription() != null) existing.setDescription(product.getDescription());
        if (product.getDetail() != null) existing.setDetail(product.getDetail());
        if (product.getPrice() != null) existing.setPrice(product.getPrice());
        if (product.getOriginalPrice() != null) existing.setOriginalPrice(product.getOriginalPrice());
        if (product.getImage() != null) existing.setImage(product.getImage());
        if (product.getCategory() != null) existing.setCategory(product.getCategory());
        if (product.getStock() != null) existing.setStock(product.getStock());
        if (product.getIsHot() != null) existing.setIsHot(product.getIsHot());
        if (product.getIsNew() != null) existing.setIsNew(product.getIsNew());

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setEnabled(false);
        productRepository.save(product);
    }
}