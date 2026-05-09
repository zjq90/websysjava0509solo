package com.traceability.service;

import com.traceability.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 测试数据生成服务
 * 用于生成测试数据辅助功能测试
 */
@Service
@Transactional
public class TestDataService {

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

    private Random random = new Random();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 生成完整的测试数据（一个完整的追溯链条）
     */
    public String generateCompleteTestData() {
        String batchNo = "BAT" + System.currentTimeMillis();
        
        generateSeedBatch(batchNo);
        generateParentSource(batchNo);
        generateFieldPlanting(batchNo);
        generateHarvest(batchNo);
        generateProcessing(batchNo);
        String packageNo = generatePackaging(batchNo);
        generateQualityReport(batchNo);
        generateSale(batchNo, packageNo);
        generateQrTrace(batchNo, packageNo);
        
        return batchNo;
    }

    /**
     * 批量生成测试数据
     */
    public int generateBatchTestData(int count) {
        for (int i = 0; i < count; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generateCompleteTestData();
        }
        return count;
    }

    private void generateSeedBatch(String batchNo) {
        SeedBatch seedBatch = new SeedBatch();
        seedBatch.setBatchNo(batchNo);
        seedBatch.setSeedName(getRandomSeedName());
        seedBatch.setSeedVariety(getRandomVariety());
        seedBatch.setSeedType("常规种");
        seedBatch.setQuantity(new BigDecimal(1000 + random.nextInt(5000)));
        seedBatch.setUnit("公斤");
        seedBatch.setProducer("优质种子有限公司");
        seedBatch.setProductionLocation("北京市海淀区");
        seedBatch.setProductionDate(LocalDateTime.now().minusDays(30).format(formatter));
        seedBatch.setShelfLife("12个月");
        seedBatch.setStatus("已包装");
        seedBatch.setRemark("测试数据");
        seedBatchService.save(seedBatch);
    }

    private void generateParentSource(String batchNo) {
        ParentSource parentSource = new ParentSource();
        parentSource.setBatchNo(batchNo);
        parentSource.setParentName("亲本-" + batchNo);
        parentSource.setSourceOrigin("北京农业科学院");
        parentSource.setSourceType("自育亲本");
        parentSource.setCollectionDate(LocalDateTime.now().minusDays(365).format(formatter));
        parentSource.setCollector("张研究员");
        parentSource.setCollectionLocation("北京昌平试验基地");
        parentSource.setGeneticInfo("高产、抗病、优质");
        parentSource.setCertificateNo("CERT" + System.currentTimeMillis());
        parentSource.setRemark("测试数据");
        parentSourceService.save(parentSource);
    }

    private void generateFieldPlanting(String batchNo) {
        FieldPlanting fieldPlanting = new FieldPlanting();
        fieldPlanting.setBatchNo(batchNo);
        fieldPlanting.setFieldName("试验田A区");
        fieldPlanting.setFieldLocation("河北省石家庄市");
        fieldPlanting.setFieldArea(new BigDecimal(50 + random.nextInt(50)));
        fieldPlanting.setPlantingDate(LocalDateTime.now().minusDays(120).format(formatter));
        fieldPlanting.setPlantingMethod("机械播种");
        fieldPlanting.setPlantingDensity("15cm x 25cm");
        fieldPlanting.setSoilType("壤土");
        fieldPlanting.setFertilizerInfo("复合肥15-15-15，50kg/亩");
        fieldPlanting.setPesticideInfo("吡虫啉，常规用量");
        fieldPlanting.setIrrigationInfo("滴灌，每3天一次");
        fieldPlanting.setGrower("李师傅");
        fieldPlanting.setGrowthStage("已成熟");
        fieldPlanting.setRemark("测试数据");
        fieldPlantingService.save(fieldPlanting);
    }

    private void generateHarvest(String batchNo) {
        Harvest harvest = new Harvest();
        harvest.setBatchNo(batchNo);
        harvest.setHarvestDate(LocalDateTime.now().minusDays(20).format(formatter));
        harvest.setHarvestLocation("河北省石家庄市");
        harvest.setHarvestQuantity(new BigDecimal(800 + random.nextInt(400)));
        harvest.setUnit("公斤");
        harvest.setHarvestMethod("机械收获");
        harvest.setHarvester("王师傅");
        harvest.setMaturityLevel("完全成熟");
        harvest.setMoistureContent("12%");
        harvest.setQualityGrade("一级");
        harvest.setStorageLocation("1号仓库");
        harvest.setRemark("测试数据");
        harvestService.save(harvest);
    }

    private void generateProcessing(String batchNo) {
        Processing processing = new Processing();
        processing.setBatchNo(batchNo);
        processing.setProcessingDate(LocalDateTime.now().minusDays(10).format(formatter));
        processing.setProcessingLocation("加工车间A区");
        processing.setProcessingType("精选加工");
        processing.setProcessingEquipment("种子精选机、包衣机");
        processing.setInputQuantity(new BigDecimal(800 + random.nextInt(200)));
        processing.setOutputQuantity(new BigDecimal(750 + random.nextInt(150)));
        processing.setUnit("公斤");
        processing.setProcessingPerson("赵师傅");
        processing.setProcessingSteps("清选→精选→包衣→干燥→包装");
        processing.setQualityCheckResult("合格");
        processing.setRemark("测试数据");
        processingService.save(processing);
    }

    private String generatePackaging(String batchNo) {
        Packaging packaging = new Packaging();
        packaging.setBatchNo(batchNo);
        packaging.setPackagingDate(LocalDateTime.now().minusDays(5).format(formatter));
        packaging.setPackagingLocation("包装车间");
        packaging.setPackageType("复合袋");
        packaging.setPackageSpec("5kg/袋");
        packaging.setNetWeight(new BigDecimal(5));
        packaging.setUnit("公斤");
        packaging.setPackagingPerson("孙师傅");
        packaging.setStatus("待销售");
        packaging.setRemark("测试数据，一袋一码");
        packagingService.save(packaging);
        return packaging.getPackageNo();
    }

    private void generateQualityReport(String batchNo) {
        QualityReport report = new QualityReport();
        report.setBatchNo(batchNo);
        report.setTestDate(LocalDateTime.now().minusDays(15).format(formatter));
        report.setTestInstitution("本单位质检部");
        report.setTestItems("发芽率、纯度、净度、水分、千粒重");
        report.setGerminationRate(new BigDecimal("92.5"));
        report.setPurity(new BigDecimal("99.0"));
        report.setClarity(new BigDecimal("99.5"));
        report.setMoisture(new BigDecimal("12.0"));
        report.setThousandGrainWeight(new BigDecimal("35.5"));
        report.setHealthDegree(new BigDecimal("98.0"));
        report.setVigorIndex(new BigDecimal("85.0"));
        report.setTestResult("合格");
        report.setStatus("已发布");
        report.setTester("质检员刘");
        report.setReportFileUrl("/uploads/reports/" + batchNo + ".pdf");
        report.setRemark("测试数据");
        qualityReportService.save(report);
    }

    private void generateSale(String batchNo, String packageNo) {
        Sale sale = new Sale();
        sale.setBatchNo(batchNo);
        sale.setPackageNo(packageNo);
        sale.setSaleDate(LocalDateTime.now().minusDays(3).format(formatter));
        sale.setCustomerName("测试客户");
        sale.setCustomerPhone("13800138000");
        sale.setCustomerAddress("北京市朝阳区");
        sale.setSaleQuantity(new BigDecimal(100 + random.nextInt(100)));
        sale.setUnit("公斤");
        sale.setUnitPrice(new BigDecimal(15 + random.nextInt(10)));
        sale.setTotalAmount(sale.getSaleQuantity().multiply(sale.getUnitPrice()));
        sale.setSalesperson("销售员张");
        sale.setLogisticsInfo("顺丰速运");
        sale.setDeliveryStatus("已发货");
        sale.setRemark("测试数据");
        saleService.save(sale);
    }

    private void generateQrTrace(String batchNo, String packageNo) {
        QrTrace qrTrace = new QrTrace();
        Packaging packaging = packagingService.findByPackageNo(packageNo).orElse(null);
        if (packaging != null) {
            qrTrace.setQrCode(packaging.getQrCode());
            qrTrace.setPackageNo(packageNo);
            qrTrace.setBatchNo(batchNo);
            qrTrace.setScanTime(LocalDateTime.now().format(formatter));
            qrTrace.setScanLocation("北京市");
            qrTrace.setScannerIp("192.168.1.100");
            qrTrace.setScanDevice("智能手机");
            qrTrace.setScanCount(random.nextInt(5) + 1);
            qrTrace.setRemark("测试扫描记录");
            qrTraceService.save(qrTrace);
        }
    }

    private String getRandomSeedName() {
        String[] names = {"玉米种子", "小麦种子", "水稻种子", "大豆种子", "棉花种子", "花生种子", "油菜种子"};
        return names[random.nextInt(names.length)];
    }

    private String getRandomVariety() {
        String[] varieties = {"登海605", "郑单958", "先玉335", "农大108", "中单909", "浚单20", "伟科702"};
        return varieties[random.nextInt(varieties.length)];
    }
}
