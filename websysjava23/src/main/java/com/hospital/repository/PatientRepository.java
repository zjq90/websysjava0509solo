package com.hospital.repository;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByPatientNo(String patientNo);

    List<Patient> findByNameContaining(String name);

    List<Patient> findByStatus(String status);

    @Query("SELECT p FROM Patient p WHERE p.name LIKE %?1% OR p.patientNo LIKE %?1% OR p.phone LIKE %?1%")
    List<Patient> search(String keyword);
}
