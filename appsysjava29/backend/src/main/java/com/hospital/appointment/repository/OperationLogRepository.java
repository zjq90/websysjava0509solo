package com.hospital.appointment.repository;

import com.hospital.appointment.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    List<OperationLog> findByUserIdOrderByOperationTimeDesc(Long userId);
    
    @Query("SELECT o FROM OperationLog o WHERE o.operationTime >= :startTime " +
           "AND o.operationTime <= :endTime ORDER BY o.operationTime DESC")
    List<OperationLog> findByTimeRange(@Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT o FROM OperationLog o WHERE o.operationTime >= :sixMonthsAgo " +
           "ORDER BY o.operationTime DESC")
    List<OperationLog> findLastSixMonths(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);
}
