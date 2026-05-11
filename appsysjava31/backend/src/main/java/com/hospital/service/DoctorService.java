package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医生服务类
 * 处理医生信息管理业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * 根据科室ID获取医生列表
     * 
     * @param deptId 科室ID
     * @return 医生列表
     */
    public List<Doctor> getDoctorsByDeptId(Long deptId) {
        return doctorRepository.findByDeptIdAndStatusOrderByConsultationCountDesc(deptId, 1);
    }

    /**
     * 获取推荐医生
     * 
     * @return 推荐医生列表
     */
    public List<Doctor> getRecommendedDoctors() {
        return doctorRepository.findByIsRecommendedAndStatusOrderByRatingDesc(1, 1);
    }

    /**
     * 根据ID获取医生信息
     * 
     * @param id 医生ID
     * @return 医生信息
     */
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
    }

    /**
     * 根据用户ID获取医生信息
     * 
     * @param userId 用户ID
     * @return 医生信息
     */
    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
    }

    /**
     * 获取替代医生（医生停诊时推荐）
     * 
     * @param deptId 科室ID
     * @param excludeDoctorId 排除的医生ID
     * @return 替代医生列表
     */
    public List<Doctor> getAlternativeDoctors(Long deptId, Long excludeDoctorId) {
        return doctorRepository.findAlternativeDoctors(deptId, excludeDoctorId, 1);
    }
}
