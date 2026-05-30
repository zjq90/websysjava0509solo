package com.gameplatform.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private String reportId;
    private String reportType;
    private String targetId;
    private String targetName;
    private String reasonType;
    private String reasonDetail;
    private String reporterId;
    private String reporterName;
    private Integer status;
    private LocalDateTime createTime;
}
