package com.secondhand.repository;

import com.secondhand.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {

    List<OperationLog> findByOperatorId(Long operatorId);

    List<OperationLog> findByModule(String module);

    List<OperationLog> findByOperationTimeBetween(LocalDateTime start, LocalDateTime end);

    List<OperationLog> findByOperatorIdAndOperationTimeBetween(Long operatorId, LocalDateTime start, LocalDateTime end);

}