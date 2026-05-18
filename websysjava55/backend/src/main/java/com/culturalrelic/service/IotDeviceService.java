package com.culturalrelic.service;

import com.culturalrelic.entity.IotDevice;
import com.culturalrelic.repository.IotDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 物联网设备业务逻辑层
 */
@Service
public class IotDeviceService {

    @Autowired
    private IotDeviceRepository iotDeviceRepository;

    /**
     * 新增设备
     */
    public IotDevice save(IotDevice device) {
        return iotDeviceRepository.save(device);
    }

    /**
     * 根据ID查询
     */
    public Optional<IotDevice> findById(Long id) {
        return iotDeviceRepository.findById(id);
    }

    /**
     * 查询所有（分页）
     */
    public Page<IotDevice> findAll(Pageable pageable) {
        return iotDeviceRepository.findAll(pageable);
    }

    /**
     * 查询所有
     */
    public List<IotDevice> findAll() {
        return iotDeviceRepository.findAll();
    }

    /**
     * 更新设备
     */
    public IotDevice update(IotDevice device) {
        return iotDeviceRepository.save(device);
    }

    /**
     * 删除设备
     */
    @Transactional
    public boolean delete(Long id) {
        int rows = iotDeviceRepository.logicDelete(id);
        return rows > 0;
    }

    /**
     * 根据设备编号查询
     */
    public IotDevice findByDeviceNo(String deviceNo) {
        return iotDeviceRepository.findByDeviceNo(deviceNo);
    }

    /**
     * 根据NFC标签ID查询
     */
    public IotDevice findByNfcTagId(String nfcTagId) {
        return iotDeviceRepository.findByNfcTagId(nfcTagId);
    }

    /**
     * 根据文物ID查询绑定设备
     */
    public List<IotDevice> findByRelicId(Long relicId) {
        return iotDeviceRepository.findByRelicId(relicId);
    }

    /**
     * 绑定设备到文物（NFC绑定）
     */
    @Transactional
    public IotDevice bindToRelic(Long deviceId, Long relicId, String relicName) {
        Optional<IotDevice> deviceOpt = iotDeviceRepository.findById(deviceId);
        if (deviceOpt.isPresent()) {
            IotDevice device = deviceOpt.get();
            device.setRelicId(relicId);
            device.setRelicName(relicName);
            device.setBound(1);
            device.setBindTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return iotDeviceRepository.save(device);
        }
        return null;
    }

    /**
     * 解绑设备
     */
    @Transactional
    public IotDevice unbind(Long deviceId) {
        Optional<IotDevice> deviceOpt = iotDeviceRepository.findById(deviceId);
        if (deviceOpt.isPresent()) {
            IotDevice device = deviceOpt.get();
            device.setRelicId(null);
            device.setRelicName(null);
            device.setBound(0);
            device.setBindTime(null);
            return iotDeviceRepository.save(device);
        }
        return null;
    }

    /**
     * 查询告警设备
     */
    public List<IotDevice> findByAlarmStatus(Integer alarmStatus) {
        return iotDeviceRepository.findByAlarmStatus(alarmStatus);
    }
}
