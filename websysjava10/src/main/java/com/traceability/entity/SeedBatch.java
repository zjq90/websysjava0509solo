package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 种子批次实体类
 * 管理种子的核心批次信息，是整个追溯链条的核心
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seed_batch")
public class SeedBatch extends BaseEntity {

    @Column(name = "batch_no", nullable = false, unique = true, length = 50)
    private String batchNo;

    @Column(name = "seed_name", nullable = false, length = 100)
    private String seedName;

    @Column(name = "seed_variety", length = 100)
    private String seedVariety;

    @Column(name = "seed_type", length = 50)
    private String seedType;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "producer", length = 100)
    private String producer;

    @Column(name = "production_location", length = 200)
    private String productionLocation;

    @Column(name = "production_date")
    private String productionDate;

    @Column(name = "shelf_life", length = 50)
    private String shelfLife;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;
}
