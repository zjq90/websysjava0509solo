package com.heritage.service;

import com.heritage.entity.IotDeviceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IotDataValidationService {

    private static final BigDecimal MIN_TEMPERATURE = new BigDecimal("-20");
    private static final BigDecimal MAX_TEMPERATURE = new BigDecimal("60");
    private static final BigDecimal MIN_HUMIDITY = BigDecimal.ZERO;
    private static final BigDecimal MAX_HUMIDITY = new BigDecimal("100");
    private static final BigDecimal MIN_LIGHT = BigDecimal.ZERO;
    private static final BigDecimal MAX_LIGHT = new BigDecimal("100000");

    public IotDeviceData validateAndSave(IotDeviceData data) {
        List<String> validationMessages = new ArrayList<>();

        if (data.getTemperature() != null) {
            if (data.getTemperature().compareTo(MIN_TEMPERATURE) < 0 || 
                data.getTemperature().compareTo(MAX_TEMPERATURE) > 0) {
                validationMessages.add("温度值超出有效范围: " + data.getTemperature());
            }
        }

        if (data.getHumidity() != null) {
            if (data.getHumidity().compareTo(MIN_HUMIDITY) < 0 || 
                data.getHumidity().compareTo(MAX_HUMIDITY) > 0) {
                validationMessages.add("湿度值超出有效范围: " + data.getHumidity());
            }
        }

        if (data.getLightIntensity() != null) {
            if (data.getLightIntensity().compareTo(MIN_LIGHT) < 0 || 
                data.getLightIntensity().compareTo(MAX_LIGHT) > 0) {
                validationMessages.add("光照强度超出有效范围: " + data.getLightIntensity());
            }
        }

        if (data.getDeviceId() == null || data.getDeviceId().trim().isEmpty()) {
            validationMessages.add("设备ID不能为空");
        }

        if (data.getDataTime() == null) {
            validationMessages.add("数据时间不能为空");
        }

        if (validationMessages.isEmpty()) {
            data.setIsValid(true);
            data.setValidationMessage("数据验证通过");
            log.info("设备数据验证通过: {}", data.getDeviceId());
        } else {
            data.setIsValid(false);
            data.setValidationMessage(String.join("; ", validationMessages));
            log.warn("设备数据验证失败: {} - {}", data.getDeviceId(), data.getValidationMessage());
        }

        return data;
    }

    public List<IotDeviceData> batchValidate(List<IotDeviceData> dataList) {
        List<IotDeviceData> validatedList = new ArrayList<>();
        for (IotDeviceData data : dataList) {
            validatedList.add(validateAndSave(data));
        }
        return validatedList;
    }

    public double calculateValidRate(List<IotDeviceData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return 0.0;
        }
        long validCount = dataList.stream()
                .filter(d -> Boolean.TRUE.equals(d.getIsValid()))
                .count();
        return (double) validCount / dataList.size() * 100;
    }
}
