package com.hospital.repository;

import com.hospital.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 病历Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    MedicalRecord findByRecordNo(String recordNo);

    List<MedicalRecord> findByHospitalizationId(Long hospitalizationId);

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByRecordType(String recordType);

    List<MedicalRecord> findByStatus(String status);

    @Query("SELECT m FROM MedicalRecord m WHERE m.hospitalizationId = ?1 AND m.recordType = ?2")
    List<MedicalRecord> findByHospitalizationIdAndRecordType(Long hospitalizationId, String recordType);
}
