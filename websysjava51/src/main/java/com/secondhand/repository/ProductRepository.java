package com.secondhand.repository;

import com.secondhand.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long sellerId);

    List<Product> findByStatus(String status);

    @Query("SELECT p FROM Product p WHERE p.stock < :threshold AND p.status = 'ON_SALE'")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    @Modifying
    @Query("UPDATE Product p SET p.status = :status WHERE p.id IN :ids")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id IN :ids")
    int batchDelete(@Param("ids") List<Long> ids);

    List<Product> findByCategoryAndStatus(String category, String status);

}