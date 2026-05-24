package com.plate.dto;

import lombok.Data;

@Data
public class DashboardStats {
    private Long onlineCameras;
    private Long totalCameras;
    private Long todayRecognitions;
    private Long anomalyCount;
    private Long activeBlacklist;
}
