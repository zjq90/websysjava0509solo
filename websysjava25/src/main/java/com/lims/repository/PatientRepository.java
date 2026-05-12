package com.lims.repository;

import com.lims.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * 根据患者编号查询
     */
    Patient findByPatientNo(String patientNo);

    /**
     * 根据患者姓名模糊查询
     */
    List<Patient> findByPatientNameContaining(String patientName);

    /**
     * 根据状态查询
     */
    List<Patient> findByStatus(String status);
}
