package com.plate.repository;

import com.plate.entity.RecognitionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecognitionRecordRepository extends JpaRepository<RecognitionRecord, Long> {
    long countByPassTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByIsAnomalyTrueAndPassTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT r FROM RecognitionRecord r WHERE " +
           "(:plateNumber IS NULL OR r.plateNumber LIKE %:plateNumber%) AND " +
           "(:cameraId IS NULL OR r.cameraId = :cameraId) AND " +
           "(:anomalyType IS NULL OR r.anomalyType = :anomalyType) AND " +
           "(:startTime IS NULL OR r.passTime >= :startTime) AND " +
           "(:endTime IS NULL OR r.passTime <= :endTime)")
    Page<RecognitionRecord> findByConditions(
            @Param("plateNumber") String plateNumber,
            @Param("cameraId") String cameraId,
            @Param("anomalyType") String anomalyType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT r FROM RecognitionRecord r WHERE " +
           "(:plateNumber IS NULL OR r.plateNumber LIKE %:plateNumber%) AND " +
           "(:cameraId IS NULL OR r.cameraId = :cameraId) AND " +
           "(:anomalyType IS NULL OR r.anomalyType = :anomalyType) AND " +
           "(:startTime IS NULL OR r.passTime >= :startTime) AND " +
           "(:endTime IS NULL OR r.passTime <= :endTime)")
    List<RecognitionRecord> findAllByConditions(
            @Param("plateNumber") String plateNumber,
            @Param("cameraId") String cameraId,
            @Param("anomalyType") String anomalyType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
