package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    List<Patient> findByUserIdOrderByIsDefaultDesc(Long userId);
    
    Optional<Patient> findByUserIdAndIsDefault(Long userId, Integer isDefault);
    
    void deleteByUserIdAndId(Long userId, Long id);
}
