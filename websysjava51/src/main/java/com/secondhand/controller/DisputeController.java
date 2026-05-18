package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Dispute;
import com.secondhand.service.DisputeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/disputes")
@Tag(name = "纠纷管理", description = "纠纷的增删改查、证据上传、仲裁结果录入等接口")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @GetMapping
    @Operation(summary = "分页查询纠纷列表")
    public Result<Page<Dispute>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long complainantId,
            @RequestParam(required = false) Long respondentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(disputeService.findAll(title, type, status, complainantId, respondentId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询纠纷")
    public Result<Dispute> getById(@PathVariable Long id) {
        Optional<Dispute> dispute = disputeService.findById(id);
        return dispute.map(Result::success).orElse(Result.error("纠纷不存在"));
    }

    @PostMapping
    @Operation(summary = "新增纠纷")
    public Result<Dispute> create(@RequestBody Dispute dispute) {
        return Result.success(disputeService.save(dispute));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新纠纷")
    public Result<Dispute> update(@PathVariable Long id, @RequestBody Dispute dispute) {
        dispute.setId(id);
        return Result.success(disputeService.save(dispute));
    }

    @PostMapping("/upload-evidence")
    @Operation(summary = "上传证据文件")
    public Result<String> uploadEvidence(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(disputeService.uploadEvidence(file));
    }

    @PostMapping("/{id}/arbitration-result")
    @Operation(summary = "录入仲裁结果")
    public Result<Dispute> enterArbitrationResult(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String arbitrationResult = (String) params.get("arbitrationResult");
        Long arbitratorId = params.get("arbitratorId") != null ? Long.valueOf(params.get("arbitratorId").toString()) : null;
        return Result.success(disputeService.enterArbitrationResult(id, arbitrationResult, arbitratorId));
    }

}