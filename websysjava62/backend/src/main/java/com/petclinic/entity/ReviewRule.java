package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 审核规则实体类
 * 用于动态配置审核规则，如敏感词列表等
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "review_rule")
@Schema(description = "审核规则")
public class ReviewRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "规则ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "规则名称", required = true)
    private String ruleName;

    @Column(length = 50)
    @Schema(description = "规则类型：SENSITIVE_WORD-敏感词, CONTENT_AUDIT-内容审核, OTHER-其他")
    private String ruleType;

    @Column(length = 2000)
    @Schema(description = "规则内容（JSON格式，如敏感词列表）")
    private String ruleContent;

    @Column(length = 500)
    @Schema(description = "规则描述")
    private String description;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Column(length = 200)
    @Schema(description = "配置人")
    private String configuredBy;

    @Schema(description = "配置时间")
    private LocalDateTime configuredTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
