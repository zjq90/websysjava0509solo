package com.music.platform.controller;

import com.music.platform.common.Result;
import com.music.platform.entity.Copyright;
import com.music.platform.entity.CopyrightReport;
import com.music.platform.service.CopyrightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/copyright")
@RequiredArgsConstructor
public class CopyrightController {
    private final CopyrightService copyrightService;

    @PostMapping("/declare")
    public Result<Copyright> declare(@RequestBody Map<String, Object> body) {
        Copyright copyright = copyrightService.createDeclaration(
                Long.valueOf(body.get("musicId").toString()),
                Long.valueOf(body.get("userId").toString()),
                (String) body.get("type"),
                (String) body.get("declaration"),
                body.get("isOriginal") != null ? Boolean.valueOf(body.get("isOriginal").toString()) : true
        );
        return Result.success(copyright);
    }

    @GetMapping("/music/{musicId}")
    public Result<Copyright> getByMusicId(@PathVariable Long musicId) {
        return copyrightService.getByMusicId(musicId)
                .map(Result::success)
                .orElse(Result.error(404, "Copyright not found"));
    }

    @PostMapping("/report")
    public Result<CopyrightReport> report(@RequestBody Map<String, Object> body) {
        CopyrightReport report = copyrightService.reportInfringement(
                Long.valueOf(body.get("copyrightId").toString()),
                Long.valueOf(body.get("reporterId").toString()),
                Long.valueOf(body.get("musicId").toString()),
                (String) body.get("reason"),
                (String) body.get("evidence")
        );
        return Result.success(report);
    }

    @GetMapping("/reports/pending")
    public Result<List<CopyrightReport>> getPendingReports() {
        return Result.success(copyrightService.getPendingReports());
    }

    @GetMapping("/reports/music/{musicId}")
    public Result<List<CopyrightReport>> getReportsByMusic(@PathVariable Long musicId) {
        return Result.success(copyrightService.getReportsByMusicId(musicId));
    }

    @PutMapping("/reports/{id}/status")
    public Result<CopyrightReport> updateReportStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.success(copyrightService.updateReportStatus(id, body.get("status")));
    }
}
