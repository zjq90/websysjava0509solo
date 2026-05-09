package com.production.config;

import com.production.entity.*;
import com.production.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 测试数据初始化器
 * 在应用启动时自动生成测试数据
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProcessingRecordService processingRecordService;

    @Autowired
    private ProcessParameterService processParameterService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentRecordService equipmentRecordService;

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // 只有在没有数据时才生成测试数据
        if (productService.findAll().isEmpty()) {
            System.out.println("======================================");
            System.out.println("  正在生成测试数据...");
            System.out.println("======================================");
            
            generateProducts();
            generateEquipment();
            generateProductionPlans();
            generateProcessingRecords();
            generateProcessParameters();
            generateEquipmentRecords();
            
            System.out.println("======================================");
            System.out.println("  测试数据生成完成!");
            System.out.println("======================================");
        }
    }

    /**
     * 生成产品测试数据
     */
    private void generateProducts() {
        String[][] productData = {
            {"P001", "玉米种子-A型", "5kg/袋", "种子", "袋"},
            {"P002", "玉米种子-B型", "10kg/袋", "种子", "袋"},
            {"P003", "小麦种子-优级", "25kg/袋", "种子", "袋"},
            {"P004", "水稻种子-杂交", "2kg/袋", "种子", "袋"},
            {"P005", "大豆种子-高蛋白", "50kg/袋", "种子", "袋"},
            {"P006", "复合肥-NPK15-15-15", "50kg/袋", "化肥", "袋"},
            {"P007", "尿素-46%", "40kg/袋", "化肥", "袋"},
            {"P008", "杀虫剂-高效", "1L/瓶", "农药", "瓶"}
        };

        for (String[] data : productData) {
            Product product = new Product();
            product.setProductCode(data[0]);
            product.setProductName(data[1]);
            product.setSpecification(data[2]);
            product.setProductType(data[3]);
            product.setUnit(data[4]);
            product.setDescription("测试产品数据 - " + data[1]);
            product.setStatus("ACTIVE");
            productService.save(product);
        }
        System.out.println("  - 已生成 8 个产品");
    }

    /**
     * 生成设备测试数据
     */
    private void generateEquipment() {
        String[][] equipmentData = {
            {"EQ001", "清选机-1号", "CLEANER", "QX-2000", "甲车间-1线", "192.168.1.101", "8080"},
            {"EQ002", "清选机-2号", "CLEANER", "QX-2000", "甲车间-2线", "192.168.1.102", "8080"},
            {"EQ003", "包衣机-1号", "COATER", "BY-500", "乙车间-1线", "192.168.1.201", "8081"},
            {"EQ004", "包衣机-2号", "COATER", "BY-500", "乙车间-2线", "192.168.1.202", "8081"},
            {"EQ005", "包装机-A型", "PACKAGER", "BZ-A100", "丙车间-1线", "192.168.1.301", "8082"},
            {"EQ006", "包装机-B型", "PACKAGER", "BZ-B200", "丙车间-2线", "192.168.1.302", "8082"},
            {"EQ007", "质量检测仪", "INSPECTOR", "JC-300", "丁车间", "192.168.1.401", "8083"},
            {"EQ008", "输送机-主线路", "CONVEYOR", "SS-500", "主车间", "192.168.1.501", "8084"}
        };

        String[] statuses = {"RUNNING", "IDLE", "RUNNING", "IDLE", "RUNNING", "MAINTENANCE", "RUNNING", "RUNNING"};
        String[] persons = {"张工", "李工", "王工", "赵工", "刘工", "陈工", "周工", "吴工"};

        for (int i = 0; i < equipmentData.length; i++) {
            String[] data = equipmentData[i];
            Equipment equipment = new Equipment();
            equipment.setEquipmentCode(data[0]);
            equipment.setEquipmentName(data[1]);
            equipment.setEquipmentType(data[2]);
            equipment.setModel(data[3]);
            equipment.setManufacturer("XX机械设备制造有限公司");
            equipment.setLocation(data[4]);
            equipment.setIpAddress(data[5]);
            equipment.setPort(Integer.parseInt(data[6]));
            equipment.setStatus(statuses[i]);
            equipment.setResponsiblePerson(persons[i]);
            equipment.setDescription("测试设备 - " + data[1]);
            equipmentService.save(equipment);
        }
        System.out.println("  - 已生成 8 台设备");
    }

    /**
     * 生成生产计划测试数据
     */
    private void generateProductionPlans() {
        List<Product> products = productService.findAllActive();
        LocalDate today = LocalDate.now();
        
        String[] priorities = {"HIGH", "MEDIUM", "MEDIUM", "LOW", "MEDIUM", "HIGH", "MEDIUM", "LOW"};
        String[] statuses = {"RUNNING", "PENDING", "COMPLETED", "PENDING", "RUNNING", "PENDING", "PAUSED", "COMPLETED"};
        String[] lines = {"A线", "B线", "A线", "C线", "B线", "A线", "C线", "B线"};

        for (int i = 0; i < 8; i++) {
            Product product = products.get(i % products.size());
            
            ProductionPlan plan = new ProductionPlan();
            plan.setPlanCode("PLAN" + String.format("%03d", i + 1));
            plan.setPlanName(product.getProductName() + "生产计划-" + (i + 1));
            plan.setProduct(product);
            plan.setBatchNumber("B" + today.getYear() + String.format("%02d%02d", today.getMonthValue(), today.getDayOfMonth()) + String.format("%02d", i + 1));
            plan.setPlanQuantity(new BigDecimal(1000 + random.nextInt(5000)));
            plan.setProducedQuantity(new BigDecimal(random.nextInt(plan.getPlanQuantity().intValue())));
            plan.setPlanStartDate(today.minusDays(random.nextInt(5)));
            plan.setPlanEndDate(today.plusDays(random.nextInt(10) + 1));
            plan.setProductionLine(lines[i]);
            plan.setPriority(priorities[i]);
            plan.setStatus(statuses[i]);
            plan.setResponsiblePerson("生产主管-" + (i % 4 + 1));
            plan.setRemarks("测试生产计划");
            
            if ("RUNNING".equals(statuses[i])) {
                plan.setActualStartTime(LocalDateTime.now().minusHours(random.nextInt(24)));
            } else if ("COMPLETED".equals(statuses[i])) {
                plan.setActualStartTime(LocalDateTime.now().minusDays(random.nextInt(5) + 1));
                plan.setActualEndTime(LocalDateTime.now().minusHours(random.nextInt(24)));
                plan.setProducedQuantity(plan.getPlanQuantity());
            }
            
            productionPlanService.save(plan);
        }
        System.out.println("  - 已生成 8 个生产计划");
    }

    /**
     * 生成加工流程记录测试数据
     */
    private void generateProcessingRecords() {
        List<ProductionPlan> plans = productionPlanService.findAll();
        List<Equipment> equipments = equipmentService.findAll();

        String[][] processTypes = {
            {"CLEANING", "原料清选工序", "1"},
            {"COATING", "种子包衣工序", "2"},
            {"PACKAGING", "产品分装工序", "3"},
            {"INSPECTION", "质量检验工序", "4"}
        };

        int recordCount = 0;
        for (ProductionPlan plan : plans) {
            for (String[] process : processTypes) {
                // 随机跳过一些工序（模拟部分完成）
                if (random.nextDouble() > 0.3) {
                    ProcessingRecord record = new ProcessingRecord();
                    record.setRecordCode("PR" + String.format("%05d", recordCount + 1));
                    record.setProductionPlan(plan);
                    record.setProcessType(process[0]);
                    record.setProcessName(process[1]);
                    record.setProcessOrder(Integer.parseInt(process[2]));
                    record.setQuantity(plan.getPlanQuantity());
                    record.setQualifiedQuantity(plan.getPlanQuantity().multiply(new BigDecimal("0.98")));
                    record.setUnqualifiedQuantity(plan.getPlanQuantity().multiply(new BigDecimal("0.02")));
                    record.setStartTime(LocalDateTime.now().minusDays(random.nextInt(5)));
                    
                    String status = random.nextBoolean() ? "COMPLETED" : "PROCESSING";
                    record.setStatus(status);
                    if ("COMPLETED".equals(status)) {
                        record.setEndTime(record.getStartTime().plusHours(random.nextInt(8) + 1));
                    }
                    
                    record.setOperator("操作员-" + (random.nextInt(10) + 1));
                    
                    // 随机分配设备
                    String type = process[0];
                    for (Equipment eq : equipments) {
                        if (type.equals(eq.getEquipmentType()) || 
                            (type.equals("PACKAGING") && "PACKAGER".equals(eq.getEquipmentType())) ||
                            (type.equals("CLEANING") && "CLEANER".equals(eq.getEquipmentType())) ||
                            (type.equals("COATING") && "COATER".equals(eq.getEquipmentType())) ||
                            (type.equals("INSPECTION") && "INSPECTOR".equals(eq.getEquipmentType()))) {
                            if (random.nextBoolean()) {
                                record.setEquipment(eq);
                                break;
                            }
                        }
                    }
                    
                    record.setRemarks("测试加工记录");
                    processingRecordService.save(record);
                    recordCount++;
                }
            }
        }
        System.out.println("  - 已生成 " + recordCount + " 条加工记录");
    }

    /**
     * 生成工艺参数测试数据
     */
    private void generateProcessParameters() {
        List<ProcessingRecord> records = processingRecordService.findAll();
        
        int paramCount = 0;
        for (ProcessingRecord record : records) {
            if (random.nextDouble() > 0.3) {
                createParameter(record, "温度", "temp", "℃", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("25"));
                paramCount++;
            }
            if (random.nextDouble() > 0.3) {
                createParameter(record, "湿度", "humidity", "%", new BigDecimal("40"), new BigDecimal("60"), new BigDecimal("50"));
                paramCount++;
            }
            if (random.nextDouble() > 0.3) {
                createParameter(record, "转速", "speed", "rpm", new BigDecimal("800"), new BigDecimal("1200"), new BigDecimal("1000"));
                paramCount++;
            }
            if (random.nextDouble() > 0.3) {
                createParameter(record, "压力", "pressure", "MPa", new BigDecimal("0.2"), new BigDecimal("0.4"), new BigDecimal("0.3"));
                paramCount++;
            }
        }
        System.out.println("  - 已生成 " + paramCount + " 条工艺参数");
    }

    private void createParameter(ProcessingRecord record, String name, String code, String unit, 
                                 BigDecimal lower, BigDecimal upper, BigDecimal base) {
        ProcessParameter parameter = new ProcessParameter();
        parameter.setProcessingRecord(record);
        parameter.setParameterName(name);
        parameter.setParameterCode(code);
        parameter.setParameterUnit(unit);
        parameter.setLowerLimit(lower);
        parameter.setUpperLimit(upper);
        
        BigDecimal variance = base.multiply(new BigDecimal("0.1"));
        BigDecimal actualValue;
        
        if (random.nextDouble() > 0.9) {
            // 超出范围的数据
            if (random.nextBoolean()) {
                actualValue = parameter.getLowerLimit().subtract(variance);
            } else {
                actualValue = parameter.getUpperLimit().add(variance);
            }
        } else {
            // 正常范围内波动
            actualValue = base.add(variance.multiply(new BigDecimal(random.nextDouble() * 2 - 1)));
        }
        
        parameter.setParameterValue(actualValue.setScale(2, BigDecimal.ROUND_HALF_UP));
        parameter.setRecordTime(record.getStartTime().plusMinutes(random.nextInt(120)));
        parameter.checkValueStatus();
        
        processParameterService.save(parameter);
    }

    /**
     * 生成设备数据记录测试数据
     */
    private void generateEquipmentRecords() {
        List<Equipment> equipments = equipmentService.findAll();
        List<ProductionPlan> plans = productionPlanService.findAll();

        int recordCount = 0;
        for (Equipment equipment : equipments) {
            for (int i = 0; i < 5; i++) {
                EquipmentRecord record = new EquipmentRecord();
                record.setEquipment(equipment);
                
                // 随机关联生产计划
                if (!plans.isEmpty() && random.nextDouble() > 0.5) {
                    record.setProductionPlan(plans.get(random.nextInt(plans.size())));
                }
                
                record.setDataSource("AUTO");
                
                String status = equipment.getStatus();
                if ("RUNNING".equals(status)) {
                    record.setRunStatus("RUNNING");
                    record.setCurrentSpeed(new BigDecimal(100 + random.nextInt(50)));
                    record.setProductionOutput(new BigDecimal(random.nextInt(1000)));
                    record.setRunDuration(new BigDecimal(random.nextInt(480)));
                    record.setTemperature(new BigDecimal(35 + random.nextDouble() * 20).setScale(2, BigDecimal.ROUND_HALF_UP));
                    record.setPressure(new BigDecimal(0.2 + random.nextDouble() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else if ("FAULT".equals(status)) {
                    record.setRunStatus("FAULT");
                    record.setAlarmMessage("设备故障告警：电机过载");
                } else if ("MAINTENANCE".equals(status)) {
                    record.setRunStatus("IDLE");
                } else {
                    record.setRunStatus("IDLE");
                }
                
                record.setRecordTime(LocalDateTime.now().minusMinutes(random.nextInt(1440)));
                
                equipmentRecordService.save(record);
                recordCount++;
            }
        }
        System.out.println("  - 已生成 " + recordCount + " 条设备数据记录");
    }
}
