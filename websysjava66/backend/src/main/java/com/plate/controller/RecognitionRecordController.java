package com.plate.controller;

import com.plate.common.Result;
import com.plate.dto.RecordQueryDTO;
import com.plate.entity.RecognitionRecord;
import com.plate.service.RecognitionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@Tag(name = "识别记录管理", description = "历史记录查询和导出接口")
public class RecognitionRecordController {
    @Autowired
    private RecognitionRecordService recordService;

    @PostMapping("/query")
    @Operation(summary = "分页查询识别记录")
    public Result<Page<RecognitionRecord>> queryRecords(@RequestBody RecordQueryDTO queryDTO) {
        return Result.success(recordService.getRecords(queryDTO));
    }

    @PostMapping
    @Operation(summary = "创建识别记录")
    public Result<RecognitionRecord> create(@RequestBody RecognitionRecord record) {
        return Result.success(recordService.saveRecord(record));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除识别记录")
    public Result<Void> delete(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return Result.success();
    }

    @PostMapping("/export/csv")
    @Operation(summary = "导出CSV")
    public ResponseEntity<byte[]> exportCsv(@RequestBody RecordQueryDTO queryDTO) throws IOException {
        List<RecognitionRecord> records = recordService.getAllRecordsForExport(queryDTO);
        byte[] data = recordService.exportToCsv(records);

        String filename = "records_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(data);
    }

    @PostMapping("/export/excel")
    @Operation(summary = "导出Excel")
    public ResponseEntity<byte[]> exportExcel(@RequestBody RecordQueryDTO queryDTO) throws IOException {
        List<RecognitionRecord> records = recordService.getAllRecordsForExport(queryDTO);
        byte[] data = recordService.exportToExcel(records);

        String filename = "records_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }
}
