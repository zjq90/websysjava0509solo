package com.petclinic.config;

import com.petclinic.entity.*;
import com.petclinic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化器
 * 系统启动时自动生成测试数据
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmergencySymptomRepository emergencySymptomRepository;

    @Autowired
    private MedicineCategoryRepository medicineCategoryRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private ReviewRuleRepository reviewRuleRepository;

    @Autowired
    private SmartDeviceRepository smartDeviceRepository;

    @Autowired
    private ConsultationRecordRepository consultationRecordRepository;

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Override
    public void run(String... args) throws Exception {
        initEmergencySymptoms();
        initMedicineCategories();
        initMedicines();
        initReviewRules();
        initSmartDevices();
        initConsultationRecords();
        initInsuranceCompanies();
    }

    private void initEmergencySymptoms() {
        if (emergencySymptomRepository.count() > 0) {
            return;
        }

        String[] symptoms = {
                "宠物突发抽搐,四肢僵硬,意识丧失,可能是癫痫发作",
                "宠物严重呕吐腹泻,精神萎靡,可能是急性肠胃炎或中毒",
                "宠物呼吸困难,张口呼吸,舌头发紫,可能是心肺疾病",
                "宠物无法站立,步态不稳,可能是神经或骨骼问题",
                "宠物大量出血,伤口无法止血,需要紧急处理",
                "宠物误食异物,表现为频繁干呕、流口水",
                "宠物急性过敏,面部肿胀、皮肤发红发痒",
                "宠物眼球突出、受伤,需要紧急眼科处理"
        };

        String[] measures = {
                "保持宠物安静,避免刺激,立即就医,不要强行喂食",
                "禁食禁水,记录呕吐物/粪便情况,立即就医检查",
                "保持呼吸通畅,避免剧烈运动,立即吸氧并就医",
                "避免移动宠物,防止二次伤害,立即拍片检查",
                "用干净纱布按压止血,抬高伤肢,立即就医处理",
                "不要催吐,立即就医X光检查,必要时手术取出",
                "立即注射抗过敏药物,观察呼吸情况,严重就医",
                "不要触碰眼球,用湿纱布覆盖,立即找专科医生"
        };

        String[] severities = {"CRITICAL", "HIGH", "CRITICAL", "HIGH", "CRITICAL", "HIGH", "MEDIUM", "HIGH"};
        String[] petTypes = {"通用", "通用", "通用", "通用", "通用", "狗", "通用", "通用"};

        for (int i = 0; i < symptoms.length; i++) {
            EmergencySymptom symptom = new EmergencySymptom();
            symptom.setSymptomName("紧急症状" + (i + 1));
            symptom.setDescription(symptoms[i]);
            symptom.setEmergencyMeasure(measures[i]);
            symptom.setSeverityLevel(severities[i]);
            symptom.setPetType(petTypes[i]);
            symptom.setRevisedBy("张兽医");
            symptom.setRevisedTime(LocalDateTime.now());
            symptom.setStatus("ACTIVE");
            emergencySymptomRepository.save(symptom);
        }
    }

    private void initMedicineCategories() {
        if (medicineCategoryRepository.count() > 0) {
            return;
        }

        String[] categories = {"感冒药", "肠胃药", "消炎药", "皮肤病药", "驱虫药", "营养补充剂"};
        String[] descriptions = {
                "用于治疗宠物感冒、发烧等症状",
                "用于治疗宠物肠胃道疾病",
                "用于各种感染性疾病的消炎治疗",
                "用于治疗宠物各种皮肤疾病",
                "用于体内外寄生虫的驱除",
                "用于宠物日常营养补充"
        };

        for (int i = 0; i < categories.length; i++) {
            MedicineCategory category = new MedicineCategory();
            category.setCategoryName(categories[i]);
            category.setDescription(descriptions[i]);
            category.setSortOrder(i + 1);
            category.setStatus("ACTIVE");
            medicineCategoryRepository.save(category);
        }
    }

    private void initMedicines() {
        if (medicineRepository.count() > 0) {
            return;
        }

        String[][] medicines = {
                {"宠物感冒灵", "1", "用于治疗宠物感冒、咳嗽、流鼻涕", "98.00", "100"},
                {"肠胃宝", "2", "调理宠物肠胃,治疗腹泻、消化不良", "68.00", "200"},
                {"速诺消炎片", "3", "广谱抗生素,用于各种细菌感染", "128.00", "150"},
                {"皮特芬喷剂", "4", "治疗真菌性皮肤病、猫藓", "58.00", "300"},
                {"拜宠清驱虫药", "5", "体内驱虫,驱除蛔虫、绦虫", "45.00", "500"},
                {"维生素营养膏", "6", "补充多种维生素,增强免疫力", "88.00", "250"}
        };

        for (String[] med : medicines) {
            Medicine medicine = new Medicine();
            medicine.setMedicineName(med[0]);
            medicine.setCategoryId(Long.parseLong(med[1]));
            medicine.setCategoryName(medicineCategoryRepository.findById(Long.parseLong(med[1])).get().getCategoryName());
            medicine.setDescription(med[2]);
            medicine.setManufacturer("宠物制药厂");
            medicine.setPrice(new BigDecimal(med[3]));
            medicine.setStock(Integer.parseInt(med[4]));
            medicine.setPetType("通用");
            medicine.setStatus("ACTIVE");
            medicineRepository.save(medicine);
        }
    }

    private void initReviewRules() {
        if (reviewRuleRepository.count() > 0) {
            return;
        }

        ReviewRule rule1 = new ReviewRule();
        rule1.setRuleName("默认敏感词列表");
        rule1.setRuleType("SENSITIVE_WORD");
        rule1.setRuleContent("[\"违禁药\", \"处方\", \"违法\", \"走私\", \"假\"]");
        rule1.setDescription("系统默认的敏感词过滤列表");
        rule1.setEnabled(true);
        rule1.setConfiguredBy("System");
        rule1.setConfiguredTime(LocalDateTime.now());
        reviewRuleRepository.save(rule1);

        ReviewRule rule2 = new ReviewRule();
        rule2.setRuleName("内容审核规则");
        rule2.setRuleType("CONTENT_AUDIT");
        rule2.setRuleContent("{\"maxLength\": 5000, \"forbiddenImages\": true, \"needReview\": true}");
        rule2.setDescription("用户发布内容的审核规则");
        rule2.setEnabled(true);
        rule2.setConfiguredBy("System");
        rule2.setConfiguredTime(LocalDateTime.now());
        reviewRuleRepository.save(rule2);
    }

    private void initSmartDevices() {
        if (smartDeviceRepository.count() > 0) {
            return;
        }

        String[][] devices = {
                {"智能喂食器-A1", "FEEDER", "FEEDER001", "小白", "客厅"},
                {"智能饮水机-B1", "WATER_DISPENSER", "WATER001", "小白", "客厅"},
                {"智能喂食器-A2", "FEEDER", "FEEDER002", "大黄", "阳台"},
                {"智能饮水机-B2", "WATER_DISPENSER", "WATER002", "大黄", "阳台"}
        };

        for (String[] dev : devices) {
            SmartDevice device = new SmartDevice();
            device.setDeviceName(dev[0]);
            device.setDeviceType(dev[1]);
            device.setDeviceCode(dev[2]);
            device.setPetName(dev[3]);
            device.setLocation(dev[4]);
            device.setStatus("ONLINE");
            device.setLastOnlineTime(LocalDateTime.now().toString());
            smartDeviceRepository.save(device);
        }
    }

    private void initConsultationRecords() {
        if (consultationRecordRepository.count() > 0) {
            return;
        }

        String[][] records = {
                {"小白", "猫", "2岁", "李明", "13800138001", "持续发烧、精神不振", "发烧40度,食欲下降",
                 "病毒性感冒", "抗病毒治疗,补充营养", "注意保暖,多喝水", "王医生", "280.00"},
                {"大黄", "狗", "3岁", "王芳", "13900139002", "呕吐腹泻", "一天呕吐3次,腹泻4次",
                 "急性肠胃炎", "消炎止吐,禁食24小时", "观察精神状态,清淡饮食", "张医生", "350.00"},
                {"花花", "猫", "1岁", "张伟", "13700137003", "皮肤瘙痒、脱毛", "背部多处脱毛,皮肤发红",
                 "真菌性皮肤病", "外用药物+药浴", "保持环境干燥,定期消毒", "李医生", "420.00"}
        };

        for (String[] rec : records) {
            ConsultationRecord record = new ConsultationRecord();
            record.setConsultationNo("CZ" + System.currentTimeMillis());
            record.setPetName(rec[0]);
            record.setPetType(rec[1]);
            record.setPetAge(rec[2]);
            record.setOwnerName(rec[3]);
            record.setOwnerPhone(rec[4]);
            record.setChiefComplaint(rec[5]);
            record.setSymptomDetail(rec[6]);
            record.setDiagnosisResult(rec[7]);
            record.setTreatmentPlan(rec[8]);
            record.setDoctorAdvice(rec[9]);
            record.setDoctorName(rec[10]);
            record.setConsultationFee(new BigDecimal(rec[11]));
            record.setStatus("COMPLETED");
            record.setClaimed(false);
            consultationRecordRepository.save(record);
        }
    }

    private void initInsuranceCompanies() {
        if (insuranceCompanyRepository.count() > 0) {
            return;
        }

        String[][] companies = {
                {"众安宠物保险", "ZACN", "https://api.zhongan.com/pet", "70.00", "100.00", "5000.00", "覆盖宠物疾病、意外事故、住院医疗费用报销"},
                {"平安宠物保险", "PAIC", "https://api.pingan.com/pet", "80.00", "200.00", "10000.00", "覆盖宠物疾病、疫苗、绝育等，保额高"},
                {"太保宠物保险", "CPIC", "https://api.cpic.com/pet", "75.00", "150.00", "8000.00", "覆盖宠物意外医疗、第三方责任险"}
        };

        for (String[] company : companies) {
            InsuranceCompany insuranceCompany = new InsuranceCompany();
            insuranceCompany.setCompanyName(company[0]);
            insuranceCompany.setCompanyCode(company[1]);
            insuranceCompany.setApiUrl(company[2]);
            insuranceCompany.setDefaultClaimRate(new BigDecimal(company[3]));
            insuranceCompany.setDeductible(new BigDecimal(company[4]));
            insuranceCompany.setMaxClaimAmount(new BigDecimal(company[5]));
            insuranceCompany.setCoverageDescription(company[6]);
            insuranceCompany.setContactPerson("客服专员");
            insuranceCompany.setContactPhone("400-888-8888");
            insuranceCompany.setAddress("北京市朝阳区金融街");
            insuranceCompany.setStatus("ACTIVE");
            insuranceCompany.setAutoClaimEnabled(true);
            insuranceCompanyRepository.save(insuranceCompany);
        }
    }
}
