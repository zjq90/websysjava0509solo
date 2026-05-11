package com.hospital.appointment.service;

import com.hospital.appointment.entity.Schedule;
import com.hospital.appointment.repository.DepartmentRepository;
import com.hospital.appointment.repository.DoctorRepository;
import com.hospital.appointment.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班号源服务类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Schedule> getSchedulesByDate(LocalDate date, Long deptId, Long doctorId, String timePeriod) {
        List<Schedule> schedules = scheduleRepository.findByDateAndConditions(date, deptId, doctorId, timePeriod);
        for (Schedule s : schedules) {
            doctorRepository.findById(s.getDoctorId()).ifPresent(s::setDoctor);
            departmentRepository.findById(s.getDeptId()).ifPresent(dept -> s.setDeptName(dept.getDeptName()));
        }
        return schedules;
    }

    public List<Schedule> getDoctorSchedules(Long doctorId, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByDoctorIdAndScheduleDateOrderByStartTimeAsc(doctorId, date);
        for (Schedule s : schedules) {
            doctorRepository.findById(s.getDoctorId()).ifPresent(s::setDoctor);
            departmentRepository.findById(s.getDeptId()).ifPresent(dept -> s.setDeptName(dept.getDeptName()));
        }
        return schedules;
    }

    public List<LocalDate> getAvailableDates() {
        return scheduleRepository.findAvailableDates();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
}
