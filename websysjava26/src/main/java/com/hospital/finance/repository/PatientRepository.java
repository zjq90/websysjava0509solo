package com.hospital.finance.repository;

import com.hospital.finance.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者信息数据访问层
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * 根据患者编号查询
     */
    Patient findByPatientNo(String patientNo);

    /**
     * 根据姓名模糊查询
     */
    List<Patient> findByNameContaining(String name);

    /**
     * 根据身份证号查询
     */
    Patient findByIdCard(String idCard);

    /**
     * 查询有医保的患者
     */
    List<Patient> findByHasInsuranceTrue();
}