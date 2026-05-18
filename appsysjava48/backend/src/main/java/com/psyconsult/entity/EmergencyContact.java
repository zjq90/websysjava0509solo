package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emergency_contact")
@ApiModel(description = "紧急联系人实体")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "联系人ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "关联用户ID")
    private Long userId;

    @Column(nullable = false, length = 50)
    @ApiModelProperty(value = "联系人姓名")
    private String name;

    @Column(length = 20)
    @ApiModelProperty(value = "与用户关系")
    private String relationship;

    @Column(nullable = false, length = 20)
    @ApiModelProperty(value = "联系电话")
    private String phone;

    @Column(length = 100)
    @ApiModelProperty(value = "备用电话")
    private String backupPhone;

    @Column(length = 200)
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "is_primary")
    @ApiModelProperty(value = "是否主要联系人")
    private Boolean isPrimary = false;

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
