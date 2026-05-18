package com.psyconsult.repository;

import com.psyconsult.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<Consultation> findByCounselorIdOrderByCreateTimeDesc(Long counselorId);
    List<Consultation> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, String status);
}
