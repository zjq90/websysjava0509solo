package com.referee.config;

import com.referee.entity.*;
import com.referee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 数据初始化类
 * 系统启动时自动创建测试数据
 *
 * @author Referee System
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CompetitionAthleteRepository competitionAthleteRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public void run(String... args) {
        initAdmin();
        initReferees();
        initAthletes();
        initCompetitions();
        initCompetitionAthletes();
        initScores();
    }

    /**
     * 初始化管理员账号
     */
    private void initAdmin() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setRealName("系统管理员");
        admin.setRole("ADMIN");
        admin.setPhone("13800000000");
        admin.setEmail("admin@referee.com");
        admin.setStatus(1);
        userRepository.save(admin);
        System.out.println("管理员账号已创建: admin/123456");
    }

    /**
     * 初始化裁判数据
     */
    private void initReferees() {
        Referee chief1 = new Referee();
        chief1.setRefereeNo("R001");
        chief1.setName("张明");
        chief1.setGender("男");
        chief1.setAge(45);
        chief1.setIdCard("110101197901011234");
        chief1.setLevel("国家级");
        chief1.setOrganization("国家体育总局");
        chief1.setSpecialty("田径");
        chief1.setPhone("13900000001");
        chief1.setEmail("zhangming@referee.com");
        chief1.setIsChief(1);
        chief1.setStatus(1);
        refereeRepository.save(chief1);

        User user1 = new User();
        user1.setUsername("referee_R001");
        user1.setPassword("123456");
        user1.setRealName("张明");
        user1.setRole("CHIEF_REFEREE");
        user1.setPhone("13900000001");
        user1.setEmail("zhangming@referee.com");
        user1.setStatus(1);
        user1.setRefereeId(chief1.getId());
        userRepository.save(user1);

        Referee ref1 = new Referee();
        ref1.setRefereeNo("R002");
        ref1.setName("李华");
        ref1.setGender("男");
        ref1.setAge(38);
        ref1.setIdCard("110101198601011235");
        ref1.setLevel("一级");
        ref1.setOrganization("北京市体育局");
        ref1.setSpecialty("田径");
        ref1.setPhone("13900000002");
        ref1.setEmail("lihua@referee.com");
        ref1.setIsChief(0);
        ref1.setStatus(1);
        refereeRepository.save(ref1);

        User user2 = new User();
        user2.setUsername("referee_R002");
        user2.setPassword("123456");
        user2.setRealName("李华");
        user2.setRole("REFEREE");
        user2.setPhone("13900000002");
        user2.setEmail("lihua@referee.com");
        user2.setStatus(1);
        user2.setRefereeId(ref1.getId());
        userRepository.save(user2);

        Referee ref2 = new Referee();
        ref2.setRefereeNo("R003");
        ref2.setName("王芳");
        ref2.setGender("女");
        ref2.setAge(35);
        ref2.setIdCard("110101198901011236");
        ref2.setLevel("一级");
        ref2.setOrganization("上海市体育局");
        ref2.setSpecialty("体操");
        ref2.setPhone("13900000003");
        ref2.setEmail("wangfang@referee.com");
        ref2.setIsChief(0);
        ref2.setStatus(1);
        refereeRepository.save(ref2);

        User user3 = new User();
        user3.setUsername("referee_R003");
        user3.setPassword("123456");
        user3.setRealName("王芳");
        user3.setRole("REFEREE");
        user3.setPhone("13900000003");
        user3.setEmail("wangfang@referee.com");
        user3.setStatus(1);
        user3.setRefereeId(ref2.getId());
        userRepository.save(user3);

        System.out.println("裁判数据已创建: 3名裁判（1名裁判长）");
    }

    /**
     * 初始化运动员数据
     */
    private void initAthletes() {
        Athlete ath1 = new Athlete();
        ath1.setAthleteNo("A001");
        ath1.setName("刘翔宇");
        ath1.setGender("男");
        ath1.setAge(22);
        ath1.setIdCard("310101200201011234");
        ath1.setSchool("北京大学");
        ath1.setEvent("100米短跑");
        ath1.setPhone("13700000001");
        ath1.setEmail("liuxiangyu@athlete.com");
        ath1.setStatus(1);
        athleteRepository.save(ath1);

        User user1 = new User();
        user1.setUsername("athlete_A001");
        user1.setPassword("123456");
        user1.setRealName("刘翔宇");
        user1.setRole("ATHLETE");
        user1.setPhone("13700000001");
        user1.setEmail("liuxiangyu@athlete.com");
        user1.setStatus(1);
        user1.setAthleteId(ath1.getId());
        userRepository.save(user1);

        Athlete ath2 = new Athlete();
        ath2.setAthleteNo("A002");
        ath2.setName("陈雨萱");
        ath2.setGender("女");
        ath2.setAge(20);
        ath2.setIdCard("310101200401011235");
        ath2.setSchool("清华大学");
        ath2.setEvent("100米短跑");
        ath2.setPhone("13700000002");
        ath2.setEmail("chenyuxuan@athlete.com");
        ath2.setStatus(1);
        athleteRepository.save(ath2);

        User user2 = new User();
        user2.setUsername("athlete_A002");
        user2.setPassword("123456");
        user2.setRealName("陈雨萱");
        user2.setRole("ATHLETE");
        user2.setPhone("13700000002");
        user2.setEmail("chenyuxuan@athlete.com");
        user2.setStatus(1);
        user2.setAthleteId(ath2.getId());
        userRepository.save(user2);

        Athlete ath3 = new Athlete();
        ath3.setAthleteNo("A003");
        ath3.setName("赵子豪");
        ath3.setGender("男");
        ath3.setAge(21);
        ath3.setIdCard("310101200301011236");
        ath3.setSchool("复旦大学");
        ath3.setEvent("100米短跑");
        ath3.setPhone("13700000003");
        ath3.setEmail("zizihao@athlete.com");
        ath3.setStatus(1);
        athleteRepository.save(ath3);

        User user3 = new User();
        user3.setUsername("athlete_A003");
        user3.setPassword("123456");
        user3.setRealName("赵子豪");
        user3.setRole("ATHLETE");
        user3.setPhone("13700000003");
        user3.setEmail("zizihao@athlete.com");
        user3.setStatus(1);
        user3.setAthleteId(ath3.getId());
        userRepository.save(user3);

        System.out.println("运动员数据已创建: 3名运动员");
    }

    /**
     * 初始化比赛数据
     */
    private void initCompetitions() {
        Competition comp1 = new Competition();
        comp1.setCompetitionNo("C001");
        comp1.setName("2024年春季田径运动会");
        comp1.setEvent("100米短跑");
        comp1.setLocation("国家体育场");
        comp1.setStartTime(new Date());
        comp1.setFullScore(100.0);
        comp1.setPassScore(60.0);
        comp1.setChiefRefereeId(1L);
        comp1.setChiefRefereeName("张明");
        comp1.setRules("采用国际田联最新比赛规则，按成绩排名");
        comp1.setStatus(1);
        competitionRepository.save(comp1);

        Competition comp2 = new Competition();
        comp2.setCompetitionNo("C002");
        comp2.setName("2024年夏季体操锦标赛");
        comp2.setEvent("体操全能");
        comp2.setLocation("上海体育馆");
        comp2.setFullScore(100.0);
        comp2.setPassScore(60.0);
        comp2.setChiefRefereeId(1L);
        comp2.setChiefRefereeName("张明");
        comp2.setRules("采用国际体操联合会评分规则");
        comp2.setStatus(0);
        competitionRepository.save(comp2);

        System.out.println("比赛数据已创建: 2场比赛");
    }

    /**
     * 初始化比赛运动员关联
     */
    private void initCompetitionAthletes() {
        String[] lanes = {"第1道", "第2道", "第3道"};
        for (int i = 1; i <= 3; i++) {
            CompetitionAthlete ca = new CompetitionAthlete();
            ca.setCompetitionId(1L);
            ca.setCompetitionNo("C001");
            ca.setCompetitionName("2024年春季田径运动会");
            ca.setAthleteId((long) i);
            ca.setAthleteNo("A00" + i);
            ca.setAthleteName(i == 1 ? "刘翔宇" : (i == 2 ? "陈雨萱" : "赵子豪"));
            ca.setLane(lanes[i - 1]);
            ca.setCheckInStatus(0);
            ca.setStatus(1);
            competitionAthleteRepository.save(ca);
        }
        System.out.println("比赛运动员关联已创建: 3名运动员报名");
    }

    /**
     * 初始化评分数据
     */
    private void initScores() {
        for (int athleteId = 1; athleteId <= 3; athleteId++) {
            for (int refereeId = 2; refereeId <= 3; refereeId++) {
                Score score = new Score();
                score.setCompetitionId(1L);
                score.setCompetitionNo("C001");
                score.setCompetitionName("2024年春季田径运动会");
                score.setAthleteId((long) athleteId);
                score.setAthleteNo("A00" + athleteId);
                score.setAthleteName(athleteId == 1 ? "刘翔宇" : (athleteId == 2 ? "陈雨萱" : "赵子豪"));
                score.setRefereeId((long) refereeId);
                score.setRefereeNo("R00" + refereeId);
                score.setRefereeName(refereeId == 2 ? "李华" : "王芳");
                
                double technical = 40 + Math.random() * 10;
                double performance = 40 + Math.random() * 10;
                score.setTechnicalScore(Math.round(technical * 10) / 10.0);
                score.setPerformanceScore(Math.round(performance * 10) / 10.0);
                score.setTotalScore(Math.round((technical + performance) * 10) / 10.0);
                
                score.setComment("表现" + (score.getTotalScore() >= 85 ? "优秀" : (score.getTotalScore() >= 75 ? "良好" : "合格")));
                score.setScoreTime(new Date());
                score.setAuditStatus(athleteId == 1 ? 1 : 0);
                score.setAuditorId(athleteId == 1 ? 1L : null);
                score.setAuditorName(athleteId == 1 ? "张明" : null);
                score.setStatus(1);
                scoreRepository.save(score);
            }
        }
        System.out.println("评分数据已创建: 6条评分记录");
        System.out.println("=== 所有测试数据初始化完成 ===");
    }
}
