package com.club.config;

import com.club.entity.*;
import com.club.enums.*;
import com.club.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 应用启动时自动生成测试数据
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final AnnualRegistrationRepository annualRegistrationRepository;
    private final ViolationRecordRepository violationRecordRepository;
    private final ActivityRepository activityRepository;
    private final FinanceRecordRepository financeRecordRepository;
    private final MessageTemplateRepository messageTemplateRepository;
    private final DataBackupRepository dataBackupRepository;
    private final ContentReviewRepository contentReviewRepository;

    public DataInitializer(UserRepository userRepository,
                           ClubRepository clubRepository,
                           ClubApplicationRepository clubApplicationRepository,
                           AnnualRegistrationRepository annualRegistrationRepository,
                           ViolationRecordRepository violationRecordRepository,
                           ActivityRepository activityRepository,
                           FinanceRecordRepository financeRecordRepository,
                           MessageTemplateRepository messageTemplateRepository,
                           DataBackupRepository dataBackupRepository,
                           ContentReviewRepository contentReviewRepository) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.clubApplicationRepository = clubApplicationRepository;
        this.annualRegistrationRepository = annualRegistrationRepository;
        this.violationRecordRepository = violationRecordRepository;
        this.activityRepository = activityRepository;
        this.financeRecordRepository = financeRecordRepository;
        this.messageTemplateRepository = messageTemplateRepository;
        this.dataBackupRepository = dataBackupRepository;
        this.contentReviewRepository = contentReviewRepository;
    }

    @Override
    public void run(String... args) {
        initUsers();
        initClubs();
        initClubApplications();
        initAnnualRegistrations();
        initViolationRecords();
        initActivities();
        initFinanceRecords();
        initMessageTemplates();
        initDataBackups();
        initContentReviews();
    }

    private void initUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRealName("系统管理员");
        admin.setPhone("13800000001");
        admin.setEmail("admin@club.edu.cn");
        admin.setDepartment("校团委");
        admin.setRoleType(RoleType.SUPER_ADMIN);
        admin.setEnabled(true);
        admin.setRemark("超级管理员账号");
        userRepository.save(admin);

        User schoolAdmin = new User();
        schoolAdmin.setUsername("school_admin");
        schoolAdmin.setPassword("123456");
        schoolAdmin.setRealName("张老师");
        schoolAdmin.setPhone("13800000002");
        schoolAdmin.setEmail("zhang@club.edu.cn");
        schoolAdmin.setDepartment("校社联");
        schoolAdmin.setRoleType(RoleType.SCHOOL_ADMIN);
        schoolAdmin.setEnabled(true);
        schoolAdmin.setRemark("校级管理员");
        userRepository.save(schoolAdmin);

        User leader = new User();
        leader.setUsername("leader1");
        leader.setPassword("123456");
        leader.setRealName("李四");
        leader.setPhone("13800000003");
        leader.setEmail("lisi@club.edu.cn");
        leader.setDepartment("计算机学院");
        leader.setRoleType(RoleType.CLUB_LEADER);
        leader.setEnabled(true);
        leader.setRemark("计算机协会负责人");
        userRepository.save(leader);
    }

    private void initClubs() {
        Club club1 = new Club();
        club1.setName("计算机协会");
        club1.setShortName("计协");
        club1.setType(ClubType.ACADEMIC);
        club1.setDepartment("计算机学院");
        club1.setStatus(ClubStatus.NORMAL);
        club1.setDescription("计算机协会成立于2005年，是一个以学习和推广计算机技术为宗旨的学术科技类社团。");
        club1.setConstitution("第一章 总则\n第二章 会员\n第三章 组织机构\n第四章 活动内容");
        club1.setFoundDate(LocalDate.of(2005, 9, 1));
        club1.setLeaderName("李四");
        club1.setLeaderPhone("13800000003");
        club1.setAdvisor("王教授");
        club1.setMemberCount(150);
        club1.setFundBalance(new BigDecimal("8500.00"));
        club1.setAnnualActivityCount(18);
        club1.setAnnualRegistered(true);
        club1.setRegisterYear(2024);
        clubRepository.save(club1);

        Club club2 = new Club();
        club2.setName("大学生艺术团");
        club2.setShortName("艺术团");
        club2.setType(ClubType.CULTURAL);
        club2.setDepartment("艺术学院");
        club2.setStatus(ClubStatus.NORMAL);
        club2.setDescription("大学生艺术团是学校规模最大的文化艺术类社团，涵盖舞蹈、音乐、戏剧等多个艺术门类。");
        club2.setConstitution("第一章 总则\n第二章 团员权利与义务\n第三章 组织架构");
        club2.setFoundDate(LocalDate.of(2008, 9, 15));
        club2.setLeaderName("王芳");
        club2.setLeaderPhone("13800000004");
        club2.setAdvisor("李教授");
        club2.setMemberCount(280);
        club2.setFundBalance(new BigDecimal("12000.00"));
        club2.setAnnualActivityCount(25);
        club2.setAnnualRegistered(true);
        club2.setRegisterYear(2024);
        clubRepository.save(club2);

        Club club3 = new Club();
        club3.setName("篮球协会");
        club3.setShortName("篮协");
        club3.setType(ClubType.SPORTS);
        club3.setDepartment("体育学院");
        club3.setStatus(ClubStatus.WARNING);
        club3.setDescription("篮球协会致力于推广篮球运动，提高学生身体素质。");
        club3.setConstitution("第一章 总则\n第二章 会员管理\n第三章 活动组织");
        club3.setFoundDate(LocalDate.of(2010, 3, 20));
        club3.setLeaderName("张伟");
        club3.setLeaderPhone("13800000005");
        club3.setAdvisor("陈教练");
        club3.setMemberCount(120);
        club3.setFundBalance(new BigDecimal("3200.00"));
        club3.setAnnualActivityCount(12);
        club3.setAnnualRegistered(false);
        clubRepository.save(club3);

        Club club4 = new Club();
        club4.setName("志愿者协会");
        club4.setShortName("志协");
        club4.setType(ClubType.VOLUNTEER);
        club4.setDepartment("校团委");
        club4.setStatus(ClubStatus.NORMAL);
        club4.setDescription("志愿者协会秉承'奉献、友爱、互助、进步'的志愿精神，组织各类志愿服务活动。");
        club4.setConstitution("第一章 总则\n第二章 志愿者\n第三章 服务项目");
        club4.setFoundDate(LocalDate.of(2012, 5, 4));
        club4.setLeaderName("刘洋");
        club4.setLeaderPhone("13800000006");
        club4.setAdvisor("赵老师");
        club4.setMemberCount(350);
        club4.setFundBalance(new BigDecimal("15000.00"));
        club4.setAnnualActivityCount(30);
        club4.setAnnualRegistered(true);
        club4.setRegisterYear(2024);
        clubRepository.save(club4);

        Club club5 = new Club();
        club5.setName("创新创业协会");
        club5.setShortName("双创协会");
        club5.setType(ClubType.INNOVATION);
        club5.setDepartment("创新创业学院");
        club5.setStatus(ClubStatus.SUSPENDED);
        club5.setDescription("创新创业协会旨在培养学生创新创业意识，提高创新创业能力。");
        club5.setConstitution("第一章 总则\n第二章 会员\n第三章 项目孵化");
        club5.setFoundDate(LocalDate.of(2016, 6, 1));
        club5.setLeaderName("陈明");
        club5.setLeaderPhone("13800000007");
        club5.setAdvisor("刘教授");
        club5.setMemberCount(85);
        club5.setFundBalance(new BigDecimal("6800.00"));
        club5.setAnnualActivityCount(8);
        club5.setAnnualRegistered(false);
        clubRepository.save(club5);

        Club club6 = new Club();
        club6.setName("摄影协会");
        club6.setShortName("摄协");
        club6.setType(ClubType.HOBBY);
        club6.setDepartment("传媒学院");
        club6.setStatus(ClubStatus.NORMAL);
        club6.setDescription("摄影协会为摄影爱好者提供学习交流平台，记录美好瞬间。");
        club6.setConstitution("第一章 总则\n第二章 会员\n第三章 活动安排");
        club6.setFoundDate(LocalDate.of(2014, 10, 15));
        club6.setLeaderName("周静");
        club6.setLeaderPhone("13800000008");
        club6.setAdvisor("孙老师");
        club6.setMemberCount(95);
        club6.setFundBalance(new BigDecimal("4200.00"));
        club6.setAnnualActivityCount(15);
        club6.setAnnualRegistered(true);
        club6.setRegisterYear(2024);
        clubRepository.save(club6);
    }

    private void initClubApplications() {
        ClubApplication app1 = new ClubApplication();
        app1.setClubName("人工智能协会");
        app1.setClubType("ACADEMIC");
        app1.setDepartment("计算机学院");
        app1.setDescription("致力于人工智能技术的学习与实践，开展AI相关讲座和项目开发。");
        app1.setConstitution("第一章 总则\n第二章 会员制度\n第三章 活动组织");
        app1.setInitiatorName("王五");
        app1.setInitiatorStudentId("2021001001");
        app1.setInitiatorPhone("13800000101");
        app1.setInitiatorEmail("wangwu@club.edu.cn");
        app1.setAdvisorName("李教授");
        app1.setAdvisorOpinion("同意担任指导老师");
        app1.setInitiatorCount(12);
        app1.setStatus(ApprovalStatus.PENDING);
        clubApplicationRepository.save(app1);

        ClubApplication app2 = new ClubApplication();
        app2.setClubName("动漫社团");
        app2.setClubType("HOBBY");
        app2.setDepartment("艺术学院");
        app2.setDescription("推广动漫文化，组织COSPLAY、动漫观影等活动。");
        app2.setConstitution("第一章 总则\n第二章 会员");
        app2.setInitiatorName("赵六");
        app2.setInitiatorStudentId("2021002002");
        app2.setInitiatorPhone("13800000102");
        app2.setInitiatorEmail("zhaoliu@club.edu.cn");
        app2.setAdvisorName("王老师");
        app2.setAdvisorOpinion("同意");
        app2.setInitiatorCount(8);
        app2.setStatus(ApprovalStatus.APPROVED);
        app2.setApproverId(1L);
        app2.setApproverName("系统管理员");
        app2.setApproveTime(LocalDateTime.now().minusDays(5));
        app2.setApproveOpinion("符合社团成立条件，同意成立");
        clubApplicationRepository.save(app2);

        ClubApplication app3 = new ClubApplication();
        app3.setClubName("电竞协会");
        app3.setClubType("HOBBY");
        app3.setDepartment("计算机学院");
        app3.setDescription("组织电子竞技比赛，提高电竞水平。");
        app3.setConstitution("第一章 总则");
        app3.setInitiatorName("孙七");
        app3.setInitiatorStudentId("2021003003");
        app3.setInitiatorPhone("13800000103");
        app3.setInitiatorEmail("sunqi@club.edu.cn");
        app3.setAdvisorName("周老师");
        app3.setAdvisorOpinion("需要进一步完善章程");
        app3.setInitiatorCount(5);
        app3.setStatus(ApprovalStatus.REJECTED);
        app3.setApproverId(1L);
        app3.setApproverName("系统管理员");
        app3.setApproveTime(LocalDateTime.now().minusDays(10));
        app3.setApproveOpinion("发起人人数不足，章程不完善，请补充后重新申请");
        clubApplicationRepository.save(app3);
    }

    private void initAnnualRegistrations() {
        AnnualRegistration reg1 = new AnnualRegistration();
        reg1.setClubId(1L);
        reg1.setClubName("计算机协会");
        reg1.setRegisterYear(2024);
        reg1.setAnnualSummary("本年度共举办技术讲座8次，编程大赛2次，参加人数累计500人次。");
        reg1.setNextYearPlan("计划举办人工智能专题讲座，组织参加省级编程比赛。");
        reg1.setActivityCount(18);
        reg1.setMemberChange(30);
        reg1.setFinanceReport("年度收入15000元，支出8000元，主要用于活动物资和奖品。");
        reg1.setProblems("活动场地紧张，设备更新需求大");
        reg1.setStatus(ApprovalStatus.APPROVED);
        reg1.setApproverId(2L);
        reg1.setApproverName("张老师");
        reg1.setApproveTime(LocalDateTime.now().minusMonths(2));
        reg1.setApproveOpinion("年度总结详实，计划合理，同意注册");
        annualRegistrationRepository.save(reg1);

        AnnualRegistration reg2 = new AnnualRegistration();
        reg2.setClubId(3L);
        reg2.setClubName("篮球协会");
        reg2.setRegisterYear(2024);
        reg2.setAnnualSummary("本年度举办校内篮球赛3次，参加校外友谊赛2次。");
        reg2.setNextYearPlan("计划扩大招新，组织院际篮球联赛。");
        reg2.setActivityCount(12);
        reg2.setMemberChange(-10);
        reg2.setFinanceReport("收入6000元，支出5000元。");
        reg2.setProblems("会员流失较多，活动形式单一");
        reg2.setStatus(ApprovalStatus.PENDING);
        annualRegistrationRepository.save(reg2);

        AnnualRegistration reg3 = new AnnualRegistration();
        reg3.setClubId(5L);
        reg3.setClubName("创新创业协会");
        reg3.setRegisterYear(2024);
        reg3.setAnnualSummary("本年度组织创业沙龙5次，孵化创业项目3个。");
        reg3.setNextYearPlan("计划与企业合作，提供更多实践机会。");
        reg3.setActivityCount(8);
        reg3.setMemberChange(15);
        reg3.setFinanceReport("收入20000元，支出15000元。");
        reg3.setProblems("项目资金不足");
        reg3.setStatus(ApprovalStatus.REJECTED);
        reg3.setApproverId(2L);
        reg3.setApproverName("张老师");
        reg3.setApproveTime(LocalDateTime.now().minusMonths(1));
        reg3.setApproveOpinion("活动数量不足，请整改后重新提交");
        annualRegistrationRepository.save(reg3);
    }

    private void initViolationRecords() {
        ViolationRecord v1 = new ViolationRecord();
        v1.setClubId(3L);
        v1.setClubName("篮球协会");
        v1.setViolationType(ViolationType.INACTIVE);
        v1.setDescription("连续2个月未开展任何活动，会员活跃度低");
        v1.setHandleType(ClubStatus.WARNING);
        v1.setHandleOpinion("限期1个月内组织至少1次活动，否则将暂停运营");
        v1.setHandlerId(2L);
        v1.setHandlerName("张老师");
        v1.setHandleTime(LocalDateTime.now().minusWeeks(2));
        v1.setRectificationDeadline(LocalDateTime.now().plusWeeks(2));
        v1.setRectified(false);
        violationRecordRepository.save(v1);

        ViolationRecord v2 = new ViolationRecord();
        v2.setClubId(5L);
        v2.setClubName("创新创业协会");
        v2.setViolationType(ViolationType.FINANCIAL);
        v2.setDescription("财务报表不完整，多笔支出无凭证");
        v2.setHandleType(ClubStatus.SUSPENDED);
        v2.setHandleOpinion("暂停运营，限期整改财务问题，提交完整财务报告");
        v2.setHandlerId(1L);
        v2.setHandlerName("系统管理员");
        v2.setHandleTime(LocalDateTime.now().minusMonths(1));
        v2.setRectificationDeadline(LocalDateTime.now().plusMonths(1));
        v2.setRectified(false);
        violationRecordRepository.save(v2);
    }

    private void initActivities() {
        Activity act1 = new Activity();
        act1.setClubId(1L);
        act1.setClubName("计算机协会");
        act1.setName("2024年校园编程大赛");
        act1.setType(ActivityType.SCHOOL_LEVEL);
        act1.setTheme("代码创造未来");
        act1.setDescription("面向全校学生的编程比赛，分为算法组和开发组。");
        act1.setStartTime(LocalDateTime.now().plusWeeks(2));
        act1.setEndTime(LocalDateTime.now().plusWeeks(2).plusHours(8));
        act1.setLocation("学校计算机中心");
        act1.setExpectedParticipants(150);
        act1.setBudget(new BigDecimal("5000.00"));
        act1.setOrganizerName("李四");
        act1.setOrganizerPhone("13800000003");
        act1.setNeedApproval(true);
        act1.setApprovalStatus(ApprovalStatus.APPROVED);
        act1.setApproverId(2L);
        act1.setApproverName("张老师");
        act1.setApproveTime(LocalDateTime.now().minusDays(3));
        act1.setApproveOpinion("活动方案详细，预算合理，同意举办");
        act1.setContentCompliant(true);
        act1.setStatus(0);
        activityRepository.save(act1);

        Activity act2 = new Activity();
        act2.setClubId(2L);
        act2.setClubName("大学生艺术团");
        act2.setName("2024年元旦文艺晚会");
        act2.setType(ActivityType.SCHOOL_LEVEL);
        act2.setTheme("青春绽放，梦想启航");
        act2.setDescription("全校性的文艺演出，展示艺术团风采。");
        act2.setStartTime(LocalDateTime.now().minusMonths(5));
        act2.setEndTime(LocalDateTime.now().minusMonths(5).plusHours(3));
        act2.setLocation("学校大礼堂");
        act2.setExpectedParticipants(800);
        act2.setActualParticipants(750);
        act2.setBudget(new BigDecimal("20000.00"));
        act2.setOrganizerName("王芳");
        act2.setOrganizerPhone("13800000004");
        act2.setNeedApproval(true);
        act2.setApprovalStatus(ApprovalStatus.APPROVED);
        act2.setApproverId(1L);
        act2.setApproverName("系统管理员");
        act2.setApproveTime(LocalDateTime.now().minusMonths(6));
        act2.setApproveOpinion("大型活动，同意举办，请注意安全");
        act2.setContentCompliant(true);
        act2.setStatus(2);
        activityRepository.save(act2);

        Activity act3 = new Activity();
        act3.setClubId(4L);
        act3.setClubName("志愿者协会");
        act3.setName("社区敬老志愿服务");
        act3.setType(ActivityType.INTERNAL);
        act3.setTheme("关爱老人，传递温暖");
        act3.setDescription("组织志愿者到社区敬老院开展慰问活动。");
        act3.setStartTime(LocalDateTime.now().minusWeeks(1));
        act3.setEndTime(LocalDateTime.now().minusWeeks(1).plusHours(4));
        act3.setLocation("阳光社区敬老院");
        act3.setExpectedParticipants(50);
        act3.setActualParticipants(48);
        act3.setBudget(new BigDecimal("1000.00"));
        act3.setOrganizerName("刘洋");
        act3.setOrganizerPhone("13800000006");
        act3.setNeedApproval(false);
        act3.setContentCompliant(true);
        act3.setStatus(2);
        activityRepository.save(act3);

        Activity act4 = new Activity();
        act4.setClubId(1L);
        act4.setClubName("计算机协会");
        act4.setName("人工智能技术前沿讲座");
        act4.setType(ActivityType.CROSS_SCHOOL);
        act4.setTheme("AI前沿技术分享");
        act4.setDescription("邀请知名高校AI专家进行学术分享。");
        act4.setStartTime(LocalDateTime.now().plusWeeks(4));
        act4.setEndTime(LocalDateTime.now().plusWeeks(4).plusHours(3));
        act4.setLocation("学校学术报告厅");
        act4.setExpectedParticipants(300);
        act4.setBudget(new BigDecimal("8000.00"));
        act4.setOrganizerName("李四");
        act4.setOrganizerPhone("13800000003");
        act4.setNeedApproval(true);
        act4.setApprovalStatus(ApprovalStatus.PENDING);
        act4.setContentCompliant(true);
        act4.setStatus(0);
        activityRepository.save(act4);

        Activity act5 = new Activity();
        act5.setClubId(6L);
        act5.setClubName("摄影协会");
        act5.setName("校园风光摄影展");
        act5.setType(ActivityType.SCHOOL_LEVEL);
        act5.setTheme("发现校园之美");
        act5.setDescription("展出优秀校园摄影作品。");
        act5.setStartTime(LocalDateTime.now().minusWeeks(2));
        act5.setEndTime(LocalDateTime.now().minusWeeks(2).plusDays(5));
        act5.setLocation("学校图书馆大厅");
        act5.setExpectedParticipants(500);
        act5.setActualParticipants(420);
        act5.setBudget(new BigDecimal("2000.00"));
        act5.setOrganizerName("周静");
        act5.setOrganizerPhone("13800000008");
        act5.setNeedApproval(true);
        act5.setApprovalStatus(ApprovalStatus.APPROVED);
        act5.setApproverId(2L);
        act5.setApproverName("张老师");
        act5.setApproveTime(LocalDateTime.now().minusWeeks(3));
        act5.setApproveOpinion("同意举办");
        act5.setContentCompliant(true);
        act5.setStatus(2);
        activityRepository.save(act5);

        Activity act6 = new Activity();
        act6.setClubId(1L);
        act6.setClubName("计算机协会");
        act6.setName("2024年省大学生程序设计竞赛");
        act6.setType(ActivityType.CROSS_SCHOOL);
        act6.setTheme("编程竞技，创新未来");
        act6.setDescription("邀请省内10所高校参加程序设计竞赛，展示大学生编程能力。");
        act6.setStartTime(LocalDateTime.now().plusWeeks(8));
        act6.setEndTime(LocalDateTime.now().plusWeeks(8).plusDays(2));
        act6.setLocation("学校体育馆");
        act6.setExpectedParticipants(800);
        act6.setBudget(new BigDecimal("50000.00"));
        act6.setOrganizerName("李四");
        act6.setOrganizerPhone("13800000003");
        act6.setIsLargeScale(true);
        act6.setApprovalLevel(3);
        act6.setCurrentApprovalStage(1);
        act6.setNeedApproval(true);
        act6.setApprovalStatus(ApprovalStatus.PENDING);
        act6.setParticipatingSchools("清华大学、北京大学、浙江大学、复旦大学、上海交通大学、南京大学、中国科学技术大学、武汉大学、华中科技大学、中山大学");
        act6.setCrossSchoolContact("省教育厅 王老师");
        act6.setCrossSchoolPhone("020-12345678");
        act6.setSafetyPlan("1. 设立医疗点，配备医护人员；2. 制定人员疏散预案；3. 安排安保人员维持秩序；4. 购买活动保险；5. 制定食品安全保障措施。");
        act6.setContentCompliant(true);
        act6.setStatus(0);
        activityRepository.save(act6);

        Activity act7 = new Activity();
        act7.setClubId(2L);
        act7.setClubName("大学生艺术团");
        act7.setName("2024年校园文化艺术节开幕式");
        act7.setType(ActivityType.SCHOOL_LEVEL);
        act7.setTheme("青春绽放，梦想起航");
        act7.setDescription("举办校园文化艺术节开幕式，包含文艺汇演、社团展示等环节。");
        act7.setStartTime(LocalDateTime.now().plusWeeks(6));
        act7.setEndTime(LocalDateTime.now().plusWeeks(6).plusHours(4));
        act7.setLocation("学校大礼堂");
        act7.setExpectedParticipants(1200);
        act7.setBudget(new BigDecimal("30000.00"));
        act7.setOrganizerName("王芳");
        act7.setOrganizerPhone("13800000004");
        act7.setIsLargeScale(true);
        act7.setApprovalLevel(2);
        act7.setCurrentApprovalStage(2);
        act7.setNeedApproval(true);
        act7.setApprovalStatus(ApprovalStatus.PENDING);
        act7.setAssociationApproverId(2L);
        act7.setAssociationApproverName("张老师");
        act7.setAssociationApproveTime(LocalDateTime.now().minusDays(2));
        act7.setAssociationApproveOpinion("活动内容积极向上，同意通过社联审核（通过）");
        act7.setSafetyPlan("1. 控制入场人数，避免拥挤；2. 设置应急通道；3. 配备消防器材；4. 安排专人负责安全检查；5. 制定停电、火灾等应急预案。");
        act7.setContentCompliant(true);
        act7.setStatus(0);
        activityRepository.save(act7);

        Activity act8 = new Activity();
        act8.setClubId(5L);
        act8.setClubName("创新创业协会");
        act8.setName("2024年大学生创新创业论坛");
        act8.setType(ActivityType.CROSS_SCHOOL);
        act8.setTheme("创新驱动发展，创业成就梦想");
        act8.setDescription("邀请多所高校创业团队和投资人参加，分享创业经验。");
        act8.setStartTime(LocalDateTime.now().plusWeeks(10));
        act8.setEndTime(LocalDateTime.now().plusWeeks(10).plusDays(3));
        act8.setLocation("学校学术交流中心");
        act8.setExpectedParticipants(600);
        act8.setBudget(new BigDecimal("80000.00"));
        act8.setOrganizerName("陈明");
        act8.setOrganizerPhone("13800000007");
        act8.setIsLargeScale(true);
        act8.setApprovalLevel(3);
        act8.setCurrentApprovalStage(1);
        act8.setNeedApproval(true);
        act8.setApprovalStatus(ApprovalStatus.PENDING);
        act8.setParticipatingSchools("北京大学、清华大学、复旦大学、上海交通大学、浙江大学");
        act8.setCrossSchoolContact("市科技局 李科长");
        act8.setCrossSchoolPhone("020-87654321");
        act8.setSafetyPlan("1. 参会人员签到登记；2. 会场安保巡逻；3. 防疫措施落实；4. 应急照明设备准备；5. 与附近医院建立应急联动。");
        act8.setContentCompliant(true);
        act8.setStatus(0);
        activityRepository.save(act8);

        Activity act9 = new Activity();
        act9.setClubId(3L);
        act9.setClubName("体育协会");
        act9.setName("2024年校园篮球联赛");
        act9.setType(ActivityType.SCHOOL_LEVEL);
        act9.setTheme("篮不住的青春");
        act9.setDescription("组织全校各学院篮球代表队进行联赛。");
        act9.setStartTime(LocalDateTime.now().plusWeeks(3));
        act9.setEndTime(LocalDateTime.now().plusWeeks(3).plusDays(7));
        act9.setLocation("学校篮球场");
        act9.setExpectedParticipants(300);
        act9.setBudget(new BigDecimal("15000.00"));
        act9.setOrganizerName("赵强");
        act9.setOrganizerPhone("13800000005");
        act9.setIsLargeScale(false);
        act9.setApprovalLevel(1);
        act9.setCurrentApprovalStage(1);
        act9.setNeedApproval(true);
        act9.setApprovalStatus(ApprovalStatus.PENDING);
        act9.setContentCompliant(true);
        act9.setStatus(0);
        activityRepository.save(act9);
    }

    private void initFinanceRecords() {
        FinanceRecord f1 = new FinanceRecord();
        f1.setClubId(1L);
        f1.setClubName("计算机协会");
        f1.setType(FinanceType.INCOME);
        f1.setRecordNo("FIN202401001");
        f1.setAmount(new BigDecimal("5000.00"));
        f1.setCategory("社团经费");
        f1.setPurpose("2024年度学校社团经费拨款");
        f1.setOccurDate(LocalDate.now().minusMonths(4));
        f1.setHandler("李四");
        f1.setHasVoucher(true);
        f1.setAbnormal(false);
        financeRecordRepository.save(f1);

        FinanceRecord f2 = new FinanceRecord();
        f2.setClubId(1L);
        f2.setClubName("计算机协会");
        f2.setType(FinanceType.EXPENSE);
        f2.setRecordNo("FIN202401002");
        f2.setAmount(new BigDecimal("1500.00"));
        f2.setCategory("活动支出");
        f2.setPurpose("购买编程大赛奖品");
        f2.setOccurDate(LocalDate.now().minusMonths(3));
        f2.setHandler("李四");
        f2.setVoucherNo("PZ202401001");
        f2.setHasVoucher(true);
        f2.setAbnormal(false);
        financeRecordRepository.save(f2);

        FinanceRecord f3 = new FinanceRecord();
        f3.setClubId(2L);
        f3.setClubName("大学生艺术团");
        f3.setType(FinanceType.EXPENSE);
        f3.setRecordNo("FIN202401003");
        f3.setAmount(new BigDecimal("8000.00"));
        f3.setCategory("活动支出");
        f3.setPurpose("元旦晚会场地租赁及道具采购");
        f3.setOccurDate(LocalDate.now().minusMonths(5));
        f3.setHandler("王芳");
        f3.setVoucherNo("PZ202401002");
        f3.setHasVoucher(true);
        f3.setAbnormal(false);
        financeRecordRepository.save(f3);

        FinanceRecord f4 = new FinanceRecord();
        f4.setClubId(5L);
        f4.setClubName("创新创业协会");
        f4.setType(FinanceType.EXPENSE);
        f4.setRecordNo("FIN202401004");
        f4.setAmount(new BigDecimal("3500.00"));
        f4.setCategory("办公支出");
        f4.setPurpose("办公用品采购");
        f4.setOccurDate(LocalDate.now().minusMonths(2));
        f4.setHandler("陈明");
        f4.setHasVoucher(false);
        f4.setAbnormal(true);
        f4.setAbnormalNote("大额支出无凭证");
        f4.setMarkedForInvestigation(true);
        f4.setInvestigationStatus(1);
        financeRecordRepository.save(f4);

        FinanceRecord f5 = new FinanceRecord();
        f5.setClubId(4L);
        f5.setClubName("志愿者协会");
        f5.setType(FinanceType.INCOME);
        f5.setRecordNo("FIN202401005");
        f5.setAmount(new BigDecimal("10000.00"));
        f5.setCategory("社会捐赠");
        f5.setPurpose("爱心企业捐赠");
        f5.setOccurDate(LocalDate.now().minusMonths(1));
        f5.setHandler("刘洋");
        f5.setVoucherNo("PZ202401003");
        f5.setHasVoucher(true);
        f5.setAbnormal(false);
        financeRecordRepository.save(f5);

        FinanceRecord f6 = new FinanceRecord();
        f6.setClubId(4L);
        f6.setClubName("志愿者协会");
        f6.setType(FinanceType.EXPENSE);
        f6.setRecordNo("FIN202401006");
        f6.setAmount(new BigDecimal("800.00"));
        f6.setCategory("慰问支出");
        f6.setPurpose("敬老院慰问品采购");
        f6.setOccurDate(LocalDate.now().minusWeeks(1));
        f6.setHandler("刘洋");
        f6.setVoucherNo("PZ202401004");
        f6.setHasVoucher(true);
        f6.setAbnormal(false);
        financeRecordRepository.save(f6);
    }

    private void initMessageTemplates() {
        MessageTemplate t1 = new MessageTemplate();
        t1.setCode("ACTIVITY_REMINDER");
        t1.setName("活动提醒");
        t1.setType("REMINDER");
        t1.setTitle("活动即将开始提醒");
        t1.setContent("尊敬的会员，您报名的活动【{activityName}】将于{startTime}在{location}开始，请准时参加。");
        t1.setVariables("activityName:活动名称,startTime:开始时间,location:活动地点");
        t1.setEnabled(true);
        t1.setPushType("站内信,短信");
        messageTemplateRepository.save(t1);

        MessageTemplate t2 = new MessageTemplate();
        t2.setCode("APPROVAL_RESULT");
        t2.setName("审核结果通知");
        t2.setType("NOTIFICATION");
        t2.setTitle("申请审核结果通知");
        t2.setContent("您的【{applicationType}】申请已{result}，审核意见：{opinion}");
        t2.setVariables("applicationType:申请类型,result:审核结果,opinion:审核意见");
        t2.setEnabled(true);
        t2.setPushType("站内信,邮件");
        messageTemplateRepository.save(t2);

        MessageTemplate t3 = new MessageTemplate();
        t3.setName("社团违规通知");
        t3.setCode("VIOLATION_NOTICE");
        t3.setType("NOTIFICATION");
        t3.setTitle("社团违规处理通知");
        t3.setContent("贵社团因【{violationReason}】被给予{handleType}处理，请于{deadline}前完成整改。");
        t3.setVariables("violationReason:违规原因,handleType:处理类型,deadline:整改期限");
        t3.setEnabled(true);
        t3.setPushType("站内信,邮件");
        messageTemplateRepository.save(t3);

        MessageTemplate t4 = new MessageTemplate();
        t4.setCode("ANNUAL_REGISTER_REMINDER");
        t4.setName("年度注册提醒");
        t4.setType("REMINDER");
        t4.setTitle("社团年度注册提醒");
        t4.setContent("请贵社团于{deadline}前完成年度注册工作，逾期未注册将影响社团正常运营。");
        t4.setVariables("deadline:截止日期");
        t4.setEnabled(true);
        t4.setPushType("站内信,邮件");
        messageTemplateRepository.save(t4);
    }

    private void initDataBackups() {
        DataBackup b1 = new DataBackup();
        b1.setName("20240520_全校社团数据备份");
        b1.setType("SCHEDULED");
        b1.setFilePath("/backup/20240520_backup.sql");
        b1.setFileSize(1048576L);
        b1.setStatus(1);
        b1.setStartTime(LocalDateTime.now().minusDays(7).minusHours(2));
        b1.setEndTime(LocalDateTime.now().minusDays(7).minusHours(1).minusMinutes(30));
        b1.setOperatorId(1L);
        b1.setOperatorName("系统管理员");
        b1.setDescription("每日定时自动备份");
        dataBackupRepository.save(b1);

        DataBackup b2 = new DataBackup();
        b2.setName("20240527_手动备份");
        b2.setType("MANUAL");
        b2.setFilePath("/backup/20240527_manual_backup.sql");
        b2.setFileSize(1153433L);
        b2.setStatus(1);
        b2.setStartTime(LocalDateTime.now().minusHours(3));
        b2.setEndTime(LocalDateTime.now().minusHours(2).minusMinutes(45));
        b2.setOperatorId(2L);
        b2.setOperatorName("张老师");
        b2.setDescription("月度数据手动备份");
        dataBackupRepository.save(b2);

        DataBackup b3 = new DataBackup();
        b3.setName("20240527_测试备份");
        b3.setType("MANUAL");
        b3.setStatus(0);
        b3.setStartTime(LocalDateTime.now().minusMinutes(30));
        b3.setOperatorId(1L);
        b3.setOperatorName("系统管理员");
        b3.setDescription("备份进行中...");
        dataBackupRepository.save(b3);
    }

    private void initContentReviews() {
        ContentReview r1 = new ContentReview();
        r1.setActivityId(2L);
        r1.setActivityName("违规内容测试活动");
        r1.setClubName("测试社团");
        r1.setReviewType(ReviewType.DESCRIPTION);
        r1.setFieldName("description");
        r1.setOriginalContent("这是一个包含违规内容的活动描述，涉及违禁词等敏感信息。");
        r1.setSensitiveWords("[\"违规\",\"违禁词\"]");
        r1.setStatus(ReviewStatus.PENDING);
        contentReviewRepository.save(r1);

        ContentReview r2 = new ContentReview();
        r2.setActivityId(2L);
        r2.setActivityName("违规内容测试活动");
        r2.setClubName("测试社团");
        r2.setReviewType(ReviewType.POSTER);
        r2.setFieldName("poster");
        r2.setOriginalContent("海报内容描述中包含非法和违规的宣传词汇。");
        r2.setSensitiveWords("[\"非法\",\"违规\"]");
        r2.setStatus(ReviewStatus.PENDING);
        contentReviewRepository.save(r2);

        ContentReview r3 = new ContentReview();
        r3.setActivityId(1L);
        r3.setActivityName("2024年编程大赛");
        r3.setClubName("计算机协会");
        r3.setReviewType(ReviewType.NAME);
        r3.setFieldName("name");
        r3.setOriginalContent("正常活动名称测试敏感词检测");
        r3.setSensitiveWords("[\"敏感词\"]");
        r3.setStatus(ReviewStatus.APPROVED);
        r3.setReviewerId(2L);
        r3.setReviewerName("张老师");
        r3.setReviewOpinion("内容正常，审核通过");
        r3.setReviewTime(LocalDateTime.now().minusDays(1));
        contentReviewRepository.save(r3);

        ContentReview r4 = new ContentReview();
        r4.setActivityId(3L);
        r4.setActivityName("志愿者招募活动");
        r4.setClubName("志愿者协会");
        r4.setReviewType(ReviewType.THEME);
        r4.setFieldName("theme");
        r4.setOriginalContent("活动主题中包含低俗词汇需要屏蔽。");
        r4.setSensitiveWords("[\"低俗\"]");
        r4.setStatus(ReviewStatus.BLOCKED);
        r4.setReviewerId(2L);
        r4.setReviewerName("张老师");
        r4.setReviewOpinion("内容违规，已屏蔽");
        r4.setReviewTime(LocalDateTime.now().minusDays(2));
        contentReviewRepository.save(r4);
    }
}
