package com.appsys.service;

import com.appsys.entity.FaultReport;
import com.appsys.repository.FaultReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class FaultReportService {

    @Autowired
    private FaultReportRepository faultReportRepository;

    public List<FaultReport> findByUserId(Long userId) {
        return faultReportRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Optional<FaultReport> findById(Long id) {
        return faultReportRepository.findById(id);
    }

    public Optional<FaultReport> findByReportNo(String reportNo) {
        return faultReportRepository.findByReportNo(reportNo);
    }

    public FaultReport save(FaultReport faultReport) {
        if (faultReport.getReportNo() == null) {
            String reportNo = "FR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + new Random().nextInt(1000);
            faultReport.setReportNo(reportNo);
        }
        return faultReportRepository.save(faultReport);
    }

    public Map<String, Object> diagnose(String faultType) {
        Map<String, Object> result = new HashMap<>();
        result.put("modemStatus", "正常");
        result.put("signalStrength", -65 + new Random().nextInt(30));
        result.put("dnsStatus", "正常");
        result.put("ping", 15 + new Random().nextInt(30));

        StringBuilder solution = new StringBuilder();
        switch (faultType) {
            case "无法上网":
                solution.append("1. 请检查光猫和路由器电源是否正常；");
                solution.append("2. 尝试重启光猫和路由器；");
                solution.append("3. 检查网线连接是否松动；");
                break;
            case "网速慢":
                solution.append("1. 检查是否有设备正在下载大文件；");
                solution.append("2. 尝试靠近路由器使用；");
                solution.append("3. 更改WiFi信道减少干扰；");
                break;
            case "频繁掉线":
                solution.append("1. 检查光猫指示灯状态是否正常；");
                solution.append("2. 联系运营商检查线路状态；");
                solution.append("3. 尝试更换路由器位置；");
                break;
            default:
                solution.append("建议联系人工客服进行详细排查。");
        }
        result.put("suggestion", solution.toString());
        return result;
    }

    public FaultReport updateStatus(Long id, String status) {
        Optional<FaultReport> reportOpt = faultReportRepository.findById(id);
        if (reportOpt.isPresent()) {
            FaultReport report = reportOpt.get();
            report.setStatus(status);
            if ("COMPLETED".equals(status)) {
                report.setCompleteTime(LocalDateTime.now());
            }
            return faultReportRepository.save(report);
        }
        return null;
    }

    public void deleteById(Long id) {
        faultReportRepository.deleteById(id);
    }
}
