package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 田间种植实体类
 * 记录田间种植环节信息，追溯链条的第二个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "field_planting")
public class FieldPlanting extends BaseEntity {

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Column(name = "field_location", length = 200)
    private String fieldLocation;

    @Column(name = "field_area", precision = 10, scale = 2)
    private BigDecimal fieldArea;

    @Column(name = "planting_date", length = 20)
    private String plantingDate;

    @Column(name = "planting_method", length = 50)
    private String plantingMethod;

    @Column(name = "planting_density", length = 50)
    private String plantingDensity;

    @Column(name = "soil_type", length = 50)
    private String soilType;

    @Column(name = "fertilizer_info", length = 500)
    private String fertilizerInfo;

    @Column(name = "pesticide_info", length = 500)
    private String pesticideInfo;

    @Column(name = "irrigation_info", length = 500)
    private String irrigationInfo;

    @Column(name = "grower", length = 50)
    private String grower;

    @Column(name = "growth_stage", length = 50)
    private String growthStage;

    @Column(name = "remark", length = 500)
    private String remark;
}
