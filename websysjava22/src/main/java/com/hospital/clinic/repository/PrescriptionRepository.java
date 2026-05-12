package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处方Repository
 * 处方数据访问接口
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    /**
     * 根据处方号查询
     */
    Prescription findByPrescriptionNo(String prescriptionNo);

    /**
     * 根据患者查询处方
     */
    List<Prescription> findByPatient(Patient patient);

    /**
     * 根据医生查询处方
     */
    List<Prescription> findByDoctor(Doctor doctor);

    /**
     * 根据科室查询处方
     */
    List<Prescription> findByDepartment(Department department);

    /**
     * 根据状态查询处方
     */
    List<Prescription> findByStatus(String status);
}
