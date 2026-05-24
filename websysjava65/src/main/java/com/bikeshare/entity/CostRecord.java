package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成本记录实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "cost_records")
public class CostRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String recordNo;

    @Column(nullable = false, length = 20)
    private String costType;

    @Column(nullable = false, length = 50)
    private String costCategory;

    private Long bikeId;

    @Column(length = 30)
    private String bikeNo;

    private Long areaId;

    @Column(length = 50)
    private String areaName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private LocalDate costDate;

    @Column(length = 50)
    private String operator;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
