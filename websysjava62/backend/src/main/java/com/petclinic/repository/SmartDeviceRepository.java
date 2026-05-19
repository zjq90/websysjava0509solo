package com.petclinic.repository;

import com.petclinic.entity.SmartDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 智能设备Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface SmartDeviceRepository extends JpaRepository<SmartDevice, Long> {

    /**
     * 根据设备状态查询
     */
    List<SmartDevice> findByStatus(String status);

    /**
     * 根据设备类型查询
     */
    List<SmartDevice> findByDeviceType(String deviceType);

    /**
     * 根据设备编号查询
     */
    Optional<SmartDevice> findByDeviceCode(String deviceCode);

    /**
     * 根据设备名称查询
     */
    List<SmartDevice> findByDeviceNameContaining(String deviceName);
}
