package com.hospital.config;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据初始化类
 * 系统启动时自动初始化测试数据
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private HospitalizationRepository hospitalizationRepository;

    @Autowired
    private MedicalOrderRepository medicalOrderRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Override
    public void run(String... args) throws Exception {
        initStaff();
        initPatients();
        initBeds();
        initMedicines();
        initHospitalizations();
        initMedicalOrders();
        initMedicalRecords();
        initVitalSigns();
        initFeeRecords();
        
        System.out.println("========================================");
        System.out.println("  测试数据初始化完成！");
        System.out.println("========================================");
    }

    private void initStaff() {
        if (staffRepository.count() > 0) return;

        String[] roles = {"医生", "护士"};
        String[] departments = {"内科", "外科", "儿科", "妇产科", "骨科"};
        String[] doctorNames = {"张医生", "李医生", "王医生", "赵医生", "刘医生"};
        String[] nurseNames = {"王护士", "李护士", "张护士", "陈护士", "杨护士"};

        for (int i = 0; i < 5; i++) {
            Staff doctor = new Staff();
            doctor.setStaffNo("DOC" + String.format("%03d", i + 1));
            doctor.setName(doctorNames[i]);
            doctor.setGender(i % 2 == 0 ? "男" : "女");
            doctor.setAge(30 + i);
            doctor.setRole("医生");
            doctor.setDepartment(departments[i]);
            doctor.setPosition("主治医师");
            doctor.setTitle("副主任医师");
            doctor.setStatus("在职");
            doctor.setPhone("138" + String.format("%08d", i));
            staffRepository.save(doctor);
        }

        for (int i = 0; i < 5; i++) {
            Staff nurse = new Staff();
            nurse.setStaffNo("NUR" + String.format("%03d", i + 1));
            nurse.setName(nurseNames[i]);
            nurse.setGender("女");
            nurse.setAge(25 + i);
            nurse.setRole("护士");
            nurse.setDepartment(departments[i]);
            nurse.setPosition("护士");
            nurse.setTitle("护师");
            nurse.setStatus("在职");
            nurse.setPhone("139" + String.format("%08d", i));
            staffRepository.save(nurse);
        }
    }

    private void initPatients() {
        if (patientRepository.count() > 0) return;

        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] genders = {"男", "女"};
        String[] addresses = {"北京市朝阳区", "北京市海淀区", "北京市东城区", "北京市西城区"};

        for (int i = 0; i < 8; i++) {
            Patient patient = new Patient();
            patient.setPatientNo("P" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%04d", i + 1));
            patient.setName(names[i]);
            patient.setGender(genders[i % 2]);
            patient.setAge(25 + i * 5);
            patient.setIdCard("110101199" + i + "0101" + String.format("%04d", i));
            patient.setPhone("138" + String.format("%08d", 1000 + i));
            patient.setAddress(addresses[i % 4]);
            patient.setMaritalStatus(i % 2 == 0 ? "已婚" : "未婚");
            patient.setOccupation(i % 3 == 0 ? "职员" : i % 3 == 1 ? "教师" : "医生");
            patient.setEmergencyContact("联系人" + i);
            patient.setEmergencyPhone("139" + String.format("%08d", 2000 + i));
            patient.setMedicalHistory(i % 3 == 0 ? "高血压" : i % 3 == 1 ? "糖尿病" : "无");
            patient.setAllergyHistory(i % 2 == 0 ? "青霉素" : "无");
            patient.setStatus("正常");
            patientRepository.save(patient);
        }
    }

    private void initBeds() {
        if (bedRepository.count() > 0) return;

        String[] wards = {"内科一病区", "内科二病区", "外科一病区", "外科二病区", "儿科病区"};
        String[] statuses = {"空闲", "占用", "维修中"};

        int bedNo = 1;
        for (String ward : wards) {
            for (int i = 0; i < 6; i++) {
                Bed bed = new Bed();
                bed.setBedNo("BED" + String.format("%03d", bedNo++));
                bed.setWardName(ward);
                bed.setRoomNo(ward.substring(0, 2) + (i / 2 + 1) + "室");
                bed.setBedType(i % 3 == 0 ? "重症监护床" : "普通床");
                bed.setDepartment(ward.substring(0, 2));
                bed.setStatus(statuses[i % 3]);
                bed.setDailyRate(new BigDecimal(i % 3 == 0 ? "200" : "50"));
                bedRepository.save(bed);
            }
        }
    }

    private void initMedicines() {
        if (medicineRepository.count() > 0) return;

        String[][] medicines = {
            {"阿莫西林胶囊", "阿莫仙", "0.5g*24粒", "西药"},
            {"头孢克肟分散片", "世福素", "0.1g*6片", "西药"},
            {"布洛芬缓释胶囊", "芬必得", "0.3g*20粒", "西药"},
            {"奥美拉唑肠溶胶囊", "洛赛克", "20mg*14粒", "西药"},
            {"复方氨酚烷胺片", "感康", "12片/盒", "西药"},
            {"板蓝根颗粒", "同仁堂", "10g*20袋", "中成药"},
            {"双黄连口服液", "太龙", "10ml*10支", "中成药"},
            {"维生素C片", "维福佳", "0.1g*100片", "西药"},
            {"甲硝唑片", "华意", "0.2g*100片", "西药"},
            {"氯化钠注射液", "生理盐水", "0.9% 500ml", "西药"}
        };

        for (int i = 0; i < medicines.length; i++) {
            Medicine medicine = new Medicine();
            medicine.setMedicineCode("MED" + String.format("%03d", i + 1));
            medicine.setGenericName(medicines[i][0]);
            medicine.setTradeName(medicines[i][1]);
            medicine.setSpecification(medicines[i][2]);
            medicine.setCategory(medicines[i][3]);
            medicine.setManufacturer("制药厂" + (i + 1));
            medicine.setUnit("盒");
            medicine.setPrice(new BigDecimal(String.format("%.2f", 10 + i * 5.5)));
            medicine.setStockQuantity(50 + i * 10);
            medicine.setStatus("正常");
            medicineRepository.save(medicine);
        }
    }

    private void initHospitalizations() {
        if (hospitalizationRepository.count() > 0) return;

        for (int i = 1; i <= 4; i++) {
            Hospitalization hospitalization = new Hospitalization();
            hospitalization.setHospitalNo("H" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%04d", i));
            hospitalization.setPatientId((long) i);
            hospitalization.setBedId((long) i);
            hospitalization.setDepartment(i % 2 == 0 ? "内科" : "外科");
            hospitalization.setDoctorId((long) (i % 5 + 1));
            hospitalization.setNurseId((long) (i % 5 + 6));
            hospitalization.setAdmissionDate(LocalDateTime.now().minusDays(i));
            hospitalization.setAdmissionDiagnosis(i % 2 == 0 ? "上呼吸道感染" : "急性阑尾炎");
            hospitalization.setStatus("住院中");
            hospitalizationRepository.save(hospitalization);
        }
    }

    private void initMedicalOrders() {
        if (medicalOrderRepository.count() > 0) return;

        String[] categories = {"药品", "检查", "治疗", "护理"};
        String[] orderTypes = {"长期", "临时"};

        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < 3; j++) {
                MedicalOrder order = new MedicalOrder();
                order.setOrderNo("O" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + i + j);
                order.setHospitalizationId((long) i);
                order.setPatientId((long) i);
                order.setOrderType(orderTypes[j % 2]);
                order.setCategory(categories[j % 4]);
                order.setName(getOrderName(categories[j % 4], j));
                order.setContent(getOrderContent(categories[j % 4], j));
                order.setDosage(j % 2 == 0 ? "1片" : "1支");
                order.setFrequency(j % 2 == 0 ? "每日三次" : "每日一次");
                order.setRoute(j % 2 == 0 ? "口服" : "静脉滴注");
                order.setPrice(new BigDecimal(String.format("%.2f", 20.0 + j * 15.0)));
                order.setQuantity(j + 1);
                order.setTotalAmount(order.getPrice().multiply(new BigDecimal(order.getQuantity())));
                order.setDoctorId((long) (i % 5 + 1));
                order.setStartTime(LocalDateTime.now().minusDays(i));
                order.setStatus(j % 3 == 0 ? "已执行" : j % 3 == 1 ? "待执行" : "执行中");
                if (order.getStatus().equals("已执行")) {
                    order.setExecuteTime(LocalDateTime.now().minusHours(j * 2));
                    order.setExecuteNurseId((long) (i % 5 + 6));
                }
                medicalOrderRepository.save(order);
            }
        }
    }

    private String getOrderName(String category, int index) {
        switch (category) {
            case "药品":
                return new String[]{"阿莫西林胶囊", "布洛芬缓释胶囊", "奥美拉唑肠溶胶囊"}[index];
            case "检查":
                return new String[]{"血常规检查", "尿常规检查", "肝功能检查"}[index];
            case "治疗":
                return new String[]{"静脉输液", "氧气吸入", "物理降温"}[index];
            case "护理":
                return new String[]{"一级护理", "口腔护理", "皮肤护理"}[index];
            default:
                return "其他医嘱";
        }
    }

    private String getOrderContent(String category, int index) {
        switch (category) {
            case "药品":
                return new String[]{"抗感染治疗", "退热止痛", "护胃治疗"}[index];
            case "检查":
                return new String[]{"常规检查", "常规检查", "术前检查"}[index];
            case "治疗":
                return new String[]{"补充体液", "改善缺氧", "对症处理"}[index];
            case "护理":
                return new String[]{"密切观察病情", "保持口腔清洁", "预防压疮"}[index];
            default:
                return "其他内容";
        }
    }

    private void initMedicalRecords() {
        if (medicalRecordRepository.count() > 0) return;

        String[] recordTypes = {"首次病程", "病程记录", "出院小结"};

        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < 2; j++) {
                MedicalRecord record = new MedicalRecord();
                record.setRecordNo("R" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + i + j);
                record.setHospitalizationId((long) i);
                record.setPatientId((long) i);
                record.setRecordType(recordTypes[j]);
                record.setTitle(j == 0 ? "首次病程记录" : "日常病程记录");
                record.setChiefComplaint("发热、咳嗽" + (i + 2) + "天");
                record.setPresentIllness("患者" + (i + 2) + "天前无明显诱因出现发热，体温最高38.5℃，伴咳嗽、咳痰，痰色黄质粘，无胸痛、咯血，无呼吸困难。自服感冒药效果不佳，遂来我院就诊。");
                record.setPastHistory(i % 2 == 0 ? "既往体健，否认高血压、糖尿病病史" : "既往有高血压病史5年，规律服药");
                record.setPersonalHistory("生于原籍，久居本地，无疫区旅居史。");
                record.setFamilyHistory("父母体健，否认家族性遗传病史。");
                record.setPhysicalExamination("T:38.2℃，P:88次/分，R:20次/分，BP:125/75mmHg。神志清楚，精神可，全身皮肤粘膜无黄染，浅表淋巴结未触及肿大。");
                record.setAuxiliaryExamination("血常规：白细胞12.5×10^9/L，中性粒细胞85%；胸片：肺纹理增粗。");
                record.setDiagnosis(i % 2 == 0 ? "上呼吸道感染" : "急性支气管炎");
                record.setTreatmentPlan("1. 抗感染治疗；2. 对症支持治疗；3. 完善相关检查。");
                record.setDoctorId((long) (i % 5 + 1));
                record.setStatus(j == 0 ? "已提交" : "草稿");
                medicalRecordRepository.save(record);
            }
        }
    }

    private void initVitalSigns() {
        if (vitalSignsRepository.count() > 0) return;

        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < 3; j++) {
                VitalSigns vs = new VitalSigns();
                vs.setHospitalizationId((long) i);
                vs.setPatientId((long) i);
                vs.setTemperature(new BigDecimal(String.format("%.1f", 36.5 + j * 0.3)));
                vs.setPulse(70 + j * 5 + i * 2);
                vs.setRespiration(18 + j + i);
                vs.setSystolicPressure(115 + j * 5 + i * 3);
                vs.setDiastolicPressure(75 + j * 3 + i * 2);
                vs.setOxygenSaturation(new BigDecimal("98.5"));
                vs.setBloodGlucose(new BigDecimal(String.format("%.1f", 5.0 + j * 0.5)));
                vs.setPainScore(j);
                vs.setConsciousness("清醒");
                vs.setNurseId((long) (i % 5 + 6));
                vs.setRecordTime(LocalDateTime.now().minusDays(j).minusHours(i));
                vitalSignsRepository.save(vs);
            }
        }
    }

    private void initFeeRecords() {
        if (feeRecordRepository.count() > 0) return;

        String[][] feeTypes = {
            {"药品", "阿莫西林胶囊", "0.5g*24粒"},
            {"检查", "血常规检查", "常规"},
            {"治疗", "静脉输液", "次"},
            {"护理", "一级护理", "日"},
            {"床位", "普通床位费", "日"}
        };

        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < feeTypes.length; j++) {
                FeeRecord fee = new FeeRecord();
                fee.setFeeNo("F" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + i + j);
                fee.setHospitalizationId((long) i);
                fee.setPatientId((long) i);
                fee.setFeeType(feeTypes[j][0]);
                fee.setItemName(feeTypes[j][1]);
                fee.setItemSpec(feeTypes[j][2]);
                fee.setPrice(new BigDecimal(String.format("%.2f", 20.0 + j * 15.0)));
                fee.setQuantity(j + 1);
                fee.setUnit(j % 2 == 0 ? "盒" : "次");
                fee.setAmount(fee.getPrice().multiply(new BigDecimal(fee.getQuantity())));
                fee.setOperatorId((long) (i % 5 + 6));
                fee.setPaymentStatus(j % 2 == 0 ? "已缴费" : "未缴费");
                if (fee.getPaymentStatus().equals("已缴费")) {
                    fee.setPaymentTime(LocalDateTime.now().minusHours(j * 3));
                }
                feeRecordRepository.save(fee);
            }
        }
    }
}
