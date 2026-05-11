package com.hospital.appointment.service;

import com.hospital.appointment.common.BusinessException;
import com.hospital.appointment.entity.Department;
import com.hospital.appointment.entity.Doctor;
import com.hospital.appointment.repository.DepartmentRepository;
import com.hospital.appointment.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 医生服务类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByStatusOrderBySortOrderAsc(1);
        enrichDoctors(doctors);
        return doctors;
    }

    public List<Doctor> getDoctorsByConditions(Long deptId, String titleLevel, Integer isExpert) {
        List<Doctor> doctors = doctorRepository.findByConditions(1, deptId, titleLevel, isExpert);
        enrichDoctors(doctors);
        return doctors;
    }

    public Doctor getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("医生不存在"));
        enrichDoctor(doctor);
        return doctor;
    }

    public List<Doctor> getDoctorsByDept(Long deptId) {
        List<Doctor> doctors = doctorRepository.findByDeptIdAndStatusOrderBySortOrderAsc(deptId, 1);
        enrichDoctors(doctors);
        return doctors;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findByStatusOrderBySortOrderAsc(1);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("科室不存在"));
    }

    private void enrichDoctors(List<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            enrichDoctor(doctor);
        }
    }

    private void enrichDoctor(Doctor doctor) {
        if (doctor.getDeptId() != null) {
            Optional<Department> deptOpt = departmentRepository.findById(doctor.getDeptId());
            deptOpt.ifPresent(dept -> doctor.setDeptName(dept.getDeptName()));
        }
    }
}
