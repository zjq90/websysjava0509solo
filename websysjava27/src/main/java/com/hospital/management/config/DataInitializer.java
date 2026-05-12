package com.hospital.management.config;

import com.hospital.management.entity.CostBenefit;
import com.hospital.management.entity.CustomReport;
import com.hospital.management.entity.MedicalQuality;
import com.hospital.management.entity.OperationMetrics;
import com.hospital.management.repository.CostBenefitRepository;
import com.hospital.management.repository.CustomReportRepository;
import com.hospital.management.repository.MedicalQualityRepository;
import com.hospital.management.repository.OperationMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据初始化器
 * 应用启动时生成测试数据
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OperationMetricsRepository operationMetricsRepository;

    @Autowired
    private MedicalQualityRepository medicalQualityRepository;

    @Autowired
    private CostBenefitRepository costBenefitRepository;

    @Autowired
    private CustomReportRepository customReportRepository;

    private static final String[] DEPARTMENTS = {
            "内科", "外科", "妇产科", "儿科", "眼科", "口腔科",
            "皮肤科", "肿瘤科", "骨科", "心血管内科", "神经内科", "急诊科"
    };

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        initOperationMetrics();
        initMedicalQuality();
        initCostBenefit();
        initCustomReports();
    }

    /**
     * 初始化运营指标数据
     */
    private void initOperationMetrics() {
        List<OperationMetrics> metricsList = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusDays(30);

        for (int day = 0; day < 30; day++) {
            LocalDate statDate = startDate.plusDays(day);
            for (int i = 0; i < DEPARTMENTS.length; i++) {
                OperationMetrics metrics = new OperationMetrics();
                metrics.setStatDate(statDate);
                metrics.setDepartmentId((long) (i + 1));
                metrics.setDepartmentName(DEPARTMENTS[i]);
                metrics.setOutpatientCount(100 + random.nextInt(200));
                metrics.setInpatientCount(20 + random.nextInt(50));
                metrics.setSurgeryCount(5 + random.nextInt(20));
                metrics.setTotalBeds(50 + random.nextInt(50));
                metrics.setUsedBeds(40 + random.nextInt(30));
                metrics.setBedUsageRate(70.0 + random.nextDouble() * 25.0);
                metrics.setAvgHospitalizationDays(5.0 + random.nextDouble() * 10.0);
                
                double medicineIncome = 50000 + random.nextDouble() * 100000;
                double consumableIncome = 20000 + random.nextDouble() * 50000;
                double totalIncome = medicineIncome + consumableIncome + 30000 + random.nextDouble() * 50000;
                
                metrics.setMedicineIncome(medicineIncome);
                metrics.setConsumableIncome(consumableIncome);
                metrics.setTotalIncome(totalIncome);
                metrics.setMedicineRatio((medicineIncome / totalIncome) * 100);
                metrics.setConsumableRatio((consumableIncome / totalIncome) * 100);
                
                metricsList.add(metrics);
            }
        }
        
        operationMetricsRepository.saveAll(metricsList);
        System.out.println("运营指标测试数据初始化完成，共" + metricsList.size() + "条记录");
    }

    /**
     * 初始化医疗质量数据
     */
    private void initMedicalQuality() {
        List<MedicalQuality> qualityList = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusDays(30);

        for (int day = 0; day < 30; day++) {
            LocalDate statDate = startDate.plusDays(day);
            for (int i = 0; i < DEPARTMENTS.length; i++) {
                MedicalQuality quality = new MedicalQuality();
                quality.setStatDate(statDate);
                quality.setDepartmentId((long) (i + 1));
                quality.setDepartmentName(DEPARTMENTS[i]);
                
                int totalRecords = 50 + random.nextInt(100);
                int gradeA = (int) (totalRecords * (0.85 + random.nextDouble() * 0.1));
                int gradeB = (int) (totalRecords * (0.05 + random.nextDouble() * 0.05));
                int gradeC = totalRecords - gradeA - gradeB;
                
                quality.setTotalRecords(totalRecords);
                quality.setGradeARecords(gradeA);
                quality.setGradeBRecords(gradeB);
                quality.setGradeCRecords(gradeC);
                quality.setRecordQualificationRate(((double) (gradeA + gradeB) / totalRecords) * 100);
                
                int totalPrescriptions = 200 + random.nextInt(300);
                int unreasonablePrescriptions = random.nextInt(20);
                quality.setTotalPrescriptions(totalPrescriptions);
                quality.setUnreasonablePrescriptions(unreasonablePrescriptions);
                quality.setRationalDrugUseRate(((double) (totalPrescriptions - unreasonablePrescriptions) / totalPrescriptions) * 100);
                quality.setAntibioticUseIntensity(30.0 + random.nextDouble() * 40.0);
                
                int nosocomialInfections = random.nextInt(5);
                quality.setNosocomialInfectionCount(nosocomialInfections);
                quality.setNosocomialInfectionRate(((double) nosocomialInfections / (20 + random.nextInt(30))) * 100);
                
                quality.setAdverseEventCount(random.nextInt(3));
                quality.setSeriousAdverseEventCount(random.nextInt(1));
                quality.setAdverseEventReportRate(80.0 + random.nextDouble() * 20.0);
                
                qualityList.add(quality);
            }
        }
        
        medicalQualityRepository.saveAll(qualityList);
        System.out.println("医疗质量测试数据初始化完成，共" + qualityList.size() + "条记录");
    }

    /**
     * 初始化成本效益数据
     */
    private void initCostBenefit() {
        List<CostBenefit> benefitList = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusDays(30);

        for (int day = 0; day < 30; day++) {
            LocalDate statDate = startDate.plusDays(day);
            for (int i = 0; i < DEPARTMENTS.length; i++) {
                CostBenefit benefit = new CostBenefit();
                benefit.setStatDate(statDate);
                benefit.setDepartmentId((long) (i + 1));
                benefit.setDepartmentName(DEPARTMENTS[i]);
                
                double laborCost = 30000 + random.nextDouble() * 50000;
                double medicineCost = 20000 + random.nextDouble() * 40000;
                double consumableCost = 10000 + random.nextDouble() * 30000;
                double equipmentDepreciation = 5000 + random.nextDouble() * 15000;
                double utilityCost = 3000 + random.nextDouble() * 5000;
                double otherCost = 2000 + random.nextDouble() * 8000;
                double totalCost = laborCost + medicineCost + consumableCost + equipmentDepreciation + utilityCost + otherCost;
                
                benefit.setLaborCost(laborCost);
                benefit.setMedicineCost(medicineCost);
                benefit.setConsumableCost(consumableCost);
                benefit.setEquipmentDepreciation(equipmentDepreciation);
                benefit.setUtilityCost(utilityCost);
                benefit.setOtherCost(otherCost);
                benefit.setTotalCost(totalCost);
                
                double medicalIncome = 40000 + random.nextDouble() * 80000;
                double medicineIncome = 30000 + random.nextDouble() * 60000;
                double examinationIncome = 20000 + random.nextDouble() * 40000;
                double otherIncome = 10000 + random.nextDouble() * 20000;
                double totalIncome = medicalIncome + medicineIncome + examinationIncome + otherIncome;
                
                benefit.setMedicalIncome(medicalIncome);
                benefit.setMedicineIncome(medicineIncome);
                benefit.setExaminationIncome(examinationIncome);
                benefit.setOtherIncome(otherIncome);
                benefit.setTotalIncome(totalIncome);
                
                double profit = totalIncome - totalCost;
                benefit.setProfit(profit);
                benefit.setProfitMargin((profit / totalIncome) * 100);
                benefit.setCostBenefitRatio((totalIncome / totalCost) * 100);
                benefit.setIncomePerCapita(totalIncome / (5 + random.nextInt(15)));
                
                benefitList.add(benefit);
            }
        }
        
        costBenefitRepository.saveAll(benefitList);
        System.out.println("成本效益测试数据初始化完成，共" + benefitList.size() + "条记录");
    }

    /**
     * 初始化自定义报表数据
     */
    private void initCustomReports() {
        List<CustomReport> reportList = new ArrayList<>();

        CustomReport report1 = new CustomReport();
        report1.setReportName("门诊量趋势分析报表");
        report1.setReportType("OPERATION");
        report1.setDescription("统计各科室门诊量的月度趋势分析");
        report1.setDimensions("[\"日期\",\"科室\"]");
        report1.setMetrics("[\"门诊量\"]");
        report1.setFilters("{}");
        report1.setChartType("LINE");
        report1.setCreator("系统管理员");
        report1.setEnabled(true);
        report1.setIsSystem(true);
        reportList.add(report1);

        CustomReport report2 = new CustomReport();
        report2.setReportName("科室收入分析报表");
        report2.setReportType("COST");
        report2.setDescription("各科室收入、成本、利润的对比分析");
        report2.setDimensions("[\"科室\"]");
        report2.setMetrics("[\"总收入\",\"总成本\",\"利润\",\"利润率\"]");
        report2.setFilters("{}");
        report2.setChartType("BAR");
        report2.setCreator("系统管理员");
        report2.setEnabled(true);
        report2.setIsSystem(true);
        reportList.add(report2);

        CustomReport report3 = new CustomReport();
        report3.setReportName("病历质量分析报表");
        report3.setReportType("QUALITY");
        report3.setDescription("各科室病历质量统计分析");
        report3.setDimensions("[\"科室\"]");
        report3.setMetrics("[\"甲级病历数\",\"乙级病历数\",\"丙级病历数\",\"合格率\"]");
        report3.setFilters("{}");
        report3.setChartType("PIE");
        report3.setCreator("系统管理员");
        report3.setEnabled(true);
        report3.setIsSystem(true);
        reportList.add(report3);

        CustomReport report4 = new CustomReport();
        report4.setReportName("床位使用率分析报表");
        report4.setReportType("OPERATION");
        report4.setDescription("各科室床位使用率统计分析");
        report4.setDimensions("[\"科室\",\"日期\"]");
        report4.setMetrics("[\"床位总数\",\"使用床位\",\"床位使用率\"]");
        report4.setFilters("{}");
        report4.setChartType("TABLE");
        report4.setCreator("系统管理员");
        report4.setEnabled(true);
        report4.setIsSystem(true);
        reportList.add(report4);

        customReportRepository.saveAll(reportList);
        System.out.println("自定义报表测试数据初始化完成，共" + reportList.size() + "条记录");
    }
}
