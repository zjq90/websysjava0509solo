package com.culturalrelic.service;

import com.culturalrelic.entity.AuditRule;
import com.culturalrelic.repository.AuditRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 审核规则业务逻辑层
 */
@Service
public class AuditRuleService {

    @Autowired
    private AuditRuleRepository auditRuleRepository;

    public AuditRule save(AuditRule rule) {
        return auditRuleRepository.save(rule);
    }

    public Optional<AuditRule> findById(Long id) {
        return auditRuleRepository.findById(id);
    }

    public Page<AuditRule> findAll(Pageable pageable) {
        return auditRuleRepository.findAll(pageable);
    }

    public List<AuditRule> findAll() {
        return auditRuleRepository.findAll();
    }

    public AuditRule update(AuditRule rule) {
        return auditRuleRepository.save(rule);
    }

    @Transactional
    public boolean delete(Long id) {
        int rows = auditRuleRepository.logicDelete(id);
        return rows > 0;
    }

    public AuditRule findByRuleNo(String ruleNo) {
        return auditRuleRepository.findByRuleNo(ruleNo);
    }

    public List<AuditRule> findByRuleType(Integer ruleType) {
        return auditRuleRepository.findByRuleType(ruleType);
    }

    public List<AuditRule> findByStatus(Integer status) {
        return auditRuleRepository.findByStatus(status);
    }
}
