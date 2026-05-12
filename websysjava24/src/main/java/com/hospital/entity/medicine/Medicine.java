package com.hospital.entity.medicine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine")
@Schema(description = "药品字典实体")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "药品ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "药品编码")
    private String medicineCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "药品名称")
    private String medicineName;

    @Column(length = 200)
    @Schema(description = "药品通用名")
    private String genericName;

    @Column(length = 50)
    @Schema(description = "药品规格")
    private String specification;

    @Column(length = 50)
    @Schema(description = "剂型")
    private String dosageForm;

    @Column(length = 100)
    @Schema(description = "生产厂家")
    private String manufacturer;

    @Column(length = 50)
    @Schema(description = "批准文号")
    private String approvalNumber;

    @Column(precision = 10, scale = 2)
    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;

    @Column(precision = 10, scale = 2)
    @Schema(description = "零售价格")
    private BigDecimal retailPrice;

    @Column(length = 50)
    @Schema(description = "药品分类")
    private String category;

    @Column(length = 20)
    @Schema(description = "存储条件")
    private String storageCondition;

    @Column(length = 500)
    @Schema(description = "用法用量")
    private String usageDosage;

    @Column(length = 1000)
    @Schema(description = "禁忌症")
    private String contraindication;

    @Column(length = 1000)
    @Schema(description = "药物相互作用")
    private String drugInteraction;

    @Column
    @Schema(description = "是否处方药")
    private Boolean isPrescription;

    @Column
    @Schema(description = "状态(0-停用,1-启用)")
    private Integer status;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
        if (isPrescription == null) {
            isPrescription = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
