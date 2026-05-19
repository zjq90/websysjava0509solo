package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 知识库更新记录实体类
 * 记录知识库内容的更新历史，如新版《兽医临床指南》等
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_knowledge_update")
public class KnowledgeUpdate {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 更新标题
     */
    @Column(nullable = false, length = 200)
    private String title;
    
    /**
     * 更新类型：disease-疾病，medicine-药品，case-案例，other-其他
     */
    @Column(nullable = false, length = 20)
    private String type;
    
    /**
     * 更新内容描述
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    /**
     * 版本号
     */
    @Column(nullable = false, length = 50)
    private String version;
    
    /**
     * 更新来源（如《兽医临床指南》第5版）
     */
    @Column(length = 200)
    private String source;
    
    /**
     * 关联数据ID（多个用逗号分隔）
     */
    @Column(length = 1000)
    private String relatedIds;
    
    /**
     * 更新人ID
     */
    @Column(nullable = false)
    private Long operatorId;
    
    /**
     * 更新人姓名
     */
    @Column(length = 50)
    private String operatorName;
    
    /**
     * 状态：0-草稿，1-已发布
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
