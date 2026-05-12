package com.lims.repository;

import com.lims.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * 根据设备编号查询
     */
    Device findByDeviceCode(String deviceCode);

    /**
     * 根据设备类型查询
     */
    List<Device> findByDeviceType(String deviceType);

    /**
     * 根据所属科室ID查询
     */
    List<Device> findByDepartmentId(Long departmentId);

    /**
     * 根据状态查询
     */
    List<Device> findByStatus(String status);
}
