package com.traceability.service;

import com.traceability.entity.*;
import com.traceability.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 追溯核心服务类
 * 将所有环节关联起来，实现全链条追溯
 */
@Service
@Transactional
public class TraceabilityService {

    @Autowired
    private SeedBatchService seedBatchService;

    @Autowired
    private ParentSourceService parentSourceService;

    @Autowired
    private FieldPlantingService fieldPlantingService;

    @Autowired
    private HarvestService harvestService;

    @Autowired
    private ProcessingService processingService;

    @Autowired
    private PackagingService packagingService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private QualityReportService qualityReportService;

    @Autowired
    private QrTraceService qrTraceService;

    /**
     * 根据批次号获取完整追溯链条
     */
    public Map<String, Object> getTraceabilityChainByBatchNo(String batchNo) {
        Map<String, Object> chain = new HashMap<>();
        
        seedBatchService.findByBatchNo(batchNo).ifPresent(seedBatch -> chain.put("seedBatch", seedBatch));
        chain.put("parentSources", parentSourceService.findByBatchNo(batchNo));
        chain.put("fieldPlantings", fieldPlantingService.findByBatchNo(batchNo));
        chain.put("harvests", harvestService.findByBatchNo(batchNo));
        chain.put("processings", processingService.findByBatchNo(batchNo));
        chain.put("packagings", packagingService.findByBatchNo(batchNo));
        chain.put("sales", saleService.findByBatchNo(batchNo));
        chain.put("qualityReports", qualityReportService.findByBatchNo(batchNo));
        chain.put("qrTraces", qrTraceService.findByBatchNo(batchNo));
        
        return chain;
    }

    /**
     * 根据二维码获取完整追溯链条
     */
    public Map<String, Object> getTraceabilityChainByQrCode(String qrCode) {
        Map<String, Object> chain = new HashMap<>();
        
        packagingService.findByQrCode(qrCode).ifPresent(packaging -> {
            chain.put("packaging", packaging);
            String batchNo = packaging.getBatchNo();
            
            seedBatchService.findByBatchNo(batchNo).ifPresent(seedBatch -> chain.put("seedBatch", seedBatch));
            chain.put("parentSources", parentSourceService.findByBatchNo(batchNo));
            chain.put("fieldPlantings", fieldPlantingService.findByBatchNo(batchNo));
            chain.put("harvests", harvestService.findByBatchNo(batchNo));
            chain.put("processings", processingService.findByBatchNo(batchNo));
            chain.put("sales", saleService.findByPackageNo(packaging.getPackageNo()));
            chain.put("qualityReports", qualityReportService.findByBatchNo(batchNo));
            chain.put("qrTraces", qrTraceService.findByQrCode(qrCode));
        });
        
        return chain;
    }

    /**
     * 根据包装号获取完整追溯链条
     */
    public Map<String, Object> getTraceabilityChainByPackageNo(String packageNo) {
        Map<String, Object> chain = new HashMap<>();
        
        packagingService.findByPackageNo(packageNo).ifPresent(packaging -> {
            chain.put("packaging", packaging);
            String batchNo = packaging.getBatchNo();
            
            seedBatchService.findByBatchNo(batchNo).ifPresent(seedBatch -> chain.put("seedBatch", seedBatch));
            chain.put("parentSources", parentSourceService.findByBatchNo(batchNo));
            chain.put("fieldPlantings", fieldPlantingService.findByBatchNo(batchNo));
            chain.put("harvests", harvestService.findByBatchNo(batchNo));
            chain.put("processings", processingService.findByBatchNo(batchNo));
            chain.put("sales", saleService.findByPackageNo(packaging.getPackageNo()));
            chain.put("qualityReports", qualityReportService.findByBatchNo(batchNo));
            chain.put("qrTraces", qrTraceService.findByPackageNo(packageNo));
        });
        
        return chain;
    }

    /**
     * 生成二维码图片（Base64）
     */
    public String generateQrCodeImage(String content) {
        return QrCodeUtil.generateQrCodeBase64(content);
    }
}
