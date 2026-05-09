package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 加工实体类
 * 记录加工环节信息，追溯链条的第四个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "processing")
public class Processing extends BaseEntity {

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "processing_date", length = 20)
    private String processingDate;

    @Column(name = "processing_location", length = 200)
    private String processingLocation;

    @Column(name = "processing_type", length = 50)
    private String processingType;

    @Column(name = "processing_equipment", length = 100)
    private String processingEquipment;

    @Column(name = "input_quantity", precision = 10, scale = 2)
    private BigDecimal inputQuantity;

    @Column(name = "output_quantity", precision = 10, scale = 2)
    private BigDecimal outputQuantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "processing_person", length = 50)
    private String processingPerson;

    @Column(name = "processing_steps", length = 1000)
    private String processingSteps;

    @Column(name = "quality_check_result", length = 200)
    private String qualityCheckResult;

    @Column(name = "remark", length = 500)
    private String remark;
}
