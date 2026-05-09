package com.inventory.service;

import com.inventory.entity.Alert;
import com.inventory.entity.EnvironmentMonitor;
import com.inventory.repository.AlertRepository;
import com.inventory.repository.EnvironmentMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 温湿度监控服务层
 * 功能：处理温湿度监控数据的增删改查，支持温湿度预警
 */
@Service
@Transactional
public class EnvironmentMonitorService {

    @Autowired
    private EnvironmentMonitorRepository environmentMonitorRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Value("${inventory.temperature.min:0}")
    private BigDecimal tempMin;

    @Value("${inventory.temperature.max:30}")
    private BigDecimal tempMax;

    @Value("${inventory.humidity.min:30}")
    private BigDecimal humidityMin;

    @Value("${inventory.humidity.max:80}")
    private BigDecimal humidityMax;

    public List<EnvironmentMonitor> findAll() {
        return environmentMonitorRepository.findAll();
    }

    public Optional<EnvironmentMonitor> findById(Long id) {
        return environmentMonitorRepository.findById(id);
    }

    public List<EnvironmentMonitor> findByWarehouseId(Long warehouseId) {
        return environmentMonitorRepository.findByWarehouseIdOrderByMonitorTimeDesc(warehouseId);
    }

    public List<EnvironmentMonitor> findByStoreId(Long storeId) {
        return environmentMonitorRepository.findByStoreIdOrderByMonitorTimeDesc(storeId);
    }

    public List<EnvironmentMonitor> findRecentByWarehouse(Long warehouseId, int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return environmentMonitorRepository.findRecentByWarehouse(warehouseId, startTime);
    }

    public List<EnvironmentMonitor> findRecentByStore(Long storeId, int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return environmentMonitorRepository.findRecentByStore(storeId, startTime);
    }

    public EnvironmentMonitor save(EnvironmentMonitor monitor) {
        checkAndCreateAlert(monitor);
        return environmentMonitorRepository.save(monitor);
    }

    public void deleteById(Long id) {
        environmentMonitorRepository.deleteById(id);
    }

    private void checkAndCreateAlert(EnvironmentMonitor monitor) {
        BigDecimal temp = monitor.getTemperature();
        BigDecimal humidity = monitor.getHumidity();
        String locationName = "";
        Long entityId = null;
        String entityType = "";

        if (monitor.getWarehouse() != null) {
            locationName = "仓库: " + monitor.getWarehouse().getWarehouseName();
            entityId = monitor.getWarehouse().getId();
            entityType = "WAREHOUSE";
        } else if (monitor.getStore() != null) {
            locationName = "门店: " + monitor.getStore().getStoreName();
            entityId = monitor.getStore().getId();
            entityType = "STORE";
        }

        if (temp.compareTo(tempMin) < 0 || temp.compareTo(tempMax) > 0) {
            Alert alert = new Alert();
            alert.setAlertType("TEMPERATURE");
            alert.setLevel("WARNING");
            alert.setMessage(locationName + " 温度异常: " + temp + "°C (正常范围: " + tempMin + "~" + tempMax + "°C)");
            alert.setRelatedEntity(entityType);
            alert.setRelatedEntityId(entityId);
            alertRepository.save(alert);
        }

        if (humidity.compareTo(humidityMin) < 0 || humidity.compareTo(humidityMax) > 0) {
            Alert alert = new Alert();
            alert.setAlertType("HUMIDITY");
            alert.setLevel("WARNING");
            alert.setMessage(locationName + " 湿度异常: " + humidity + "% (正常范围: " + humidityMin + "~" + humidityMax + "%)");
            alert.setRelatedEntity(entityType);
            alert.setRelatedEntityId(entityId);
            alertRepository.save(alert);
        }
    }
}
