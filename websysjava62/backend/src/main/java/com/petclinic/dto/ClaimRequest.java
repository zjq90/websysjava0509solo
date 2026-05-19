package com.petclinic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 理赔申请请求DTO
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Schema(description = "理赔申请请求")
public class ClaimRequest {

    @Schema(description = "问诊记录ID", required = true)
    private Long consultationId;

    @Schema(description = "保险公司ID")
    private Long insuranceCompanyId;

    @Schema(description = "保险公司ID（别名，兼容前端）")
    private Long companyId;

    public Long getInsuranceCompanyId() {
        return insuranceCompanyId != null ? insuranceCompanyId : companyId;
    }

    @Schema(description = "保单号", required = true)
    private String policyNo;

    @Schema(description = "宠主姓名")
    private String ownerName;

    @Schema(description = "宠主电话")
    private String ownerPhone;

    @Schema(description = "宠主身份证号")
    private String ownerIdCard;

    @Schema(description = "宠物名称")
    private String petName;

    @Schema(description = "问诊总费用")
    private BigDecimal totalFee;

    @Schema(description = "理赔说明")
    private String claimDescription;

    @Schema(description = "诊断证明文件URL（多个用逗号分隔）")
    private String diagnosisFiles;

    @Schema(description = "发票文件URL（多个用逗号分隔）")
    private String invoiceFiles;

    @Schema(description = "是否自动提交到保险公司")
    private Boolean autoSubmit = true;
}
