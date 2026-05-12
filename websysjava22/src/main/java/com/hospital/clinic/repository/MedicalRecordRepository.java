package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.MedicalRecord;
import com.hospital.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电子病历Repository
 * 电子病历数据访问接口
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    /**
     * 根据病历号查询
     */
    MedicalRecord findByRecordNo(String recordNo);

    /**
     * 根据患者查询病历
     */
    List<MedicalRecord> findByPatient(Patient patient);

    /**
     * 根据医生查询病历
     */
    List<MedicalRecord> findByDoctor(Doctor doctor);

    /**
     * 根据科室查询病历
     */
    List<MedicalRecord> findByDepartment(Department department);
}
