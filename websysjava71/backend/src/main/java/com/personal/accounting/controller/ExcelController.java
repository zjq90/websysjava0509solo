package com.personal.accounting.controller;

import com.personal.accounting.dto.TransactionDTO;
import com.personal.accounting.service.ExcelService;
import com.personal.accounting.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Excel导入导出控制器
 * 提供Excel/CSV文件的导入导出API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@Tag(name = "Excel导入导出", description = "交易记录的Excel/CSV导入导出API")
public class ExcelController {

    private final ExcelService excelService;
    private final TransactionService transactionService;

    @GetMapping("/export")
    @Operation(summary = "导出交易记录为Excel", description = "导出指定时间范围内的交易记录为Excel文件，包含自动计算公式")
    public ResponseEntity<byte[]> exportTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) throws IOException {
        
        List<TransactionDTO> transactions = transactionService.findByTimeRange(start, end);
        byte[] excelData = excelService.exportTransactionsToExcel(transactions);
        
        String filename = "transactions_" + System.currentTimeMillis() + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
    }

    @PostMapping("/import/preview")
    @Operation(summary = "预览CSV导入数据", description = "解析银行CSV文件并预览数据，不保存到数据库")
    public ResponseEntity<List<TransactionDTO>> previewImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long defaultAccountId) throws IOException {
        
        List<TransactionDTO> transactions = excelService.importBankCSV(file, defaultAccountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/import")
    @Operation(summary = "确认导入CSV数据", description = "保存预览的交易记录到数据库")
    public ResponseEntity<List<TransactionDTO>> confirmImport(
            @RequestBody List<TransactionDTO> transactions) {
        
        List<TransactionDTO> saved = excelService.saveImportedTransactions(transactions);
        return ResponseEntity.ok(saved);
    }
}
