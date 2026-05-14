package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户套餐实体类
 * 记录用户购买的套餐信息
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_user_package")
@Schema(description = "用户套餐信息")
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "套餐ID")
    @Column(nullable = false)
    private Long packageId;

    @Schema(description = "套餐名称")
    @Column(length = 100)
    private String packageName;

    @Schema(description = "宽带号码")
    @Column(length = 20)
    private String broadbandNumber;

    @Schema(description = "套餐类型: 1-基础套餐 2-提速包 3-安全防护包")
    private Integer packageType;

    @Schema(description = "生效时间")
    @Column(nullable = false)
    private LocalDate effectiveDate;

    @Schema(description = "到期时间")
    @Column(nullable = false)
    private LocalDate expireDate;

    @Schema(description = "订购周期: 1-月付 2-季付 3-年付")
    @Column(nullable = false)
    private Integer period;

    @Schema(description = "订单金额")
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Schema(description = "状态: 0-待生效 1-生效中 2-已过期 3-已取消")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer status = 0;

    @Schema(description = "安装地址")
    @Column(length = 512)
    private String installAddress;

    @Schema(description = "安装时间")
    private LocalDateTime installTime;

    @Schema(description = "安装人员")
    @Column(length = 50)
    private String installer;

    @Schema(description = "安装人员电话")
    @Column(length = 20)
    private String installerPhone;

    @Schema(description = "备注")
    @Column(length = 500)
    private String remark;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

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
