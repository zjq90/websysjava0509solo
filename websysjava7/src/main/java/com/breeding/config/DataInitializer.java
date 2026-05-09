package com.breeding.config;

import com.breeding.entity.*;
import com.breeding.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * 数据初始化类
 * 系统启动时自动初始化测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BreedingProjectRepository projectRepository;
    @Autowired
    private ParentPlantRepository parentRepository;
    @Autowired
    private CrossCombinationRepository combinationRepository;
    @Autowired
    private FieldExperimentRepository experimentRepository;
    @Autowired
    private WeatherDataRepository weatherRepository;
    @Autowired
    private SoilDataRepository soilRepository;

    @Override
    public void run(String... args) throws Exception {
        if (projectRepository.count() == 0) {
            initProjects();
            initParents();
            initCombinations();
            initExperiments();
            initWeatherData();
            initSoilData();
            System.out.println("========== 测试数据初始化完成 ==========");
        }
    }

    private void initProjects() {
        BreedingProject p1 = new BreedingProject();
        p1.setProjectCode("PRJ-2024-001");
        p1.setProjectName("小麦高产品种选育");
        p1.setCropType("小麦");
        p1.setBreedingGoal("选育高产、抗病、优质小麦新品种");
        p1.setResponsiblePerson("张研究员");
        p1.setContactPhone("13800138001");
        p1.setStartDate(LocalDate.of(2024, 1, 15));
        p1.setStatus("进行中");
        p1.setDescription("本项目旨在选育适应黄淮海地区的高产小麦品种");
        projectRepository.save(p1);

        BreedingProject p2 = new BreedingProject();
        p2.setProjectCode("PRJ-2024-002");
        p2.setProjectName("水稻抗逆品种选育");
        p2.setCropType("水稻");
        p2.setBreedingGoal("选育抗逆、优质水稻品种");
        p2.setResponsiblePerson("李研究员");
        p2.setContactPhone("13800138002");
        p2.setStartDate(LocalDate.of(2024, 2, 20));
        p2.setStatus("进行中");
        projectRepository.save(p2);

        BreedingProject p3 = new BreedingProject();
        p3.setProjectCode("PRJ-2023-001");
        p3.setProjectName("玉米新品种选育");
        p3.setCropType("玉米");
        p3.setBreedingGoal("选育高抗逆性玉米品种");
        p3.setResponsiblePerson("王研究员");
        p3.setStartDate(LocalDate.of(2023, 5, 10));
        p3.setStatus("已完成");
        p3.setEndDate(LocalDate.of(2024, 3, 20));
        projectRepository.save(p3);
    }

    private void initParents() {
        ParentPlant p1 = new ParentPlant();
        p1.setParentCode("PAR-001");
        p1.setVarietyName("济麦22");
        p1.setParentType("父本");
        p1.setCropType("小麦");
        p1.setOrigin("山东");
        p1.setGeneration("稳定系");
        p1.setYieldPotential(650.0);
        p1.setGrowthPeriod(235);
        p1.setDiseaseResistance("抗锈病、中抗白粉病");
        p1.setQualityTraits("蛋白质含量14.2%，湿面筋32.5%");
        p1.setNotes("骨干父本材料");
        parentRepository.save(p1);

        ParentPlant p2 = new ParentPlant();
        p2.setParentCode("PAR-002");
        p2.setVarietyName("周麦18");
        p2.setParentType("母本");
        p2.setCropType("小麦");
        p2.setOrigin("河南");
        p2.setGeneration("稳定系");
        p2.setYieldPotential(600.0);
        p2.setGrowthPeriod(232);
        p2.setDiseaseResistance("抗白粉病、中抗纹枯病");
        p2.setQualityTraits("容重795g/L，蛋白质13.8%");
        parentRepository.save(p2);

        ParentPlant p3 = new ParentPlant();
        p3.setParentCode("PAR-003");
        p3.setVarietyName("郑麦9023");
        p3.setParentType("父本");
        p3.setCropType("小麦");
        p3.setOrigin("河南");
        p3.setGeneration("稳定系");
        p3.setYieldPotential(580.0);
        p3.setGrowthPeriod(228);
        p3.setDiseaseResistance("抗条锈病");
        parentRepository.save(p3);

        ParentPlant p4 = new ParentPlant();
        p4.setParentCode("PAR-004");
        p4.setVarietyName("淮稻5号");
        p4.setParentType("母本");
        p4.setCropType("水稻");
        p4.setOrigin("江苏");
        p4.setGeneration("稳定系");
        p4.setYieldPotential(680.0);
        p4.setGrowthPeriod(145);
        p4.setDiseaseResistance("抗稻瘟病、纹枯病");
        parentRepository.save(p4);
    }

    private void initCombinations() {
        CrossCombination c1 = new CrossCombination();
        c1.setCombinationCode("COM-2024-001");
        c1.setCombinationName("济麦22×周麦18");
        c1.setCrossDate(LocalDate.of(2024, 4, 15));
        c1.setCrossMethod("人工去雄杂交");
        c1.setGeneration("F1");
        c1.setSeedCount(500);
        c1.setSeedSetRate(85.5);
        c1.setStatus("进行中");
        c1.setNotes("主要目标组合，重点观察");
        combinationRepository.save(c1);

        CrossCombination c2 = new CrossCombination();
        c2.setCombinationCode("COM-2024-002");
        c2.setCombinationName("济麦22×郑麦9023");
        c2.setCrossDate(LocalDate.of(2024, 4, 18));
        c2.setCrossMethod("人工去雄杂交");
        c2.setGeneration("F1");
        c2.setSeedCount(380);
        c2.setSeedSetRate(78.2);
        c2.setStatus("进行中");
        combinationRepository.save(c2);

        CrossCombination c3 = new CrossCombination();
        c3.setCombinationCode("COM-2023-005");
        c3.setCombinationName("济麦22×淮稻5号");
        c3.setCrossDate(LocalDate.of(2023, 8, 20));
        c3.setCrossMethod("杂交");
        c3.setGeneration("F3");
        c3.setSeedCount(2000);
        c3.setSeedSetRate(92.0);
        c3.setStatus("已完成");
        combinationRepository.save(c3);
    }

    private void initExperiments() {
        FieldExperiment e1 = new FieldExperiment();
        e1.setExperimentCode("EXP-2024-001");
        e1.setExperimentName("2024年小麦品比试验");
        e1.setLocation("河南郑州试验站");
        e1.setYear(2024);
        e1.setSowingDate(LocalDate.of(2024, 10, 10));
        e1.setHeadingDate(LocalDate.of(2024, 4, 20));
        e1.setMaturityDate(LocalDate.of(2024, 6, 5));
        e1.setPlantHeight(85.5);
        e1.setEarLength(10.2);
        e1.setThousandGrainWeight(45.8);
        e1.setYieldPerMu(620.5);
        e1.setPowderyMildewResistance("中抗");
        e1.setRustResistance("抗");
        e1.setLodgingResistance("强");
        e1.setStatus("已完成");
        e1.setNotes("品比试验表现优异");
        experimentRepository.save(e1);

        FieldExperiment e2 = new FieldExperiment();
        e2.setExperimentCode("EXP-2024-002");
        e2.setExperimentName("2024年小区试验");
        e2.setLocation("山东济南试验站");
        e2.setYear(2024);
        e2.setSowingDate(LocalDate.of(2024, 10, 15));
        e2.setPlantHeight(88.0);
        e2.setEarLength(9.8);
        e2.setThousandGrainWeight(44.5);
        e2.setYieldPerMu(595.0);
        e2.setPowderyMildewResistance("高抗");
        e2.setRustResistance("中抗");
        e2.setLodgingResistance("中");
        e2.setStatus("进行中");
        experimentRepository.save(e2);

        FieldExperiment e3 = new FieldExperiment();
        e3.setExperimentCode("EXP-2023-003");
        e3.setExperimentName("2023年生产试验");
        e3.setLocation("河南郑州试验站");
        e3.setYear(2023);
        e3.setPlantHeight(82.0);
        e3.setEarLength(10.5);
        e3.setThousandGrainWeight(46.2);
        e3.setYieldPerMu(635.0);
        e3.setPowderyMildewResistance("高抗");
        e3.setRustResistance("高抗");
        e3.setLodgingResistance("强");
        e3.setStatus("已完成");
        experimentRepository.save(e3);
    }

    private void initWeatherData() {
        LocalDate baseDate = LocalDate.of(2024, 3, 1);
        for (int i = 0; i < 30; i++) {
            WeatherData w = new WeatherData();
            w.setRecordDate(baseDate.plusDays(i));
            w.setLocation("河南郑州试验站");
            w.setAvgTemperature(15.0 + i * 0.5);
            w.setMaxTemperature(22.0 + i * 0.3);
            w.setMinTemperature(8.0 + i * 0.4);
            w.setAvgHumidity(60.0 + Math.random() * 20);
            w.setRainfall(i % 3 == 0 ? 10.0 + Math.random() * 20 : 0);
            w.setSunshineHours(4.0 + Math.random() * 6);
            w.setAvgWindSpeed(1.5 + Math.random() * 2);
            w.setDataSource("自动气象站");
            weatherRepository.save(w);
        }

        for (int i = 0; i < 15; i++) {
            WeatherData w = new WeatherData();
            w.setRecordDate(LocalDate.of(2024, 4, 1).plusDays(i));
            w.setLocation("山东济南试验站");
            w.setAvgTemperature(18.0 + i * 0.3);
            w.setMaxTemperature(25.0 + i * 0.2);
            w.setMinTemperature(12.0 + i * 0.3);
            w.setAvgHumidity(55.0 + Math.random() * 25);
            w.setRainfall(i % 2 == 0 ? 8.0 + Math.random() * 15 : 0);
            w.setSunshineHours(5.0 + Math.random() * 5);
            w.setDataSource("自动气象站");
            weatherRepository.save(w);
        }
    }

    private void initSoilData() {
        SoilData s1 = new SoilData();
        s1.setRecordDate(LocalDate.of(2024, 3, 10));
        s1.setLocation("河南郑州试验站");
        s1.setSoilType("壤土");
        s1.setSoilTexture("中壤");
        s1.setPhValue(7.2);
        s1.setOrganicMatter(18.5);
        s1.setTotalNitrogen(1.25);
        s1.setAvailablePhosphorus(25.8);
        s1.setAvailablePotassium(150.2);
        s1.setTotalPhosphorus(0.85);
        s1.setTotalPotassium(18.5);
        s1.setAlkalineHydrolyzableN(95.5);
        s1.setDataSource("实验室检测");
        soilRepository.save(s1);

        SoilData s2 = new SoilData();
        s2.setRecordDate(LocalDate.of(2024, 3, 15));
        s2.setLocation("山东济南试验站");
        s2.setSoilType("潮土");
        s2.setSoilTexture("轻壤");
        s2.setPhValue(7.8);
        s2.setOrganicMatter(15.2);
        s2.setTotalNitrogen(1.05);
        s2.setAvailablePhosphorus(20.5);
        s2.setAvailablePotassium(135.0);
        s2.setTotalPhosphorus(0.72);
        s2.setTotalPotassium(16.8);
        s2.setAlkalineHydrolyzableN(85.0);
        s2.setDataSource("实验室检测");
        soilRepository.save(s2);

        SoilData s3 = new SoilData();
        s3.setRecordDate(LocalDate.of(2024, 4, 1));
        s3.setLocation("河南郑州试验站");
        s3.setSoilType("壤土");
        s3.setSoilTexture("中壤");
        s3.setPhValue(7.1);
        s3.setOrganicMatter(19.2);
        s3.setTotalNitrogen(1.30);
        s3.setAvailablePhosphorus(28.0);
        s3.setAvailablePotassium(155.5);
        s3.setDataSource("实验室检测");
        soilRepository.save(s3);
    }
}
