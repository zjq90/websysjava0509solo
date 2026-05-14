package com.appsys.config;

import com.appsys.entity.*;
import com.appsys.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberLevelRepository memberLevelRepository;
    private final UserRepository userRepository;
    private final PointGoodsRepository pointGoodsRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberLevelRepository memberLevelRepository,
                           UserRepository userRepository,
                           PointGoodsRepository pointGoodsRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.memberLevelRepository = memberLevelRepository;
        this.userRepository = userRepository;
        this.pointGoodsRepository = pointGoodsRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (memberLevelRepository.count() == 0) {
            initMemberLevels();
        }
        if (userRepository.count() == 0) {
            initUsers();
        }
        if (pointGoodsRepository.count() == 0) {
            initPointGoods();
        }
        if (notificationRepository.count() == 0) {
            initNotifications();
        }
    }

    private void initMemberLevels() {
        MemberLevel level1 = new MemberLevel();
        level1.setName("普通会员");
        level1.setLevel(1);
        level1.setRequiredGrowth(0);
        level1.setPointMultiplier(new BigDecimal("1.0"));
        level1.setPriorityService(false);
        level1.setHasAccountManager(false);
        level1.setBenefits("基础会员权益，享受1倍积分");
        memberLevelRepository.save(level1);

        MemberLevel level2 = new MemberLevel();
        level2.setName("银卡会员");
        level2.setLevel(2);
        level2.setRequiredGrowth(1000);
        level2.setPointMultiplier(new BigDecimal("1.2"));
        level2.setPriorityService(false);
        level2.setHasAccountManager(false);
        level2.setBenefits("享受1.2倍积分，生日礼包");
        memberLevelRepository.save(level2);

        MemberLevel level3 = new MemberLevel();
        level3.setName("金卡会员");
        level3.setLevel(3);
        level3.setRequiredGrowth(5000);
        level3.setPointMultiplier(new BigDecimal("1.5"));
        level3.setPriorityService(true);
        level3.setHasAccountManager(false);
        level3.setBenefits("享受1.5倍积分，优先客服通道，专属优惠活动");
        memberLevelRepository.save(level3);

        MemberLevel level4 = new MemberLevel();
        level4.setName("钻石会员");
        level4.setLevel(4);
        level4.setRequiredGrowth(20000);
        level4.setPointMultiplier(new BigDecimal("2.0"));
        level4.setPriorityService(true);
        level4.setHasAccountManager(true);
        level4.setBenefits("享受2.0倍积分，专属客户经理，VIP专属服务通道");
        memberLevelRepository.save(level4);
    }

    private void initUsers() {
        User user1 = new User();
        user1.setUsername("zhangsan");
        user1.setPassword(passwordEncoder.encode("123456"));
        user1.setPhone("13800138000");
        user1.setRealName("张三");
        user1.setEmail("zhangsan@example.com");
        user1.setMemberLevelId(1L);
        user1.setPoints(500);
        user1.setGrowth(500);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("lisi");
        user2.setPassword(passwordEncoder.encode("123456"));
        user2.setPhone("13800138001");
        user2.setRealName("李四");
        user2.setEmail("lisi@example.com");
        user2.setMemberLevelId(3L);
        user2.setPoints(3500);
        user2.setGrowth(6000);
        user2.setAccountManager("王经理-13800138888");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("wangwu");
        user3.setPassword(passwordEncoder.encode("123456"));
        user3.setPhone("13800138002");
        user3.setRealName("王五");
        user3.setEmail("wangwu@example.com");
        user3.setMemberLevelId(4L);
        user3.setPoints(12500);
        user3.setGrowth(25000);
        user3.setAccountManager("李经理-13800138999");
        userRepository.save(user3);
    }

    private void initPointGoods() {
        PointGoods goods1 = new PointGoods();
        goods1.setName("10元话费");
        goods1.setDescription("中国移动/联通/电信通用话费");
        goods1.setPoints(1000);
        goods1.setType(1);
        goods1.setValue("10元");
        goods1.setStock(100);
        goods1.setMinMemberLevel(1);
        goods1.setSort(1);
        pointGoodsRepository.save(goods1);

        PointGoods goods2 = new PointGoods();
        goods2.setName("50元话费");
        goods2.setDescription("中国移动/联通/电信通用话费");
        goods2.setPoints(4500);
        goods2.setType(1);
        goods2.setValue("50元");
        goods2.setStock(50);
        goods2.setMinMemberLevel(2);
        goods2.setSort(2);
        pointGoodsRepository.save(goods2);

        PointGoods goods3 = new PointGoods();
        goods3.setName("腾讯视频VIP月卡");
        goods3.setDescription("腾讯视频会员月卡，自动充值");
        goods3.setPoints(2000);
        goods3.setType(2);
        goods3.setValue("30元");
        goods3.setStock(80);
        goods3.setMinMemberLevel(1);
        goods3.setSort(3);
        pointGoodsRepository.save(goods3);

        PointGoods goods4 = new PointGoods();
        goods4.setName("爱奇艺VIP季卡");
        goods4.setDescription("爱奇艺黄金会员季卡");
        goods4.setPoints(5000);
        goods4.setType(2);
        goods4.setValue("68元");
        goods4.setStock(40);
        goods4.setMinMemberLevel(3);
        goods4.setSort(4);
        pointGoodsRepository.save(goods4);

        PointGoods goods5 = new PointGoods();
        goods5.setName("品牌保温杯");
        goods5.setDescription("304不锈钢保温杯，500ml");
        goods5.setPoints(8000);
        goods5.setType(3);
        goods5.setValue("99元");
        goods5.setStock(30);
        goods5.setMinMemberLevel(2);
        goods5.setSort(5);
        pointGoodsRepository.save(goods5);

        PointGoods goods6 = new PointGoods();
        goods6.setName("蓝牙音箱");
        goods6.setDescription("便携式迷你蓝牙音箱");
        goods6.setPoints(15000);
        goods6.setType(3);
        goods6.setValue("199元");
        goods6.setStock(20);
        goods6.setMinMemberLevel(3);
        goods6.setSort(6);
        pointGoodsRepository.save(goods6);
    }

    private void initNotifications() {
        Notification notice1 = new Notification();
        notice1.setTitle("本月账单已生成");
        notice1.setContent("您的11月账单已生成，金额为128.50元，请及时查看。");
        notice1.setType(1);
        notice1.setUserId(1L);
        notificationRepository.save(notice1);

        Notification notice2 = new Notification();
        notice2.setTitle("服务办理进度更新");
        notice2.setContent("您办理的宽带升级服务已完成，当前网速已升级至500M。");
        notice2.setType(2);
        notice2.setUserId(1L);
        notificationRepository.save(notice2);

        Notification notice3 = new Notification();
        notice3.setTitle("双11积分兑换特惠活动");
        notice3.setContent("活动期间积分兑换8折优惠，快来兑换心仪的礼品吧！");
        notice3.setType(3);
        notificationRepository.save(notice3);

        Notification notice4 = new Notification();
        notice4.setTitle("账户登录提醒");
        notice4.setContent("您的账户于今日10:30在新设备登录，如非本人操作请及时修改密码。");
        notice4.setType(4);
        notice4.setUserId(2L);
        notificationRepository.save(notice4);
    }
}
