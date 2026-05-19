package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "iot_device_data")
public class IotDeviceData extends BaseEntity {

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "device_name", length = 200)
    private String deviceName;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "heritage_id")
    private Long heritageId;

    @Column(name = "heritage_code", length = 50)
    private String heritageCode;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "temperature")
    private BigDecimal temperature;

    @Column(name = "humidity")
    private BigDecimal humidity;

    @Column(name = "light_intensity")
    private BigDecimal lightIntensity;

    @Column(name = "pressure")
    private BigDecimal pressure;

    @Column(name = "vibration")
    private BigDecimal vibration;

    @Column(name = "battery_level")
    private BigDecimal batteryLevel;

    @Column(name = "signal_strength")
    private BigDecimal signalStrength;

    @Column(name = "data_time", nullable = false)
    private LocalDateTime dataTime;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true;

    @Column(name = "validation_message", length = 500)
    private String validationMessage;

    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;
}
