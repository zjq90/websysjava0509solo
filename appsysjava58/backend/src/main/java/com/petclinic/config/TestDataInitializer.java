package com.petclinic.config;

import com.petclinic.entity.*;
import com.petclinic.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 测试数据初始化配置
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final DewormingRecordRepository dewormingRecordRepository;
    private final CheckupRecordRepository checkupRecordRepository;
    private final DietSuggestionRepository dietSuggestionRepository;
    private final ExerciseDataRepository exerciseDataRepository;
    private final PostRepository postRepository;
    private final PetHospitalRepository petHospitalRepository;
    private final HospitalRatingRepository hospitalRatingRepository;

    @Bean
    public CommandLineRunner initTestData() {
        return args -> {
            log.info("开始初始化测试数据...");

            User user1 = createUser("testuser", "13800138000", "测试用户", "USER");
            User user2 = createUser("expert1", "13900139000", "宠物医生王", "EXPERT");
            User admin = createUser("admin", "13700137000", "管理员", "ADMIN");

            Pet pet1 = createPet(user1, "旺财", "dog", "金毛", new BigDecimal("25.5"), LocalDate.of(2020, 5, 15));
            Pet pet2 = createPet(user1, "咪咪", "cat", "英短", new BigDecimal("4.2"), LocalDate.of(2021, 8, 20));

            createDewormingRecords(pet1, pet2);
            createCheckupRecords(pet1, pet2);
            createDietSuggestions(pet1, pet2);
            createExerciseData(pet1, pet2);
            createPosts(user1);
            createHospitals();

            log.info("测试数据初始化完成！");
        };
    }

    private User createUser(String username, String phone, String nickname, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi");
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setEmail(username + "@example.com");
        user.setRole(role);
        user.setStatus("ACTIVE");
        user.setElderMode(false);
        return userRepository.save(user);
    }

    private Pet createPet(User user, String name, String type, String breed, BigDecimal weight, LocalDate birthday) {
        Pet pet = new Pet();
        pet.setUser(user);
        pet.setName(name);
        pet.setType(type);
        pet.setBreed(breed);
        pet.setWeight(weight);
        pet.setBirthday(birthday);
        pet.setGender("male");
        pet.setColor(type.equals("dog") ? "金色" : "灰色");
        pet.setSterilized(true);
        pet.setSmartCollarId("COLLAR_" + name.toUpperCase());
        return petRepository.save(pet);
    }

    private void createDewormingRecords(Pet pet1, Pet pet2) {
        DewormingRecord d1 = new DewormingRecord();
        d1.setPet(pet1);
        d1.setDewormingDate(LocalDate.now().minusDays(10));
        d1.setMedicineName("福来恩滴剂");
        d1.setDosage("1支");
        d1.setNextDewormingDate(LocalDate.now().plusDays(20));
        d1.setCompleted(true);
        d1.setNotes("体外驱虫，无不良反应");
        dewormingRecordRepository.save(d1);

        DewormingRecord d2 = new DewormingRecord();
        d2.setPet(pet2);
        d2.setDewormingDate(LocalDate.now().minusDays(5));
        d2.setMedicineName("海乐妙");
        d2.setDosage("半片");
        d2.setNextDewormingDate(LocalDate.now().plusDays(25));
        d2.setCompleted(true);
        d2.setNotes("体内外同驱");
        dewormingRecordRepository.save(d2);

        DewormingRecord d3 = new DewormingRecord();
        d3.setPet(pet1);
        d3.setDewormingDate(LocalDate.now().minusMonths(3));
        d3.setMedicineName("拜宠清");
        d3.setDosage("1片");
        d3.setCompleted(true);
        d3.setNotes("上次体内驱虫记录");
        dewormingRecordRepository.save(d3);
    }

    private void createCheckupRecords(Pet pet1, Pet pet2) {
        CheckupRecord c1 = new CheckupRecord();
        c1.setPet(pet1);
        c1.setCheckupDate(LocalDate.now().minusDays(15));
        c1.setHospitalName("阳光宠物医院");
        c1.setVeterinarianName("李医生");
        c1.setWeight(new BigDecimal("25.5"));
        c1.setTemperature(new BigDecimal("38.5"));
        c1.setHeartRate(90);
        c1.setBloodPressure("120/80");
        c1.setGeneralHealth("健康状况良好");
        c1.setNextCheckupDate(LocalDate.now().plusDays(45));
        c1.setCompleted(true);
        c1.setNotes("年度常规体检，各项指标正常");
        checkupRecordRepository.save(c1);

        CheckupRecord c2 = new CheckupRecord();
        c2.setPet(pet2);
        c2.setCheckupDate(LocalDate.now().minusDays(8));
        c2.setHospitalName("爱心宠物诊所");
        c2.setVeterinarianName("张医生");
        c2.setWeight(new BigDecimal("4.2"));
        c2.setTemperature(new BigDecimal("38.3"));
        c2.setHeartRate(120);
        c2.setGeneralHealth("健康");
        c2.setCompleted(true);
        c2.setNotes("疫苗接种后体检");
        checkupRecordRepository.save(c2);
    }

    private void createDietSuggestions(Pet pet1, Pet pet2) {
        DietSuggestion ds1 = new DietSuggestion();
        ds1.setPet(pet1);
        ds1.setTitle("旺财的个性化饮食建议");
        ds1.setContent("根据AAFCO标准，25kg金毛每日约需820kcal热量。建议喂食高蛋白全价狗粮，每日分2-3次喂食。注意控制体重，避免肥胖。\n\n参考文献：AAFCO Dog and Cat Food Nutrient Profiles, 2023");
        ds1.setCalorieRecommendation(820);
        ds1.setFoodType("大型犬成犬粮");
        ds1.setFeedingFrequency("每日2-3次");
        ds1.setReferenceSource("AAFCO, NRC");
        ds1.setGeneratedTime(LocalDateTime.now());
        dietSuggestionRepository.save(ds1);

        DietSuggestion ds2 = new DietSuggestion();
        ds2.setPet(pet2);
        ds2.setTitle("咪咪的个性化饮食建议");
        ds2.setContent("根据WSAVA指南，4kg英短每日约需230kcal热量。建议喂食优质猫粮，确保牛磺酸含量≥0.1%。干湿搭配喂食，增加水分摄入。\n\n参考文献：WSAVA Global Nutrition Guidelines, 2022");
        ds2.setCalorieRecommendation(230);
        ds2.setFoodType("全价猫粮（干湿搭配）");
        ds2.setFeedingFrequency("每日3-4次");
        ds2.setReferenceSource("WSAVA, FEDIAF");
        ds2.setGeneratedTime(LocalDateTime.now());
        dietSuggestionRepository.save(ds2);
    }

    private void createExerciseData(Pet pet1, Pet pet2) {
        for (int i = 0; i < 7; i++) {
            ExerciseData e1 = new ExerciseData();
            e1.setPet(pet1);
            e1.setRecordTime(LocalDateTime.now().minusDays(i));
            e1.setSteps(8000 + (int)(Math.random() * 4000));
            e1.setDistance(BigDecimal.valueOf(3 + Math.random() * 2));
            e1.setCaloriesBurned(BigDecimal.valueOf(150 + Math.random() * 100));
            e1.setActiveMinutes(60 + (int)(Math.random() * 30));
            e1.setDeviceType("FitBark");
            e1.setDeviceId("COLLAR_WANGCAI");
            e1.setDataSource("THIRD_PARTY");
            e1.setSyncTime(LocalDateTime.now().minusDays(i));
            exerciseDataRepository.save(e1);

            ExerciseData e2 = new ExerciseData();
            e2.setPet(pet2);
            e2.setRecordTime(LocalDateTime.now().minusDays(i));
            e2.setSteps(2000 + (int)(Math.random() * 1000));
            e2.setDistance(BigDecimal.valueOf(0.5 + Math.random() * 0.5));
            e2.setCaloriesBurned(BigDecimal.valueOf(30 + Math.random() * 20));
            e2.setActiveMinutes(30 + (int)(Math.random() * 20));
            e2.setDeviceType("Whistle");
            e2.setDeviceId("COLLAR_MIMI");
            e2.setDataSource("THIRD_PARTY");
            e2.setSyncTime(LocalDateTime.now().minusDays(i));
            exerciseDataRepository.save(e2);
        }
    }

    private void createPosts(User user) {
        Post p1 = new Post();
        p1.setUser(user);
        p1.setTitle("金毛幼犬喂养心得分享");
        p1.setContent("分享一下我家金毛从小到大的喂养经验，选择优质狗粮很重要，定期体检也不能少。建议大家都去正规宠物医院咨询专业意见，不要轻信网上的偏方哦！");
        p1.setPostType("experience");
        p1.setTags("金毛,喂养,狗粮");
        p1.setViewCount(156);
        p1.setLikeCount(32);
        p1.setCommentCount(8);
        p1.setStatus("APPROVED");
        postRepository.save(p1);

        Post p2 = new Post();
        p2.setUser(user);
        p2.setTitle("猫咪春季驱虫注意事项");
        p2.setContent("春季是寄生虫高发期，各位铲屎官要记得按时给猫咪驱虫。体内外同驱效果更好，建议咨询兽医选择合适的驱虫药。");
        p2.setPostType("knowledge");
        p2.setTags("猫咪,驱虫,春季");
        p2.setViewCount(89);
        p2.setLikeCount(25);
        p2.setCommentCount(5);
        p2.setStatus("APPROVED");
        postRepository.save(p2);

        Post p3 = new Post();
        p3.setUser(user);
        p3.setTitle("待审核测试帖子");
        p3.setContent("这是一个待审核的帖子内容，正常内容应该可以通过审核。");
        p3.setPostType("discussion");
        p3.setTags("测试");
        p3.setViewCount(0);
        p3.setLikeCount(0);
        p3.setCommentCount(0);
        p3.setStatus("PENDING");
        postRepository.save(p3);
    }

    private void createHospitals() {
        PetHospital h1 = new PetHospital();
        h1.setName("阳光宠物医院");
        h1.setAddress("北京市朝阳区建国路88号");
        h1.setProvince("北京市");
        h1.setCity("北京市");
        h1.setDistrict("朝阳区");
        h1.setPhone("010-88888888");
        h1.setLatitude(new BigDecimal("39.9042"));
        h1.setLongitude(new BigDecimal("116.4074"));
        h1.setBusinessHours("周一至周日 9:00-21:00");
        h1.setServices("常规体检,疫苗接种,外科手术,急诊");
        h1.setAverageRating(new BigDecimal("4.8"));
        h1.setRatingCount(156);
        h1.setIs24h(false);
        h1.setIsEmergency(true);
        h1.setStatus("ACTIVE");
        petHospitalRepository.save(h1);

        PetHospital h2 = new PetHospital();
        h2.setName("爱心宠物诊所");
        h2.setAddress("北京市海淀区中关村大街1号");
        h2.setProvince("北京市");
        h2.setCity("北京市");
        h2.setDistrict("海淀区");
        h2.setPhone("010-66666666");
        h2.setLatitude(new BigDecimal("39.9847"));
        h2.setLongitude(new BigDecimal("116.3046"));
        h2.setBusinessHours("周一至周日 8:00-22:00");
        h2.setServices("常规体检,疫苗接种,内科诊疗,美容");
        h2.setAverageRating(new BigDecimal("4.6"));
        h2.setRatingCount(89);
        h2.setIs24h(true);
        h2.setIsEmergency(true);
        h2.setStatus("ACTIVE");
        petHospitalRepository.save(h2);

        PetHospital h3 = new PetHospital();
        h3.setName("宠爱动物医院");
        h3.setAddress("北京市西城区金融街2号");
        h3.setProvince("北京市");
        h3.setCity("北京市");
        h3.setDistrict("西城区");
        h3.setPhone("010-77777777");
        h3.setLatitude(new BigDecimal("39.9128"));
        h3.setLongitude(new BigDecimal("116.3634"));
        h3.setBusinessHours("周一至周日 9:00-20:00");
        h3.setServices("常规体检,疫苗接种,牙科诊疗,转诊服务");
        h3.setAverageRating(new BigDecimal("4.9"));
        h3.setRatingCount(234);
        h3.setIs24h(false);
        h3.setIsEmergency(false);
        h3.setStatus("ACTIVE");
        petHospitalRepository.save(h3);
    }
}