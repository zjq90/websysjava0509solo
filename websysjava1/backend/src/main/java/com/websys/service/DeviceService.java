package com.websys.service;

import com.websys.entity.Device;
import com.websys.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 设备服务类
 * 提供设备管理的业务逻辑，包括增删改查等操作
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * 分页查询设备列表
     * @param deviceCode 设备编号（模糊查询）
     * @param deviceName 设备名称（模糊查询）
     * @param location 投放位置（模糊查询）
     * @param status 设备状态
     * @param pageable 分页参数
     * @return 分页设备列表
     */
    public Page<Device> findPage(String deviceCode, String deviceName, String location, Integer status, Pageable pageable) {
        return deviceRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (deviceCode != null && !deviceCode.isEmpty()) {
                predicates.add(cb.like(root.get("deviceCode"), "%" + deviceCode + "%"));
            }
            
            if (deviceName != null && !deviceName.isEmpty()) {
                predicates.add(cb.like(root.get("deviceName"), "%" + deviceName + "%"));
            }
            
            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(root.get("location"), "%" + location + "%"));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 查询所有设备
     * @return 设备列表
     */
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    /**
     * 根据ID查询设备
     * @param id 设备ID
     * @return 设备对象
     */
    public Optional<Device> findById(Long id) {
        return deviceRepository.findById(id);
    }

    /**
     * 根据设备编号查询设备
     * @param deviceCode 设备编号
     * @return 设备对象
     */
    public Optional<Device> findByDeviceCode(String deviceCode) {
        return deviceRepository.findByDeviceCode(deviceCode);
    }

    /**
     * 保存设备（新增/更新）
     * @param device 设备对象
     * @return 保存后的设备对象
     */
    @Transactional
    public Device save(Device device) {
        if (device.getId() != null) {
            device.setUpdateTime(LocalDateTime.now());
        } else {
            device.setInstallTime(LocalDateTime.now());
        }
        return deviceRepository.save(device);
    }

    /**
     * 删除设备
     * @param id 设备ID
     */
    @Transactional
    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }

    /**
     * 检查设备编号是否存在
     * @param deviceCode 设备编号
     * @return 是否存在
     */
    public boolean existsByDeviceCode(String deviceCode) {
        return deviceRepository.existsByDeviceCode(deviceCode);
    }

    /**
     * 根据状态查询设备列表
     * @param status 设备状态
     * @return 设备列表
     */
    public List<Device> findByStatus(Integer status) {
        return deviceRepository.findByStatus(status);
    }

    /**
     * 统计各状态设备数量
     * @param status 设备状态
     * @return 数量
     */
    public long countByStatus(Integer status) {
        return deviceRepository.countByStatus(status);
    }

    /**
     * 更新设备状态
     * @param id 设备ID
     * @param status 新状态
     * @return 更新后的设备
     */
    @Transactional
    public Device updateStatus(Long id, Integer status) {
        Optional<Device> deviceOpt = deviceRepository.findById(id);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            device.setStatus(status);
            device.setLastReportTime(LocalDateTime.now());
            device.setUpdateTime(LocalDateTime.now());
            return deviceRepository.save(device);
        }
        throw new RuntimeException("设备不存在");
    }

    /**
     * 批量更新设备状态
     * @param ids 设备ID列表
     * @param status 新状态
     */
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            updateStatus(id, status);
        }
    }
}
