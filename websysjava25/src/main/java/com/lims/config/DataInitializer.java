package com.lims.config;

import com.lims.entity.*;
import com.lims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据初始化类
 * 项目启动时自动生成测试数据
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestItemRepository testItemRepository;

    @Autowired
    private TestApplicationRepository testApplicationRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestReportRepository testReportRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void run(String... args) {
        System.out.println("========================================");
        System.out.println("正在初始化测试数据...");
        System.out.println("========================================");

        initDepartments();
        initUsers();
        initPatients();
        initTestItems();
        initDevices();
        initTestApplications();
        initTestResults();
        initTestReports();

        System.out.println("========================================");
        System.out.println("测试数据初始化完成！");
        System.out.println("========================================");
    }

    private void initDepartments() {
        Department dept1 = new Department();
        dept1.setDeptCode("DEPT001");
        dept1.setDeptName("检验科");
        dept1.setDeptType("LABORATORY");
        dept1.setDirector("张主任");
        dept1.setPhone("010-12345678");
        dept1.setDescription("负责全院检验项目");
        departmentRepository.save(dept1);

        Department dept2 = new Department();
        dept2.setDeptCode("DEPT002");
        dept2.setDeptName("影像科");
        dept2.setDeptType("EXAMINATION");
        dept2.setDirector("李主任");
        dept2.setPhone("010-12345679");
        dept2.setDescription("负责全院影像检查项目");
        departmentRepository.save(dept2);

        Department dept3 = new Department();
        dept3.setDeptCode("DEPT003");
        dept3.setDeptName("内科");
        dept3.setDeptType("CLINICAL");
        dept3.setDirector("王主任");
        dept3.setPhone("010-12345680");
        dept3.setDescription("临床科室-内科");
        departmentRepository.save(dept3);

        Department dept4 = new Department();
        dept4.setDeptCode("DEPT004");
        dept4.setDeptName("外科");
        dept4.setDeptType("CLINICAL");
        dept4.setDirector("刘主任");
        dept4.setPhone("010-12345681");
        dept4.setDescription("临床科室-外科");
        departmentRepository.save(dept4);

        System.out.println("科室数据初始化完成，共4个科室");
    }

    private void initUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRealName("管理员");
        admin.setRole("ADMIN");
        admin.setPhone("13800138000");
        admin.setEmail("admin@lims.com");
        userRepository.save(admin);

        User doctor = new User();
        doctor.setUsername("doctor1");
        doctor.setPassword("doctor123");
        doctor.setRealName("陈医生");
        doctor.setDepartmentId(3L);
        doctor.setRole("DOCTOR");
        doctor.setPhone("13800138001");
        doctor.setEmail("doctor1@lims.com");
        userRepository.save(doctor);

        User technician = new User();
        technician.setUsername("tech1");
        technician.setPassword("tech123");
        technician.setRealName("赵技师");
        technician.setDepartmentId(1L);
        technician.setRole("TECHNICIAN");
        technician.setPhone("13800138002");
        technician.setEmail("tech1@lims.com");
        userRepository.save(technician);

        User auditor = new User();
        auditor.setUsername("auditor1");
        auditor.setPassword("auditor123");
        auditor.setRealName("孙审核");
        auditor.setDepartmentId(1L);
        auditor.setRole("AUDITOR");
        auditor.setPhone("13800138003");
        auditor.setEmail("auditor1@lims.com");
        userRepository.save(auditor);

        User patientUser = new User();
        patientUser.setUsername("patient1");
        patientUser.setPassword("patient123");
        patientUser.setRealName("患者周某某");
        patientUser.setRole("PATIENT");
        patientUser.setPhone("13800138004");
        patientUser.setEmail("patient1@lims.com");
        userRepository.save(patientUser);

        System.out.println("用户数据初始化完成，共5个用户");
    }

    private void initPatients() {
        Patient patient1 = new Patient();
        patient1.setPatientNo("PAT001");
        patient1.setPatientName("张三");
        patient1.setGender("MALE");
        patient1.setBirthDate(LocalDate.of(1990, 5, 15));
        patient1.setAge(34);
        patient1.setIdCard("110101199005151234");
        patient1.setPhone("13900139001");
        patient1.setAddress("北京市朝阳区");
        patientRepository.save(patient1);

        Patient patient2 = new Patient();
        patient2.setPatientNo("PAT002");
        patient2.setPatientName("李四");
        patient2.setGender("FEMALE");
        patient2.setBirthDate(LocalDate.of(1985, 8, 20));
        patient2.setAge(39);
        patient2.setIdCard("110101198508205678");
        patient2.setPhone("13900139002");
        patient2.setAddress("北京市海淀区");
        patientRepository.save(patient2);

        Patient patient3 = new Patient();
        patient3.setPatientNo("PAT003");
        patient3.setPatientName("王五");
        patient3.setGender("MALE");
        patient3.setBirthDate(LocalDate.of(1978, 3, 10));
        patient3.setAge(46);
        patient3.setIdCard("110101197803109012");
        patient3.setPhone("13900139003");
        patient3.setAddress("北京市西城区");
        patientRepository.save(patient3);

        System.out.println("患者数据初始化完成，共3个患者");
    }

    private void initTestItems() {
        TestItem item1 = new TestItem();
        item1.setItemCode("ITEM001");
        item1.setItemName("血常规");
        item1.setItemType("LABORATORY");
        item1.setDepartmentId(1L);
        item1.setReferenceValue("WBC:4-10×10^9/L, RBC:4-5.5×10^12/L");
        item1.setUnit("多种");
        item1.setPrice(new BigDecimal("50.00"));
        item1.setEstimatedTime(30);
        item1.setDescription("常规血液检查");
        testItemRepository.save(item1);

        TestItem item2 = new TestItem();
        item2.setItemCode("ITEM002");
        item2.setItemName("肝功能");
        item2.setItemType("LABORATORY");
        item2.setDepartmentId(1L);
        item2.setReferenceValue("ALT:0-40U/L, AST:0-40U/L");
        item2.setUnit("U/L");
        item2.setPrice(new BigDecimal("120.00"));
        item2.setEstimatedTime(60);
        item2.setDescription("肝功能检查");
        testItemRepository.save(item2);

        TestItem item3 = new TestItem();
        item3.setItemCode("ITEM003");
        item3.setItemName("胸部CT");
        item3.setItemType("EXAMINATION");
        item3.setDepartmentId(2L);
        item3.setUnit("次");
        item3.setPrice(new BigDecimal("300.00"));
        item3.setEstimatedTime(120);
        item3.setDescription("胸部CT平扫");
        testItemRepository.save(item3);

        TestItem item4 = new TestItem();
        item4.setItemCode("ITEM004");
        item4.setItemName("尿常规");
        item4.setItemType("LABORATORY");
        item4.setDepartmentId(1L);
        item4.setReferenceValue("PH:5-8, PRO:阴性");
        item4.setUnit("多种");
        item4.setPrice(new BigDecimal("25.00"));
        item4.setEstimatedTime(20);
        item4.setDescription("尿液常规检查");
        testItemRepository.save(item4);

        System.out.println("检验检查项目初始化完成，共4个项目");
    }

    private void initDevices() {
        Device device1 = new Device();
        device1.setDeviceCode("DEV001");
        device1.setDeviceName("全自动生化分析仪");
        device1.setDeviceType("LABORATORY");
        device1.setDeviceModel("BS-800");
        device1.setManufacturer("迈瑞医疗");
        device1.setDepartmentId(1L);
        device1.setInterfaceProtocol("HL7");
        device1.setInterfaceAddress("192.168.1.100:8080");
        device1.setStatus("ONLINE");
        deviceRepository.save(device1);

        Device device2 = new Device();
        device2.setDeviceCode("DEV002");
        device2.setDeviceName("全自动血细胞分析仪");
        device2.setDeviceType("LABORATORY");
        device2.setDeviceModel("BC-6800");
        device2.setManufacturer("迈瑞医疗");
        device2.setDepartmentId(1L);
        device2.setInterfaceProtocol("HL7");
        device2.setInterfaceAddress("192.168.1.101:8080");
        device2.setStatus("ONLINE");
        deviceRepository.save(device2);

        Device device3 = new Device();
        device3.setDeviceCode("DEV003");
        device3.setDeviceName("CT扫描仪");
        device3.setDeviceType("EXAMINATION");
        device3.setDeviceModel("Revolution");
        device3.setManufacturer("GE医疗");
        device3.setDepartmentId(2L);
        device3.setInterfaceProtocol("DICOM");
        device3.setInterfaceAddress("192.168.1.200:104");
        device3.setStatus("ONLINE");
        deviceRepository.save(device3);

        System.out.println("设备数据初始化完成，共3个设备");
    }

    private void initTestApplications() {
        TestApplication app1 = new TestApplication();
        app1.setApplicationNo("APP20240101001");
        app1.setPatientId(1L);
        app1.setApplyDepartmentId(3L);
        app1.setApplyDoctorId(2L);
        app1.setExecuteDepartmentId(1L);
        app1.setItemId(1L);
        app1.setApplicationType("LABORATORY");
        app1.setClinicalDiagnosis("上呼吸道感染");
        app1.setRemarks("加急");
        app1.setStatus("PUBLISHED");
        app1.setAssignedTechnicianId(3L);
        app1.setConfirmedBy(3L);
        testApplicationRepository.save(app1);

        TestApplication app2 = new TestApplication();
        app2.setApplicationNo("APP20240101002");
        app2.setPatientId(2L);
        app2.setApplyDepartmentId(4L);
        app2.setApplyDoctorId(2L);
        app2.setExecuteDepartmentId(1L);
        app2.setItemId(2L);
        app2.setApplicationType("LABORATORY");
        app2.setClinicalDiagnosis("肝功能异常");
        app2.setStatus("PROCESSING");
        app2.setAssignedTechnicianId(3L);
        testApplicationRepository.save(app2);

        TestApplication app3 = new TestApplication();
        app3.setApplicationNo("APP20240101003");
        app3.setPatientId(3L);
        app3.setApplyDepartmentId(3L);
        app3.setApplyDoctorId(2L);
        app3.setExecuteDepartmentId(2L);
        app3.setItemId(3L);
        app3.setApplicationType("EXAMINATION");
        app3.setClinicalDiagnosis("咳嗽待查");
        app3.setStatus("CONFIRMED");
        testApplicationRepository.save(app3);

        TestApplication app4 = new TestApplication();
        app4.setApplicationNo("APP20240101004");
        app4.setPatientId(1L);
        app4.setApplyDepartmentId(3L);
        app4.setApplyDoctorId(2L);
        app4.setExecuteDepartmentId(1L);
        app4.setItemId(4L);
        app4.setApplicationType("LABORATORY");
        app4.setClinicalDiagnosis("尿路感染待排");
        app4.setStatus("PENDING");
        testApplicationRepository.save(app4);

        System.out.println("检验检查申请初始化完成，共4个申请");
    }

    private void initTestResults() {
        TestResult result1 = new TestResult();
        result1.setApplicationId(1L);
        result1.setItemId(1L);
        result1.setTechnicianId(3L);
        result1.setResultValue("WBC:6.5, RBC:4.8");
        result1.setAbnormalFlag("NORMAL");
        result1.setResultDescription("白细胞、红细胞计数均在正常范围内，各项指标正常");
        result1.setDeviceCode("DEV002");
        result1.setResultSource("DEVICE");
        result1.setOperationNotes("样本合格，检测顺利");
        testResultRepository.save(result1);

        TestResult result2 = new TestResult();
        result2.setApplicationId(2L);
        result2.setItemId(2L);
        result2.setTechnicianId(3L);
        result2.setResultSource("MANUAL");
        result2.setOperationNotes("检测中");
        testResultRepository.save(result2);

        System.out.println("检验检查结果初始化完成，共2个结果");
    }

    private void initTestReports() {
        TestReport report1 = new TestReport();
        report1.setReportNo("RPT20240101001");
        report1.setApplicationId(1L);
        report1.setResultId(1L);
        report1.setReportTitle("血常规检验报告");
        report1.setReportContent("白细胞(WBC):6.5×10^9/L（参考值4-10×10^9/L）\n红细胞(RBC):4.8×10^12/L（参考值4-5.5×10^12/L）\n血红蛋白(Hb):145g/L（参考值120-160g/L）\n血小板(PLT):220×10^9/L（参考值100-300×10^9/L）");
        report1.setConclusion("各项血常规指标均在正常范围内");
        report1.setSuggestion("建议定期复查，如有不适及时就医");
        report1.setStatus("PUBLISHED");
        report1.setCreatedBy(3L);
        report1.setFirstAuditorId(4L);
        report1.setFirstAuditOpinion("审核通过，结果可信");
        report1.setSecondAuditorId(4L);
        report1.setSecondAuditOpinion("同意发布");
        report1.setPublishedBy(4L);
        report1.setPatientViewed(true);
        testReportRepository.save(report1);

        System.out.println("检验检查报告初始化完成，共1个报告");
    }
}
