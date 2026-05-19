package com.petclinic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理赔响应DTO
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Schema(description = "理赔响应")
public class ClaimResponse {

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "理赔ID")
    private Long claimId;

    @Schema(description = "理赔编号")
    private String claimNo;

    @Schema(description = "问诊编号")
    private String consultationNo;

    @Schema(description = "理赔状态")
    private String status;

    @Schema(description = "问诊总费用")
    private BigDecimal totalFee;

    @Schema(description = "免赔额")
    private BigDecimal deductible;

    @Schema(description = "理赔比例（%）")
    private BigDecimal claimRate;

    @Schema(description = "理赔金额")
    private BigDecimal claimAmount;

    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Schema(description = "保险公司返回的交易编号")
    private String transactionId;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "理赔申请时间")
    private LocalDateTime applyTime;

    @Schema(description = "预计到账时间（小时）")
    private Integer estimatedSettlementHours;

    public static ClaimResponse success(String claimNo, String consultationNo) {
        ClaimResponse response = new ClaimResponse();
        response.setSuccess(true);
        response.setClaimNo(claimNo);
        response.setConsultationNo(consultationNo);
        response.setStatus("PENDING");
        response.setMessage("理赔申请已提交，等待保险公司审核");
        response.setApplyTime(LocalDateTime.now());
        response.setEstimatedSettlementHours(24);
        return response;
    }

    public static ClaimResponse error(String message) {
        ClaimResponse response = new ClaimResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
