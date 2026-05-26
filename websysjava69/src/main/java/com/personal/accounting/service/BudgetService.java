package com.personal.accounting.service;

import com.personal.accounting.entity.Budget;
import com.personal.accounting.entity.BudgetExpense;
import com.personal.accounting.enums.BudgetCategory;
import com.personal.accounting.enums.FamilyRole;
import com.personal.accounting.repository.BudgetExpenseRepository;
import com.personal.accounting.repository.BudgetRepository;
import com.personal.accounting.repository.FamilyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetExpenseRepository budgetExpenseRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public List<Budget> getFamilyBudgets(Long familyId, YearMonth month) {
        if (month == null) {
            month = YearMonth.now();
        }
        return budgetRepository.findByFamilyIdAndBudgetMonth(familyId, month);
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("预算不存在"));
    }

    @Transactional
    public Budget createBudget(Budget budget, Long operatorId) {
        checkBudgetPermission(budget.getFamilyId(), operatorId);

        if (budget.getBudgetMonth() == null) {
            budget.setBudgetMonth(YearMonth.now());
        }
        if (budget.getSpentAmount() == null) {
            budget.setSpentAmount(BigDecimal.ZERO);
        }

        Budget existing = budgetRepository.findByFamilyIdAndCategoryAndBudgetMonth(
                budget.getFamilyId(), budget.getCategory(), budget.getBudgetMonth()).orElse(null);
        if (existing != null) {
            throw new RuntimeException("该类别本月预算已存在");
        }

        return budgetRepository.save(budget);
    }

    @Transactional
    public Budget updateBudget(Long id, Budget budget, Long operatorId) {
        Budget existing = getBudgetById(id);
        checkBudgetPermission(existing.getFamilyId(), operatorId);

        existing.setBudgetName(budget.getBudgetName());
        existing.setCategory(budget.getCategory());
        existing.setTotalAmount(budget.getTotalAmount());
        existing.setBudgetMonth(budget.getBudgetMonth());
        existing.setDescription(budget.getDescription());

        return budgetRepository.save(existing);
    }

    @Transactional
    public void deleteBudget(Long id, Long operatorId) {
        Budget budget = getBudgetById(id);
        checkBudgetPermission(budget.getFamilyId(), operatorId);
        budgetRepository.deleteById(id);
    }

    public List<BudgetExpense> getBudgetExpenses(Long budgetId) {
        return budgetExpenseRepository.findByBudgetIdOrderByExpenseDateDesc(budgetId);
    }

    public List<BudgetExpense> getFamilyBudgetExpenses(Long familyId) {
        return budgetExpenseRepository.findByFamilyIdOrderByExpenseDateDesc(familyId);
    }

    public List<BudgetExpense> getUserBudgetExpenses(Long userId) {
        return budgetExpenseRepository.findByUserIdOrderByExpenseDateDesc(userId);
    }

    @Transactional
    public BudgetExpense addExpense(BudgetExpense expense, Long operatorId) {
        Budget budget = budgetRepository.findById(expense.getBudgetId())
                .orElseThrow(() -> new RuntimeException("预算不存在"));

        expense.setFamilyId(budget.getFamilyId());
        expense.setCategory(budget.getCategory());

        BudgetExpense saved = budgetExpenseRepository.save(expense);

        updateBudgetSpentAmount(budget.getId());

        return saved;
    }

    @Transactional
    public void deleteExpense(Long expenseId, Long operatorId) {
        BudgetExpense expense = budgetExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("支出记录不存在"));
        budgetExpenseRepository.delete(expense);
        updateBudgetSpentAmount(expense.getBudgetId());
    }

    private void updateBudgetSpentAmount(Long budgetId) {
        BigDecimal totalSpent = budgetExpenseRepository.getTotalSpentByBudgetId(budgetId);
        Budget budget = budgetRepository.findById(budgetId).orElseThrow();
        budget.setSpentAmount(totalSpent);
        budgetRepository.save(budget);
    }

    private void checkBudgetPermission(Long familyId, Long userId) {
        if (familyId == null) {
            return;
        }
        boolean isAdmin = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
                .map(m -> m.getRole() == FamilyRole.ADMIN)
                .orElse(false);
        if (!isAdmin) {
            throw new RuntimeException("无权限管理家庭预算，需要管理员权限");
        }
    }
}
