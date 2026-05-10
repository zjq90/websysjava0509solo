package com.websys.service;

import com.websys.entity.Product;
import com.websys.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品服务类
 * 提供商品管理的业务逻辑，包括增删改查等操作
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 分页查询商品列表
     * @param productCode 商品编码（模糊查询）
     * @param productName 商品名称（模糊查询）
     * @param category 商品类别
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    public Page<Product> findPage(String productCode, String productName, String category, Integer status, Pageable pageable) {
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (productCode != null && !productCode.isEmpty()) {
                predicates.add(cb.like(root.get("productCode"), "%" + productCode + "%"));
            }
            
            if (productName != null && !productName.isEmpty()) {
                predicates.add(cb.like(root.get("productName"), "%" + productName + "%"));
            }
            
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 查询所有商品
     * @return 商品列表
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 根据商品编码查询商品
     * @param productCode 商品编码
     * @return 商品对象
     */
    public Optional<Product> findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    /**
     * 保存商品（新增/更新）
     * @param product 商品对象
     * @return 保存后的商品对象
     */
    @Transactional
    public Product save(Product product) {
        if (product.getId() != null) {
            product.setUpdateTime(LocalDateTime.now());
        }
        return productRepository.save(product);
    }

    /**
     * 删除商品
     * @param id 商品ID
     */
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 检查商品编码是否存在
     * @param productCode 商品编码
     * @return 是否存在
     */
    public boolean existsByProductCode(String productCode) {
        return productRepository.existsByProductCode(productCode);
    }

    /**
     * 根据类别查询商品
     * @param category 商品类别
     * @return 商品列表
     */
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * 更新商品状态
     * @param id 商品ID
     * @param status 新状态
     * @return 更新后的商品
     */
    @Transactional
    public Product updateStatus(Long id, Integer status) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus(status);
            product.setUpdateTime(LocalDateTime.now());
            return productRepository.save(product);
        }
        throw new RuntimeException("商品不存在");
    }
}
