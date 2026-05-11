package com.hospital.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 电子票据实体类
 * 存储电子发票/票据信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "biz_invoice")
public class Invoice {

    /**
     * 票据ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 票据编号（财政统一编号）
     */
    @Column(unique = true, nullable = false, length = 50)
    private String invoiceNo;

    /**
     * 票据代码
     */
    @Column(nullable = false, length = 20)
    private String invoiceCode;

    /**
     * 票据类型：REGISTRATION-挂号费，EXAM-检查费，TREATMENT-治疗费，MEDICINE-药费，OTHER-其他
     */
    @Column(nullable = false, length = 20)
    private String invoiceType;

    /**
     * 患者用户ID
     */
    @Column(nullable = false)
    private Long patientId;

    /**
     * 关联业务ID（如预约ID、支付ID）
     */
    private Long businessId;

    /**
     * 业务类型
     */
    @Column(length = 20)
    private String businessType;

    /**
     * 开票日期
     */
    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    /**
     * 开票金额（元）
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    /**
     * 开票内容
     */
    @Column(length = 500)
    private String content;

    /**
     * 开票单位名称
     */
    @Column(nullable = false, length = 200)
    private String issuerName;

    /**
     * 开票单位税号
     */
    @Column(length = 50)
    private String issuerTaxNo;

    /**
     * 付款人名称
     */
    @Column(nullable = false, length = 100)
    private String payerName;

    /**
     * 付款人税号
     */
    @Column(length = 50)
    private String payerTaxNo;

    /**
     * 校验码
     */
    @Column(length = 50)
    private String checkCode;

    /**
     * 票据状态：GENERATING-生成中，ISSUED-已开具，RED-已红冲，INVALID-已作废
     */
    @Column(nullable = false, length = 20)
    private String status = "ISSUED";

    /**
     * PDF文件路径
     */
    @Column(length = 500)
    private String pdfPath;

    /**
     * 查验链接
     */
    @Column(length = 500)
    private String verifyUrl;

    /**
     * 财政平台电子票据代码
     */
    @Column(length = 50)
    private String platformCode;

    /**
     * 财政平台票据号码
     */
    @Column(length = 50)
    private String platformNo;

    /**
     * 电子票据原始数据
     */
    @Column(columnDefinition = "TEXT")
    private String rawData;

    /**
     * 下载次数
     */
    private Integer downloadCount = 0;

    /**
     * 最后下载时间
     */
    private LocalDateTime lastDownloadTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Invoice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssuerTaxNo() {
        return issuerTaxNo;
    }

    public void setIssuerTaxNo(String issuerTaxNo) {
        this.issuerTaxNo = issuerTaxNo;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerTaxNo() {
        return payerTaxNo;
    }

    public void setPayerTaxNo(String payerTaxNo) {
        this.payerTaxNo = payerTaxNo;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public LocalDateTime getLastDownloadTime() {
        return lastDownloadTime;
    }

    public void setLastDownloadTime(LocalDateTime lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
