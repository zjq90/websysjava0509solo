package com.medical.appointment.config;

import com.medical.appointment.entity.*;
import com.medical.appointment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            initDepartments();
        }
        if (doctorRepository.count() == 0) {
            initDoctors();
        }
        if (scheduleRepository.count() == 0) {
            initSchedules();
        }
    }

    private void initDepartments() {
        Department internalMedicine = createDepartment(null, "内科", "内科是主要诊疗内科疾病的科室", 1);
        Department neurology = createDepartment(internalMedicine, "神经内科", "诊疗神经系统疾病", 1);
        Department cardiology = createDepartment(internalMedicine, "心血管内科", "诊疗心血管疾病", 2);
        Department gastroenterology = createDepartment(internalMedicine, "消化内科", "诊疗消化系统疾病", 3);
        Department endocrinology = createDepartment(internalMedicine, "内分泌科", "诊疗内分泌疾病", 4);

        Department surgery = createDepartment(null, "外科", "外科是主要诊疗外科疾病的科室", 2);
        Department orthopedics = createDepartment(surgery, "骨科", "诊疗骨骼肌肉系统疾病", 1);
        Department generalSurgery = createDepartment(surgery, "普外科", "诊疗普通外科疾病", 2);

        Department obgyn = createDepartment(null, "妇产科", "诊疗妇产科疾病", 3);
        Department gynecology = createDepartment(obgyn, "妇科", "诊疗妇科疾病", 1);
        Department obstetrics = createDepartment(obgyn, "产科", "诊疗产科疾病", 2);

        Department pediatrics = createDepartment(null, "儿科", "诊疗儿童疾病", 4);
        
        Department dermatology = createDepartment(null, "皮肤科", "诊疗皮肤疾病", 5);
        Department ophthalmology = createDepartment(null, "眼科", "诊疗眼部疾病", 6);
        Department ent = createDepartment(null, "耳鼻喉科", "诊疗耳鼻喉疾病", 7);
        Department stomatology = createDepartment(null, "口腔科", "诊疗口腔疾病", 8);
        Department psychiatry = createDepartment(null, "精神心理科", "诊疗精神心理疾病", 9);
        Department traditional = createDepartment(null, "中医科", "中医诊疗", 10);
    }

    private Department createDepartment(Department parent, String name, String description, int sortOrder) {
        Department dept = Department.builder()
                .parent(parent)
                .name(name)
                .description(description)
                .sortOrder(sortOrder)
                .isActive(true)
                .build();
        return departmentRepository.save(dept);
    }

    private void initDoctors() {
        Department neurology = departmentRepository.findByName("神经内科").orElse(null);
        Department cardiology = departmentRepository.findByName("心血管内科").orElse(null);
        Department gastroenterology = departmentRepository.findByName("消化内科").orElse(null);
        Department orthopedics = departmentRepository.findByName("骨科").orElse(null);
        Department gynecology = departmentRepository.findByName("妇科").orElse(null);
        Department pediatrics = departmentRepository.findByName("儿科").orElse(null);
        Department dermatology = departmentRepository.findByName("皮肤科").orElse(null);
        Department ophthalmology = departmentRepository.findByName("眼科").orElse(null);

        if (neurology != null) {
            createDoctor(neurology, "张明", Doctor.Title.PROFESSOR, 
                    "脑血管病、头痛、癫痫、帕金森病", 
                    "从事神经内科临床工作30年，擅长脑血管病的诊治和康复，在头痛、眩晕、癫痫等疾病的诊治方面有丰富经验。",
                    new BigDecimal("5.0"), 156, 2580, new BigDecimal("100.00"));
            
            createDoctor(neurology, "李华", Doctor.Title.ASSOCIATE_PROFESSOR,
                    "头晕、失眠、焦虑、抑郁",
                    "副主任医师，硕士研究生导师，擅长头晕、失眠等神经系统疾病的诊治。",
                    new BigDecimal("4.8"), 89, 1890, new BigDecimal("80.00"));
        }

        if (cardiology != null) {
            createDoctor(cardiology, "王芳", Doctor.Title.PROFESSOR,
                    "高血压、冠心病、心律失常、心力衰竭",
                    "心血管内科主任医师，博士生导师，在心血管疾病诊治方面有很深造诣。",
                    new BigDecimal("4.9"), 234, 3200, new BigDecimal("120.00"));
            
            createDoctor(cardiology, "刘强", Doctor.Title.ATTENDING,
                    "高血压、冠心病的诊治",
                    "主治医师，擅长高血压、冠心病等常见心血管疾病的诊治。",
                    new BigDecimal("4.7"), 67, 980, new BigDecimal("50.00"));
        }

        if (gastroenterology != null) {
            createDoctor(gastroenterology, "陈静", Doctor.Title.ASSOCIATE_PROFESSOR,
                    "胃炎、胃溃疡、肠炎、肝病",
                    "消化内科副主任医师，擅长消化系统疾病的诊治。",
                    new BigDecimal("4.8"), 145, 2100, new BigDecimal("80.00"));
        }

        if (orthopedics != null) {
            createDoctor(orthopedics, "赵刚", Doctor.Title.PROFESSOR,
                    "骨折、关节置换、脊柱疾病",
                    "骨科主任医师，擅长复杂骨折、人工关节置换术。",
                    new BigDecimal("4.9"), 189, 2800, new BigDecimal("150.00"));
            
            createDoctor(orthopedics, "孙丽", Doctor.Title.ATTENDING,
                    "关节炎、骨质疏松",
                    "主治医师，擅长骨关节疾病的诊治。",
                    new BigDecimal("4.6"), 56, 780, new BigDecimal("50.00"));
        }

        if (gynecology != null) {
            createDoctor(gynecology, "周敏", Doctor.Title.PROFESSOR,
                    "妇科肿瘤、月经不调、不孕症",
                    "妇科主任医师，在妇科疾病诊治方面经验丰富。",
                    new BigDecimal("4.9"), 210, 2950, new BigDecimal("100.00"));
        }

        if (pediatrics != null) {
            createDoctor(pediatrics, "吴娟", Doctor.Title.ASSOCIATE_PROFESSOR,
                    "小儿呼吸系统疾病、小儿消化系统疾病",
                    "儿科副主任医师，擅长儿童常见病、多发病的诊治。",
                    new BigDecimal("4.8"), 178, 2400, new BigDecimal("80.00"));
        }

        if (dermatology != null) {
            createDoctor(dermatology, "郑伟", Doctor.Title.ATTENDING,
                    "湿疹、皮炎、痤疮、银屑病",
                    "皮肤科主治医师，擅长各类皮肤疾病的诊治。",
                    new BigDecimal("4.7"), 98, 1500, new BigDecimal("50.00"));
        }

        if (ophthalmology != null) {
            createDoctor(ophthalmology, "冯雪", Doctor.Title.ASSOCIATE_PROFESSOR,
                    "白内障、青光眼、近视眼",
                    "眼科副主任医师，擅长各类眼科疾病的诊治和手术。",
                    new BigDecimal("4.8"), 134, 1900, new BigDecimal("80.00"));
        }
    }

    private void createDoctor(Department dept, String name, Doctor.Title title, 
                              String specialty, String introduction,
                              BigDecimal rating, int reviewCount, int visitCount, BigDecimal fee) {
        Doctor doctor = Doctor.builder()
                .department(dept)
                .realName(name)
                .title(title)
                .specialty(specialty)
                .introduction(introduction)
                .rating(rating)
                .reviewCount(reviewCount)
                .visitCount(visitCount)
                .consultationFee(fee)
                .isActive(true)
                .build();
        doctorRepository.save(doctor);
    }

    private void initSchedules() {
        java.util.List<Doctor> doctors = doctorRepository.findAll();
        LocalDate today = LocalDate.now();
        
        for (Doctor doctor : doctors) {
            for (int i = 0; i < 14; i++) {
                LocalDate date = today.plusDays(i);
                if (date.getDayOfWeek().getValue() <= 5) {
                    createSchedule(doctor, date, DoctorSchedule.TimeSlot.MORNING, 
                            LocalTime.of(8, 0), LocalTime.of(12, 0), 15, (int)(Math.random() * 8));
                    createSchedule(doctor, date, DoctorSchedule.TimeSlot.AFTERNOON,
                            LocalTime.of(14, 0), LocalTime.of(17, 30), 12, (int)(Math.random() * 6));
                }
            }
        }
    }

    private void createSchedule(Doctor doctor, LocalDate date, DoctorSchedule.TimeSlot slot,
                                LocalTime start, LocalTime end, int total, int booked) {
        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .scheduleDate(date)
                .timeSlot(slot)
                .startTime(start)
                .endTime(end)
                .totalSlots(total)
                .bookedSlots(booked)
                .isActive(true)
                .build();
        scheduleRepository.save(schedule);
    }
}
