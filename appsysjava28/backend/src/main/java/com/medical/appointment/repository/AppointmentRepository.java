package com.medical.appointment.repository;

import com.medical.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByUser_IdOrderByCreatedAtDesc(Long userId);
    
    List<Appointment> findByUser_IdAndStatusInOrderByCreatedAtDesc(Long userId, List<Appointment.AppointmentStatus> statuses);
    
    List<Appointment> findByPatient_IdOrderByCreatedAtDesc(Long patientId);
    
    List<Appointment> findByDoctor_IdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);
    
    Optional<Appointment> findByAppointmentNo(String appointmentNo);
    
    Optional<Appointment> findByIdAndUser_Id(Long id, Long userId);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status IN ('PAID', 'CONFIRMED', 'COMPLETED')")
    long countByDoctorAndDate(Long doctorId, LocalDate date);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.patient.id = :patientId AND a.status = 'COMPLETED' ORDER BY a.createdAt DESC")
    List<Appointment> findCompletedAppointmentsByDoctorAndPatient(Long doctorId, Long patientId);
}
