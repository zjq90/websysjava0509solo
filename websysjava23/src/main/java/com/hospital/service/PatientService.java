package com.hospital.service;

import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 患者Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient findByPatientNo(String patientNo) {
        return patientRepository.findByPatientNo(patientNo);
    }

    public List<Patient> search(String keyword) {
        return patientRepository.search(keyword);
    }

    @Transactional
    public Patient save(Patient patient) {
        if (patient.getId() == null) {
            String patientNo = "P" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            patient.setPatientNo(patientNo);
        }
        return patientRepository.save(patient);
    }

    @Transactional
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> findByStatus(String status) {
        return patientRepository.findByStatus(status);
    }
}
