package com.petclinic.service;

import com.petclinic.entity.PetDailyData;
import com.petclinic.entity.SmartDevice;
import com.petclinic.repository.PetDailyDataRepository;
import com.petclinic.repository.SmartDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 智能设备Service
 * 管理智能喂食器、饮水机等设备及宠物日常数据
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Service
public class SmartDeviceService {

    @Autowired
    private SmartDeviceRepository smartDeviceRepository;

    @Autowired
    private PetDailyDataRepository petDailyDataRepository;

    // ==================== 智能设备管理 ====================

    public List<SmartDevice> findAllDevices() {
        return smartDeviceRepository.findAll();
    }

    public Optional<SmartDevice> findDeviceById(Long id) {
        return smartDeviceRepository.findById(id);
    }

    public SmartDevice saveDevice(SmartDevice device) {
        return smartDeviceRepository.save(device);
    }

    public SmartDevice updateDevice(Long id, SmartDevice device) {
        Optional<SmartDevice> optional = smartDeviceRepository.findById(id);
        if (optional.isPresent()) {
            SmartDevice existing = optional.get();
            existing.setDeviceName(device.getDeviceName());
            existing.setDeviceCode(device.getDeviceCode());
            existing.setDeviceType(device.getDeviceType());
            existing.setDeviceModel(device.getDeviceModel());
            existing.setPetId(device.getPetId());
            existing.setPetName(device.getPetName());
            existing.setStatus(device.getStatus());
            existing.setLocation(device.getLocation());
            existing.setLastOnlineTime(LocalDateTime.now().toString());
            return smartDeviceRepository.save(existing);
        }
        return null;
    }

    public void deleteDeviceById(Long id) {
        smartDeviceRepository.deleteById(id);
    }

    public List<SmartDevice> findDevicesByStatus(String status) {
        return smartDeviceRepository.findByStatus(status);
    }

    public List<SmartDevice> findDevicesByType(String deviceType) {
        return smartDeviceRepository.findByDeviceType(deviceType);
    }

    public SmartDevice updateDeviceStatus(Long id, String status) {
        Optional<SmartDevice> optional = smartDeviceRepository.findById(id);
        if (optional.isPresent()) {
            SmartDevice device = optional.get();
            device.setStatus(status);
            device.setLastOnlineTime(LocalDateTime.now().toString());
            return smartDeviceRepository.save(device);
        }
        return null;
    }

    // ==================== 宠物日常数据管理 ====================

    public List<PetDailyData> findAllData() {
        return petDailyDataRepository.findAll();
    }

    public Optional<PetDailyData> findDataById(Long id) {
        return petDailyDataRepository.findById(id);
    }

    public PetDailyData saveData(PetDailyData data) {
        return petDailyDataRepository.save(data);
    }

    public void deleteDataById(Long id) {
        petDailyDataRepository.deleteById(id);
    }

    public List<PetDailyData> findDataByDeviceId(Long deviceId) {
        return petDailyDataRepository.findByDeviceId(deviceId);
    }

    public List<PetDailyData> findDataByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return petDailyDataRepository.findByRecordTimeBetween(startTime, endTime);
    }

    public List<PetDailyData> findRecentData(int limit) {
        return petDailyDataRepository.findTopNRecords(limit);
    }

    /**
     * 模拟设备上报数据
     */
    public PetDailyData generateMockData(Long deviceId) {
        Optional<SmartDevice> deviceOpt = smartDeviceRepository.findById(deviceId);
        if (!deviceOpt.isPresent()) {
            return null;
        }

        SmartDevice device = deviceOpt.get();
        Random random = new Random();

        PetDailyData data = new PetDailyData();
        data.setDeviceId(deviceId);
        data.setDeviceName(device.getDeviceName());
        data.setPetName(device.getPetName());

        if ("FEEDER".equals(device.getDeviceType())) {
            data.setFoodIntake(new BigDecimal(50 + random.nextInt(100)));
            data.setFeedCount(1 + random.nextInt(5));
            data.setWaterIntake(new BigDecimal(0));
            data.setDrinkCount(0);
        } else if ("WATER_DISPENSER".equals(device.getDeviceType())) {
            data.setFoodIntake(new BigDecimal(0));
            data.setFeedCount(0);
            data.setWaterIntake(new BigDecimal(100 + random.nextInt(200)));
            data.setDrinkCount(3 + random.nextInt(10));
        } else {
            data.setFoodIntake(new BigDecimal(50 + random.nextInt(100)));
            data.setFeedCount(1 + random.nextInt(5));
            data.setWaterIntake(new BigDecimal(100 + random.nextInt(200)));
            data.setDrinkCount(3 + random.nextInt(10));
        }

        data.setTemperature(new BigDecimal(20 + random.nextInt(15) + random.nextDouble()));
        data.setHumidity(new BigDecimal(40 + random.nextInt(40) + random.nextDouble()));
        data.setStatus(random.nextDouble() > 0.95 ? "ABNORMAL" : "NORMAL");
        data.setRemarks("设备自动上报数据");
        data.setRecordTime(LocalDateTime.now());

        return petDailyDataRepository.save(data);
    }
}
