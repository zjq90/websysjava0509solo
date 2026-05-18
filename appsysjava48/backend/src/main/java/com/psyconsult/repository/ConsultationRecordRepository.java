package com.psyconsult.repository;

import com.psyconsult.entity.ConsultationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRecordRepository extends JpaRepository<ConsultationRecord, Long> {

    List<ConsultationRecord> findByCounselorId(Long counselorId);

    List<ConsultationRecord> findByUserId(Long userId);

    @Query("SELECT r FROM ConsultationRecord r WHERE r.counselorId = ?1 AND r.tags LIKE %?2%")
    List<ConsultationRecord> findByCounselorIdAndTagContaining(Long counselorId, String tag);

    List<ConsultationRecord> findByCounselorIdAndStatus(Long counselorId, Integer status);
}
