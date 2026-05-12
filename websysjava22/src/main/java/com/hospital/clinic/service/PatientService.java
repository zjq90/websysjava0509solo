package com.hospital.clinic.service;

import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 患者Service
 * 患者业务逻辑处理
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
    public Patient findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElse(null);
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
     * 新增患者
     */
    public Patient save(Patient patient) {
        if (patient.getPatientNo() == null) {
            patient.setPatientNo("P" + System.currentTimeMillis());
        }
        if (patient.getStatus() == null) {
            patient.setStatus(1);
        }
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
