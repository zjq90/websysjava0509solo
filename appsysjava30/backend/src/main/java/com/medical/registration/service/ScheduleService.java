package com.medical.registration.service;

import com.medical.registration.dto.ScheduleVO;
import com.medical.registration.entity.Department;
import com.medical.registration.entity.Doctor;
import com.medical.registration.entity.Schedule;
import com.medical.registration.repository.DepartmentRepository;
import com.medical.registration.repository.DoctorRepository;
import com.medical.registration.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findByStatusOrderBySortOrderAsc(1);
    }
    
    public List<Doctor> getDoctorsByDept(String deptCode) {
        return doctorRepository.findByDeptCodeAndStatusOrderBySortOrderAsc(deptCode, 1);
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findByStatusOrderBySortOrderAsc(1);
    }
    
    public List<ScheduleVO> getSchedulesByDeptAndDate(String deptCode, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDeptCodeAndScheduleDateAndStatusOrderByStartTimeAsc(deptCode, date, 1);
        return convertToVOList(schedules);
    }
    
    public List<ScheduleVO> getSchedulesByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByDoctorIdAndScheduleDateBetweenAndStatusOrderByScheduleDateAsc(doctorId, startDate, endDate, 1);
        return convertToVOList(schedules);
    }
    
    public List<ScheduleVO> getSchedulesByDoctorAndDate(Long doctorId, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDoctorIdAndScheduleDateAndStatus(doctorId, date, 1);
        return convertToVOList(schedules);
    }
    
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
    
    private List<ScheduleVO> convertToVOList(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> doctorIds = schedules.stream().map(Schedule::getDoctorId).distinct().collect(Collectors.toList());
        List<String> deptCodes = schedules.stream().map(Schedule::getDeptCode).distinct().collect(Collectors.toList());
        
        Map<Long, Doctor> doctorMap = doctorRepository.findAllById(doctorIds).stream()
                .collect(Collectors.toMap(Doctor::getId, d -> d));
        Map<String, Department> deptMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getDeptCode, d -> d));
        
        return schedules.stream().map(s -> {
            ScheduleVO vo = new ScheduleVO();
            vo.setId(s.getId());
            vo.setDoctorId(s.getDoctorId());
            vo.setDeptCode(s.getDeptCode());
            vo.setScheduleDate(s.getScheduleDate());
            vo.setStartTime(s.getStartTime());
            vo.setEndTime(s.getEndTime());
            vo.setTimeSlot(s.getTimeSlot());
            vo.setTotalCount(s.getTotalCount());
            vo.setAvailableCount(s.getAvailableCount());
            
            Doctor doctor = doctorMap.get(s.getDoctorId());
            if (doctor != null) {
                vo.setDoctorName(doctor.getName());
                vo.setDoctorTitle(doctor.getTitle());
                vo.setRegistrationFee(doctor.getRegistrationFee());
            }
            
            Department dept = deptMap.get(s.getDeptCode());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
            
            return vo;
        }).collect(Collectors.toList());
    }
}
