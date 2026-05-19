package com.pethospital.service;

import com.pethospital.entity.Appointment;
import com.pethospital.entity.Doctor;
import com.pethospital.entity.DoctorSchedule;
import com.pethospital.repository.AppointmentRepository;
import com.pethospital.repository.DoctorRepository;
import com.pethospital.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约挂号Service
 * 提供科室选择、医生排班查询、预约改签/取消等功能
 * 
 * @author Pet Hospital Team
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    /**
     * 获取科室列表
     */
    public List<Map<String, Object>> getDepartments() {
        return Arrays.asList(
            createDepartment("内科", "internal", "heart"),
            createDepartment("外科", "surgery", "scalpel"),
            createDepartment("皮肤科", "dermatology", "skin"),
            createDepartment("牙科", "dental", "tooth"),
            createDepartment("眼科", "ophthalmology", "eye"),
            createDepartment("骨科", "orthopedics", "bone"),
            createDepartment("神经科", "neurology", "brain"),
            createDepartment("肿瘤科", "oncology", "ribbon")
        );
    }

    private Map<String, Object> createDepartment(String name, String id, String icon) {
        Map<String, Object> dept = new HashMap<>();
        dept.put("id", id);
        dept.put("name", name);
        dept.put("icon", icon);
        return dept;
    }

    /**
     * 获取科室医生列表
     */
    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartmentAndAvailableTrue(department);
    }

    /**
     * 获取医生排班
     */
    public List<DoctorSchedule> getDoctorSchedules(Long doctorId) {
        return doctorScheduleRepository.findByDoctorIdOrderByScheduleDateAsc(doctorId);
    }

    /**
     * 创建预约
     */
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        appointment.setStatus("PENDING");
        appointment.setSmsReminderSent(false);
        appointment.setAppReminderSent(false);
        return appointmentRepository.save(appointment);
    }

    /**
     * 取消预约
     */
    @Transactional
    public Appointment cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            appointment.setCancelReason(reason);
            appointment.setCancelTime(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    /**
     * 改签预约
     */
    @Transactional
    public Appointment rescheduleAppointment(Long appointmentId, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            appointment.setAppointmentTime(newTime);
            appointment.setStatus("RESCHEDULED");
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    /**
     * 获取用户预约列表
     */
    public List<Appointment> getUserAppointments(Long userId) {
        return appointmentRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取预约详情
     */
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }
}
