package com.gameplatform.dto;

import lombok.Data;

@Data
public class ReportHandleDTO {
    private String reportId;
    private Integer status;
    private String handlerId;
    private String handlerName;
}
