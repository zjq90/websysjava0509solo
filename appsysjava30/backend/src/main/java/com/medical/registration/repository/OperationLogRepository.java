package com.medical.registration.repository;

import com.medical.registration.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    Page<OperationLog> findByUserIdOrderByOperationTimeDesc(Long userId, Pageable pageable);
    
    @Query("delete from OperationLog o where o.operationTime < :time")
    void deleteByOperationTimeBefore(@Param("time") LocalDateTime time);
}
