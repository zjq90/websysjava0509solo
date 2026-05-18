package com.flower.repository;

import com.flower.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品数据访问层
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * 查询库存低于预警值的商品
     */
    List<Product> findByStockLessThanEqualAndStatus(Integer minStock, Integer status);

    /**
     * 根据状态查询商品
     */
    List<Product> findByStatus(Integer status);

    /**
     * 根据分类查询商品
     */
    List<Product> findByCategory(String category);

    /**
     * 根据名称模糊查询
     */
    List<Product> findByNameContaining(String name);
}
