package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 区域实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String areaCode;

    @Column(nullable = false, length = 100)
    private String areaName;

    @Column(precision = 10, scale = 6)
    private Double centerLongitude;

    @Column(precision = 10, scale = 6)
    private Double centerLatitude;

    @Column
    private Integer bikeCount = 0;

    @Column
    private Integer demandLevel;

    @Column(length = 50)
    private String areaType;

    @Column(nullable = false)
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
