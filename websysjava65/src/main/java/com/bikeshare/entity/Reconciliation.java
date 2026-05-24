package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账记录实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "reconciliations")
public class Reconciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String reconNo;

    @Column(nullable = false)
    private LocalDate reconDate;

    @Column(nullable = false, length = 20)
    private String reconType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal systemAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal actualAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal diffAmount;

    @Column(nullable = false, length = 20)
    private String status = "MATCHED";

    @Column(length = 200)
    private String remark;

    @Column(length = 50)
    private String operator;

    private LocalDateTime handleTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
