package com.bike.repository;

import com.bike.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 车辆数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface BikeRepository extends JpaRepository<Bike, Long>, JpaSpecificationExecutor<Bike> {

    Optional<Bike> findByBikeNo(String bikeNo);

    Optional<Bike> findByQrCode(String qrCode);

    List<Bike> findByStatus(String status);

    List<Bike> findByCurrentAreaId(Long areaId);

    List<Bike> findByIsElectricTrueAndBatteryLevelLessThanEqual(Integer level);

    long countByStatus(String status);

    long countByCurrentAreaId(Long areaId);
}
