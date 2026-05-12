package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 医护人员实体类
 * 存储医生、护士等工作人员信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "staff")
@Schema(description = "医护人员")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "人员ID")
    private Long id;

    @Column(name = "staff_no", unique = true, nullable = false, length = 32)
    @Schema(description = "工号")
    private String staffNo;

    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "姓名")
    private String name;

    @Column(name = "gender", length = 8)
    @Schema(description = "性别")
    private String gender;

    @Column(name = "age")
    @Schema(description = "年龄")
    private Integer age;

    @Column(name = "phone", length = 32)
    @Schema(description = "联系电话")
    private String phone;

    @Column(name = "id_card", length = 32)
    @Schema(description = "身份证号")
    private String idCard;

    @Column(name = "position", length = 64)
    @Schema(description = "职位：主任医师/副主任医师/主治医师/住院医师/护士长/护士")
    private String position;

    @Column(name = "title", length = 64)
    @Schema(description = "职称")
    private String title;

    @Column(name = "department", length = 64)
    @Schema(description = "所属科室")
    private String department;

    @Column(name = "specialty", length = 256)
    @Schema(description = "专长")
    private String specialty;

    @Column(name = "role", length = 32)
    @Schema(description = "角色：医生/护士/管理员")
    private String role = "医生";

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "出生日期")
    private Date birthday;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "入职日期")
    private Date hireDate;

    @Column(name = "status", length = 32)
    @Schema(description = "状态：在职/休假/离职")
    private String status = "在职";

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
