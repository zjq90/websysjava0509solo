package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 药品实体类
 * 存储药品说明书信息，包括用法用量和禁忌
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_medicine")
public class Medicine {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 药品名称
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * 通用名
     */
    @Column(length = 100)
    private String genericName;
    
    /**
     * 药品分类
     */
    @Column(length = 50)
    private String category;
    
    /**
     * 适应症
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String indication;
    
    /**
     * 用法用量
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String dosage;
    
    /**
     * 不良反应
     */
    @Column(columnDefinition = "TEXT")
    private String adverseReaction;
    
    /**
     * 禁忌
     */
    @Column(columnDefinition = "TEXT")
    private String contraindication;
    
    /**
     * 注意事项
     */
    @Column(columnDefinition = "TEXT")
    private String attention;
    
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
     * 关键词（用于搜索）
     */
    @Column(length = 500)
    private String keywords;
    
    /**
     * 状态：0-禁用，1-启用
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
