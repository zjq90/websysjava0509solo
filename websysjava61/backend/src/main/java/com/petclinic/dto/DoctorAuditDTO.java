package com.petclinic.dto;

import lombok.Data;

/**
 * 医生审核DTO
 */
@Data
public class DoctorAuditDTO {
    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 审核状态：1-通过，2-驳回
     */
    private Integer auditStatus;

    /**
     * 审核意见
     */
    private String auditRemark;
}
