package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.dto.DoctorVO;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.repository.DepartmentRepository;
import com.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医生控制器
 * 处理医生相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/public/doctors")
@Tag(name = "医生管理", description = "医生相关接口")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 获取医生列表
     * 支持按科室筛选
     */
    @GetMapping
    @Operation(summary = "获取医生列表", description = "获取所有医生或指定科室的医生列表")
    public Result<List<DoctorVO>> getDoctors(
            @RequestParam(required = false) Long departmentId) {
        List<Doctor> doctors;
        if (departmentId != null) {
            doctors = doctorService.getDoctorsByDeptId(departmentId);
        } else {
            doctors = doctorService.getRecommendedDoctors();
        }
        List<DoctorVO> voList = doctors.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 根据科室获取医生列表
     */
    @GetMapping("/department/{deptId}")
    @Operation(summary = "获取科室医生", description = "根据科室ID获取医生列表")
    public Result<List<DoctorVO>> getDoctorsByDeptId(@PathVariable Long deptId) {
        List<Doctor> doctors = doctorService.getDoctorsByDeptId(deptId);
        List<DoctorVO> voList = doctors.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 获取推荐医生
     */
    @GetMapping("/recommended")
    @Operation(summary = "获取推荐医生", description = "获取系统推荐的医生列表")
    public Result<List<DoctorVO>> getRecommendedDoctors() {
        List<Doctor> doctors = doctorService.getRecommendedDoctors();
        List<DoctorVO> voList = doctors.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 获取医生详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取医生详情", description = "根据ID获取医生详细信息")
    public Result<DoctorVO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return Result.success(convertToVO(doctor));
    }

    /**
     * 获取替代医生
     */
    @GetMapping("/alternatives")
    @Operation(summary = "获取替代医生", description = "医生停诊时推荐的同科室医生")
    public Result<List<DoctorVO>> getAlternativeDoctors(
            @RequestParam Long deptId,
            @RequestParam Long excludeDoctorId) {
        List<Doctor> doctors = doctorService.getAlternativeDoctors(deptId, excludeDoctorId);
        List<DoctorVO> voList = doctors.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 将Doctor转换为DoctorVO
     */
    private DoctorVO convertToVO(Doctor doctor) {
        DoctorVO vo = new DoctorVO();
        vo.setId(doctor.getId());
        vo.setDeptId(doctor.getDeptId());
        vo.setName(doctor.getDoctorName());
        vo.setDoctorName(doctor.getDoctorName());
        vo.setTitle(doctor.getTitle());
        vo.setEducation(doctor.getEducation());
        vo.setExperienceYears(doctor.getExperienceYears());
        vo.setSpecialties(doctor.getSpecialty());
        vo.setSpecialty(doctor.getSpecialty());
        vo.setIntroduction(doctor.getIntroduction());
        vo.setFee(doctor.getRegistrationFee());
        vo.setRegistrationFee(doctor.getRegistrationFee());
        vo.setAvatar(doctor.getAvatar());
        vo.setRating(doctor.getRating());
        vo.setConsultationCount(doctor.getConsultationCount());
        vo.setIsRecommended(doctor.getIsRecommended());
        vo.setStatus(doctor.getStatus());
        
        Department dept = departmentRepository.findById(doctor.getDeptId()).orElse(null);
        if (dept != null) {
            vo.setDepartmentName(dept.getDeptName());
        }
        
        return vo;
    }
}
