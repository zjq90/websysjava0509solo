package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门店实体类
 * 存储影楼门店的基本信息
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_store")
@Schema(description = "门店信息")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "门店ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "门店名称")
    private String name;

    @Column(length = 200)
    @Schema(description = "门店地址")
    private String address;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

    @Column(length = 50)
    @Schema(description = "店长姓名")
    private String manager;

    @Column(precision = 10, scale = 2)
    @Schema(description = "月度目标业绩")
    private BigDecimal monthlyTarget;

    @Column(name = "is_active")
    @Schema(description = "是否启用")
    private Boolean active = true;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
