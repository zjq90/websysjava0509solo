package com.secondhand.repository;

import com.secondhand.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByUserIdAndStatus(Long userId, Integer status);

    List<Product> findByCategoryIdAndStatus(Long categoryId, Integer status);

    @Query("SELECT p FROM Product p WHERE p.status = 1 AND (p.title LIKE %?1% OR p.description LIKE %?1%)")
    List<Product> searchByKeyword(String keyword);

    List<Product> findByStatusOrderByRefreshTimeDesc(Integer status);

    List<Product> findByStatusOrderByViewCountDesc(Integer status);

    List<Product> findByStatusOrderByPriceAsc(Integer status);

    List<Product> findByStatusOrderByPriceDesc(Integer status);
}
