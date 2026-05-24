package com.bike.repository;

import com.bike.entity.HeatPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 热力点数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface HeatPointRepository extends JpaRepository<HeatPoint, Long>, JpaSpecificationExecutor<HeatPoint> {

    List<HeatPoint> findByAreaId(Long areaId);

    List<HeatPoint> findByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT h FROM HeatPoint h WHERE h.recordTime >= :startTime ORDER BY h.heatValue DESC")
    List<HeatPoint> findTopHeatPoints(LocalDateTime startTime);
}
