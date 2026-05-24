package com.bikeshare.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 数据看板DTO
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
public class DashboardDTO {

    private OperationStats operationStats;
    private BikeStatusDistribution bikeStatus;
    private List<AreaUsage> areaUsage;

    @Data
    public static class OperationStats {
        private Long todayRides;
        private Long monthRides;
        private BigDecimal todayRevenue;
        private BigDecimal monthRevenue;
        private Long todayNewUsers;
        private Long monthNewUsers;
    }

    @Data
    public static class BikeStatusDistribution {
        private Long onlineCount;
        private Long offlineCount;
        private Long faultCount;
        private Long maintenanceCount;
    }

    @Data
    public static class AreaUsage {
        private String areaName;
        private Long rideCount;
        private Integer bikeCount;
    }
}
