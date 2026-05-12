package com.hospital.repository;

import com.hospital.entity.Hospitalization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 住院记录Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long> {

    Hospitalization findByHospitalNo(String hospitalNo);

    List<Hospitalization> findByPatientId(Long patientId);

    List<Hospitalization> findByStatus(String status);

    List<Hospitalization> findByBedId(Long bedId);

    @Query("SELECT h FROM Hospitalization h WHERE h.status = '住院中'")
    List<Hospitalization> findAllHospitalized();

    @Query("SELECT h FROM Hospitalization h WHERE h.patientId = ?1 AND h.status = '住院中'")
    Hospitalization findCurrentHospitalizationByPatientId(Long patientId);
}
