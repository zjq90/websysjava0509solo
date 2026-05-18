package com.psyconsult.repository;

import com.psyconsult.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCounselorId(Long counselorId);

    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByCounselorIdAndDate(Long counselorId, LocalDate date);

    List<Appointment> findByCounselorIdAndStatus(Long counselorId, String status);

    List<Appointment> findByUserIdAndStatus(Long userId, String status);
}
