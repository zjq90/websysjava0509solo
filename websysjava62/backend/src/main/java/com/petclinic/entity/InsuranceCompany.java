package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保险公司配置实体类
 * 用于管理对接的保险公司信息，包括API配置、理赔比例等
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "insurance_company")
@Schema(description = "保险公司配置")
public class InsuranceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "保险公司ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "保险公司名称", required = true)
    private String companyName;

    @Column(length = 100)
    @Schema(description = "公司编码")
    private String companyCode;

    @Column(length = 200)
    @Schema(description = "API接口地址")
    private String apiUrl;

    @Column(length = 100)
    @Schema(description = "API密钥")
    private String apiKey;

    @Column(length = 100)
    @Schema(description = "API密钥")
    private String apiSecret;

    @Column(precision = 5, scale = 2)
    @Schema(description = "默认理赔比例（百分比）")
    private BigDecimal defaultClaimRate;

    @Column(precision = 10, scale = 2)
    @Schema(description = "单次理赔最高限额")
    private BigDecimal maxClaimAmount;

    @Column(precision = 10, scale = 2)
    @Schema(description = "免赔额")
    private BigDecimal deductible;

    @Column(length = 2000)
    @Schema(description = "保障范围说明")
    private String coverageDescription;

    @Column(length = 200)
    @Schema(description = "联系人")
    private String contactPerson;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String contactPhone;

    @Column(length = 200)
    @Schema(description = "公司地址")
    private String address;

    @Column(length = 50)
    @Schema(description = "状态：ACTIVE-启用, INACTIVE-禁用")
    private String status;

    @Schema(description = "是否支持自动理赔")
    private Boolean autoClaimEnabled;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (autoClaimEnabled == null) {
            autoClaimEnabled = true;
        }
        if (defaultClaimRate == null) {
            defaultClaimRate = new BigDecimal("70.00");
        }
        if (maxClaimAmount == null) {
            maxClaimAmount = new BigDecimal("5000.00");
        }
        if (deductible == null) {
            deductible = new BigDecimal("100.00");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
