package com.bikeshare.repository;

import com.bikeshare.entity.RideRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 骑行记录数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface RideRecordRepository extends JpaRepository<RideRecord, Long> {

    List<RideRecord> findByUserId(Long userId);

    List<RideRecord> findByBikeId(Long bikeId);

    @Query("SELECT COUNT(r) FROM RideRecord r WHERE r.startTime >= :startTime")
    Long countRides(LocalDateTime startTime);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RideRecord r WHERE r.startTime >= :startTime AND r.status = 'COMPLETED'")
    java.math.BigDecimal sumRevenue(LocalDateTime startTime);

    @Query("SELECT r.startAreaName, COUNT(r) FROM RideRecord r WHERE r.startTime >= :startTime GROUP BY r.startAreaName")
    List<Object[]> countRidesByArea(LocalDateTime startTime);
}
