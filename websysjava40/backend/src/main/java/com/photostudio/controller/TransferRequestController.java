package com.photostudio.controller;

import com.photostudio.entity.TransferRequest;
import com.photostudio.entity.TransferRequest.RequestStatus;
import com.photostudio.service.TransferRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调班申请控制器
 * 提供调班申请管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/transfer-requests")
@Tag(name = "调班申请", description = "调班申请的管理与审批")
public class TransferRequestController {

    private final TransferRequestService transferRequestService;

    @Autowired
    public TransferRequestController(TransferRequestService transferRequestService) {
        this.transferRequestService = transferRequestService;
    }

    @GetMapping
    @Operation(summary = "获取所有调班申请", description = "获取系统中所有调班申请")
    public ResponseEntity<List<TransferRequest>> getAllRequests() {
        return ResponseEntity.ok(transferRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取调班申请", description = "根据申请ID获取详细信息")
    public ResponseEntity<TransferRequest> getRequestById(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        return transferRequestService.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/requester/{requesterId}")
    @Operation(summary = "根据申请人获取调班申请", description = "获取指定员工的调班申请")
    public ResponseEntity<List<TransferRequest>> getRequestsByRequester(
            @Parameter(description = "申请人ID") @PathVariable Long requesterId) {
        return ResponseEntity.ok(transferRequestService.getRequestsByRequester(requesterId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取调班申请", description = "获取指定状态的调班申请")
    public ResponseEntity<List<TransferRequest>> getRequestsByStatus(
            @Parameter(description = "申请状态") @PathVariable RequestStatus status) {
        return ResponseEntity.ok(transferRequestService.getRequestsByStatus(status));
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待审批的调班申请", description = "获取所有待审批的调班申请")
    public ResponseEntity<List<TransferRequest>> getPendingRequests() {
        return ResponseEntity.ok(transferRequestService.getPendingRequests());
    }

    @PostMapping
    @Operation(summary = "创建调班申请", description = "提交新的调班申请")
    public ResponseEntity<TransferRequest> createRequest(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferRequestService.createRequest(request));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "审批调班申请", description = "审批调班申请，通过或拒绝")
    public ResponseEntity<TransferRequest> approveRequest(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        Long approverId = Long.parseLong(request.get("approverId").toString());
        boolean approved = Boolean.parseBoolean(request.get("approved").toString());
        String remark = request.get("remark") != null ? request.get("remark").toString() : null;
        
        return ResponseEntity.ok(transferRequestService.approveRequest(id, approverId, approved, remark));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消调班申请", description = "取消待审批的调班申请")
    public ResponseEntity<TransferRequest> cancelRequest(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        return ResponseEntity.ok(transferRequestService.cancelRequest(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除调班申请", description = "删除调班申请记录")
    public ResponseEntity<Void> deleteRequest(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        transferRequestService.deleteRequest(id);
        return ResponseEntity.ok().build();
    }
}
