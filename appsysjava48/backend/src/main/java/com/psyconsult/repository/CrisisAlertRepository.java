package com.psyconsult.repository;

import com.psyconsult.entity.CrisisAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrisisAlertRepository extends JpaRepository<CrisisAlert, Long> {

    List<CrisisAlert> findByStatus(String status);

    List<CrisisAlert> findByCounselorId(Long counselorId);

    List<CrisisAlert> findByUserId(Long userId);

    List<CrisisAlert> findByAlertLevel(String alertLevel);
}
