package com.pethospital.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 统计数据DTO
 * 封装数据统计结果
 * 
 * @author Pet Hospital Team
 */
@Data
public class StatisticsDTO {
    
    /**
     * 接诊量统计
     */
    private ConsultationStats consultationStats;
    
    /**
     * 常见疾病排行
     */
    private List<DiseaseRank> diseaseRanks;
    
    /**
     * 药品使用分析
     */
    private List<MedicineUsage> medicineUsages;
    
    /**
     * 接诊量统计内部类
     */
    @Data
    public static class ConsultationStats {
        private Long totalCount;
        private Long todayCount;
        private Long weekCount;
        private Long monthCount;
        private List<Map<String, Object>> dailyData;
        private List<Map<String, Object>> weeklyData;
        private List<Map<String, Object>> monthlyData;
    }
    
    /**
     * 疾病排行内部类
     */
    @Data
    public static class DiseaseRank {
        private Long diseaseId;
        private String diseaseName;
        private Long count;
        private Double percentage;
        private String treatment;
    }
    
    /**
     * 药品使用内部类
     */
    @Data
    public static class MedicineUsage {
        private Long medicineId;
        private String medicineName;
        private String category;
        private Long usageCount;
        private Double percentage;
    }
}
