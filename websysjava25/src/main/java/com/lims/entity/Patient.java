package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者实体类
 * 存储患者基本信息
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
@Schema(description = "患者信息")
public class Patient {

    /**
     * 患者ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "患者ID", example = "1")
    private Long id;

    /**
     * 患者编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "患者编号", example = "PAT001")
    private String patientNo;

    /**
     * 患者姓名
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "患者姓名", example = "李四")
    private String patientName;

    /**
     * 性别：MALE-男，FEMALE-女，UNKNOWN-未知
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "性别", example = "MALE")
    private String gender;

    /**
     * 出生日期
     */
    @Column
    @Schema(description = "出生日期", example = "1990-01-01")
    private LocalDate birthDate;

    /**
     * 年龄
     */
    @Column
    @Schema(description = "年龄", example = "30")
    private Integer age;

    /**
     * 身份证号
     */
    @Column(length = 20)
    @Schema(description = "身份证号", example = "110101199001011234")
    private String idCard;

    /**
     * 联系电话
     */
    @Column(length = 20)
    @Schema(description = "联系电话", example = "13900139000")
    private String phone;

    /**
     * 地址
     */
    @Column(length = 200)
    @Schema(description = "地址", example = "北京市朝阳区")
    private String address;

    /**
     * 过敏史
     */
    @Column(length = 500)
    @Schema(description = "过敏史", example = "青霉素过敏")
    private String allergyHistory;

    /**
     * 状态：NORMAL-正常，DELETED-已删除
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "状态", example = "NORMAL")
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "NORMAL";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
