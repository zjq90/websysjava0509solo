package com.petclinic.dto;

import lombok.Data;

/**
 * 首页统计数据DTO
 */
@Data
public class DashboardStatsDTO {

    /**
     * 今日接诊量
     */
    private Long todayConsultations;

    /**
     * 今日收入
     */
    private Double todayRevenue;

    /**
     * 今日急诊数
     */
    private Long todayEmergency;

    /**
     * 待审核医生数
     */
    private Long pendingDoctorAudit;

    /**
     * 待审核宠物主人数
     */
    private Long pendingOwnerAudit;

    /**
     * 库存预警药品数
     */
    private Long warningMedicineCount;
}
