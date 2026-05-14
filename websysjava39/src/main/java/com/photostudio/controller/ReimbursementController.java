package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Employee;
import com.photostudio.entity.Reimbursement;
import com.photostudio.entity.ReimbursementApprovalRecord;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.EmployeeRepository;
import com.photostudio.repository.ReimbursementRepository;
import com.photostudio.service.ReimbursementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 报销管理控制器
 * 提供报销的增删改查API接口，支持上传凭证、审批留痕功能
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reimbursements")
@CrossOrigin(origins = "*")
@Tag(name = "报销管理", description = "报销申请、审批CRUD API，支持凭证上传和审批留痕")
public class ReimbursementController {

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementService reimbursementService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    /**
     * 获取所有报销记录
     */
    @GetMapping
    @Operation(summary = "获取所有报销记录", description = "获取所有报销记录列表")
    public Result<List<Reimbursement>> getAllReimbursements() {
        return Result.success(reimbursementRepository.findAll());
    }

    /**
     * 根据状态获取报销记录
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取报销记录", description = "根据状态获取报销记录列表")
    public Result<List<Reimbursement>> getReimbursementsByStatus(@PathVariable String status) {
        return Result.success(reimbursementRepository.findByStatus(status));
    }

    /**
     * 根据员工ID获取报销记录
     */
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "根据员工ID获取报销记录", description = "根据员工ID获取报销记录列表")
    public Result<List<Reimbursement>> getReimbursementsByEmployeeId(@PathVariable Long employeeId) {
        return Result.success(reimbursementRepository.findByEmployeeId(employeeId));
    }

    /**
     * 根据ID获取报销记录详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取报销详情", description = "根据ID获取报销详情")
    public Result<Reimbursement> getReimbursementById(@PathVariable Long id) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("报销记录不存在: " + id));
        return Result.success(reimbursement);
    }

    /**
     * 获取报销的审批记录
     */
    @GetMapping("/{id}/records")
    @Operation(summary = "获取审批记录", description = "获取指定报销的审批流程记录")
    public Result<List<ReimbursementApprovalRecord>> getApprovalRecords(@PathVariable Long id) {
        return Result.success(reimbursementService.getApprovalRecords(id));
    }

    /**
     * 创建报销申请
     */
    @PostMapping("/apply")
    @Operation(summary = "创建报销申请", description = "创建新的报销申请")
    public Result<Reimbursement> createReimbursement(
            @Parameter(description = "申请人ID") @RequestParam Long employeeId,
            @Parameter(description = "报销标题") @RequestParam String title,
            @Parameter(description = "报销说明") @RequestParam(required = false) String description,
            @Parameter(description = "报销分类") @RequestParam String category,
            @Parameter(description = "报销金额") @RequestParam java.math.BigDecimal amount,
            @Parameter(description = "凭证图片URL") @RequestParam(required = false) String voucherUrl) {
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("员工不存在: " + employeeId));

        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setEmployee(employee);
        reimbursement.setTitle(title);
        reimbursement.setDescription(description);
        reimbursement.setCategory(category);
        reimbursement.setAmount(amount);
        reimbursement.setVoucherUrl(voucherUrl);

        return Result.success(reimbursementService.createReimbursement(reimbursement));
    }

    /**
     * 上传凭证图片
     */
    @PostMapping("/upload")
    @Operation(summary = "上传凭证图片", description = "单独上传报销凭证图片")
    public Result<Map<String, String>> uploadVoucherFile(@RequestParam("file") MultipartFile file) {
        String url = uploadVoucher(file);
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.success(result);
    }

    /**
     * 审批报销
     */
    @PutMapping("/{id}/approve")
    @Operation(summary = "审批报销", description = "审批报销申请（批准/拒绝），自动记录审批日志")
    public Result<Reimbursement> approveReimbursement(
            @PathVariable Long id,
            @Parameter(description = "审批人ID") @RequestParam Long approverId,
            @Parameter(description = "审批状态：APPROVED-批准，REJECTED-拒绝") @RequestParam String status,
            @Parameter(description = "审批备注") @RequestParam(required = false) String remark) {
        
        return Result.success(reimbursementService.approveReimbursement(id, approverId, status, remark));
    }

    /**
     * 更新报销申请
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新报销申请", description = "根据ID更新报销申请")
    public Result<Reimbursement> updateReimbursement(@PathVariable Long id, @RequestBody Reimbursement reimbursementDetails) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("报销记录不存在: " + id));
        
        reimbursement.setTitle(reimbursementDetails.getTitle());
        reimbursement.setDescription(reimbursementDetails.getDescription());
        reimbursement.setCategory(reimbursementDetails.getCategory());
        reimbursement.setAmount(reimbursementDetails.getAmount());
        reimbursement.setVoucherUrl(reimbursementDetails.getVoucherUrl());
        
        return Result.success(reimbursementRepository.save(reimbursement));
    }

    /**
     * 删除报销申请
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除报销申请", description = "根据ID删除报销申请")
    public Result<Void> deleteReimbursement(@PathVariable Long id) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("报销记录不存在: " + id));
        
        reimbursementRepository.delete(reimbursement);
        return Result.success();
    }

    /**
     * 上传凭证文件到服务器
     */
    private String uploadVoucher(MultipartFile file) {
        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : ".png";
            String fileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 返回访问路径
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
}
