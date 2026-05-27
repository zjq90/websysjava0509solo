package com.club.service;

import com.club.dto.PageQuery;
import com.club.entity.FinanceRecord;
import com.club.enums.FinanceType;
import com.club.repository.FinanceRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务管理服务类
 * 包含财务记录CRUD、异常标记、调查等业务逻辑
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Service
public class FinanceService {

    private final FinanceRecordRepository financeRecordRepository;

    private static final BigDecimal ABNORMAL_THRESHOLD = new BigDecimal("3000");

    public FinanceService(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    /**
     * 分页查询财务记录
     */
    public Page<FinanceRecord> getFinanceRecordList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<FinanceRecord> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("purpose"), "%" + query.getKeyword() + "%"));
            }
            if (StringUtils.hasText(query.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), FinanceType.valueOf(query.getType())));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return financeRecordRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询财务记录
     */
    public FinanceRecord getFinanceRecordById(Long id) {
        return financeRecordRepository.findById(id).orElse(null);
    }

    /**
     * 根据社团ID查询财务记录
     */
    public List<FinanceRecord> getFinanceRecordsByClubId(Long clubId) {
        return financeRecordRepository.findByClubId(clubId);
    }

    /**
     * 创建财务记录
     */
    public FinanceRecord createFinanceRecord(FinanceRecord record) {
        // 自动检查是否异常
        checkAbnormal(record);
        return financeRecordRepository.save(record);
    }

    /**
     * 更新财务记录
     */
    public FinanceRecord updateFinanceRecord(Long id, FinanceRecord record) {
        FinanceRecord existing = getFinanceRecordById(id);
        if (existing == null) {
            throw new RuntimeException("财务记录不存在");
        }
        record.setId(id);
        checkAbnormal(record);
        return financeRecordRepository.save(record);
    }

    /**
     * 删除财务记录
     */
    public void deleteFinanceRecord(Long id) {
        financeRecordRepository.deleteById(id);
    }

    /**
     * 检查是否为异常支出
     */
    public void checkAbnormal(FinanceRecord record) {
        boolean abnormal = false;
        StringBuilder note = new StringBuilder();
        
        // 大额支出无凭证
        if (record.getType() == FinanceType.EXPENSE 
            && record.getAmount().compareTo(ABNORMAL_THRESHOLD) > 0 
            && !record.getHasVoucher()) {
            abnormal = true;
            note.append("大额支出（").append(record.getAmount()).append("元）无凭证；");
        }
        
        record.setAbnormal(abnormal);
        if (abnormal) {
            record.setAbnormalNote(note.toString());
        }
    }

    /**
     * 标记异常记录进行调查
     */
    public FinanceRecord markForInvestigation(Long id) {
        FinanceRecord record = getFinanceRecordById(id);
        if (record == null) {
            throw new RuntimeException("财务记录不存在");
        }
        record.setMarkedForInvestigation(true);
        record.setInvestigationStatus(1);
        return financeRecordRepository.save(record);
    }

    /**
     * 完成调查
     */
    public FinanceRecord completeInvestigation(Long id, String result) {
        FinanceRecord record = getFinanceRecordById(id);
        if (record == null) {
            throw new RuntimeException("财务记录不存在");
        }
        record.setInvestigationStatus(2);
        record.setInvestigationResult(result);
        return financeRecordRepository.save(record);
    }

    /**
     * 获取财务统计数据
     */
    public Map<String, Object> getFinanceStatistics() {
        Map<String, Object> stats = new HashMap<>();
        BigDecimal totalIncome = financeRecordRepository.sumTotalIncome();
        BigDecimal totalExpense = financeRecordRepository.sumTotalExpense();
        
        stats.put("totalRecords", financeRecordRepository.count());
        stats.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        stats.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        stats.put("balance", totalIncome != null && totalExpense != null 
            ? totalIncome.subtract(totalExpense) : BigDecimal.ZERO);
        stats.put("abnormalCount", financeRecordRepository.countByAbnormalTrue());
        stats.put("pendingInvestigation", financeRecordRepository.countByMarkedForInvestigationTrueAndInvestigationStatus(1));
        return stats;
    }

    /**
     * 获取异常财务记录
     */
    public List<FinanceRecord> getAbnormalRecords() {
        return financeRecordRepository.findByAbnormalTrue();
    }

    /**
     * 获取待调查记录
     */
    public List<FinanceRecord> getPendingInvestigationRecords() {
        return financeRecordRepository.findByMarkedForInvestigationTrue();
    }
}
