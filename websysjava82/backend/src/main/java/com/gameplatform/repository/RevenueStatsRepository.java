package com.gameplatform.repository;

import com.gameplatform.entity.RevenueStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueStatsRepository extends JpaRepository<RevenueStats, Long> {
    Optional<RevenueStats> findByAdSlotIdAndStatDate(Long adSlotId, LocalDate statDate);

    List<RevenueStats> findByAdSlotId(Long adSlotId);

    List<RevenueStats> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    List<RevenueStats> findByAdSlotIdAndStatDateBetween(Long adSlotId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM RevenueStats r WHERE r.statDate BETWEEN :startDate AND :endDate ORDER BY r.statDate")
    List<RevenueStats> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
