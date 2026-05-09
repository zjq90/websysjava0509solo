package com.agricultural.service;

import com.agricultural.dto.CostSummaryDTO;
import com.agricultural.entity.CostRecord;
import com.agricultural.entity.enums.CostCategory;
import com.agricultural.repository.CostRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 成本管理服务类
 * 多角度核算成本：育种成本、田间投入、加工费用、包装物流、管理费用
 */
@Service
public class CostService {

    private final CostRecordRepository costRecordRepository;

    public CostService(CostRecordRepository costRecordRepository) {
        this.costRecordRepository = costRecordRepository;
    }

    /**
     * 查询所有成本记录
     */
    public List<CostRecord> findAll() {
        return costRecordRepository.findAll();
    }

    /**
     * 分页查询所有成本记录
     */
    public Page<CostRecord> findAll(Pageable pageable) {
        return costRecordRepository.findAll(pageable);
    }

    /**
     * 根据成本类别查询
     */
    public List<CostRecord> findByCategory(CostCategory category) {
        return costRecordRepository.findByCategoryOrderByExpenseDateDesc(category);
    }

    /**
     * 根据品种ID查询
     */
    public List<CostRecord> findByVarietyId(Long varietyId) {
        return costRecordRepository.findByVarietyIdOrderByExpenseDateDesc(varietyId);
    }

    /**
     * 根据日期范围查询
     */
    public List<CostRecord> findByDateRange(LocalDate start, LocalDate end) {
        return costRecordRepository.findByExpenseDateBetweenOrderByExpenseDateDesc(start, end);
    }

    /**
     * 根据ID查询
     */
    public Optional<CostRecord> findById(Long id) {
        return costRecordRepository.findById(id);
    }

    /**
     * 根据成本单号查询
     */
    public Optional<CostRecord> findByCostNo(String costNo) {
        return costRecordRepository.findByCostNo(costNo);
    }

    /**
     * 生成成本单号
     */
    public String generateCostNo() {
        return "COST" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 保存成本记录
     */
    @Transactional
    public CostRecord save(CostRecord record) {
        if (record.getTotalAmount() == null || record.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
            record.calculateTotalAmount();
        }
        return costRecordRepository.save(record);
    }

    /**
     * 删除成本记录
     */
    @Transactional
    public void deleteById(Long id) {
        costRecordRepository.deleteById(id);
    }

    /**
     * 获取指定类别的总成本
     */
    public BigDecimal getTotalByCategory(CostCategory category) {
        return costRecordRepository.sumByCategory(category);
    }

    /**
     * 获取所有成本总额
     */
    public BigDecimal getTotalAll() {
        return costRecordRepository.sumAll();
    }

    /**
     * 获取成本汇总（按类别分组，带百分比）
     */
    public List<CostSummaryDTO> getCostSummary() {
        List<Object[]> results = costRecordRepository.sumGroupByCategory();
        BigDecimal totalAll = getTotalAll();
        List<CostSummaryDTO> summary = new ArrayList<>();
        
        for (Object[] row : results) {
            CostCategory category = (CostCategory) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            BigDecimal percentage = totalAll.compareTo(BigDecimal.ZERO) > 0 
                ? amount.multiply(new BigDecimal("100")).divide(totalAll, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            
            summary.add(new CostSummaryDTO(
                category.name(),
                category.getDescription(),
                amount,
                percentage
            ));
        }
        
        return summary;
    }
}
