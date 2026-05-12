package com.lims.service;

import com.lims.entity.Patient;
import com.lims.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 患者业务逻辑层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * 查询所有患者
     */
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    /**
     * 根据ID查询患者
     */
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * 根据患者编号查询
     */
    public Patient findByPatientNo(String patientNo) {
        return patientRepository.findByPatientNo(patientNo);
    }

    /**
     * 根据患者姓名模糊查询
     */
    public List<Patient> findByPatientNameContaining(String name) {
        return patientRepository.findByPatientNameContaining(name);
    }

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
        return patientRepository.save(patient);
    }

    /**
     * 删除患者
     */
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }
}
