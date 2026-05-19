package com.heritage.service;

import com.heritage.entity.IotDeviceData;
import com.heritage.entity.RiskAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RiskAlertService {

    private Map<String, BigDecimal> temperatureThresholds = new HashMap<>();
    private Map<String, BigDecimal> humidityThresholds = new HashMap<>();
    private Map<String, BigDecimal> vibrationThresholds = new HashMap<>();

    public RiskAlertService() {
        temperatureThresholds.put("default", new BigDecimal("30"));
        temperatureThresholds.put("warning", new BigDecimal("35"));
        temperatureThresholds.put("critical", new BigDecimal("40"));

        humidityThresholds.put("default", new BigDecimal("60"));
        humidityThresholds.put("warning", new BigDecimal("70"));
        humidityThresholds.put("critical", new BigDecimal("80"));

        vibrationThresholds.put("default", new BigDecimal("0.5"));
        vibrationThresholds.put("warning", new BigDecimal("1.0"));
        vibrationThresholds.put("critical", new BigDecimal("2.0"));
    }

    public List<RiskAlert> checkEnvironmentRisks(List<IotDeviceData> deviceDataList) {
        List<RiskAlert> alerts = new ArrayList<>();

        for (IotDeviceData data : deviceDataList) {
            if (data.getTemperature() != null) {
                RiskAlert tempAlert = checkTemperature(data);
                if (tempAlert != null) {
                    alerts.add(tempAlert);
                }
            }

            if (data.getHumidity() != null) {
                RiskAlert humidityAlert = checkHumidity(data);
                if (humidityAlert != null) {
                    alerts.add(humidityAlert);
                }
            }

            if (data.getVibration() != null) {
                RiskAlert vibrationAlert = checkVibration(data);
                if (vibrationAlert != null) {
                    alerts.add(vibrationAlert);
                }
            }
        }

        log.info("环境风险检测完成，生成 {} 条预警", alerts.size());
        return alerts;
    }

    private RiskAlert checkTemperature(IotDeviceData data) {
        BigDecimal temperature = data.getTemperature();

        if (temperature.compareTo(temperatureThresholds.get("critical")) >= 0) {
            return createAlert(data, "TEMPERATURE", "critical", temperature, temperatureThresholds.get("critical"));
        } else if (temperature.compareTo(temperatureThresholds.get("warning")) >= 0) {
            return createAlert(data, "TEMPERATURE", "warning", temperature, temperatureThresholds.get("warning"));
        }

        return null;
    }

    private RiskAlert checkHumidity(IotDeviceData data) {
        BigDecimal humidity = data.getHumidity();

        if (humidity.compareTo(humidityThresholds.get("critical")) >= 0) {
            return createAlert(data, "HUMIDITY", "critical", humidity, humidityThresholds.get("critical"));
        } else if (humidity.compareTo(humidityThresholds.get("warning")) >= 0) {
            return createAlert(data, "HUMIDITY", "warning", humidity, humidityThresholds.get("warning"));
        }

        return null;
    }

    private RiskAlert checkVibration(IotDeviceData data) {
        BigDecimal vibration = data.getVibration();

        if (vibration.compareTo(vibrationThresholds.get("critical")) >= 0) {
            return createAlert(data, "VIBRATION", "critical", vibration, vibrationThresholds.get("critical"));
        } else if (vibration.compareTo(vibrationThresholds.get("warning")) >= 0) {
            return createAlert(data, "VIBRATION", "warning", vibration, vibrationThresholds.get("warning"));
        }

        return null;
    }

    private RiskAlert createAlert(IotDeviceData data, String alertType, String level, BigDecimal currentValue, BigDecimal thresholdValue) {
        RiskAlert alert = new RiskAlert();
        alert.setAlertNo("ALT" + System.currentTimeMillis());
        alert.setAlertType(alertType);
        alert.setAlertLevel(level);
        alert.setTargetType("device");
        alert.setTargetId(Long.parseLong(data.getDeviceId()));
        alert.setTargetName(data.getDeviceName());
        alert.setAlertTitle(getAlertTitle(alertType, level));
        alert.setAlertContent(getAlertContent(alertType, level, currentValue, thresholdValue));
        alert.setCurrentValue(currentValue);
        alert.setThresholdValue(thresholdValue);
        alert.setAlertTime(LocalDateTime.now());
        alert.setStatus("PENDING");

        return alert;
    }

    private String getAlertTitle(String alertType, String level) {
        String levelText = "critical".equals(level) ? "严重" : "警告";
        switch (alertType) {
            case "TEMPERATURE":
                return "温度" + levelText + "预警";
            case "HUMIDITY":
                return "湿度" + levelText + "预警";
            case "VIBRATION":
                return "震动" + levelText + "预警";
            default:
                return "未知预警";
        }
    }

    private String getAlertContent(String alertType, String level, BigDecimal currentValue, BigDecimal thresholdValue) {
        String typeText;
        switch (alertType) {
            case "TEMPERATURE":
                typeText = "温度";
                break;
            case "HUMIDITY":
                typeText = "湿度";
                break;
            case "VIBRATION":
                typeText = "震动";
                break;
            default:
                typeText = "未知指标";
        }
        return String.format("%s当前值为 %s，超过阈值 %s，请及时处理", typeText, currentValue, thresholdValue);
    }

    public void updateThreshold(String alertType, String level, BigDecimal newValue) {
        switch (alertType) {
            case "TEMPERATURE":
                temperatureThresholds.put(level, newValue);
                break;
            case "HUMIDITY":
                humidityThresholds.put(level, newValue);
                break;
            case "VIBRATION":
                vibrationThresholds.put(level, newValue);
                break;
        }
        log.info("更新 {} {} 级阈值为: {}", alertType, level, newValue);
    }

    public Map<String, Map<String, BigDecimal>> getAllThresholds() {
        Map<String, Map<String, BigDecimal>> allThresholds = new HashMap<>();
        allThresholds.put("temperature", new HashMap<>(temperatureThresholds));
        allThresholds.put("humidity", new HashMap<>(humidityThresholds));
        allThresholds.put("vibration", new HashMap<>(vibrationThresholds));
        return allThresholds;
    }

    public Map<String, Object> getRiskStatistics(List<RiskAlert> alerts) {
        Map<String, Object> stats = new HashMap<>();
        int criticalCount = 0, warningCount = 0, pendingCount = 0, handledCount = 0;

        for (RiskAlert alert : alerts) {
            if ("critical".equals(alert.getAlertLevel())) {
                criticalCount++;
            } else if ("warning".equals(alert.getAlertLevel())) {
                warningCount++;
            }

            if ("PENDING".equals(alert.getStatus())) {
                pendingCount++;
            } else if ("HANDLED".equals(alert.getStatus())) {
                handledCount++;
            }
        }

        stats.put("totalAlerts", alerts.size());
        stats.put("criticalCount", criticalCount);
        stats.put("warningCount", warningCount);
        stats.put("pendingCount", pendingCount);
        stats.put("handledCount", handledCount);

        return stats;
    }
}
