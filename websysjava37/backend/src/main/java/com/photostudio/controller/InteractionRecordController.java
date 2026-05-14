package com.photostudio.controller;

import com.photostudio.entity.InteractionRecord;
import com.photostudio.service.InteractionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 互动记录管理控制器
 * 提供客户互动记录相关的REST API
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/interactions")
@Tag(name = "互动记录管理", description = "客户互动记录的增删改查")
@CrossOrigin(origins = "*")
public class InteractionRecordController {

    @Autowired
    private InteractionRecordService interactionRecordService;

    /**
     * 创建互动记录
     */
    @PostMapping
    @Operation(summary = "创建互动记录", description = "创建新的客户互动记录")
    public ResponseEntity<InteractionRecord> createInteractionRecord(
            @Valid @RequestBody InteractionRecord record) {
        InteractionRecord createdRecord = interactionRecordService.createInteractionRecord(record);
        return ResponseEntity.ok(createdRecord);
    }

    /**
     * 更新互动记录
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新互动记录", description = "根据ID更新互动记录")
    public ResponseEntity<InteractionRecord> updateInteractionRecord(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @Valid @RequestBody InteractionRecord recordDetails) {
        InteractionRecord updatedRecord = interactionRecordService.updateInteractionRecord(id, recordDetails);
        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * 删除互动记录
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除互动记录", description = "根据ID删除互动记录")
    public ResponseEntity<Void> deleteInteractionRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        interactionRecordService.deleteInteractionRecord(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据ID查询互动记录
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询互动记录", description = "根据ID查询互动记录")
    public ResponseEntity<InteractionRecord> getInteractionRecordById(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        return interactionRecordService.getInteractionRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询客户的所有互动记录
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "查询客户互动记录", description = "根据客户ID查询所有互动记录")
    public ResponseEntity<List<InteractionRecord>> getInteractionRecordsByCustomerId(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<InteractionRecord> records = interactionRecordService.getInteractionRecordsByCustomerId(customerId);
        return ResponseEntity.ok(records);
    }

    /**
     * 查询所有互动记录
     */
    @GetMapping
    @Operation(summary = "查询所有互动记录", description = "获取所有互动记录列表")
    public ResponseEntity<List<InteractionRecord>> getAllInteractionRecords() {
        List<InteractionRecord> records = interactionRecordService.getAllInteractionRecords();
        return ResponseEntity.ok(records);
    }
}
