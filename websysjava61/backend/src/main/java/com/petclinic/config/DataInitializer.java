package com.petclinic.config;

import com.petclinic.entity.*;
import com.petclinic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

/**
 * 数据初始化器 - 生成测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DiseaseRecordRepository diseaseRecordRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            initDepartments();
        }
        if (doctorRepository.count() == 0) {
            initDoctors();
        }
        if (scheduleRepository.count() == 0) {
            initSchedules();
        }
        if (medicineRepository.count() == 0) {
            initMedicines();
        }
        if (petOwnerRepository.count() == 0) {
            initPetOwners();
        }
        if (consultationRepository.count() == 0) {
            initConsultations();
        }
        if (diseaseRecordRepository.count() == 0) {
            initDiseaseRecords();
        }
    }

    private void initDepartments() {
        String[][] depts = {
            {"内科", "NEIKE", "负责宠物内科疾病诊治"},
            {"外科", "WAIKE", "负责宠物外科手术和治疗"},
            {"急诊", "JIZHEN", "24小时急诊服务"},
            {"皮肤科", "PIFU", "皮肤疾病专科"},
            {"口腔科", "KOUQIANG", "口腔健康护理"},
            {"影像科", "YINGXIANG", "X光、B超等检查"}
        };

        for (String[] dept : depts) {
            Department department = new Department();
            department.setName(dept[0]);
            department.setCode(dept[1]);
            department.setDescription(dept[2]);
            department.setLocation("门诊" + (random.nextInt(5) + 1) + "楼");
            department.setStatus(1);
            departmentRepository.save(department);
        }
    }

    private void initDoctors() {
        String[][] doctors = {
            {"张医生", "13800138001", "主任医师", "擅长犬猫内科疾病"},
            {"李医生", "13800138002", "副主任医师", "擅长骨科手术"},
            {"王医生", "13800138003", "主治医师", "擅长皮肤疾病"},
            {"赵医生", "13800138004", "主治医师", "擅长急诊处理"},
            {"刘医生", "13800138005", "医师", "擅长口腔科"},
            {"陈医生", "13800138006", "医师", "擅长影像诊断"}
        };

        java.util.List<Department> departments = departmentRepository.findAll();

        for (int i = 0; i < doctors.length; i++) {
            Doctor doctor = new Doctor();
            doctor.setName(doctors[i][0]);
            doctor.setPhone(doctors[i][1]);
            doctor.setDepartmentId(departments.get(i % departments.size()).getId());
            doctor.setTitle(doctors[i][2]);
            doctor.setSpecialty(doctors[i][3]);
            doctor.setLicenseNumber("LIC-" + (1000 + i));
            // 使用随机生成的执业证图片（演示用）
            doctor.setLicenseImageUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=medical%20license%20certificate%20professional%20document%20blue%20background&image_size=square");
            doctor.setAuditStatus(i < 4 ? 1 : 0); // 前4个审核通过，后2个待审核
            doctor.setStatus(1);
            doctorRepository.save(doctor);
        }
    }

    private void initSchedules() {
        java.util.List<Doctor> doctors = doctorRepository.findAll();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate scheduleDate = today.plusDays(i);
            for (Doctor doctor : doctors) {
                if (random.nextBoolean()) {
                    Schedule schedule = new Schedule();
                    schedule.setDoctorId(doctor.getId());
                    schedule.setDepartmentId(doctor.getDepartmentId());
                    schedule.setScheduleDate(scheduleDate);
                    schedule.setStartTime(LocalTime.of(8 + random.nextInt(4), 0));
                    schedule.setEndTime(LocalTime.of(14 + random.nextInt(4), 0));
                    schedule.setMaxCount(8 + random.nextInt(6));
                    schedule.setBookedCount(random.nextInt(5));
                    schedule.setStatus(1);
                    scheduleRepository.save(schedule);
                }
            }
        }
    }

    private void initMedicines() {
        String[][] medicines = {
            {"驱虫药-体内外", "QC001", "驱虫药", "100ml/瓶", "拜耳", "5", "10"},
            {"感冒药", "GM001", "感冒药", "50片/盒", "辉瑞", "15", "20"},
            {"消炎药", "XY001", "抗生素", "30片/盒", "默沙东", "8", "15"},
            {"皮肤病软膏", "PF001", "外用药", "20g/支", "维克", "3", "10"},
            {"眼药水", "YY001", "眼科用药", "10ml/瓶", "爱尔康", "12", "8"},
            {"钙片", "GP001", "营养品", "100片/瓶", "卫仕", "20", "15"},
            {"维生素片", "WSS001", "营养品", "60片/瓶", "麦德氏", "25", "10"},
            {"止泻药", "ZX001", "消化药", "10袋/盒", "妈咪爱", "7", "10"}
        };

        for (String[] med : medicines) {
            Medicine medicine = new Medicine();
            medicine.setName(med[0]);
            medicine.setCode(med[1]);
            medicine.setCategory(med[2]);
            medicine.setSpecification(med[3]);
            medicine.setManufacturer(med[4]);
            medicine.setStockQuantity(Integer.parseInt(med[5]));
            medicine.setWarningQuantity(Integer.parseInt(med[6]));
            medicine.setPrice(new BigDecimal(20 + random.nextInt(100)));
            medicine.setUnit("盒");
            medicine.setStatus(1);
            medicineRepository.save(medicine);
        }
    }

    private void initPetOwners() {
        String[][] owners = {
            {"王先生", "13900139001", "wang@example.com", "北京市朝阳区"},
            {"李女士", "13900139002", "li@example.com", "北京市海淀区"},
            {"张先生", "13900139003", "zhang@example.com", "北京市西城区"},
            {"刘女士", "13900139004", "liu@example.com", "北京市东城区"},
            {"陈先生", "13900139005", "chen@example.com", "北京市丰台区"},
            {"杨女士", "13900139006", "yang@example.com", "北京市石景山区"},
            {"黄先生", "13900139007", "huang@example.com", "北京市通州区"},
            {"赵女士", "13900139008", "zhao@example.com", "北京市大兴区"}
        };

        for (String[] owner : owners) {
            PetOwner petOwner = new PetOwner();
            petOwner.setName(owner[0]);
            petOwner.setPhone(owner[1]);
            petOwner.setEmail(owner[2]);
            petOwner.setAddress(owner[3]);
            petOwner.setIdCard("1101011990" + String.format("%06d", random.nextInt(1000000)));
            petOwner.setRealNameStatus(random.nextInt(3) + 1);
            petOwner.setCreditScore(90 + random.nextInt(20));
            petOwner.setStatus(1);
            petOwnerRepository.save(petOwner);
        }
    }

    private void initConsultations() {
        java.util.List<PetOwner> owners = petOwnerRepository.findAll();
        java.util.List<Doctor> doctors = doctorRepository.findAll();
        String[] petTypes = {"猫", "狗", "兔子", "仓鼠", "鹦鹉"};
        String[] symptoms = {"食欲不振", "呕吐腹泻", "皮肤瘙痒", "眼睛红肿", "咳嗽打喷嚏"};

        LocalDateTime startDate = LocalDateTime.now().minusDays(30);

        for (int i = 0; i < 50; i++) {
            Consultation consultation = new Consultation();
            consultation.setConsultationNo("CZ" + System.currentTimeMillis() + i);
            consultation.setPetOwnerId(owners.get(random.nextInt(owners.size())).getId());
            consultation.setDoctorId(doctors.get(random.nextInt(doctors.size())).getId());
            consultation.setDepartmentId(doctors.get(random.nextInt(doctors.size())).getDepartmentId());
            consultation.setPetName("宠物" + (i + 1));
            consultation.setPetType(petTypes[random.nextInt(petTypes.length)]);
            consultation.setPetAge(1 + random.nextInt(10));
            consultation.setSymptom(symptoms[random.nextInt(symptoms.length)]);
            consultation.setDiagnosis("初步诊断结果");
            consultation.setIsEmergency(random.nextInt(5) == 0 ? 1 : 0);
            consultation.setFee(new BigDecimal(50 + random.nextInt(200)));
            consultation.setStatus(random.nextInt(4));
            consultation.setCreateTime(startDate.plusHours(i * 3));
            consultationRepository.save(consultation);
        }
    }

    private void initDiseaseRecords() {
        java.util.List<Consultation> consultations = consultationRepository.findAll();
        String[] diseases = {"感冒", "肠胃炎", "皮肤病", "耳螨", "结膜炎", "牙结石", "关节炎"};
        String[] petTypes = {"猫", "狗", "兔子", "仓鼠"};

        for (int i = 0; i < 80; i++) {
            DiseaseRecord record = new DiseaseRecord();
            record.setConsultationId(consultations.get(random.nextInt(consultations.size())).getId());
            record.setDiseaseName(diseases[random.nextInt(diseases.length)]);
            record.setPetType(petTypes[random.nextInt(petTypes.length)]);
            record.setPetAge(1 + random.nextInt(12));
            record.setSeverity(1 + random.nextInt(3));
            record.setRecordTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            diseaseRecordRepository.save(record);
        }
    }
}
