package com.hospital.repository;

import com.hospital.entity.VitalSigns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生命体征Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface VitalSignsRepository extends JpaRepository<VitalSigns, Long> {

    List<VitalSigns> findByHospitalizationId(Long hospitalizationId);

    List<VitalSigns> findByPatientId(Long patientId);

    @Query("SELECT v FROM VitalSigns v WHERE v.hospitalizationId = ?1 ORDER BY v.recordTime DESC")
    List<VitalSigns> findByHospitalizationIdOrderByRecordTimeDesc(Long hospitalizationId);

    @Query("SELECT v FROM VitalSigns v WHERE v.patientId = ?1 ORDER BY v.recordTime DESC")
    List<VitalSigns> findByPatientIdOrderByRecordTimeDesc(Long patientId);
}
