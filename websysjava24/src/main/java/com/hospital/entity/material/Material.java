package com.hospital.entity.material;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "material")
@Schema(description = "物资耗材实体")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "物资ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "物资编码")
    private String materialCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "物资名称")
    private String materialName;

    @Column(length = 50)
    @Schema(description = "规格型号")
    private String specification;

    @Column(length = 50)
    @Schema(description = "型号")
    private String model;

    @Column(length = 50)
    @Schema(description = "单位")
    private String unit;

    @Column(length = 100)
    @Schema(description = "生产厂家")
    private String manufacturer;

    @Column(length = 50)
    @Schema(description = "注册证号")
    private String registrationNo;

    @Column(precision = 10, scale = 2)
    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;

    @Column(precision = 10, scale = 2)
    @Schema(description = "领用价格")
    private BigDecimal usePrice;

    @Column(length = 50)
    @Schema(description = "物资分类")
    private String category;

    @Column(length = 50)
    @Schema(description = "物资类型(0-普通耗材,1-高值耗材,2-办公用品)")
    private String materialType;

    @Column
    @Schema(description = "是否高值耗材")
    private Boolean isHighValue;

    @Column(length = 100)
    @Schema(description = "唯一标识规则(高值耗材)")
    private String uniqueIdRule;

    @Column(length = 20)
    @Schema(description = "存储条件")
    private String storageCondition;

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
        if (isHighValue == null) {
            isHighValue = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
