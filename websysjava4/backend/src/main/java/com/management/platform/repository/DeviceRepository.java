package com.management.platform.repository;

import com.management.platform.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备数据访问层
 * 提供设备的增删改查和统计查询功能
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * 按设备编号查询设备
     * @param deviceCode 设备编号
     * @return 设备对象
     */
    Device findByDeviceCode(String deviceCode);

    /**
     * 按状态统计设备数量
     * @param status 设备状态
     * @return 设备数量
     */
    long countByStatus(String status);

    /**
     * 按类型统计设备
     * @return 类型和数量列表
     */
    @Query("SELECT d.type, COUNT(d) FROM Device d GROUP BY d.type")
    List<Object[]> countByType();

    /**
     * 查询单机产出最高的设备
     * @param limit 限制返回数量
     * @return 高产出设备列表
     */
    @Query("SELECT d FROM Device d ORDER BY d.totalOutput DESC")
    List<Device> findTopNByTotalOutputDesc(int limit);
}
