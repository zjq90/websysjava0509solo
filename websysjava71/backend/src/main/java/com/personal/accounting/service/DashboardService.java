package com.personal.accounting.service;

import com.personal.accounting.dto.*;
import com.personal.accounting.entity.Transaction;
import com.personal.accounting.entity.enums.TransactionType;
import com.personal.accounting.repository.AccountRepository;
import com.personal.accounting.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 仪表盘服务类
 * 提供仪表盘概览、数据分析等功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * 获取仪表盘概览数据
     * 包含今日/本周/本月收支总额、支出占比、账户余额趋势
     */
    @Transactional(readOnly = true)
    public DashboardOverviewDTO getDashboardOverview() {
        log.debug("获取仪表盘概览数据");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        
        // 今日时间范围
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(LocalTime.MAX);
        
        // 本周时间范围（周一到周日）
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        // 本月时间范围
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());
        
        DashboardOverviewDTO overview = new DashboardOverviewDTO();
        
        // 今日收支
        overview.setTodayIncome(getTotalAmount(TransactionType.INCOME, todayStart, todayEnd));
        overview.setTodayExpense(getTotalAmount(TransactionType.EXPENSE, todayStart, todayEnd));
        
        // 本周收支
        overview.setWeekIncome(getTotalAmount(TransactionType.INCOME, weekStart.atStartOfDay(), weekEnd.atTime(LocalTime.MAX)));
        overview.setWeekExpense(getTotalAmount(TransactionType.EXPENSE, weekStart.atStartOfDay(), weekEnd.atTime(LocalTime.MAX)));
        
        // 本月收支
        overview.setMonthIncome(getTotalAmount(TransactionType.INCOME, monthStart.atStartOfDay(), monthEnd.atTime(LocalTime.MAX)));
        overview.setMonthExpense(getTotalAmount(TransactionType.EXPENSE, monthStart.atStartOfDay(), monthEnd.atTime(LocalTime.MAX)));
        
        // 总资产
        overview.setTotalAssets(accountRepository.calculateTotalAssets());
        
        // 支出分类占比（本月）
        overview.setCategoryExpenseRatio(getCategoryExpenseRatio(monthStart.atStartOfDay(), monthEnd.atTime(LocalTime.MAX)));
        
        // 账户余额趋势（最近30天）
        overview.setBalanceTrend(getBalanceTrend(30));
        
        return overview;
    }

    /**
     * 获取指定时间范围内的总金额
     */
    private BigDecimal getTotalAmount(TransactionType type, LocalDateTime start, LocalDateTime end) {
        BigDecimal amount = transactionRepository.sumAmountByTypeAndTimeBetween(type, start, end);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    /**
     * 获取支出分类占比
     */
    @Transactional(readOnly = true)
    public List<CategoryExpenseDTO> getCategoryExpenseRatio(LocalDateTime start, LocalDateTime end) {
        List<Object[]> results = transactionRepository.sumExpenseGroupByCategory(start, end);
        BigDecimal totalExpense = results.stream()
                .map(row -> (BigDecimal) row[3])
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<CategoryExpenseDTO> ratioList = new ArrayList<>();
        for (Object[] row : results) {
            CategoryExpenseDTO dto = new CategoryExpenseDTO();
            dto.setCategoryId((Long) row[0]);
            dto.setCategoryName((String) row[1]);
            dto.setColor((String) row[2]);
            BigDecimal amount = (BigDecimal) row[3];
            dto.setAmount(amount);
            
            if (totalExpense.compareTo(BigDecimal.ZERO) > 0) {
                dto.setRatio(amount.doubleValue() / totalExpense.doubleValue() * 100);
            } else {
                dto.setRatio(0.0);
            }
            ratioList.add(dto);
        }
        return ratioList;
    }

    /**
     * 获取账户余额趋势
     */
    @Transactional(readOnly = true)
    public List<BalanceTrendDTO> getBalanceTrend(int days) {
        List<BalanceTrendDTO> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        BigDecimal currentBalance = accountRepository.calculateTotalAssets();
        
        // 获取历史交易计算每日余额
        Map<LocalDate, BigDecimal> dailyNet = new HashMap<>();
        LocalDateTime startDate = today.minusDays(days).atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository
                .findByTransactionTimeBetweenOrderByTransactionTimeDesc(startDate, endDate);
        
        for (Transaction t : transactions) {
            LocalDate date = t.getTransactionTime().toLocalDate();
            BigDecimal amount = t.getType() == TransactionType.INCOME ? 
                    t.getAmount() : t.getAmount().negate();
            dailyNet.merge(date, amount, BigDecimal::add);
        }
        
        // 从今天往前推计算余额
        BigDecimal balance = currentBalance;
        for (int i = 0; i < days; i++) {
            LocalDate date = today.minusDays(i);
            trend.add(new BalanceTrendDTO(date, balance));
            BigDecimal net = dailyNet.getOrDefault(date, BigDecimal.ZERO);
            balance = balance.subtract(net);
        }
        
        Collections.reverse(trend);
        return trend;
    }

    /**
     * 获取月度消费趋势
     */
    @Transactional(readOnly = true)
    public List<MonthlyTrendDTO> getMonthlyTrend(int months) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.minusMonths(months - 1).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime end = today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        
        List<Object[]> results = transactionRepository.sumByMonth(start, end);
        
        Map<String, MonthlyTrendDTO> monthMap = new LinkedHashMap<>();
        
        // 初始化所有月份
        for (int i = months - 1; i >= 0; i--) {
            LocalDate date = today.minusMonths(i);
            String key = date.getYear() + "-" + date.getMonthValue();
            MonthlyTrendDTO dto = new MonthlyTrendDTO();
            dto.setYear(date.getYear());
            dto.setMonth(date.getMonthValue());
            dto.setIncome(BigDecimal.ZERO);
            dto.setExpense(BigDecimal.ZERO);
            dto.setNetIncome(BigDecimal.ZERO);
            monthMap.put(key, dto);
        }
        
        // 填充数据
        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            Integer month = (Integer) row[1];
            TransactionType type = (TransactionType) row[2];
            BigDecimal amount = (BigDecimal) row[3];
            
            String key = year + "-" + month;
            MonthlyTrendDTO dto = monthMap.get(key);
            if (dto != null) {
                if (TransactionType.INCOME.equals(type)) {
                    dto.setIncome(amount);
                } else {
                    dto.setExpense(amount);
                }
                dto.setNetIncome(dto.getIncome().subtract(dto.getExpense()));
            }
        }
        
        return new ArrayList<>(monthMap.values());
    }

    /**
     * 获取异常交易检测结果
     */
    @Transactional(readOnly = true)
    public List<Transaction> getAbnormalTransactions() {
        return transactionRepository.findByIsAbnormalTrueOrderByTransactionTimeDesc();
    }

    /**
     * 检测并标记异常交易
     * 规则：单笔支出 > 月均支出的3倍
     */
    @Transactional
    public void detectAbnormalTransactions() {
        log.info("开始检测异常交易");
        
        LocalDate today = LocalDate.now();
        LocalDateTime threeMonthsAgo = today.minusMonths(3).atStartOfDay();
        LocalDateTime now = today.atTime(LocalTime.MAX);
        
        // 计算月均支出
        BigDecimal totalExpense = transactionRepository.sumAmountByTypeAndTimeBetween(
                TransactionType.EXPENSE, threeMonthsAgo, now);
        BigDecimal monthlyAverage = totalExpense.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal threshold = monthlyAverage.multiply(BigDecimal.valueOf(3));
        
        log.info("月均支出: {}, 异常阈值: {}", monthlyAverage, threshold);
        
        // 查询所有交易并检测
        List<Transaction> transactions = transactionRepository
                .findByTransactionTimeBetweenOrderByTransactionTimeDesc(threeMonthsAgo, now);
        
        int abnormalCount = 0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE && t.getAmount().compareTo(threshold) > 0) {
                t.setIsAbnormal(true);
                t.setAbnormalReason("超过月均支出3倍（月均: " + monthlyAverage + "）");
                abnormalCount++;
            }
        }
        transactionRepository.saveAll(transactions);
        
        log.info("检测完成，发现 {} 笔异常交易", abnormalCount);
    }
}
