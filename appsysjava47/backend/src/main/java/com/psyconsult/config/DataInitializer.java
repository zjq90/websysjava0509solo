package com.psyconsult.config;

import com.psyconsult.entity.*;
import com.psyconsult.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   CounselorRepository counselorRepository,
                                   EmergencyContactRepository emergencyContactRepository,
                                   ResourceRepository resourceRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = new User();
                user1.setUsername("user1");
                user1.setPassword(passwordEncoder.encode("123456"));
                user1.setPhone("13800138001");
                user1.setEmail("user1@example.com");
                user1.setNickname("小明");
                user1.setIsAnonymous(false);
                user1.setIsVerified(true);
                user1.setEncryptRecords(true);
                user1.setElderMode(false);
                user1.setRole("USER");
                user1.setEnabled(true);
                user1.setCreateTime(LocalDateTime.now());
                userRepository.save(user1);

                User user2 = new User();
                user2.setUsername("user2");
                user2.setPassword(passwordEncoder.encode("123456"));
                user2.setPhone("13800138002");
                user2.setEmail("user2@example.com");
                user2.setNickname("匿名用户");
                user2.setIsAnonymous(true);
                user2.setIsVerified(false);
                user2.setEncryptRecords(true);
                user2.setElderMode(true);
                user2.setRole("USER");
                user2.setEnabled(true);
                user2.setCreateTime(LocalDateTime.now());
                userRepository.save(user2);

                System.out.println("测试用户已创建");
            }

            if (counselorRepository.count() == 0) {
                Counselor counselor1 = new Counselor();
                counselor1.setUsername("counselor1");
                counselor1.setPassword(passwordEncoder.encode("123456"));
                counselor1.setName("张医生");
                counselor1.setPhone("13900139001");
                counselor1.setEmail("zhang@psy.com");
                counselor1.setQualification("国家二级心理咨询师，心理学博士");
                counselor1.setIntroduction("擅长抑郁、焦虑情绪管理，婚姻家庭咨询，青少年心理问题。15年临床经验，帮助数千来访者走出心理困境。");
                counselor1.setSpecialties(Arrays.asList("抑郁", "焦虑", "情绪管理", "婚姻家庭"));
                counselor1.setLanguages(Arrays.asList("中文", "英语"));
                counselor1.setPricePerHour(new BigDecimal("300.00"));
                counselor1.setExperienceYears(15);
                counselor1.setRating(new BigDecimal("4.8"));
                counselor1.setTotalConsultations(580);
                counselor1.setIsAvailable(true);
                counselor1.setRole("COUNSELOR");
                counselor1.setEnabled(true);
                counselor1.setCreateTime(LocalDateTime.now());
                counselorRepository.save(counselor1);

                Counselor counselor2 = new Counselor();
                counselor2.setUsername("counselor2");
                counselor2.setPassword(passwordEncoder.encode("123456"));
                counselor2.setName("李咨询师");
                counselor2.setPhone("13900139002");
                counselor2.setEmail("li@psy.com");
                counselor2.setQualification("注册心理师，临床心理学硕士");
                counselor2.setIntroduction("专注职场压力管理、人际关系、个人成长领域。8年企业EAP经验，擅长认知行为疗法。");
                counselor2.setSpecialties(Arrays.asList("职场压力", "人际关系", "个人成长", "职业规划"));
                counselor2.setLanguages(Arrays.asList("中文"));
                counselor2.setPricePerHour(new BigDecimal("250.00"));
                counselor2.setExperienceYears(8);
                counselor2.setRating(new BigDecimal("4.6"));
                counselor2.setTotalConsultations(320);
                counselor2.setIsAvailable(true);
                counselor2.setRole("COUNSELOR");
                counselor2.setEnabled(true);
                counselor2.setCreateTime(LocalDateTime.now());
                counselorRepository.save(counselor2);

                Counselor counselor3 = new Counselor();
                counselor3.setUsername("counselor3");
                counselor3.setPassword(passwordEncoder.encode("123456"));
                counselor3.setName("王教授");
                counselor3.setPhone("13900139003");
                counselor3.setEmail("wang@psy.com");
                counselor3.setQualification("心理学教授，博士生导师");
                counselor3.setIntroduction("儿童青少年心理专家，专注于亲子关系、学业压力、青春期心理问题研究20余年。");
                counselor3.setSpecialties(Arrays.asList("亲子关系", "青少年心理", "学业压力", "青春期"));
                counselor3.setLanguages(Arrays.asList("中文", "日语"));
                counselor3.setPricePerHour(new BigDecimal("500.00"));
                counselor3.setExperienceYears(22);
                counselor3.setRating(new BigDecimal("4.9"));
                counselor3.setTotalConsultations(890);
                counselor3.setIsAvailable(true);
                counselor3.setRole("COUNSELOR");
                counselor3.setEnabled(true);
                counselor3.setCreateTime(LocalDateTime.now());
                counselorRepository.save(counselor3);

                System.out.println("测试咨询师已创建");
            }

            if (emergencyContactRepository.count() == 0) {
                EmergencyContact contact1 = new EmergencyContact();
                contact1.setName("全国心理援助热线");
                contact1.setPhone("400-161-9995");
                contact1.setType("热线");
                contact1.setIsHotline(true);
                contact1.setSortOrder(1);
                contact1.setEnabled(true);
                emergencyContactRepository.save(contact1);

                EmergencyContact contact2 = new EmergencyContact();
                contact2.setName("北京心理危机研究与干预中心");
                contact2.setPhone("010-82951332");
                contact2.setType("危机干预");
                contact2.setIsHotline(true);
                contact2.setSortOrder(2);
                contact2.setEnabled(true);
                emergencyContactRepository.save(contact2);

                EmergencyContact contact3 = new EmergencyContact();
                contact3.setName("希望24热线");
                contact3.setPhone("400-161-9995");
                contact3.setType("生命教育");
                contact3.setIsHotline(true);
                contact3.setSortOrder(3);
                contact3.setEnabled(true);
                emergencyContactRepository.save(contact3);

                EmergencyContact contact4 = new EmergencyContact();
                contact4.setName("北京市心理卫生协会");
                contact4.setPhone("010-62038088");
                contact4.setAddress("北京市海淀区");
                contact4.setType("机构");
                contact4.setIsHotline(false);
                contact4.setSortOrder(4);
                contact4.setEnabled(true);
                emergencyContactRepository.save(contact4);

                System.out.println("紧急联系人已创建");
            }

            if (resourceRepository.count() == 0) {
                Resource resource1 = new Resource();
                resource1.setType("AUDIO");
                resource1.setTitle("正念冥想：缓解焦虑");
                resource1.setContent("通过正念冥想练习，帮助您缓解焦虑情绪，找回内心平静。适合每天15-20分钟练习。");
                resource1.setCategory("冥想");
                resource1.setTags("焦虑,冥想,放松");
                resource1.setDurationSeconds(900);
                resource1.setViewCount(1256);
                resource1.setLikeCount(328);
                resource1.setEnabled(true);
                resource1.setCreateTime(LocalDateTime.now());
                resourceRepository.save(resource1);

                Resource resource2 = new Resource();
                resource2.setType("AUDIO");
                resource2.setTitle("深呼吸放松训练");
                resource2.setContent("学习正确的深呼吸技巧，快速缓解紧张情绪。建议在感到压力时练习。");
                resource2.setCategory("放松");
                resource2.setTags("深呼吸,放松,压力");
                resource2.setDurationSeconds(480);
                resource2.setViewCount(2341);
                resource2.setLikeCount(567);
                resource2.setEnabled(true);
                resource2.setCreateTime(LocalDateTime.now());
                resourceRepository.save(resource2);

                Resource resource3 = new Resource();
                resource3.setType("ARTICLE");
                resource3.setTitle("如何识别抑郁信号");
                resource3.setContent("本文详细介绍抑郁症的早期症状和识别方法，帮助您及时发现问题并寻求帮助。");
                resource3.setCategory("科普");
                resource3.setTags("抑郁,心理健康,科普");
                resource3.setViewCount(5678);
                resource3.setLikeCount(892);
                resource3.setEnabled(true);
                resource3.setCreateTime(LocalDateTime.now());
                resourceRepository.save(resource3);

                Resource resource4 = new Resource();
                resource4.setType("ARTICLE");
                resource4.setTitle("职场压力管理10招");
                resource4.setContent("实用的职场压力管理技巧，帮助您在工作中保持良好的心理状态。");
                resource4.setCategory("职场");
                resource4.setTags("职场,压力,管理");
                resource4.setViewCount(3456);
                resource4.setLikeCount(623);
                resource4.setEnabled(true);
                resource4.setCreateTime(LocalDateTime.now());
                resourceRepository.save(resource4);

                Resource resource5 = new Resource();
                resource5.setType("AUDIO");
                resource5.setTitle("睡前冥想：改善睡眠");
                resource5.setContent("专为改善睡眠质量设计的冥想引导，帮助您快速入睡，提高睡眠质量。");
                resource5.setCategory("睡眠");
                resource5.setTags("睡眠,冥想,放松");
                resource5.setDurationSeconds(1200);
                resource5.setViewCount(1892);
                resource5.setLikeCount(456);
                resource5.setEnabled(true);
                resource5.setCreateTime(LocalDateTime.now());
                resourceRepository.save(resource5);

                System.out.println("心理健康资源已创建");
            }
        };
    }
}
