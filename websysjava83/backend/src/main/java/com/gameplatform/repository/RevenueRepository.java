package com.gameplatform.repository;

import com.gameplatform.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM revenues WHERE revenue_type = 'AD' AND created_at >= :start AND created_at <= :end", nativeQuery = true)
    Double getTotalAdRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM revenues WHERE revenue_type = 'VIP' AND created_at >= :start AND created_at <= :end", nativeQuery = true)
    Double getTotalVipRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM revenues WHERE created_at >= :start AND created_at <= :end", nativeQuery = true)
    Double getTotalRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT CAST(created_at AS DATE) as dt, revenue_type, SUM(amount) as total " +
           "FROM revenues WHERE created_at >= :start AND created_at <= :end " +
           "GROUP BY CAST(created_at AS DATE), revenue_type ORDER BY dt", nativeQuery = true)
    List<Object[]> getRevenueTrend(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
