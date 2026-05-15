package com.teaching.service;

import com.teaching.entity.*;
import com.teaching.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestDataService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private CourseAnnouncementRepository announcementRepository;

    @Autowired
    private CourseCommentRepository commentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("测试数据已存在，跳过初始化");
            return;
        }

        System.out.println("开始初始化测试数据...");
        initTestData();
        System.out.println("测试数据初始化完成！");
    }

    private void initTestData() {
        User teacher = createUser("teacher", "123456", "张老师", "男", "teacher@example.com", 0);
        User student1 = createUser("student1", "123456", "学生甲", "男", "student1@example.com", 1);
        User student2 = createUser("student2", "123456", "学生乙", "女", "student2@example.com", 1);
        User student3 = createUser("student3", "123456", "学生丙", "男", "student3@example.com", 1);

        Course course1 = createCourse("Java程序设计", "学习Java基础编程，包括面向对象、集合框架、IO流等核心知识", teacher);
        Course course2 = createCourse("Spring Boot开发", "Spring Boot企业级应用开发实战，包含Web开发、数据访问、安全框架等", teacher);
        Course course3 = createCourse("前端Vue开发", "Vue.js前端框架实战，从基础到进阶，掌握组件化开发", teacher);

        enrollStudent(course1, student1);
        enrollStudent(course1, student2);
        enrollStudent(course2, student1);
        enrollStudent(course2, student3);
        enrollStudent(course3, student2);
        enrollStudent(course3, student3);

        createAnnouncement(course1, teacher, "课程通知", "各位同学，下周一开始进行第一章的学习，请提前做好预习准备。");
        createAnnouncement(course1, teacher, "作业提交提醒", "第一次作业请于本周五晚上12点前提交，逾期将扣分处理。");
        createAnnouncement(course2, teacher, "课程安排调整", "本周六的课程调整到周日下午2点，请大家注意时间。");
        createAnnouncement(course3, teacher, "欢迎学习Vue", "欢迎大家加入Vue课程学习，让我们一起进步！");

        createComment(course1, student1, "老师讲得很清晰，学到了很多知识！");
        createComment(course1, student2, "课程内容很实用，希望能有更多实战项目。");
        createComment(course2, student1, "Spring Boot真的很强大，期待后续的微服务内容。");
        createComment(course3, student2, "Vue的组件化开发太方便了，大大提高了开发效率！");

        createMessage(null, student1, "欢迎加入教学系统", "欢迎您加入教学辅助系统！请完善个人资料，开始您的学习之旅。");
        createMessage(null, student2, "欢迎加入教学系统", "欢迎您加入教学辅助系统！请完善个人资料，开始您的学习之旅。");
        createMessage(null, student3, "欢迎加入教学系统", "欢迎您加入教学辅助系统！请完善个人资料，开始您的学习之旅。");
        createMessage(teacher.getId(), student1, "作业提醒", "同学你好，请注意按时完成作业。");
        createMessage(teacher.getId(), student2, "课程提醒", "明天有重要的课程内容，请准时参加。");
    }

    private User createUser(String username, String password, String realName, String gender, String email, int role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setGender(gender);
        user.setEmail(email);
        user.setEmailBound(true);
        user.setRole(role);
        user.setStatus(1);
        user.setPhone("13800138000");
        user.setAvatar("");
        user.setSignature("");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    private Course createCourse(String name, String description, User teacher) {
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setCover("");
        course.setTeacherId(teacher.getId());
        course.setTeacherName(teacher.getRealName());
        course.setCapacity(50);
        course.setStudentCount(0);
        course.setStatus(1);
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        Course savedCourse = courseRepository.save(course);

        CourseUser courseUser = new CourseUser();
        courseUser.setCourseId(savedCourse.getId());
        courseUser.setCourseName(savedCourse.getName());
        courseUser.setUserId(teacher.getId());
        courseUser.setUserName(teacher.getRealName());
        courseUser.setUserRole(0);
        courseUser.setStatus(1);
        courseUser.setCreateTime(LocalDateTime.now());
        courseUser.setUpdateTime(LocalDateTime.now());
        courseUserRepository.save(courseUser);

        return savedCourse;
    }

    private void enrollStudent(Course course, User student) {
        CourseUser courseUser = new CourseUser();
        courseUser.setCourseId(course.getId());
        courseUser.setCourseName(course.getName());
        courseUser.setUserId(student.getId());
        courseUser.setUserName(student.getRealName());
        courseUser.setUserRole(1);
        courseUser.setStatus(1);
        courseUser.setCreateTime(LocalDateTime.now());
        courseUser.setUpdateTime(LocalDateTime.now());
        courseUserRepository.save(courseUser);

        course.setStudentCount(course.getStudentCount() + 1);
        courseRepository.save(course);
    }

    private void createAnnouncement(Course course, User publisher, String title, String content) {
        CourseAnnouncement announcement = new CourseAnnouncement();
        announcement.setCourseId(course.getId());
        announcement.setCourseName(course.getName());
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPublisherId(publisher.getId());
        announcement.setPublisherName(publisher.getRealName());
        announcement.setIsTop(false);
        announcement.setStatus(1);
        announcement.setCreateTime(LocalDateTime.now());
        announcement.setUpdateTime(LocalDateTime.now());
        announcementRepository.save(announcement);
    }

    private void createComment(Course course, User user, String content) {
        CourseComment comment = new CourseComment();
        comment.setCourseId(course.getId());
        comment.setCourseName(course.getName());
        comment.setContent(content);
        comment.setUserId(user.getId());
        comment.setUserName(user.getRealName());
        comment.setAvatar(user.getAvatar());
        comment.setStatus(1);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    private void createMessage(Long senderId, User receiver, String title, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setSenderName(senderId == null ? "系统" : "老师");
        message.setReceiverId(receiver.getId());
        message.setReceiverName(receiver.getRealName());
        message.setTitle(title);
        message.setContent(content);
        message.setIsRead(false);
        message.setType(1);
        message.setStatus(1);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        messageRepository.save(message);
    }
}
