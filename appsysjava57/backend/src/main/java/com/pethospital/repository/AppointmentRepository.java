package com.pethospital.repository;

import com.pethospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByStatusAndAppointmentTimeBetween(String status, LocalDateTime start, LocalDateTime end);
}
