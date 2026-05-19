package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "risk_alert")
public class RiskAlert extends BaseEntity {

    @Column(name = "alert_no", unique = true, nullable = false, length = 50)
    private String alertNo;

    @Column(name = "alert_type", length = 50)
    private String alertType;

    @Column(name = "alert_level", length = 20)
    private String alertLevel;

    @Column(name = "target_type", length = 50)
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "target_name", length = 200)
    private String targetName;

    @Column(name = "alert_title", length = 200)
    private String alertTitle;

    @Column(name = "alert_content", columnDefinition = "TEXT")
    private String alertContent;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    @Column(name = "threshold_value")
    private BigDecimal thresholdValue;

    @Column(name = "threshold_id")
    private Long thresholdId;

    @Column(name = "alert_time", nullable = false)
    private LocalDateTime alertTime;

    @Column(name = "handler_id")
    private Long handlerId;

    @Column(name = "handler_name", length = 100)
    private String handlerName;

    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    @Column(name = "handle_result", length = 500)
    private String handleResult;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;
}
