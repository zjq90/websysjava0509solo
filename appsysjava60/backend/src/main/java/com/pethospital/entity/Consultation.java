package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 接诊记录实体类
 * 存储医生的接诊记录，用于数据统计
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_consultation")
public class Consultation {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 医生ID
     */
    @Column(nullable = false)
    private Long doctorId;
    
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
     * 宠物名称
     */
    @Column(length = 50)
    private String petName;
    
    /**
     * 主人姓名
     */
    @Column(length = 50)
    private String ownerName;
    
    /**
     * 联系电话
     */
    @Column(length = 20)
    private String ownerPhone;
    
    /**
     * 主诉
     */
    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;
    
    /**
     * 诊断结果（关联疾病ID，多个用逗号分隔）
     */
    @Column(length = 500)
    private String diagnosisIds;
    
    /**
     * 诊断描述
     */
    @Column(columnDefinition = "TEXT")
    private String diagnosisDesc;
    
    /**
     * 处方药品（关联药品ID，多个用逗号分隔）
     */
    @Column(length = 1000)
    private String medicineIds;
    
    /**
     * 治疗费用
     */
    private Double cost;
    
    /**
     * 接诊日期
     */
    @Column(nullable = false)
    private LocalDateTime consultationDate;
    
    /**
     * 状态：0-进行中，1-已完成，2-已取消
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
        if (consultationDate == null) {
            consultationDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
