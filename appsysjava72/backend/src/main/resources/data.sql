-- 初始化社团分类数据
INSERT INTO club_category (id, name, icon, sort) VALUES
(1, '学术科技', '📚', 1),
(2, '文化艺术', '🎨', 2),
(3, '体育运动', '⚽', 3),
(4, '公益志愿', '❤️', 4),
(5, '创新创业', '💡', 5),
(6, '兴趣爱好', '🎮', 6);

-- 初始化测试用户数据
INSERT INTO sys_user (id, username, password, nickname, avatar, phone, student_no, college, major, grade, gender, bio, status) VALUES
(1, '2021001', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=1', '13800138001', '2021001', '计算机学院', '软件工程', '2021级', 1, '热爱编程，喜欢技术创新', 1),
(2, '2021002', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', '13800138002', '2021002', '电子信息学院', '电子工程', '2021级', 1, '喜欢阅读和写作', 1),
(3, '2021003', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '王五', 'https://api.dicebear.com/7.x/avataaars/svg?seed=3', '13800138003', '2021003', '艺术设计学院', '视觉传达', '2021级', 2, '热爱艺术设计', 1);

-- 初始化社团数据
INSERT INTO club (id, name, logo, cover_image, category_id, description, purpose, rules, member_count, max_members, president_id, status) VALUES
(1, 'AI人工智能协会', 'https://api.dicebear.com/7.x/icons/svg?seed=ai', 'https://picsum.photos/800/400?random=1', 1, 'AI人工智能协会致力于推广人工智能技术，开展AI相关的学术交流和项目实践。', '让AI走进校园，让技术改变生活', '1. 热爱人工智能技术；2. 积极参加协会活动；3. 遵守协会章程', 128, 200, 1, 1),
(2, '编程俱乐部', 'https://api.dicebear.com/7.x/icons/svg?seed=code', 'https://picsum.photos/800/400?random=2', 1, '编程俱乐部是一个面向全体编程爱好者的学术性社团，提供编程学习和交流平台。', '提高编程能力，培养创新思维', '1. 按时参加活动；2. 积极完成任务；3. 互帮互助', 96, 150, 2, 1),
(3, '书画协会', 'https://api.dicebear.com/7.x/icons/svg?seed=art', 'https://picsum.photos/800/400?random=3', 2, '书画协会传承中华传统文化，开展书法、绘画等艺术交流活动。', '弘扬国粹，陶冶情操', '1. 热爱书画艺术；2. 尊重艺术创作；3. 积极参与活动', 68, 100, 3, 1),
(4, '篮球协会', 'https://api.dicebear.com/7.x/icons/svg?seed=basketball', 'https://picsum.photos/800/400?random=4', 3, '篮球协会致力于推广篮球运动，组织各类篮球比赛和训练活动。', '强身健体，以球会友', '1. 热爱篮球运动；2. 遵守比赛规则；3. 团结协作', 156, 200, 1, 1),
(5, '志愿者协会', 'https://api.dicebear.com/7.x/icons/svg?seed=volunteer', 'https://picsum.photos/800/400?random=5', 4, '志愿者协会组织各类志愿服务活动，传递爱心，服务社会。', '奉献、友爱、互助、进步', '1. 有奉献精神；2. 遵守志愿者守则；3. 积极参与服务', 200, 300, 2, 1);

-- 初始化社团成员数据
INSERT INTO club_member (club_id, user_id, role) VALUES
(1, 1, 2),
(1, 2, 0),
(1, 3, 0),
(2, 2, 2),
(2, 1, 0),
(3, 3, 2),
(4, 1, 2),
(4, 2, 0),
(5, 2, 2),
(5, 3, 0);

-- 初始化活动数据
INSERT INTO club_activity (id, club_id, title, cover, description, activity_type, location, start_time, end_time, sign_up_start_time, sign_up_end_time, max_participants, current_participants, need_sign_in, status, publisher_id) VALUES
(1, 1, 'AI技术分享会', 'https://picsum.photos/800/400?random=11', '邀请业界专家分享AI最新技术动态和应用案例', '学术讲座', '图书馆报告厅', '2024-06-15 14:00:00', '2024-06-15 17:00:00', '2024-06-01 00:00:00', '2024-06-14 23:59:59', 200, 156, 1, 0, 1),
(2, 2, '编程马拉松', 'https://picsum.photos/800/400?random=12', '48小时不间断编程挑战赛，展示你的编程实力', '竞赛', '创新实验室', '2024-06-20 09:00:00', '2024-06-22 09:00:00', '2024-06-10 00:00:00', '2024-06-18 23:59:59', 50, 48, 1, 0, 2),
(3, 4, '校园篮球联赛', 'https://picsum.photos/800/400?random=13', '各学院代表队之间的篮球友谊赛', '体育比赛', '篮球场', '2024-06-10 16:00:00', '2024-06-10 18:00:00', '2024-06-01 00:00:00', '2024-06-08 23:59:59', 0, 30, 1, 1, 1),
(4, 5, '敬老院志愿服务', 'https://picsum.photos/800/400?random=14', '前往敬老院开展关爱老人志愿服务活动', '志愿服务', '阳光敬老院', '2024-06-18 08:00:00', '2024-06-18 12:00:00', '2024-06-10 00:00:00', '2024-06-16 23:59:59', 30, 25, 1, 0, 2),
(5, 3, '书画作品展', 'https://picsum.photos/800/400?random=15', '会员优秀书画作品展览', '展览', '艺术楼展厅', '2024-06-25 09:00:00', '2024-06-30 18:00:00', '2024-06-20 00:00:00', '2024-06-24 23:59:59', 0, 0, 0, 0, 3);

-- 初始化活动报名数据
INSERT INTO activity_sign_up (activity_id, user_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(4, 2),
(4, 3);

-- 初始化社团动态数据
INSERT INTO club_feed (id, club_id, user_id, content, images, like_count, comment_count) VALUES
(1, 1, 1, '今天我们成功举办了AI技术分享会，感谢大家的参与！期待下次活动~', 'https://picsum.photos/400/300?random=21,https://picsum.photos/400/300?random=22', 32, 8),
(2, 4, 1, '篮球联赛圆满结束，感谢所有参赛队员的精彩表现！', 'https://picsum.photos/400/300?random=23', 56, 12),
(3, 5, 2, '敬老院志愿服务活动圆满完成，老人们都很开心，我们也收获了很多感动。', 'https://picsum.photos/400/300?random=24,https://picsum.photos/400/300?random=25,https://picsum.photos/400/300?random=26', 78, 15),
(4, 2, 2, '编程马拉松即将开始，各位选手准备好了吗？', '', 23, 5);

-- 初始化消息通知设置
INSERT INTO notification_setting (user_id) VALUES
(1),
(2),
(3);

-- 初始化消息数据
INSERT INTO sys_message (user_id, type, title, content, biz_id) VALUES
(1, 'activity', '活动提醒', '您报名的"AI技术分享会"将于明天下午2点开始，请准时参加！', 1),
(1, 'club', '社团通知', '您已成功加入AI人工智能协会，欢迎成为我们的一员！', 1),
(2, 'activity', '活动提醒', '您报名的"编程马拉松"还有3天开始，请做好准备。', 2),
(2, 'system', '系统通知', '您的账号安全验证已完成，感谢您的配合。', NULL),
(3, 'club', '社团通知', '书画协会将举办作品展，请积极参与投稿。', 3);
