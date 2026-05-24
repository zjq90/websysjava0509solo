package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 维修工单实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "repair_order")
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(nullable = false, length = 50)
    private String taskNo;

    @Column(nullable = false, length = 20)
    private String priority;

    @Column(nullable = false, length = 20)
    private String status;

    @Column
    private Long bikeId;

    @Column(length = 50)
    private String bikeNo;

    @Column(length = 100)
    private String bikeLocation;

    @Column(precision = 10, scale = 6)
    private Double bikeLongitude;

    @Column(precision = 10, scale = 6)
    private Double bikeLatitude;

    @Column(length = 50)
    private String faultType;

    @Column(length = 500)
    private String faultDescription;

    @Column
    private Long staffId;

    @Column(length = 50)
    private String staffName;

    @Column(length = 2000)
    private String usedParts;

    @Column(length = 500)
    private String repairDescription;

    @Column(length = 500)
    private String repairImages;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime acceptTime;

    @Column
    private LocalDateTime startRepairTime;

    @Column
    private LocalDateTime completeTime;

    @Column
    private Integer repairDuration;

    @Column
    private Float repairCost;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
