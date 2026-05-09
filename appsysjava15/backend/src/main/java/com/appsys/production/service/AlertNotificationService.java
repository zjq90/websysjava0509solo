package com.appsys.production.service;

import com.appsys.production.entity.AlertNotification;
import com.appsys.production.repository.AlertNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertNotificationService {

    @Autowired
    private AlertNotificationRepository alertRepository;

    public List<AlertNotification> getBySupervisorId(Long supervisorId) {
        return alertRepository.findBySupervisorIdOrderByCreateTimeDesc(supervisorId);
    }

    public List<AlertNotification> getByBatchId(Long batchId) {
        return alertRepository.findByBatchId(batchId);
    }

    @Transactional
    public AlertNotification markAsRead(Long id) {
        AlertNotification alert = alertRepository.findById(id).orElseThrow(() -> new RuntimeException("通知不存在"));
        alert.setStatus("READ");
        alert.setReadTime(LocalDateTime.now());
        return alertRepository.save(alert);
    }
}
