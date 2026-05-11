package com.medical.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "号源VO")
public class ScheduleVO {
    
    @Schema(description = "号源ID")
    private Long id;
    
    @Schema(description = "医生ID")
    private Long doctorId;
    
    @Schema(description = "医生姓名")
    private String doctorName;
    
    @Schema(description = "医生职称")
    private String doctorTitle;
    
    @Schema(description = "科室编码")
    private String deptCode;
    
    @Schema(description = "科室名称")
    private String deptName;
    
    @Schema(description = "号源日期")
    private LocalDate scheduleDate;
    
    @Schema(description = "开始时间")
    private LocalTime startTime;
    
    @Schema(description = "结束时间")
    private LocalTime endTime;
    
    @Schema(description = "时间段")
    private String timeSlot;
    
    @Schema(description = "总号数")
    private Integer totalCount;
    
    @Schema(description = "剩余号数")
    private Integer availableCount;
    
    @Schema(description = "挂号费")
    private java.math.BigDecimal registrationFee;
}
