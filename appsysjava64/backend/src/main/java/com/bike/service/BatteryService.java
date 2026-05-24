package com.bike.service;

import com.bike.entity.Battery;
import com.bike.entity.BatteryLog;
import com.bike.entity.SwapStation;
import com.bike.entity.Bike;
import com.bike.repository.BatteryRepository;
import com.bike.repository.BatteryLogRepository;
import com.bike.repository.SwapStationRepository;
import com.bike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 电池管理服务
 * 
 * @author bike-sharing
 */
@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private BatteryLogRepository batteryLogRepository;

    @Autowired
    private SwapStationRepository swapStationRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Cacheable(value = "batteries", key = "'all'")
    public List<Battery> getAllBatteries() {
        return batteryRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<Battery> getBatteriesByStatus(String status) {
        return batteryRepository.findByStatus(status);
    }

    public List<Battery> getLowBatteries(Integer level) {
        return batteryRepository.findByCurrentLevelLessThanEqual(level);
    }

    public List<Battery> getBatteriesByBikeId(Long bikeId) {
        return batteryRepository.findByCurrentBikeId(bikeId);
    }

    public List<Battery> getBatteriesByStationId(Long stationId) {
        return batteryRepository.findByCurrentStationId(stationId);
    }

    public Battery getBatteryById(Long id) {
        return batteryRepository.findById(id).orElse(null);
    }

    public Battery getBatteryByNo(String batteryNo) {
        return batteryRepository.findByBatteryNo(batteryNo).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "batteries", allEntries = true)
    public Battery createBattery(Battery battery) {
        String batteryNo = "BAT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        battery.setBatteryNo(batteryNo);
        if (battery.getStatus() == null) {
            battery.setStatus("AVAILABLE");
        }
        if (battery.getHealthDegree() == null) {
            battery.setHealthDegree(100.0f);
        }
        if (battery.getChargeCount() == null) {
            battery.setChargeCount(0);
        }
        if (battery.getTotalChargeCount() == null) {
            battery.setTotalChargeCount(0);
        }
        return batteryRepository.save(battery);
    }

    @Transactional
    @CacheEvict(value = "batteries", allEntries = true)
    public Battery updateBattery(Long id, Battery battery) {
        Battery existing = batteryRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (battery.getModel() != null) existing.setModel(battery.getModel());
        if (battery.getCapacity() != null) existing.setCapacity(battery.getCapacity());
        if (battery.getCurrentLevel() != null) existing.setCurrentLevel(battery.getCurrentLevel());
        if (battery.getHealthDegree() != null) existing.setHealthDegree(battery.getHealthDegree());
        if (battery.getStatus() != null) existing.setStatus(battery.getStatus());
        return batteryRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "batteries", allEntries = true)
    public Battery updateBatteryLevel(Long batteryId, Integer newLevel, Long operatorId, String operatorName) {
        Battery battery = batteryRepository.findById(batteryId).orElse(null);
        if (battery == null) {
            return null;
        }
        
        Integer beforeLevel = battery.getCurrentLevel();
        battery.setCurrentLevel(newLevel);
        
        if (newLevel > beforeLevel) {
            battery.setChargeCount(battery.getChargeCount() + 1);
            battery.setTotalChargeCount(battery.getTotalChargeCount() + 1);
            battery.setLastChargeTime(LocalDateTime.now());
            
            float health = battery.getHealthDegree();
            if (battery.getTotalChargeCount() > 100) {
                health = Math.max(50.0f, 100.0f - (battery.getTotalChargeCount() - 100) * 0.1f);
                battery.setHealthDegree(health);
            }
        }
        
        Battery saved = batteryRepository.save(battery);
        
        BatteryLog log = new BatteryLog();
        log.setBatteryId(batteryId);
        log.setBatteryNo(battery.getBatteryNo());
        log.setOperationType("LEVEL_UPDATE");
        log.setBeforeLevel(beforeLevel);
        log.setAfterLevel(newLevel);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        batteryLogRepository.save(log);
        
        if (battery.getCurrentBikeId() != null) {
            Bike bike = bikeRepository.findById(battery.getCurrentBikeId()).orElse(null);
            if (bike != null) {
                bike.setBatteryLevel(newLevel);
                bikeRepository.save(bike);
            }
        }
        
        return saved;
    }

    @Transactional
    @CacheEvict(value = "batteries", allEntries = true)
    public Map<String, Object> swapBattery(Long bikeId, Long stationId, Long operatorId, String operatorName) {
        Map<String, Object> result = new HashMap<>();
        
        Bike bike = bikeRepository.findById(bikeId).orElse(null);
        if (bike == null || !bike.getIsElectric()) {
            result.put("success", false);
            result.put("message", "车辆不存在或不是电动车");
            return result;
        }
        
        SwapStation station = swapStationRepository.findById(stationId).orElse(null);
        if (station == null || station.getAvailableBatteries() <= 0) {
            result.put("success", false);
            result.put("message", "换电站不存在或没有可用电池");
            return result;
        }
        
        List<Battery> oldBatteries = batteryRepository.findByCurrentBikeId(bikeId);
        Battery oldBattery = oldBatteries.isEmpty() ? null : oldBatteries.get(0);
        
        List<Battery> availableBatteries = batteryRepository.findByCurrentStationId(stationId).stream()
                .filter(b -> "AVAILABLE".equals(b.getStatus()) && b.getCurrentLevel() >= 80)
                .collect(java.util.stream.Collectors.toList());
        
        if (availableBatteries.isEmpty()) {
            result.put("success", false);
            result.put("message", "换电站没有充满的电池");
            return result;
        }
        
        Battery newBattery = availableBatteries.get(0);
        
        if (oldBattery != null) {
            oldBattery.setCurrentBikeId(null);
            oldBattery.setCurrentStationId(stationId);
            oldBattery.setStatus("CHARGING");
            batteryRepository.save(oldBattery);
            
            BatteryLog oldLog = new BatteryLog();
            oldLog.setBatteryId(oldBattery.getId());
            oldLog.setBatteryNo(oldBattery.getBatteryNo());
            oldLog.setOperationType("UNINSTALL");
            oldLog.setBeforeLevel(oldBattery.getCurrentLevel());
            oldLog.setAfterLevel(oldBattery.getCurrentLevel());
            oldLog.setBikeId(bikeId);
            oldLog.setStationId(stationId);
            oldLog.setOperatorId(operatorId);
            oldLog.setOperatorName(operatorName);
            oldLog.setRemark("从车辆卸下");
            batteryLogRepository.save(oldLog);
        }
        
        newBattery.setCurrentBikeId(bikeId);
        newBattery.setCurrentStationId(null);
        newBattery.setStatus("IN_USE");
        newBattery.setInstallDate(LocalDateTime.now());
        batteryRepository.save(newBattery);
        
        bike.setBatteryId(newBattery.getId());
        bike.setBatteryLevel(newBattery.getCurrentLevel());
        bikeRepository.save(bike);
        
        BatteryLog newLog = new BatteryLog();
        newLog.setBatteryId(newBattery.getId());
        newLog.setBatteryNo(newBattery.getBatteryNo());
        newLog.setOperationType("INSTALL");
        newLog.setBeforeLevel(newBattery.getCurrentLevel());
        newLog.setAfterLevel(newBattery.getCurrentLevel());
        newLog.setBikeId(bikeId);
        newLog.setStationId(stationId);
        newLog.setOperatorId(operatorId);
        newLog.setOperatorName(operatorName);
        newLog.setRemark("安装到车辆");
        batteryLogRepository.save(newLog);
        
        station.setAvailableBatteries((int) batteryRepository.findByCurrentStationId(stationId).stream()
                .filter(b -> "AVAILABLE".equals(b.getStatus()) && b.getCurrentLevel() >= 80)
                .count());
        station.setChargingBatteries((int) batteryRepository.findByCurrentStationId(stationId).stream()
                .filter(b -> "CHARGING".equals(b.getStatus()))
                .count());
        swapStationRepository.save(station);
        
        result.put("success", true);
        result.put("message", "换电成功");
        result.put("oldBattery", oldBattery);
        result.put("newBattery", newBattery);
        return result;
    }

    @Cacheable(value = "swapStations", key = "'all'")
    public List<SwapStation> getAllSwapStations() {
        return swapStationRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<SwapStation> getSwapStationsWithAvailableBatteries() {
        return swapStationRepository.findByAvailableBatteriesGreaterThanEqual(1);
    }

    public SwapStation getSwapStationById(Long id) {
        return swapStationRepository.findById(id).orElse(null);
    }

    public SwapStation getSwapStationByNo(String stationNo) {
        return swapStationRepository.findByStationNo(stationNo).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "swapStations", allEntries = true)
    public SwapStation createSwapStation(SwapStation station) {
        String stationNo = "ST" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        station.setStationNo(stationNo);
        if (station.getStatus() == null) {
            station.setStatus("OPEN");
        }
        if (station.getAvailableBatteries() == null) {
            station.setAvailableBatteries(0);
        }
        if (station.getChargingBatteries() == null) {
            station.setChargingBatteries(0);
        }
        return swapStationRepository.save(station);
    }

    @Transactional
    @CacheEvict(value = "swapStations", allEntries = true)
    public SwapStation updateSwapStation(Long id, SwapStation station) {
        SwapStation existing = swapStationRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (station.getStationName() != null) existing.setStationName(station.getStationName());
        if (station.getAddress() != null) existing.setAddress(station.getAddress());
        if (station.getLongitude() != null) existing.setLongitude(station.getLongitude());
        if (station.getLatitude() != null) existing.setLatitude(station.getLatitude());
        if (station.getTotalSlots() != null) existing.setTotalSlots(station.getTotalSlots());
        if (station.getStatus() != null) existing.setStatus(station.getStatus());
        if (station.getBusinessHours() != null) existing.setBusinessHours(station.getBusinessHours());
        if (station.getContactPhone() != null) existing.setContactPhone(station.getContactPhone());
        return swapStationRepository.save(existing);
    }

    public List<BatteryLog> getBatteryLogs(Long batteryId) {
        if (batteryId != null) {
            return batteryLogRepository.findByBatteryId(batteryId);
        }
        return batteryLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<Battery> getLowBatteryWarning() {
        List<Battery> lowBatteries = batteryRepository.findByCurrentLevelLessThanEqual(20);
        return lowBatteries.stream()
                .filter(b -> "IN_USE".equals(b.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    @CacheEvict(value = "batteries", allEntries = true)
    public void simulateBatteryDischarge() {
        List<Battery> batteries = batteryRepository.findByStatus("IN_USE");
        for (Battery battery : batteries) {
            if (battery.getCurrentLevel() > 0) {
                int newLevel = Math.max(0, battery.getCurrentLevel() - 1);
                battery.setCurrentLevel(newLevel);
                batteryRepository.save(battery);
                
                if (battery.getCurrentBikeId() != null) {
                    Bike bike = bikeRepository.findById(battery.getCurrentBikeId()).orElse(null);
                    if (bike != null) {
                        bike.setBatteryLevel(newLevel);
                        bikeRepository.save(bike);
                    }
                }
            }
        }
    }

    public List<Map<String, Object>> getBatteryLifeCycleStats() {
        List<Map<String, Object>> stats = new ArrayList<>();
        List<Battery> batteries = batteryRepository.findAll();
        
        for (Battery battery : batteries) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("batteryId", battery.getId());
            stat.put("batteryNo", battery.getBatteryNo());
            stat.put("model", battery.getModel());
            stat.put("capacity", battery.getCapacity());
            stat.put("healthDegree", battery.getHealthDegree());
            stat.put("totalChargeCount", battery.getTotalChargeCount());
            stat.put("currentLevel", battery.getCurrentLevel());
            stat.put("status", battery.getStatus());
            stat.put("manufactureDate", battery.getManufactureDate());
            stat.put("installDate", battery.getInstallDate());
            stats.add(stat);
        }
        
        return stats;
    }
}
