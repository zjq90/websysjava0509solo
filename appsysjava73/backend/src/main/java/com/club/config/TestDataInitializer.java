package com.club.config;

import cn.hutool.crypto.digest.BCrypt;
import com.club.entity.*;
import com.club.entity.enums.ApplyStatusEnum;
import com.club.entity.enums.ClubCategoryEnum;
import com.club.entity.enums.MemberRoleEnum;
import com.club.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 测试数据初始化器
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubDepartmentRepository clubDepartmentRepository;

    @Autowired
    private ClubActivityRepository clubActivityRepository;

    @Autowired
    private ClubHonorRepository clubHonorRepository;

    @Autowired
    private ClubRecruitRepository clubRecruitRepository;

    @Autowired
    private ClubFollowRepository clubFollowRepository;

    @Autowired
    private ClubPostRepository clubPostRepository;

    @Autowired
    private ClubMilestoneRepository clubMilestoneRepository;

    @Autowired
    private PastPresidentRepository pastPresidentRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) {
        log.info("开始初始化测试数据...");

        initSchools();
        initUsers();
        initClubs();
        initClubMembers();
        initClubDepartments();
        initClubActivities();
        initClubHonors();
        initClubRecruits();
        initClubFollows();
        initClubPosts();
        initClubMilestones();
        initPastPresidents();

        log.info("测试数据初始化完成！");
        log.info("测试账号：admin / 123456");
        log.info("测试账号：user1 / 123456");
        log.info("测试账号：user2 / 123456");
    }

    private void initSchools() {
        if (schoolRepository.count() > 0) return;

        String[][] schools = {
                {"1", "清华大学", "北京", "http://example.com/school1.png"},
                {"2", "北京大学", "北京", "http://example.com/school2.png"},
                {"3", "复旦大学", "上海", "http://example.com/school3.png"},
                {"4", "浙江大学", "杭州", "http://example.com/school4.png"},
                {"5", "南京大学", "南京", "http://example.com/school5.png"}
        };

        for (String[] s : schools) {
            School school = new School();
            school.setName(s[1]);
            school.setCity(s[2]);
            school.setLogo(s[3]);
            school.setDescription(s[1] + "是一所著名高等学府");
            schoolRepository.save(school);
        }
        log.info("学校数据初始化完成，共5条");
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;

        String[][] users = {
                {"admin", "123456", "系统管理员", "1", "计算机学院", "软件工程", "2021001", "13800138001", "MALE"},
                {"user1", "123456", "张三", "1", "计算机学院", "软件工程", "2021002", "13800138002", "MALE"},
                {"user2", "123456", "李四", "2", "文学院", "汉语言文学", "2022001", "13800138003", "FEMALE"},
                {"user3", "123456", "王五", "1", "电子信息学院", "通信工程", "2021003", "13800138004", "MALE"},
                {"user4", "123456", "赵六", "3", "经济学院", "金融学", "2022002", "13800138005", "FEMALE"},
                {"user5", "123456", "孙七", "1", "机械学院", "机械工程", "2021004", "13800138006", "MALE"}
        };

        for (String[] u : users) {
            User user = new User();
            user.setUsername(u[0]);
            user.setPassword(BCrypt.hashpw(u[1], BCrypt.gensalt()));
            user.setRealName(u[2]);
            user.setSchoolId(Long.parseLong(u[3]));
            user.setDepartment(u[4]);
            user.setMajor(u[5]);
            user.setStudentId(u[6]);
            user.setPhone(u[7]);
            user.setGender(u[8]);
            user.setAvatar("http://example.com/avatar/" + u[0] + ".png");
            user.setRole(u[0].equals("admin") ? "ADMIN" : "USER");
            userRepository.save(user);
        }
        log.info("用户数据初始化完成，共6条");
    }

    private void initClubs() {
        if (clubRepository.count() > 0) return;

        Object[][] clubs = {
                {"科技创新协会", ClubCategoryEnum.ACADEMIC_TECHNOLOGY, 1L, "致力于科技创新实践，培养学生创新能力", "http://example.com/club1.png", "学术科技", "13800138011", "北京市海淀区清华大学", "https://tech.example.com"},
                {"舞蹈协会", ClubCategoryEnum.CULTURE_ART, 1L, "推广舞蹈艺术，丰富校园文化生活", "http://example.com/club2.png", "文化艺术", "13800138012", "北京市海淀区清华大学", "https://dance.example.com"},
                {"篮球协会", ClubCategoryEnum.SPORTS, 1L, "组织篮球运动，增强学生体质", "http://example.com/club3.png", "体育竞技", "13800138013", "北京市海淀区清华大学", "https://basketball.example.com"},
                {"志愿者协会", ClubCategoryEnum.PUBLIC_WELFARE, 2L, "组织志愿服务活动，传递爱心", "http://example.com/club4.png", "公益实践", "13800138014", "北京市海淀区北京大学", "https://volunteer.example.com"},
                {"创业协会", ClubCategoryEnum.INNOVATION_ENTREPRENEURSHIP, 1L, "培养创业意识，提供创业指导", "http://example.com/club5.png", "创新创业", "13800138015", "北京市海淀区清华大学", "https://startup.example.com"},
                {"书法协会", ClubCategoryEnum.CULTURE_ART, 2L, "传承书法艺术，弘扬传统文化", "http://example.com/club6.png", "文化艺术", "13800138016", "北京市海淀区北京大学", "https://calligraphy.example.com"}
        };

        for (Object[] c : clubs) {
            Club club = new Club();
            club.setName((String) c[0]);
            club.setCategory((ClubCategoryEnum) c[1]);
            club.setSchoolId((Long) c[2]);
            club.setDescription((String) c[3]);
            club.setLogo((String) c[4]);
            club.setCategoryName((String) c[5]);
            club.setContactPhone((String) c[6]);
            club.setAddress((String) c[7]);
            club.setWebsite((String) c[8]);
            club.setMemberCount(0);
            club.setFollowCount(0);
            club.setActivityCount(0);
            club.setStatus(1);
            clubRepository.save(club);
        }
        log.info("社团数据初始化完成，共6条");
    }

    private void initClubMembers() {
        if (clubMemberRepository.count() > 0) return;

        Object[][] members = {
                {1L, 2L, MemberRoleEnum.PRESIDENT, 1L, "技术部", "张三", "2021002", "13800138002", "计算机学院", "软件工程"},
                {1L, 4L, MemberRoleEnum.VICE_PRESIDENT, 1L, "技术部", "王五", "2021003", "13800138004", "电子信息学院", "通信工程"},
                {1L, 5L, MemberRoleEnum.DEPARTMENT_HEAD, 2L, "策划部", "赵六", "2022002", "13800138005", "经济学院", "金融学"},
                {1L, 6L, MemberRoleEnum.NORMAL, 1L, "技术部", "孙七", "2021004", "13800138006", "机械学院", "机械工程"},
                {2L, 3L, MemberRoleEnum.PRESIDENT, 3L, "表演部", "李四", "2022001", "13800138003", "文学院", "汉语言文学"},
                {2L, 2L, MemberRoleEnum.NORMAL, 3L, "表演部", "张三", "2021002", "13800138002", "计算机学院", "软件工程"},
                {3L, 6L, MemberRoleEnum.PRESIDENT, 4L, "训练部", "孙七", "2021004", "13800138006", "机械学院", "机械工程"},
                {4L, 5L, MemberRoleEnum.PRESIDENT, 5L, "组织部", "赵六", "2022002", "13800138005", "经济学院", "金融学"},
                {5L, 4L, MemberRoleEnum.PRESIDENT, 6L, "项目部", "王五", "2021003", "13800138004", "电子信息学院", "通信工程"},
                {6L, 3L, MemberRoleEnum.PRESIDENT, 7L, "创作部", "李四", "2022001", "13800138003", "文学院", "汉语言文学"}
        };

        for (Object[] m : members) {
            ClubMember member = new ClubMember();
            member.setClubId((Long) m[0]);
            member.setUserId((Long) m[1]);
            member.setRole((MemberRoleEnum) m[2]);
            member.setDepartmentId((Long) m[3]);
            member.setDepartmentName((String) m[4]);
            member.setRealName((String) m[5]);
            member.setStudentId((String) m[6]);
            member.setPhone((String) m[7]);
            member.setDepartment((String) m[8]);
            member.setMajor((String) m[9]);
            member.setAvatar("http://example.com/avatar/user" + m[1] + ".png");
            member.setActive(1);
            member.setActivityCount(0);
            clubMemberRepository.save(member);
        }

        clubRepository.updateMemberCount(1L, 4);
        clubRepository.updateMemberCount(2L, 2);
        clubRepository.updateMemberCount(3L, 1);
        clubRepository.updateMemberCount(4L, 1);
        clubRepository.updateMemberCount(5L, 1);
        clubRepository.updateMemberCount(6L, 1);

        log.info("社团成员数据初始化完成，共10条");
    }

    private void initClubDepartments() {
        if (clubDepartmentRepository.count() > 0) return;

        Object[][] depts = {
                {1L, "技术部", "负责技术研发和项目开发", 1L, "张三", 1},
                {1L, "策划部", "负责活动策划和组织", 2L, "赵六", 2},
                {1L, "宣传部", "负责社团宣传和推广", null, null, 3},
                {2L, "表演部", "负责舞蹈表演和排练", 3L, "李四", 1},
                {2L, "外联部", "负责对外联系和赞助", null, null, 2},
                {3L, "训练部", "负责篮球训练和比赛", 4L, "孙七", 1},
                {4L, "组织部", "负责志愿活动组织", 5L, "赵六", 1},
                {5L, "项目部", "负责创业项目孵化", 6L, "王五", 1},
                {6L, "创作部", "负责书法创作和展览", 7L, "李四", 1}
        };

        for (Object[] d : depts) {
            ClubDepartment dept = new ClubDepartment();
            dept.setClubId((Long) d[0]);
            dept.setName((String) d[1]);
            dept.setDescription((String) d[2]);
            dept.setLeaderId((Long) d[3]);
            dept.setLeaderName((String) d[4]);
            dept.setSortOrder((Integer) d[5]);
            dept.setMemberCount(0);
            clubDepartmentRepository.save(dept);
        }

        clubDepartmentRepository.updateMemberCount(1L, 3);
        clubDepartmentRepository.updateMemberCount(2L, 1);

        log.info("社团部门数据初始化完成，共9条");
    }

    private void initClubActivities() {
        if (clubActivityRepository.count() > 0) return;

        Object[][] activities = {
                {1L, "科技创新大赛", "ACADEMIC", "组织参加全国科技创新大赛", "http://example.com/act1.png",
                        "清华大学主楼会议室", LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7).plusHours(3),
                        LocalDateTime.now().plusDays(5), 50, "张三", "13800138002", 1, 30, 120, 1, 1},
                {1L, "技术分享会", "ACADEMIC", "每周技术分享，交流学习心得", "http://example.com/act2.png",
                        "清华大学西阶教室", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(2),
                        LocalDateTime.now().plusDays(2), 30, "王五", "13800138004", 1, 15, 80, 1, 1},
                {2L, "舞蹈专场演出", "CULTURE", "年度舞蹈专场汇报演出", "http://example.com/act3.png",
                        "清华大学大礼堂", LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(14).plusHours(2),
                        LocalDateTime.now().plusDays(10), 200, "李四", "13800138003", 1, 150, 500, 1, 1},
                {3L, "校级篮球联赛", "SPORTS", "参加校级篮球联赛", "http://example.com/act4.png",
                        "清华大学篮球场", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2),
                        LocalDateTime.now(), 12, "孙七", "13800138006", 0, 12, 0, 0, 1},
                {4L, "敬老院志愿服务", "PUBLIC_WELFARE", "前往敬老院开展志愿服务活动", "http://example.com/act5.png",
                        "北京市海淀区敬老院", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4),
                        LocalDateTime.now().plusDays(8), 30, "赵六", "13800138005", 1, 25, 200, 1, 1}
        };

        for (Object[] a : activities) {
            ClubActivity activity = new ClubActivity();
            activity.setClubId((Long) a[0]);
            activity.setTitle((String) a[1]);
            activity.setType((String) a[2]);
            activity.setContent((String) a[3]);
            activity.setCover((String) a[4]);
            activity.setLocation((String) a[5]);
            activity.setStartTime((LocalDateTime) a[6]);
            activity.setEndTime((LocalDateTime) a[7]);
            activity.setSignupDeadline((LocalDateTime) a[8]);
            activity.setMaxParticipants((Integer) a[9]);
            activity.setContactName((String) a[10]);
            activity.setContactPhone((String) a[11]);
            activity.setStatus((Integer) a[12]);
            activity.setParticipantCount((Integer) a[13]);
            activity.setViewCount((Integer) a[14]);
            activity.setNotifyFollowers((Integer) a[15]);

            Club club = clubRepository.findById((Long) a[0]).orElse(null);
            if (club != null) {
                activity.setClubName(club.getName());
                activity.setClubLogo(club.getLogo());
            }

            clubActivityRepository.save(activity);
        }

        clubRepository.updateActivityCount(1L, 2);
        clubRepository.updateActivityCount(2L, 1);
        clubRepository.updateActivityCount(3L, 1);
        clubRepository.updateActivityCount(4L, 1);

        log.info("社团活动数据初始化完成，共5条");
    }

    private void initClubHonors() {
        if (clubHonorRepository.count() > 0) return;

        Object[][] honors = {
                {1L, "全国科技创新大赛一等奖", "2023-09", "国家级", "在全国大学生科技创新大赛中荣获一等奖", "http://example.com/honor1.png"},
                {1L, "北京市优秀社团", "2023-05", "省级", "荣获北京市优秀学生社团称号", "http://example.com/honor2.png"},
                {2L, "全国大学生舞蹈比赛金奖", "2023-10", "国家级", "在全国大学生舞蹈比赛中荣获金奖", "http://example.com/honor3.png"},
                {3L, "校级篮球联赛冠军", "2023-06", "校级", "荣获校级篮球联赛冠军", "http://example.com/honor4.png"},
                {4L, "北京市优秀志愿服务团队", "2023-03", "省级", "荣获北京市优秀志愿服务团队称号", "http://example.com/honor5.png"}
        };

        for (Object[] h : honors) {
            ClubHonor honor = new ClubHonor();
            honor.setClubId((Long) h[0]);
            honor.setName((String) h[1]);
            honor.setAwardDate((String) h[2]);
            honor.setLevel((String) h[3]);
            honor.setDescription((String) h[4]);
            honor.setImage((String) h[5]);
            clubHonorRepository.save(honor);
        }
        log.info("社团荣誉数据初始化完成，共5条");
    }

    private void initClubRecruits() {
        if (clubRecruitRepository.count() > 0) return;

        Object[][] recruits = {
                {1L, "2024年春季招新", "面向全校招收热爱科技创新的同学", 20, "要求：对技术有热情，有一定编程基础优先",
                        LocalDateTime.now().plusDays(30), 1, 5, 0, "2024-03-01 10:00:00"},
                {2L, "2024年舞蹈队招新", "招收舞蹈爱好者，有无基础均可", 30, "要求：热爱舞蹈，能保证每周训练时间",
                        LocalDateTime.now().plusDays(20), 1, 8, 0, "2024-03-05 14:00:00"},
                {3L, "篮球队招新", "招收篮球队员，参加校级比赛", 15, "要求：有一定篮球基础，身体素质良好",
                        LocalDateTime.now().plusDays(15), 1, 3, 0, "2024-02-28 16:00:00"}
        };

        for (Object[] r : recruits) {
            ClubRecruit recruit = new ClubRecruit();
            recruit.setClubId((Long) r[0]);
            recruit.setTitle((String) r[1]);
            recruit.setDescription((String) r[2]);
            recruit.setQuota((Integer) r[3]);
            recruit.setRequirements((String) r[4]);
            recruit.setDeadline((LocalDateTime) r[5]);
            recruit.setStatus((Integer) r[6]);
            recruit.setApprovedCount((Integer) r[7]);
            recruit.setRejectedCount((Integer) r[8]);
            recruit.setCreateTime(LocalDateTime.parse((String) r[9], FORMATTER));
            clubRecruitRepository.save(recruit);
        }
        log.info("社团招新数据初始化完成，共3条");
    }

    private void initClubFollows() {
        if (clubFollowRepository.count() > 0) return;

        Object[][] follows = {
                {2L, 1L},
                {2L, 2L},
                {3L, 1L},
                {3L, 4L},
                {4L, 1L},
                {4L, 5L},
                {5L, 1L},
                {5L, 2L},
                {5L, 6L},
                {6L, 3L}
        };

        for (Object[] f : follows) {
            ClubFollow follow = new ClubFollow();
            follow.setUserId((Long) f[0]);
            follow.setClubId((Long) f[1]);
            clubFollowRepository.save(follow);
        }

        clubRepository.updateFollowCount(1L, 4);
        clubRepository.updateFollowCount(2L, 2);
        clubRepository.updateFollowCount(3L, 1);
        clubRepository.updateFollowCount(4L, 1);
        clubRepository.updateFollowCount(5L, 1);
        clubRepository.updateFollowCount(6L, 1);

        log.info("社团关注数据初始化完成，共10条");
    }

    private void initClubPosts() {
        if (clubPostRepository.count() > 0) return;

        Object[][] posts = {
                {1L, 2L, "user1", "张三", "http://example.com/avatar/user1.png", "DISCUSSION",
                        "关于新活动的想法", "大家好，我想建议我们社团组织一次户外实践活动，大家有什么好的想法吗？", 0, 5, 3, 0, 0, 0},
                {1L, 4L, "user3", "王五", "http://example.com/avatar/user3.png", "NOTICE",
                        "本周技术分享会通知", "本周六下午2点在西阶教室举办技术分享会，主题是微服务架构，欢迎大家参加！", 1, 20, 0, 1, 0, 0},
                {1L, 5L, "user4", "赵六", "http://example.com/avatar/user4.png", "DISCUSSION",
                        "招新准备工作讨论", "马上要开始招新了，大家一起来讨论一下招新的准备工作吧", 0, 8, 5, 0, 0, 0},
                {2L, 3L, "user2", "李四", "http://example.com/avatar/user2.png", "NOTICE",
                        "舞蹈专场排练通知", "下周开始舞蹈专场的排练，请大家准时参加，时间是每周二四晚上7点", 0, 15, 2, 0, 0, 0},
                {2L, 2L, "user1", "张三", "http://example.com/avatar/user1.png", "DISCUSSION",
                        "求推荐舞蹈视频", "想学习一些新的舞蹈，大家有什么好的视频推荐吗？", 0, 3, 4, 0, 0, 0}
        };

        for (Object[] p : posts) {
            ClubPost post = new ClubPost();
            post.setClubId((Long) p[0]);
            post.setUserId((Long) p[1]);
            post.setUsername((String) p[2]);
            post.setRealName((String) p[3]);
            post.setAvatar((String) p[4]);
            post.setType((String) p[5]);
            post.setTitle((String) p[6]);
            post.setContent((String) p[7]);
            post.setTop((Integer) p[8]);
            post.setViewCount((Integer) p[9]);
            post.setLikeCount((Integer) p[10]);
            post.setEssence((Integer) p[11]);
            post.setCommentCount((Integer) p[12]);
            post.setStatus((Integer) p[13]);
            clubPostRepository.save(post);
        }
        log.info("社团帖子数据初始化完成，共5条");
    }

    private void initClubMilestones() {
        if (clubMilestoneRepository.count() > 0) return;

        Object[][] milestones = {
                {1L, "社团成立", "2018-09-01", "社团正式成立，第一届社长上任", "FOUND", null},
                {1L, "首次参加科创比赛", "2019-05-15", "首次参加全国科技创新大赛并获得三等奖", "AWARD", "http://example.com/ms1.png"},
                {1L, "社团十周年庆典", "2023-09-01", "举办社团成立十周年庆典活动", "ANNIVERSARY", "http://example.com/ms2.png"},
                {2L, "社团成立", "2019-03-01", "舞蹈协会正式成立", "FOUND", null},
                {2L, "首次举办专场演出", "2019-12-10", "成功举办第一届舞蹈专场演出", "ACTIVITY", "http://example.com/ms3.png"}
        };

        for (Object[] m : milestones) {
            ClubMilestone ms = new ClubMilestone();
            ms.setClubId((Long) m[0]);
            ms.setTitle((String) m[1]);
            ms.setEventDate((String) m[2]);
            ms.setDescription((String) m[3]);
            ms.setType((String) m[4]);
            ms.setImage((String) m[5]);
            clubMilestoneRepository.save(ms);
        }
        log.info("社团大事记数据初始化完成，共5条");
    }

    private void initPastPresidents() {
        if (pastPresidentRepository.count() > 0) return;

        Object[][] presidents = {
                {1L, "陈一", "http://example.com/avatar/p1.png", "2018001", "计算机学院", "软件工程",
                        "2018-09", "2019-09", "创建社团，组织首次科创比赛", "chenyi@example.com"},
                {1L, "刘二", "http://example.com/avatar/p2.png", "2019001", "电子信息学院", "通信工程",
                        "2019-09", "2020-09", "扩大社团规模，建立部门制度", "liuer@example.com"},
                {1L, "杨三", "http://example.com/avatar/p3.png", "2020001", "计算机学院", "人工智能",
                        "2020-09", "2021-09", "带领社团获得国家级奖项", "yangsan@example.com"},
                {1L, "黄四", "http://example.com/avatar/p4.png", "2021001", "软件学院", "软件工程",
                        "2021-09", "2022-09", "举办社团十周年庆典", "huangsi@example.com"},
                {2L, "周五", "http://example.com/avatar/p5.png", "2019002", "文学院", "舞蹈学",
                        "2019-03", "2020-09", "创建舞蹈协会，举办首次专场", "zhouwu@example.com"},
                {2L, "吴六", "http://example.com/avatar/p6.png", "2020002", "音乐学院", "音乐表演",
                        "2020-09", "2021-09", "带领社团获得全国舞蹈比赛金奖", "wuliu@example.com"}
        };

        for (Object[] p : presidents) {
            PastPresident pp = new PastPresident();
            pp.setClubId((Long) p[0]);
            pp.setName((String) p[1]);
            pp.setAvatar((String) p[2]);
            pp.setStudentId((String) p[3]);
            pp.setDepartment((String) p[4]);
            pp.setMajor((String) p[5]);
            pp.setTermStart((String) p[6]);
            pp.setTermEnd((String) p[7]);
            pp.setAchievements((String) p[8]);
            pp.setContact((String) p[9]);
            pastPresidentRepository.save(pp);
        }
        log.info("历任社长数据初始化完成，共6条");
    }
}
