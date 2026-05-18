package com.flower.repository;

import com.flower.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryAndEnabled(String category, Boolean enabled, Pageable pageable);

    Page<Product> findByEnabled(Boolean enabled, Pageable pageable);

    List<Product> findByIsHotTrueAndEnabled(Boolean enabled);

    List<Product> findByIsNewTrueAndEnabled(Boolean enabled);

    Page<Product> findByNameContainingAndEnabled(String keyword, Boolean enabled, Pageable pageable);
}