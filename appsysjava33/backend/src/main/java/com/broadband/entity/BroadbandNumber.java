package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宽带号码实体类
 * 管理宽带号码资源
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_broadband_number")
@Schema(description = "宽带号码")
public class BroadbandNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "宽带号码")
    @Column(length = 20, nullable = false, unique = true)
    private String number;

    @Schema(description = "号码类型: 1-普通号 2-靓号")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer type = 1;

    @Schema(description = "靓号等级: A/B/C级")
    @Column(length = 10)
    private String level;

    @Schema(description = "靓号附加费")
    @Column(precision = 10, scale = 2)
    private BigDecimal extraFee;

    @Schema(description = "号码特点标签,逗号分隔")
    @Column(length = 200)
    private String tags;

    @Schema(description = "归属地区")
    @Column(length = 100)
    private String region;

    @Schema(description = "状态: 0-已占用 1-可选用 2-已锁定 3-已停用")
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer status = 1;

    @Schema(description = "锁定用户ID")
    private Long lockUserId;

    @Schema(description = "锁定时间")
    private LocalDateTime lockTime;

    @Schema(description = "使用用户ID")
    private Long useUserId;

    @Schema(description = "启用时间")
    private LocalDateTime useTime;

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
