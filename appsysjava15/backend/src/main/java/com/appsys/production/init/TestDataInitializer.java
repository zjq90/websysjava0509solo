package com.appsys.production.init;

import com.appsys.production.config.EncryptionConfig;
import com.appsys.production.entity.*;
import com.appsys.production.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductionBatchRepository batchRepository;

    @Autowired
    private BatchStageRepository stageRepository;

    @Autowired
    private QualityInspectionRepository inspectionRepository;

    @Autowired
    private EncryptionConfig encryptionConfig;

    private static final List<String> STAGES = Arrays.asList("CLEANING", "COATING", "PACKAGING", "INSPECTION");
    private static final List<String> STAGE_NAMES = Arrays.asList("清选", "包衣", "分装", "质检");

    @Override
    public void run(String... args) {
        System.out.println("========== 开始初始化测试数据 ==========");
        try {
            initUsers();
            System.out.println("用户数据初始化完成");
            
            initBatches();
            System.out.println("批次数据初始化完成");
            
            System.out.println("========== 测试数据初始化成功 ==========");
        } catch (Exception e) {
            System.err.println("========== 测试数据初始化失败 ==========");
            System.err.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initUsers() {
        if (userRepository.count() > 0) {
            System.out.println("用户数据已存在，跳过初始化");
            return;
        }

        System.out.println("开始创建用户数据...");

        try {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRealName("系统管理员");
            admin.setRole("ADMIN");
            admin.setPhoneEncrypted(safeEncrypt("13800138001"));
            admin.setEmail("admin@example.com");
            admin.setEnabled(true);
            userRepository.save(admin);

            User supervisor = new User();
            supervisor.setUsername("supervisor");
            supervisor.setPassword("123456");
            supervisor.setRealName("张主管");
            supervisor.setRole("SUPERVISOR");
            supervisor.setPhoneEncrypted(safeEncrypt("13800138002"));
            supervisor.setEmail("supervisor@example.com");
            supervisor.setEnabled(true);
            userRepository.save(supervisor);

            User operator1 = new User();
            operator1.setUsername("operator1");
            operator1.setPassword("123456");
            operator1.setRealName("李操作");
            operator1.setRole("OPERATOR");
            operator1.setPhoneEncrypted(safeEncrypt("13800138003"));
            operator1.setEmail("operator1@example.com");
            operator1.setEnabled(true);
            userRepository.save(operator1);

            User operator2 = new User();
            operator2.setUsername("operator2");
            operator2.setPassword("123456");
            operator2.setRealName("王操作");
            operator2.setRole("OPERATOR");
            operator2.setPhoneEncrypted(safeEncrypt("13800138004"));
            operator2.setEmail("operator2@example.com");
            operator2.setEnabled(true);
            userRepository.save(operator2);

            System.out.println("创建了4个用户");
        } catch (Exception e) {
            System.err.println("创建用户时出错: " + e.getMessage());
            throw e;
        }
    }

    private void initBatches() {
        if (batchRepository.count() > 0) {
            System.out.println("批次数据已存在，跳过初始化");
            return;
        }

        System.out.println("开始创建批次数据...");

        try {
            createBatch1();
            createBatch2();
            createBatch3();
            System.out.println("创建了3个批次");
        } catch (Exception e) {
            System.err.println("创建批次时出错: " + e.getMessage());
            throw e;
        }
    }

    private void createBatch1() {
        ProductionBatch batch = new ProductionBatch();
        batch.setBatchNo("BATCH001");
        batch.setProductName("优质小麦种子");
        batch.setQuantity(5000.0);
        batch.setUnit("公斤");
        batch.setShelfLife(LocalDate.now().plusMonths(12));
        batch.setCustomerName("丰收农业公司");
        batch.setCustomerPhoneEncrypted(safeEncrypt("13900139001"));
        batch.setStatus("IN_PROGRESS");
        batch.setCurrentStage("COATING");
        batch.setRemark("春播第一批");
        batch.setOperatorId(3L);
        batchRepository.save(batch);
        createStagesForBatch1(batch.getId());
    }

    private void createStagesForBatch1(Long batchId) {
        LocalDateTime now = LocalDateTime.now();

        BatchStage stage1 = new BatchStage();
        stage1.setBatchId(batchId);
        stage1.setStageCode("CLEANING");
        stage1.setStageName("清选");
        stage1.setSortOrder(1);
        stage1.setStartTime(now.minusHours(5));
        stage1.setEndTime(now.minusHours(3));
        stage1.setOperatorId(3L);
        stage1.setOperatorName("李操作");
        stage1.setStatus("COMPLETED");
        stage1.setProcessParams("{\"cleaningSpeed\":\"500kg/h\",\"cleaningLevel\":\"一级\"}");
        stageRepository.save(stage1);

        BatchStage stage2 = new BatchStage();
        stage2.setBatchId(batchId);
        stage2.setStageCode("COATING");
        stage2.setStageName("包衣");
        stage2.setSortOrder(2);
        stage2.setStartTime(now.minusHours(2));
        stage2.setOperatorId(3L);
        stage2.setOperatorName("李操作");
        stage2.setStatus("IN_PROGRESS");
        stage2.setProcessParams("{\"coatingType\":\"红色包衣剂\",\"dosage\":\"2ml/kg\"}");
        stageRepository.save(stage2);

        for (int i = 2; i < STAGES.size(); i++) {
            BatchStage stage = new BatchStage();
            stage.setBatchId(batchId);
            stage.setStageCode(STAGES.get(i));
            stage.setStageName(STAGE_NAMES.get(i));
            stage.setSortOrder(i + 1);
            stage.setStatus("PENDING");
            stageRepository.save(stage);
        }

        QualityInspection inspection1 = new QualityInspection();
        inspection1.setBatchId(batchId);
        inspection1.setStageCode("CLEANING");
        inspection1.setOperatorId(3L);
        inspection1.setOperatorName("李操作");
        inspection1.setMoisture(new BigDecimal("12.5"));
        inspection1.setPurity(new BigDecimal("98.5"));
        inspection1.setGerminationRate(new BigDecimal("92.0"));
        inspection1.setResult("PASS");
        inspectionRepository.save(inspection1);
    }

    private void createBatch2() {
        ProductionBatch batch = new ProductionBatch();
        batch.setBatchNo("BATCH002");
        batch.setProductName("玉米杂交种子");
        batch.setQuantity(3000.0);
        batch.setUnit("公斤");
        batch.setShelfLife(LocalDate.now().plusMonths(9));
        batch.setCustomerName("绿源种植合作社");
        batch.setCustomerPhoneEncrypted(safeEncrypt("13900139002"));
        batch.setStatus("COMPLETED");
        batch.setCurrentStage(null);
        batch.setRemark("夏播专用品种");
        batch.setOperatorId(4L);
        batchRepository.save(batch);
        createStagesForBatch2(batch.getId());
    }

    private void createStagesForBatch2(Long batchId) {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < STAGES.size(); i++) {
            BatchStage stage = new BatchStage();
            stage.setBatchId(batchId);
            stage.setStageCode(STAGES.get(i));
            stage.setStageName(STAGE_NAMES.get(i));
            stage.setSortOrder(i + 1);
            stage.setStartTime(now.minusDays(2).plusHours(i * 4));
            stage.setEndTime(now.minusDays(2).plusHours((i + 1) * 4));
            stage.setOperatorId(4L);
            stage.setOperatorName("王操作");
            stage.setStatus("COMPLETED");
            if (i == 0) {
                stage.setProcessParams("{\"cleaningSpeed\":\"450kg/h\",\"cleaningLevel\":\"特级\"}");
            } else if (i == 1) {
                stage.setProcessParams("{\"coatingType\":\"蓝色包衣剂\",\"dosage\":\"2.5ml/kg\"}");
            } else if (i == 2) {
                stage.setProcessParams("{\"packageType\":\"编织袋\",\"weightPerBag\":\"25kg\"}");
            }
            stageRepository.save(stage);

            QualityInspection inspection = new QualityInspection();
            inspection.setBatchId(batchId);
            inspection.setStageCode(STAGES.get(i));
            inspection.setOperatorId(4L);
            inspection.setOperatorName("王操作");
            inspection.setMoisture(new BigDecimal("11.5").add(new BigDecimal(i * 0.3)));
            inspection.setPurity(new BigDecimal("99.0").subtract(new BigDecimal(i * 0.1)));
            inspection.setGerminationRate(new BigDecimal("95.0").subtract(new BigDecimal(i * 0.5)));
            inspection.setResult("PASS");
            inspectionRepository.save(inspection);
        }
    }

    private void createBatch3() {
        ProductionBatch batch = new ProductionBatch();
        batch.setBatchNo("BATCH003");
        batch.setProductName("大豆优良品种");
        batch.setQuantity(2000.0);
        batch.setUnit("公斤");
        batch.setShelfLife(LocalDate.now().plusMonths(8));
        batch.setCustomerName("新希望农场");
        batch.setCustomerPhoneEncrypted(safeEncrypt("13900139003"));
        batch.setStatus("PENDING");
        batch.setCurrentStage("CLEANING");
        batch.setRemark("高蛋白品种");
        batch.setOperatorId(null);
        batchRepository.save(batch);

        for (int i = 0; i < STAGES.size(); i++) {
            BatchStage stage = new BatchStage();
            stage.setBatchId(batch.getId());
            stage.setStageCode(STAGES.get(i));
            stage.setStageName(STAGE_NAMES.get(i));
            stage.setSortOrder(i + 1);
            stage.setStatus("PENDING");
            stageRepository.save(stage);
        }
    }

    private String safeEncrypt(String data) {
        if (data == null) {
            return null;
        }
        try {
            return encryptionConfig.encrypt(data);
        } catch (Exception e) {
            System.err.println("加密失败: " + data + ", 错误: " + e.getMessage());
            return data;
        }
    }
}
