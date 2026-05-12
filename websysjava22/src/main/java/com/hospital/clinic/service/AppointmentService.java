package com.hospital.clinic.service;

import com.hospital.clinic.entity.Appointment;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 预约挂号Service
 * 预约挂号业务逻辑处理
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * 查询所有预约
     */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * 根据ID查询预约
     */
    public Appointment findById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }

    /**
     * 根据预约单号查询
     */
    public Appointment findByAppointmentNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo);
    }

    /**
     * 根据患者查询预约
     */
    public List<Appointment> findByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    /**
     * 根据医生查询预约
     */
    public List<Appointment> findByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    /**
     * 根据日期查询预约
     */
    public List<Appointment> findByAppointmentDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    /**
     * 根据医生和日期查询预约
     */
    public List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date) {
        return appointmentRepository.findByDoctorAndAppointmentDate(doctor, date);
    }

    /**
     * 根据状态查询
     */
    public List<Appointment> findByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    /**
     * 新增预约
     */
    public Appointment save(Appointment appointment) {
        if (appointment.getAppointmentNo() == null) {
            appointment.setAppointmentNo(generateAppointmentNo());
        }
        if (appointment.getQueueNumber() == null) {
            appointment.setQueueNumber(generateQueueNumber(appointment.getDoctor(), appointment.getAppointmentDate()));
        }
        if (appointment.getStatus() == null) {
            appointment.setStatus("已预约");
        }
        return appointmentRepository.save(appointment);
    }

    /**
     * 更新预约
     */
    public Appointment update(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * 删除预约
     */
    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    /**
     * 叫号
     */
    public Appointment callNumber(Long id) {
        Appointment appointment = findById(id);
        if (appointment != null) {
            appointment.setStatus("已叫号");
            appointment.setCallTime(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    /**
     * 完成就诊
     */
    public Appointment complete(Long id) {
        Appointment appointment = findById(id);
        if (appointment != null) {
            appointment.setStatus("已完成");
            appointment.setCompleteTime(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    /**
     * 生成预约单号
     */
    private String generateAppointmentNo() {
        return "APT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 生成排队号
     */
    private Integer generateQueueNumber(Doctor doctor, LocalDate date) {
        Integer maxQueueNumber = appointmentRepository.findMaxQueueNumberByDoctorAndDate(doctor, date);
        return maxQueueNumber == null ? 1 : maxQueueNumber + 1;
    }
}
