package com.personal.accounting.repository;

import com.personal.accounting.entity.Budget;
import com.personal.accounting.enums.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByFamilyIdAndBudgetMonth(Long familyId, YearMonth budgetMonth);
    List<Budget> findByUserIdAndBudgetMonth(Long userId, YearMonth budgetMonth);
    Optional<Budget> findByFamilyIdAndCategoryAndBudgetMonth(Long familyId, BudgetCategory category, YearMonth budgetMonth);
    List<Budget> findByFamilyId(Long familyId);
}
