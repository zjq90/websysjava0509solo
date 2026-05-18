package com.psyconsult.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_user")
@ApiModel(description = "用户实体")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(nullable = false, length = 100)
    @ApiModelProperty(value = "密码")
    private String password;

    @Column(nullable = false, length = 50)
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @Column(length = 20)
    @ApiModelProperty(value = "手机号")
    private String phone;

    @Column(length = 100)
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Column(length = 10)
    @ApiModelProperty(value = "性别")
    private String gender;

    @Column(length = 200)
    @ApiModelProperty(value = "头像URL")
    private String avatar;

    @Column(length = 500)
    @ApiModelProperty(value = "备注")
    private String remark;

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
