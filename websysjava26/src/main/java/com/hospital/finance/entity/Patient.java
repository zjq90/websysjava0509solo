package com.hospital.finance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者信息实体类
 * 存储患者的基本信息，包括姓名、性别、年龄、联系方式、医保信息等
 */
@Data
@Entity
@Table(name = "patient")
@Schema(description = "患者信息")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "患者ID")
    private Long id;

    @Column(name = "patient_no", unique = true, nullable = false, length = 50)
    @Schema(description = "患者编号")
    private String patientNo;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "患者姓名")
    private String name;

    @Column(name = "gender", length = 10)
    @Schema(description = "性别")
    private String gender;

    @Column(name = "age")
    @Schema(description = "年龄")
    private Integer age;

    @Column(name = "id_card", unique = true, length = 18)
    @Schema(description = "身份证号")
    private String idCard;

    @Column(name = "phone", length = 20)
    @Schema(description = "联系电话")
    private String phone;

    @Column(name = "address", length = 500)
    @Schema(description = "家庭住址")
    private String address;

    @Column(name = "has_insurance")
    @Schema(description = "是否有医保")
    private Boolean hasInsurance;

    @Column(name = "insurance_no", length = 50)
    @Schema(description = "医保编号")
    private String insuranceNo;

    @Column(name = "insurance_type", length = 50)
    @Schema(description = "医保类型(城镇职工、城乡居民、新农合等)")
    private String insuranceType;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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