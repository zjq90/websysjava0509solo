package com.hospital.config;

import com.hospital.entity.*;
import com.hospital.repository.*;
import com.hospital.util.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 数据初始化器
 * 系统启动时自动创建测试数据
 * 
 * @author hospital
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private AdmissionRepository admissionRepository;
    
    @Autowired
    private HospitalFeeRepository hospitalFeeRepository;
    
    @Autowired
    private DepositPaymentRepository depositPaymentRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void run(String... args) {
        if (permissionRepository.count() == 0) {
            initPermissions();
        }
        if (roleRepository.count() == 0) {
            initRoles();
        }
        if (userRepository.count() == 0) {
            initUsers();
        }
        if (departmentRepository.count() == 0) {
            initDepartments();
        }
        if (admissionRepository.count() == 0) {
            initTestData();
        }
        System.out.println("=========================================");
        System.out.println("    测试数据初始化完成！");
        System.out.println("    测试账号：");
        System.out.println("    管理员: admin / 123456");
        System.out.println("    患者: patient1 / 123456");
        System.out.println("    医生: doctor1 / 123456");
        System.out.println("=========================================");
    }

    private void initPermissions() {
        List<Permission> permissions = Arrays.asList(
            createPermission("用户管理", "user:view", "/user", "GET", "查看用户列表"),
            createPermission("用户新增", "user:add", "/user", "POST", "新增用户"),
            createPermission("用户修改", "user:edit", "/user", "PUT", "修改用户"),
            createPermission("用户删除", "user:delete", "/user", "DELETE", "删除用户"),
            
            createPermission("入院预约管理", "admission:view", "/admission", "GET", "查看入院预约"),
            createPermission("入院预约申请", "admission:add", "/admission", "POST", "提交入院申请"),
            createPermission("入院预约审核", "admission:audit", "/admission/audit", "POST", "审核入院申请"),
            createPermission("入院预约取消", "admission:cancel", "/admission/cancel", "POST", "取消入院申请"),
            
            createPermission("费用查询", "fee:view", "/hospital-fee", "GET", "查看住院费用"),
            createPermission("费用录入", "fee:add", "/hospital-fee", "POST", "录入住院费用"),
            
            createPermission("押金管理", "deposit:view", "/deposit", "GET", "查看押金记录"),
            createPermission("押金补缴", "deposit:add", "/deposit", "POST", "补缴住院押金"),
            
            createPermission("审计日志", "audit:view", "/audit-log", "GET", "查看审计日志"),
            
            createPermission("科室管理", "dept:view", "/department", "GET", "查看科室列表"),
            createPermission("科室管理", "dept:edit", "/department", "PUT", "修改科室信息")
        );
        permissionRepository.saveAll(permissions);
    }

    private Permission createPermission(String name, String code, String path, String method, String desc) {
        Permission p = new Permission();
        p.setPermissionName(name);
        p.setPermissionCode(code);
        p.setPath(path);
        p.setMethod(method);
        p.setDescription(desc);
        return p;
    }

    private void initRoles() {
        List<Permission> allPermissions = permissionRepository.findAll();
        
        Role adminRole = new Role();
        adminRole.setRoleName("系统管理员");
        adminRole.setRoleCode("ADMIN");
        adminRole.setDescription("拥有系统所有权限");
        adminRole.setPermissions(new HashSet<>(allPermissions));
        roleRepository.save(adminRole);
        
        List<String> patientPermissionCodes = Arrays.asList(
            "admission:view", "admission:add", "admission:cancel",
            "fee:view", "deposit:view", "deposit:add"
        );
        Set<Permission> patientPermissions = new HashSet<>();
        for (Permission p : allPermissions) {
            if (patientPermissionCodes.contains(p.getPermissionCode())) {
                patientPermissions.add(p);
            }
        }
        
        Role patientRole = new Role();
        patientRole.setRoleName("患者");
        patientRole.setRoleCode("PATIENT");
        patientRole.setDescription("患者角色，可预约入院、查询费用、补缴押金");
        patientRole.setPermissions(patientPermissions);
        roleRepository.save(patientRole);
        
        List<String> doctorPermissionCodes = Arrays.asList(
            "admission:view", "admission:audit",
            "fee:view", "fee:add",
            "deposit:view"
        );
        Set<Permission> doctorPermissions = new HashSet<>();
        for (Permission p : allPermissions) {
            if (doctorPermissionCodes.contains(p.getPermissionCode())) {
                doctorPermissions.add(p);
            }
        }
        
        Role doctorRole = new Role();
        doctorRole.setRoleName("医生");
        doctorRole.setRoleCode("DOCTOR");
        doctorRole.setDescription("医生角色，可审核入院、录入费用");
        doctorRole.setPermissions(doctorPermissions);
        roleRepository.save(doctorRole);
    }

    private void initUsers() {
        Role adminRole = roleRepository.findByRoleCode("ADMIN").orElse(null);
        Role patientRole = roleRepository.findByRoleCode("PATIENT").orElse(null);
        Role doctorRole = roleRepository.findByRoleCode("DOCTOR").orElse(null);
        
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRealName("系统管理员");
        admin.setPhone(SM4Util.encryptSensitive("13800000000"));
        admin.setIdCard(SM4Util.encryptSensitive("110101199001010001"));
        admin.setStatus(1);
        admin.setCreateBy("system");
        if (adminRole != null) {
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
        }
        userRepository.save(admin);
        
        User doctor1 = new User();
        doctor1.setUsername("doctor1");
        doctor1.setPassword(passwordEncoder.encode("123456"));
        doctor1.setRealName("李医生");
        doctor1.setPhone(SM4Util.encryptSensitive("13900000001"));
        doctor1.setIdCard(SM4Util.encryptSensitive("110101198505050001"));
        doctor1.setGender("男");
        doctor1.setStatus(1);
        doctor1.setCreateBy("system");
        if (doctorRole != null) {
            doctor1.setRoles(new HashSet<>(Arrays.asList(doctorRole)));
        }
        userRepository.save(doctor1);
        
        User patient1 = new User();
        patient1.setUsername("patient1");
        patient1.setPassword(passwordEncoder.encode("123456"));
        patient1.setRealName("张伟");
        patient1.setPhone(SM4Util.encryptSensitive("13700000001"));
        patient1.setIdCard(SM4Util.encryptSensitive("110101199003031234"));
        patient1.setGender("男");
        patient1.setAge(35);
        patient1.setPatientNo("P2024001");
        patient1.setStatus(1);
        patient1.setCreateBy("system");
        if (patientRole != null) {
            patient1.setRoles(new HashSet<>(Arrays.asList(patientRole)));
        }
        userRepository.save(patient1);
        
        User patient2 = new User();
        patient2.setUsername("patient2");
        patient2.setPassword(passwordEncoder.encode("123456"));
        patient2.setRealName("李芳");
        patient2.setPhone(SM4Util.encryptSensitive("13700000002"));
        patient2.setIdCard(SM4Util.encryptSensitive("110101198808084321"));
        patient2.setGender("女");
        patient2.setAge(37);
        patient2.setPatientNo("P2024002");
        patient2.setStatus(1);
        patient2.setCreateBy("system");
        if (patientRole != null) {
            patient2.setRoles(new HashSet<>(Arrays.asList(patientRole)));
        }
        userRepository.save(patient2);
    }

    private void initDepartments() {
        List<Department> departments = Arrays.asList(
            createDepartment("内科", "NEIKE", "内科", 50, 15, "王医生"),
            createDepartment("外科", "WAIKE", "外科", 40, 10, "赵医生"),
            createDepartment("妇产科", "FUCHAN", "其他", 30, 8, "陈医生"),
            createDepartment("儿科", "ERKE", "其他", 25, 12, "刘医生"),
            createDepartment("骨科", "GUKE", "外科", 35, 5, "孙医生"),
            createDepartment("神经内科", "SHENJING", "内科", 20, 3, "周医生")
        );
        departmentRepository.saveAll(departments);
    }

    private Department createDepartment(String name, String code, String type, int total, int available, String head) {
        Department d = new Department();
        d.setDeptName(name);
        d.setDeptCode(code);
        d.setDeptType(type);
        d.setTotalBeds(total);
        d.setAvailableBeds(available);
        d.setHeadDoctor(head);
        d.setLocation("门诊楼" + (int)(Math.random() * 10 + 1) + "层");
        d.setPhone("010-" + (int)(Math.random() * 90000000 + 10000000));
        d.setStatus(1);
        d.setSortOrder((int)(Math.random() * 100));
        return d;
    }

    private void initTestData() {
        List<User> patients = userRepository.findAll();
        User patient1 = patients.stream().filter(u -> "patient1".equals(u.getUsername())).findFirst().orElse(null);
        User patient2 = patients.stream().filter(u -> "patient2".equals(u.getUsername())).findFirst().orElse(null);
        User doctor1 = patients.stream().filter(u -> "doctor1".equals(u.getUsername())).findFirst().orElse(null);
        
        if (patient1 != null) {
            createAdmissionWithFees(patient1, "NEIKE", "内科", LocalDate.now().minusDays(5), 0, doctor1);
            createAdmissionWithFees(patient1, "WAIKE", "外科", LocalDate.now().plusDays(3), 1, doctor1);
        }
        
        if (patient2 != null) {
            createAdmissionWithFees(patient2, "FUCHAN", "妇产科", LocalDate.now().minusDays(10), 2, doctor1);
        }
    }

    private void createAdmissionWithFees(User patient, String deptCode, String deptName, LocalDate admissionDate, int status, User doctor) {
        Admission admission = new Admission();
        admission.setApplicationNo("AP" + System.currentTimeMillis() + (int)(Math.random() * 1000));
        admission.setUserId(patient.getId());
        admission.setPatientName(patient.getRealName());
        admission.setDeptCode(deptCode);
        admission.setDeptName(deptName);
        admission.setWardType(status == 2 ? "普通病房" : "待分配");
        admission.setExpectBedNo("A-0" + (int)(Math.random() * 20 + 1));
        admission.setActualBedNo(status >= 1 ? "B-0" + (int)(Math.random() * 20 + 1) : null);
        admission.setAdmissionDate(admissionDate);
        admission.setDiagnosis(getRandomDiagnosis(deptName));
        admission.setDoctorId(doctor != null ? doctor.getId() : null);
        admission.setDoctorName(doctor != null ? doctor.getRealName() : "李医生");
        admission.setStatus(status);
        admission.setAuditUserId(doctor != null ? doctor.getId() : null);
        admission.setAuditUserName(doctor != null ? doctor.getRealName() : "李医生");
        admission.setAuditTime(LocalDateTime.now().minusDays(2));
        if (status >= 2) {
            admission.setActualAdmissionTime(admissionDate.atStartOfDay().plusHours(9));
        }
        if (status == 3) {
            admission.setDischargeTime(LocalDateTime.now());
        }
        admission.setDepositAmount(new BigDecimal("2000"));
        admission.setCreateBy("patient");
        admissionRepository.save(admission);
        
        if (status >= 1) {
            createFeesForAdmission(admission, patient);
            createDepositPayment(admission, patient);
        }
    }

    private String getRandomDiagnosis(String deptName) {
        Map<String, String[]> diagnosisMap = new HashMap<>();
        diagnosisMap.put("内科", new String[]{"高血压", "糖尿病", "冠心病", "支气管炎"});
        diagnosisMap.put("外科", new String[]{"阑尾炎", "胆囊炎", "腹股沟疝", "脂肪瘤"});
        diagnosisMap.put("妇产科", new String[]{"先兆流产", "异位妊娠", "子宫肌瘤", "卵巢囊肿"});
        diagnosisMap.put("儿科", new String[]{"急性支气管炎", "小儿肺炎", "小儿腹泻", "高热惊厥"});
        diagnosisMap.put("骨科", new String[]{"骨折", "腰椎间盘突出", "关节炎", "骨质疏松"});
        diagnosisMap.put("神经内科", new String[]{"脑梗塞", "偏头痛", "癫痫", "帕金森病"});
        
        String[] options = diagnosisMap.getOrDefault(deptName, new String[]{"待进一步检查"});
        return options[(int)(Math.random() * options.length)];
    }

    private void createFeesForAdmission(Admission admission, User patient) {
        LocalDate startDate = admission.getAdmissionDate();
        int days = (int)(Math.random() * 10 + 3);
        
        String[] feeTypes = {"药费", "检查费", "治疗费", "护理费", "床位费"};
        String[][] feeItems = {
            {"阿莫西林胶囊", "头孢呋辛酯片", "布洛芬缓释片", "奥美拉唑肠溶胶囊"},
            {"血常规检查", "肝功能检查", "肾功能检查", "心电图检查"},
            {"静脉输液", "雾化吸入", "理疗", "换药"},
            {"一级护理", "二级护理", "三级护理", "特殊护理"},
            {"床位费", "空调费", "水电费"}
        };
        double[][] feePrices = {
            {25.5, 45.0, 18.8, 32.0},
            {35.0, 85.0, 65.0, 25.0},
            {50.0, 30.0, 80.0, 25.0},
            {80.0, 50.0, 30.0, 150.0},
            {120.0, 20.0, 15.0}
        };
        
        for (int i = 0; i < days; i++) {
            LocalDate feeDate = startDate.plusDays(i);
            int feeCount = (int)(Math.random() * 3 + 2);
            for (int j = 0; j < feeCount; j++) {
                int typeIndex = (int)(Math.random() * feeTypes.length);
                int itemIndex = (int)(Math.random() * feeItems[typeIndex].length);
                
                HospitalFee fee = new HospitalFee();
                fee.setAdmissionId(admission.getId());
                fee.setUserId(patient.getId());
                fee.setPatientName(patient.getRealName());
                fee.setApplicationNo(admission.getApplicationNo());
                fee.setFeeDate(feeDate);
                fee.setFeeType(feeTypes[typeIndex]);
                fee.setItemName(feeItems[typeIndex][itemIndex]);
                fee.setItemDesc("常规" + feeTypes[typeIndex]);
                BigDecimal price = new BigDecimal(feePrices[typeIndex][itemIndex]);
                fee.setUnitPrice(price);
                int quantity = (int)(Math.random() * 3 + 1);
                fee.setQuantity(quantity);
                fee.setAmount(price.multiply(new BigDecimal(quantity)));
                fee.setDoctorName(admission.getDoctorName());
                fee.setExecuteDept(admission.getDeptName());
                fee.setSettled(admission.getStatus() == 3 ? 1 : 0);
                if (fee.getSettled() == 1) {
                    fee.setSettleTime(LocalDateTime.now());
                }
                fee.setCreateBy("system");
                hospitalFeeRepository.save(fee);
            }
        }
    }

    private void createDepositPayment(Admission admission, User patient) {
        DepositPayment payment = new DepositPayment();
        payment.setOrderNo("DP" + System.currentTimeMillis() + (int)(Math.random() * 1000));
        payment.setAdmissionId(admission.getId());
        payment.setUserId(patient.getId());
        payment.setPatientName(patient.getRealName());
        payment.setAmount(new BigDecimal("2000"));
        payment.setPaymentMethod("微信");
        payment.setTransactionId("WX" + System.currentTimeMillis());
        payment.setStatus(2);
        payment.setPaymentTime(LocalDateTime.now().minusDays(3));
        payment.setRemark("住院押金补缴");
        payment.setCreateBy(patient.getRealName());
        depositPaymentRepository.save(payment);
    }
}
