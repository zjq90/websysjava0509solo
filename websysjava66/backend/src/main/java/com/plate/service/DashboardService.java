package com.plate.service;

import com.plate.dto.DashboardStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private CameraService cameraService;

    @Autowired
    private RecognitionRecordService recordService;

    @Autowired
    private BlacklistService blacklistService;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setOnlineCameras(cameraService.getOnlineCameraCount());
        stats.setTotalCameras(cameraService.getTotalCameras());
        stats.setTodayRecognitions(recordService.getTodayRecognitions());
        stats.setAnomalyCount(recordService.getTodayAnomalyCount());
        stats.setActiveBlacklist(blacklistService.getActiveBlacklistCount());
        return stats;
    }
}
