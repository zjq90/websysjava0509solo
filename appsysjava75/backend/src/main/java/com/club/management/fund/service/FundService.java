package com.club.management.fund.service;

import com.club.management.common.exception.BusinessException;
import com.club.management.common.result.PageResult;
import com.club.management.fund.entity.FundRecord;
import com.club.management.fund.entity.FundStatistics;
import com.club.management.fund.entity.Reimbursement;
import com.club.management.fund.repository.FundRecordRepository;
import com.club.management.fund.repository.FundStatisticsRepository;
import com.club.management.fund.repository.ReimbursementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 经费管理Service
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FundService {

    private final FundRecordRepository fundRecordRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final FundStatisticsRepository fundStatisticsRepository;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public PageResult<FundRecord> getFundRecordList(Long clubId, Integer type, Integer pageNum, Integer pageSize) {
        log.info("获取经费记录列表，社团ID：{}，类型：{}", clubId, type);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<FundRecord> page;
        if (type != null) {
            page = fundRecordRepository.findByClubIdAndTypeOrderByCreateTimeDesc(clubId, type, pageable);
        } else {
            page = fundRecordRepository.findByClubIdOrderByCreateTimeDesc(clubId, pageable);
        }
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public FundRecord addFundRecord(FundRecord record) {
        log.info("添加经费记录，社团ID：{}，类型：{}，金额：{}", record.getClubId(), record.getType(), record.getAmount());
        record.setRecordNo(generateRecordNo());

        Double totalIncome = fundRecordRepository.sumIncomeByClubId(record.getClubId());
        Double totalExpense = fundRecordRepository.sumExpenseByClubId(record.getClubId());
        Double currentBalance = totalIncome - totalExpense;

        if (record.getType() == 1) {
            record.setBalance(currentBalance + record.getAmount());
        } else {
            record.setBalance(currentBalance - record.getAmount());
        }

        FundRecord saved = fundRecordRepository.save(record);
        log.info("经费记录添加成功，记录ID：{}，记录编号：{}", saved.getId(), saved.getRecordNo());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public FundRecord updateFundRecord(FundRecord record) {
        log.info("更新经费记录，记录ID：{}", record.getId());
        FundRecord existing = fundRecordRepository.findById(record.getId())
                .orElseThrow(() -> new BusinessException("记录不存在"));
        record.setRecordNo(existing.getRecordNo());
        FundRecord saved = fundRecordRepository.save(record);
        log.info("经费记录更新成功，记录ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFundRecord(Long id) {
        log.info("删除经费记录，记录ID：{}", id);
        FundRecord record = fundRecordRepository.findById(id)
                .orElseThrow(() -> new BusinessException("记录不存在"));
        record.setIsDeleted(1);
        fundRecordRepository.save(record);
    }

    public FundRecord getFundRecordDetail(Long id) {
        log.info("获取经费记录详情，记录ID：{}", id);
        return fundRecordRepository.findById(id)
                .orElseThrow(() -> new BusinessException("记录不存在"));
    }

    public Map<String, Object> getFundSummary(Long clubId) {
        log.info("获取经费汇总信息，社团ID：{}", clubId);
        Map<String, Object> summary = new HashMap<>();
        Double totalIncome = fundRecordRepository.sumIncomeByClubId(clubId);
        Double totalExpense = fundRecordRepository.sumExpenseByClubId(clubId);
        FundRecord lastRecord = fundRecordRepository.findTopByClubIdOrderByIdDesc(clubId);

        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", lastRecord != null ? lastRecord.getBalance() : 0);
        summary.put("netIncome", totalIncome - totalExpense);
        return summary;
    }

    public PageResult<Reimbursement> getReimbursementList(Long clubId, Integer status, Integer pageNum, Integer pageSize) {
        log.info("获取报销申请列表，社团ID：{}，状态：{}", clubId, status);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Reimbursement> page;
        if (status != null) {
            page = reimbursementRepository.findByClubIdAndStatusOrderByCreateTimeDesc(clubId, status, pageable);
        } else {
            page = reimbursementRepository.findByClubIdOrderByCreateTimeDesc(clubId, pageable);
        }
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public PageResult<Reimbursement> getMyReimbursementList(Long applicantId, Integer pageNum, Integer pageSize) {
        log.info("获取我的报销申请列表，申请人ID：{}", applicantId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Reimbursement> page = reimbursementRepository.findByApplicantIdOrderByCreateTimeDesc(applicantId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public Reimbursement submitReimbursement(Reimbursement reimbursement) {
        log.info("提交报销申请，申请人ID：{}，金额：{}", reimbursement.getApplicantId(), reimbursement.getAmount());
        reimbursement.setReimburseNo(generateReimburseNo());
        reimbursement.setStatus(0);
        reimbursement.setCurrentNode(1);
        Reimbursement saved = reimbursementRepository.save(reimbursement);
        log.info("报销申请提交成功，申请ID：{}，报销单号：{}", saved.getId(), saved.getReimburseNo());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public Reimbursement auditReimbursement(Long id, Integer status, Long auditorId, String auditorName, String auditOpinion) {
        log.info("审批报销申请，申请ID：{}，状态：{}，审批人：{}", id, status, auditorName);
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("申请不存在"));

        reimbursement.setStatus(status);
        reimbursement.setAuditorId(auditorId);
        reimbursement.setAuditorName(auditorName);
        reimbursement.setAuditTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reimbursement.setAuditOpinion(auditOpinion);

        if (status == 1) {
            FundRecord record = new FundRecord();
            record.setRecordNo(generateRecordNo());
            record.setClubId(reimbursement.getClubId());
            record.setClubName(reimbursement.getClubName());
            record.setType(0);
            record.setAmount(reimbursement.getAmount());
            record.setCategory(reimbursement.getCategory());
            record.setCategoryName(reimbursement.getCategoryName());
            record.setOccurDate(reimbursement.getOccurDate());
            record.setSummary(reimbursement.getReason());
            record.setActivityId(reimbursement.getActivityId());
            record.setActivityName(reimbursement.getActivityName());
            record.setHandlerId(reimbursement.getApplicantId());
            record.setHandlerName(reimbursement.getApplicantName());
            record.setVouchers(reimbursement.getVouchers());
            record.setRemark("报销审批通过");
            record.setStatus(1);
            record.setAuditorId(auditorId);
            record.setAuditorName(auditorName);
            record.setAuditTime(reimbursement.getAuditTime());

            FundRecord savedRecord = addFundRecord(record);
            reimbursement.setFundRecordId(savedRecord.getId());
        }

        Reimbursement saved = reimbursementRepository.save(reimbursement);
        log.info("报销申请审批完成，申请ID：{}，状态：{}", saved.getId(), status);
        return saved;
    }

    public Reimbursement getReimbursementDetail(Long id) {
        log.info("获取报销申请详情，申请ID：{}", id);
        return reimbursementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("申请不存在"));
    }

    public FundStatistics getFundStatistics(Long clubId, Integer statisticsType, String period) {
        log.info("获取经费统计报表，社团ID：{}，统计类型：{}，周期：{}", clubId, statisticsType, period);
        FundStatistics statistics = fundStatisticsRepository.findByClubIdAndStatisticsTypeAndPeriod(clubId, statisticsType, period);
        if (statistics == null) {
            statistics = generateFundStatistics(clubId, statisticsType, period);
        }
        return statistics;
    }

    public List<FundStatistics> getFundStatisticsList(Long clubId, Integer statisticsType) {
        log.info("获取经费统计报表列表，社团ID：{}，统计类型：{}", clubId, statisticsType);
        return fundStatisticsRepository.findByClubIdAndStatisticsTypeOrderByPeriodDesc(clubId, statisticsType);
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    @Transactional(rollbackFor = Exception.class)
    public void generateMonthlyStatistics() {
        log.info("开始生成月度经费统计报表");
        List<Long> clubIds = List.of(1L, 2L, 3L);
        String lastMonth = LocalDate.now().minusMonths(1).format(MONTH_FORMATTER);
        for (Long clubId : clubIds) {
            try {
                generateFundStatistics(clubId, 2, lastMonth);
            } catch (Exception e) {
                log.error("生成社团{}月度统计报表失败", clubId, e);
            }
        }
        log.info("月度经费统计报表生成完成");
    }

    @Transactional(rollbackFor = Exception.class)
    public FundStatistics generateFundStatistics(Long clubId, Integer statisticsType, String period) {
        log.info("生成经费统计报表，社团ID：{}，统计类型：{}，周期：{}", clubId, statisticsType, period);

        LocalDateTime startTime;
        LocalDateTime endTime;

        if (statisticsType == 2) {
            LocalDate monthDate = LocalDate.parse(period + "-01", DATE_FORMATTER);
            startTime = monthDate.atStartOfDay();
            endTime = monthDate.plusMonths(1).atStartOfDay();
        } else {
            return null;
        }

        FundStatistics statistics = new FundStatistics();
        statistics.setClubId(clubId);
        statistics.setStatisticsType(statisticsType);
        statistics.setPeriod(period);

        Double totalIncome = 0.0;
        Double totalExpense = 0.0;

        List<Object[]> incomeStats = fundRecordRepository.sumByCategoryAndTimeRange(clubId, 1, startTime, endTime);
        List<Object[]> expenseStats = fundRecordRepository.sumByCategoryAndTimeRange(clubId, 0, startTime, endTime);

        Map<String, Double> incomeCategoryMap = new HashMap<>();
        String[] incomeCategories = {"会费", "赞助", "拨款", "其他收入"};
        for (Object[] stat : incomeStats) {
            Integer category = (Integer) stat[0];
            Double amount = (Double) stat[1];
            totalIncome += amount;
            String categoryName = category < incomeCategories.length ? incomeCategories[category] : "其他";
            incomeCategoryMap.put(categoryName, amount);
        }
        for (String category : incomeCategories) {
            incomeCategoryMap.putIfAbsent(category, 0.0);
        }

        Map<String, Double> expenseCategoryMap = new HashMap<>();
        String[] expenseCategories = {"活动物料", "场地费", "宣传费用", "差旅费用", "其他支出"};
        for (Object[] stat : expenseStats) {
            Integer category = (Integer) stat[0];
            Double amount = (Double) stat[1];
            totalExpense += amount;
            int idx = category - 10;
            String categoryName = idx >= 0 && idx < expenseCategories.length ? expenseCategories[idx] : "其他";
            expenseCategoryMap.put(categoryName, amount);
        }
        for (String category : expenseCategories) {
            expenseCategoryMap.putIfAbsent(category, 0.0);
        }

        statistics.setTotalIncome(totalIncome);
        statistics.setTotalExpense(totalExpense);
        statistics.setNetIncome(totalIncome - totalExpense);
        statistics.setOpeningBalance(0.0);
        statistics.setClosingBalance(totalIncome - totalExpense);
        statistics.setIncomeCount(incomeStats.size());
        statistics.setExpenseCount(expenseStats.size());
        statistics.setMaxIncome(incomeStats.stream().mapToDouble(s -> (Double) s[1]).max().orElse(0));
        statistics.setMaxExpense(expenseStats.stream().mapToDouble(s -> (Double) s[1]).max().orElse(0));

        try {
            statistics.setIncomeCategoryStats(objectMapper.writeValueAsString(incomeCategoryMap));
            statistics.setExpenseCategoryStats(objectMapper.writeValueAsString(expenseCategoryMap));
        } catch (JsonProcessingException e) {
            log.error("序列化统计数据失败", e);
        }

        FundStatistics existing = fundStatisticsRepository.findByClubIdAndStatisticsTypeAndPeriod(clubId, statisticsType, period);
        if (existing != null) {
            statistics.setId(existing.getId());
        }

        return fundStatisticsRepository.save(statistics);
    }

    private String generateRecordNo() {
        return "FR" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String generateReimburseNo() {
        return "RE" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
