package com.appsys.repository;

import com.appsys.entity.FaultReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FaultReportRepository extends JpaRepository<FaultReport, Long> {

    List<FaultReport> findByUserIdOrderByCreateTimeDesc(Long userId);

    Optional<FaultReport> findByReportNo(String reportNo);
}
