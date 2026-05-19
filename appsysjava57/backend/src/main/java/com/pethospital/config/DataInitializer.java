package com.pethospital.config;

import com.pethospital.entity.*;
import com.pethospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 测试数据初始化器
 * 系统启动时自动生成测试数据
 * 
 * @author Pet Hospital Team
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private VaccineRecordRepository vaccineRecordRepository;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initPets();
        initDoctors();
        initDoctorSchedules();
        initMedicines();
        initHospitals();
        initVaccineRecords();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword("123456");
            user.setPhone("13800138000");
            user.setEmail("test@example.com");
            user.setRealName("张三");
            user.setGender("男");
            user.setAge(30);
            user.setAddress("北京市朝阳区");
            user.setEnabled(true);
            user.setRole("USER");
            user.setElderMode(false);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    private void initPets() {
        if (petRepository.count() == 0) {
            User user = userRepository.findByUsername("testuser").orElse(null);
            if (user != null) {
                Pet pet1 = new Pet();
                pet1.setUserId(user.getId());
                pet1.setName("豆豆");
                pet1.setSpecies("狗");
                pet1.setBreed("金毛");
                pet1.setGender("公");
                pet1.setBirthDate(LocalDate.of(2020, 5, 15));
                pet1.setWeight(25.5);
                pet1.setDescription("活泼可爱的金毛犬");
                pet1.setAllergyInfo("无");
                pet1.setNeutered(true);
                pet1.setCreateTime(LocalDateTime.now());
                pet1.setUpdateTime(LocalDateTime.now());
                petRepository.save(pet1);

                Pet pet2 = new Pet();
                pet2.setUserId(user.getId());
                pet2.setName("咪咪");
                pet2.setSpecies("猫");
                pet2.setBreed("英短");
                pet2.setGender("母");
                pet2.setBirthDate(LocalDate.of(2021, 3, 20));
                pet2.setWeight(4.5);
                pet2.setDescription("温顺的英短蓝猫");
                pet2.setAllergyInfo("海鲜过敏");
                pet2.setNeutered(false);
                pet2.setCreateTime(LocalDateTime.now());
                pet2.setUpdateTime(LocalDateTime.now());
                petRepository.save(pet2);
            }
        }
    }

    private void initDoctors() {
        if (doctorRepository.count() == 0) {
            String[] departments = {"内科", "外科", "皮肤科", "牙科", "眼科"};
            String[] titles = {"主任医师", "副主任医师", "主治医师"};
            String[] names = {"王医生", "李医生", "张医生", "刘医生", "陈医生"};
            
            for (int i = 0; i < 5; i++) {
                Doctor doctor = new Doctor();
                doctor.setName(names[i]);
                doctor.setDepartment(departments[i]);
                doctor.setTitle(titles[i % 3]);
                doctor.setSpecialties("擅长" + departments[i] + "疾病诊治");
                doctor.setIntroduction("从事宠物医疗工作" + (5 + i) + "年，经验丰富");
                doctor.setExperienceYears(5 + i);
                doctor.setPhone("1390013800" + i);
                doctor.setEmail("doctor" + i + "@pethospital.com");
                doctor.setLicenseNumber("LIC" + (1000 + i));
                doctor.setConsultationFee(BigDecimal.valueOf(50 + i * 10));
                doctor.setRating(4.5 + i * 0.1);
                doctor.setReviewCount(10 + i * 5);
                doctor.setAvailable(true);
                doctor.setCreateTime(LocalDateTime.now());
                doctor.setUpdateTime(LocalDateTime.now());
                doctorRepository.save(doctor);
            }
        }
    }

    private void initDoctorSchedules() {
        if (doctorScheduleRepository.count() == 0) {
            doctorRepository.findAll().forEach(doctor -> {
                for (int i = 0; i < 7; i++) {
                    DoctorSchedule schedule = new DoctorSchedule();
                    schedule.setDoctorId(doctor.getId());
                    schedule.setScheduleDate(LocalDate.now().plusDays(i));
                    schedule.setStartTime(LocalTime.of(9, 0));
                    schedule.setEndTime(LocalTime.of(12, 0));
                    schedule.setTimeSlot("上午");
                    schedule.setMaxPatients(10);
                    schedule.setBookedCount((int) (Math.random() * 5));
                    schedule.setStatus("AVAILABLE");
                    schedule.setCreateTime(LocalDateTime.now());
                    schedule.setUpdateTime(LocalDateTime.now());
                    doctorScheduleRepository.save(schedule);
                }
            });
        }
    }

    private void initMedicines() {
        if (medicineRepository.count() == 0) {
            String[][] medicines = {
                {"驱虫药", "体内驱虫片", "deworming", "false"},
                {"驱虫药", "体外驱虫滴剂", "deworming", "false"},
                {"营养膏", "综合营养膏", "nutrition", "false"},
                {"感冒药", "宠物感冒颗粒", "cold", "false"},
                {"皮肤药", "皮肤消炎膏", "skin", "false"},
                {"疫苗", "狂犬疫苗", "vaccine", "false"},
                {"保健品", "维生素片", "health", "false"},
                {"处方药", "抗生素A", "prescription", "true"}
            };

            for (String[] med : medicines) {
                Medicine medicine = new Medicine();
                medicine.setCategory(med[0]);
                medicine.setName(med[1]);
                medicine.setGenericName(med[1] + "通用");
                medicine.setPrescriptionRequired(Boolean.parseBoolean(med[3]));
                medicine.setDescription("这是" + med[1] + "的详细描述");
                medicine.setUsageInstructions("按说明书使用");
                medicine.setSideEffects("暂无明显副作用");
                medicine.setContraindications("过敏者禁用");
                medicine.setManufacturer("某制药厂");
                medicine.setSpecification("100片/盒");
                medicine.setPrice(BigDecimal.valueOf(30 + (int)(Math.random() * 70)));
                medicine.setStockQuantity(100);
                medicine.setStatus("AVAILABLE");
                medicine.setCreateTime(LocalDateTime.now());
                medicine.setUpdateTime(LocalDateTime.now());
                medicineRepository.save(medicine);
            }
        }
    }

    private void initHospitals() {
        if (hospitalRepository.count() == 0) {
            String[][] hospitals = {
                {"宠物急救中心", "北京市朝阳区", "010-12345678", "true", "true"},
                {"爱宠医院", "北京市海淀区", "010-87654321", "true", "true"},
                {"阳光宠物诊所", "北京市西城区", "010-11112222", "false", "false"}
            };

            for (String[] hosp : hospitals) {
                Hospital hospital = new Hospital();
                hospital.setName(hosp[0]);
                hospital.setAddress(hosp[1]);
                hospital.setPhone(hosp[2]);
                hospital.setLatitude(39.9 + Math.random() * 0.1);
                hospital.setLongitude(116.3 + Math.random() * 0.1);
                hospital.setBusinessHours("24小时营业");
                hospital.setIs24Hours(Boolean.parseBoolean(hosp[3]));
                hospital.setEmergencyService(Boolean.parseBoolean(hosp[4]));
                hospital.setServices("全科医疗、急诊、手术、住院");
                hospital.setRating(4.5 + Math.random() * 0.5);
                hospital.setReviewCount(50 + (int) (Math.random() * 100));
                hospital.setDescription(hosp[0] + "是专业的宠物医疗机构");
                hospital.setEnabled(true);
                hospital.setCreateTime(LocalDateTime.now());
                hospital.setUpdateTime(LocalDateTime.now());
                hospitalRepository.save(hospital);
            }
        }
    }

    private void initVaccineRecords() {
        if (vaccineRecordRepository.count() == 0) {
            petRepository.findAll().forEach(pet -> {
                VaccineRecord record1 = new VaccineRecord();
                record1.setPetId(pet.getId());
                record1.setVaccineName("狂犬疫苗");
                record1.setVaccinationDate(LocalDate.now().minusMonths(11));
                record1.setExpiryDate(LocalDate.now().plusDays(20));
                record1.setManufacturer("某疫苗厂");
                record1.setBatchNumber("BATCH2024001");
                record1.setVeterinary("王医生");
                record1.setHospital("爱宠医院");
                record1.setReminderSent(false);
                record1.setCreateTime(LocalDateTime.now());
                record1.setUpdateTime(LocalDateTime.now());
                vaccineRecordRepository.save(record1);

                VaccineRecord record2 = new VaccineRecord();
                record2.setPetId(pet.getId());
                record2.setVaccineName("六联疫苗");
                record2.setVaccinationDate(LocalDate.now().minusMonths(6));
                record2.setExpiryDate(LocalDate.now().plusMonths(6));
                record2.setManufacturer("某疫苗厂");
                record2.setBatchNumber("BATCH2024002");
                record2.setVeterinary("李医生");
                record2.setHospital("爱宠医院");
                record2.setReminderSent(false);
                record2.setCreateTime(LocalDateTime.now());
                record2.setUpdateTime(LocalDateTime.now());
                vaccineRecordRepository.save(record2);
            });
        }
    }
}
