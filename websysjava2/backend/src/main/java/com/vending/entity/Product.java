package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品实体类
 * 包含商品名称、图片、规格、条形码、分类、成本价、零售价等信息
 */
@Data
@Entity
@Table(name = "product")
@Schema(description = "商品信息")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "商品ID")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "商品名称", required = true)
    private String name;
    
    @Schema(description = "商品图片URL")
    private String imageUrl;
    
    @Schema(description = "商品规格")
    private String specification;
    
    @Column(unique = true)
    @Schema(description = "条形码")
    private String barcode;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    @Schema(description = "所属分类")
    private Category category;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "成本价", required = true)
    private BigDecimal costPrice;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "零售价", required = true)
    private BigDecimal retailPrice;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "库存预警阈值")
    private Integer stockThreshold = 5;
    
    @Schema(description = "商品描述")
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Schema(description = "是否上架")
    private Boolean active = true;
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Slot> slots = new ArrayList<>();
    
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
