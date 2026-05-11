package com.hospital.service;

import com.hospital.entity.Invoice;
import com.hospital.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 电子票据服务类
 * 处理电子发票/票据业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * 获取患者票据列表
     * 
     * @param patientId 患者ID
     * @return 票据列表
     */
    public List<Invoice> getPatientInvoices(Long patientId) {
        return invoiceRepository.findByPatientIdOrderByInvoiceDateDesc(patientId);
    }

    /**
     * 根据ID获取票据详情
     * 
     * @param id 票据ID
     * @param userId 用户ID
     * @return 票据详情
     */
    public Invoice getInvoiceById(Long id, Long userId) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("票据不存在"));

        if (!invoice.getPatientId().equals(userId)) {
            throw new RuntimeException("无权查看此票据");
        }

        return invoice;
    }

    /**
     * 按业务ID查询票据
     * 
     * @param businessId 业务ID
     * @param businessType 业务类型
     * @return 票据列表
     */
    public List<Invoice> getInvoicesByBusiness(Long businessId, String businessType) {
        return invoiceRepository.findByBusinessIdAndBusinessType(businessId, businessType);
    }

    /**
     * 创建电子票据
     * 
     * @param invoice 票据信息
     * @return 创建的票据
     */
    @Transactional(rollbackFor = Exception.class)
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNo("INV" + System.currentTimeMillis());
        invoice.setInvoiceCode("123456789012");
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setStatus("ISSUED");
        invoice.setIssuerName("某某市人民医院");
        invoice.setCheckCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setVerifyUrl("https://fapiao.chinatax.gov.cn/");
        invoice.setDownloadCount(0);

        return invoiceRepository.save(invoice);
    }

    /**
     * 记录下载操作
     * 
     * @param id 票据ID
     * @param userId 用户ID
     * @return 下载链接
     */
    @Transactional(rollbackFor = Exception.class)
    public String recordDownload(Long id, Long userId) {
        Invoice invoice = getInvoiceById(id, userId);
        
        invoice.setDownloadCount(invoice.getDownloadCount() + 1);
        invoice.setLastDownloadTime(LocalDateTime.now());
        invoiceRepository.save(invoice);

        return "http://localhost:8080/api/invoices/" + id + "/pdf";
    }
}
