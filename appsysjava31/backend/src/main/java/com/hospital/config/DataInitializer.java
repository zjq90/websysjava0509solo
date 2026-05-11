package com.hospital.config;

import com.hospital.entity.*;
import com.hospital.repository.*;
import com.hospital.util.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 系统启动时初始化测试数据
 * 
 * @author hospital
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Sm4Util sm4Util;

    @Override
    @Transactional
    public void run(String... args) {
        initUsers();
        initDepartments();
        initDoctors();
        initTestData();
    }

    private void initUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("系统管理员");
        admin.setRole("ADMIN");
        admin.setStatus(1);
        admin.setPhone(sm4Util.encrypt("13800138000"));
        userRepository.save(admin);

        User patient1 = new User();
        patient1.setUsername("patient");
        patient1.setPassword(passwordEncoder.encode("123456"));
        patient1.setName("张三");
        patient1.setRole("PATIENT");
        patient1.setStatus(1);
        patient1.setGender("M");
        patient1.setPhone(sm4Util.encrypt("13900139001"));
        patient1.setIdCard(sm4Util.encrypt("110101199003071234"));
        userRepository.save(patient1);

        NotificationSetting setting1 = new NotificationSetting();
        setting1.setUserId(patient1.getId());
        notificationSettingRepository.save(setting1);

        User patient2 = new User();
        patient2.setUsername("patient2");
        patient2.setPassword(passwordEncoder.encode("123456"));
        patient2.setName("李四");
        patient2.setRole("PATIENT");
        patient2.setStatus(1);
        patient2.setGender("F");
        patient2.setPhone(sm4Util.encrypt("13900139002"));
        patient2.setIdCard(sm4Util.encrypt("110101199205085678"));
        userRepository.save(patient2);

        NotificationSetting setting2 = new NotificationSetting();
        setting2.setUserId(patient2.getId());
        notificationSettingRepository.save(setting2);

        User doctorUser = new User();
        doctorUser.setUsername("doctor");
        doctorUser.setPassword(passwordEncoder.encode("123456"));
        doctorUser.setName("王医生");
        doctorUser.setRole("DOCTOR");
        doctorUser.setStatus(1);
        doctorUser.setPhone(sm4Util.encrypt("13900139011"));
        userRepository.save(doctorUser);

        User doctorUser2 = new User();
        doctorUser2.setUsername("doctor2");
        doctorUser2.setPassword(passwordEncoder.encode("123456"));
        doctorUser2.setName("李医生");
        doctorUser2.setRole("DOCTOR");
        doctorUser2.setStatus(1);
        doctorUser2.setPhone(sm4Util.encrypt("13900139012"));
        userRepository.save(doctorUser2);
    }

    private void initDepartments() {
        if (departmentRepository.count() > 0) {
            return;
        }

        String[][] depts = {
                {"DEPT001", "内科", "INTERNAL", "门诊三楼"},
                {"DEPT002", "外科", "SURGERY", "门诊一楼"},
                {"DEPT003", "儿科", "OTHER", "门诊二楼"},
                {"DEPT004", "妇产科", "OTHER", "门诊四楼"},
                {"DEPT005", "眼科", "OTHER", "门诊五楼"},
                {"DEPT006", "耳鼻喉科", "OTHER", "门诊五楼"},
                {"DEPT007", "口腔科", "OTHER", "门诊二楼"},
                {"DEPT008", "皮肤科", "OTHER", "门诊三楼"},
                {"DEPT009", "急诊科", "EMERGENCY", "急诊楼"},
                {"DEPT010", "中医科", "OTHER", "门诊四楼"}
        };

        for (int i = 0; i < depts.length; i++) {
            Department dept = new Department();
            dept.setDeptCode(depts[i][0]);
            dept.setDeptName(depts[i][1]);
            dept.setDeptType(depts[i][2]);
            dept.setParentId(0L);
            dept.setLocation(depts[i][3]);
            dept.setDescription("专业的" + depts[i][1] + "诊疗服务");
            dept.setSortOrder(i + 1);
            dept.setStatus(1);
            departmentRepository.save(dept);
        }
    }

    private void initDoctors() {
        if (doctorRepository.count() > 0) {
            return;
        }

        User doctorUser1 = userRepository.findByUsername("doctor").orElse(null);
        User doctorUser2 = userRepository.findByUsername("doctor2").orElse(null);
        if (doctorUser1 == null || doctorUser2 == null) return;

        Department internal = departmentRepository.findByDeptCode("DEPT001").orElse(null);
        Department surgery = departmentRepository.findByDeptCode("DEPT002").orElse(null);
        Department pediatric = departmentRepository.findByDeptCode("DEPT003").orElse(null);

        if (internal != null) {
            Doctor d1 = new Doctor();
            d1.setUserId(doctorUser1.getId());
            d1.setDeptId(internal.getId());
            d1.setDoctorCode("DOC001");
            d1.setDoctorName("王大明");
            d1.setTitle("PROFESSOR");
            d1.setEducation("DOCTOR");
            d1.setExperienceYears(20);
            d1.setSpecialty("心血管疾病、高血压、冠心病");
            d1.setIntroduction("主任医师，医学博士，从事心血管内科临床工作20年，擅长冠心病、高血压、心律失常等疾病的诊治。");
            d1.setRegistrationFee(new BigDecimal("50.00"));
            d1.setRating(new BigDecimal("4.8"));
            d1.setConsultationCount(1500);
            d1.setIsRecommended(1);
            d1.setStatus(1);
            doctorRepository.save(d1);

            Doctor d2 = new Doctor();
            d2.setUserId(doctorUser2.getId());
            d2.setDeptId(internal.getId());
            d2.setDoctorCode("DOC002");
            d2.setDoctorName("李小红");
            d2.setTitle("ASSOCIATE_PROFESSOR");
            d2.setEducation("MASTER");
            d2.setExperienceYears(15);
            d2.setSpecialty("糖尿病、内分泌疾病");
            d2.setIntroduction("副主任医师，医学硕士，擅长糖尿病、甲状腺疾病等内分泌系统疾病的诊治。");
            d2.setRegistrationFee(new BigDecimal("35.00"));
            d2.setRating(new BigDecimal("4.6"));
            d2.setConsultationCount(1200);
            d2.setIsRecommended(1);
            d2.setStatus(1);
            doctorRepository.save(d2);
        }

        if (surgery != null) {
            Doctor d3 = new Doctor();
            d3.setUserId(4L);
            d3.setDeptId(surgery.getId());
            d3.setDoctorCode("DOC003");
            d3.setDoctorName("张伟");
            d3.setTitle("ATTENDING");
            d3.setEducation("MASTER");
            d3.setExperienceYears(10);
            d3.setSpecialty("普外科疾病、腹腔镜手术");
            d3.setIntroduction("主治医师，医学硕士，擅长普外科常见疾病的诊断和手术治疗。");
            d3.setRegistrationFee(new BigDecimal("25.00"));
            d3.setRating(new BigDecimal("4.5"));
            d3.setConsultationCount(800);
            d3.setIsRecommended(0);
            d3.setStatus(1);
            doctorRepository.save(d3);
        }

        if (pediatric != null) {
            Doctor d4 = new Doctor();
            d4.setUserId(5L);
            d4.setDeptId(pediatric.getId());
            d4.setDoctorCode("DOC004");
            d4.setDoctorName("陈静");
            d4.setTitle("ASSOCIATE_PROFESSOR");
            d4.setEducation("DOCTOR");
            d4.setExperienceYears(18);
            d4.setSpecialty("小儿呼吸系统疾病、小儿感染性疾病");
            d4.setIntroduction("副主任医师，医学博士，从事儿科临床工作18年，对小儿呼吸系统疾病有丰富的诊治经验。");
            d4.setRegistrationFee(new BigDecimal("40.00"));
            d4.setRating(new BigDecimal("4.9"));
            d4.setConsultationCount(2000);
            d4.setIsRecommended(1);
            d4.setStatus(1);
            doctorRepository.save(d4);
        }
    }

    private void initTestData() {
        User patient1 = userRepository.findByUsername("patient").orElse(null);
        Doctor doctor1 = doctorRepository.findByDoctorCode("DOC001").orElse(null);

        if (patient1 != null && doctor1 != null) {
            Appointment appt = new Appointment();
            appt.setAppointmentNo("APT" + System.currentTimeMillis());
            appt.setPatientId(patient1.getId());
            appt.setDoctorId(doctor1.getId());
            appt.setDeptId(doctor1.getDeptId());
            appt.setAppointmentDate(LocalDateTime.now().plusDays(1));
            appt.setStartTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0));
            appt.setEndTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(30).withSecond(0));
            appt.setTimeSlotType("MORNING");
            appt.setTimeSlotNo(1);
            appt.setFee(doctor1.getRegistrationFee());
            appt.setPaymentStatus(1);
            appt.setPaymentMethod("WECHAT");
            appt.setPaymentTime(LocalDateTime.now());
            appt.setPaymentNo("PAY" + System.currentTimeMillis());
            appt.setStatus("PENDING");
            appt.setRoomNo("301诊室");
            appt.setQueueNo(1);
            appt.setChiefComplaint("胸闷、气短一周");
            appt.setNotifiedSuccess(1);
            appointmentRepository.save(appt);

            Report report = new Report();
            report.setReportNo("RPT" + System.currentTimeMillis());
            report.setReportType("LAB");
            report.setReportName("血常规检验报告");
            report.setPatientId(patient1.getId());
            report.setDoctorId(doctor1.getId());
            report.setDeptId(doctor1.getDeptId());
            report.setAppointmentId(appt.getId());
            report.setExamItems("白细胞计数、红细胞计数、血红蛋白、血小板计数");
            report.setExamDate(LocalDateTime.now().minusDays(1));
            report.setReportDate(LocalDateTime.now());
            report.setAuditDoctor("检验师：刘芳");
            report.setSummary("血常规各项指标基本正常");
            report.setConclusion("各项指标在正常范围内");
            report.setSuggestion("建议定期复查");
            report.setSourceSystem("LIS");
            report.setIsAbnormal(0);
            report.setStatus("READY");
            report.setNotified(1);
            reportRepository.save(report);

            Report report2 = new Report();
            report2.setReportNo("RPT" + (System.currentTimeMillis() + 1));
            report2.setReportType("EXAM");
            report2.setReportName("心电图检查报告");
            report2.setPatientId(patient1.getId());
            report2.setDoctorId(doctor1.getId());
            report2.setDeptId(doctor1.getDeptId());
            report2.setAppointmentId(appt.getId());
            report2.setExamItems("常规心电图");
            report2.setExamDate(LocalDateTime.now().minusDays(1));
            report2.setReportDate(LocalDateTime.now());
            report2.setAuditDoctor("技师：王明");
            report2.setSummary("窦性心律，正常心电图");
            report2.setConclusion("心电图未见明显异常");
            report2.setSourceSystem("PACS");
            report2.setIsAbnormal(0);
            report2.setStatus("READY");
            report2.setNotified(1);
            reportRepository.save(report2);

            Invoice invoice = new Invoice();
            invoice.setInvoiceNo("INV" + System.currentTimeMillis());
            invoice.setInvoiceCode("123456789012");
            invoice.setInvoiceType("REGISTRATION");
            invoice.setPatientId(patient1.getId());
            invoice.setBusinessId(appt.getId());
            invoice.setBusinessType("APPOINTMENT");
            invoice.setInvoiceDate(LocalDateTime.now());
            invoice.setAmount(doctor1.getRegistrationFee());
            invoice.setContent("内科门诊挂号费");
            invoice.setIssuerName("某某市人民医院");
            invoice.setPayerName(patient1.getName());
            invoice.setCheckCode("ABC12345");
            invoice.setStatus("ISSUED");
            invoice.setVerifyUrl("https://fapiao.chinatax.gov.cn/");
            invoice.setDownloadCount(0);
            invoiceRepository.save(invoice);
        }
    }
}
