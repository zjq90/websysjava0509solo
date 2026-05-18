package com.psyconsult.config;

import com.psyconsult.entity.*;
import com.psyconsult.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounselorRepository counselorRepository;

    @Autowired
    private CaseTagRepository caseTagRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始初始化测试数据...");

        if (userRepository.count() == 0) {
            initUsers();
        }

        if (counselorRepository.count() == 0) {
            initCounselors();
        }

        if (caseTagRepository.count() == 0) {
            initCaseTags();
        }

        if (courseRepository.count() == 0) {
            initCourses();
        }

        if (scheduleRepository.count() == 0) {
            initSchedules();
        }

        System.out.println("测试数据初始化完成！");
    }

    private void initUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("123456");
        user1.setRealName("张小明");
        user1.setPhone("13800138001");
        user1.setEmail("user1@example.com");
        user1.setGender("男");
        user1.setStatus(1);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("123456");
        user2.setRealName("李小红");
        user2.setPhone("13800138002");
        user2.setEmail("user2@example.com");
        user2.setGender("女");
        user2.setStatus(1);
        userRepository.save(user2);
    }

    private void initCounselors() {
        Counselor counselor1 = new Counselor();
        counselor1.setUserId(1L);
        counselor1.setName("王医生");
        counselor1.setTitle("主任医师");
        counselor1.setQualification("国家二级心理咨询师");
        counselor1.setBio("从事心理咨询工作15年，擅长认知行为疗法、精神分析");
        counselor1.setExpertise("抑郁症,焦虑症,强迫症,婚恋情感");
        counselor1.setConsultationFee(new BigDecimal("500.00"));
        counselor1.setExperienceYears(15);
        counselor1.setIsSenior(true);
        counselor1.setAccepting(true);
        counselor1.setStatus(1);
        counselorRepository.save(counselor1);

        Counselor counselor2 = new Counselor();
        counselor2.setUserId(2L);
        counselor2.setName("李医生");
        counselor2.setTitle("副主任医师");
        counselor2.setQualification("国家二级心理咨询师");
        counselor2.setBio("从事心理咨询工作10年，擅长家庭治疗、青少年心理问题");
        counselor2.setExpertise("青少年心理,家庭关系,职场压力");
        counselor2.setConsultationFee(new BigDecimal("400.00"));
        counselor2.setExperienceYears(10);
        counselor2.setIsSenior(false);
        counselor2.setAccepting(true);
        counselor2.setStatus(1);
        counselorRepository.save(counselor2);
    }

    private void initCaseTags() {
        CaseTag tag1 = new CaseTag();
        tag1.setName("抑郁");
        tag1.setCategory("emotion");
        tag1.setDescription("抑郁症、抑郁情绪");
        tag1.setStatus(1);
        caseTagRepository.save(tag1);

        CaseTag tag2 = new CaseTag();
        tag2.setName("焦虑");
        tag2.setCategory("emotion");
        tag2.setDescription("焦虑症、焦虑情绪");
        tag2.setStatus(1);
        caseTagRepository.save(tag2);

        CaseTag tag3 = new CaseTag();
        tag3.setName("恐慌");
        tag3.setCategory("emotion");
        tag3.setDescription("恐慌症、惊恐发作");
        tag3.setStatus(1);
        caseTagRepository.save(tag3);

        CaseTag tag4 = new CaseTag();
        tag4.setName("婚恋情感");
        tag4.setCategory("relationship");
        tag4.setDescription("恋爱、婚姻、情感问题");
        tag4.setStatus(1);
        caseTagRepository.save(tag4);

        CaseTag tag5 = new CaseTag();
        tag5.setName("职场压力");
        tag5.setCategory("work");
        tag5.setDescription("工作压力、职业倦怠");
        tag5.setStatus(1);
        caseTagRepository.save(tag5);
    }

    private void initCourses() {
        Course course1 = new Course();
        course1.setTitle("认知行为疗法(CBT)入门与实践");
        course1.setDescription("系统学习认知行为疗法的基本理论和实践技巧，掌握常见心理问题的CBT干预方法。");
        course1.setInstructor("王医生");
        course1.setCategory("认知行为");
        course1.setTotalDuration(480);
        course1.setLessonCount(12);
        course1.setStatus(1);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setTitle("抑郁症的识别与干预");
        course2.setDescription("学习抑郁症的临床表现、诊断标准、干预策略和危机处理方法。");
        course2.setInstructor("李医生");
        course2.setCategory("抑郁症");
        course2.setTotalDuration(360);
        course2.setLessonCount(8);
        course2.setStatus(1);
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setTitle("焦虑障碍的心理咨询实务");
        course3.setDescription("深入学习各种焦虑障碍（广泛性焦虑、惊恐障碍、社交焦虑等）的咨询技术。");
        course3.setInstructor("王医生");
        course3.setCategory("焦虑症");
        course3.setTotalDuration(420);
        course3.setLessonCount(10);
        course3.setStatus(1);
        courseRepository.save(course3);
    }

    private void initSchedules() {
        LocalDate today = LocalDate.now();
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            
            Schedule schedule1 = new Schedule();
            schedule1.setCounselorId(1L);
            schedule1.setDate(date);
            schedule1.setStartTime(LocalTime.of(9, 0));
            schedule1.setEndTime(LocalTime.of(10, 0));
            schedule1.setMaxAppointments(1);
            schedule1.setCurrentAppointments(0);
            schedule1.setIsAvailable(true);
            schedule1.setStatus(1);
            scheduleRepository.save(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setCounselorId(1L);
            schedule2.setDate(date);
            schedule2.setStartTime(LocalTime.of(14, 0));
            schedule2.setEndTime(LocalTime.of(15, 0));
            schedule2.setMaxAppointments(1);
            schedule2.setCurrentAppointments(0);
            schedule2.setIsAvailable(true);
            schedule2.setStatus(1);
            scheduleRepository.save(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setCounselorId(2L);
            schedule3.setDate(date);
            schedule3.setStartTime(LocalTime.of(10, 0));
            schedule3.setEndTime(LocalTime.of(11, 0));
            schedule3.setMaxAppointments(1);
            schedule3.setCurrentAppointments(0);
            schedule3.setIsAvailable(true);
            schedule3.setStatus(1);
            scheduleRepository.save(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setCounselorId(2L);
            schedule4.setDate(date);
            schedule4.setStartTime(LocalTime.of(15, 0));
            schedule4.setEndTime(LocalTime.of(16, 0));
            schedule4.setMaxAppointments(1);
            schedule4.setCurrentAppointments(0);
            schedule4.setIsAvailable(true);
            schedule4.setStatus(1);
            scheduleRepository.save(schedule4);
        }
    }
}
