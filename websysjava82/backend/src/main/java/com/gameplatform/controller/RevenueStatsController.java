package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.RevenueStats;
import com.gameplatform.service.RevenueStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/revenue-stats")
public class RevenueStatsController {
    @Autowired
    private RevenueStatsService revenueStatsService;

    @GetMapping
    public Result<List<RevenueStats>> findAll() {
        return Result.success(revenueStatsService.findAll());
    }

    @GetMapping("/{id}")
    public Result<RevenueStats> findById(@PathVariable Long id) {
        return revenueStatsService.findById(id)
                .map(Result::success)
                .orElse(Result.error("统计数据不存在"));
    }

    @GetMapping("/slot/{adSlotId}")
    public Result<List<RevenueStats>> findByAdSlotId(@PathVariable Long adSlotId) {
        return Result.success(revenueStatsService.findByAdSlotId(adSlotId));
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary(
            @RequestParam(required = false) Long adSlotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(revenueStatsService.getSummary(adSlotId, startDate, endDate));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) Long adSlotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {
        byte[] excelBytes = revenueStatsService.exportToExcel(adSlotId, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "revenue_stats_" + startDate + "_" + endDate + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @PostMapping("/impression/{adSlotId}")
    public Result<RevenueStats> recordImpression(@PathVariable Long adSlotId) {
        return Result.success(revenueStatsService.recordImpression(adSlotId));
    }

    @PostMapping("/click/{adSlotId}")
    public Result<RevenueStats> recordClick(@PathVariable Long adSlotId) {
        return Result.success(revenueStatsService.recordClick(adSlotId));
    }
}
