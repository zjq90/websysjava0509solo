package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("auto_audit_rule")
public class AutoAuditRule extends BaseEntity {
    private String ruleName;
    private String ruleType;
    private String ruleValue;
    private Integer action;
    private Integer status;
}
