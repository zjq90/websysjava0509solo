package com.inventory.service;

import com.inventory.entity.Alert;
import com.inventory.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 预警服务层
 * 功能：处理预警记录的增删改查业务逻辑
 */
@Service
@Transactional
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    public Optional<Alert> findById(Long id) {
        return alertRepository.findById(id);
    }

    public List<Alert> findByStatus(String status) {
        return alertRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    public List<Alert> findByType(String type) {
        return alertRepository.findByAlertType(type);
    }

    public List<Alert> findActiveAlerts() {
        return alertRepository.findByStatusOrderByCreateTimeDesc("ACTIVE");
    }

    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    public boolean resolveAlert(Long id) {
        Alert alert = alertRepository.findById(id).orElse(null);
        if (alert == null) {
            return false;
        }
        alert.setStatus("RESOLVED");
        alertRepository.save(alert);
        return true;
    }

    public void deleteById(Long id) {
        alertRepository.deleteById(id);
    }
}
