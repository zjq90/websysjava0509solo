package com.petclinic.service;

import com.petclinic.entity.CreditRecord;
import com.petclinic.entity.Doctor;
import com.petclinic.entity.PetOwner;
import com.petclinic.repository.CreditRecordRepository;
import com.petclinic.repository.DoctorRepository;
import com.petclinic.repository.PetOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户管理Service
 */
@Service
public class UserService {

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CreditRecordRepository creditRecordRepository;

    // ==================== 宠物主人管理 ====================

    public List<PetOwner> getAllPetOwners() {
        return petOwnerRepository.findAll();
    }

    public PetOwner getPetOwnerById(Long id) {
        return petOwnerRepository.findById(id).orElse(null);
    }

    public List<PetOwner> getPetOwnersByRealNameStatus(Integer status) {
        return petOwnerRepository.findByRealNameStatus(status);
    }

    @Transactional
    public PetOwner createPetOwner(PetOwner petOwner) {
        petOwner.setCreditScore(100);
        petOwner.setRealNameStatus(0);
        return petOwnerRepository.save(petOwner);
    }

    @Transactional
    public PetOwner updatePetOwner(Long id, PetOwner petOwner) {
        Optional<PetOwner> existing = petOwnerRepository.findById(id);
        if (existing.isPresent()) {
            PetOwner owner = existing.get();
            owner.setName(petOwner.getName());
            owner.setPhone(petOwner.getPhone());
            owner.setEmail(petOwner.getEmail());
            owner.setAddress(petOwner.getAddress());
            owner.setStatus(petOwner.getStatus());
            return petOwnerRepository.save(owner);
        }
        return null;
    }

    /**
     * 实名认证审核
     */
    @Transactional
    public PetOwner auditRealName(Long id, Integer status, Long operatorId) {
        Optional<PetOwner> existing = petOwnerRepository.findById(id);
        if (existing.isPresent()) {
            PetOwner owner = existing.get();
            owner.setRealNameStatus(status);
            // 审核通过加分
            if (status == 2) {
                owner.setCreditScore(owner.getCreditScore() + 10);
                CreditRecord record = new CreditRecord();
                record.setUserId(id);
                record.setUserType(1);
                record.setScoreChange(10);
                record.setCurrentScore(owner.getCreditScore());
                record.setReason("实名认证通过");
                record.setOperatorId(operatorId);
                creditRecordRepository.save(record);
            }
            return petOwnerRepository.save(owner);
        }
        return null;
    }

    @Transactional
    public void deletePetOwner(Long id) {
        petOwnerRepository.deleteById(id);
    }

    // ==================== 医生审核管理 ====================

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public List<Doctor> getDoctorsByAuditStatus(Integer auditStatus) {
        return doctorRepository.findByAuditStatus(auditStatus);
    }

    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        doctor.setAuditStatus(0);
        doctor.setStatus(1);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Optional<Doctor> existing = doctorRepository.findById(id);
        if (existing.isPresent()) {
            Doctor d = existing.get();
            d.setName(doctor.getName());
            d.setPhone(doctor.getPhone());
            d.setDepartmentId(doctor.getDepartmentId());
            d.setTitle(doctor.getTitle());
            d.setSpecialty(doctor.getSpecialty());
            d.setLicenseNumber(doctor.getLicenseNumber());
            if (doctor.getLicenseImageUrl() != null) {
                d.setLicenseImageUrl(doctor.getLicenseImageUrl());
            }
            return doctorRepository.save(d);
        }
        return null;
    }

    /**
     * 医生资质审核
     */
    @Transactional
    public Doctor auditDoctor(Long id, Integer auditStatus, String auditRemark, Long operatorId) {
        Optional<Doctor> existing = doctorRepository.findById(id);
        if (existing.isPresent()) {
            Doctor doctor = existing.get();
            doctor.setAuditStatus(auditStatus);
            doctor.setAuditRemark(auditRemark);
            doctor.setAuditTime(LocalDateTime.now());
            doctor.setAuditUserId(operatorId);
            return doctorRepository.save(doctor);
        }
        return null;
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // ==================== 信用分管理 ====================

    /**
     * 调整用户信用分
     */
    @Transactional
    public CreditRecord adjustCreditScore(Long userId, Integer userType, Integer scoreChange, 
                                           String reason, Long businessId, Long operatorId) {
        // 查询当前用户
        Integer currentScore = 0;
        if (userType == 1) {
            PetOwner owner = petOwnerRepository.findById(userId).orElse(null);
            if (owner == null) return null;
            currentScore = owner.getCreditScore() + scoreChange;
            owner.setCreditScore(currentScore);
            petOwnerRepository.save(owner);
        } else if (userType == 2) {
            // 医生信用分逻辑
        }

        CreditRecord record = new CreditRecord();
        record.setUserId(userId);
        record.setUserType(userType);
        record.setScoreChange(scoreChange);
        record.setCurrentScore(currentScore);
        record.setReason(reason);
        record.setBusinessId(businessId);
        record.setOperatorId(operatorId);
        return creditRecordRepository.save(record);
    }

    /**
     * 获取用户信用记录
     */
    public List<CreditRecord> getCreditRecords(Long userId, Integer userType) {
        return creditRecordRepository.findByUserIdAndUserTypeOrderByCreateTimeDesc(userId, userType);
    }
}
