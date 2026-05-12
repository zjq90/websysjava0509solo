package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者Repository
 * 患者数据访问接口
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * 根据患者编号查询
     */
    Patient findByPatientNo(String patientNo);

    /**
     * 根据身份证号查询
     */
    Patient findByIdCard(String idCard);

    /**
     * 根据姓名模糊查询
     */
    List<Patient> findByNameContaining(String name);

    /**
     * 根据手机号查询
     */
    Patient findByPhone(String phone);

    /**
     * 根据状态查询
     */
    List<Patient> findByStatus(Integer status);
}
