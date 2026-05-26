package com.personal.accounting.config;

import com.personal.accounting.entity.*;
import com.personal.accounting.entity.enums.CategoryType;
import com.personal.accounting.entity.enums.TransactionType;
import com.personal.accounting.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据初始化类
 * 应用启动时自动初始化基础数据和测试数据
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final SavingGoalRepository savingGoalRepository;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) {
        log.info("开始初始化数据...");
        
        if (categoryRepository.count() == 0) {
            initCategories();
        }
        
        if (accountRepository.count() == 0) {
            initAccounts();
        }
        
        if (transactionRepository.count() == 0) {
            initTestTransactions();
        }
        
        if (budgetRepository.count() == 0) {
            initBudgets();
        }
        
        if (savingGoalRepository.count() == 0) {
            initSavingGoals();
        }
        
        log.info("数据初始化完成！");
    }

    /**
     * 初始化分类数据
     */
    private void initCategories() {
        log.info("初始化分类数据...");
        
        List<Category> categories = new ArrayList<>();
        
        // 支出分类
        categories.add(createCategory("餐饮", CategoryType.EXPENSE, "#FF6B6B", "food", "餐饮相关支出", 1));
        categories.add(createCategory("交通", CategoryType.EXPENSE, "#4ECDC4", "car", "交通出行支出", 2));
        categories.add(createCategory("购物", CategoryType.EXPENSE, "#FFD93D", "shopping", "购物消费支出", 3));
        categories.add(createCategory("娱乐", CategoryType.EXPENSE, "#95E1D3", "game", "娱乐休闲支出", 4));
        categories.add(createCategory("居住", CategoryType.EXPENSE, "#A66CFF", "home", "房租水电等支出", 5));
        categories.add(createCategory("医疗", CategoryType.EXPENSE, "#F38181", "medical", "医疗健康支出", 6));
        categories.add(createCategory("教育", CategoryType.EXPENSE, "#FCBAD3", "book", "学习培训支出", 7));
        categories.add(createCategory("其他支出", CategoryType.EXPENSE, "#AAAAAA", "other", "其他杂项支出", 8));
        
        // 收入分类
        categories.add(createCategory("工资", CategoryType.INCOME, "#6BCB77", "salary", "工资收入", 1));
        categories.add(createCategory("奖金", CategoryType.INCOME, "#4D96FF", "bonus", "奖金收入", 2));
        categories.add(createCategory("投资收益", CategoryType.INCOME, "#FFB84C", "investment", "投资收益", 3));
        categories.add(createCategory("其他收入", CategoryType.INCOME, "#9DB2BF", "other", "其他收入", 4));
        
        categoryRepository.saveAll(categories);
        log.info("分类数据初始化完成，共 {} 条", categories.size());
    }

    /**
     * 初始化账户数据
     */
    private void initAccounts() {
        log.info("初始化账户数据...");
        
        List<Account> accounts = new ArrayList<>();
        
        accounts.add(createAccount("工商银行储蓄卡", "储蓄卡", new BigDecimal("15000.00"), "#4ECDC4", "工资卡"));
        accounts.add(createAccount("招商银行信用卡", "信用卡", new BigDecimal("3000.00"), "#FF6B6B", "日常消费"));
        accounts.add(createAccount("支付宝余额", "电子钱包", new BigDecimal("2500.00"), "#1677ff", "支付宝"));
        accounts.add(createAccount("微信钱包", "电子钱包", new BigDecimal("1800.00"), "#07C160", "微信支付"));
        accounts.add(createAccount("现金", "现金", new BigDecimal("500.00"), "#FFD93D", "手头现金"));
        
        accountRepository.saveAll(accounts);
        log.info("账户数据初始化完成，共 {} 条", accounts.size());
    }

    /**
     * 初始化测试交易数据
     */
    @Transactional
    public void initTestTransactions() {
        log.info("初始化测试交易数据...");
        
        List<Category> expenseCategories = categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType.EXPENSE);
        List<Category> incomeCategories = categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType.INCOME);
        List<Account> accounts = accountRepository.findByEnabledTrueOrderBySortOrderAsc();
        
        List<Transaction> transactions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // 生成最近90天的交易数据
        for (int daysAgo = 89; daysAgo >= 0; daysAgo--) {
            LocalDateTime dayTime = now.minusDays(daysAgo);
            int dayOfWeek = dayTime.getDayOfWeek().getValue();
            
            // 每天生成3-8笔支出
            int expenseCount = 3 + random.nextInt(6);
            for (int i = 0; i < expenseCount; i++) {
                Transaction t = createRandomExpense(dayTime, expenseCategories, accounts);
                transactions.add(t);
            }
            
            // 每周末增加一笔娱乐支出
            if (dayOfWeek >= 6) {
                Transaction entertainment = createTransaction(
                        TransactionType.EXPENSE,
                        new BigDecimal(50 + random.nextInt(200)),
                        expenseCategories.get(3),
                        accounts.get(random.nextInt(accounts.size())),
                        dayTime.withHour(19 + random.nextInt(3)),
                        "周末娱乐",
                        "娱乐",
                        "周末休闲"
                );
                transactions.add(entertainment);
            }
            
            // 每月10号发工资
            if (dayTime.getDayOfMonth() == 10) {
                Transaction salary = createTransaction(
                        TransactionType.INCOME,
                        new BigDecimal("15000.00"),
                        incomeCategories.get(0),
                        accounts.get(0),
                        dayTime.withHour(9),
                        "月薪",
                        "工资",
                        "月度工资"
                );
                transactions.add(salary);
            }
            
            // 每月15号还信用卡
            if (dayTime.getDayOfMonth() == 15) {
                Transaction rent = createTransaction(
                        TransactionType.EXPENSE,
                        new BigDecimal("3000.00"),
                        expenseCategories.get(4),
                        accounts.get(0),
                        dayTime.withHour(10),
                        "房租",
                        "房租",
                        "每月房租"
                );
                transactions.add(rent);
            }
            
            // 每月随机有1-2笔大额支出（异常交易）
            if (random.nextInt(15) == 0) {
                Transaction bigExpense = createTransaction(
                        TransactionType.EXPENSE,
                        new BigDecimal(2000 + random.nextInt(3000)),
                        expenseCategories.get(2),
                        accounts.get(random.nextInt(accounts.size())),
                        dayTime.withHour(14 + random.nextInt(4)),
                        "大额购物",
                        "购物",
                        "大额消费"
                );
                transactions.add(bigExpense);
            }
        }
        
        transactionRepository.saveAll(transactions);
        log.info("测试交易数据初始化完成，共 {} 条", transactions.size());
    }

    /**
     * 初始化预算数据
     */
    private void initBudgets() {
        log.info("初始化预算数据...");
        
        List<Category> expenseCategories = categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType.EXPENSE);
        YearMonth currentMonth = YearMonth.now();
        
        List<Budget> budgets = new ArrayList<>();
        
        budgets.add(createBudget(expenseCategories.get(0), currentMonth, new BigDecimal("2000.00")));
        budgets.add(createBudget(expenseCategories.get(1), currentMonth, new BigDecimal("800.00")));
        budgets.add(createBudget(expenseCategories.get(2), currentMonth, new BigDecimal("1500.00")));
        budgets.add(createBudget(expenseCategories.get(3), currentMonth, new BigDecimal("500.00")));
        budgets.add(createBudget(expenseCategories.get(4), currentMonth, new BigDecimal("3500.00")));
        budgets.add(createBudget(expenseCategories.get(5), currentMonth, new BigDecimal("300.00")));
        
        budgetRepository.saveAll(budgets);
        log.info("预算数据初始化完成，共 {} 条", budgets.size());
    }

    /**
     * 初始化储蓄目标数据
     */
    private void initSavingGoals() {
        log.info("初始化储蓄目标数据...");
        
        List<SavingGoal> goals = new ArrayList<>();
        
        SavingGoal travel = new SavingGoal();
        travel.setName("旅行基金");
        travel.setDescription("存够5万元去欧洲旅行");
        travel.setTargetAmount(new BigDecimal("50000.00"));
        travel.setCurrentAmount(new BigDecimal("15000.00"));
        travel.setTargetDate(LocalDateTime.now().plusMonths(12).toLocalDate());
        travel.setColor("#FFD93D");
        travel.setIcon("travel");
        goals.add(travel);
        
        SavingGoal emergency = new SavingGoal();
        emergency.setName("应急储备金");
        emergency.setDescription("存够6个月生活费作为应急资金");
        emergency.setTargetAmount(new BigDecimal("30000.00"));
        emergency.setCurrentAmount(new BigDecimal("20000.00"));
        emergency.setTargetDate(LocalDateTime.now().plusMonths(6).toLocalDate());
        emergency.setColor("#4ECDC4");
        emergency.setIcon("safe");
        goals.add(emergency);
        
        SavingGoal phone = new SavingGoal();
        phone.setName("新手机");
        phone.setDescription("换新手机");
        phone.setTargetAmount(new BigDecimal("6000.00"));
        phone.setCurrentAmount(new BigDecimal("4500.00"));
        phone.setTargetDate(LocalDateTime.now().plusMonths(2).toLocalDate());
        phone.setColor("#A66CFF");
        phone.setIcon("phone");
        goals.add(phone);
        
        savingGoalRepository.saveAll(goals);
        log.info("储蓄目标数据初始化完成，共 {} 条", goals.size());
    }

    private Category createCategory(String name, CategoryType type, String color, String icon, String desc, int sort) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setColor(color);
        category.setIcon(icon);
        category.setDescription(desc);
        category.setSortOrder(sort);
        category.setEnabled(true);
        return category;
    }

    private Account createAccount(String name, String type, BigDecimal balance, String color, String notes) {
        Account account = new Account();
        account.setName(name);
        account.setAccountType(type);
        account.setInitialBalance(balance);
        account.setCurrentBalance(balance);
        account.setColor(color);
        account.setNotes(notes);
        account.setIncludeInTotal(true);
        account.setSortOrder(0);
        account.setEnabled(true);
        return account;
    }

    private Budget createBudget(Category category, YearMonth month, BigDecimal amount) {
        Budget budget = new Budget();
        budget.setCategory(category);
        budget.setBudgetMonth(month);
        budget.setBudgetAmount(amount);
        budget.setRolloverRemaining(false);
        return budget;
    }

    private Transaction createRandomExpense(LocalDateTime dayTime, List<Category> categories, List<Account> accounts) {
        int hour = 7 + random.nextInt(14);
        Category category = categories.get(random.nextInt(Math.min(5, categories.size())));
        BigDecimal amount;
        
        // 根据分类生成合理金额
        if (category.getName().equals("餐饮")) {
            amount = new BigDecimal(15 + random.nextInt(80));
        } else if (category.getName().equals("交通")) {
            amount = new BigDecimal(5 + random.nextInt(50));
        } else if (category.getName().equals("购物")) {
            amount = new BigDecimal(50 + random.nextInt(300));
        } else {
            amount = new BigDecimal(20 + random.nextInt(150));
        }
        
        String[] descriptions = {"午餐", "晚餐", "早餐", "超市购物", "地铁", "打车", "咖啡", "零食"};
        String description = descriptions[random.nextInt(descriptions.length)];
        
        return createTransaction(
                TransactionType.EXPENSE,
                amount,
                category,
                accounts.get(random.nextInt(accounts.size())),
                dayTime.withHour(hour).withMinute(random.nextInt(60)),
                description,
                description,
                null
        );
    }

    private Transaction createTransaction(TransactionType type, BigDecimal amount, Category category,
                                          Account account, LocalDateTime time, String description,
                                          String tags, String notes) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setTransactionTime(time);
        transaction.setDescription(description);
        transaction.setTags(tags);
        transaction.setNotes(notes);
        transaction.setMerchant(description);
        transaction.setIsAbnormal(false);
        return transaction;
    }
}
