package com.lims.service;

import com.lims.entity.Device;
import com.lims.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 设备业务逻辑层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * 查询所有设备
     */
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    /**
     * 根据ID查询设备
     */
    public Optional<Device> findById(Long id) {
        return deviceRepository.findById(id);
    }

    /**
     * 根据设备编号查询
     */
    public Device findByDeviceCode(String deviceCode) {
        return deviceRepository.findByDeviceCode(deviceCode);
    }

    /**
     * 新增设备
     */
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    /**
     * 更新设备
     */
    public Device update(Device device) {
        return deviceRepository.save(device);
    }

    /**
     * 删除设备
     */
    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }
}
