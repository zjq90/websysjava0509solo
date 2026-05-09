package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.service.TraceabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 追溯查询REST API控制器
 * 实现全链条追溯功能
 */
@RestController
@RequestMapping("/api/traceability")
public class TraceabilityController {

    @Autowired
    private TraceabilityService traceabilityService;

    /**
     * 根据批次号查询完整追溯链条
     */
    @GetMapping("/batch-no/{batchNo}")
    public Result<Map<String, Object>> getByBatchNo(@PathVariable String batchNo) {
        Map<String, Object> chain = traceabilityService.getTraceabilityChainByBatchNo(batchNo);
        return Result.success(chain);
    }

    /**
     * 根据二维码查询完整追溯链条
     */
    @GetMapping("/qr-code/{qrCode}")
    public Result<Map<String, Object>> getByQrCode(@PathVariable String qrCode) {
        Map<String, Object> chain = traceabilityService.getTraceabilityChainByQrCode(qrCode);
        return Result.success(chain);
    }

    /**
     * 根据包装号查询完整追溯链条
     */
    @GetMapping("/package-no/{packageNo}")
    public Result<Map<String, Object>> getByPackageNo(@PathVariable String packageNo) {
        Map<String, Object> chain = traceabilityService.getTraceabilityChainByPackageNo(packageNo);
        return Result.success(chain);
    }

    /**
     * 生成二维码图片
     */
    @GetMapping("/generate-qr")
    public Result<String> generateQrCode(@RequestParam String content) {
        String qrImage = traceabilityService.generateQrCodeImage(content);
        if (qrImage != null) {
            return Result.success(qrImage);
        }
        return Result.error("二维码生成失败");
    }
}
