package com.inventory.repository;

import com.inventory.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(String status);
    List<Alert> findByAlertType(String alertType);
    List<Alert> findByLevel(String level);
    List<Alert> findByStatusOrderByCreateTimeDesc(String status);
}
