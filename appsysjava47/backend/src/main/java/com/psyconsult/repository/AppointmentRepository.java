package com.psyconsult.repository;

import com.psyconsult.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<Appointment> findByCounselorIdOrderByCreateTimeDesc(Long counselorId);
    List<Appointment> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, String status);
}
