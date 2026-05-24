package com.bike.repository;

import com.bike.entity.BatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电池操作日志数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface BatteryLogRepository extends JpaRepository<BatteryLog, Long>, JpaSpecificationExecutor<BatteryLog> {

    List<BatteryLog> findByBatteryId(Long batteryId);

    List<BatteryLog> findByOperatorId(Long operatorId);

    List<BatteryLog> findByOperationType(String operationType);

    List<BatteryLog> findByBikeId(Long bikeId);

    List<BatteryLog> findByStationId(Long stationId);
}
