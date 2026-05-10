package com.seedtrace.controller;

import com.seedtrace.dto.ApiResponse;
import com.seedtrace.dto.TraceResponse;
import com.seedtrace.service.PdfExportService;
import com.seedtrace.service.TraceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 溯源查询Controller
 * 
 * <p>提供种子质量追溯查询的REST API接口。</p>
 * 
 * <p>主要功能：
 * <ul>
 *   <li>通过批次号查询全链路溯源信息</li>
 *   <li>导出溯源报告PDF</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/trace")
@Tag(name = "溯源查询", description = "种子质量追溯查询相关接口")
@CrossOrigin(origins = "*")
public class TraceController {

    private static final Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Autowired
    private TraceService traceService;

    @Autowired
    private PdfExportService pdfExportService;

    /**
     * 根据批次号查询完整溯源信息
     * 
     * <p>这是系统的核心查询接口，用于前端展示全链路溯源信息。</p>
     * 
     * @param batchCode 批次编号（8位数字+字母）
     * @param queryIp 查询IP（可选，用于日志记录）
     * @param queryDevice 查询设备（可选，用于日志记录）
     * @param userAgent 浏览器/应用信息（可选，用于日志记录）
     * @return 完整的溯源信息
     */
    @GetMapping("/query/{batchCode}")
    @Operation(summary = "查询溯源信息", description = "根据批次号查询完整的种子质量溯源信息，包括亲本来源、田间管理、加工流程、质检报告、销售记录等")
    public ApiResponse<TraceResponse> queryTrace(
            @Parameter(description = "批次编号（8位数字+字母）", required = true)
            @PathVariable String batchCode,
            @Parameter(description = "查询IP地址")
            @RequestHeader(value = "X-Forwarded-For", required = false) String queryIp,
            @Parameter(description = "查询设备信息")
            @RequestHeader(value = "X-Device-Info", required = false) String queryDevice,
            @Parameter(description = "浏览器/应用信息")
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        
        logger.info("收到溯源查询请求，批次号：{}", batchCode);
        
        try {
            TraceResponse traceInfo = traceService.getTraceByBatchCode(batchCode, queryIp, queryDevice, userAgent);
            return ApiResponse.success("查询成功", traceInfo);
        } catch (IllegalArgumentException e) {
            logger.warn("溯源查询参数错误：{}", e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("溯源查询失败", e);
            return ApiResponse.error(500, "查询失败：" + e.getMessage());
        }
    }

    /**
     * 导出溯源报告PDF
     * 
     * <p>生成包含完整溯源信息的PDF报告，用于下载和分享。</p>
     * 
     * @param batchCode 批次编号
     * @return PDF文件
     */
    @GetMapping("/export/{batchCode}")
    @Operation(summary = "导出PDF报告", description = "根据批次号生成并下载溯源报告PDF")
    public ResponseEntity<byte[]> exportPdf(
            @Parameter(description = "批次编号", required = true)
            @PathVariable String batchCode) {
        
        logger.info("收到PDF导出请求，批次号：{}", batchCode);
        
        try {
            byte[] pdfData = pdfExportService.generateTracePdf(batchCode);
            
            String fileName = "溯源报告_" + batchCode + ".pdf";
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8")
                    .replace("+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentLength(pdfData.length);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            
            logger.info("PDF导出成功，批次号：{}，大小：{} bytes", batchCode, pdfData.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfData);
                    
        } catch (IllegalArgumentException e) {
            logger.warn("PDF导出参数错误：{}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("PDF导出失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
