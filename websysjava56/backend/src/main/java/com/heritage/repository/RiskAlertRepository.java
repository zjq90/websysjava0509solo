package com.heritage.repository;

import com.heritage.entity.RiskAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskAlertRepository extends JpaRepository<RiskAlert, Long>, JpaSpecificationExecutor<RiskAlert> {

    Page<RiskAlert> findByStatus(String status, Pageable pageable);

    Page<RiskAlert> findByAlertLevel(String alertLevel, Pageable pageable);

    List<RiskAlert> findByTargetTypeAndTargetId(String targetType, Long targetId);

    long countByStatus(String status);
}
