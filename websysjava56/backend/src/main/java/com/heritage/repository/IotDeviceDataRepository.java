package com.heritage.repository;

import com.heritage.entity.IotDeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IotDeviceDataRepository extends JpaRepository<IotDeviceData, Long>, JpaSpecificationExecutor<IotDeviceData> {

    List<IotDeviceData> findByDeviceId(String deviceId);

    List<IotDeviceData> findByDeviceIdAndDataTimeBetween(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

    List<IotDeviceData> findByIsValidTrue();

    List<IotDeviceData> findByHeritageId(Long heritageId);
}
