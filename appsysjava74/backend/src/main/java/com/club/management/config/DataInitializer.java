package com.club.management.config;

import com.club.management.entity.*;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.RegistrationScope;
import com.club.management.entity.enums.RegistrationStatus;
import com.club.management.entity.enums.UserRole;
import com.club.management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试数据初始化类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ActivityRepository activityRepository;
    private final RegistrationRepository registrationRepository;
    private final SignInRepository signInRepository;
    private final ActivitySummaryRepository activitySummaryRepository;
    private final ActivityRatingRepository activityRatingRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();
    private static final String[] DEPARTMENTS = {
            "计算机学院", "电子信息学院", "机械工程学院", "经济管理学院",
            "外国语学院", "艺术设计学院", "法学院", "文学院",
            "数学与统计学院", "物理与光电工程学院"
    };
    private static final String[] MAJORS = {
            "计算机科学与技术", "软件工程", "人工智能", "数据科学与大数据技术",
            "电子信息工程", "通信工程", "自动化", "机械设计制造及其自动化",
            "工商管理", "会计学", "金融学", "市场营销",
            "英语", "日语", "环境设计", "视觉传达设计"
    };
    private static final String[] FIRST_NAMES = {
            "张", "李", "王", "刘", "陈", "杨", "黄", "赵", "周", "吴",
            "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"
    };
    private static final String[] LAST_NAMES = {
            "伟", "芳", "娜", "敏", "静", "丽", "强", "磊", "洋", "艳",
            "勇", "军", "杰", "娟", "涛", "明", "超", "秀英", "霞", "平"
    };
    private static final String[] CLUB_TYPES = {
            "学术科技", "文化艺术", "体育运动", "志愿服务", "创新创业", "社会实践"
    };
    private static final String[] CLUB_NAMES = {
            "计算机协会", "英语俱乐部", "篮球协会", "志愿者联盟", "创新创业协会",
            "动漫社", "音乐协会", "舞蹈协会", "摄影协会", "文学社"
    };

    @Override
    @Transactional
    public void run(String... args) {
        log.info("开始初始化测试数据...");

        if (userRepository.count() == 0) {
            initUsers();
            initClubs();
            initClubMembers();
            initActivities();
            initRegistrations();
            initSignIns();
            initActivitySummaries();
            initActivityRatings();
            log.info("测试数据初始化完成！");
        } else {
            log.info("测试数据已存在，跳过初始化");
        }
    }

    private void initUsers() {
        log.info("初始化用户数据...");
        List<User> users = new ArrayList<>();

        User admin = createUser(1L, "admin", "系统管理员", "ADMIN001", UserRole.ADMIN);
        users.add(admin);

        User clubAdmin1 = createUser(2L, "clubadmin1", "社团管理员1", "CA001", UserRole.CLUB_ADMIN);
        users.add(clubAdmin1);

        User clubAdmin2 = createUser(3L, "clubadmin2", "社团管理员2", "CA002", UserRole.CLUB_ADMIN);
        users.add(clubAdmin2);

        for (int i = 1; i <= 20; i++) {
            User student = createUser(
                    (long) (3 + i),
                    "student" + i,
                    generateName(),
                    String.format("2021%04d", i),
                    UserRole.STUDENT
            );
            users.add(student);
        }

        userRepository.saveAll(users);
        log.info("用户数据初始化完成，共{}个用户", users.size());
    }

    private User createUser(Long id, String username, String realName, String studentNo, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRealName(realName);
        user.setStudentNo(studentNo);
        user.setRole(role);
        user.setEnabled(true);
        user.setGender(random.nextBoolean() ? "男" : "女");
        user.setPhone("138" + String.format("%08d", random.nextInt(100000000)));
        user.setEmail(username + "@university.edu.cn");
        user.setDepartment(DEPARTMENTS[random.nextInt(DEPARTMENTS.length)]);
        user.setMajor(MAJORS[random.nextInt(MAJORS.length)]);
        user.setClassName((random.nextInt(4) + 1) + "班");
        user.setGrade("202" + (random.nextInt(4) + 1));
        user.setCreateBy(1L);
        user.setUpdateBy(1L);
        return user;
    }

    private void initClubs() {
        log.info("初始化社团数据...");
        List<Club> clubs = new ArrayList<>();

        for (int i = 0; i < CLUB_NAMES.length; i++) {
            Club club = new Club();
            club.setId((long) (i + 1));
            club.setName(CLUB_NAMES[i]);
            club.setShortName(CLUB_NAMES[i].replace("协会", "").replace("社", ""));
            club.setType(CLUB_TYPES[random.nextInt(CLUB_TYPES.length)]);
            club.setDescription(CLUB_NAMES[i] + "是一个充满活力的学生社团，致力于丰富校园文化生活，提高学生综合素质。");
            club.setAdvisor("张老师");
            club.setContactPhone("010-12345678");
            club.setContactEmail(CLUB_NAMES[i].replaceAll("[\\u4e00-\\u9fa5]", "") + "@university.edu.cn");
            club.setMemberCount(10 + random.nextInt(40));
            club.setStatus(1);
            club.setPresidentId(2L + (i % 2));
            club.setCreateBy(1L);
            club.setUpdateBy(1L);
            clubs.add(club);
        }

        clubRepository.saveAll(clubs);
        log.info("社团数据初始化完成，共{}个社团", clubs.size());
    }

    private void initClubMembers() {
        log.info("初始化社团成员数据...");
        List<ClubMember> members = new ArrayList<>();
        long memberId = 1;

        for (int clubId = 1; clubId <= 5; clubId++) {
            final long currentClubId = clubId;
            for (int i = 0; i < 15; i++) {
                Long userId = 4L + random.nextInt(20);
                
                boolean exists = members.stream()
                        .anyMatch(m -> m.getClubId() == currentClubId && m.getUserId().equals(userId));
                if (exists) continue;

                User user = userRepository.findById(userId).orElse(null);
                if (user == null) continue;

                ClubMember member = new ClubMember();
                member.setId(memberId++);
                member.setClubId((long) clubId);
                member.setUserId(userId);
                member.setStudentNo(user.getStudentNo());
                member.setRealName(user.getRealName());
                member.setDepartment(user.getDepartment());
                member.setMajor(user.getMajor());
                member.setClassName(user.getClassName());
                member.setPosition(i == 0 ? "社长" : (i < 3 ? "部长" : "干事"));
                member.setJoinTime(LocalDateTime.now().minusMonths(random.nextInt(12)));
                member.setStatus(1);
                member.setCreateBy(1L);
                member.setUpdateBy(1L);
                members.add(member);
            }
        }

        clubMemberRepository.saveAll(members);
        log.info("社团成员数据初始化完成，共{}条记录", members.size());
    }

    private void initActivities() {
        log.info("初始化活动数据...");
        List<Activity> activities = new ArrayList<>();

        String[] activityNames = {
                "2024年迎新晚会", "编程大赛", "英语口语训练营", "篮球友谊赛",
                "公益志愿者活动", "动漫展", "音乐节", "摄影展",
                "创新创业论坛", "文学沙龙", "技术分享会", "职业规划讲座"
        };

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < activityNames.length; i++) {
            Activity activity = new Activity();
            activity.setId((long) (i + 1));
            activity.setClubId((long) (i % 5 + 1));
            activity.setName(activityNames[i]);
            activity.setStartTime(now.plusDays(i * 2 - 5));
            activity.setEndTime(now.plusDays(i * 2 - 5).plusHours(2));
            activity.setLocation("学校" + (i % 3 == 0 ? "大礼堂" : (i % 3 == 1 ? "体育馆" : "教学楼") + (i % 5 + 1) + "室"));
            activity.setQuota(20 + random.nextInt(80));
            activity.setRegisteredCount(0);
            activity.setRegistrationStartTime(now.plusDays(i * 2 - 10));
            activity.setRegistrationEndTime(now.plusDays(i * 2 - 6));
            activity.setRequirements("热爱活动，遵守纪律，准时参加");
            activity.setDescription("这是一个精彩的活动，欢迎广大同学积极参与！");
            activity.setRegistrationScope(random.nextBoolean() ? RegistrationScope.ALL_STUDENTS : RegistrationScope.CLUB_MEMBERS_ONLY);
            activity.setNeedApproval(random.nextBoolean());
            activity.setSignInStartTime(now.plusDays(i * 2 - 5).minusMinutes(30));
            activity.setSignInEndTime(now.plusDays(i * 2 - 5).plusMinutes(30));
            activity.setOrganizerName(generateName());
            activity.setOrganizerPhone("139" + String.format("%08d", random.nextInt(100000000)));
            activity.setArchived(i < 3);
            activity.setCreateBy(2L + (i % 2));
            activity.setUpdateBy(2L + (i % 2));

            ActivityStatus status;
            if (i < 3) {
                status = ActivityStatus.COMPLETED;
            } else if (i < 5) {
                status = ActivityStatus.ONGOING;
            } else if (i < 7) {
                status = ActivityStatus.REGISTRATION_OPEN;
            } else if (i < 9) {
                status = ActivityStatus.REGISTRATION_CLOSED;
            } else {
                status = ActivityStatus.DRAFT;
            }
            activity.setStatus(status);

            activities.add(activity);
        }

        activityRepository.saveAll(activities);
        log.info("活动数据初始化完成，共{}个活动", activities.size());
    }

    private void initRegistrations() {
        log.info("初始化报名数据...");
        List<Registration> registrations = new ArrayList<>();
        long regId = 1;

        for (int activityId = 1; activityId <= 6; activityId++) {
            final long currentActivityId = activityId;
            Activity activity = activityRepository.findById((long) activityId).orElse(null);
            if (activity == null) continue;

            int regCount = Math.min(activity.getQuota(), 10 + random.nextInt(20));
            for (int i = 0; i < regCount; i++) {
                Long userId = 4L + random.nextInt(20);
                
                boolean exists = registrations.stream()
                        .anyMatch(r -> r.getActivityId() == currentActivityId && r.getUserId().equals(userId));
                if (exists) continue;

                User user = userRepository.findById(userId).orElse(null);
                if (user == null) continue;

                Registration reg = new Registration();
                reg.setId(regId++);
                reg.setActivityId((long) activityId);
                reg.setUserId(userId);
                reg.setStudentNo(user.getStudentNo());
                reg.setRealName(user.getRealName());
                reg.setDepartment(user.getDepartment());
                reg.setMajor(user.getMajor());
                reg.setClassName(user.getClassName());
                reg.setPhone(user.getPhone());
                reg.setRemark("期待活动！");
                reg.setStatus(random.nextBoolean() ? RegistrationStatus.APPROVED : RegistrationStatus.PENDING);
                reg.setSignedIn(reg.getStatus() == RegistrationStatus.APPROVED && random.nextBoolean());
                if (reg.getSignedIn()) {
                    reg.setSignInTime(activity.getStartTime().plusMinutes(random.nextInt(30)));
                }
                reg.setCreateBy(userId);
                reg.setUpdateBy(userId);
                registrations.add(reg);
            }

            final long countActivityId = activityId;
            activity.setRegisteredCount((int) registrations.stream()
                    .filter(r -> r.getActivityId() == countActivityId && r.getStatus() == RegistrationStatus.APPROVED)
                    .count());
            activityRepository.save(activity);
        }

        registrationRepository.saveAll(registrations);
        log.info("报名数据初始化完成，共{}条记录", registrations.size());
    }

    private void initSignIns() {
        log.info("初始化签到数据...");
        List<SignIn> signIns = new ArrayList<>();
        long signInId = 1;

        for (int activityId = 1; activityId <= 3; activityId++) {
            List<Registration> approvedRegs = registrationRepository
                    .findByActivityIdAndStatusAndDeletedFalse((long) activityId, RegistrationStatus.APPROVED);

            for (Registration reg : approvedRegs) {
                if (!reg.getSignedIn()) continue;

                SignIn signIn = new SignIn();
                signIn.setId(signInId++);
                signIn.setActivityId((long) activityId);
                signIn.setRegistrationId(reg.getId());
                signIn.setUserId(reg.getUserId());
                signIn.setStudentNo(reg.getStudentNo());
                signIn.setRealName(reg.getRealName());
                signIn.setDepartment(reg.getDepartment());
                signIn.setSignInTime(reg.getSignInTime());
                signIn.setSignInIp("192.168." + random.nextInt(256) + "." + random.nextInt(256));
                signIn.setSignInDevice("iPhone 14");
                signIn.setSignInLocation(random.nextDouble(39, 40) + "," + random.nextDouble(116, 117));
                signIn.setSignInMethod(random.nextInt(10) < 8 ? "QR_CODE" : "MANUAL");
                signIn.setStatus(random.nextInt(10) < 7 ? 
                        com.club.management.entity.enums.SignInStatus.SIGNED : 
                        (random.nextBoolean() ? com.club.management.entity.enums.SignInStatus.LATE : 
                         com.club.management.entity.enums.SignInStatus.MAKE_UP));
                if (signIn.getStatus() == com.club.management.entity.enums.SignInStatus.MAKE_UP) {
                    signIn.setMakeUpReason("特殊情况补签");
                    signIn.setMakeUpBy(2L);
                    signIn.setMakeUpTime(LocalDateTime.now());
                }
                signIn.setCreateBy(reg.getUserId());
                signIn.setUpdateBy(reg.getUserId());
                signIns.add(signIn);
            }
        }

        signInRepository.saveAll(signIns);
        log.info("签到数据初始化完成，共{}条记录", signIns.size());
    }

    private void initActivitySummaries() {
        log.info("初始化活动总结数据...");
        List<ActivitySummary> summaries = new ArrayList<>();

        for (int activityId = 1; activityId <= 3; activityId++) {
            Activity activity = activityRepository.findById((long) activityId).orElse(null);
            if (activity == null) continue;

            ActivitySummary summary = new ActivitySummary();
            summary.setId((long) activityId);
            summary.setActivityId((long) activityId);
            summary.setClubId(activity.getClubId());
            summary.setTitle(activity.getName() + "活动总结");
            summary.setContent("本次活动取得了圆满成功，同学们积极参与，活动氛围热烈。通过本次活动，大家不仅学到了知识，还结交了朋友，收获满满。");
            summary.setHighlights("1. 参与人数众多，热情高涨\\n2. 活动内容丰富，形式多样\\n3. 组织有序，效果良好");
            summary.setShortcomings("1. 时间安排稍显紧凑\\n2. 场地空间略显不足");
            summary.setImprovements("1. 提前规划时间，预留充足时间\\n2. 考虑更换更大的活动场地");
            summary.setAchievements("本次活动共吸引了" + activity.getRegisteredCount() + "名同学参与，收到了良好的反馈，为后续活动积累了宝贵经验。");
            summary.setSyncToClubPage(true);
            summary.setIsPublic(true);
            summary.setPublished(true);
            summary.setPublishTime(LocalDateTime.now());
            summary.setPublishBy(2L);
            summary.setCreateBy(2L);
            summary.setUpdateBy(2L);
            summaries.add(summary);
        }

        activitySummaryRepository.saveAll(summaries);
        log.info("活动总结数据初始化完成，共{}条记录", summaries.size());
    }

    private void initActivityRatings() {
        log.info("初始化活动评分数据...");
        List<ActivityRating> ratings = new ArrayList<>();
        long ratingId = 1;

        String[] comments = {
                "活动非常精彩，收获满满！",
                "组织得很好，期待下次活动",
                "内容丰富，讲解清晰",
                "氛围很好，认识了很多朋友",
                "整体不错，希望能多举办类似活动",
                "很有意义的一次活动",
                "超出预期，非常满意",
                "活动很棒，继续加油！"
        };

        for (int activityId = 1; activityId <= 3; activityId++) {
            final long currentActivityId = activityId;
            for (int i = 0; i < 8; i++) {
                Long userId = 4L + random.nextInt(20);
                
                boolean exists = ratings.stream()
                        .anyMatch(r -> r.getActivityId() == currentActivityId && r.getUserId().equals(userId));
                if (exists) continue;

                User user = userRepository.findById(userId).orElse(null);
                if (user == null) continue;

                ActivityRating rating = new ActivityRating();
                rating.setId(ratingId++);
                rating.setActivityId((long) activityId);
                rating.setUserId(userId);
                rating.setUsername(user.getUsername());
                rating.setRating(3 + random.nextInt(3));
                rating.setContentRating(3 + random.nextInt(3));
                rating.setOrganizationRating(3 + random.nextInt(3));
                rating.setVenueRating(3 + random.nextInt(3));
                rating.setComment(comments[random.nextInt(comments.length)]);
                rating.setSuggestion("希望能增加更多互动环节");
                rating.setAnonymous(random.nextBoolean());
                rating.setCreateBy(userId);
                rating.setUpdateBy(userId);
                ratings.add(rating);
            }

            long actId = activityId;
            double avgRating = ratings.stream()
                    .filter(r -> r.getActivityId() == actId)
                    .mapToInt(ActivityRating::getRating)
                    .average()
                    .orElse(0.0);
            long count = ratings.stream()
                    .filter(r -> r.getActivityId() == actId)
                    .count();

            Activity activity = activityRepository.findById((long) activityId).orElse(null);
            if (activity != null) {
                activity.setAverageRating(Math.round(avgRating * 10) / 10.0);
                activity.setRatingCount((int) count);
                activityRepository.save(activity);
            }
        }

        activityRatingRepository.saveAll(ratings);
        log.info("活动评分数据初始化完成，共{}条记录", ratings.size());
    }

    private String generateName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }
}
