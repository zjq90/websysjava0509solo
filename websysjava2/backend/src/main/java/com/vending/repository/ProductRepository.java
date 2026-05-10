package com.vending.repository;

import com.vending.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByBarcode(String barcode);
    
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findByActiveTrue();
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% OR p.barcode LIKE %?1%")
    List<Product> searchProducts(String keyword);
    
    boolean existsByBarcode(String barcode);
}
