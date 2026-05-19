package com.pethospital.repository;

import com.pethospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医院数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    List<Hospital> findByIs24HoursTrueAndEmergencyServiceTrueAndEnabledTrue();
}
