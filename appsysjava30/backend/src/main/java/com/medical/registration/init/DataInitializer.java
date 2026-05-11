package com.medical.registration.init;

import com.medical.registration.entity.*;
import com.medical.registration.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initUsers();
        initDepartments();
        initDoctors();
        initSchedules();
    }
    
    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setRoleCode("USER");
            userRole.setRoleName("普通用户");
            userRole.setDescription("普通患者用户");
            roleRepository.save(userRole);
            
            Role adminRole = new Role();
            adminRole.setRoleCode("ADMIN");
            adminRole.setRoleName("管理员");
            adminRole.setDescription("系统管理员");
            roleRepository.save(adminRole);
        }
    }
    
    private void initUsers() {
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("zhangshan");
            user1.setPassword(passwordEncoder.encode("123456"));
            user1.setRealName("张三");
            user1.setGender("男");
            user1.setAge(35);
            user1.setStatus(1);
            user1 = userRepository.save(user1);
            
            User user2 = new User();
            user2.setUsername("lisi");
            user2.setPassword(passwordEncoder.encode("123456"));
            user2.setRealName("李四");
            user2.setGender("女");
            user2.setAge(28);
            user2.setStatus(1);
            userRepository.save(user2);
            
            Role userRole = roleRepository.findByRoleCode("USER").orElse(null);
            if (userRole != null) {
                UserRole ur1 = new UserRole();
                ur1.setUserId(user1.getId());
                ur1.setRoleId(userRole.getId());
                userRoleRepository.save(ur1);
                
                UserRole ur2 = new UserRole();
                ur2.setUserId(user2.getId());
                ur2.setRoleId(userRole.getId());
                userRoleRepository.save(ur2);
            }
        }
    }
    
    private void initDepartments() {
        if (departmentRepository.count() == 0) {
            String[][] depts = {
                {"001", "内科", "内科门诊"},
                {"002", "外科", "外科门诊"},
                {"003", "儿科", "儿科门诊"},
                {"004", "妇科", "妇科门诊"},
                {"005", "口腔科", "口腔科门诊"},
                {"006", "眼科", "眼科门诊"},
                {"007", "皮肤科", "皮肤科门诊"},
                {"008", "中医科", "中医科门诊"}
            };
            
            for (int i = 0; i < depts.length; i++) {
                Department dept = new Department();
                dept.setDeptCode(depts[i][0]);
                dept.setDeptName(depts[i][1]);
                dept.setDescription(depts[i][2]);
                dept.setSortOrder(i + 1);
                dept.setStatus(1);
                departmentRepository.save(dept);
            }
        }
    }
    
    private void initDoctors() {
        if (doctorRepository.count() == 0) {
            Object[][] doctors = {
                {"001", "王建国", "主任医师", "擅长心血管疾病诊治", "冠心病、高血压、心律失常", new BigDecimal("50.00"), 1},
                {"001", "陈晓梅", "副主任医师", "擅长呼吸系统疾病", "肺炎、哮喘、支气管炎", new BigDecimal("35.00"), 2},
                {"002", "李伟明", "主任医师", "擅长普外科手术", "阑尾炎、疝气、胆囊疾病", new BigDecimal("50.00"), 1},
                {"002", "赵志强", "主治医师", "擅长外伤处理", "外伤缝合、骨折处理", new BigDecimal("25.00"), 2},
                {"003", "孙丽娟", "主任医师", "擅长儿科常见病", "小儿感冒、发热、腹泻", new BigDecimal("40.00"), 1},
                {"003", "周小波", "副主任医师", "擅长新生儿疾病", "新生儿护理、黄疸", new BigDecimal("35.00"), 2},
                {"004", "吴雅琳", "主任医师", "擅长妇科疾病", "月经不调、妇科炎症", new BigDecimal("45.00"), 1},
                {"004", "郑美玲", "主治医师", "擅长妇科检查", "常规妇检、孕检", new BigDecimal("25.00"), 2},
                {"005", "钱光明", "副主任医师", "擅长口腔疾病", "龋齿、牙周炎、拔牙", new BigDecimal("35.00"), 1},
                {"006", "冯明亮", "主治医师", "擅长眼科疾病", "近视、白内障、结膜炎", new BigDecimal("25.00"), 1},
                {"007", "韩晓燕", "副主任医师", "擅长皮肤病", "湿疹、皮炎、痤疮", new BigDecimal("35.00"), 1},
                {"008", "徐志远", "主任医师", "擅长中医调理", "中医养生、慢性病调理", new BigDecimal("50.00"), 1}
            };
            
            for (Object[] doc : doctors) {
                Doctor doctor = new Doctor();
                doctor.setDeptCode((String) doc[0]);
                doctor.setName((String) doc[1]);
                doctor.setTitle((String) doc[2]);
                doctor.setIntroduction((String) doc[3]);
                doctor.setSpecialties((String) doc[4]);
                doctor.setRegistrationFee((BigDecimal) doc[5]);
                doctor.setSortOrder((Integer) doc[6]);
                doctor.setStatus(1);
                doctorRepository.save(doctor);
            }
        }
    }
    
    private void initSchedules() {
        if (scheduleRepository.count() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate dayAfter = today.plusDays(2);
            
            String[] timeSlots = {"08:00-09:00", "09:00-10:00", "10:00-11:00", "14:00-15:00", "15:00-16:00", "16:00-17:00"};
            LocalTime[][] times = {
                {LocalTime.of(8, 0), LocalTime.of(9, 0)},
                {LocalTime.of(9, 0), LocalTime.of(10, 0)},
                {LocalTime.of(10, 0), LocalTime.of(11, 0)},
                {LocalTime.of(14, 0), LocalTime.of(15, 0)},
                {LocalTime.of(15, 0), LocalTime.of(16, 0)},
                {LocalTime.of(16, 0), LocalTime.of(17, 0)}
            };
            
            doctorRepository.findAll().forEach(doctor -> {
                for (LocalDate date : Arrays.asList(today, tomorrow, dayAfter)) {
                    for (int i = 0; i < timeSlots.length; i++) {
                        Schedule schedule = new Schedule();
                        schedule.setDoctorId(doctor.getId());
                        schedule.setDeptCode(doctor.getDeptCode());
                        schedule.setScheduleDate(date);
                        schedule.setTimeSlot(timeSlots[i]);
                        schedule.setStartTime(times[i][0]);
                        schedule.setEndTime(times[i][1]);
                        schedule.setTotalCount(10);
                        schedule.setAvailableCount(8 + (int) (Math.random() * 2));
                        schedule.setStatus(1);
                        scheduleRepository.save(schedule);
                    }
                }
            });
        }
    }
}
