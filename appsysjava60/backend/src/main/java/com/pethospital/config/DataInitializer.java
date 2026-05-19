package com.pethospital.config;

import com.pethospital.entity.*;
import com.pethospital.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 数据初始化类
 * 系统启动时自动生成测试数据
 * 
 * @author Pet Hospital Team
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DiseaseRepository diseaseRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private CaseRepository caseRepository;
    
    @Autowired
    private ConsultationRepository consultationRepository;
    
    private final Random random = new Random();
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化测试数据...");
        
        initDoctors();
        initDiseases();
        initMedicines();
        initCases();
        initConsultations();
        
        log.info("测试数据初始化完成！");
    }
    
    /**
     * 初始化医生数据
     */
    private void initDoctors() {
        if (doctorRepository.count() > 0) return;
        
        String[][] doctors = {
            {"张医生", "13800138001", "主治医师", "内科、传染病"},
            {"李医生", "13800138002", "副主任医师", "外科、骨科"},
            {"王医生", "13800138003", "主治医师", "皮肤科、眼科"},
            {"赵医生", "13800138004", "执业医师", "预防医学、疫苗接种"}
        };
        
        for (String[] doc : doctors) {
            Doctor doctor = new Doctor();
            doctor.setName(doc[0]);
            doctor.setPhone(doc[1]);
            doctor.setPassword("123456");
            doctor.setTitle(doc[2]);
            doctor.setSpecialty(doc[3]);
            doctor.setStatus(1);
            doctor.setCreateTime(LocalDateTime.now());
            doctor.setUpdateTime(LocalDateTime.now());
            doctorRepository.save(doctor);
        }
        log.info("医生数据初始化完成，共4条");
    }
    
    /**
     * 初始化疾病数据
     */
    private void initDiseases() {
        if (diseaseRepository.count() > 0) return;
        
        Object[][] diseases = {
            {"猫瘟热", "猫", "发热、呕吐、腹泻、精神萎靡、白细胞减少", 
             "1. 抗病毒治疗：使用猫干扰素；2. 对症治疗：止吐、止泻、补液；3. 抗生素预防继发感染；4. 营养支持", 
             "细小病毒感染", "接种疫苗、定期消毒、避免接触病猫", 3, "猫瘟,呕吐,腹泻,发热,白细胞减少"},
            {"猫鼻支", "猫", "打喷嚏、流鼻涕、流眼泪、结膜炎、发热", 
             "1. 抗病毒治疗：猫用干扰素；2. 抗生素治疗：预防细菌感染；3. 眼部护理：眼药水；4. 支持治疗：补液、营养", 
             "猫疱疹病毒感染", "接种疫苗、保持环境清洁、通风", 2, "猫鼻支,打喷嚏,流鼻涕,结膜炎,流泪"},
            {"猫传腹", "猫", "腹水、黄疸、发热、体重下降、精神萎靡", 
             "1. 441/GS-376抗病毒治疗；2. 保肝护肾治疗；3. 抗炎治疗；4. 营养支持治疗", 
             "猫冠状病毒变异", "减少应激、定期体检、早期发现", 3, "猫传腹,腹水,黄疸,发热,体重下降"},
            {"犬细小病毒病", "狗", "剧烈呕吐、血便、发热、脱水、精神沉郁", 
             "1. 抗病毒治疗：犬用干扰素、单抗；2. 止吐止泻；3. 补液纠正脱水；4. 抗生素预防继发感染；5. 营养支持", 
             "细小病毒感染", "按时接种疫苗、避免接触病犬、定期消毒", 3, "犬细小,呕吐,血便,发热,脱水"},
            {"犬瘟热", "狗", "双相热、呼吸道症状、消化道症状、神经症状", 
             "1. 抗病毒治疗：单抗、干扰素；2. 控制继发感染；3. 对症治疗：止咳、止吐、抗惊厥；4. 支持治疗", 
             "犬瘟热病毒感染", "按时接种疫苗、避免接触病犬、加强护理", 3, "犬瘟热,发热,咳嗽,呕吐,神经症状"},
            {"皮肤真菌病", "通用", "圆形脱毛、皮屑、瘙痒、红斑、结痂", 
             "1. 外用抗真菌药：药浴、软膏；2. 口服抗真菌药；3. 环境消毒；4. 增强营养", 
             "犬小孢子菌、须毛癣菌感染", "保持皮肤清洁干燥、定期驱虫、环境消毒", 1, "真菌,脱毛,皮屑,瘙痒,皮肤病"},
            {"耳螨病", "通用", "耳朵瘙痒、摇头、抓耳、耳道有黑褐色分泌物", 
             "1. 清洁耳道；2. 使用耳螨药物：滴耳液；3. 体外驱虫；4. 必要时抗生素治疗继发感染", 
             "耳螨感染", "定期体外驱虫、保持耳道清洁", 1, "耳螨,耳朵瘙痒,摇头,耳部分泌物"},
            {"胃肠炎", "通用", "呕吐、腹泻、腹痛、食欲下降、精神不振", 
             "1. 禁食禁水24小时；2. 止吐止泻药物；3. 益生菌调理肠胃；4. 严重时输液治疗", 
             "饮食不当、细菌病毒感染、寄生虫", "合理饮食、定期驱虫、避免误食异物", 2, "胃肠炎,呕吐,腹泻,腹痛,消化不良"}
        };
        
        for (Object[] dis : diseases) {
            Disease disease = new Disease();
            disease.setName((String) dis[0]);
            disease.setPetType((String) dis[1]);
            disease.setSymptoms((String) dis[2]);
            disease.setTreatment((String) dis[3]);
            disease.setCause((String) dis[4]);
            disease.setPrevention((String) dis[5]);
            disease.setSeverity((Integer) dis[6]);
            disease.setKeywords((String) dis[7]);
            disease.setStatus(1);
            disease.setVersion(1);
            disease.setCreateTime(LocalDateTime.now());
            disease.setUpdateTime(LocalDateTime.now());
            diseaseRepository.save(disease);
        }
        log.info("疾病数据初始化完成，共8条");
    }
    
    /**
     * 初始化药品数据
     */
    private void initMedicines() {
        if (medicineRepository.count() > 0) return;
        
        Object[][] medicines = {
            {"猫用干扰素", "干扰素", "用于猫瘟热、猫鼻支等病毒性疾病的治疗", 
             "皮下注射，每次100-200万单位，每日1次，连用3-5天", 
             "注射部位可能有轻微红肿，过敏反应少见", "对干扰素过敏者禁用", "2-8℃冷藏保存", "2ml:100万单位", "某生物制药公司", "干扰素,抗病毒,猫瘟,猫鼻支"},
            {"犬细小单抗", "单抗", "用于犬细小病毒病的特异性治疗", 
             "静脉滴注或皮下注射，每公斤体重1-2ml，每日1次，连用3-5天", 
             "少数犬可能出现过敏反应", "对本品过敏者禁用，孕犬慎用", "2-8℃冷藏保存", "5ml/支", "某生物制药公司", "细小,单抗,抗病毒,犬细小"},
            {"头孢噻呋钠", "抗生素", "用于敏感菌引起的呼吸道、消化道、泌尿生殖道感染", 
             "肌肉注射，每公斤体重5mg，每日1次，连用3-5天", 
             "可能出现胃肠道反应、过敏反应", "对头孢类过敏者禁用，肾功能不全者慎用", "阴凉干燥处保存", "0.5g/瓶", "某制药公司", "头孢,抗生素,消炎,感染"},
            {"奥美拉唑", "消化系统", "用于胃炎、胃溃疡、反流性食管炎等", 
             "口服，每公斤体重0.5-1mg，每日1次，饭前服用", 
             "长期使用可能影响食欲", "肝肾功能不全者慎用", "阴凉干燥处保存", "20mg/片", "某制药公司", "奥美拉唑,胃炎,胃溃疡,止吐"},
            {"伊曲康唑", "抗真菌药", "用于皮肤真菌病、猫癣等真菌感染", 
             "口服，每公斤体重5-10mg，每日1次，连用4-8周", 
             "可能出现胃肠道反应，长期使用需监测肝功能", 
             "肝功能不全者禁用，孕猫禁用", "阴凉干燥处保存", "100mg/粒", "某制药公司", "伊曲康唑,真菌,猫癣,皮肤病"},
            {"体内外驱虫药", "驱虫药", "用于驱除犬猫体内外寄生虫", 
             "体外滴颈背部，体内口服。每月1次", 
             "少数可能出现短暂流涎、呕吐", 
             "幼龄宠物按体重选择剂量", "阴凉干燥处保存", "1ml/支", "某兽药公司", "驱虫,体内外,寄生虫,跳蚤,蜱虫"}
        };
        
        for (Object[] med : medicines) {
            Medicine medicine = new Medicine();
            medicine.setName((String) med[0]);
            medicine.setCategory((String) med[1]);
            medicine.setIndication((String) med[2]);
            medicine.setDosage((String) med[3]);
            medicine.setAdverseReaction((String) med[4]);
            medicine.setContraindication((String) med[5]);
            medicine.setAttention((String) med[6]);
            medicine.setSpecification((String) med[7]);
            medicine.setManufacturer((String) med[8]);
            medicine.setKeywords((String) med[9]);
            medicine.setStatus(1);
            medicine.setCreateTime(LocalDateTime.now());
            medicine.setUpdateTime(LocalDateTime.now());
            medicineRepository.save(medicine);
        }
        log.info("药品数据初始化完成，共6条");
    }
    
    /**
     * 初始化案例数据
     */
    private void initCases() {
        if (caseRepository.count() > 0) return;
        
        Object[][] cases = {
            {"2岁英短猫瘟热治疗案例", "猫", "英短", "2岁", "公", 
             "主诉猫咪近2天持续呕吐，腹泻，精神很差，不吃东西", 
             "体温40.2℃，血常规白细胞总数2.8×10^9/L，猫瘟抗原检测阳性", 
             "猫瘟热", 
             "1. 猫用干扰素200万单位皮下注射，每日1次；2. 头孢噻呋钠抗感染；3. 静脉补液纠正脱水；4. 止吐、止泻对症治疗；5. 高蛋白营养膏支持", 
             "治疗5天后症状明显改善，7天后痊愈出院，建议恢复期少量多餐，补充营养", 
             1L, "张医生", "猫瘟,呕吐,腹泻,英短,白细胞减少"},
            {"3岁金毛犬细小病毒病治疗案例", "狗", "金毛", "3岁", "公", 
             "主诉狗狗昨天开始呕吐，今天拉血便，精神很差", 
             "体温39.8℃，血常规白细胞总数3.2×10^9/L，犬细小抗原检测阳性", 
             "犬细小病毒病", 
             "1. 犬细小单抗静脉滴注；2. 干扰素抗病毒；3. 头孢噻呋钠预防继发感染；4. 静脉补液、止血；5. 禁食禁水，肠外营养支持", 
             "治疗6天后血便停止，10天后痊愈出院，建议恢复期易消化饮食", 
             1L, "张医生", "细小,金毛,血便,呕吐,脱水"},
            {"1岁布偶猫猫癣治疗案例", "猫", "布偶", "1岁", "母", 
             "主诉猫咪身上多处脱毛，有皮屑，经常抓挠", 
             "伍德氏灯检查阳性，皮肤刮片镜检发现真菌孢子", 
             "皮肤真菌病（猫癣）", 
             "1. 伊曲康唑口服，每日1次；2. 抗真菌药浴，每周2次；3. 环境消毒；4. 补充维生素B", 
             "治疗4周后症状消失，继续巩固2周，完全治愈，建议定期环境消毒", 
             3L, "王医生", "猫癣,真菌,布偶,脱毛,皮屑"},
            {"5岁泰迪犬耳螨治疗案例", "狗", "泰迪", "5岁", "母", 
             "主诉狗狗经常摇头、抓耳朵，耳道有很多黑褐色分泌物", 
             "耳道分泌物镜检发现大量耳螨成虫和虫卵", 
             "耳螨病", 
             "1. 宠物专用洗耳液清洁耳道；2. 耳螨滴耳液，每日2次；3. 体外驱虫；4. 同时使用杀耳螨喷剂环境消毒", 
             "治疗2周后复查，耳螨已清除，耳道恢复正常，建议定期驱虫清洁", 
             3L, "王医生", "耳螨,泰迪,耳朵瘙痒,摇头,耳部分泌物"}
        };
        
        for (Object[] c : cases) {
            Case caseObj = new Case();
            caseObj.setTitle((String) c[0]);
            caseObj.setPetType((String) c[1]);
            caseObj.setBreed((String) c[2]);
            caseObj.setAge((String) c[3]);
            caseObj.setGender((String) c[4]);
            caseObj.setChiefComplaint((String) c[5]);
            caseObj.setClinicalExamination((String) c[6]);
            caseObj.setDiagnosis((String) c[7]);
            caseObj.setTreatment((String) c[8]);
            caseObj.setTreatmentEffect((String) c[9]);
            caseObj.setDoctorId((Long) c[10]);
            caseObj.setDoctorName((String) c[11]);
            caseObj.setKeywords((String) c[12]);
            caseObj.setViewCount(random.nextInt(100));
            caseObj.setStatus(1);
            caseObj.setCreateTime(LocalDateTime.now());
            caseObj.setUpdateTime(LocalDateTime.now());
            caseRepository.save(caseObj);
        }
        log.info("案例数据初始化完成，共4条");
    }
    
    /**
     * 初始化接诊记录数据
     */
    private void initConsultations() {
        if (consultationRepository.count() > 0) return;
        
        String[] petTypes = {"猫", "狗"};
        String[] catBreeds = {"英短", "美短", "布偶", "蓝猫", "橘猫"};
        String[] dogBreeds = {"金毛", "泰迪", "柯基", "拉布拉多", "哈士奇"};
        String[] ownerNames = {"王先生", "李女士", "张先生", "刘女士", "陈先生", "杨女士"};
        String[] chiefComplaints = {
            "呕吐、腹泻", "食欲不振", "精神萎靡", "发烧", "咳嗽、打喷嚏",
            "皮肤瘙痒、脱毛", "耳朵瘙痒、分泌物多", "拉稀、便血", "呕吐黄水", "不愿活动"
        };
        
        // 生成30天的接诊记录，每天3-8个
        for (int day = 0; day < 30; day++) {
            int countToday = 3 + random.nextInt(6);
            for (int i = 0; i < countToday; i++) {
                Consultation consultation = new Consultation();
                consultation.setDoctorId((long) (1 + random.nextInt(4)));
                
                String petType = petTypes[random.nextInt(2)];
                consultation.setPetType(petType);
                
                String[] breeds = "猫".equals(petType) ? catBreeds : dogBreeds;
                consultation.setBreed(breeds[random.nextInt(breeds.length)]);
                
                consultation.setPetName("宠物" + (day * 10 + i));
                consultation.setOwnerName(ownerNames[random.nextInt(ownerNames.length)]);
                consultation.setOwnerPhone("138" + String.format("%08d", random.nextInt(100000000)));
                consultation.setChiefComplaint(chiefComplaints[random.nextInt(chiefComplaints.length)]);
                
                // 随机分配疾病ID（1-8）
                int diseaseCount = 1 + random.nextInt(2);
                StringBuilder diagnosisIds = new StringBuilder();
                for (int j = 0; j < diseaseCount; j++) {
                    if (j > 0) diagnosisIds.append(",");
                    diagnosisIds.append(1 + random.nextInt(8));
                }
                consultation.setDiagnosisIds(diagnosisIds.toString());
                consultation.setDiagnosisDesc("初步诊断结果");
                
                // 随机分配药品ID（1-6）
                int medicineCount = 1 + random.nextInt(3);
                StringBuilder medicineIds = new StringBuilder();
                for (int j = 0; j < medicineCount; j++) {
                    if (j > 0) medicineIds.append(",");
                    medicineIds.append(1 + random.nextInt(6));
                }
                consultation.setMedicineIds(medicineIds.toString());
                
                consultation.setCost(100 + random.nextInt(500) + random.nextDouble() * 100);
                
                LocalDateTime consultDate = LocalDateTime.now().minusDays(day).minusHours(random.nextInt(12));
                consultation.setConsultationDate(consultDate);
                consultation.setStatus(1);
                consultation.setCreateTime(consultDate);
                consultation.setUpdateTime(consultDate);
                
                consultationRepository.save(consultation);
            }
        }
        log.info("接诊记录数据初始化完成，共{}条", consultationRepository.count());
    }
}
