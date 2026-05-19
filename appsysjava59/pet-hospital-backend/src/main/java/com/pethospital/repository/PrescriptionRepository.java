package com.pethospital.repository;

import com.pethospital.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByConsultationId(Long consultationId);
    List<Prescription> findByDoctorId(Long doctorId);
    List<Prescription> findByPetId(Long petId);
}
