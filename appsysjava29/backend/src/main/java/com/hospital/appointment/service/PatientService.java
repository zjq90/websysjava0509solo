package com.hospital.appointment.service;

import com.hospital.appointment.common.BusinessException;
import com.hospital.appointment.entity.Patient;
import com.hospital.appointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 就诊人服务类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getUserPatients(Long userId) {
        return patientRepository.findByUserIdOrderByIsDefaultDesc(userId);
    }

    public Patient getPatientById(Long userId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("就诊人不存在"));
        
        if (!patient.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该就诊人");
        }
        
        return patient;
    }

    @Transactional
    public Patient addPatient(Long userId, Patient patient) {
        patient.setUserId(userId);
        
        if (patient.getIsDefault() != null && patient.getIsDefault() == 1) {
            List<Patient> existing = patientRepository.findByUserIdOrderByIsDefaultDesc(userId);
            for (Patient p : existing) {
                if (p.getIsDefault() != null && p.getIsDefault() == 1) {
                    p.setIsDefault(0);
                    patientRepository.save(p);
                }
            }
        }
        
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long userId, Long patientId, Patient updatePatient) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("就诊人不存在"));
        
        if (!patient.getUserId().equals(userId)) {
            throw new BusinessException("无权修改该就诊人");
        }
        
        if (updatePatient.getPatientName() != null) {
            patient.setPatientName(updatePatient.getPatientName());
        }
        if (updatePatient.getPhone() != null) {
            patient.setPhone(updatePatient.getPhone());
        }
        if (updatePatient.getIdCard() != null) {
            patient.setIdCard(updatePatient.getIdCard());
        }
        if (updatePatient.getGender() != null) {
            patient.setGender(updatePatient.getGender());
        }
        if (updatePatient.getBirthday() != null) {
            patient.setBirthday(updatePatient.getBirthday());
        }
        if (updatePatient.getRelation() != null) {
            patient.setRelation(updatePatient.getRelation());
        }
        if (updatePatient.getInsuranceNo() != null) {
            patient.setInsuranceNo(updatePatient.getInsuranceNo());
        }
        if (updatePatient.getAllergyHistory() != null) {
            patient.setAllergyHistory(updatePatient.getAllergyHistory());
        }
        
        if (updatePatient.getIsDefault() != null && updatePatient.getIsDefault() == 1) {
            List<Patient> existing = patientRepository.findByUserIdOrderByIsDefaultDesc(userId);
            for (Patient p : existing) {
                if (!p.getId().equals(patientId) && p.getIsDefault() != null && p.getIsDefault() == 1) {
                    p.setIsDefault(0);
                    patientRepository.save(p);
                }
            }
            patient.setIsDefault(1);
        }
        
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long userId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("就诊人不存在"));
        
        if (!patient.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该就诊人");
        }
        
        patientRepository.delete(patient);
    }

    @Transactional
    public Patient setDefaultPatient(Long userId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("就诊人不存在"));
        
        if (!patient.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该就诊人");
        }
        
        List<Patient> existing = patientRepository.findByUserIdOrderByIsDefaultDesc(userId);
        for (Patient p : existing) {
            if (p.getIsDefault() != null && p.getIsDefault() == 1) {
                p.setIsDefault(0);
                patientRepository.save(p);
            }
        }
        
        patient.setIsDefault(1);
        return patientRepository.save(patient);
    }
}
