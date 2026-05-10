package com.management.platform.repository;

import com.management.platform.entity.DeviceFault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备故障记录数据访问层
 * 提供设备故障记录的增删改查和统计查询功能
 */
@Repository
public interface DeviceFaultRepository extends JpaRepository<DeviceFault, Long> {

    /**
     * 按设备ID查询故障记录
     * @param deviceId 设备ID
     * @return 故障记录列表
     */
    List<DeviceFault> findByDeviceId(Long deviceId);

    /**
     * 统计指定设备的故障次数
     * @param deviceId 设备ID
     * @return 故障次数
     */
    long countByDeviceId(Long deviceId);

    /**
     * 统计指定设备的维修总成本
     * @param deviceId 设备ID
     * @return 维修总成本
     */
    @Query("SELECT COALESCE(SUM(df.repairCost), 0) FROM DeviceFault df WHERE df.deviceId = :deviceId")
    BigDecimal sumRepairCostByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 按严重程度统计故障数量
     * @return 严重程度和数量列表
     */
    @Query("SELECT df.severity, COUNT(df) FROM DeviceFault df GROUP BY df.severity")
    List<Object[]> countBySeverity();

    /**
     * 统计故障总数
     * @return 故障总数
     */
    @Query("SELECT COUNT(df) FROM DeviceFault df")
    long countTotalFaults();

    /**
     * 统计总维修成本
     * @return 总维修成本
     */
    @Query("SELECT COALESCE(SUM(df.repairCost), 0) FROM DeviceFault df")
    BigDecimal sumTotalRepairCost();
}
