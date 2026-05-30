MERGE INTO sys_user (id, username, nickname, role) VALUES (1, 'admin', '系统管理员', 'super_admin');

MERGE INTO game_category (id, name, sort, game_count) VALUES (1, '动作', 1, 15);
MERGE INTO game_category (id, name, sort, game_count) VALUES (2, '益智', 2, 12);
MERGE INTO game_category (id, name, sort, game_count) VALUES (3, '休闲', 3, 20);
MERGE INTO game_category (id, name, sort, game_count) VALUES (4, '竞技', 4, 8);
MERGE INTO game_category (id, name, sort, game_count) VALUES (5, '双人', 5, 6);

MERGE INTO game_tag (id, name, alias, game_count) VALUES (1, 'IO游戏', '.io游戏', 10);
MERGE INTO game_tag (id, name, game_count) VALUES (2, '多人', 15);
MERGE INTO game_tag (id, name, game_count) VALUES (3, '单人', 25);
MERGE INTO game_tag (id, name, game_count) VALUES (4, '闯关', 18);
MERGE INTO game_tag (id, name, game_count) VALUES (5, '策略', 12);
MERGE INTO game_tag (id, name, game_count) VALUES (6, '益智', 20);
MERGE INTO game_tag (id, name, game_count) VALUES (7, '休闲', 22);
MERGE INTO game_tag (id, name, game_count) VALUES (8, '竞技', 8);
MERGE INTO game_tag (id, name, game_count) VALUES (9, '低使用率', 2);
MERGE INTO game_tag (id, name, game_count) VALUES (10, '待合并1', 3);

MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (1, 'G001', '超级马里奥冒险', 'https://picsum.photos/200/150?random=1', '平台跳跃', 1, '任天堂工作室', '经典的马里奥冒险游戏，收集金币，拯救公主！', 'https://example.com/game/mario', '单人,闯关,休闲', 1, 9850, 156800, 1, '2024-01-15 10:30:00', '2024-01-16 14:20:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (2, 'G002', '俄罗斯方块经典', 'https://picsum.photos/200/150?random=2', '消除', 2, '经典游戏公司', '经典的俄罗斯方块游戏，考验你的反应速度！', 'https://example.com/game/tetris', '单人,益智,休闲', 1, 8760, 234500, 1, '2024-01-10 09:00:00', '2024-01-11 11:30:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (3, 'G003', '贪吃蛇大作战', 'https://picsum.photos/200/150?random=3', 'IO', 1, '小蛇工作室', '多人在线贪吃蛇游戏，吃掉对手成为最长的蛇！', 'https://example.com/game/snake', 'IO游戏,多人,竞技', 1, 12300, 456700, 1, '2024-01-20 14:00:00', '2024-01-21 09:15:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (4, 'G004', '2048数字合成', 'https://picsum.photos/200/150?random=4', '数字合成', 2, '数字游戏公司', '合并相同数字，挑战2048！', 'https://example.com/game/2048', '单人,益智,休闲', 1, 7650, 189200, 0, '2024-01-05 16:30:00', '2024-01-06 10:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (5, 'G005', '愤怒的小鸟', 'https://picsum.photos/200/150?random=5', '物理', 3, 'Rovio娱乐', '用弹弓发射小鸟，消灭绿色小猪！', 'https://example.com/game/angrybirds', '单人,闯关,休闲', 1, 9120, 312400, 0, '2024-01-08 11:00:00', '2024-01-09 15:45:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time) VALUES (6, 'G006', '王者竞技场', 'https://picsum.photos/200/150?random=6', 'MOBA', 4, '天美工作室', '5v5公平竞技手游，凭实力carry全场！', 'https://example.com/game/wzry', '多人,竞技,策略', 0, 0, 0, 0, '2024-01-25 08:30:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time) VALUES (7, 'G007', '消消乐乐园', 'https://picsum.photos/200/150?random=7', '三消', 2, '乐元素', '轻松愉快的三消游戏，海量关卡等你挑战！', 'https://example.com/game/xxl', '单人,益智,休闲', 0, 0, 0, 0, '2024-01-26 10:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (8, 'G008', '五子棋对战', 'https://picsum.photos/200/150?random=8', '棋类', 5, '棋类游戏工作室', '双人对战五子棋，先连成五子者获胜！', 'https://example.com/game/gomoku', '双人,益智,策略', 1, 5430, 87600, 0, '2024-01-12 09:30:00', '2024-01-13 14:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time, offline_time) VALUES (9, 'G009', '植物大战僵尸', 'https://picsum.photos/200/150?random=9', '塔防', 3, '宝开游戏', '经典塔防游戏，种植植物抵御僵尸入侵！', 'https://example.com/game/pvz', '单人,策略,闯关', 2, 6780, 123400, 0, '2024-01-01 10:00:00', '2024-01-02 12:00:00', '2024-01-20 18:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time) VALUES (10, 'G010', '斗地主', 'https://picsum.photos/200/150?random=10', '卡牌', 5, '腾讯游戏', '经典斗地主，三人对战，欢乐无限！', 'https://example.com/game/landlord', '多人,休闲,卡牌', 3, 0, 0, 0, '2024-01-24 15:30:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (11, 'G011', '神庙逃亡', 'https://picsum.photos/200/150?random=11', '跑酷', 1, 'Imangi Studios', '在古老的神庙中奔跑，躲避障碍和怪物！', 'https://example.com/game/templerun', '单人,闯关,休闲', 1, 8450, 278900, 0, '2024-01-14 11:30:00', '2024-01-15 09:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (12, 'G012', '开心消消乐', 'https://picsum.photos/200/150?random=12', '三消', 2, '乐元素', '超好玩的三消游戏，和好友一起玩！', 'https://example.com/game/happy', '单人,益智,休闲', 1, 11200, 389000, 1, '2024-01-18 14:00:00', '2024-01-19 10:30:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time) VALUES (13, 'G013', '我的世界', 'https://picsum.photos/200/150?random=13', '沙盒', 1, 'Mojang', '自由建造你的世界，无限可能！', 'https://example.com/game/minecraft', '单人,多人,创造', 0, 0, 0, 0, '2024-01-27 09:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (14, 'G014', '象棋大师', 'https://picsum.photos/200/150?random=14', '棋类', 5, '棋类游戏工作室', '中国象棋大师，人机对战，提升棋力！', 'https://example.com/game/chess', '单人,双人,策略', 1, 4560, 67800, 0, '2024-01-16 10:00:00', '2024-01-17 15:00:00');
MERGE INTO game_info (id, game_id, name, cover, type, category_id, developer, description, play_url, tags, status, hot_value, play_count, recommend, submit_time, online_time) VALUES (15, 'G015', '弹弹堂', 'https://picsum.photos/200/150?random=15', '射击', 4, '第七大道', '多人在线弹射对战，角度力度精准控制！', 'https://example.com/game/ddt', '多人,竞技,策略', 1, 7890, 156700, 0, '2024-01-19 13:00:00', '2024-01-20 11:00:00');

MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (1, CURRENT_DATE - 6, 1100, 150, 7500, 4800.00, 950);
MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (2, CURRENT_DATE - 5, 1250, 180, 8200, 5200.00, 1050);
MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (3, CURRENT_DATE - 4, 1180, 160, 7800, 4900.00, 980);
MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (4, CURRENT_DATE - 3, 1320, 200, 9100, 5800.00, 1100);
MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (5, CURRENT_DATE - 2, 1400, 210, 9500, 6100.00, 1180);
MERGE INTO daily_stats (id, stat_date, online_users, new_users, game_launches, ad_revenue, active_users) VALUES (6, CURRENT_DATE - 1, 1350, 195, 8900, 5600.00, 1120);

MERGE INTO auto_audit_rule (id, rule_name, rule_type, rule_value, action, status) VALUES (1, '开发者第5款及以上自动通过', 'developer_game_count', '5', 1, 1);
MERGE INTO auto_audit_rule (id, rule_name, rule_type, rule_value, action, status) VALUES (2, '含敏感词自动拒绝', 'sensitive_words', '赌博,色情,暴力', 0, 1);

MERGE INTO dashboard_config (id, name, description, layout_config, is_default) VALUES (1, '日常监控', '日常运营监控看板', '[{"id":"stats","x":0,"y":0,"w":12,"h":4},{"id":"trend7","x":0,"y":4,"w":6,"h":8},{"id":"trend30","x":6,"y":4,"w":6,"h":8}]', 1);
MERGE INTO dashboard_config (id, name, description, layout_config, is_default) VALUES (2, '活动期间', '活动期间重点关注数据', '[{"id":"stats","x":0,"y":0,"w":12,"h":4},{"id":"trend7","x":0,"y":4,"w":12,"h":8}]', 0);

MERGE INTO game_recommendation (id, game_id, game_name, type, sort_weight, start_time, end_time, display_frequency) VALUES (1, 1, '超级马里奥冒险', 'home', 100, CURRENT_DATE - 1, CURRENT_DATE + 30, 1);
MERGE INTO game_recommendation (id, game_id, game_name, type, sort_weight, start_time, end_time, display_frequency) VALUES (2, 3, '贪吃蛇大作战', 'home', 90, CURRENT_DATE - 1, CURRENT_DATE + 30, 1);
MERGE INTO game_recommendation (id, game_id, game_name, type, sort_weight, start_time, end_time, display_frequency) VALUES (3, 12, '开心消消乐', 'home', 80, CURRENT_DATE - 1, CURRENT_DATE + 30, 1);
MERGE INTO game_recommendation (id, game_id, game_name, type, category_id, sort_weight, start_time, end_time) VALUES (4, 2, '俄罗斯方块经典', 'category', 2, 100, CURRENT_DATE - 1, CURRENT_DATE + 30);
MERGE INTO game_recommendation (id, game_id, game_name, type, category_id, sort_weight, start_time, end_time) VALUES (5, 4, '2048数字合成', 'category', 2, 90, CURRENT_DATE - 1, CURRENT_DATE + 30);
MERGE INTO game_recommendation (id, game_id, game_name, type, category_id, sort_weight, start_time, end_time) VALUES (6, 1, '超级马里奥冒险', 'category', 1, 100, CURRENT_DATE - 1, CURRENT_DATE + 30);
MERGE INTO game_recommendation (id, game_id, game_name, type, sort_weight, start_time, end_time, display_frequency) VALUES (7, 3, '贪吃蛇大作战', 'popup', 100, CURRENT_DATE - 1, CURRENT_DATE + 7, 1);

MERGE INTO audit_log (id, game_id, game_name, auditor_id, auditor_name, audit_time, audit_result, remark) VALUES (1, 1, '超级马里奥冒险', 1, '系统管理员', '2024-01-16 14:20:00', 1, '游戏内容健康，玩法有趣，通过审核');
MERGE INTO audit_log (id, game_id, game_name, auditor_id, auditor_name, audit_time, audit_result, remark) VALUES (2, 2, '俄罗斯方块经典', 1, '系统管理员', '2024-01-11 11:30:00', 1, '经典游戏，通过审核');
MERGE INTO audit_log (id, game_id, game_name, auditor_id, auditor_name, audit_time, audit_result, reject_reason, remark) VALUES (3, 10, '斗地主', 1, '系统管理员', '2024-01-25 09:00:00', 0, '涉及赌博内容', '游戏内容涉嫌赌博，拒绝审核');
