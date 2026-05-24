package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 热力点实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "heat_point")
public class HeatPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 6)
    private Double longitude;

    @Column(precision = 10, scale = 6)
    private Double latitude;

    @Column(length = 100)
    private String locationName;

    @Column(nullable = false)
    private Integer heatValue;

    @Column(length = 50)
    private String pointType;

    @Column
    private Long areaId;

    @Column(nullable = false)
    private LocalDateTime recordTime;

    @Column
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
