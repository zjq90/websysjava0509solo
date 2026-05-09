package com.agricultural.service;

import com.agricultural.entity.Finance;
import com.agricultural.entity.FinancePayment;
import com.agricultural.entity.enums.FinanceStatus;
import com.agricultural.entity.enums.FinanceType;
import com.agricultural.repository.FinancePaymentRepository;
import com.agricultural.repository.FinanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 财务账款管理服务类
 * 管理应收应付账款，支持收付款操作
 */
@Service
public class FinanceService {

    private final FinanceRepository financeRepository;
    private final FinancePaymentRepository paymentRepository;

    public FinanceService(FinanceRepository financeRepository,
                          FinancePaymentRepository paymentRepository) {
        this.financeRepository = financeRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * 查询所有财务账款
     */
    public List<Finance> findAll() {
        return financeRepository.findAll();
    }

    /**
     * 分页查询所有财务账款
     */
    public Page<Finance> findAll(Pageable pageable) {
        return financeRepository.findAll(pageable);
    }

    /**
     * 根据类型查询
     */
    public List<Finance> findByType(FinanceType type) {
        return financeRepository.findByFinanceTypeOrderByFinanceDateDesc(type);
    }

    /**
     * 根据状态查询
     */
    public List<Finance> findByStatus(FinanceStatus status) {
        return financeRepository.findByStatusOrderByFinanceDateDesc(status);
    }

    /**
     * 根据ID查询
     */
    public Optional<Finance> findById(Long id) {
        return financeRepository.findById(id);
    }

    /**
     * 根据财务单号查询
     */
    public Optional<Finance> findByFinanceNo(String financeNo) {
        return financeRepository.findByFinanceNo(financeNo);
    }

    /**
     * 根据客户ID查询
     */
    public List<Finance> findByCustomerId(Long customerId) {
        return financeRepository.findByCustomerIdOrderByFinanceDateDesc(customerId);
    }

    /**
     * 生成财务单号
     * 应收账款：AR + 年月日 + 序号
     * 应付账款：AP + 年月日 + 序号
     */
    public String generateFinanceNo(FinanceType type) {
        String prefix = type == FinanceType.RECEIVABLE ? "AR" : "AP";
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String searchPrefix = prefix + dateStr;
        
        String maxNo = financeRepository.findMaxFinanceNoByPrefix(searchPrefix);
        int sequence = 1;
        if (maxNo != null && maxNo.length() > searchPrefix.length()) {
            try {
                sequence = Integer.parseInt(maxNo.substring(searchPrefix.length())) + 1;
            } catch (NumberFormatException e) {
                sequence = 1;
            }
        }
        
        return searchPrefix + String.format("%03d", sequence);
    }

    /**
     * 生成付款单号
     */
    public String generatePaymentNo() {
        return "PAY" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 保存财务账款
     */
    @Transactional
    public Finance save(Finance finance) {
        finance.calculateRemainingAndStatus();
        return financeRepository.save(finance);
    }

    /**
     * 收款/付款操作
     * 更新已收/已付金额，自动计算剩余金额和状态
     */
    @Transactional
    public FinancePayment makePayment(Long financeId, FinancePayment payment) {
        return financeRepository.findById(financeId).map(finance -> {
            payment.setFinance(finance);
            payment.setPaymentNo(generatePaymentNo());
            FinancePayment savedPayment = paymentRepository.save(payment);
            
            BigDecimal totalPaid = paymentRepository.sumAmountByFinanceId(financeId);
            finance.setPaidAmount(totalPaid);
            finance.calculateRemainingAndStatus();
            financeRepository.save(finance);
            
            return savedPayment;
        }).orElse(null);
    }

    /**
     * 查询指定财务的所有收付款记录
     */
    public List<FinancePayment> findPaymentsByFinanceId(Long financeId) {
        return paymentRepository.findByFinanceIdOrderByPaymentDateDesc(financeId);
    }

    /**
     * 删除财务账款
     */
    @Transactional
    public void deleteById(Long id) {
        financeRepository.deleteById(id);
    }

    /**
     * 统计未结清账款数量
     */
    public long countUnsettled() {
        return financeRepository.countUnsettled();
    }

    /**
     * 获取应收账款总额（未结清）
     */
    public BigDecimal getTotalReceivable() {
        return financeRepository.sumRemainingAmountByType(FinanceType.RECEIVABLE);
    }

    /**
     * 获取应付账款总额（未结清）
     */
    public BigDecimal getTotalPayable() {
        return financeRepository.sumRemainingAmountByType(FinanceType.PAYABLE);
    }
}
