package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 药品实体类
 */
@Data
@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 药品名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 药品编码
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 药品分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 规格
     */
    @Column(length = 100)
    private String specification;

    /**
     * 生产厂家
     */
    @Column(length = 200)
    private String manufacturer;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    private Integer stockQuantity = 0;

    /**
     * 预警数量
     */
    @Column(nullable = false)
    private Integer warningQuantity = 10;

    /**
     * 单价
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 单位
     */
    @Column(length = 20)
    private String unit;

    /**
     * 描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 状态：0-下架，1-上架
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(nullable = false)
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
