package com.inventory.repository;

import com.inventory.entity.EnvironmentMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnvironmentMonitorRepository extends JpaRepository<EnvironmentMonitor, Long> {
    List<EnvironmentMonitor> findByWarehouseIdOrderByMonitorTimeDesc(Long warehouseId);
    List<EnvironmentMonitor> findByStoreIdOrderByMonitorTimeDesc(Long storeId);
    
    @Query("SELECT e FROM EnvironmentMonitor e WHERE e.warehouse.id = :warehouseId AND e.monitorTime >= :startTime")
    List<EnvironmentMonitor> findRecentByWarehouse(@Param("warehouseId") Long warehouseId, @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT e FROM EnvironmentMonitor e WHERE e.store.id = :storeId AND e.monitorTime >= :startTime")
    List<EnvironmentMonitor> findRecentByStore(@Param("storeId") Long storeId, @Param("startTime") LocalDateTime startTime);
}
