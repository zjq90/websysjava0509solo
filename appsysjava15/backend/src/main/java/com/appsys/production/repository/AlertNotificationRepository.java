package com.appsys.production.repository;

import com.appsys.production.entity.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertNotificationRepository extends JpaRepository<AlertNotification, Long> {
    List<AlertNotification> findBySupervisorIdOrderByCreateTimeDesc(Long supervisorId);
    List<AlertNotification> findByBatchId(Long batchId);
}
