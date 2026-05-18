package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "counselor")
@ApiModel(description = "咨询师实体")
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "咨询师ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "关联用户ID")
    private Long userId;

    @Column(nullable = false, length = 50)
    @ApiModelProperty(value = "真实姓名")
    private String name;

    @Column(length = 20)
    @ApiModelProperty(value = "职称")
    private String title;

    @Column(length = 500)
    @ApiModelProperty(value = "专业资质")
    private String qualification;

    @Column(length = 2000)
    @ApiModelProperty(value = "个人简介")
    private String bio;

    @Column(length = 500)
    @ApiModelProperty(value = "擅长领域")
    private String expertise;

    @Column(precision = 10, scale = 2)
    @ApiModelProperty(value = "咨询费用")
    private BigDecimal consultationFee;

    @Column(name = "experience_years")
    @ApiModelProperty(value = "从业年限")
    private Integer experienceYears;

    @Column(length = 200)
    @ApiModelProperty(value = "照片URL")
    private String photo;

    @Column(name = "is_senior")
    @ApiModelProperty(value = "是否资深咨询师（可担任督导）")
    private Boolean isSenior = false;

    @Column(name = "accepting")
    @ApiModelProperty(value = "是否接单")
    private Boolean accepting = true;

    @Column(nullable = false)
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status = 1;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
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
