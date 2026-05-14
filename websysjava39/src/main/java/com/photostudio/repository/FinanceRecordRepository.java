package com.photostudio.repository;

import com.photostudio.entity.FinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收支记录Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    
    /**
     * 根据类型、时间范围和门店统计金额
     */
    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinanceRecord fr " +
           "WHERE fr.type = :type AND fr.recordDate BETWEEN :startDate AND :endDate " +
           "AND (:storeId IS NULL OR fr.store.id = :storeId)")
    BigDecimal sumByTypeAndDateRangeAndStore(@Param("type") String type,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("storeId") Long storeId);
                                              
    /**
     * 根据分类、时间范围和门店统计金额
     */
    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinanceRecord fr " +
           "WHERE fr.category = :category AND fr.recordDate BETWEEN :startDate AND :endDate " +
           "AND (:storeId IS NULL OR fr.store.id = :storeId)")
    BigDecimal sumByCategoryAndDateRangeAndStore(@Param("category") String category,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  @Param("storeId") Long storeId);
}
