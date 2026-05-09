package com.appsys.production.dto;

import lombok.Data;

@Data
public class StageOperationDTO {
    private Long batchId;
    private String stageCode;
    private Long operatorId;
    private String operatorName;
    private String processParams;
    private String remark;
}
