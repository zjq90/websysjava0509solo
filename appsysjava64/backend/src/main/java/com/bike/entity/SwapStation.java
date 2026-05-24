package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 换电站实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "swap_station")
public class SwapStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String stationNo;

    @Column(nullable = false, length = 100)
    private String stationName;

    @Column(length = 200)
    private String address;

    @Column(precision = 10, scale = 6)
    private Double longitude;

    @Column(precision = 10, scale = 6)
    private Double latitude;

    @Column
    private Integer totalSlots;

    @Column
    private Integer availableBatteries;

    @Column
    private Integer chargingBatteries;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String businessHours;

    @Column(length = 20)
    private String contactPhone;

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
