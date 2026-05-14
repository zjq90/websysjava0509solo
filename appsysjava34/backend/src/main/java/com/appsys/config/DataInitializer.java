package com.appsys.config;

import com.appsys.entity.*;
import com.appsys.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private FaultReportRepository faultReportRepository;

    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;

    @Autowired
    private WiFiSettingRepository wiFiSettingRepository;

    @Autowired
    private ChildGuardRepository childGuardRepository;

    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("123456");
        user.setPhone("13800138000");
        user.setIdCard("110101199001011234");
        user.setRealName("张三");
        user.setEmail("zhangsan@example.com");
        user.setAddress("北京市朝阳区");
        user.setStatus(1);
        user.setElderMode(false);
        userRepository.save(user);

        ServiceOrder order = new ServiceOrder();
        order.setOrderNo("SO202401010001");
        order.setUserId(user.getId());
        order.setServiceType("宽带安装");
        order.setStatus("装维派单");
        order.setCurrentStep(2);
        order.setTotalSteps(4);
        order.setWorkerName("李师傅");
        order.setWorkerPhone("13900139000");
        order.setWorkerLat(39.9042);
        order.setWorkerLng(116.4074);
        order.setAddress("北京市朝阳区");
        order.setDescription("100M宽带安装");
        serviceOrderRepository.save(order);

        FaultReport report = new FaultReport();
        report.setReportNo("FR202401010001");
        report.setUserId(user.getId());
        report.setFaultType("无法上网");
        report.setFaultDesc("今天突然无法上网，光猫灯亮");
        report.setStatus("PROCESSING");
        report.setWorkerName("王师傅");
        report.setWorkerPhone("13700137000");
        report.setAddress("北京市朝阳区");
        faultReportRepository.save(report);

        String[] deviceNames = {"iPhone 15", "MacBook Pro", "小米电视", "iPad Air", "华为Mate60"};
        String[] deviceTypes = {"手机", "电脑", "电视", "平板", "手机"};
        for (int i = 0; i < 5; i++) {
            NetworkDevice device = new NetworkDevice();
            device.setUserId(user.getId());
            device.setDeviceId("DEV" + (i + 1));
            device.setDeviceName(deviceNames[i]);
            device.setDeviceType(deviceTypes[i]);
            device.setMacAddress("00:11:22:33:44:5" + i);
            device.setIpAddress("192.168.1." + (100 + i));
            device.setSignalStrength(70 + i * 5);
            device.setStatus("ONLINE");
            device.setUploadSpeed(10.5 + i);
            device.setDownloadSpeed(50.5 + i * 10);
            device.setIsBlocked(false);
            if (i == 0) {
                device.setPosition("客厅");
            } else if (i == 4) {
                device.setParentDeviceId("DEV1");
                device.setPosition("卧室");
            } else {
                device.setPosition("书房");
            }
            networkDeviceRepository.save(device);
        }

        WiFiSetting wiFiSetting = new WiFiSetting();
        wiFiSetting.setUserId(user.getId());
        wiFiSetting.setSsid("HomeWiFi_2.4G");
        wiFiSetting.setPassword("12345678");
        wiFiSetting.setIsHidden(false);
        wiFiSetting.setBand("2.4G");
        wiFiSetting.setChannel(6);
        wiFiSetting.setGuestSsid("GuestWiFi");
        wiFiSetting.setGuestPassword("guest123");
        wiFiSetting.setGuestEnabled(true);
        wiFiSetting.setStatus("ENABLED");
        wiFiSettingRepository.save(wiFiSetting);

        ChildGuard childGuard = new ChildGuard();
        childGuard.setUserId(user.getId());
        childGuard.setChildName("小明");
        childGuard.setDeviceId("DEV1");
        childGuard.setStartTime("08:00");
        childGuard.setEndTime("21:00");
        childGuard.setWeekDays("1,2,3,4,5");
        childGuard.setAppWhitelist("微信,QQ,腾讯视频");
        childGuard.setAntiAddiction(true);
        childGuard.setReminderTime(60);
        childGuard.setEnabled(true);
        childGuardRepository.save(childGuard);
    }
}
