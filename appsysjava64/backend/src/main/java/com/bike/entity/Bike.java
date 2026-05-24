package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 车辆实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "bike")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String bikeNo;

    @Column(nullable = false, length = 50)
    private String qrCode;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(precision = 10, scale = 6)
    private Double longitude;

    @Column(precision = 10, scale = 6)
    private Double latitude;

    @Column(length = 100)
    private String location;

    @Column(length = 200)
    private String faultType;

    @Column(length = 500)
    private String faultDescription;

    @Column(nullable = false)
    private Boolean isElectric = false;

    @Column
    private Integer batteryLevel;

    @Column
    private Long batteryId;

    @Column
    private Long currentAreaId;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @Column
    private LocalDateTime lastMaintenanceTime;

    @Column
    private Integer totalRideCount = 0;

    @Column
    private Integer totalMileage = 0;

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
