package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 收获实体类
 * 记录收获环节信息，追溯链条的第三个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "harvest")
public class Harvest extends BaseEntity {

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "harvest_date", length = 20)
    private String harvestDate;

    @Column(name = "harvest_location", length = 200)
    private String harvestLocation;

    @Column(name = "harvest_quantity", precision = 10, scale = 2)
    private BigDecimal harvestQuantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "harvest_method", length = 50)
    private String harvestMethod;

    @Column(name = "harvester", length = 50)
    private String harvester;

    @Column(name = "maturity_level", length = 50)
    private String maturityLevel;

    @Column(name = "moisture_content", length = 20)
    private String moistureContent;

    @Column(name = "quality_grade", length = 20)
    private String qualityGrade;

    @Column(name = "storage_location", length = 100)
    private String storageLocation;

    @Column(name = "remark", length = 500)
    private String remark;
}
