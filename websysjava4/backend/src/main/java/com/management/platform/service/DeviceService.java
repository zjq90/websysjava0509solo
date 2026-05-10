package com.management.platform.service;

import com.management.platform.entity.Device;
import com.management.platform.repository.DeviceFaultRepository;
import com.management.platform.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备服务层
 * 提供设备的CRUD和统计分析功能
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceFaultRepository deviceFaultRepository;

    /**
     * 创建新设备
     * @param device 设备对象
     * @return 创建后的设备
     */
    @Transactional
    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    /**
     * 根据ID查询设备
     * @param id 设备ID
     * @return 设备对象，不存在则返回null
     */
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询所有设备
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 分页结果
     */
    public Page<Device> getAllDevices(int page, int size) {
        return deviceRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    /**
     * 更新设备信息
     * @param id 设备ID
     * @param device 更新的设备信息
     * @return 更新后的设备
     */
    @Transactional
    public Device updateDevice(Long id, Device device) {
        Device existingDevice = deviceRepository.findById(id).orElse(null);
        if (existingDevice != null) {
            existingDevice.setDeviceCode(device.getDeviceCode());
            existingDevice.setName(device.getName());
            existingDevice.setType(device.getType());
            existingDevice.setLocation(device.getLocation());
            existingDevice.setStatus(device.getStatus());
            existingDevice.setTotalRuntime(device.getTotalRuntime());
            existingDevice.setTotalOutput(device.getTotalOutput());
            existingDevice.setFaultCount(device.getFaultCount());
            existingDevice.setMaintenanceCost(device.getMaintenanceCost());
            return deviceRepository.save(existingDevice);
        }
        return null;
    }

    /**
     * 删除设备
     * @param id 设备ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteDevice(Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 获取单机产出统计
     * @param limit 返回数量限制
     * @return 单机产出数据
     */
    public List<Map<String, Object>> getSingleMachineOutput(int limit) {
        List<Device> devices = deviceRepository.findTopNByTotalOutputDesc(limit);
        List<Map<String, Object>> result = new ArrayList<>();
        
        int count = Math.min(limit, devices.size());
        for (int i = 0; i < count; i++) {
            Device d = devices.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("deviceCode", d.getDeviceCode());
            map.put("name", d.getName());
            map.put("type", d.getType());
            map.put("location", d.getLocation());
            map.put("totalOutput", d.getTotalOutput());
            map.put("totalRuntime", d.getTotalRuntime());
            
            // 计算每小时产出
            if (d.getTotalRuntime() > 0) {
                map.put("outputPerHour", 
                    BigDecimal.valueOf(d.getTotalOutput())
                        .divide(BigDecimal.valueOf(d.getTotalRuntime()), 2, RoundingMode.HALF_UP));
            } else {
                map.put("outputPerHour", BigDecimal.ZERO);
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 获取故障率统计
     * @return 故障率统计数据
     */
    public Map<String, Object> getFaultRateStatistics() {
        List<Device> devices = deviceRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> deviceFaults = new ArrayList<>();
        
        long totalFaults = deviceFaultRepository.countTotalFaults();
        long totalDevices = devices.size();
        long runningDevices = deviceRepository.countByStatus("running");
        
        for (Device d : devices) {
            long faultCount = deviceFaultRepository.countByDeviceId(d.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("deviceCode", d.getDeviceCode());
            map.put("name", d.getName());
            map.put("status", d.getStatus());
            map.put("faultCount", faultCount);
            map.put("totalRuntime", d.getTotalRuntime());
            
            // 计算故障率（每100小时故障次数）
            if (d.getTotalRuntime() > 0) {
                double faultRate = (double) faultCount / d.getTotalRuntime() * 100;
                map.put("faultRatePer100Hours", 
                    BigDecimal.valueOf(faultRate).setScale(2, RoundingMode.HALF_UP));
            } else {
                map.put("faultRatePer100Hours", BigDecimal.ZERO);
            }
            deviceFaults.add(map);
        }
        
        result.put("deviceFaults", deviceFaults);
        result.put("totalDevices", totalDevices);
        result.put("runningDevices", runningDevices);
        result.put("totalFaults", totalFaults);
        
        // 总体故障率（平均每台设备故障次数）
        if (totalDevices > 0) {
            result.put("averageFaultsPerDevice", 
                BigDecimal.valueOf((double) totalFaults / totalDevices)
                    .setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("averageFaultsPerDevice", BigDecimal.ZERO);
        }
        
        return result;
    }

    /**
     * 获取运维成本分析
     * @return 运维成本统计数据
     */
    public Map<String, Object> getMaintenanceCostAnalysis() {
        List<Device> devices = deviceRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> deviceCosts = new ArrayList<>();
        
        BigDecimal totalMaintenanceCost = deviceFaultRepository.sumTotalRepairCost();
        
        for (Device d : devices) {
            BigDecimal repairCost = deviceFaultRepository.sumRepairCostByDeviceId(d.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("deviceCode", d.getDeviceCode());
            map.put("name", d.getName());
            map.put("type", d.getType());
            map.put("maintenanceCost", repairCost);
            map.put("totalOutput", d.getTotalOutput());
            
            // 计算单位产出运维成本
            if (d.getTotalOutput() > 0) {
                map.put("costPerUnitOutput", 
                    repairCost.divide(BigDecimal.valueOf(d.getTotalOutput()), 4, RoundingMode.HALF_UP));
            } else {
                map.put("costPerUnitOutput", BigDecimal.ZERO);
            }
            deviceCosts.add(map);
        }
        
        result.put("deviceCosts", deviceCosts);
        result.put("totalMaintenanceCost", totalMaintenanceCost);
        
        // 按类型统计
        Map<String, List<Device>> typeMap = new HashMap<>();
        for (Device d : devices) {
            typeMap.computeIfAbsent(d.getType(), k -> new ArrayList<>()).add(d);
        }
        
        List<Map<String, Object>> byType = new ArrayList<>();
        for (Map.Entry<String, List<Device>> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            BigDecimal typeCost = BigDecimal.ZERO;
            long typeOutput = 0;
            
            for (Device d : entry.getValue()) {
                typeCost = typeCost.add(deviceFaultRepository.sumRepairCostByDeviceId(d.getId()));
                typeOutput += d.getTotalOutput();
            }
            
            Map<String, Object> typeMap2 = new HashMap<>();
            typeMap2.put("type", type);
            typeMap2.put("cost", typeCost);
            typeMap2.put("output", typeOutput);
            byType.add(typeMap2);
        }
        
        result.put("byType", byType);
        
        return result;
    }
}
