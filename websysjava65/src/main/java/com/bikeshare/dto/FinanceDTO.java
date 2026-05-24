package com.bikeshare.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务DTO
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
public class FinanceDTO {

    @Data
    public static class ReconciliationResult {
        private String reconNo;
        private String reconDate;
        private String reconType;
        private BigDecimal systemAmount;
        private BigDecimal actualAmount;
        private BigDecimal diffAmount;
        private String status;
    }

    @Data
    public static class CostAnalysis {
        private BigDecimal totalDepreciation;
        private BigDecimal totalMaintenance;
        private BigDecimal totalOperation;
        private List<CostCategoryDetail> categoryDetails;
    }

    @Data
    public static class CostCategoryDetail {
        private String category;
        private BigDecimal amount;
    }
}
