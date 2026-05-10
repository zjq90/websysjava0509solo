package com.websys.service;

import com.websys.entity.DeviceStatus;
import com.websys.repository.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 设备状态服务类
 * 提供设备状态管理的业务逻辑，包括状态查询、远程控制等操作
 */
@Service
public class DeviceStatusService {

    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    /**
     * 查询设备最新状态
     * @param deviceId 设备ID
     * @return 设备状态
     */
    public Optional<DeviceStatus> findLatestByDeviceId(Long deviceId) {
        return deviceStatusRepository.findTopByDeviceIdOrderByReportTimeDesc(deviceId);
    }

    /**
     * 根据设备编号查询最新状态
     * @param deviceCode 设备编号
     * @return 设备状态
     */
    public Optional<DeviceStatus> findLatestByDeviceCode(String deviceCode) {
        return deviceStatusRepository.findTopByDeviceCodeOrderByReportTimeDesc(deviceCode);
    }

    /**
     * 查询设备历史状态
     * @param deviceId 设备ID
     * @return 状态记录列表
     */
    public List<DeviceStatus> findHistoryByDeviceId(Long deviceId) {
        return deviceStatusRepository.findByDeviceIdOrderByReportTimeDesc(deviceId);
    }

    /**
     * 保存设备状态
     * @param deviceStatus 设备状态对象
     * @return 保存后的状态对象
     */
    @Transactional
    public DeviceStatus save(DeviceStatus deviceStatus) {
        deviceStatus.setReportTime(LocalDateTime.now());
        return deviceStatusRepository.save(deviceStatus);
    }

    /**
     * 远程重启设备
     * @param deviceId 设备ID
     * @return 操作结果
     */
    @Transactional
    public String restartDevice(Long deviceId) {
        Optional<DeviceStatus> statusOpt = findLatestByDeviceId(deviceId);
        if (statusOpt.isPresent()) {
            DeviceStatus status = statusOpt.get();
            DeviceStatus newStatus = new DeviceStatus();
            newStatus.setDeviceId(status.getDeviceId());
            newStatus.setDeviceCode(status.getDeviceCode());
            newStatus.setNetworkQuality(1);
            newStatus.setSignalStrength(status.getSignalStrength());
            newStatus.setMotorStatus(1);
            newStatus.setDoorStatus(1);
            newStatus.setTemperature(status.getTemperature());
            newStatus.setHumidity(status.getHumidity());
            newStatus.setPowerStatus(1);
            newStatus.setBatteryLevel(status.getBatteryLevel());
            newStatus.setTargetTemperature(status.getTargetTemperature());
            newStatus.setAdvertContent(status.getAdvertContent());
            save(newStatus);
            return "设备重启指令已发送";
        }
        throw new RuntimeException("设备状态不存在");
    }

    /**
     * 远程开锁
     * @param deviceId 设备ID
     * @return 操作结果
     */
    @Transactional
    public String unlockDevice(Long deviceId) {
        Optional<DeviceStatus> statusOpt = findLatestByDeviceId(deviceId);
        if (statusOpt.isPresent()) {
            DeviceStatus status = statusOpt.get();
            DeviceStatus newStatus = cloneStatus(status);
            newStatus.setDoorStatus(2);
            save(newStatus);
            return "设备开锁指令已发送";
        }
        throw new RuntimeException("设备状态不存在");
    }

    /**
     * 调整温度
     * @param deviceId 设备ID
     * @param targetTemperature 目标温度
     * @return 操作结果
     */
    @Transactional
    public String adjustTemperature(Long deviceId, BigDecimal targetTemperature) {
        Optional<DeviceStatus> statusOpt = findLatestByDeviceId(deviceId);
        if (statusOpt.isPresent()) {
            DeviceStatus status = statusOpt.get();
            DeviceStatus newStatus = cloneStatus(status);
            newStatus.setTargetTemperature(targetTemperature);
            newStatus.setTemperature(targetTemperature);
            save(newStatus);
            return "温度调整指令已发送，目标温度：" + targetTemperature + "℃";
        }
        throw new RuntimeException("设备状态不存在");
    }

    /**
     * 设置屏幕广告
     * @param deviceId 设备ID
     * @param advertContent 广告内容
     * @return 操作结果
     */
    @Transactional
    public String setAdvertContent(Long deviceId, String advertContent) {
        Optional<DeviceStatus> statusOpt = findLatestByDeviceId(deviceId);
        if (statusOpt.isPresent()) {
            DeviceStatus status = statusOpt.get();
            DeviceStatus newStatus = cloneStatus(status);
            newStatus.setAdvertContent(advertContent);
            save(newStatus);
            return "广告内容设置成功";
        }
        throw new RuntimeException("设备状态不存在");
    }

    /**
     * 固件升级
     * @param deviceIds 设备ID列表
     * @return 操作结果
     */
    @Transactional
    public String firmwareUpgrade(List<Long> deviceIds) {
        for (Long deviceId : deviceIds) {
            Optional<DeviceStatus> statusOpt = findLatestByDeviceId(deviceId);
            if (statusOpt.isPresent()) {
                DeviceStatus status = statusOpt.get();
                DeviceStatus newStatus = cloneStatus(status);
                save(newStatus);
            }
        }
        return "固件升级指令已发送到 " + deviceIds.size() + " 台设备";
    }

    /**
     * 克隆设备状态
     * @param source 源状态
     * @return 新状态
     */
    private DeviceStatus cloneStatus(DeviceStatus source) {
        DeviceStatus target = new DeviceStatus();
        target.setDeviceId(source.getDeviceId());
        target.setDeviceCode(source.getDeviceCode());
        target.setNetworkQuality(source.getNetworkQuality());
        target.setSignalStrength(source.getSignalStrength());
        target.setMotorStatus(source.getMotorStatus());
        target.setDoorStatus(source.getDoorStatus());
        target.setTemperature(source.getTemperature());
        target.setHumidity(source.getHumidity());
        target.setPowerStatus(source.getPowerStatus());
        target.setBatteryLevel(source.getBatteryLevel());
        target.setTargetTemperature(source.getTargetTemperature());
        target.setAdvertContent(source.getAdvertContent());
        return target;
    }
}
