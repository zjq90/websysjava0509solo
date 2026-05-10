package com.websys.repository;

import com.websys.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * 设备数据访问接口
 * 提供设备的增删改查操作
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    /**
     * 根据设备编号查找设备
     * @param deviceCode 设备编号
     * @return 设备对象
     */
    Optional<Device> findByDeviceCode(String deviceCode);

    /**
     * 判断设备编号是否存在
     * @param deviceCode 设备编号
     * @return 是否存在
     */
    boolean existsByDeviceCode(String deviceCode);

    /**
     * 根据设备状态查找设备列表
     * @param status 设备状态
     * @return 设备列表
     */
    List<Device> findByStatus(Integer status);

    /**
     * 统计各状态设备数量
     * @param status 设备状态
     * @return 数量
     */
    long countByStatus(Integer status);
}
