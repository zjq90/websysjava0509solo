package com.bike.service;

import com.bike.entity.Bike;
import com.bike.entity.RepairOrder;
import com.bike.entity.FaultRecord;
import com.bike.repository.BikeRepository;
import com.bike.repository.RepairOrderRepository;
import com.bike.repository.FaultRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 车辆管理服务
 * 
 * @author bike-sharing
 */
@Service
public class BikeService {

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private FaultRecordRepository faultRecordRepository;

    @Cacheable(value = "bikes", key = "'all'")
    public List<Bike> getAllBikes() {
        return bikeRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<Bike> getBikesByStatus(String status) {
        return bikeRepository.findByStatus(status);
    }

    public List<Bike> getBikesByAreaId(Long areaId) {
        return bikeRepository.findByCurrentAreaId(areaId);
    }

    public List<Bike> getLowBatteryBikes() {
        return bikeRepository.findByIsElectricTrueAndBatteryLevelLessThanEqual(20);
    }

    public Bike getBikeById(Long id) {
        return bikeRepository.findById(id).orElse(null);
    }

    public Bike getBikeByBikeNo(String bikeNo) {
        return bikeRepository.findByBikeNo(bikeNo).orElse(null);
    }

    public Bike getBikeByQrCode(String qrCode) {
        return bikeRepository.findByQrCode(qrCode).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "bikes", allEntries = true)
    public Bike createBike(Bike bike) {
        String bikeNo = "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        bike.setBikeNo(bikeNo);
        bike.setQrCode("QR" + bikeNo);
        if (bike.getStatus() == null) {
            bike.setStatus("AVAILABLE");
        }
        if (bike.getIsElectric() == null) {
            bike.setIsElectric(false);
        }
        if (bike.getType() == null) {
            bike.setType(bike.getIsElectric() ? "ELECTRIC" : "STANDARD");
        }
        return bikeRepository.save(bike);
    }

    @Transactional
    @CacheEvict(value = "bikes", allEntries = true)
    public Bike updateBike(Long id, Bike bike) {
        Bike existing = bikeRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (bike.getStatus() != null) existing.setStatus(bike.getStatus());
        if (bike.getLongitude() != null) existing.setLongitude(bike.getLongitude());
        if (bike.getLatitude() != null) existing.setLatitude(bike.getLatitude());
        if (bike.getLocation() != null) existing.setLocation(bike.getLocation());
        if (bike.getFaultType() != null) existing.setFaultType(bike.getFaultType());
        if (bike.getFaultDescription() != null) existing.setFaultDescription(bike.getFaultDescription());
        if (bike.getBatteryLevel() != null) existing.setBatteryLevel(bike.getBatteryLevel());
        if (bike.getBatteryId() != null) existing.setBatteryId(bike.getBatteryId());
        if (bike.getCurrentAreaId() != null) existing.setCurrentAreaId(bike.getCurrentAreaId());
        return bikeRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "bikes", allEntries = true)
    public void deleteBike(Long id) {
        bikeRepository.deleteById(id);
    }

    public Map<String, Object> getBikeDetail(Long bikeId) {
        Map<String, Object> result = new HashMap<>();
        
        Bike bike = bikeRepository.findById(bikeId).orElse(null);
        if (bike == null) {
            return null;
        }
        
        result.put("bike", bike);
        
        List<FaultRecord> faultRecords = faultRecordRepository.findByBikeId(bikeId);
        result.put("faultRecords", faultRecords);
        
        List<RepairOrder> repairOrders = repairOrderRepository.findByBikeId(bikeId);
        result.put("repairOrders", repairOrders);
        
        return result;
    }

    public Map<String, Object> getBikeStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalBikes = bikeRepository.count();
        long availableBikes = bikeRepository.countByStatus("AVAILABLE");
        long inUseBikes = bikeRepository.countByStatus("IN_USE");
        long faultBikes = bikeRepository.countByStatus("FAULT");
        long maintenanceBikes = bikeRepository.countByStatus("MAINTENANCE");
        
        stats.put("totalBikes", totalBikes);
        stats.put("availableBikes", availableBikes);
        stats.put("inUseBikes", inUseBikes);
        stats.put("faultBikes", faultBikes);
        stats.put("maintenanceBikes", maintenanceBikes);
        stats.put("utilizationRate", totalBikes > 0 ? (inUseBikes * 100.0 / totalBikes) : 0);
        
        return stats;
    }
}
