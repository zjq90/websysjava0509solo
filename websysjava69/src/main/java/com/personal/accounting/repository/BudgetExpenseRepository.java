package com.personal.accounting.repository;

import com.personal.accounting.entity.BudgetExpense;
import com.personal.accounting.enums.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetExpenseRepository extends JpaRepository<BudgetExpense, Long> {
    List<BudgetExpense> findByBudgetIdOrderByExpenseDateDesc(Long budgetId);
    List<BudgetExpense> findByFamilyIdOrderByExpenseDateDesc(Long familyId);
    List<BudgetExpense> findByUserIdOrderByExpenseDateDesc(Long userId);
    List<BudgetExpense> findByBudgetIdAndUserId(Long budgetId, Long userId);
    
    @Query("SELECT COALESCE(SUM(be.amount), 0) FROM BudgetExpense be WHERE be.budgetId = :budgetId")
    BigDecimal getTotalSpentByBudgetId(Long budgetId);
    
    @Query("SELECT COALESCE(SUM(be.amount), 0) FROM BudgetExpense be WHERE be.familyId = :familyId AND be.category = :category AND be.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpentByFamilyIdAndCategoryAndDateRange(Long familyId, BudgetCategory category, LocalDate startDate, LocalDate endDate);
}
