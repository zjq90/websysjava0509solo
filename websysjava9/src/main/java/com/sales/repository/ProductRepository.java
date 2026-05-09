package com.sales.repository;

import com.sales.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据产品编码查找产品
     */
    Optional<Product> findByCode(String code);

    /**
     * 查找所有启用的产品
     */
    List<Product> findByActiveTrue();

    /**
     * 按类别查找产品
     */
    List<Product> findByCategory(String category);

    /**
     * 根据名称模糊查找
     */
    List<Product> findByNameContaining(String name);

    /**
     * 分页查询所有产品，按创建时间倒序
     */
    Page<Product> findAllByOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 分页查询所有启用的产品
     */
    Page<Product> findByActiveTrueOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 按类别分页查询
     */
    Page<Product> findByCategoryOrderByCreateTimeDesc(String category, Pageable pageable);

    /**
     * 按名称模糊分页查询
     */
    Page<Product> findByNameContainingOrderByCreateTimeDesc(String name, Pageable pageable);
}
