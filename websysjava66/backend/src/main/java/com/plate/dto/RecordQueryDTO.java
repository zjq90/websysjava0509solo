package com.plate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecordQueryDTO {
    private String plateNumber;
    private String cameraId;
    private String anomalyType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer page = 0;
    private Integer size = 20;
}
