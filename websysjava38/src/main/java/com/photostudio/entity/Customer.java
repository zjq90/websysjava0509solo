package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "customer")
@Schema(description = "客户信息")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "客户ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @Schema(description = "客户姓名", example = "张三")
    private String name;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    @Column(name = "email", length = 100)
    @Schema(description = "邮箱地址", example = "zhangsan@example.com")
    private String email;

    @Column(name = "wechat", length = 50)
    @Schema(description = "微信号", example = "zhangsan_wx")
    private String wechat;

    @Column(name = "gender", length = 10)
    @Schema(description = "性别", example = "男")
    private String gender;

    @Column(name = "age")
    @Schema(description = "年龄", example = "28")
    private Integer age;

    @Column(name = "address", length = 255)
    @Schema(description = "地址", example = "北京市朝阳区xxx街道xxx号")
    private String address;

    @Column(name = "source", length = 50)
    @Schema(description = "客户来源", example = "美团/抖音/小程序/门店")
    private String source;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注", example = "客户对照片质量要求较高")
    private String remark;

    @Column(name = "vip_level")
    @Schema(description = "VIP等级", example = "1")
    private Integer vipLevel;

    @Column(name = "total_amount")
    @Schema(description = "累计消费金额", example = "15800.00")
    private java.math.BigDecimal totalAmount;

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
        if (totalAmount == null) {
            totalAmount = java.math.BigDecimal.ZERO;
        }
        if (vipLevel == null) {
            vipLevel = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
