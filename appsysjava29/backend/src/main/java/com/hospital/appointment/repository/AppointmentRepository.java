package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Optional<Appointment> findByAppointmentNo(String appointmentNo);
    
    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId " +
           "AND (:status IS NULL OR a.status = :status) " +
           "ORDER BY a.createdAt DESC")
    List<Appointment> findByUserIdAndStatus(@Param("userId") Long userId,
                                             @Param("status") String status);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId " +
           "AND (:status IS NULL OR a.status = :status) " +
           "ORDER BY a.createdAt DESC")
    List<Appointment> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId,
                                               @Param("status") String status);
}
