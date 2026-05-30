package com.gameplatform.config;

import com.gameplatform.entity.*;
import com.gameplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdSlotRepository adSlotRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private RevenueStatsRepository revenueStatsRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private PopupAnnouncementRepository popupAnnouncementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushTaskRepository pushTaskRepository;

    @Autowired
    private MessageRepository messageRepository;

    private Random random = new Random();

    @Override
    public void run(String... args) {
        initAdSlots();
        initAdvertisements();
        initRevenueStats();
        initGames();
        initMessageTemplates();
        initPopupAnnouncements();
        initUsers();
        initPushTasks();
    }

    private void initAdSlots() {
        if (adSlotRepository.count() > 0) return;

        String[][] slots = {
                {"LOADING_PAGE", "游戏加载页广告", "加载页", "游戏启动时展示的全屏广告"},
                {"HOME_BANNER", "首页Banner", "Banner", "首页顶部轮播广告位"},
                {"POPUP_AD", "弹窗广告", "弹窗", "应用内弹窗广告"},
                {"REWARD_VIDEO", "激励视频入口", "激励视频", "观看视频获取奖励的入口"}
        };

        for (String[] slot : slots) {
            AdSlot adSlot = new AdSlot();
            adSlot.setCode(slot[0]);
            adSlot.setName(slot[1]);
            adSlot.setSlotType(slot[2]);
            adSlot.setDescription(slot[3]);
            adSlot.setEnabled(true);
            adSlot.setDisplayFrequency("ALWAYS");
            if ("HOME_BANNER".equals(slot[0])) {
                adSlot.setWidth(750);
                adSlot.setHeight(200);
            } else if ("LOADING_PAGE".equals(slot[0])) {
                adSlot.setWidth(750);
                adSlot.setHeight(1334);
            }
            adSlotRepository.save(adSlot);
        }
    }

    private void initAdvertisements() {
        if (advertisementRepository.count() > 0) return;

        LocalDateTime now = LocalDateTime.now();
        String[][] ads = {
                {"1", "新春特惠活动", "IMAGE", "https://picsum.photos/750/1334?random=1", "https://example.com/newyear"},
                {"2", "新版本上线", "IMAGE", "https://picsum.photos/750/200?random=2", "https://example.com/update"},
                {"3", "限时充值返利", "IMAGE", "https://picsum.photos/600/400?random=3", "https://example.com/recharge"},
                {"4", "每日签到奖励", "VIDEO", "https://picsum.photos/600/400?random=4", "https://example.com/signin"},
                {"1", "五一活动推广", "H5", "https://picsum.photos/750/1334?random=5", "https://example.com/mayday"}
        };

        for (String[] ad : ads) {
            Advertisement advertisement = new Advertisement();
            advertisement.setAdSlotId(Long.parseLong(ad[0]));
            advertisement.setTitle(ad[1]);
            advertisement.setAdType(ad[2]);
            advertisement.setMaterialUrl(ad[3]);
            advertisement.setJumpUrl(ad[4]);
            advertisement.setStartTime(now.minusDays(7));
            advertisement.setEndTime(now.plusDays(30));
            advertisement.setDisplayStartTime(LocalTime.of(0, 0));
            advertisement.setDisplayEndTime(LocalTime.of(23, 59));
            advertisement.setDisplayFrequency("ALWAYS");
            advertisement.setActive(true);
            advertisement.setPriority(random.nextInt(10));
            advertisementRepository.save(advertisement);
        }
    }

    private void initRevenueStats() {
        if (revenueStatsRepository.count() > 0) return;

        LocalDate today = LocalDate.now();
        for (int slot = 1; slot <= 4; slot++) {
            for (int day = 0; day < 14; day++) {
                LocalDate date = today.minusDays(day);
                RevenueStats stats = new RevenueStats();
                stats.setAdSlotId((long) slot);
                stats.setStatDate(date);
                stats.setImpressions((long) (1000 + random.nextInt(5000)));
                stats.setClicks((long) (50 + random.nextInt(200)));
                stats.setRevenue(BigDecimal.valueOf(10 + random.nextDouble() * 100).setScale(2, BigDecimal.ROUND_HALF_UP));
                revenueStatsRepository.save(stats);
            }
        }
    }

    private void initGames() {
        if (gameRepository.count() > 0) return;

        String[][] games = {
                {"GAME001", "斗地主", "经典斗地主游戏", "观看后获得双倍积分"},
                {"GAME002", "麻将", "四川麻将", "观看后获得额外金币"},
                {"GAME003", "消消乐", "休闲消除游戏", "观看后获得无限体力30分钟"},
                {"GAME004", "捕鱼", "街机捕鱼游戏", "观看后获得高级炮券x5"},
                {"GAME005", "象棋", "中国象棋", "观看后获得悔棋卡x3"}
        };

        for (int i = 0; i < games.length; i++) {
            String[] gameData = games[i];
            Game game = new Game();
            game.setCode(gameData[0]);
            game.setName(gameData[1]);
            game.setDescription(gameData[2]);
            game.setIcon("https://picsum.photos/100/100?random=" + (i + 10));
            game.setEnabled(true);
            game.setRewardVideoEnabled(i < 3);
            game.setRewardContent(gameData[3]);
            game.setDailyWatchLimit(5 + random.nextInt(10));
            gameRepository.save(game);
        }
    }

    private void initMessageTemplates() {
        if (messageTemplateRepository.count() > 0) return;

        String[][] templates = {
                {"版本更新通知", "SYSTEM", "新版本v2.0已上线", "亲爱的玩家，我们的游戏已更新至v2.0版本！新增了以下内容：\n\n1. 全新游戏模式\n2. 优化了游戏体验\n3. 修复了已知bug\n\n立即登录体验吧！"},
                {"活动邀请", "MARKETING", "周末双倍积分活动", "尊敬的玩家：\n\n本周末（周六周日）全场游戏双倍积分！\n\n活动时间：本周六00:00-周日23:59\n\n不要错过哦！"},
                {"充值优惠", "MARKETING", "限时充值返利", "亲爱的玩家：\n\n限时充值返利活动开始了！\n\n充值满100元返20%\n充值满500元返30%\n充值满1000元返50%\n\n活动截止到本月底！"},
                {"系统维护通知", "SYSTEM", "系统维护通知", "尊敬的玩家：\n\n为了提供更好的服务，我们将于本周四凌晨2:00-4:00进行系统维护。\n\n维护期间将无法登录游戏，给您带来的不便敬请谅解。"},
                {"节日祝福", "MARKETING", "节日快乐！", "亲爱的玩家：\n\n在这个特别的日子里，祝您节日快乐！\n\n登录游戏领取节日专属礼包！"}
        };

        for (String[] templateData : templates) {
            MessageTemplate template = new MessageTemplate();
            template.setName(templateData[0]);
            template.setTemplateType(templateData[1]);
            template.setTitle(templateData[2]);
            template.setContent(templateData[3]);
            template.setSystem("SYSTEM".equals(templateData[1]));
            template.setEnabled(true);
            messageTemplateRepository.save(template);
        }
    }

    private void initPopupAnnouncements() {
        if (popupAnnouncementRepository.count() > 0) return;

        LocalDateTime now = LocalDateTime.now();
        String[][] announcements = {
                {"欢迎公告", "欢迎来到游戏平台！\n\n新用户注册即送100积分。", "LOGIN", "ONCE", "立即领取", "https://example.com/welcome"},
                {"活动公告", "🎉 限时活动进行中！\n\n参与活动赢取丰厚奖励。", "HOME", "DAILY", "查看详情", "https://example.com/activity"},
                {"版本更新", "新版本已发布！\n\n点击查看更新内容。", "LOGIN", "ONCE", "立即更新", "https://example.com/update"}
        };

        for (int i = 0; i < announcements.length; i++) {
            String[] ann = announcements[i];
            PopupAnnouncement pa = new PopupAnnouncement();
            pa.setTitle(ann[0]);
            pa.setContent(ann[1]);
            pa.setDisplayTrigger(ann[2]);
            pa.setDisplayFrequency(ann[3]);
            pa.setButtonText(ann[4]);
            pa.setJumpUrl(ann[5]);
            pa.setImageUrl("https://picsum.photos/600/400?random=" + (i + 20));
            pa.setStartTime(now.minusDays(1));
            pa.setEndTime(now.plusDays(30));
            pa.setActive(true);
            pa.setPriority(random.nextInt(10));
            pa.setViewCount((long) (100 + random.nextInt(1000)));
            pa.setClickCount((long) (10 + random.nextInt(100)));
            popupAnnouncementRepository.save(pa);
        }
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;

        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "郑十一", "王十二"};
        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setUsername("user" + (i + 1));
            user.setNickName(names[i % names.length] + (i / 10 > 0 ? i / 10 : ""));
            user.setMemberLevel(1 + random.nextInt(5));
            user.setLastActiveTime(LocalDateTime.now().minusHours(random.nextInt(72)));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    private void initPushTasks() {
        if (pushTaskRepository.count() > 0) return;

        LocalDateTime now = LocalDateTime.now();
        PushTask task1 = new PushTask();
        task1.setTitle("欢迎新用户");
        task1.setContent("欢迎加入我们的游戏平台！\n\n新用户专享福利已发放至您的账户，请查收。");
        task1.setTargetType("ALL");
        task1.setStatus("SENT");
        task1.setTimed(false);
        task1.setTotalUsers(50);
        task1.setDeliveredCount(50);
        task1.setOpenedCount(35);
        task1.setSentTime(now.minusDays(3));
        task1.setCreatedBy("admin");
        pushTaskRepository.save(task1);

        PushTask task2 = new PushTask();
        task2.setTitle("周末活动提醒");
        task2.setContent("本周末双倍积分活动即将开始，敬请期待！");
        task2.setTargetType("MEMBER_LEVEL");
        task2.setMemberLevel(2);
        task2.setActiveDays(7);
        task2.setStatus("PENDING");
        task2.setTimed(true);
        task2.setScheduledTime(now.plusDays(1));
        task2.setCreatedBy("admin");
        pushTaskRepository.save(task2);

        PushTask task3 = new PushTask();
        task3.setTitle("版本更新通知");
        task3.setContent("新版本已发布，点击查看详情。");
        task3.setTargetType("ACTIVE");
        task3.setActiveDays(3);
        task3.setStatus("SENT");
        task3.setTimed(false);
        task3.setTotalUsers(30);
        task3.setDeliveredCount(30);
        task3.setOpenedCount(22);
        task3.setSentTime(now.minusDays(1));
        task3.setCreatedBy("admin");
        pushTaskRepository.save(task3);

        for (int i = 1; i <= 10; i++) {
            Message message = new Message();
            message.setTaskId(1L);
            message.setUserId((long) i);
            message.setTitle("欢迎新用户");
            message.setContent("欢迎加入我们的游戏平台！\n\n新用户专享福利已发放至您的账户，请查收。");
            message.setSentTime(now.minusDays(3));
            message.setRead(i <= 7);
            if (i <= 7) {
                message.setReadTime(now.minusDays(3).plusHours(random.nextInt(24)));
            }
            messageRepository.save(message);
        }
    }
}
