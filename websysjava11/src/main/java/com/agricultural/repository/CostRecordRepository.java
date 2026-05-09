package com.agricultural.repository;

import com.agricultural.entity.CostRecord;
import com.agricultural.entity.enums.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 成本记录数据访问层
 */
@Repository
public interface CostRecordRepository extends JpaRepository<CostRecord, Long> {

    /**
     * 根据成本单号查找
     */
    Optional<CostRecord> findByCostNo(String costNo);

    /**
     * 根据成本类别查询
     */
    List<CostRecord> findByCategoryOrderByExpenseDateDesc(CostCategory category);

    /**
     * 根据品种ID查询
     */
    List<CostRecord> findByVarietyIdOrderByExpenseDateDesc(Long varietyId);

    /**
     * 根据日期范围查询
     */
    List<CostRecord> findByExpenseDateBetweenOrderByExpenseDateDesc(LocalDate startDate, LocalDate endDate);

    /**
     * 检查成本单号是否存在
     */
    boolean existsByCostNo(String costNo);

    /**
     * 计算指定类别的总成本
     */
    @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM CostRecord c WHERE c.category = :category")
    BigDecimal sumByCategory(@Param("category") CostCategory category);

    /**
     * 按类别分组统计总成本
     */
    @Query("SELECT c.category, COALESCE(SUM(c.totalAmount), 0) FROM CostRecord c GROUP BY c.category")
    List<Object[]> sumGroupByCategory();

    /**
     * 计算所有成本总额
     */
    @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM CostRecord c")
    BigDecimal sumAll();

    /**
     * 查询最大的成本单号
     */
    @Query("SELECT MAX(c.costNo) FROM CostRecord c WHERE c.costNo LIKE :prefix%")
    String findMaxCostNoByPrefix(@Param("prefix") String prefix);
}
