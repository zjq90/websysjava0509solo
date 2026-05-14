package com.photostudio.config;

import com.photostudio.entity.Customer;
import com.photostudio.entity.InteractionRecord;
import com.photostudio.entity.Reminder;
import com.photostudio.entity.Tag;
import com.photostudio.repository.CustomerRepository;
import com.photostudio.repository.InteractionRecordRepository;
import com.photostudio.repository.ReminderRepository;
import com.photostudio.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 测试数据初始化器
 * 系统启动时自动初始化测试数据
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private InteractionRecordRepository interactionRecordRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Override
    public void run(String... args) throws Exception {
        initTags();
        initCustomers();
        initInteractionRecords();
        initReminders();
        System.out.println("========================================");
        System.out.println("  测试数据初始化完成!");
        System.out.println("========================================");
    }

    private void initTags() {
        if (tagRepository.count() == 0) {
            Tag tag1 = new Tag();
            tag1.setName("森系风格爱好者");
            tag1.setDescription("偏好森林、自然环境拍摄风格的客户");
            tag1.setAutoTag(false);

            Tag tag2 = new Tag();
            tag2.setName("二胎家庭");
            tag2.setDescription("有两个孩子的家庭客户");
            tag2.setAutoTag(false);

            Tag tag3 = new Tag();
            tag3.setName("老客户转介绍");
            tag3.setDescription("由老客户推荐来的新客户");
            tag3.setAutoTag(false);

            Tag tag4 = new Tag();
            tag4.setName("VIP客户");
            tag4.setDescription("高价值VIP客户");
            tag4.setAutoTag(false);

            Tag tag5 = new Tag();
            tag5.setName("孕照客户");
            tag5.setDescription("拍摄孕照的客户");
            tag5.setAutoTag(false);

            tagRepository.saveAll(Arrays.asList(tag1, tag2, tag3, tag4, tag5));
            System.out.println("标签数据初始化完成");
        }
    }

    private void initCustomers() {
        if (customerRepository.count() == 0) {
            Customer customer1 = new Customer();
            customer1.setName("张三");
            customer1.setPhone("13800138001");
            customer1.setBirthday(LocalDate.of(1990, 5, 15));
            customer1.setFamilyMembers("配偶：李四，孩子：张小宝");
            customer1.setPhotoType(Customer.PhotoType.WEDDING);
            customer1.setPreference("喜欢森系风格，偏好外景拍摄");
            customer1.setLifecycle(Customer.CustomerLifecycle.DELIVERED);

            Customer customer2 = new Customer();
            customer2.setName("王芳");
            customer2.setPhone("13800138002");
            customer2.setBirthday(LocalDate.of(1992, 8, 20));
            customer2.setFamilyMembers("丈夫：李明，怀孕中");
            customer2.setPhotoType(Customer.PhotoType.MATERNITY);
            customer2.setPreference("喜欢温馨风格，偏好室内拍摄");
            customer2.setLifecycle(Customer.CustomerLifecycle.PHOTOGRAPHED);

            Customer customer3 = new Customer();
            customer3.setName("赵小明");
            customer3.setPhone("13800138003");
            customer3.setBirthday(LocalDate.of(2018, 3, 10));
            customer3.setFamilyMembers("父亲：赵刚，母亲：陈红");
            customer3.setPhotoType(Customer.PhotoType.CHILDREN);
            customer3.setPreference("喜欢活泼可爱风格，需要多场景拍摄");
            customer3.setLifecycle(Customer.CustomerLifecycle.INTENTION);

            Customer customer4 = new Customer();
            customer4.setName("钱伟");
            customer4.setPhone("13800138004");
            customer4.setBirthday(LocalDate.of(1988, 11, 25));
            customer4.setFamilyMembers("配偶：孙丽，孩子：钱多多、钱贝贝");
            customer4.setPhotoType(Customer.PhotoType.FAMILY);
            customer4.setPreference("喜欢传统全家福风格");
            customer4.setLifecycle(Customer.CustomerLifecycle.ORDERED);

            Customer customer5 = new Customer();
            customer5.setName("周婷");
            customer5.setPhone("13800138005");
            customer5.setBirthday(LocalDate.of(1995, 7, 8));
            customer5.setPhotoType(Customer.PhotoType.PERSONAL);
            customer5.setPreference("喜欢文艺、小清新风格");
            customer5.setLifecycle(Customer.CustomerLifecycle.POTENTIAL);

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3, customer4, customer5));
            System.out.println("客户数据初始化完成");
        }
    }

    private void initInteractionRecords() {
        if (interactionRecordRepository.count() == 0) {
            InteractionRecord record1 = new InteractionRecord();
            record1.setCustomerId(1L);
            record1.setContent("客户咨询婚纱照套餐，表示对森系风格特别感兴趣，预算在8000-10000元之间");
            record1.setFollowUpPerson("王经理");
            record1.setNextFollowUpTime(LocalDateTime.now().plusDays(3));

            InteractionRecord record2 = new InteractionRecord();
            record2.setCustomerId(1L);
            record2.setContent("客户已确定拍摄日期为下周六，需要提前准备服装，已发送注意事项");
            record2.setFollowUpPerson("李顾问");

            InteractionRecord record3 = new InteractionRecord();
            record3.setCustomerId(2L);
            record3.setContent("孕照客户咨询，怀孕6个月，希望下个月拍摄，需要温馨风格");
            record3.setFollowUpPerson("张顾问");
            record3.setNextFollowUpTime(LocalDateTime.now().plusDays(1));

            InteractionRecord record4 = new InteractionRecord();
            record4.setCustomerId(3L);
            record4.setContent("儿童摄影咨询，孩子3岁，需要拍摄生日纪念照");
            record4.setFollowUpPerson("王经理");

            interactionRecordRepository.saveAll(Arrays.asList(record1, record2, record3, record4));
            System.out.println("互动记录数据初始化完成");
        }
    }

    private void initReminders() {
        if (reminderRepository.count() == 0) {
            Reminder reminder1 = new Reminder();
            reminder1.setCustomerId(1L);
            reminder1.setTitle("拍摄前一天提醒");
            reminder1.setContent("提醒客户明天拍摄，请准时到达，带好相关物品");
            reminder1.setReminderTime(LocalDateTime.now().plusHours(2));
            reminder1.setType(Reminder.ReminderType.SHOOTING_BEFORE);
            reminder1.setProcessed(false);

            Reminder reminder2 = new Reminder();
            reminder2.setCustomerId(1L);
            reminder2.setTitle("结婚周年纪念");
            reminder2.setContent("客户结婚三周年纪念日，发送祝福信息并推荐纪念照套餐");
            reminder2.setReminderTime(LocalDateTime.now().plusDays(7));
            reminder2.setType(Reminder.ReminderType.ANNIVERSARY);
            reminder2.setProcessed(false);

            Reminder reminder3 = new Reminder();
            reminder3.setCustomerId(2L);
            reminder3.setTitle("取件后回访");
            reminder3.setContent("客户取件后7天，进行满意度回访");
            reminder3.setReminderTime(LocalDateTime.now().minusDays(1));
            reminder3.setType(Reminder.ReminderType.PICKUP_AFTER);
            reminder3.setProcessed(false);

            Reminder reminder4 = new Reminder();
            reminder4.setCustomerId(2L);
            reminder4.setTitle("百天照预售优惠");
            reminder4.setContent("向孕照客户推荐宝宝百天照预售优惠活动");
            reminder4.setReminderTime(LocalDateTime.now().plusMonths(3));
            reminder4.setType(Reminder.ReminderType.MARKETING);
            reminder4.setProcessed(false);

            reminderRepository.saveAll(Arrays.asList(reminder1, reminder2, reminder3, reminder4));
            System.out.println("提醒数据初始化完成");
        }
    }
}
