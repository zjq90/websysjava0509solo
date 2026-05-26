package com.personal.accounting.service;

import com.personal.accounting.dto.BudgetProgressDTO;
import com.personal.accounting.entity.Budget;
import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.enums.CategoryType;
import com.personal.accounting.repository.BudgetRepository;
import com.personal.accounting.repository.CategoryRepository;
import com.personal.accounting.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 预算服务类
 * 提供预算设置和进度跟踪功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    /**
     * 获取指定月份的预算列表
     */
    @Transactional(readOnly = true)
    public List<Budget> getBudgetsByMonth(YearMonth month) {
        return budgetRepository.findByBudgetMonthWithCategory(month);
    }

    /**
     * 获取指定月份的预算进度列表
     */
    @Transactional(readOnly = true)
    public List<BudgetProgressDTO> getBudgetProgress(YearMonth month) {
        log.debug("获取 {} 月预算进度", month);
        
        List<Budget> budgets = budgetRepository.findByBudgetMonthWithCategory(month);
        List<BudgetProgressDTO> progressList = new ArrayList<>();
        
        LocalDateTime monthStart = month.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = month.atEndOfMonth().atTime(23, 59, 59);
        
        for (Budget budget : budgets) {
            BudgetProgressDTO dto = new BudgetProgressDTO();
            dto.setCategoryId(budget.getCategory().getId());
            dto.setCategoryName(budget.getCategory().getName());
            dto.setColor(budget.getCategory().getColor());
            dto.setBudgetAmount(budget.getBudgetAmount());
            
            // 计算已支出金额
            BigDecimal spent = transactionRepository.sumExpenseByCategoryAndTimeBetween(
                    budget.getCategory().getId(), monthStart, monthEnd);
            dto.setSpentAmount(spent != null ? spent : BigDecimal.ZERO);
            
            // 计算剩余金额
            dto.setRemainingAmount(budget.getBudgetAmount().subtract(dto.getSpentAmount()));
            
            // 计算使用百分比
            if (budget.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0) {
                dto.setUsedPercentage(dto.getSpentAmount().doubleValue() 
                        / budget.getBudgetAmount().doubleValue() * 100);
            } else {
                dto.setUsedPercentage(0.0);
            }
            
            progressList.add(dto);
        }
        
        return progressList;
    }

    /**
     * 设置或更新预算
     */
    @Transactional
    public Budget setBudget(Long categoryId, YearMonth month, BigDecimal amount, Boolean rolloverRemaining) {
        log.debug("设置预算: categoryId={}, month={}, amount={}", categoryId, month, amount);
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + categoryId));
        
        if (category.getType() != CategoryType.EXPENSE) {
            throw new IllegalArgumentException("只能为支出分类设置预算");
        }
        
        Optional<Budget> existing = budgetRepository.findByCategoryIdAndBudgetMonth(categoryId, month);
        Budget budget;
        
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setBudgetAmount(amount);
            budget.setRolloverRemaining(rolloverRemaining);
            log.info("预算已更新: {}", budget.getId());
        } else {
            budget = new Budget();
            budget.setCategory(category);
            budget.setBudgetMonth(month);
            budget.setBudgetAmount(amount);
            budget.setRolloverRemaining(rolloverRemaining);
            log.info("预算已创建");
        }
        
        return budgetRepository.save(budget);
    }

    /**
     * 删除预算
     */
    @Transactional
    public void deleteBudget(Long id) {
        log.debug("删除预算: {}", id);
        budgetRepository.deleteById(id);
        log.info("预算已删除: {}", id);
    }

    /**
     * 复制上月预算到本月
     */
    @Transactional
    public int copyLastMonthBudget(YearMonth targetMonth) {
        YearMonth lastMonth = targetMonth.minusMonths(1);
        log.debug("复制 {} 月预算到 {} 月", lastMonth, targetMonth);
        
        List<Budget> lastMonthBudgets = budgetRepository.findByBudgetMonth(lastMonth);
        int count = 0;
        
        for (Budget lastBudget : lastMonthBudgets) {
            Optional<Budget> existing = budgetRepository.findByCategoryIdAndBudgetMonth(
                    lastBudget.getCategory().getId(), targetMonth);
            
            if (existing.isEmpty()) {
                Budget newBudget = new Budget();
                newBudget.setCategory(lastBudget.getCategory());
                newBudget.setBudgetMonth(targetMonth);
                newBudget.setBudgetAmount(lastBudget.getBudgetAmount());
                newBudget.setRolloverRemaining(lastBudget.getRolloverRemaining());
                budgetRepository.save(newBudget);
                count++;
            }
        }
        
        log.info("预算复制完成，共复制 {} 条", count);
        return count;
    }
}
