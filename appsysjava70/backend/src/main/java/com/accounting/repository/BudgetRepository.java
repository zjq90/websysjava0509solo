package com.accounting.repository;

import com.accounting.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.startDate <= :date AND b.endDate >= :date")
    List<Budget> findActiveBudgets(@Param("date") LocalDateTime date);

    List<Budget> findByCategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long categoryId, LocalDateTime start, LocalDateTime end);
}
