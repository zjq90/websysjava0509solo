package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 案例实体类
 * 存储匿名分享的诊疗案例
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_case")
public class Case {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 案例标题
     */
    @Column(nullable = false, length = 200)
    private String title;
    
    /**
     * 宠物类型
     */
    @Column(nullable = false, length = 20)
    private String petType;
    
    /**
     * 宠物品种
     */
    @Column(length = 50)
    private String breed;
    
    /**
     * 宠物年龄
     */
    @Column(length = 20)
    private String age;
    
    /**
     * 性别：公、母
     */
    @Column(length = 10)
    private String gender;
    
    /**
     * 主诉
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String chiefComplaint;
    
    /**
     * 临床检查
     */
    @Column(columnDefinition = "TEXT")
    private String clinicalExamination;
    
    /**
     * 诊断结果
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;
    
    /**
     * 治疗方案
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String treatment;
    
    /**
     * 治疗效果
     */
    @Column(columnDefinition = "TEXT")
    private String treatmentEffect;
    
    /**
     * 医生ID
     */
    @Column(nullable = false)
    private Long doctorId;
    
    /**
     * 医生姓名（匿名显示）
     */
    @Column(length = 50)
    private String doctorName;
    
    /**
     * 关键词（用于搜索）
     */
    @Column(length = 500)
    private String keywords;
    
    /**
     * 浏览次数
     */
    @Column(nullable = false)
    private Integer viewCount = 0;
    
    /**
     * 状态：0-待审核，1-已发布，2-已驳回
     */
    @Column(nullable = false)
    private Integer status = 0;
    
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
