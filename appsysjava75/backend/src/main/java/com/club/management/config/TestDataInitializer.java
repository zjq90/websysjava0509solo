package com.club.management.config;

import com.club.management.club.entity.Club;
import com.club.management.club.entity.ClubMember;
import com.club.management.club.repository.ClubMemberRepository;
import com.club.management.club.repository.ClubRepository;
import com.club.management.fund.entity.FundRecord;
import com.club.management.fund.entity.Reimbursement;
import com.club.management.fund.repository.FundRecordRepository;
import com.club.management.fund.repository.ReimbursementRepository;
import com.club.management.message.entity.*;
import com.club.management.message.repository.*;
import com.club.management.resource.entity.CooperationApply;
import com.club.management.resource.entity.EnterpriseCooperation;
import com.club.management.resource.entity.ResourceShare;
import com.club.management.resource.repository.CooperationApplyRepository;
import com.club.management.resource.repository.EnterpriseCooperationRepository;
import com.club.management.resource.repository.ResourceShareRepository;
import com.club.management.square.entity.*;
import com.club.management.square.repository.*;
import com.club.management.system.entity.User;
import com.club.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final SystemMessageRepository systemMessageRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final ChatGroupMemberRepository chatGroupMemberRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final ActivityPostRepository activityPostRepository;
    private final TopicRepository topicRepository;
    private final TopicPostRepository topicPostRepository;
    private final SeniorShareRepository seniorShareRepository;
    private final CommentRepository commentRepository;
    private final FundRecordRepository fundRecordRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final ResourceShareRepository resourceShareRepository;
    private final EnterpriseCooperationRepository enterpriseCooperationRepository;
    private final CooperationApplyRepository cooperationApplyRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) {
        log.info("开始初始化测试数据...");

        initUsers();
        initClubs();
        initClubMembers();
        initSystemMessages();
        initChatGroups();
        initGroupMessages();
        initPrivateMessages();
        initActivityPosts();
        initTopics();
        initTopicPosts();
        initSeniorShares();
        initComments();
        initFundRecords();
        initReimbursements();
        initResourceShares();
        initEnterpriseCooperations();
        initCooperationApplies();

        log.info("测试数据初始化完成！");
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;
        log.info("初始化用户数据...");

        String[] avatars = {
                "https://api.dicebear.com/7.x/avataaars/svg?seed=1",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=3",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=4",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=5",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=6",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=7",
                "https://api.dicebear.com/7.x/avataaars/svg?seed=8"
        };

        String[][] userData = {
                {"admin", "123456", "系统管理员", "00000001", "13800138000", "admin@club.com", "1", "2021", "计算机科学与技术", "计算机学院", "1"},
                {"zhangsan", "123456", "张三", "2021001", "13800138001", "zhangsan@club.com", "1", "2021", "软件工程", "计算机学院", "2"},
                {"lisi", "123456", "李四", "2021002", "13800138002", "lisi@club.com", "2", "2021", "计算机科学与技术", "计算机学院", "2"},
                {"wangwu", "123456", "王五", "2022001", "13800138003", "wangwu@club.com", "1", "2022", "电子信息工程", "电子信息学院", "2"},
                {"zhaoliu", "123456", "赵六", "2022002", "13800138004", "zhaoliu@club.com", "2", "2022", "通信工程", "电子信息学院", "0"},
                {"sunqi", "123456", "孙七", "2023001", "13800138005", "sunqi@club.com", "1", "2023", "机械设计", "机械工程学院", "0"},
                {"zhouba", "123456", "周八", "2023002", "13800138006", "zhouba@club.com", "2", "2023", "会计学", "经济管理学院", "0"},
                {"wujiu", "123456", "吴九", "2020001", "13800138007", "wujiu@club.com", "1", "2020", "软件工程", "计算机学院", "0"}
        };

        for (int i = 0; i < userData.length; i++) {
            User user = new User();
            user.setUsername(userData[i][0]);
            user.setPassword(userData[i][1]);
            user.setRealName(userData[i][2]);
            user.setStudentNo(userData[i][3]);
            user.setAvatar(avatars[i]);
            user.setPhone(userData[i][4]);
            user.setEmail(userData[i][5]);
            user.setGender(Integer.parseInt(userData[i][6]));
            user.setGrade(userData[i][7]);
            user.setMajor(userData[i][8]);
            user.setCollege(userData[i][9]);
            user.setUserType(Integer.parseInt(userData[i][10]));
            user.setStatus(1);
            user.setBio("热爱生活，热爱社团活动！");
            user.setCreateBy("system");
            userRepository.save(user);
        }
        log.info("用户数据初始化完成，共{}条", userData.length);
    }

    private void initClubs() {
        if (clubRepository.count() > 0) return;
        log.info("初始化社团数据...");

        String[][] clubData = {
                {"计算机协会", "https://api.dicebear.com/7.x/icons/svg?seed=it", "0", "致力于推广计算机技术，提高成员的编程能力和创新思维",
                        "技术改变世界，创新引领未来", "2", "张三", "李教授", "50", "2018-09-01", "1", "欢迎加入计算机协会！"},
                {"科技创新社", "https://api.dicebear.com/7.x/icons/svg?seed=tech", "3", "专注于科技创新项目研发，培养学生创新能力",
                        "创新驱动发展，科技引领未来", "3", "王五", "王教授", "35", "2019-03-15", "1", "让创新成为习惯！"},
                {"文学社", "https://api.dicebear.com/7.x/icons/svg?seed=liter", "4", "弘扬文学艺术，丰富校园文化生活",
                        "以文会友，以笔抒怀", "4", "周八", "张教授", "40", "2017-10-20", "1", "文学让生活更美好！"}
        };

        for (String[] data : clubData) {
            Club club = new Club();
            club.setName(data[0]);
            club.setLogo(data[1]);
            club.setType(Integer.parseInt(data[2]));
            club.setDescription(data[3]);
            club.setPurpose(data[4]);
            club.setLeaderId(Long.parseLong(data[5]));
            club.setLeaderName(data[6]);
            club.setAdvisor(data[7]);
            club.setMemberCount(Integer.parseInt(data[8]));
            club.setEstablishDate(data[9]);
            club.setStatus(Integer.parseInt(data[10]));
            club.setAnnouncement(data[11]);
            club.setCreateBy("system");
            clubRepository.save(club);
        }
        log.info("社团数据初始化完成，共{}条", clubData.length);
    }

    private void initClubMembers() {
        if (clubMemberRepository.count() > 0) return;
        log.info("初始化社团成员数据...");

        for (long clubId = 1; clubId <= 3; clubId++) {
            for (long userId = 2; userId <= 7; userId++) {
                ClubMember member = new ClubMember();
                member.setClubId(clubId);
                member.setUserId(userId);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    member.setUsername(user.getUsername());
                    member.setRealName(user.getRealName());
                    member.setAvatar(user.getAvatar());
                }
                if (userId == clubId + 1) {
                    member.setPosition(4);
                    member.setPositionName("会长");
                } else if (userId == clubId + 2) {
                    member.setPosition(3);
                    member.setPositionName("副会长");
                } else if (userId == clubId + 3) {
                    member.setPosition(2);
                    member.setPositionName("部长");
                } else {
                    member.setPosition(0);
                    member.setPositionName("普通成员");
                }
                String[] departments = {"技术部", "宣传部", "外联部", "策划部", "财务部"};
                member.setDepartment(departments[(int) (userId - 2) % 5]);
                member.setJoinDate("2023-09-01");
                member.setStatus(1);
                member.setCreateBy("system");
                clubMemberRepository.save(member);
            }
        }
        log.info("社团成员数据初始化完成");
    }

    private void initSystemMessages() {
        if (systemMessageRepository.count() > 0) return;
        log.info("初始化系统消息数据...");

        String[][] messageData = {
                {"0", "社团招新审核结果通知", "恭喜您！您申请加入的计算机协会已审核通过，请及时参加新人见面会。", "2", "", "1", "club_join"},
                {"1", "活动开始提醒", "您报名参加的\"编程大赛培训\"活动将于明天下午2点在计算机楼301室开始，请准时参加。", "2", "", "2", "activity"},
                {"2", "社团公告", "计算机协会本周六将组织户外拓展活动，请各位成员准时参加！", "", "1", "1", "club_notice"},
                {"3", "招新进度通知", "社团招新工作已完成80%，还剩余20个名额，请有意向的同学尽快报名。", "", "1", "1", "recruit"},
                {"0", "活动审批结果通知", "您申请的\"编程技术分享会\"活动已通过审批，请按计划执行。", "2", "", "1", "activity_approve"}
        };

        for (int i = 0; i < messageData.length; i++) {
            SystemMessage message = new SystemMessage();
            message.setMessageType(Integer.parseInt(messageData[i][0]));
            message.setTitle(messageData[i][1]);
            message.setContent(messageData[i][2]);
            if (messageData[i][3] != null && !messageData[i][3].isEmpty()) {
                message.setReceiverId(Long.parseLong(messageData[i][3]));
            }
            if (messageData[i][4] != null && !messageData[i][4].isEmpty()) {
                message.setClubId(Long.parseLong(messageData[i][4]));
            }
            message.setBusinessId(Long.parseLong(messageData[i][5]));
            message.setBusinessType(messageData[i][6]);
            message.setIsRead(i < 2 ? 1 : 0);
            message.setIsImportant(i == 0 ? 1 : 0);
            message.setReadCount(0);
            message.setTotalCount(50);
            message.setPushTime(LocalDateTime.now().minusHours(i).format(DATE_FORMATTER));
            message.setCreateBy("system");
            systemMessageRepository.save(message);
        }
        log.info("系统消息数据初始化完成，共{}条", messageData.length);
    }

    private void initChatGroups() {
        if (chatGroupRepository.count() > 0) return;
        log.info("初始化群聊数据...");

        String[][] groupData = {
                {"计算机协会交流群", "https://api.dicebear.com/7.x/icons/svg?seed=group1", "计算机协会官方交流群", "1", "全体成员", "2"},
                {"计算机协会-技术部", "https://api.dicebear.com/7.x/icons/svg?seed=group2", "技术部内部工作群", "1", "技术部", "2"},
                {"科技创新社官方群", "https://api.dicebear.com/7.x/icons/svg?seed=group3", "科技创新社官方交流群", "2", "全体成员", "3"},
                {"文学社交流群", "https://api.dicebear.com/7.x/icons/svg?seed=group4", "文学社官方交流群", "3", "全体成员", "4"}
        };

        for (String[] data : groupData) {
            ChatGroup group = new ChatGroup();
            group.setGroupName(data[0]);
            group.setGroupAvatar(data[1]);
            group.setGroupDesc(data[2]);
            group.setClubId(Long.parseLong(data[3]));
            group.setDepartment(data[4]);
            group.setOwnerId(Long.parseLong(data[5]));
            group.setMemberCount(6);
            group.setStatus(0);
            group.setAnnouncement("欢迎加入本群，请注意文明发言！");
            group.setLastMessage("大家好，欢迎加入群聊！");
            group.setLastMessageTime(LocalDateTime.now().format(DATE_FORMATTER));
            group.setCreateBy("system");
            ChatGroup saved = chatGroupRepository.save(group);

            for (long userId = 2; userId <= 7; userId++) {
                ChatGroupMember member = new ChatGroupMember();
                member.setGroupId(saved.getId());
                member.setUserId(userId);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    member.setUsername(user.getUsername());
                    member.setRealName(user.getRealName());
                    member.setAvatar(user.getAvatar());
                    member.setNickname(user.getRealName());
                }
                member.setRole(userId == Long.parseLong(data[5]) ? 2 : 0);
                member.setUnreadCount((int) (Math.random() * 10));
                member.setIsMute(0);
                member.setIsTop(0);
                member.setStatus(0);
                member.setCreateBy("system");
                chatGroupMemberRepository.save(member);
            }
        }
        log.info("群聊数据初始化完成，共{}条", groupData.length);
    }

    private void initGroupMessages() {
        if (groupMessageRepository.count() > 0) return;
        log.info("初始化群消息数据...");

        String[] contents = {
                "大家好，新人报到，请多关照！",
                "本周的活动大家有什么建议吗？",
                "刚刚看完那个技术文档，受益匪浅！",
                "有人能帮我看看这个代码问题吗？",
                "明天的会议大家别忘了参加哦",
                "分享一个很棒的学习资源给大家",
                "这个问题我知道怎么解决，我来帮你",
                "最近有什么新的活动计划吗？",
                "大家周末一起聚餐怎么样？",
                "感谢大家的支持，我们的活动圆满成功！"
        };

        for (long groupId = 1; groupId <= 4; groupId++) {
            for (int i = 0; i < 10; i++) {
                GroupMessage message = new GroupMessage();
                message.setGroupId(groupId);
                long senderId = (i % 6) + 2;
                message.setSenderId(senderId);
                User user = userRepository.findById(senderId).orElse(null);
                if (user != null) {
                    message.setSenderName(user.getRealName());
                    message.setSenderAvatar(user.getAvatar());
                }
                message.setMessageType(i % 5 == 0 ? 1 : 0);
                message.setContent(contents[i]);
                if (i % 5 == 0) {
                    message.setFileUrl("https://picsum.photos/400/300?random=" + i);
                }
                message.setReadCount((int) (Math.random() * 6));
                message.setTotalCount(6);
                message.setIsRecall(0);
                message.setCreateBy(user != null ? user.getUsername() : "system");
                groupMessageRepository.save(message);
            }
        }
        log.info("群消息数据初始化完成");
    }

    private void initPrivateMessages() {
        if (privateMessageRepository.count() > 0) return;
        log.info("初始化私聊消息数据...");

        String[][] messageData = {
                {"2", "3", "你好，关于明天的活动我想咨询一下", "0"},
                {"3", "2", "你好，请问有什么问题？", "1"},
                {"2", "3", "明天的活动需要准备什么材料吗？", "0"},
                {"3", "2", "只需要带笔记本电脑就可以了", "1"},
                {"2", "3", "好的，谢谢！", "0"},
                {"4", "5", "上次的项目文档我已经发到你邮箱了", "1"},
                {"5", "4", "收到，我看一下，有问题再跟你说", "1"},
                {"6", "7", "你好，能帮我看一下这份策划书吗？", "0"},
                {"7", "6", "好的，我抽空看看", "1"}
        };

        for (String[] data : messageData) {
            PrivateMessage message = new PrivateMessage();
            long senderId = Long.parseLong(data[0]);
            long receiverId = Long.parseLong(data[1]);
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);

            User sender = userRepository.findById(senderId).orElse(null);
            User receiver = userRepository.findById(receiverId).orElse(null);
            if (sender != null) {
                message.setSenderName(sender.getRealName());
                message.setSenderAvatar(sender.getAvatar());
            }
            if (receiver != null) {
                message.setReceiverName(receiver.getRealName());
                message.setReceiverAvatar(receiver.getAvatar());
            }

            message.setContent(data[2]);
            message.setMessageType(0);
            message.setIsRead(Integer.parseInt(data[3]));
            message.setIsRecall(0);
            message.setCreateBy(sender != null ? sender.getUsername() : "system");
            privateMessageRepository.save(message);
        }
        log.info("私聊消息数据初始化完成，共{}条", messageData.length);
    }

    private void initActivityPosts() {
        if (activityPostRepository.count() > 0) return;
        log.info("初始化活动圈动态数据...");

        String[][] postData = {
                {"2", "张三", "1", "编程大赛培训", "1", "计算机协会", "今天参加了编程大赛的培训，收获满满！老师讲了很多实用的算法技巧，期待下次培训。",
                        "https://picsum.photos/800/600?random=1,https://picsum.photos/800/600?random=2"},
                {"3", "李四", "2", "科技创新大赛", "2", "科技创新社", "我们的项目进入决赛了！感谢团队的努力，继续加油！",
                        "https://picsum.photos/800/600?random=3"},
                {"4", "王五", "3", "户外拓展活动", "1", "计算机协会", "周末的户外拓展活动太好玩了，大家玩得很开心，增进了友谊！",
                        "https://picsum.photos/800/600?random=4,https://picsum.photos/800/600?random=5,https://picsum.photos/800/600?random=6"},
                {"5", "赵六", "4", "读书会", "3", "文学社", "今天的读书会分享了《百年孤独》，大家的讨论非常热烈，收获很多感悟。",
                        "https://picsum.photos/800/600?random=7"},
                {"6", "孙七", "5", "技术分享会", "1", "计算机协会", "第一次做技术分享，有点紧张但很开心！感谢大家的支持和鼓励。",
                        "https://picsum.photos/800/600?random=8,https://picsum.photos/800/600?random=9"}
        };

        for (int i = 0; i < postData.length; i++) {
            ActivityPost post = new ActivityPost();
            post.setUserId(Long.parseLong(postData[i][0]));
            post.setUsername(postData[i][0]);
            post.setRealName(postData[i][1]);
            User user = userRepository.findById(Long.parseLong(postData[i][0])).orElse(null);
            if (user != null) {
                post.setAvatar(user.getAvatar());
            }
            post.setActivityId(Long.parseLong(postData[i][2]));
            post.setActivityName(postData[i][3]);
            post.setClubId(Long.parseLong(postData[i][4]));
            post.setClubName(postData[i][5]);
            post.setContent(postData[i][6]);
            post.setImages(postData[i][7]);
            post.setLikeCount((int) (Math.random() * 50) + 10);
            post.setCommentCount((int) (Math.random() * 20) + 5);
            post.setViewCount((int) (Math.random() * 200) + 50);
            post.setStatus(0);
            post.setLocation(i % 2 == 0 ? "学校图书馆" : "校外活动中心");
            post.setCreateBy(postData[i][0]);
            activityPostRepository.save(post);
        }
        log.info("活动圈动态数据初始化完成，共{}条", postData.length);
    }

    private void initTopics() {
        if (topicRepository.count() > 0) return;
        log.info("初始化话题数据...");

        String[][] topicData = {
                {"#社团生活日常", "https://picsum.photos/400/300?topic=1", "0", "分享你的社团生活日常，记录美好时光", "1", "张三"},
                {"#编程学习交流", "https://picsum.photos/400/300?topic=2", "1", "交流编程学习心得，共同进步", "2", "李四"},
                {"#就业经验分享", "https://picsum.photos/400/300?topic=3", "2", "分享找工作的经验和技巧", "1", "王五"},
                {"#考研交流", "https://picsum.photos/400/300?topic=4", "1", "考研路上我们一起加油", "3", "赵六"},
                {"#摄影爱好者", "https://picsum.photos/400/300?topic=5", "3", "分享你的摄影作品，交流摄影技巧", "2", "孙七"},
                {"#美食探店", "https://picsum.photos/400/300?topic=6", "3", "发现校园周边美食，一起去探店", "4", "周八"}
        };

        for (int i = 0; i < topicData.length; i++) {
            Topic topic = new Topic();
            topic.setName(topicData[i][0]);
            topic.setCover(topicData[i][1]);
            topic.setCategory(Integer.parseInt(topicData[i][2]));
            topic.setDescription(topicData[i][3]);
            topic.setCreatorId(Long.parseLong(topicData[i][4]));
            topic.setCreatorName(topicData[i][5]);
            topic.setPostCount((int) (Math.random() * 100) + 20);
            topic.setFollowCount((int) (Math.random() * 500) + 100);
            topic.setViewCount((int) (Math.random() * 2000) + 500);
            topic.setIsHot(i < 3 ? 1 : 0);
            topic.setIsTop(i == 0 ? 1 : 0);
            topic.setStatus(0);
            topic.setCreateBy(topicData[i][4]);
            topicRepository.save(topic);
        }
        log.info("话题数据初始化完成，共{}条", topicData.length);
    }

    private void initTopicPosts() {
        if (topicPostRepository.count() > 0) return;
        log.info("初始化话题帖子数据...");

        String[][] postData = {
                {"1", "#社团生活日常", "2", "zhangsan", "张三", "加入社团一年了，说说我的收获",
                        "从大一开始加入计算机协会，一年的时间里，我学到了很多技术知识，也认识了很多志同道合的朋友。社团的活动很丰富，每周的技术分享会让我受益匪浅。",
                        "https://picsum.photos/800/600?post=1"},
                {"2", "#编程学习交流", "3", "lisi", "李四", "Java学习路线分享，适合零基础的同学",
                        "作为一个Java开发的学习者，我整理了一份适合零基础同学的学习路线。从基础语法到框架应用，循序渐进。希望对大家有帮助！",
                        "https://picsum.photos/800/600?post=2"},
                {"3", "#就业经验分享", "4", "wangwu", "王五", "我的秋招经验总结，拿到了心仪的offer",
                        "秋招终于结束了，拿到了心仪公司的offer。在这里分享一下我的面试经验和准备过程，希望对正在找工作的同学有帮助。",
                        "https://picsum.photos/800/600?post=3"},
                {"2", "#编程学习交流", "5", "zhaoliu", "赵六", "Python入门推荐，这几本书真的很棒",
                        "最近在学Python，看了几本书觉得非常不错，推荐给想要入门Python的同学们。这些书由浅入深，很适合初学者。",
                        "https://picsum.photos/800/600?post=4"},
                {"4", "#考研交流", "6", "sunqi", "孙七", "考研上岸经验分享，考研加油！",
                        "刚查到考研成绩，成功上岸了！分享一下我的备考经验和时间安排，希望能帮到正在备考的学弟学妹们。",
                        "https://picsum.photos/800/600?post=5"}
        };

        for (String[] data : postData) {
            TopicPost post = new TopicPost();
            post.setTopicId(Long.parseLong(data[0]));
            post.setTopicName(data[1]);
            post.setUserId(Long.parseLong(data[2]));
            post.setUsername(data[3]);
            post.setRealName(data[4]);
            User user = userRepository.findById(Long.parseLong(data[2])).orElse(null);
            if (user != null) {
                post.setAvatar(user.getAvatar());
            }
            post.setTitle(data[5]);
            post.setContent(data[6]);
            post.setImages(data[7]);
            post.setLikeCount((int) (Math.random() * 80) + 20);
            post.setCommentCount((int) (Math.random() * 30) + 10);
            post.setViewCount((int) (Math.random() * 500) + 100);
            post.setIsEssence(data[1].contains("编程学习") && data[5].contains("Java") ? 1 : 0);
            post.setIsTop(data[1].contains("社团生活") ? 1 : 0);
            post.setStatus(0);
            post.setCreateBy(data[3]);
            topicPostRepository.save(post);
        }
        log.info("话题帖子数据初始化完成，共{}条", postData.length);
    }

    private void initSeniorShares() {
        if (seniorShareRepository.count() > 0) return;
        log.info("初始化学长分享数据...");

        String[][] shareData = {
                {"从社团到职场：我的技术成长之路", "https://picsum.photos/800/400?share=1", "0",
                        "大家好，我是2020届的毕业生，在校期间曾任计算机协会会长。今天想和大家分享一下我的社团经历对我职业发展的帮助。",
                        "8", "吴九", "https://api.dicebear.com/7.x/avataaars/svg?seed=8",
                        "热爱技术，热爱分享", "2024", "字节跳动", "高级开发工程师", "计算机协会",
                        "https://picsum.photos/800/600?share=11", "", ""},
                {"互联网大厂面试经验全解析", "https://picsum.photos/800/400?share=2", "1",
                        "作为一个已经工作两年的学长，我想和大家分享一下互联网大厂的面试经验。从简历准备到技术面试，再到HR面，都有很多需要注意的地方。",
                        "8", "吴九", "https://api.dicebear.com/7.x/avataaars/svg?seed=8",
                        "专注后端开发，热爱开源", "2024", "阿里巴巴", "技术专家", "科技创新社",
                        "https://picsum.photos/800/600?share=21", "", ""},
                {"考研复习规划指南，帮你少走弯路", "https://picsum.photos/800/400?share=3", "2",
                        "考研是一场持久战，合理的复习规划非常重要。我将分享我的复习计划和时间安排，希望能帮助正在备考的同学们。",
                        "2", "张三", "https://api.dicebear.com/7.x/avataaars/svg?seed=2",
                        "考研上岸985", "2025", "清华大学（在读）", "硕士研究生", "计算机协会",
                        "https://picsum.photos/800/600?share=31", "", ""},
                {"程序员必备的高效学习方法", "https://picsum.photos/800/400?share=4", "3",
                        "作为程序员，持续学习是必不可少的。分享几个我总结的高效学习方法，帮助大家在有限的时间里学到更多知识。",
                        "3", "李四", "https://api.dicebear.com/7.x/avataaars/svg?seed=3",
                        "技术改变世界", "2025", "腾讯", "全栈开发工程师", "科技创新社",
                        "https://picsum.photos/800/600?share=41", "", ""}
        };

        for (int i = 0; i < shareData.length; i++) {
            SeniorShare share = new SeniorShare();
            share.setTitle(shareData[i][0]);
            share.setCover(shareData[i][1]);
            share.setCategory(Integer.parseInt(shareData[i][2]));
            share.setContent(shareData[i][3]);
            share.setAuthorId(Long.parseLong(shareData[i][4]));
            share.setAuthorName(shareData[i][5]);
            share.setAuthorAvatar(shareData[i][6]);
            share.setAuthorBio(shareData[i][7]);
            share.setGraduateYear(shareData[i][8]);
            share.setCompany(shareData[i][9]);
            share.setPosition(shareData[i][10]);
            share.setOriginalClub(shareData[i][11]);
            share.setImages(shareData[i][12]);
            share.setAttachmentUrl(shareData[i][13].isEmpty() ? null : shareData[i][13]);
            share.setAttachmentName(shareData[i][14].isEmpty() ? null : shareData[i][14]);
            share.setLikeCount((int) (Math.random() * 150) + 50);
            share.setCommentCount((int) (Math.random() * 50) + 20);
            share.setFavoriteCount((int) (Math.random() * 200) + 100);
            share.setViewCount((int) (Math.random() * 1000) + 300);
            share.setIsEssence(i < 2 ? 1 : 0);
            share.setIsTop(i == 0 ? 1 : 0);
            share.setStatus(0);
            share.setCreateBy(shareData[i][5]);
            seniorShareRepository.save(share);
        }
        log.info("学长分享数据初始化完成，共{}条", shareData.length);
    }

    private void initComments() {
        if (commentRepository.count() > 0) return;
        log.info("初始化评论数据...");

        String[] comments = {
                "写得太好了，学到了很多！",
                "感谢分享，非常实用的经验",
                "我也有同样的经历，感同身受",
                "请问能详细说说这个问题吗？",
                "太棒了，给楼主点赞！",
                "学到了，谢谢学长的分享",
                "我正在准备这个，正好需要",
                "说得很有道理，收藏了",
                "请问有相关的学习资料吗？",
                "期待更多这样的分享！"
        };

        for (int businessType = 0; businessType < 3; businessType++) {
            for (long businessId = 1; businessId <= 3; businessId++) {
                for (int i = 0; i < 5; i++) {
                    Comment comment = new Comment();
                    comment.setBusinessType(businessType);
                    comment.setBusinessId(businessId);
                    long userId = (i % 6) + 2;
                    comment.setUserId(userId);
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        comment.setUsername(user.getUsername());
                        comment.setRealName(user.getRealName());
                        comment.setAvatar(user.getAvatar());
                    }
                    comment.setContent(comments[i]);
                    comment.setLikeCount((int) (Math.random() * 20));
                    comment.setStatus(0);
                    comment.setCreateBy(user != null ? user.getUsername() : "system");
                    Comment saved = commentRepository.save(comment);

                    if (i == 0) {
                        Comment reply = new Comment();
                        reply.setBusinessType(businessType);
                        reply.setBusinessId(businessId);
                        reply.setParentId(saved.getId());
                        reply.setUserId(userId + 1 > 7 ? 2 : userId + 1);
                        reply.setReplyUserId(userId);
                        reply.setReplyUserName(user != null ? user.getRealName() : "");
                        User replyUser = userRepository.findById(reply.getUserId()).orElse(null);
                        if (replyUser != null) {
                            reply.setUsername(replyUser.getUsername());
                            reply.setRealName(replyUser.getRealName());
                            reply.setAvatar(replyUser.getAvatar());
                        }
                        reply.setContent("同意！我也这么觉得");
                        reply.setLikeCount((int) (Math.random() * 10));
                        reply.setStatus(0);
                        reply.setCreateBy(replyUser != null ? replyUser.getUsername() : "system");
                        commentRepository.save(reply);
                    }
                }
            }
        }
        log.info("评论数据初始化完成");
    }

    private void initFundRecords() {
        if (fundRecordRepository.count() > 0) return;
        log.info("初始化经费记录数据...");

        String[][] recordData = {
                {"1", "计算机协会", "1", "5000.0", "0", "会费", "2024-09-01", "2024年度会员会费收入", "2", "张三"},
                {"1", "计算机协会", "1", "3000.0", "1", "赞助", "2024-09-10", "科技公司赞助", "2", "张三"},
                {"1", "计算机协会", "0", "1500.0", "10", "活动物料", "2024-09-15", "编程大赛物资采购", "2", "张三"},
                {"1", "计算机协会", "0", "800.0", "11", "场地费", "2024-09-20", "活动场地租赁费用", "3", "李四"},
                {"1", "计算机协会", "0", "500.0", "12", "宣传费用", "2024-09-25", "招新海报及宣传品", "3", "李四"},
                {"2", "科技创新社", "1", "8000.0", "2", "拨款", "2024-09-01", "学校创新创业项目拨款", "3", "王五"},
                {"2", "科技创新社", "1", "2000.0", "1", "赞助", "2024-09-15", "企业合作赞助", "3", "王五"},
                {"2", "科技创新社", "0", "3000.0", "10", "活动物料", "2024-09-20", "比赛材料和设备", "4", "赵六"},
                {"2", "科技创新社", "0", "1200.0", "13", "差旅费用", "2024-09-28", "外出参加比赛差旅费", "4", "赵六"},
                {"3", "文学社", "1", "2000.0", "0", "会费", "2024-09-01", "2024年度会员会费", "4", "周八"},
                {"3", "文学社", "0", "600.0", "10", "活动物料", "2024-09-15", "读书会书籍采购", "4", "周八"},
                {"3", "文学社", "0", "400.0", "11", "场地费", "2024-09-20", "诗歌朗诵会场租", "6", "孙七"}
        };

        double balance1 = 0, balance2 = 0, balance3 = 0;
        for (String[] data : recordData) {
            FundRecord record = new FundRecord();
            record.setClubId(Long.parseLong(data[0]));
            record.setClubName(data[1]);
            record.setType(Integer.parseInt(data[2]));
            record.setAmount(Double.parseDouble(data[3]));
            record.setCategory(Integer.parseInt(data[4]));
            record.setCategoryName(data[5]);
            record.setOccurDate(data[6]);
            record.setSummary(data[7]);
            record.setHandlerId(Long.parseLong(data[8]));
            record.setHandlerName(data[9]);
            record.setStatus(1);
            record.setAuditorId(1L);
            record.setAuditorName("系统管理员");
            record.setAuditTime(LocalDateTime.now().format(DATE_FORMATTER));

            if (record.getClubId() == 1) {
                balance1 = record.getType() == 1 ? balance1 + record.getAmount() : balance1 - record.getAmount();
                record.setBalance(balance1);
            } else if (record.getClubId() == 2) {
                balance2 = record.getType() == 1 ? balance2 + record.getAmount() : balance2 - record.getAmount();
                record.setBalance(balance2);
            } else {
                balance3 = record.getType() == 1 ? balance3 + record.getAmount() : balance3 - record.getAmount();
                record.setBalance(balance3);
            }

            record.setCreateBy(data[9]);
            fundRecordRepository.save(record);
        }
        log.info("经费记录数据初始化完成，共{}条", recordData.length);
    }

    private void initReimbursements() {
        if (reimbursementRepository.count() > 0) return;
        log.info("初始化报销申请数据...");

        String[][] reimburseData = {
                {"1", "计算机协会", "5", "500.0", "10", "活动物料", "购买编程大赛奖状和奖品", "2024-10-01", "0"},
                {"1", "计算机协会", "6", "300.0", "12", "宣传费用", "印制技术分享会宣传海报", "2024-10-05", "1"},
                {"2", "科技创新社", "5", "800.0", "10", "活动物料", "购买创新比赛所需电子元件", "2024-10-02", "0"},
                {"2", "科技创新社", "6", "200.0", "13", "差旅费用", "参加比赛的交通费", "2024-10-08", "2"},
                {"3", "文学社", "7", "450.0", "10", "活动物料", "购买文学周活动书籍", "2024-10-03", "0"}
        };

        for (String[] data : reimburseData) {
            Reimbursement reimburse = new Reimbursement();
            reimburse.setClubId(Long.parseLong(data[0]));
            reimburse.setClubName(data[1]);
            reimburse.setApplicantId(Long.parseLong(data[2]));
            User user = userRepository.findById(reimburse.getApplicantId()).orElse(null);
            reimburse.setApplicantName(user != null ? user.getRealName() : "");
            reimburse.setAmount(Double.parseDouble(data[3]));
            reimburse.setCategory(Integer.parseInt(data[4]));
            reimburse.setCategoryName(data[5]);
            reimburse.setReason(data[6]);
            reimburse.setOccurDate(data[7]);
            reimburse.setStatus(Integer.parseInt(data[8]));
            reimburse.setCurrentNode(1);

            if (reimburse.getStatus() != 0) {
                reimburse.setAuditorId(1L);
                reimburse.setAuditorName("系统管理员");
                reimburse.setAuditTime(LocalDateTime.now().format(DATE_FORMATTER));
                reimburse.setAuditOpinion(reimburse.getStatus() == 1 ? "同意报销" : "发票不全，不予报销");
            }

            reimburse.setCreateBy(user != null ? user.getUsername() : "system");
            reimbursementRepository.save(reimburse);
        }
        log.info("报销申请数据初始化完成，共{}条", reimburseData.length);
    }

    private void initResourceShares() {
        if (resourceShareRepository.count() > 0) return;
        log.info("初始化资源分享数据...");

        String[][] resourceData = {
                {"Java面试宝典2024版", "https://picsum.photos/200/200?res=1", "0", "0",
                        "整理了Java面试常见的问题和答案，包含基础、集合、并发、JVM等方面",
                        "2", "张三", "1", "0", "https://example.com/java-interview.pdf",
                        "Java面试宝典.pdf", "1024000", "pdf", "https://picsum.photos/400/300?res=1",
                        "Java,面试,后端"},
                {"Python数据分析实战教程", "https://picsum.photos/200/200?res=2", "0", "1",
                        "使用Python进行数据分析的实战教程，包含numpy、pandas、matplotlib等库的使用",
                        "3", "李四", "1", "1", "https://example.com/python-data.mp4",
                        "Python数据分析实战.mp4", "512000000", "mp4", "https://picsum.photos/400/300?res=2",
                        "Python,数据分析,pandas"},
                {"算法竞赛入门指南", "https://picsum.photos/200/200?res=3", "1", "0",
                        "算法竞赛入门到进阶，包含常见算法和数据结构的讲解",
                        "4", "王五", "1", "0", "https://example.com/algorithm-book.pdf",
                        "算法竞赛入门指南.pdf", "2048000", "pdf", "https://picsum.photos/400/300?res=3",
                        "算法,数据结构,竞赛"},
                {"PS入门到精通视频教程", "https://picsum.photos/200/200?res=4", "2", "1",
                        "Photoshop从入门到精通的视频教程，适合零基础学习者",
                        "5", "赵六", "1", "1", "https://example.com/ps-tutorial.zip",
                        "PS教程全集.zip", "1024000000", "zip", "https://picsum.photos/400/300?res=4",
                        "PS,设计,图像处理"},
                {"考研英语词汇大全", "https://picsum.photos/200/200?res=5", "0", "0",
                        "考研英语5500词汇整理，附带记忆方法和例句",
                        "6", "孙七", "1", "0", "https://example.com/english-words.pdf",
                        "考研英语词汇大全.pdf", "1536000", "pdf", "https://picsum.photos/400/300?res=5",
                        "考研,英语,词汇"}
        };

        for (String[] data : resourceData) {
            ResourceShare resource = new ResourceShare();
            resource.setTitle(data[0]);
            resource.setCover(data[1]);
            resource.setCategory(Integer.parseInt(data[2]));
            resource.setResourceType(Integer.parseInt(data[3]));
            String[] categoryNames = {"学习资料", "比赛经验", "技能教程", "工具软件", "其他"};
            resource.setCategoryName(categoryNames[Integer.parseInt(data[2])]);
            resource.setDescription(data[4]);
            resource.setUploaderId(Long.parseLong(data[5]));
            resource.setUploaderName(data[6]);
            User user = userRepository.findById(resource.getUploaderId()).orElse(null);
            if (user != null) {
                resource.setUploaderAvatar(user.getAvatar());
            }
            resource.setClubId(Long.parseLong(data[7]));
            resource.setIsPublic(Integer.parseInt(data[8]));
            resource.setFileUrl(data[9]);
            resource.setFileName(data[10]);
            resource.setFileSize(Long.parseLong(data[11]));
            resource.setFileFormat(data[12]);
            resource.setPreviewUrl(data[13]);
            resource.setTags(data[14]);
            resource.setDownloadCount((int) (Math.random() * 500) + 100);
            resource.setViewCount((int) (Math.random() * 1000) + 200);
            resource.setFavoriteCount((int) (Math.random() * 200) + 50);
            resource.setLikeCount((int) (Math.random() * 100) + 30);
            resource.setRating(4.0 + Math.random());
            resource.setRatingCount((int) (Math.random() * 50) + 10);
            resource.setIsEssence(resource.getCategory() == 0 ? 1 : 0);
            resource.setIsTop(data[0].contains("Java") ? 1 : 0);
            resource.setStatus(0);
            resource.setCreateBy(data[6]);
            resourceShareRepository.save(resource);
        }
        log.info("资源分享数据初始化完成，共{}条", resourceData.length);
    }

    private void initEnterpriseCooperations() {
        if (enterpriseCooperationRepository.count() > 0) return;
        log.info("初始化校企对接数据...");

        String[][] cooperationData = {
                {"0", "实习信息", "字节跳动2025暑期实习招聘", "https://picsum.photos/800/400?coop=1",
                        "字节跳动", "https://api.dicebear.com/7.x/icons/svg?seed=bytedance",
                        "字节跳动是全球化的科技公司，旗下产品包括今日头条、抖音等",
                        "互联网", "10000人以上", "HR王经理", "13800138101", "hr@bytedance.com",
                        "200-300/天", "北京", "", "", "",
                        "岗位要求：1. 熟悉Java或Go语言；2. 了解常用数据结构和算法；3. 有实习经验优先",
                        "岗位职责：1. 参与后端服务开发；2. 参与系统设计和优化；3. 编写技术文档",
                        "招聘后端开发实习生，欢迎2025届毕业生投递",
                        "2", "2025-06-30", "1", "0"},
                {"1", "赞助信息", "科技公司活动赞助", "https://picsum.photos/800/400?coop=2",
                        "科技创新有限公司", "https://api.dicebear.com/7.x/icons/svg?seed=techco",
                        "专注于人工智能和大数据领域的创新型科技公司",
                        "科技", "100-500人", "市场部李总", "13800138102", "marketing@techco.com",
                        "", "", "10000.0", "0", "冠名权、宣传展位",
                        "", "",
                        "赞助高校社团活动，提升品牌知名度，发掘优秀人才",
                        "2", "2025-12-31", "1", "0"},
                {"0", "实习信息", "腾讯云后端开发实习", "https://picsum.photos/800/400?coop=3",
                        "腾讯", "https://api.dicebear.com/7.x/icons/svg?seed=tencent",
                        "腾讯是中国最大的互联网综合服务提供商之一",
                        "互联网", "10000人以上", "HR张小姐", "13800138103", "hr@tencent.com",
                        "250-350/天", "深圳", "", "", "",
                        "岗位要求：1. 熟练掌握Java/C++/Go中至少一种；2. 熟悉MySQL、Redis等存储；3. 有分布式系统经验优先",
                        "岗位职责：1. 参与腾讯云产品后端开发；2. 参与性能优化和架构设计；3. 解决线上问题",
                        "腾讯云团队招聘优秀实习生，表现优异可转正",
                        "3", "2025-05-31", "1", "1"},
                {"3", "招聘信息", "阿里巴巴2025校园招聘", "https://picsum.photos/800/400?coop=4",
                        "阿里巴巴", "https://api.dicebear.com/7.x/icons/svg?seed=alibaba",
                        "阿里巴巴集团是全球领先的电子商务和云计算公司",
                        "互联网", "10000人以上", "HR刘经理", "13800138104", "campus@alibaba.com",
                        "25-35万/年", "杭州", "", "", "",
                        "岗位要求：1. 计算机相关专业；2. 扎实的算法和数据结构基础；3. 良好的沟通能力",
                        "岗位职责：1. 负责电商平台核心系统开发；2. 参与技术架构设计；3. 推动技术创新",
                        "阿里巴巴2025校园招聘正式启动，欢迎优秀毕业生加入",
                        "4", "2025-04-30", "1", "1"},
                {"2", "合作项目", "企业级应用开发合作", "https://picsum.photos/800/400?coop=5",
                        "智慧科技股份有限公司", "https://api.dicebear.com/7.x/icons/svg?seed=smart",
                        "专注于企业数字化转型解决方案的科技公司",
                        "软件服务", "500-1000人", "项目部陈经理", "13800138105", "project@smarttech.com",
                        "", "北京", "", "1", "提供技术指导和项目资源",
                        "", "",
                        "寻找高校社团合作开发企业级应用项目，提供实习机会和项目经费",
                        "2", "2025-09-30", "1", "0"}
        };

        for (String[] data : cooperationData) {
            EnterpriseCooperation cooperation = new EnterpriseCooperation();
            cooperation.setType(Integer.parseInt(data[0]));
            cooperation.setTypeName(data[1]);
            cooperation.setTitle(data[2]);
            cooperation.setCover(data[3]);
            cooperation.setEnterpriseName(data[4]);
            cooperation.setEnterpriseLogo(data[5]);
            cooperation.setEnterpriseIntro(data[6]);
            cooperation.setIndustry(data[7]);
            cooperation.setScale(data[8]);
            cooperation.setContactPerson(data[9]);
            cooperation.setContactPhone(data[10]);
            cooperation.setContactEmail(data[11]);
            cooperation.setSalary(data[12].isEmpty() ? null : data[12]);
            cooperation.setLocation(data[13].isEmpty() ? null : data[13]);
            cooperation.setSponsorAmount(data[14].isEmpty() ? null : Double.parseDouble(data[14]));
            cooperation.setSponsorForm(data[15].isEmpty() ? null : Integer.parseInt(data[15]));
            cooperation.setSponsorRequirement(data[16].isEmpty() ? null : data[16]);
            cooperation.setJobRequirement(data[17].isEmpty() ? null : data[17]);
            cooperation.setJobResponsibility(data[18].isEmpty() ? null : data[18]);
            cooperation.setDescription(data[19]);
            cooperation.setPublisherId(Long.parseLong(data[20]));
            cooperation.setPublisherName("企业管理员");
            cooperation.setDeadline(data[21]);
            cooperation.setViewCount((int) (Math.random() * 500) + 100);
            cooperation.setApplyCount((int) (Math.random() * 50) + 10);
            cooperation.setFavoriteCount((int) (Math.random() * 100) + 20);
            cooperation.setIsRecommend(Integer.parseInt(data[22]));
            cooperation.setIsTop(Integer.parseInt(data[23]));
            cooperation.setStatus(0);
            cooperation.setCreateBy("enterprise");
            enterpriseCooperationRepository.save(cooperation);
        }
        log.info("校企对接数据初始化完成，共{}条", cooperationData.length);
    }

    private void initCooperationApplies() {
        if (cooperationApplyRepository.count() > 0) return;
        log.info("初始化校企对接申请数据...");

        for (long cooperationId = 1; cooperationId <= 3; cooperationId++) {
            for (int i = 0; i < 3; i++) {
                CooperationApply apply = new CooperationApply();
                apply.setCooperationId(cooperationId);
                long userId = (i % 6) + 2;
                apply.setApplicantId(userId);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    apply.setApplicantName(user.getRealName());
                    apply.setApplicantAvatar(user.getAvatar());
                    apply.setPhone(user.getPhone());
                    apply.setEmail(user.getEmail());
                }
                apply.setApplyType(0);
                apply.setApplyReason("我对这个机会非常感兴趣，希望能够获得这次机会。我有相关的学习和实践经验，相信能够胜任。");
                apply.setStatus(i == 0 ? 1 : (i == 1 ? 0 : 2));
                if (apply.getStatus() != 0) {
                    apply.setAuditorId(1L);
                    apply.setAuditorName("企业管理员");
                    apply.setAuditTime(LocalDateTime.now().format(DATE_FORMATTER));
                    apply.setAuditOpinion(apply.getStatus() == 1 ? "简历已通过，请等待面试通知" : "感谢您的申请，我们会保留您的简历");
                }
                apply.setCreateBy(user != null ? user.getUsername() : "system");
                cooperationApplyRepository.save(apply);
            }
        }
        log.info("校企对接申请数据初始化完成");
    }
}
