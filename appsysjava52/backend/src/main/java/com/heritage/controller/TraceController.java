package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.TraceRecord;
import com.heritage.repository.TraceRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 溯源查询Controller
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/trace")
@Tag(name = "溯源查询", description = "区块链溯源、流转记录查询相关接口")
public class TraceController {

    @Autowired
    private TraceRecordRepository traceRecordRepository;

    /**
     * 查询文物溯源记录
     */
    @GetMapping("/heritage/{heritageId}")
    @Operation(summary = "查询文物溯源记录")
    public Result<List<TraceRecord>> getHeritageTrace(@PathVariable Long heritageId) {
        List<TraceRecord> records = traceRecordRepository.findByHeritageIdOrderByOccurTimeDesc(heritageId);
        return Result.success(records);
    }

    /**
     * 添加溯源记录
     */
    @PostMapping("/record")
    @Operation(summary = "添加溯源记录")
    public Result<TraceRecord> addTraceRecord(@RequestBody TraceRecord record) {
        // 模拟区块链哈希
        String blockchainHash = "0x" + Long.toHexString(System.currentTimeMillis()) 
                + record.getHeritageId() + record.getRecordType();
        record.setBlockchainHash(blockchainHash);
        TraceRecord saved = traceRecordRepository.save(record);
        return Result.success("溯源记录添加成功", saved);
    }

    /**
     * 同步到国家文物数据库
     */
    @PostMapping("/sync-national/{id}")
    @Operation(summary = "同步到国家文物数据库")
    public Result<Void> syncToNational(@PathVariable Long id) {
        TraceRecord record = traceRecordRepository.findById(id).orElse(null);
        if (record == null) {
            return Result.error("记录不存在");
        }
        record.setNationalSyncStatus(1);
        record.setNationalRecordNo("CN-HERITAGE-" + System.currentTimeMillis());
        traceRecordRepository.save(record);
        return Result.success("已同步到国家文物数据库");
    }
}