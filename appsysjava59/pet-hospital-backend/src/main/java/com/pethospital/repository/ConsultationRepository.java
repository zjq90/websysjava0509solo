package com.pethospital.repository;

import com.pethospital.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByDoctorId(Long doctorId);
    List<Consultation> findByDoctorIdAndStatus(Long doctorId, String status);
    List<Consultation> findByOwnerId(Long ownerId);
    
    @Query("SELECT c FROM Consultation c WHERE c.doctorId = :doctorId AND c.status IN :statuses ORDER BY CASE c.emergencyLevel WHEN 'URGENT' THEN 0 ELSE 1 END, c.createTime DESC")
    List<Consultation> findDoctorConsultationsOrdered(@Param("doctorId") Long doctorId, @Param("statuses") List<String> statuses);
}
