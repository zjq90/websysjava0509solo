package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 电池实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "battery")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String batteryNo;

    @Column(length = 50)
    private String model;

    @Column
    private Integer capacity;

    @Column
    private Integer currentLevel;

    @Column
    private Float healthDegree;

    @Column
    private Integer chargeCount;

    @Column
    private Integer totalChargeCount;

    @Column(length = 20)
    private String status;

    @Column
    private Long currentBikeId;

    @Column
    private Long currentStationId;

    @Column
    private LocalDateTime manufactureDate;

    @Column
    private LocalDateTime installDate;

    @Column
    private LocalDateTime lastChargeTime;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
