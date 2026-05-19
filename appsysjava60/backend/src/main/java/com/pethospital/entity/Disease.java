package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 疾病实体类
 * 存储疾病百科信息，包括症状和治疗方案
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_disease")
public class Disease {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 疾病名称
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * 宠物类型：猫、狗、其他
     */
    @Column(nullable = false, length = 20)
    private String petType;
    
    /**
     * 症状描述
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String symptoms;
    
    /**
     * 治疗方案
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String treatment;
    
    /**
     * 病因分析
     */
    @Column(columnDefinition = "TEXT")
    private String cause;
    
    /**
     * 预防措施
     */
    @Column(columnDefinition = "TEXT")
    private String prevention;
    
    /**
     * 严重程度：1-轻度，2-中度，3-重度
     */
    @Column(nullable = false)
    private Integer severity = 2;
    
    /**
     * 关键词（用于搜索）
     */
    @Column(length = 500)
    private String keywords;
    
    /**
     * 版本号（用于知识库更新）
     */
    @Column(nullable = false)
    private Integer version = 1;
    
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
