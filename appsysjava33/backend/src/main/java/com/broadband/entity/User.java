package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储用户基本信息和认证信息
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "手机号(加密存储)")
    @Column(length = 512)
    private String phone;

    @Schema(description = "用户姓名")
    @Column(length = 100)
    private String name;

    @Schema(description = "身份证号(加密存储)")
    @Column(length = 512)
    private String idCard;

    @Schema(description = "身份证正面照片路径")
    @Column(length = 512)
    private String idCardFront;

    @Schema(description = "身份证反面照片路径")
    @Column(length = 512)
    private String idCardBack;

    @Schema(description = "实名认证状态: 0-未认证 1-认证中 2-已认证 3-认证失败")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer realNameStatus = 0;

    @Schema(description = "活体检测状态: 0-未检测 1-已通过")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer livenessStatus = 0;

    @Schema(description = "微信OpenID")
    @Column(length = 100)
    private String wxOpenId;

    @Schema(description = "支付宝UserID")
    @Column(length = 100)
    private String alipayUserId;

    @Schema(description = "头像URL")
    @Column(length = 512)
    private String avatar;

    @Schema(description = "邮箱")
    @Column(length = 100)
    private String email;

    @Schema(description = "安装地址")
    @Column(length = 512)
    private String installAddress;

    @Schema(description = "详细地址")
    @Column(length = 512)
    private String detailAddress;

    @Schema(description = "经度")
    @Column(precision = 10, scale = 6)
    private Double longitude;

    @Schema(description = "纬度")
    @Column(precision = 10, scale = 6)
    private Double latitude;

    @Schema(description = "用户类型: 1-个人用户 2-企业用户")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer userType = 1;

    @Schema(description = "企业名称")
    @Column(length = 200)
    private String companyName;

    @Schema(description = "企业营业执照号")
    @Column(length = 100)
    private String businessLicense;

    @Schema(description = "长辈模式: 0-关闭 1-开启")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer elderMode = 0;

    @Schema(description = "自动续费: 0-关闭 1-开启")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer autoRenewal = 0;

    @Schema(description = "账号状态: 0-禁用 1-正常")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer status = 1;

    @Schema(description = "注册时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

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
