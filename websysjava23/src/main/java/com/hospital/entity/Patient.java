package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 患者实体类
 * 存储患者基本信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
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

    @Column(name = "patient_no", unique = true, nullable = false, length = 32)
    @Schema(description = "患者编号")
    private String patientNo;

    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "患者姓名")
    private String name;

    @Column(name = "gender", length = 8)
    @Schema(description = "性别")
    private String gender;

    @Column(name = "age")
    @Schema(description = "年龄")
    private Integer age;

    @Column(name = "id_card", length = 32)
    @Schema(description = "身份证号")
    private String idCard;

    @Column(name = "phone", length = 32)
    @Schema(description = "联系电话")
    private String phone;

    @Column(name = "address", length = 256)
    @Schema(description = "家庭住址")
    private String address;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "出生日期")
    private Date birthday;

    @Column(name = "marital_status", length = 32)
    @Schema(description = "婚姻状况")
    private String maritalStatus;

    @Column(name = "occupation", length = 64)
    @Schema(description = "职业")
    private String occupation;

    @Column(name = "emergency_contact", length = 64)
    @Schema(description = "紧急联系人")
    private String emergencyContact;

    @Column(name = "emergency_phone", length = 32)
    @Schema(description = "紧急联系电话")
    private String emergencyPhone;

    @Column(name = "medical_history", length = 1024)
    @Schema(description = "既往病史")
    private String medicalHistory;

    @Column(name = "allergy_history", length = 512)
    @Schema(description = "过敏史")
    private String allergyHistory;

    @Column(name = "status", length = 32)
    @Schema(description = "患者状态：正常/住院中/已出院")
    private String status = "正常";

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
