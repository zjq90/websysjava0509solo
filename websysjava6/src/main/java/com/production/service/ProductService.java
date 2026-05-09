package com.production.service;

import com.production.entity.Product;
import com.production.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 产品业务逻辑层
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 分页查询所有产品
     */
    public Page<Product> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productRepository.findAll(pageable);
    }

    /**
     * 查询所有产品
     */
    public List<Product> findAll() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询产品
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 根据产品编号查询
     */
    public Optional<Product> findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    /**
     * 保存产品
     */
    public Product save(Product product) {
        if (product.getId() == null) {
            // 新增产品，检查编号是否已存在
            if (productRepository.existsByProductCode(product.getProductCode())) {
                throw new RuntimeException("产品编号已存在：" + product.getProductCode());
            }
        }
        product.setUpdateTime(LocalDateTime.now());
        return productRepository.save(product);
    }

    /**
     * 删除产品
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 逻辑删除（修改状态）
     */
    public void deactivate(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus("INACTIVE");
            product.setUpdateTime(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    /**
     * 激活产品
     */
    public void activate(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus("ACTIVE");
            product.setUpdateTime(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    /**
     * 查询所有激活的产品
     */
    public List<Product> findAllActive() {
        return productRepository.findAllActive();
    }

    /**
     * 根据名称模糊搜索
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByProductNameContaining(name);
    }
}