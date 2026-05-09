package com.inventory.service;

import com.inventory.entity.EnvRecord;
import com.inventory.entity.Warehouse;
import com.inventory.repository.EnvRecordRepository;
import com.inventory.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 环境记录Service类
 * 管理仓库温湿度记录和预警
 */
@Service
public class EnvRecordService {

    @Autowired
    private EnvRecordRepository envRecordRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    /**
     * 查询所有环境记录
     */
    public List<EnvRecord> findAll() {
        return envRecordRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public EnvRecord findById(Long id) {
        Optional<EnvRecord> optional = envRecordRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 根据仓库ID查询环境记录
     */
    public List<EnvRecord> findByWarehouseId(Long warehouseId) {
        return envRecordRepository.findByWarehouseIdOrderByRecordTimeDesc(warehouseId);
    }

    /**
     * 查询某个仓库最新的环境记录
     */
    public EnvRecord findLatestByWarehouseId(Long warehouseId) {
        List<EnvRecord> list = envRecordRepository.findLatestByWarehouseId(warehouseId);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    /**
     * 保存环境记录（自动检查温湿度异常）
     */
    @Transactional(rollbackFor = Exception.class)
    public EnvRecord save(EnvRecord record) {
        Warehouse warehouse = warehouseRepository.findById(record.getWarehouseId()).orElse(null);
        
        // 设置阈值（使用仓库配置的阈值，如无则使用默认值）
        double tempMin = warehouse != null && warehouse.getTempWarningMin() != null 
                ? warehouse.getTempWarningMin() : 0.0;
        double tempMax = warehouse != null && warehouse.getTempWarningMax() != null 
                ? warehouse.getTempWarningMax() : 35.0;
        double humidityMin = warehouse != null && warehouse.getHumidityWarningMin() != null 
                ? warehouse.getHumidityWarningMin() : 30.0;
        double humidityMax = warehouse != null && warehouse.getHumidityWarningMax() != null 
                ? warehouse.getHumidityWarningMax() : 70.0;

        record.setTempThresholdMin(tempMin);
        record.setTempThresholdMax(tempMax);
        record.setHumidityThresholdMin(humidityMin);
        record.setHumidityThresholdMax(humidityMax);

        // 检查温湿度是否异常
        boolean tempAbnormal = record.getTemperature() < tempMin || record.getTemperature() > tempMax;
        boolean humidityAbnormal = record.getHumidity() < humidityMin || record.getHumidity() > humidityMax;

        // 设置状态
        if (tempAbnormal && humidityAbnormal) {
            record.setStatus(3);
            record.setWarningMessage("温度和湿度均超出正常范围！");
        } else if (tempAbnormal) {
            record.setStatus(1);
            record.setWarningMessage("温度超出正常范围！");
        } else if (humidityAbnormal) {
            record.setStatus(2);
            record.setWarningMessage("湿度超出正常范围！");
        } else {
            record.setStatus(0);
            record.setWarningMessage(null);
        }

        if (record.getRecordTime() == null) {
            record.setRecordTime(LocalDateTime.now());
        }

        // 更新仓库的当前温湿度
        if (warehouse != null) {
            warehouse.setCurrentTemperature(record.getTemperature());
            warehouse.setCurrentHumidity(record.getHumidity());
            warehouseRepository.save(warehouse);
        }

        return envRecordRepository.save(record);
    }

    /**
     * 删除环境记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        envRecordRepository.deleteById(id);
    }

    /**
     * 查询异常记录
     */
    public List<EnvRecord> findAbnormalRecords() {
        return envRecordRepository.findByStatusNot(0);
    }

    /**
     * 查询某个仓库的异常记录
     */
    public List<EnvRecord> findAbnormalRecords(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime) {
        return envRecordRepository.findByWarehouseIdAndStatusNotAndRecordTimeBetween(warehouseId, 0, startTime, endTime);
    }
}
