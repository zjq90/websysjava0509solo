package com.seedinventory.entity;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 种子实体类
 * 存储种子的基础信息
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "seeds")
public class Seed {
    
    /**
     * 种子ID，主键，自动增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 种子编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 30)
    private String seedCode;
    
    /**
     * 种子名称
     */
    @Column(nullable = false, length = 100)
    private String seedName;
    
    /**
     * 种子类别：如蔬菜、粮食、油料等
     */
    @Column(length = 50)
    private String category;
    
    /**
     * 种子品种
     */
    @Column(length = 100)
    private String variety;
    
    /**
     * 规格
     */
    @Column(length = 50)
    private String specification;
    
    /**
     * 单位：如kg、袋、包等
     */
    @Column(length = 10)
    private String unit;
    
    /**
     * 参考单价
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal referencePrice;
    
    /**
     * 供应商
     */
    @Column(length = 100)
    private String supplier;
    
    /**
     * 状态：ACTIVE-在售，INACTIVE-下架
     */
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
    
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
    
    /**
     * 备注信息
     */
    @Column(length = 500)
    private String remark;
    
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
