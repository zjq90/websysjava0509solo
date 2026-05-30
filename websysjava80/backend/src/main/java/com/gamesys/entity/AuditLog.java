package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_log")
public class AuditLog extends BaseEntity {
    private Long gameId;
    private String gameName;
    private Long auditorId;
    private String auditorName;
    private LocalDateTime auditTime;
    private Integer auditResult;
    private String rejectReason;
    private String remark;
}
