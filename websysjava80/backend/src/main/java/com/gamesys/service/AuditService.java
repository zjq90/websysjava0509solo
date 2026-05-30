package com.gamesys.service;

import com.gamesys.common.PageResult;
import com.gamesys.dto.AuditDTO;
import com.gamesys.entity.AuditLog;
import com.gamesys.entity.AutoAuditRule;

import java.util.List;

public interface AuditService {
    void audit(AuditDTO dto);
    void autoAudit(Long gameId);
    PageResult<AuditLog> getAuditLogs(Long gameId, Integer pageNum, Integer pageSize);
    List<AutoAuditRule> getAutoAuditRules();
    void saveAutoAuditRule(AutoAuditRule rule);
    void deleteAutoAuditRule(Long id);
    void updateAutoAuditRuleStatus(Long id, Integer status);
}
