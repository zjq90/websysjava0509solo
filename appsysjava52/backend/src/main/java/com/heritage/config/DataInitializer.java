package com.heritage.config;

import com.heritage.entity.*;
import com.heritage.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

/**
 * 数据初始化配置类
 * 应用启动时自动生成测试数据
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Configuration
public class DataInitializer {

    private final Random random = new Random();

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            ExpertRepository expertRepository,
            HeritageRepository heritageRepository,
            AuthenticationRequestRepository authRequestRepository,
            TraceRecordRepository traceRecordRepository,
            CollectionItemRepository collectionItemRepository,
            EnvironmentDataRepository environmentDataRepository,
            MaintenanceReminderRepository maintenanceReminderRepository) {

        return args -> {
            System.out.println("========== 开始初始化测试数据 ==========");

            // 1. 初始化用户数据
            initUsers(userRepository);

            // 2. 初始化专家数据
            initExperts(expertRepository, userRepository);

            // 3. 初始化文物数据
            initHeritages(heritageRepository);

            // 4. 初始化鉴定申请数据
            initAuthRequests(authRequestRepository, userRepository, expertRepository, heritageRepository);

            // 5. 初始化溯源记录数据
            initTraceRecords(traceRecordRepository, heritageRepository);

            // 6. 初始化收藏数据
            initCollections(collectionItemRepository, userRepository, heritageRepository);

            // 7. 初始化环境监测数据
            initEnvironmentData(environmentDataRepository, collectionItemRepository);

            // 8. 初始化保养提醒数据
            initMaintenanceReminders(maintenanceReminderRepository, collectionItemRepository);

            System.out.println("========== 测试数据初始化完成 ==========");
        };
    }

    private void initUsers(UserRepository repository) {
        if (repository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi"); // 123456
        admin.setNickname("管理员");
        admin.setPhone("13800138000");
        admin.setEmail("admin@heritage.com");
        admin.setUserType(3);
        admin.setStatus(1);
        repository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi");
        user.setNickname("收藏家小王");
        user.setPhone("13800138001");
        user.setEmail("user@heritage.com");
        user.setUserType(1);
        user.setStatus(1);
        repository.save(user);

        User expertUser = new User();
        expertUser.setUsername("expert");
        expertUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi");
        expertUser.setNickname("张专家");
        expertUser.setPhone("13800138002");
        expertUser.setEmail("expert@heritage.com");
        expertUser.setUserType(2);
        expertUser.setStatus(1);
        repository.save(expertUser);

        System.out.println("✓ 初始化用户数据完成，共3条");
    }

    private void initExperts(ExpertRepository repository, UserRepository userRepository) {
        if (repository.count() > 0) return;

        User expertUser = userRepository.findByUsername("expert").orElse(null);
        if (expertUser == null) return;

        Expert expert = new Expert();
        expert.setUserId(expertUser.getId());
        expert.setName("张教授");
        expert.setTitle("博士生导师");
        expert.setInstitution("故宫博物院");
        expert.setSpecialties("青铜器、陶瓷鉴定");
        expert.setBio("从事文物鉴定工作30年，参与多项国家级文物鉴定项目。资质：国家文物鉴定委员会委员,中国古陶瓷学会理事");
        expert.setExperienceYears(30);
        expert.setSuccessCount(156);
        expert.setStatus(1); // 1-已认证
        repository.save(expert);

        Expert expert2 = new Expert();
        expert2.setUserId(expertUser.getId() + 1);
        expert2.setName("李研究员");
        expert2.setTitle("研究员");
        expert2.setInstitution("国家博物馆");
        expert2.setSpecialties("书画、玉器鉴定");
        expert2.setBio("从事书画鉴定工作25年。资质：中国美术家协会会员");
        expert2.setExperienceYears(25);
        expert2.setSuccessCount(98);
        expert2.setStatus(1); // 1-已认证
        repository.save(expert2);

        System.out.println("✓ 初始化专家数据完成，共2条");
    }

    private void initHeritages(HeritageRepository repository) {
        if (repository.count() > 0) return;

        String[] names = {"司母戊鼎", "清明上河图", "翠玉白菜", "四羊方尊", "越王勾践剑", "马踏飞燕", "金缕玉衣", "曾侯乙编钟"};
        Integer[] categories = {1, 3, 4, 1, 1, 1, 4, 5}; // 1-青铜器，2-陶瓷，3-书画，4-玉器，5-杂项
        String[] periods = {"商代", "北宋", "清代", "商代", "春秋", "东汉", "西汉", "战国"};
        String[] materials = {"青铜", "绢本", "翡翠", "青铜", "青铜", "青铜", "玉片、金丝", "青铜"};
        String[] descriptions = {
            "后母戊鼎，又称司母戊大方鼎，是中国商代后期王室祭祀用的青铜方鼎。",
            "清明上河图，中国十大传世名画之一，为北宋风俗画。",
            "翠玉白菜，是与真实白菜相似度几乎百分之百的翡翠作品。",
            "四羊方尊是商朝晚期青铜礼器，祭祀用品。",
            "越王勾践剑，春秋晚期越国青铜器。",
            "马踏飞燕又名马超龙雀、铜奔马，为东汉青铜器。",
            "金缕玉衣是汉代规格最高的丧葬殓服。",
            "曾侯乙编钟，战国早期文物，中国首批禁止出国展览文物。"
        };

        for (int i = 0; i < names.length; i++) {
            Heritage heritage = new Heritage();
            heritage.setName(names[i]);
            heritage.setCategory(categories[i]);
            heritage.setPeriod(periods[i]);
            heritage.setMaterial(materials[i]);
            heritage.setDescription(descriptions[i]);
            heritage.setEstimatedValue(BigDecimal.valueOf(1000000 + random.nextInt(9000000)));
            heritage.setStatus(1);
            heritage.setIsPublic(1);
            if (i % 3 == 0) {
                heritage.setModel3d("3d_model_" + i + ".glb");
            }
            heritage.setViewCount(random.nextInt(10000));
            repository.save(heritage);
        }

        System.out.println("✓ 初始化文物数据完成，共" + names.length + "条");
    }

    private void initAuthRequests(AuthenticationRequestRepository repository, UserRepository userRepository, ExpertRepository expertRepository, HeritageRepository heritageRepository) {
        if (repository.count() > 0) return;

        User user = userRepository.findByUsername("user").orElse(null);
        Expert expert = expertRepository.findAll().get(0);
        Heritage heritage = heritageRepository.findAll().get(0);

        if (user == null || expert == null || heritage == null) return;

        String[] titles = {"家传青铜器鉴定", "清代玉佩鉴定", "古画真伪鉴定", "瓷器年代鉴定", "玉器品质鉴定"};
        String[] descriptions = {
            "家中祖传青铜器，需要鉴定年代和价值",
            "清代玉佩，求专家鉴定材质和年代",
            "收藏的古画，希望鉴定是否为真迹",
            "祖传瓷器，请求鉴定具体年代",
            "新购玉器，需要专家鉴定品质"
        };

        for (int i = 0; i < titles.length; i++) {
            AuthenticationRequest request = new AuthenticationRequest();
            request.setUserId(user.getId());
            request.setRequestNo("JD" + System.currentTimeMillis() + i);
            request.setHeritageName("待鉴定文物" + (i + 1));
            request.setCategory(i % 2 == 0 ? 1 : 4); // 1-青铜器，4-玉器
            request.setTitle(titles[i]);
            request.setDescription(descriptions[i]);
            request.setStatus(i < 2 ? 3 : i < 4 ? 2 : 1);
            if (i < 2) {
                request.setExpertId(expert.getId());
                request.setExpertOpinion("经鉴定，该" + (i % 2 == 0 ? "青铜器" : "玉器") + "为真品，具有较高收藏价值。");
                request.setResult(1);
                request.setEstimatedValue(new java.math.BigDecimal(50000 + i * 10000));
            }
            repository.save(request);
        }

        System.out.println("✓ 初始化鉴定申请数据完成，共" + titles.length + "条");
    }

    private void initTraceRecords(TraceRecordRepository repository, HeritageRepository heritageRepository) {
        if (repository.count() > 0) return;

        Heritage heritage = heritageRepository.findAll().get(0);
        if (heritage == null) return;

        String[] transactions = {
            "2020年 故宫博物院收藏",
            "1980年 某收藏家捐赠",
            "1949年 国家文物局接收",
            "1900年 清宫旧藏"
        };

        for (int i = 0; i < transactions.length; i++) {
            TraceRecord record = new TraceRecord();
            record.setHeritageId(heritage.getId());
            record.setTransactionType(1);
            record.setTransactionDesc(transactions[i]);
            record.setTransactionDate(LocalDateTime.now().minusYears(5 + i * 10));
            record.setPreviousOwner("历史收藏");
            record.setCurrentOwner(i == 0 ? "故宫博物院" : "国家文物局");
            record.setBlockchainHash("0x" + generateRandomHash());
            record.setVerified(true);
            repository.save(record);
        }

        System.out.println("✓ 初始化溯源记录数据完成，共" + transactions.length + "条");
    }

    private void initCollections(CollectionItemRepository repository, UserRepository userRepository, HeritageRepository heritageRepository) {
        if (repository.count() > 0) return;

        User user = userRepository.findByUsername("user").orElse(null);
        if (user == null) return;

        String[] locations = {"书房展示柜A区", "卧室保险柜", "地下室储藏室", "客厅展示架", "专用收藏室"};
        String[] customNames = {"司母戊鼎珍藏", "清明上河图摹本", "翠玉白菜摆件", "四羊方尊复刻", "越王勾践剑仿品"};

        java.util.List<Heritage> heritages = heritageRepository.findAll();
        for (int i = 0; i < Math.min(5, heritages.size()); i++) {
            CollectionItem item = new CollectionItem();
            item.setUserId(user.getId());
            item.setHeritageId(heritages.get(i).getId());
            item.setCustomName(customNames[i]);
            item.setCustomDescription(heritages.get(i).getDescription());
            item.setCategory(heritages.get(i).getCategory());
            item.setMaterial(heritages.get(i).getMaterial());
            item.setCollectionDate(java.time.LocalDate.now().minusMonths(i * 3));
            item.setEstimatedValue(heritages.get(i).getEstimatedValue());
            item.setEnvMonitorEnabled(i < 3 ? 1 : 0);
            item.setRemark("存放位置：" + locations[i]);
            repository.save(item);
        }

        System.out.println("✓ 初始化收藏数据完成，共5条");
    }

    private void initEnvironmentData(EnvironmentDataRepository repository, CollectionItemRepository collectionItemRepository) {
        if (repository.count() > 0) return;

        java.util.List<CollectionItem> items = collectionItemRepository.findByEnvMonitorEnabled(1);
        if (items.isEmpty()) return;

        for (CollectionItem item : items) {
            for (int i = 0; i < 24; i++) {
                EnvironmentData data = new EnvironmentData();
                data.setCollectionItemId(item.getId());
                data.setDeviceId("DEV_" + item.getId());
                data.setTemperature(java.math.BigDecimal.valueOf(20 + random.nextDouble() * 5));
                data.setHumidity(java.math.BigDecimal.valueOf(45 + random.nextDouble() * 15));
                data.setLightIntensity(java.math.BigDecimal.valueOf(100 + random.nextInt(400)));
                data.setRecordTime(LocalDateTime.now().minusHours(24 - i));
                data.setIsAbnormal(0);
                repository.save(data);
            }
        }

        System.out.println("✓ 初始化环境监测数据完成，共" + (items.size() * 24) + "条");
    }

    private void initMaintenanceReminders(MaintenanceReminderRepository repository, CollectionItemRepository collectionItemRepository) {
        if (repository.count() > 0) return;

        java.util.List<CollectionItem> items = collectionItemRepository.findAll();
        if (items.isEmpty()) return;

        Integer[] maintenanceTypes = {1, 2, 3, 4, 5}; // 1-日常清洁，2-防潮处理，3-防蛀处理，4-修复保养，5-其他
        String[] titles = {"日常清洁", "防潮处理", "温湿度监控", "防虫处理", "修复保养"};
        String[] contents = {
            "建议进行全面检查，检查是否有氧化或损坏情况",
            "建议进行专业清洁，保持文物外观整洁",
            "当前温湿度波动较大，建议调节环境参数",
            "建议进行防虫处理，防止虫害侵袭",
            "发现轻微瑕疵，建议进行专业修复维护"
        };
        String[] references = {
            "《青铜器保护修复技术规范》",
            "《馆藏文物清洁规范》",
            "《博物馆环境监控标准》",
            "《文物防虫技术指南》",
            "《文物修复操作规范》"
        };

        for (int i = 0; i < Math.min(3, items.size()); i++) {
            for (int j = 0; j < 2; j++) {
                MaintenanceReminder reminder = new MaintenanceReminder();
                reminder.setCollectionItemId(items.get(i).getId());
                reminder.setMaintenanceType(maintenanceTypes[j]);
                reminder.setTitle(titles[j] + " - " + items.get(i).getCustomName());
                reminder.setContent(contents[j]);
                reminder.setReference(references[j]);
                reminder.setScheduledDate(java.time.LocalDate.now().plusDays(j * 7));
                reminder.setStatus(j == 0 ? 2 : 1);
                repository.save(reminder);
            }
        }

        System.out.println("✓ 初始化保养提醒数据完成，共6条");
    }

    private String generateRandomHash() {
        String chars = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(64);
        for (int i = 0; i < 64; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
