INSERT INTO games (name, description, icon, category, is_active, created_at, updated_at) VALUES
('消消乐', '经典消除类休闲游戏', 'icon1.png', '休闲', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('跳一跳', '趣味跳跃小游戏', 'icon2.png', '休闲', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('贪吃蛇', '经典贪吃蛇游戏', 'icon3.png', '经典', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('俄罗斯方块', '经典方块消除游戏', 'icon4.png', '经典', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2048', '数字合成益智游戏', 'icon5.png', '益智', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('五子棋', '经典双人对战棋类游戏', 'icon6.png', '棋类', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('飞行棋', '经典骰子游戏', 'icon7.png', '棋类', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('扫雷', '经典益智游戏', 'icon8.png', '益智', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('连连看', '经典配对消除游戏', 'icon9.png', '休闲', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('泡泡龙', '经典射击消除游戏', 'icon10.png', '射击', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (username, email, region, source_channel, is_vip, created_at, last_login_at) VALUES
('user1', 'user1@example.com', '北京', 'direct', FALSE, TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user2', 'user2@example.com', '上海', 'search', FALSE, TIMESTAMPADD(DAY, -25, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user3', 'user3@example.com', '广州', 'recommend', TRUE, TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user4', 'user4@example.com', '深圳', 'share', FALSE, TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user5', 'user5@example.com', '杭州', 'direct', TRUE, TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user6', 'user6@example.com', '成都', 'search', FALSE, TIMESTAMPADD(DAY, -8, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user7', 'user7@example.com', '武汉', 'recommend', FALSE, TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user8', 'user8@example.com', '南京', 'direct', TRUE, TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user9', 'user9@example.com', '西安', 'share', FALSE, TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('user10', 'user10@example.com', '重庆', 'search', FALSE, TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);

INSERT INTO user_activities (user_id, activity_type, activity_time, ip_address, user_agent) VALUES
(1, 'login', TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP), '192.168.1.1', 'Chrome'),
(2, 'login', TIMESTAMPADD(HOUR, -4, CURRENT_TIMESTAMP), '192.168.1.2', 'Firefox'),
(3, 'login', TIMESTAMPADD(HOUR, -6, CURRENT_TIMESTAMP), '192.168.1.3', 'Safari'),
(4, 'login', TIMESTAMPADD(HOUR, -8, CURRENT_TIMESTAMP), '192.168.1.4', 'Edge'),
(5, 'login', TIMESTAMPADD(HOUR, -10, CURRENT_TIMESTAMP), '192.168.1.5', 'Chrome'),
(6, 'login', TIMESTAMPADD(HOUR, -12, CURRENT_TIMESTAMP), '192.168.1.6', 'Firefox'),
(7, 'login', TIMESTAMPADD(HOUR, -14, CURRENT_TIMESTAMP), '192.168.1.7', 'Safari'),
(8, 'login', TIMESTAMPADD(HOUR, -16, CURRENT_TIMESTAMP), '192.168.1.8', 'Edge'),
(9, 'login', TIMESTAMPADD(HOUR, -18, CURRENT_TIMESTAMP), '192.168.1.9', 'Chrome'),
(10, 'login', TIMESTAMPADD(HOUR, -20, CURRENT_TIMESTAMP), '192.168.1.10', 'Firefox');

INSERT INTO game_play_sessions (user_id, game_id, start_time, end_time, duration_seconds, source_channel, ad_clicked, purchase_made, purchase_amount) VALUES
(1, 1, TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 3600, 'direct', TRUE, FALSE, NULL),
(2, 2, TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP), 3600, 'search', FALSE, TRUE, 9.99),
(3, 3, TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP), 3600, 'recommend', TRUE, TRUE, 19.99),
(4, 4, TIMESTAMPADD(HOUR, -4, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP), 3600, 'share', FALSE, FALSE, NULL),
(5, 5, TIMESTAMPADD(HOUR, -5, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -4, CURRENT_TIMESTAMP), 3600, 'direct', TRUE, FALSE, NULL),
(1, 2, TIMESTAMPADD(HOUR, -6, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -5, CURRENT_TIMESTAMP), 3600, 'search', FALSE, TRUE, 4.99),
(2, 3, TIMESTAMPADD(HOUR, -7, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -6, CURRENT_TIMESTAMP), 3600, 'recommend', TRUE, FALSE, NULL),
(3, 4, TIMESTAMPADD(HOUR, -8, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -7, CURRENT_TIMESTAMP), 3600, 'share', FALSE, FALSE, NULL),
(4, 5, TIMESTAMPADD(HOUR, -9, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -8, CURRENT_TIMESTAMP), 3600, 'direct', TRUE, TRUE, 14.99),
(5, 1, TIMESTAMPADD(HOUR, -10, CURRENT_TIMESTAMP), TIMESTAMPADD(HOUR, -9, CURRENT_TIMESTAMP), 3600, 'search', FALSE, FALSE, NULL);

INSERT INTO revenues (revenue_type, amount, user_id, game_id, created_at, description) VALUES
('AD', 0.5, 1, 1, TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP), '广告收入'),
('VIP', 29.99, 3, NULL, TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP), '会员订阅'),
('IAP', 9.99, 2, 2, TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP), '内购收入'),
('AD', 0.3, 3, 3, TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP), '广告收入'),
('VIP', 59.99, 5, NULL, TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP), '会员订阅'),
('IAP', 19.99, 3, 3, TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP), '内购收入'),
('AD', 0.7, 5, 5, TIMESTAMPADD(HOUR, -5, CURRENT_TIMESTAMP), '广告收入'),
('IAP', 4.99, 1, 2, TIMESTAMPADD(HOUR, -6, CURRENT_TIMESTAMP), '内购收入'),
('AD', 0.4, 4, 5, TIMESTAMPADD(HOUR, -9, CURRENT_TIMESTAMP), '广告收入'),
('IAP', 14.99, 4, 5, TIMESTAMPADD(HOUR, -9, CURRENT_TIMESTAMP), '内购收入');

INSERT INTO system_configs (config_key, config_value, description, created_at, updated_at) VALUES
('site_config', '{"siteName":"游戏平台","siteLogo":"/logo.png","copyright":"© 2024 游戏平台 版权所有","contactEmail":"support@gameplatform.com","contactPhone":"400-123-4567","wechatLink":"https://weixin.qq.com/","weiboLink":"https://weibo.com/","twitterLink":"https://twitter.com/"}', '站点基础配置', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('seo_config', '{"seoTitle":"游戏平台 - 在线玩小游戏","seoDescription":"免费在线小游戏平台，提供海量精选小游戏","seoKeywords":"小游戏,在线游戏,休闲游戏,益智游戏"}', 'SEO配置', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
