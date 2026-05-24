package com.bike.repository;

import com.bike.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 电池数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long>, JpaSpecificationExecutor<Battery> {

    Optional<Battery> findByBatteryNo(String batteryNo);

    List<Battery> findByStatus(String status);

    List<Battery> findByCurrentLevelLessThanEqual(Integer level);

    List<Battery> findByCurrentBikeId(Long bikeId);

    List<Battery> findByCurrentStationId(Long stationId);

    List<Battery> findByHealthDegreeLessThanEqual(Float healthDegree);
}
