package com.management.repository;

import com.management.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售明细数据访问接口
 */
@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findBySaleId(Long saleId);

    @Query("SELECT SUM(si.quantity) FROM SaleItem si WHERE si.product.id = :productId " +
            "AND si.sale.saleDate BETWEEN :startDate AND :endDate")
    Integer sumQuantityByProductAndDateRange(@Param("productId") Long productId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT si.product.id, si.product.productName, SUM(si.quantity) as totalQty, " +
            "SUM(si.amount) as totalAmount, SUM(si.profit) as totalProfit " +
            "FROM SaleItem si " +
            "WHERE si.sale.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY si.product.id, si.product.productName " +
            "ORDER BY totalQty DESC")
    List<Object[]> findProductSalesReport(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT si.product.id, si.product.productName, " +
            "SUM(si.quantity) as totalQty, SUM(si.amount) as totalAmount, " +
            "SUM(si.cost) as totalCost, SUM(si.profit) as totalProfit " +
            "FROM SaleItem si " +
            "GROUP BY si.product.id, si.product.productName")
    List<Object[]> findAllProductGrossMargin();
}
