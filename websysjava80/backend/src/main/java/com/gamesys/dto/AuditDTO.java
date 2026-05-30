package com.gamesys.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AuditDTO {
    @NotEmpty(message = "游戏ID不能为空")
    private List<Long> gameIds;

    @NotNull(message = "审核结果不能为空")
    private Integer auditResult;

    private String rejectReason;
    private String remark;

    private Long auditorId;
    private String auditorName;
}
