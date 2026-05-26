package com.accounting.service;

import com.accounting.dto.AbnormalExpenseDTO;
import com.accounting.dto.DashboardDTO;
import com.accounting.entity.Bill;
import com.accounting.entity.Budget;
import com.accounting.enums.BillType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BillService billService;
    private final AccountService accountService;
    private final BudgetService budgetService;

    @Cacheable(value = "dashboard", key = "'data'", unless = "#result == null")
    public DashboardDTO getDashboardData() {
        log.info("获取看板数据");
        LocalDate today = LocalDate.now();

        LocalDateTime dayStart = billService.getStartOfDay(today);
        LocalDateTime dayEnd = billService.getEndOfDay(today);
        LocalDateTime monthStart = billService.getStartOfMonth(today);
        LocalDateTime monthEnd = billService.getEndOfMonth(today);

        BigDecimal todayIncome = billService.calculateTotalByTypeAndDateRange(BillType.INCOME, dayStart, dayEnd);
        BigDecimal todayExpense = billService.calculateTotalByTypeAndDateRange(BillType.EXPENSE, dayStart, dayEnd);
        BigDecimal monthIncome = billService.calculateTotalByTypeAndDateRange(BillType.INCOME, monthStart, monthEnd);
        BigDecimal monthExpense = billService.calculateTotalByTypeAndDateRange(BillType.EXPENSE, monthStart, monthEnd);

        int daysInMonth = today.lengthOfMonth();
        int currentDay = today.getDayOfMonth();
        BigDecimal monthAverageExpense = currentDay > 0
                ? monthExpense.divide(BigDecimal.valueOf(currentDay), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal totalAssets = accountService.getTotalBalance();
        BigDecimal totalLiabilities = BigDecimal.ZERO;

        BigDecimal budgetUsagePercent = BigDecimal.ZERO;
        boolean budgetWarning = false;
        Optional<Budget> totalBudgetOpt = budgetService.getCurrentTotalBudget();
        if (totalBudgetOpt.isPresent()) {
            Budget totalBudget = totalBudgetOpt.get();
            budgetUsagePercent = budgetService.getBudgetUsage(totalBudget.getId());
            budgetWarning = budgetService.isBudgetWarning(totalBudget);
        }

        List<AbnormalExpenseDTO> abnormalExpenses = checkAbnormalExpenses(monthStart, monthEnd, monthAverageExpense);

        return DashboardDTO.builder()
                .totalAssets(totalAssets)
                .totalLiabilities(totalLiabilities)
                .todayIncome(todayIncome)
                .todayExpense(todayExpense)
                .todayBalance(todayIncome.subtract(todayExpense))
                .monthIncome(monthIncome)
                .monthExpense(monthExpense)
                .monthAverageExpense(monthAverageExpense)
                .budgetUsagePercent(budgetUsagePercent)
                .budgetWarning(budgetWarning)
                .abnormalExpenses(abnormalExpenses)
                .build();
    }

    public List<AbnormalExpenseDTO> checkAbnormalExpenses(LocalDateTime start, LocalDateTime end, BigDecimal averageExpense) {
        log.info("检查异常消费: 月均支出={}", averageExpense);
        List<AbnormalExpenseDTO> abnormalList = new ArrayList<>();

        if (averageExpense.compareTo(BigDecimal.ZERO) == 0) {
            return abnormalList;
        }

        List<Bill> bills = billService.getBillsByDateRange(start, end);
        BigDecimal threshold = averageExpense.multiply(new BigDecimal("3"));

        for (Bill bill : bills) {
            if (bill.getType() == BillType.EXPENSE && bill.getAmount().compareTo(threshold) > 0) {
                BigDecimal multiple = bill.getAmount().divide(averageExpense, 2, RoundingMode.HALF_UP);
                AbnormalExpenseDTO abnormal = AbnormalExpenseDTO.builder()
                        .billId(bill.getId())
                        .amount(bill.getAmount())
                        .categoryName(bill.getCategory().getName())
                        .merchant(bill.getMerchant())
                        .transactionTime(bill.getTransactionTime())
                        .averageExpense(averageExpense)
                        .multiple(multiple)
                        .build();
                abnormalList.add(abnormal);
                log.warn("发现异常消费: 金额={}, 月均={}, 倍数={}", bill.getAmount(), averageExpense, multiple);
            }
        }

        return abnormalList;
    }
}
