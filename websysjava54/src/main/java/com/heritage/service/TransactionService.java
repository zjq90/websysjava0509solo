package com.heritage.service;

import com.heritage.entity.MonitoringRule;
import com.heritage.entity.Transaction;
import com.heritage.entity.User;
import com.heritage.enums.AuditStatus;
import com.heritage.repository.MonitoringRuleRepository;
import com.heritage.repository.TransactionRepository;
import com.heritage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonitoringRuleRepository monitoringRuleRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        if (transaction.getTransactionNo() == null) {
            transaction.setTransactionNo("TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findAbnormalTransactions() {
        return transactionRepository.findByIsAbnormalTrue();
    }

    public List<Transaction> findFrozenTransactions() {
        return transactionRepository.findByFundsFrozenTrue();
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTransactionNo("TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Transaction saved = transactionRepository.save(transaction);
        checkAbnormal(saved.getId());
        return saved;
    }

    @Transactional
    public void checkAbnormal(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("交易不存在"));

        List<MonitoringRule> rules = monitoringRuleRepository.findByEnabledTrue();

        for (MonitoringRule rule : rules) {
            if ("FREQUENCY".equals(rule.getRuleType())) {
                LocalDateTime startTime = LocalDateTime.now().minusMinutes(rule.getFrequencyMinutes());
                Long count = transactionRepository.countByUserIdAndCreateTimeAfter(transaction.getBuyerId(), startTime);
                if (count >= rule.getFrequencyThreshold()) {
                    transaction.setIsAbnormal(true);
                    transaction.setAbnormalReason("高频交易警告：" + rule.getFrequencyMinutes() + "分钟内交易" + count + "次");
                    break;
                }
            } else if ("PRICE_DEVIATION".equals(rule.getRuleType())) {
                if (transaction.getEstimatedValue() != null && transaction.getEstimatedValue().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal deviation = transaction.getAmount()
                            .subtract(transaction.getEstimatedValue())
                            .abs()
                            .multiply(new BigDecimal(100))
                            .divide(transaction.getEstimatedValue(), 2, RoundingMode.HALF_UP);
                    if (deviation.compareTo(rule.getPriceDeviationThreshold()) > 0) {
                        transaction.setIsAbnormal(true);
                        transaction.setAbnormalReason("价格偏离警告：偏离估值" + deviation + "%");
                        break;
                    }
                }
            } else if ("LOW_CREDIT".equals(rule.getRuleType())) {
                User buyer = userRepository.findById(transaction.getBuyerId()).orElse(null);
                User seller = userRepository.findById(transaction.getSellerId()).orElse(null);
                if ((buyer != null && buyer.getCreditScore() < rule.getLowCreditScoreThreshold())
                        || (seller != null && seller.getCreditScore() < rule.getLowCreditScoreThreshold())) {
                    transaction.setIsAbnormal(true);
                    transaction.setAbnormalReason("低信用分交易：买家或卖家信用分低于" + rule.getLowCreditScoreThreshold());
                    break;
                }
            }
        }
        transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction requestFreeze(Long transactionId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("交易不存在"));
        transaction.setFundsFrozen(true);
        transaction.setFreezeReason(reason);
        transaction.setFreezeAuditStatus(AuditStatus.PENDING);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction auditFreeze(Long transactionId, AuditStatus status, String remark, Long auditorId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("交易不存在"));
        transaction.setFreezeAuditStatus(status);
        transaction.setFreezeAuditRemark(remark);
        transaction.setFreezeAuditorId(auditorId);
        transaction.setFreezeAuditTime(LocalDateTime.now());
        if (status == AuditStatus.REJECTED) {
            transaction.setFundsFrozen(false);
            transaction.setFreezeReason(null);
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> findRecentTransactions(int minutes) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(minutes);
        return transactionRepository.findByCreateTimeAfterOrderByCreateTimeDesc(startTime);
    }
}
