package com.gameplatform.dto;

import lombok.Data;

@Data
public class CommentAuditDTO {
    private String commentId;
    private Integer auditStatus;
    private String auditorId;
}
