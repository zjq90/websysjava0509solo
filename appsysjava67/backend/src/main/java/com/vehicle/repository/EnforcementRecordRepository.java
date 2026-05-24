package com.vehicle.repository;

import com.vehicle.entity.EnforcementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnforcementRecordRepository extends JpaRepository<EnforcementRecord, Long> {

    List<EnforcementRecord> findByPlateNumberOrderByEnforcementTimeDesc(String plateNumber);

    List<EnforcementRecord> findByOfficerNameOrderByEnforcementTimeDesc(String officerName);

    List<EnforcementRecord> findBySyncedFalse();
}
