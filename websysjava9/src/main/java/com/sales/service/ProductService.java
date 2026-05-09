package com.sales.service;

import com.sales.entity.Product;
import com.sales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 产品服务层
 * 处理产品相关的业务逻辑
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 获取所有产品
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * 获取所有启用的产品
     */
    public List<Product> findAllActive() {
        return productRepository.findByActiveTrue();
    }

    /**
     * 根据ID获取产品
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 根据编码获取产品
     */
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    /**
     * 保存产品
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 更新产品
     */
    public Product update(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setCategory(productDetails.getCategory());
            product.setSpecification(productDetails.getSpecification());
            product.setBasePrice(productDetails.getBasePrice());
            product.setStockQuantity(productDetails.getStockQuantity());
            product.setUnit(productDetails.getUnit());
            product.setDescription(productDetails.getDescription());
            product.setActive(productDetails.getActive());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("产品不存在: " + id));
    }

    /**
     * 删除产品
     */
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 按名称搜索
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    /**
     * 按类别查找
     */
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * 分页查询所有产品
     */
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAllByOrderByCreateTimeDesc(pageable);
    }

    /**
     * 分页查询所有启用的产品
     */
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findByActiveTrueOrderByCreateTimeDesc(pageable);
    }

    /**
     * 按类别分页查询
     */
    public Page<Product> findByCategory(String category, Pageable pageable) {
        return productRepository.findByCategoryOrderByCreateTimeDesc(category, pageable);
    }

    /**
     * 按名称模糊分页查询
     */
    public Page<Product> searchByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingOrderByCreateTimeDesc(name, pageable);
    }
}
