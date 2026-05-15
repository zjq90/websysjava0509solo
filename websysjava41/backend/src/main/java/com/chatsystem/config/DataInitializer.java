package com.chatsystem.config;

import com.chatsystem.entity.Friend;
import com.chatsystem.entity.User;
import com.chatsystem.repository.FriendRepository;
import com.chatsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化类 - 系统启动时自动创建测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (userRepository.count() > 0) {
            System.out.println("已有测试数据，跳过初始化");
            return;
        }

        System.out.println("开始初始化测试数据...");

        // 创建测试用户1
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("123456");
        user1.setNickname("张三");
        user1.setStatus(0);
        user1.setCreateTime(LocalDateTime.now());
        user1.setUpdateTime(LocalDateTime.now());
        userRepository.save(user1);

        // 创建测试用户2
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("123456");
        user2.setNickname("李四");
        user2.setStatus(0);
        user2.setCreateTime(LocalDateTime.now());
        user2.setUpdateTime(LocalDateTime.now());
        userRepository.save(user2);

        // 创建测试用户3
        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("123456");
        user3.setNickname("王五");
        user3.setStatus(0);
        user3.setCreateTime(LocalDateTime.now());
        user3.setUpdateTime(LocalDateTime.now());
        userRepository.save(user3);

        // 创建测试用户4
        User user4 = new User();
        user4.setUsername("user4");
        user4.setPassword("123456");
        user4.setNickname("赵六");
        user4.setStatus(0);
        user4.setCreateTime(LocalDateTime.now());
        user4.setUpdateTime(LocalDateTime.now());
        userRepository.save(user4);

        // 创建好友关系 - user1和user2互加好友
        Friend friend1 = new Friend();
        friend1.setUserId(user1.getId());
        friend1.setFriendId(user2.getId());
        friend1.setStatus(1);
        friend1.setCreateTime(LocalDateTime.now());
        friendRepository.save(friend1);

        Friend friend2 = new Friend();
        friend2.setUserId(user2.getId());
        friend2.setFriendId(user1.getId());
        friend2.setStatus(1);
        friend2.setCreateTime(LocalDateTime.now());
        friendRepository.save(friend2);

        // 创建好友关系 - user1和user3互加好友
        Friend friend3 = new Friend();
        friend3.setUserId(user1.getId());
        friend3.setFriendId(user3.getId());
        friend3.setStatus(1);
        friend3.setCreateTime(LocalDateTime.now());
        friendRepository.save(friend3);

        Friend friend4 = new Friend();
        friend4.setUserId(user3.getId());
        friend4.setFriendId(user1.getId());
        friend4.setStatus(1);
        friend4.setCreateTime(LocalDateTime.now());
        friendRepository.save(friend4);

        // 创建好友请求 - user4向user1发送好友请求
        Friend friend5 = new Friend();
        friend5.setUserId(user4.getId());
        friend5.setFriendId(user1.getId());
        friend5.setStatus(0);
        friend5.setCreateTime(LocalDateTime.now());
        friendRepository.save(friend5);

        System.out.println("测试数据初始化完成！");
        System.out.println("========================================");
        System.out.println("测试账号列表：");
        System.out.println("  账号: user1, 密码: 123456, 昵称: 张三");
        System.out.println("  账号: user2, 密码: 123456, 昵称: 李四");
        System.out.println("  账号: user3, 密码: 123456, 昵称: 王五");
        System.out.println("  账号: user4, 密码: 123456, 昵称: 赵六");
        System.out.println("========================================");
        System.out.println("好友关系：");
        System.out.println("  张三 <-> 李四 (已成为好友)");
        System.out.println("  张三 <-> 王五 (已成为好友)");
        System.out.println("  赵六 -> 张三 (好友请求待确认)");
        System.out.println("========================================");
        System.out.println("API文档地址: http://localhost:8080/swagger-ui.html");
        System.out.println("H2控制台地址: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
