package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "bikes")
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String bikeNo;

    @Column(nullable = false, length = 20)
    private String status = "ONLINE";

    @Column(nullable = false, length = 20)
    private String bikeType;

    @Column(nullable = false)
    private Long areaId;

    @Column(length = 100)
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Column(nullable = false)
    private Integer battery = 100;

    @Column(nullable = false)
    private Integer totalRides = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDistance = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalCost = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer depreciationMonths = 36;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}
