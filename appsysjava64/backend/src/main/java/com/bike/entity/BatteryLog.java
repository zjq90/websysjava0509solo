package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 电池操作日志实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "battery_log")
public class BatteryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long batteryId;

    @Column(length = 50)
    private String batteryNo;

    @Column(nullable = false, length = 30)
    private String operationType;

    @Column
    private Integer beforeLevel;

    @Column
    private Integer afterLevel;

    @Column
    private Long operatorId;

    @Column(length = 50)
    private String operatorName;

    @Column
    private Long stationId;

    @Column(length = 100)
    private String stationName;

    @Column
    private Long bikeId;

    @Column(length = 50)
    private String bikeNo;

    @Column(length = 500)
    private String remark;

    @Column
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
