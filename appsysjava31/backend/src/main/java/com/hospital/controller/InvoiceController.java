package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.dto.InvoiceVO;
import com.hospital.entity.Invoice;
import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import com.hospital.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 票据控制器
 * 处理电子票据相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patient/invoices")
@Tag(name = "票据管理", description = "电子票据相关接口")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取票据列表
     */
    @GetMapping
    @Operation(summary = "获取票据列表", description = "获取当前用户的所有电子票据")
    public Result<List<InvoiceVO>> getMyInvoices(@AuthenticationPrincipal Long userId) {
        List<Invoice> invoices = invoiceService.getPatientInvoices(userId);
        List<InvoiceVO> voList = invoices.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 获取票据详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取票据详情", description = "根据ID获取票据详细信息")
    public Result<InvoiceVO> getInvoiceById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id, userId);
        return Result.success(convertToVO(invoice));
    }

    /**
     * 下载票据
     */
    @GetMapping("/{id}/download")
    @Operation(summary = "下载票据", description = "获取电子票据PDF下载链接")
    @OperationLog(value = "下载票据", operationType = "INVOICE_DOWNLOAD", module = "INVOICE")
    public Result<String> downloadInvoice(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        String downloadUrl = invoiceService.recordDownload(id, userId);
        return Result.success("下载链接", downloadUrl);
    }

    /**
     * 查验票据
     */
    @GetMapping("/{id}/verify")
    @Operation(summary = "查验票据", description = "获取票据查验信息")
    public Result<InvoiceVO> verifyInvoice(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id, userId);
        return Result.success(convertToVO(invoice));
    }

    /**
     * 按业务ID查询票据
     */
    @GetMapping("/business")
    @Operation(summary = "按业务查询票据", description = "根据业务ID获取关联票据")
    public Result<List<InvoiceVO>> getInvoicesByBusiness(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long businessId,
            @RequestParam String businessType) {
        List<Invoice> invoices = invoiceService.getInvoicesByBusiness(businessId, businessType);
        List<InvoiceVO> voList = invoices.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 创建票据（测试用）
     */
    @PostMapping("/create")
    @Operation(summary = "创建票据", description = "模拟创建电子票据（测试用）")
    public Result<InvoiceVO> createInvoice(@RequestBody Invoice invoice) {
        Invoice saved = invoiceService.createInvoice(invoice);
        return Result.success(convertToVO(saved));
    }

    /**
     * 将Invoice转换为InvoiceVO
     */
    private InvoiceVO convertToVO(Invoice invoice) {
        InvoiceVO vo = new InvoiceVO();
        vo.setId(invoice.getId());
        vo.setInvoiceNo(invoice.getInvoiceNo());
        vo.setInvoiceType(invoice.getInvoiceType());
        vo.setPatientId(invoice.getPatientId());
        vo.setAppointmentId(invoice.getBusinessId());
        vo.setAmount(invoice.getAmount());
        vo.setStatus(invoice.getStatus());
        vo.setPdfUrl(invoice.getPdfPath());
        vo.setCheckCode(invoice.getCheckCode());
        vo.setIssueTime(invoice.getInvoiceDate());
        vo.setCreateTime(invoice.getCreateTime());
        vo.setUpdateTime(invoice.getUpdateTime());
        
        String type = "MEDICAL";
        if ("REGISTRATION".equals(invoice.getInvoiceType())) {
            type = "REGISTER";
        }
        vo.setType(type);
        
        String title = "医疗电子票据";
        if ("REGISTRATION".equals(invoice.getInvoiceType())) {
            title = "挂号费电子票据";
        } else if ("EXAM".equals(invoice.getInvoiceType())) {
            title = "检查费电子票据";
        } else if ("TREATMENT".equals(invoice.getInvoiceType())) {
            title = "治疗费电子票据";
        } else if ("MEDICINE".equals(invoice.getInvoiceType())) {
            title = "药品费电子票据";
        }
        vo.setTitle(title);
        
        vo.setPatientName(invoice.getPayerName());
        if (invoice.getPayerName() == null || invoice.getPayerName().isEmpty()) {
            User patient = userRepository.findById(invoice.getPatientId()).orElse(null);
            if (patient != null) {
                vo.setPatientName(patient.getName());
            }
        }
        
        return vo;
    }
}
