package com.appsys.service;

import com.appsys.entity.Product;
import com.appsys.exception.ResourceNotFoundException;
import com.appsys.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品服务类
 * 处理产品的增删改查业务逻辑
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 获取所有产品
     * @return 产品列表
     */
    public List<Product> getAllProducts() {
        return productRepository.findAllOrderByCreatedAtDesc();
    }

    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在，ID: " + id));
    }

    /**
     * 根据批次编号获取产品
     * @param batchNumber 批次编号
     * @return 产品
     */
    public Product getProductByBatchNumber(String batchNumber) {
        return productRepository.findByBatchNumber(batchNumber)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在，批次编号: " + batchNumber));
    }

    /**
     * 创建产品
     * @param product 产品信息
     * @return 创建后的产品
     */
    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsByBatchNumber(product.getBatchNumber())) {
            throw new IllegalArgumentException("批次编号已存在: " + product.getBatchNumber());
        }
        return productRepository.save(product);
    }

    /**
     * 更新产品
     * @param id 产品ID
     * @param productDetails 更新的产品信息
     * @return 更新后的产品
     */
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        
        if (!product.getBatchNumber().equals(productDetails.getBatchNumber()) 
                && productRepository.existsByBatchNumber(productDetails.getBatchNumber())) {
            throw new IllegalArgumentException("批次编号已存在: " + productDetails.getBatchNumber());
        }
        
        product.setName(productDetails.getName());
        product.setBatchNumber(productDetails.getBatchNumber());
        product.setSpecification(productDetails.getSpecification());
        product.setExpiryDate(productDetails.getExpiryDate());
        product.setGerminationRate(productDetails.getGerminationRate());
        product.setBasePrice(productDetails.getBasePrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        
        return productRepository.save(product);
    }

    /**
     * 删除产品
     * @param id 产品ID
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    /**
     * 根据名称搜索产品
     * @param name 名称关键字
     * @return 产品列表
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    /**
     * 获取可用产品（库存>0）
     * @return 产品列表
     */
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts(0);
    }

    /**
     * 更新产品库存
     * @param id 产品ID
     * @param quantity 数量变化（正数增加，负数减少）
     * @return 更新后的产品
     */
    @Transactional
    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("库存不足，当前库存: " + product.getStockQuantity());
        }
        product.setStockQuantity(newStock);
        return productRepository.save(product);
    }

    /**
     * 根据分类查询产品
     * @param category 分类
     * @return 产品列表
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}
