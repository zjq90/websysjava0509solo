package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.FaultReport;
import com.appsys.service.FaultReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fault-report")
@Tag(name = "故障报修管理", description = "故障报修相关接口")
public class FaultReportController {

    @Autowired
    private FaultReportService faultReportService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户的报修列表")
    public Result<List<FaultReport>> listByUserId(@PathVariable Long userId) {
        return Result.success(faultReportService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取报修单")
    public Result<FaultReport> getById(@PathVariable Long id) {
        Optional<FaultReport> report = faultReportService.findById(id);
        return report.map(Result::success).orElseGet(() -> Result.error("报修单不存在"));
    }

    @PostMapping("/create")
    @Operation(summary = "创建报修单")
    public Result<FaultReport> create(@RequestBody Map<String, Object> request) {
        FaultReport faultReport = new FaultReport();
        faultReport.setUserId(Long.valueOf(request.get("userId").toString()));
        faultReport.setFaultType((String) request.get("faultType"));
        faultReport.setFaultDesc((String) request.get("faultDesc"));
        faultReport.setPhone((String) request.getOrDefault("phone", ""));
        faultReport.setAddress((String) request.getOrDefault("address", ""));
        faultReport.setImages((String) request.getOrDefault("images", ""));
        faultReport.setVideo((String) request.getOrDefault("video", ""));
        faultReport.setStatus("PENDING");
        return Result.success(faultReportService.save(faultReport));
    }

    @PostMapping("/diagnose")
    @Operation(summary = "智能诊断")
    public Result<Map<String, Object>> diagnose(@RequestParam String faultType) {
        return Result.success(faultReportService.diagnose(faultType));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新报修单状态")
    public Result<FaultReport> updateStatus(@PathVariable Long id, @RequestParam String status) {
        FaultReport report = faultReportService.updateStatus(id, status);
        if (report != null) {
            return Result.success(report);
        }
        return Result.error("报修单不存在");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除报修单")
    public Result<Void> delete(@PathVariable Long id) {
        faultReportService.deleteById(id);
        return Result.success();
    }
}
