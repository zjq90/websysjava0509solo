package com.management.repository;

import com.management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllCategories();

    List<Product> findByProductNameContaining(String keyword);
}
