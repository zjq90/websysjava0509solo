package com.medical.appointment.repository;

import com.medical.appointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    List<Patient> findByUser_IdOrderByIsPrimaryDescCreatedAtAsc(Long userId);
    
    Optional<Patient> findByIdAndUser_Id(Long id, Long userId);
    
    List<Patient> findByUser_IdAndIsPrimaryTrue(Long userId);
    
    int countByUser_Id(Long userId);
    
    boolean existsByUser_IdAndIdCardNumber(Long userId, String idCardNumber);
}
