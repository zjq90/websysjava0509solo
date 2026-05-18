package com.secondhand.repository;

import com.secondhand.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    List<Product> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(Long userId);

    List<Product> findByStatusAndIsDeletedFalseOrderByCreateTimeDesc(String status);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = 'ON_SALE' ORDER BY p.createTime DESC")
    List<Product> findLatestProducts();

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = 'ON_SALE' ORDER BY p.viewCount DESC")
    List<Product> findHotProducts();

}
