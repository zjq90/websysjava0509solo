package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.dto.DashboardStatsDTO;
import com.petclinic.dto.DiseaseHeatmapDTO;
import com.petclinic.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计分析Controller
 */
@RestController
@RequestMapping("/statistics")
@Tag(name = "统计分析", description = "数据统计、热力图、用户增长分析")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取首页统计数据")
    public Result<DashboardStatsDTO> getDashboardStats() {
        return Result.success(statisticsService.getDashboardStats());
    }

    @GetMapping("/disease-heatmap")
    @Operation(summary = "获取疾病热力图数据")
    public Result<List<DiseaseHeatmapDTO>> getDiseaseHeatmap(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        return Result.success(statisticsService.getDiseaseHeatmap(startTime, endTime, petType, minAge, maxAge));
    }

    @GetMapping("/user-growth")
    @Operation(summary = "获取用户增长数据")
    public Result<Map<String, Object>> getUserGrowthData(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        if (startTime == null) startTime = LocalDateTime.now().minusDays(30);
        if (endTime == null) endTime = LocalDateTime.now();
        return Result.success(statisticsService.getUserGrowthData(startTime, endTime));
    }

    @GetMapping("/hospital-operation")
    @Operation(summary = "获取医院运营数据")
    public Result<Map<String, Object>> getHospitalOperationData(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        if (startTime == null) startTime = LocalDateTime.now().minusDays(30);
        if (endTime == null) endTime = LocalDateTime.now();
        return Result.success(statisticsService.getHospitalOperationData(startTime, endTime));
    }

    @GetMapping(value = "/export-consultation", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "导出问诊数据（脱敏）")
    public ResponseEntity<byte[]> exportConsultationData(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) throws Exception {
        if (startTime == null) startTime = LocalDateTime.now().minusDays(30);
        if (endTime == null) endTime = LocalDateTime.now();

        byte[] data = statisticsService.exportConsultationData(startTime, endTime);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "consultation_export.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}
