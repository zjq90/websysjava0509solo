package com.appsys.service;

import com.appsys.entity.ChildGuard;
import com.appsys.entity.NetworkDevice;
import com.appsys.entity.WiFiSetting;
import com.appsys.repository.ChildGuardRepository;
import com.appsys.repository.NetworkDeviceRepository;
import com.appsys.repository.WiFiSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NetworkService {

    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;

    @Autowired
    private WiFiSettingRepository wiFiSettingRepository;

    @Autowired
    private ChildGuardRepository childGuardRepository;

    public List<NetworkDevice> findDevicesByUserId(Long userId) {
        return networkDeviceRepository.findByUserId(userId);
    }

    public Optional<NetworkDevice> findDeviceById(Long id) {
        return networkDeviceRepository.findById(id);
    }

    public NetworkDevice saveDevice(NetworkDevice device) {
        return networkDeviceRepository.save(device);
    }

    public NetworkDevice toggleDeviceBlock(Long deviceId) {
        Optional<NetworkDevice> deviceOpt = networkDeviceRepository.findById(deviceId);
        if (deviceOpt.isPresent()) {
            NetworkDevice device = deviceOpt.get();
            device.setIsBlocked(!device.getIsBlocked());
            return networkDeviceRepository.save(device);
        }
        return null;
    }

    public NetworkDevice setDeviceSpeedLimit(Long deviceId, Integer limit) {
        Optional<NetworkDevice> deviceOpt = networkDeviceRepository.findById(deviceId);
        if (deviceOpt.isPresent()) {
            NetworkDevice device = deviceOpt.get();
            device.setSpeedLimit(limit);
            return networkDeviceRepository.save(device);
        }
        return null;
    }

    public void deleteDevice(Long id) {
        networkDeviceRepository.deleteById(id);
    }

    public Optional<WiFiSetting> findWiFiSettingByUserId(Long userId) {
        return wiFiSettingRepository.findByUserId(userId);
    }

    public WiFiSetting saveWiFiSetting(WiFiSetting setting) {
        return wiFiSettingRepository.save(setting);
    }

    public WiFiSetting updateWiFiPassword(Long userId, String newPassword) {
        Optional<WiFiSetting> settingOpt = wiFiSettingRepository.findByUserId(userId);
        if (settingOpt.isPresent()) {
            WiFiSetting setting = settingOpt.get();
            setting.setPassword(newPassword);
            return wiFiSettingRepository.save(setting);
        }
        return null;
    }

    public WiFiSetting toggleWiFiVisibility(Long userId) {
        Optional<WiFiSetting> settingOpt = wiFiSettingRepository.findByUserId(userId);
        if (settingOpt.isPresent()) {
            WiFiSetting setting = settingOpt.get();
            setting.setIsHidden(!setting.getIsHidden());
            return wiFiSettingRepository.save(setting);
        }
        return null;
    }

    public List<ChildGuard> findChildGuardsByUserId(Long userId) {
        return childGuardRepository.findByUserId(userId);
    }

    public ChildGuard saveChildGuard(ChildGuard childGuard) {
        return childGuardRepository.save(childGuard);
    }

    public void deleteChildGuard(Long id) {
        childGuardRepository.deleteById(id);
    }

    public ChildGuard toggleChildGuard(Long id) {
        Optional<ChildGuard> guardOpt = childGuardRepository.findById(id);
        if (guardOpt.isPresent()) {
            ChildGuard guard = guardOpt.get();
            guard.setEnabled(!guard.getEnabled());
            return childGuardRepository.save(guard);
        }
        return null;
    }

    public void restartRouter(Long userId) {
    }

    public List<NetworkDevice> getNetworkTopology(Long userId) {
        return networkDeviceRepository.findByUserId(userId);
    }
}
