package com.accounting.service;

import com.accounting.entity.Budget;
import com.accounting.enums.BillType;
import com.accounting.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BillService billService;

    @Cacheable(value = "budgets", key = "'active'")
    public List<Budget> getActiveBudgets() {
        log.info("获取当前生效的预算列表");
        return budgetRepository.findActiveBudgets(LocalDateTime.now());
    }

    public List<Budget> getAllBudgets() {
        log.info("获取全部预算列表");
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        log.info("根据ID获取预算: {}", id);
        return budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预算不存在: " + id));
    }

    @Transactional
    @CacheEvict(value = "budgets", allEntries = true)
    public Budget createBudget(Budget budget) {
        log.info("创建预算: {}", budget.getName());
        budget.setId(null);
        return budgetRepository.save(budget);
    }

    @Transactional
    @CacheEvict(value = "budgets", allEntries = true)
    public Budget updateBudget(Long id, Budget budget) {
        log.info("更新预算: {}", id);
        Budget existing = getBudgetById(id);
        existing.setName(budget.getName());
        existing.setAmount(budget.getAmount());
        existing.setStartDate(budget.getStartDate());
        existing.setEndDate(budget.getEndDate());
        existing.setRemark(budget.getRemark());
        if (budget.getCategory() != null) {
            existing.setCategory(budget.getCategory());
        }
        return budgetRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "budgets", allEntries = true)
    public void deleteBudget(Long id) {
        log.info("删除预算: {}", id);
        if (!budgetRepository.existsById(id)) {
            throw new IllegalArgumentException("预算不存在: " + id);
        }
        budgetRepository.deleteById(id);
    }

    public BigDecimal getBudgetUsage(Long budgetId) {
        log.info("计算预算使用情况: {}", budgetId);
        Budget budget = getBudgetById(budgetId);
        BigDecimal spent = calculateSpentAmount(budget);
        if (budget.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return spent.divide(budget.getAmount(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    public BigDecimal calculateSpentAmount(Budget budget) {
        if (budget.getCategory() != null) {
            return billService.calculateTotalByTypeAndCategoryAndDateRange(
                    BillType.EXPENSE,
                    budget.getCategory().getId(),
                    budget.getStartDate(),
                    budget.getEndDate()
            );
        } else {
            return billService.calculateTotalByTypeAndDateRange(
                    BillType.EXPENSE,
                    budget.getStartDate(),
                    budget.getEndDate()
            );
        }
    }

    public boolean isBudgetWarning(Budget budget) {
        BigDecimal usagePercent = getBudgetUsage(budget.getId());
        return usagePercent.compareTo(new BigDecimal("80")) >= 0;
    }

    public Optional<Budget> getCurrentTotalBudget() {
        List<Budget> activeBudgets = getActiveBudgets();
        return activeBudgets.stream()
                .filter(b -> b.getCategory() == null)
                .findFirst();
    }
}
