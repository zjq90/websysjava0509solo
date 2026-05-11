package com.medical.appointment.service;

import com.medical.appointment.entity.Patient;
import com.medical.appointment.entity.User;
import com.medical.appointment.repository.PatientRepository;
import com.medical.appointment.security.Sm4CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private Sm4CryptoUtil cryptoUtil;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    @Transactional
    public Patient addPatient(Long userId, Patient patient) {
        User user = userService.findById(userId);
        
        int count = patientRepository.countByUser_Id(userId);
        if (count >= 5) {
            throw new RuntimeException("最多只能添加5位就诊人");
        }

        if (patient.getIdCardNumber() != null) {
            if (patientRepository.existsByUser_IdAndIdCardNumber(userId, patient.getIdCardNumber())) {
                throw new RuntimeException("该身份证号已存在");
            }
            patient.setIdCardEncrypted(cryptoUtil.encrypt(patient.getIdCardNumber()));
        }

        patient.setUser(user);
        patient.setIsPrimary(count == 0);
        patient.setVerificationStatus(Patient.VerificationStatus.UNVERIFIED);

        Patient saved = patientRepository.save(patient);
        auditLogService.logAddPatient(userId, user.getPhone(), saved.getId());
        
        return saved;
    }

    @Transactional
    public Patient updatePatient(Long userId, Long patientId, Patient patient) {
        Patient existing = patientRepository.findByIdAndUser_Id(patientId, userId)
                .orElseThrow(() -> new RuntimeException("就诊人不存在或无权限"));

        if (patient.getRealName() != null) {
            existing.setRealName(patient.getRealName());
        }
        if (patient.getGender() != null) {
            existing.setGender(patient.getGender());
        }
        if (patient.getBirthDate() != null) {
            existing.setBirthDate(patient.getBirthDate());
        }
        if (patient.getPhone() != null) {
            existing.setPhone(patient.getPhone());
        }
        if (patient.getRelation() != null) {
            existing.setRelation(patient.getRelation());
        }

        Patient saved = patientRepository.save(existing);
        User user = userService.findById(userId);
        auditLogService.logUpdatePatient(userId, user.getPhone(), patientId);
        
        return saved;
    }

    @Transactional
    public void deletePatient(Long userId, Long patientId) {
        Patient patient = patientRepository.findByIdAndUser_Id(patientId, userId)
                .orElseThrow(() -> new RuntimeException("就诊人不存在或无权限"));
        
        patientRepository.delete(patient);
        
        User user = userService.findById(userId);
        auditLogService.logDeletePatient(userId, user.getPhone(), patientId);
    }

    @Transactional
    public Patient verifyIdentity(Long userId, Long patientId, String idCardNumber, String name) {
        Patient patient = patientRepository.findByIdAndUser_Id(patientId, userId)
                .orElseThrow(() -> new RuntimeException("就诊人不存在或无权限"));

        patient.setIdCardNumber(idCardNumber);
        patient.setIdCardEncrypted(cryptoUtil.encrypt(idCardNumber));
        patient.setRealName(name);
        patient.setVerificationStatus(Patient.VerificationStatus.VERIFIED);
        patient.setVerificationTime(LocalDateTime.now());

        Patient saved = patientRepository.save(patient);
        
        User user = userService.findById(userId);
        auditLogService.logVerifyIdentity(userId, user.getPhone(), patientId);
        
        return saved;
    }

    @Transactional
    public void setPrimaryPatient(Long userId, Long patientId) {
        List<Patient> patients = patientRepository.findByUser_IdOrderByIsPrimaryDescCreatedAtAsc(userId);
        for (Patient p : patients) {
            if (p.getId().equals(patientId)) {
                p.setIsPrimary(true);
            } else {
                p.setIsPrimary(false);
            }
        }
        patientRepository.saveAll(patients);
    }

    public List<Map<String, Object>> getPatientsByUserId(Long userId) {
        List<Patient> patients = patientRepository.findByUser_IdOrderByIsPrimaryDescCreatedAtAsc(userId);
        return patients.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public Map<String, Object> getPatientById(Long userId, Long patientId) {
        Patient patient = patientRepository.findByIdAndUser_Id(patientId, userId)
                .orElseThrow(() -> new RuntimeException("就诊人不存在或无权限"));
        return convertToVO(patient);
    }

    private Map<String, Object> convertToVO(Patient patient) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", patient.getId());
        vo.put("realName", patient.getRealName());
        vo.put("gender", patient.getGender());
        vo.put("birthDate", patient.getBirthDate());
        vo.put("phone", patient.getPhone());
        vo.put("idCardNumber", patient.getIdCardNumber() != null ? 
                cryptoUtil.maskIdCard(patient.getIdCardNumber()) : null);
        vo.put("verificationStatus", patient.getVerificationStatus());
        vo.put("verificationTime", patient.getVerificationTime());
        vo.put("relation", patient.getRelation());
        vo.put("isPrimary", patient.getIsPrimary());
        vo.put("createdAt", patient.getCreatedAt());
        return vo;
    }
}
