package com.medical.appointment.repository;

import com.medical.appointment.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByDoctor_IdOrderByCreatedAtDesc(Long doctorId);
    
    List<Review> findByPatient_IdOrderByCreatedAtDesc(Long patientId);
    
    List<Review> findByUser_IdOrderByCreatedAtDesc(Long userId);
    
    Optional<Review> findByAppointment_Id(Long appointmentId);
    
    boolean existsByAppointment_Id(Long appointmentId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId")
    java.math.BigDecimal findAverageRatingByDoctorId(Long doctorId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId")
    long countByDoctorId(Long doctorId);
    
    boolean existsByAppointmentId(Long appointmentId);
}
