INSERT INTO `user` (`user_id`, `nickname`, `phone`, `avatar`, `email`, `member_status`, `user_status`, `total_game_time`, `register_time`, `last_login_time`) VALUES
('U001', '游戏达人小明', '13812345678', 'https://example.com/avatar1.jpg', 'xiaoming@example.com', 1, 0, 1250, '2024-01-15 10:30:00', '2024-05-28 14:20:00'),
('U002', '快乐玩家小红', '13987654321', 'https://example.com/avatar2.jpg', 'xiaohong@example.com', 0, 0, 890, '2024-02-20 09:15:00', '2024-05-27 18:45:00'),
('U003', '超级玩家老王', '13654789321', 'https://example.com/avatar3.jpg', 'laowang@example.com', 1, 1, 2100, '2023-11-10 16:00:00', '2024-05-15 11:30:00'),
('U004', '新手小白', '13785296412', 'https://example.com/avatar4.jpg', 'xiaobai@example.com', 0, 0, 150, '2024-04-01 20:00:00', '2024-05-28 16:00:00'),
('U005', '电竞高手张三', '13525896347', 'https://example.com/avatar5.jpg', 'zhangsan@example.com', 1, 0, 3200, '2023-09-05 12:00:00', '2024-05-28 22:15:00'),
('U006', '休闲玩家李四', '13412587963', 'https://example.com/avatar6.jpg', 'lisi@example.com', 0, 0, 450, '2024-03-10 14:30:00', '2024-05-20 10:00:00'),
('U007', '游戏博主阿花', '13198745623', 'https://example.com/avatar7.jpg', 'ahua@example.com', 1, 0, 1800, '2023-12-25 08:00:00', '2024-05-28 12:30:00'),
('U008', '测试用户001', '13254789654', 'https://example.com/avatar8.jpg', 'test001@example.com', 0, 0, 200, '2024-05-01 10:00:00', '2024-05-25 09:30:00'),
('U009', '硬核玩家大牛', '13036985274', 'https://example.com/avatar9.jpg', 'daniu@example.com', 1, 0, 2800, '2023-10-18 15:00:00', '2024-05-27 20:00:00'),
('U010', '偶尔玩玩', '13874125896', 'https://example.com/avatar10.jpg', 'sometimes@example.com', 0, 0, 80, '2024-04-20 11:00:00', '2024-05-10 14:00:00');

INSERT INTO `ban_record` (`user_id`, `ban_type`, `ban_reason`, `unban_time`, `operator_id`, `operator_name`, `status`, `create_time`) VALUES
('U003', 1, '多次发布违规言论，情节严重', NULL, 'ADMIN001', '系统管理员', 1, '2024-05-16 09:00:00'),
('U001', 0, '发布不当言论，警告处理', '2024-06-01 00:00:00', 'ADMIN002', '审核员小王', 0, '2024-05-01 10:00:00');

INSERT INTO `game` (`game_id`, `game_name`, `cover`, `description`) VALUES
('G001', '王者荣耀', 'https://example.com/g1.jpg', '腾讯出品的MOBA类手游'),
('G002', '原神', 'https://example.com/g2.jpg', '开放世界冒险游戏'),
('G003', '和平精英', 'https://example.com/g3.jpg', '战术竞技类手游'),
('G004', '英雄联盟', 'https://example.com/g4.jpg', '经典MOBA端游'),
('G005', '崩坏：星穹铁道', 'https://example.com/g5.jpg', '回合制RPG游戏');

INSERT INTO `comment` (`comment_id`, `game_id`, `user_id`, `content`, `audit_status`, `has_sensitive_word`, `create_time`) VALUES
('C001', 'G001', 'U001', '这个游戏真的很好玩，推荐大家都来玩！', 1, 0, '2024-05-20 10:30:00'),
('C002', 'G002', 'U002', '画面精美，剧情也很棒，五星好评！', 1, 0, '2024-05-21 14:00:00'),
('C003', 'G001', 'U003', '垃圾游戏，bug太多了，骗子游戏，赶紧倒闭', 0, 1, '2024-05-25 16:30:00'),
('C004', 'G003', 'U004', '新手友好，操作简单，很适合休闲玩家', 1, 0, '2024-05-22 09:15:00'),
('C005', 'G004', 'U005', '经典永不过时，玩了好多年了', 1, 0, '2024-05-23 20:00:00'),
('C006', 'G005', 'U006', '音乐超级好听，角色设计也很棒', 0, 0, '2024-05-28 11:00:00'),
('C007', 'G001', 'U007', '最新的版本更新内容很丰富，点赞！', 1, 0, '2024-05-26 15:30:00'),
('C008', 'G002', 'U008', '希望能多出一些活动和福利', 0, 0, '2024-05-27 18:00:00');

INSERT INTO `report` (`report_id`, `report_type`, `target_id`, `reason_type`, `reason_detail`, `reporter_id`, `status`, `create_time`) VALUES
('R001', 'comment', 'C003', 'other', '评论内容包含辱骂性语言', 'U001', 0, '2024-05-25 17:00:00'),
('R002', 'user', 'U003', 'violence', '用户发布暴力内容', 'U002', 0, '2024-05-26 08:30:00'),
('R003', 'comment', 'C008', 'advertise', '评论疑似广告推广', 'U003', 1, '2024-05-27 19:00:00'),
('R004', 'game', 'G001', 'porn', '游戏内有违规内容', 'U004', 2, '2024-05-20 12:00:00'),
('R005', 'user', 'U006', 'other', '疑似小号刷评论', 'U005', 0, '2024-05-28 10:00:00');

INSERT INTO `sensitive_word` (`word`, `word_type`, `category`, `create_time`) VALUES
('垃圾游戏', 0, '辱骂', '2024-01-01 00:00:00'),
('骗子', 0, '诈骗', '2024-01-01 00:00:00'),
('倒闭', 0, '恶意评论', '2024-01-01 00:00:00'),
('1[3-9]\\d{9}', 1, '手机号', '2024-01-01 00:00:00'),
('\\d{17}[\\dXx]', 1, '身份证号', '2024-01-01 00:00:00');

INSERT INTO `game_record` (`user_id`, `game_id`, `game_name`, `start_time`, `end_time`, `duration`) VALUES
('U001', 'G001', '王者荣耀', '2024-05-28 13:00:00', '2024-05-28 14:20:00', 80),
('U001', 'G002', '原神', '2024-05-27 19:00:00', '2024-05-27 20:30:00', 90),
('U002', 'G003', '和平精英', '2024-05-27 17:00:00', '2024-05-27 18:45:00', 105),
('U005', 'G004', '英雄联盟', '2024-05-28 20:00:00', '2024-05-28 22:15:00', 135),
('U007', 'G005', '崩坏：星穹铁道', '2024-05-28 11:00:00', '2024-05-28 12:30:00', 90);

INSERT INTO `favorite` (`user_id`, `game_id`, `game_name`, `game_cover`, `create_time`) VALUES
('U001', 'G001', '王者荣耀', 'https://example.com/g1.jpg', '2024-02-01 10:00:00'),
('U001', 'G002', '原神', 'https://example.com/g2.jpg', '2024-03-15 14:00:00'),
('U002', 'G003', '和平精英', 'https://example.com/g3.jpg', '2024-03-01 09:00:00'),
('U005', 'G004', '英雄联盟', 'https://example.com/g4.jpg', '2023-10-01 12:00:00');

INSERT INTO `user_behavior` (`user_id`, `behavior_type`, `behavior_detail`, `ip_address`, `device_info`, `create_time`) VALUES
('U001', 'REGISTER', '用户注册', '192.168.1.100', 'iPhone 15,iOS 17.0', '2024-01-15 10:30:00'),
('U001', 'BROWSE_HOME', '浏览首页', '192.168.1.100', 'iPhone 15,iOS 17.0', '2024-01-15 10:31:00'),
('U001', 'SEARCH_GAME', '搜索:MOBA游戏', '192.168.1.100', 'iPhone 15,iOS 17.0', '2024-01-15 10:32:00'),
('U001', 'START_GAME', '开始游戏:王者荣耀', '192.168.1.100', 'iPhone 15,iOS 17.0', '2024-01-15 10:35:00'),
('U002', 'REGISTER', '用户注册', '192.168.1.101', '小米14,Android 14', '2024-02-20 09:15:00');

INSERT INTO `login_device` (`user_id`, `device_type`, `device_model`, `os_version`, `ip_address`, `login_time`) VALUES
('U001', 'Mobile', 'iPhone 15', 'iOS 17.0', '192.168.1.100', '2024-05-28 14:20:00'),
('U001', 'PC', 'MacBook Pro', 'macOS 14.0', '192.168.1.105', '2024-05-25 10:00:00'),
('U002', 'Mobile', '小米14', 'Android 14', '192.168.1.101', '2024-05-27 18:45:00'),
('U003', 'Mobile', '华为Mate 60', 'HarmonyOS 4.0', '192.168.1.102', '2024-05-15 11:30:00'),
('U005', 'PC', 'Dell XPS 15', 'Windows 11', '192.168.1.103', '2024-05-28 22:15:00');
