package com.websys.repository;

import com.websys.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * 设备状态数据访问接口
 * 提供设备状态的增删改查操作
 */
@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long>, JpaSpecificationExecutor<DeviceStatus> {

    /**
     * 根据设备ID查找最新状态记录
     * @param deviceId 设备ID
     * @return 设备状态
     */
    Optional<DeviceStatus> findTopByDeviceIdOrderByReportTimeDesc(Long deviceId);

    /**
     * 根据设备编号查找最新状态记录
     * @param deviceCode 设备编号
     * @return 设备状态
     */
    Optional<DeviceStatus> findTopByDeviceCodeOrderByReportTimeDesc(String deviceCode);

    /**
     * 根据设备ID查找历史状态记录
     * @param deviceId 设备ID
     * @return 状态记录列表
     */
    List<DeviceStatus> findByDeviceIdOrderByReportTimeDesc(Long deviceId);

    /**
     * 删除指定设备的状态记录
     * @param deviceId 设备ID
     */
    void deleteByDeviceId(Long deviceId);
}
