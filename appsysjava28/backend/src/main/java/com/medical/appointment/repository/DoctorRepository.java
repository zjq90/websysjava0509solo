package com.medical.appointment.repository;

import com.medical.appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    List<Doctor> findByDepartment_IdAndIsActiveTrueOrderByRatingDesc(Long departmentId);
    
    List<Doctor> findByIsActiveTrueOrderByRatingDescVisitCountDesc();
    
    Optional<Doctor> findByIdAndIsActiveTrue(Long id);
    
    @Query("SELECT d FROM Doctor d WHERE d.isActive = true AND (d.realName LIKE %:keyword% OR d.specialty LIKE %:keyword% OR d.introduction LIKE %:keyword%)")
    List<Doctor> searchByKeyword(String keyword);
    
    @Query("SELECT d FROM Doctor d WHERE d.department.id = :departmentId AND d.isActive = true ORDER BY d.rating DESC")
    List<Doctor> findTopDoctorsByDepartment(Long departmentId);
    
    @Query("SELECT d FROM Doctor d WHERE d.isActive = true ORDER BY d.visitCount DESC")
    List<Doctor> findPopularDoctors();
}
