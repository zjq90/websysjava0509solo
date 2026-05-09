package com.inventory.repository;

import com.inventory.entity.SeedBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeedBatchRepository extends JpaRepository<SeedBatch, Long> {
    Optional<SeedBatch> findByBatchCode(String batchCode);
    List<SeedBatch> findByVarietyId(Long varietyId);
    
    @Query("SELECT s FROM SeedBatch s WHERE s.expiryDate BETWEEN :startDate AND :endDate")
    List<SeedBatch> findExpiringBatches(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    List<SeedBatch> findByExpiryDateLessThan(LocalDate date);
}
