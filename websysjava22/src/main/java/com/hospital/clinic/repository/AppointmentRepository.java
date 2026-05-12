package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Appointment;
import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 预约挂号Repository
 * 预约挂号数据访问接口
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * 根据预约单号查询
     */
    Appointment findByAppointmentNo(String appointmentNo);

    /**
     * 根据患者查询预约
     */
    List<Appointment> findByPatient(Patient patient);

    /**
     * 根据医生查询预约
     */
    List<Appointment> findByDoctor(Doctor doctor);

    /**
     * 根据科室查询预约
     */
    List<Appointment> findByDepartment(Department department);

    /**
     * 根据预约日期查询
     */
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    /**
     * 根据医生和日期查询预约
     */
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);

    /**
     * 根据状态查询预约
     */
    List<Appointment> findByStatus(String status);

    /**
     * 根据医生、日期和状态查询预约
     */
    List<Appointment> findByDoctorAndAppointmentDateAndStatus(Doctor doctor, LocalDate appointmentDate, String status);

    /**
     * 查询某天某医生的最大排队号
     */
    @Query("SELECT MAX(a.queueNumber) FROM Appointment a WHERE a.doctor = ?1 AND a.appointmentDate = ?2")
    Integer findMaxQueueNumberByDoctorAndDate(Doctor doctor, LocalDate appointmentDate);
}
