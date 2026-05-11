package com.medical.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Schema(description = "挂号记录VO")
public class RegistrationVO {
    
    @Schema(description = "挂号单ID")
    private Long id;
    
    @Schema(description = "挂号单号")
    private String registrationNo;
    
    @Schema(description = "科室编码")
    private String deptCode;
    
    @Schema(description = "科室名称")
    private String deptName;
    
    @Schema(description = "医生ID")
    private Long doctorId;
    
    @Schema(description = "医生姓名")
    private String doctorName;
    
    @Schema(description = "医生职称")
    private String doctorTitle;
    
    @Schema(description = "就诊日期")
    private LocalDate visitDate;
    
    @Schema(description = "就诊时间")
    private LocalTime visitTime;
    
    @Schema(description = "时间段")
    private String timeSlot;
    
    @Schema(description = "金额")
    private BigDecimal amount;
    
    @Schema(description = "支付方式")
    private String paymentMethod;
    
    @Schema(description = "支付状态")
    private String paymentStatus;
    
    @Schema(description = "状态")
    private String status;
    
    @Schema(description = "状态描述")
    private String statusDesc;
    
    @Schema(description = "状态标签：今日待就诊等")
    private String statusTag;
    
    @Schema(description = "二维码Base64")
    private String qrCode;
    
    @Schema(description = "症状")
    private String symptoms;
    
    @Schema(description = "是否可取消")
    private Boolean canCancel;
    
    @Schema(description = "取消截止时间描述")
    private String cancelDeadlineDesc;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
