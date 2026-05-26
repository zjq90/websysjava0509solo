package com.personal.accounting.controller;

import com.personal.accounting.dto.BatchUpdateDTO;
import com.personal.accounting.dto.TransactionDTO;
import com.personal.accounting.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录控制器
 * 提供交易记录的CRUD和批量操作API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "交易记录管理", description = "交易记录的增删改查及批量操作API")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "分页查询交易记录", description = "分页获取所有交易记录")
    public ResponseEntity<Page<TransactionDTO>> findAll(
            @PageableDefault(size = 20, sort = "transactionTime") Pageable pageable) {
        return ResponseEntity.ok(transactionService.findAll(pageable));
    }

    @GetMapping("/range")
    @Operation(summary = "按时间范围查询", description = "根据开始和结束时间查询交易记录")
    public ResponseEntity<List<TransactionDTO>> findByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(transactionService.findByTimeRange(start, end));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询交易记录", description = "根据交易记录ID获取详细信息")
    public ResponseEntity<TransactionDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "创建交易记录", description = "创建新的交易记录")
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO dto) {
        return new ResponseEntity<>(transactionService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新交易记录", description = "根据ID更新交易记录信息")
    public ResponseEntity<TransactionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除交易记录", description = "根据ID删除交易记录")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除交易记录", description = "根据ID列表批量删除交易记录")
    public ResponseEntity<Void> batchDelete(@RequestBody List<Long> ids) {
        transactionService.batchDelete(ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch/update-category")
    @Operation(summary = "批量更新分类", description = "为多笔交易记录批量更新分类")
    public ResponseEntity<Integer> batchUpdateCategory(@Valid @RequestBody BatchUpdateDTO dto) {
        int count = transactionService.batchUpdateCategory(dto.getTransactionIds(), dto.getCategoryId());
        return ResponseEntity.ok(count);
    }

    @PostMapping("/batch/update-tags")
    @Operation(summary = "批量更新标签", description = "为多笔交易记录批量更新标签")
    public ResponseEntity<Integer> batchUpdateTags(@Valid @RequestBody BatchUpdateDTO dto) {
        int count = transactionService.batchUpdateTags(dto.getTransactionIds(), dto.getTags());
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{id}/copy")
    @Operation(summary = "复制交易记录", description = "复制指定的交易记录（快捷键Ctrl+C）")
    public ResponseEntity<TransactionDTO> copy(@PathVariable Long id) {
        return new ResponseEntity<>(transactionService.copy(id), HttpStatus.CREATED);
    }
}
