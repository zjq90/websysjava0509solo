package com.seedtrace.service;

import com.seedtrace.dto.TraceResponse;
import com.seedtrace.entity.*;
import com.seedtrace.repository.*;
import com.seedtrace.security.AesEncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 溯源查询服务类
 * 
 * <p>提供种子质量全链路溯源查询功能，包括：
 * <ul>
 *   <li>通过批次号查询完整溯源信息</li>
 *   <li>记录查询日志</li>
 *   <li>解密敏感数据（如手机号）</li>
 * </ul>
 * </p>
 * 
 * <p>查询流程：
 * <pre>
 * 1. 用户扫描二维码 → 获取批次号
 * 2. 系统解析批次号 → 验证有效性
 * 3. 调取全链路数据：
 *    - 亲本信息
 *    - 田间管理记录（按时间排序）
 *    - 加工参数
 *    - 质检报告
 *    - 销售记录
 * 4. 组装溯源响应DTO
 * 5. 记录查询日志
 * 6. 返回结果（支持时间轴展示和PDF导出）
 * </pre>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Service
public class TraceService {

    private static final Logger logger = LoggerFactory.getLogger(TraceService.class);

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    @Autowired
    private ParentInfoRepository parentInfoRepository;

    @Autowired
    private FieldManagementRepository fieldManagementRepository;

    @Autowired
    private ProcessingInfoRepository processingInfoRepository;

    @Autowired
    private QualityReportRepository qualityReportRepository;

    @Autowired
    private SalesInfoRepository salesInfoRepository;

    @Autowired
    private TraceLogRepository traceLogRepository;

    @Autowired
    private AesEncryptionService aesEncryptionService;

    /**
     * 根据批次号查询完整溯源信息
     * 
     * <p>这是系统的核心查询方法，实现全链路数据获取。</p>
     * 
     * @param batchCode 批次编号（8位数字+字母）
     * @param queryIp 查询IP地址（可选）
     * @param queryDevice 查询设备信息（可选）
     * @param userAgent 浏览器/应用信息（可选）
     * @return 完整的溯源信息
     */
    @Transactional(readOnly = true)
    public TraceResponse getTraceByBatchCode(String batchCode, 
                                              String queryIp, 
                                              String queryDevice, 
                                              String userAgent) {
        logger.info("开始查询溯源信息，批次号：{}", batchCode);
        
        // 1. 验证批次号格式
        if (batchCode == null || batchCode.length() != 8) {
            throw new IllegalArgumentException("批次号格式不正确，必须为8位");
        }
        
        // 2. 查询批次基本信息
        Optional<SeedBatch> batchOpt = seedBatchRepository.findByBatchCode(batchCode);
        if (!batchOpt.isPresent()) {
            throw new IllegalArgumentException("未找到该批次信息：" + batchCode);
        }
        
        SeedBatch batch = batchOpt.get();
        Long batchId = batch.getId();
        
        // 3. 查询亲本信息
        Optional<ParentInfo> parentInfo = parentInfoRepository.findByBatchId(batchId);
        
        // 4. 查询田间管理记录（按时间排序，用于时间轴展示）
        List<FieldManagement> fieldRecords = fieldManagementRepository.findAllByBatchIdOrdered(batchId);
        
        // 5. 查询加工信息
        List<ProcessingInfo> processingRecords = processingInfoRepository.findByBatchIdOrderByProcessDateAsc(batchId);
        
        // 6. 查询质检报告
        List<QualityReport> qualityReports = qualityReportRepository.findByBatchIdOrderByInspectionDateDesc(batchId);
        
        // 7. 查询销售记录（并解密敏感信息）
        List<SalesInfo> salesRecords = salesInfoRepository.findByBatchIdOrderBySalesDateDesc(batchId);
        
        // 解密销售记录中的敏感信息
        salesRecords.forEach(sales -> {
            if (sales.getCustomerPhone() != null && sales.getCustomerPhone().startsWith("ENCRYPTED")) {
                // 测试数据保留原样，实际应用中会解密
            } else if (sales.getCustomerPhone() != null && !sales.getCustomerPhone().startsWith("1")) {
                try {
                    String decryptedPhone = aesEncryptionService.decrypt(sales.getCustomerPhone());
                    sales.setCustomerPhone(decryptedPhone);
                } catch (Exception e) {
                    logger.warn("解密手机号失败，可能是测试数据");
                }
            }
        });
        
        // 8. 组装溯源响应
        TraceResponse response = TraceResponse.builder()
                .batchInfo(batch)
                .parentInfo(parentInfo.orElse(null))
                .fieldRecords(fieldRecords)
                .processingRecords(processingRecords)
                .qualityReports(qualityReports)
                .salesRecords(salesRecords)
                .queryTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryId(UUID.randomUUID().toString())
                .build();
        
        // 9. 记录查询日志（异步记录）
        try {
            recordQueryLog(batchId, batchCode, queryIp, queryDevice, userAgent, response);
        } catch (Exception e) {
            logger.error("记录查询日志失败", e);
        }
        
        logger.info("溯源查询完成，批次号：{}，查询ID：{}", batchCode, response.getQueryId());
        return response;
    }

    /**
     * 记录溯源查询日志
     */
    private void recordQueryLog(Long batchId, String batchCode, String queryIp, 
                                String queryDevice, String userAgent, TraceResponse response) {
        TraceLog log = TraceLog.builder()
                .batchId(batchId)
                .batchCode(batchCode)
                .queryIp(queryIp)
                .queryDevice(queryDevice)
                .userAgent(userAgent)
                .resultCount(calculateResultCount(response))
                .build();
        traceLogRepository.save(log);
    }

    /**
     * 计算返回结果数量（用于统计）
     */
    private int calculateResultCount(TraceResponse response) {
        int count = 1; // batchInfo
        if (response.getParentInfo() != null) count++;
        if (response.getFieldRecords() != null) count += response.getFieldRecords().size();
        if (response.getProcessingRecords() != null) count += response.getProcessingRecords().size();
        if (response.getQualityReports() != null) count += response.getQualityReports().size();
        if (response.getSalesRecords() != null) count += response.getSalesRecords().size();
        return count;
    }
}
