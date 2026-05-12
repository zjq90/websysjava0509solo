package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 药品实体类
 * 存储药品信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "medicine")
@Schema(description = "药品")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "药品ID")
    private Long id;

    @Column(name = "medicine_code", unique = true, nullable = false, length = 32)
    @Schema(description = "药品编码")
    private String medicineCode;

    @Column(name = "generic_name", length = 256)
    @Schema(description = "通用名")
    private String genericName;

    @Column(name = "trade_name", length = 256)
    @Schema(description = "商品名")
    private String tradeName;

    @Column(name = "specification", length = 256)
    @Schema(description = "规格")
    private String specification;

    @Column(name = "dosage_form", length = 64)
    @Schema(description = "剂型：片剂/胶囊/注射剂/颗粒/口服液")
    private String dosageForm;

    @Column(name = "manufacturer", length = 256)
    @Schema(description = "生产厂家")
    private String manufacturer;

    @Column(name = "unit", length = 32)
    @Schema(description = "单位：盒/瓶/支/片/粒")
    private String unit;

    @Column(name = "price", precision = 10, scale = 2)
    @Schema(description = "单价")
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "stock_quantity")
    @Schema(description = "库存数量")
    private Integer stockQuantity = 0;

    @Column(name = "category", length = 64)
    @Schema(description = "药品分类：西药/中成药/中草药/特殊药品")
    private String category;

    @Column(name = "usage", length = 512)
    @Schema(description = "用法用量")
    private String usage;

    @Column(name = "indication", length = 1024)
    @Schema(description = "适应症")
    private String indication;

    @Column(name = "contraindication", length = 1024)
    @Schema(description = "禁忌症")
    private String contraindication;

    @Column(name = "side_effect", length = 1024)
    @Schema(description = "不良反应")
    private String sideEffect;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "有效期")
    private Date expiryDate;

    @Column(name = "batch_no", length = 64)
    @Schema(description = "批号")
    private String batchNo;

    @Column(name = "storage_condition", length = 256)
    @Schema(description = "储存条件")
    private String storageCondition;

    @Column(name = "status", length = 32)
    @Schema(description = "状态：正常/缺货/下架/过期")
    private String status = "正常";

    @Column(name = "remark", length = 512)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
