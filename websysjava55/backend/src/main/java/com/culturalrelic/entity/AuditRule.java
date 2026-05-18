package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 审核规则实体类
 * 维护系统的审核规则库
 */
@Data
@Entity
@Table(name = "audit_rule")
@EqualsAndHashCode(callSuper = true)
public class AuditRule extends BaseEntity {

    /**
     * 规则编号
     */
    @Column(name = "rule_no", nullable = false, unique = true, length = 50)
    private String ruleNo;

    /**
     * 规则名称
     */
    @Column(name = "rule_name", nullable = false, length = 200)
    private String ruleName;

    /**
     * 规则类型：1-文物入库审核，2-文物出库审核，3-修复审核，4-展览审核，5-其他
     */
    @Column(name = "rule_type", nullable = false)
    private Integer ruleType;

    /**
     * 规则优先级：1-高，2-中，3-低
     */
    @Column(name = "priority", nullable = false)
    private Integer priority = 2;

    /**
     * 规则条件（JSON格式存储）
     */
    @Column(name = "rule_condition", columnDefinition = "TEXT")
    private String ruleCondition;

    /**
     * 规则动作（JSON格式存储）
     */
    @Column(name = "rule_action", columnDefinition = "TEXT")
    private String ruleAction;

    /**
     * 审核级别：1-一级审核，2-二级审核，3-三级审核
     */
    @Column(name = "audit_level", nullable = false)
    private Integer auditLevel = 1;

    /**
     * 审核人角色
     */
    @Column(name = "auditor_role", length = 100)
    private String auditorRole;

    /**
     * 超时时间（小时）
     */
    @Column(name = "timeout_hours")
    private Integer timeoutHours = 24;

    /**
     * 规则状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 规则描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 版本号
     */
    @Column(name = "version", length = 50)
    private String version = "1.0";
}
