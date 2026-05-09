package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 包装实体类
 * 记录包装环节信息，实现"一袋一码"溯源
 * 追溯链条的第五个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "packaging")
public class Packaging extends BaseEntity {

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "package_no", nullable = false, unique = true, length = 50)
    private String packageNo;

    @Column(name = "packaging_date", length = 20)
    private String packagingDate;

    @Column(name = "packaging_location", length = 200)
    private String packagingLocation;

    @Column(name = "package_type", length = 50)
    private String packageType;

    @Column(name = "package_spec", length = 100)
    private String packageSpec;

    @Column(name = "net_weight", precision = 10, scale = 2)
    private BigDecimal netWeight;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "packaging_person", length = 50)
    private String packagingPerson;

    @Column(name = "qr_code", nullable = false, unique = true, length = 100)
    private String qrCode;

    @Column(name = "barcode", unique = true, length = 100)
    private String barcode;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;
}
