package com.hospital.finance.service;

import com.hospital.finance.entity.Patient;
import com.hospital.finance.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 患者信息服务层
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * 新增患者
     */
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * 更新患者
     */
    public Patient update(Patient patient) {
        patient.setUpdateTime(LocalDateTime.now());
        return patientRepository.save(patient);
    }

    /**
     * 根据ID查询
     */
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * 查询所有患者
     */
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    /**
     * 根据患者编号查询
     */
    public Patient findByPatientNo(String patientNo) {
        return patientRepository.findByPatientNo(patientNo);
    }

    /**
     * 根据姓名模糊查询
     */
    public List<Patient> findByName(String name) {
        return patientRepository.findByNameContaining(name);
    }

    /**
     * 删除患者
     */
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    /**
     * 查询有医保的患者
     */
    public List<Patient> findHasInsurance() {
        return patientRepository.findByHasInsuranceTrue();
    }
}