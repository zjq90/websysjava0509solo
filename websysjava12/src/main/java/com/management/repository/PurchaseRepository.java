package com.management.repository;

import com.management.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购数据访问接口
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(p.totalAmount) FROM Purchase p WHERE p.purchaseDate BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.quantity) FROM Purchase p WHERE p.product.id = :productId " +
            "AND p.purchaseDate BETWEEN :startDate AND :endDate")
    Integer sumQuantityByProductAndDateRange(@Param("productId") Long productId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}
