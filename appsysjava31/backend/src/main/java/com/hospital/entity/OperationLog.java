package com.hospital.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 记录系统关键操作日志
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "sys_operation_log")
public class OperationLog {

    /**
     * 日志ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作类型：LOGIN-登录，LOGOUT-登出，REGISTER-注册，APPOINTMENT_CREATE-预约挂号，APPOINTMENT_CANCEL-取消预约，
     * PAYMENT-支付，REFUND-退款，REPORT_VIEW-查看报告，INVOICE_DOWNLOAD-下载票据，USER_UPDATE-用户信息修改等
     */
    @Column(nullable = false, length = 50)
    private String operationType;

    /**
     * 操作描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    @Column(length = 100)
    private String username;

    /**
     * 操作角色
     */
    @Column(length = 20)
    private String userRole;

    /**
     * 业务模块：USER-用户模块，APPOINTMENT-预约模块，PAYMENT-支付模块，REPORT-报告模块，INVOICE-票据模块，SYSTEM-系统模块
     */
    @Column(length = 20)
    private String module;

    /**
     * 业务ID（如预约ID、报告ID等）
     */
    private Long businessId;

    /**
     * 业务编号
     */
    @Column(length = 50)
    private String businessNo;

    /**
     * 请求方法：GET/POST/PUT/DELETE
     */
    @Column(length = 10)
    private String requestMethod;

    /**
     * 请求URL
     */
    @Column(length = 500)
    private String requestUrl;

    /**
     * 请求参数
     */
    @Column(columnDefinition = "TEXT")
    private String requestParams;

    /**
     * 操作结果：SUCCESS-成功，FAIL-失败
     */
    @Column(length = 10)
    private String result = "SUCCESS";

    /**
     * 错误信息
     */
    @Column(length = 1000)
    private String errorMsg;

    /**
     * 操作IP
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 操作位置
     */
    @Column(length = 100)
    private String location;

    /**
     * 浏览器/设备信息
     */
    @Column(length = 500)
    private String userAgent;

    /**
     * 操作耗时（毫秒）
     */
    private Long duration;

    /**
     * 操作时间
     */
    @Column(nullable = false)
    private LocalDateTime operationTime;

    public OperationLog() {
    }

    @PrePersist
    protected void onCreate() {
        operationTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }
}
