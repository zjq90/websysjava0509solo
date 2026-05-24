package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑行记录实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ride_records")
public class RideRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String orderNo;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false)
    private Long bikeId;

    @Column(length = 30)
    private String bikeNo;

    @Column(nullable = false)
    private Long startAreaId;

    @Column(length = 50)
    private String startAreaName;

    private Long endAreaId;

    @Column(length = 50)
    private String endAreaName;

    @Column(length = 100)
    private String startLocation;

    @Column(length = 100)
    private String endLocation;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration;

    @Column(precision = 10, scale = 2)
    private BigDecimal distance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String status = "IN_PROGRESS";

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}
