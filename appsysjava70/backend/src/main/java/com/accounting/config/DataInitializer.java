package com.accounting.config;

import com.accounting.entity.Account;
import com.accounting.entity.Category;
import com.accounting.entity.Budget;
import com.accounting.enums.BillType;
import com.accounting.repository.AccountRepository;
import com.accounting.repository.CategoryRepository;
import com.accounting.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            initCategories();
        }
        if (accountRepository.count() == 0) {
            initAccounts();
        }
        if (budgetRepository.count() == 0) {
            initBudgets();
        }
    }

    private void initCategories() {
        log.info("初始化分类数据...");

        List<Category> expenseCategories = Arrays.asList(
                createCategory("餐饮", "🍔", BillType.EXPENSE, "食品饮料", 1),
                createCategory("交通", "🚗", BillType.EXPENSE, "交通出行", 2),
                createCategory("住房", "🏠", BillType.EXPENSE, "居家生活", 3),
                createCategory("购物", "🛒", BillType.EXPENSE, "消费购物", 4),
                createCategory("娱乐", "🎮", BillType.EXPENSE, "休闲娱乐", 5),
                createCategory("医疗", "💊", BillType.EXPENSE, "医疗健康", 6),
                createCategory("教育", "📚", BillType.EXPENSE, "学习教育", 7),
                createCategory("其他", "📦", BillType.EXPENSE, "其他支出", 8)
        );

        List<Category> incomeCategories = Arrays.asList(
                createCategory("工资", "💰", BillType.INCOME, "工作收入", 1),
                createCategory("奖金", "🎁", BillType.INCOME, "工作收入", 2),
                createCategory("红包", "🧧", BillType.INCOME, "其他收入", 3),
                createCategory("投资", "📈", BillType.INCOME, "理财收入", 4),
                createCategory("其他", "💵", BillType.INCOME, "其他收入", 5)
        );

        categoryRepository.saveAll(expenseCategories);
        categoryRepository.saveAll(incomeCategories);
        log.info("分类数据初始化完成，共{}条", expenseCategories.size() + incomeCategories.size());
    }

    private Category createCategory(String name, String icon, BillType type, String parentName, int sortOrder) {
        return Category.builder()
                .name(name)
                .icon(icon)
                .type(type)
                .parentName(parentName)
                .sortOrder(sortOrder)
                .build();
    }

    private void initAccounts() {
        log.info("初始化账户数据...");

        List<Account> accounts = Arrays.asList(
                createAccount("招商银行储蓄卡", "🏦", new BigDecimal("15000.00"), "储蓄卡", "工资卡", 1),
                createAccount("支付宝", "📱", new BigDecimal("3500.00"), "电子账户", "日常消费", 2),
                createAccount("微信钱包", "💬", new BigDecimal("800.00"), "电子账户", "零钱", 3),
                createAccount("现金", "💵", new BigDecimal("500.00"), "现金", "手头现金", 4)
        );

        accountRepository.saveAll(accounts);
        log.info("账户数据初始化完成，共{}条", accounts.size());
    }

    private Account createAccount(String name, String icon, BigDecimal balance, String type, String remark, int sortOrder) {
        return Account.builder()
                .name(name)
                .icon(icon)
                .balance(balance)
                .type(type)
                .remark(remark)
                .sortOrder(sortOrder)
                .build();
    }

    private void initBudgets() {
        log.info("初始化预算数据...");

        LocalDate today = LocalDate.now();
        LocalDateTime monthStart = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime monthEnd = today.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        Budget totalBudget = Budget.builder()
                .name(today.getYear() + "年" + today.getMonthValue() + "月总预算")
                .amount(new BigDecimal("5000.00"))
                .startDate(monthStart)
                .endDate(monthEnd)
                .remark("本月总支出预算")
                .build();

        budgetRepository.save(totalBudget);
        log.info("预算数据初始化完成");
    }
}
