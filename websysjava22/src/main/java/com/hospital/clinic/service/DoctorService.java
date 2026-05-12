package com.hospital.clinic.service;

import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 医生Service
 * 医生业务逻辑处理
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * 查询所有医生
     */
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    /**
     * 根据ID查询医生
     */
    public Doctor findById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.orElse(null);
    }

    /**
     * 根据医生工号查询
     */
    public Doctor findByDoctorNo(String doctorNo) {
        return doctorRepository.findByDoctorNo(doctorNo);
    }

    /**
     * 根据姓名模糊查询
     */
    public List<Doctor> findByName(String name) {
        return doctorRepository.findByNameContaining(name);
    }

    /**
     * 根据状态查询
     */
    public List<Doctor> findByStatus(Integer status) {
        return doctorRepository.findByStatus(status);
    }

    /**
     * 新增医生
     */
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    /**
     * 更新医生
     */
    public Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    /**
     * 删除医生
     */
    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }
}
