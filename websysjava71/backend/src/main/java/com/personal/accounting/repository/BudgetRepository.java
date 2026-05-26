package com.personal.accounting.repository;

import com.personal.accounting.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * 预算数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    /**
     * 根据月份查询预算列表
     */
    List<Budget> findByBudgetMonth(YearMonth budgetMonth);

    /**
     * 根据分类和月份查询预算
     */
    Optional<Budget> findByCategoryIdAndBudgetMonth(Long categoryId, YearMonth budgetMonth);

    /**
     * 查询某个月份的所有预算（包含分类信息）
     */
    @Query("SELECT b FROM Budget b JOIN FETCH b.category WHERE b.budgetMonth = :budgetMonth")
    List<Budget> findByBudgetMonthWithCategory(@Param("budgetMonth") YearMonth budgetMonth);
}
