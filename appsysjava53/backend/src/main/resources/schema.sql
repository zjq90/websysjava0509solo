-- ============================================
-- 文物收藏APP数据库初始化脚本
-- ============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(RSA加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    phone VARCHAR(20) UNIQUE COMMENT '手机号(RSA加密)',
    email VARCHAR(100) UNIQUE COMMENT '邮箱(RSA加密)',
    user_type TINYINT DEFAULT 1 COMMENT '用户类型: 1-普通用户 2-专家 3-管理员',
    is_verified TINYINT DEFAULT 0 COMMENT '是否实名认证: 0-否 1-是',
    is_elder_mode TINYINT DEFAULT 0 COMMENT '长辈模式: 0-关闭 1-开启',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    frozen_balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '冻结金额',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 2. 用户实名认证表
CREATE TABLE IF NOT EXISTS user_verification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '认证ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名(RSA加密)',
    id_card VARCHAR(50) NOT NULL UNIQUE COMMENT '身份证号(RSA加密)',
    face_image VARCHAR(500) COMMENT '人脸识别照片',
    id_card_front VARCHAR(500) COMMENT '身份证正面照',
    id_card_back VARCHAR(500) COMMENT '身份证背面照',
    verify_status TINYINT DEFAULT 0 COMMENT '审核状态: 0-待审核 1-通过 2-拒绝',
    verify_remark VARCHAR(500) COMMENT '审核备注',
    verify_time DATETIME COMMENT '审核时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间'
);

-- 3. 文物表
CREATE TABLE IF NOT EXISTS heritage_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文物ID',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    name VARCHAR(200) NOT NULL COMMENT '文物名称',
    category VARCHAR(50) COMMENT '文物类别: 青铜器、瓷器、书画等',
    era VARCHAR(100) COMMENT '年代',
    description TEXT COMMENT '文物描述',
    images TEXT COMMENT '图片URL列表，逗号分隔',
    videos VARCHAR(500) COMMENT '视频URL',
    authenticity_cert VARCHAR(500) COMMENT '鉴定证书',
    is_on_sale TINYINT DEFAULT 0 COMMENT '是否在售: 0-否 1-是',
    price DECIMAL(12,2) COMMENT '出售价格',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-下架 1-正常 2-审核中',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 4. 社区帖子表
CREATE TABLE IF NOT EXISTS community_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '发布用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    images TEXT COMMENT '图片URL列表，逗号分隔',
    video VARCHAR(500) COMMENT '视频URL',
    post_type TINYINT DEFAULT 1 COMMENT '帖子类型: 1-图文 2-视频',
    ai_review_status TINYINT DEFAULT 0 COMMENT 'AI审核状态: 0-待审核 1-通过 2-拒绝',
    ai_review_result TEXT COMMENT 'AI审核结果',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    view_count INT DEFAULT 0 COMMENT '浏览数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶: 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除 1-正常',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 5. 帖子评论表
CREATE TABLE IF NOT EXISTS post_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论用户ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
    content TEXT NOT NULL COMMENT '评论内容',
    ai_review_status TINYINT DEFAULT 0 COMMENT 'AI审核状态: 0-待审核 1-通过 2-拒绝',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-删除 1-正常',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间'
);

-- 6. 专家问答表
CREATE TABLE IF NOT EXISTS expert_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '问题ID',
    user_id BIGINT NOT NULL COMMENT '提问用户ID',
    expert_id BIGINT NOT NULL COMMENT '回答专家ID',
    title VARCHAR(200) NOT NULL COMMENT '问题标题',
    content TEXT NOT NULL COMMENT '问题内容',
    images TEXT COMMENT '图片URL列表',
    is_anonymous TINYINT DEFAULT 0 COMMENT '是否匿名: 0-否 1-是',
    price DECIMAL(10,2) NOT NULL COMMENT '咨询费用',
    is_paid TINYINT DEFAULT 0 COMMENT '是否已支付: 0-否 1-是',
    answer_status TINYINT DEFAULT 0 COMMENT '回答状态: 0-待回答 1-已回答 2-已结束',
    answer_content TEXT COMMENT '专家回答内容',
    answer_time DATETIME COMMENT '回答时间',
    rating INT COMMENT '评分: 1-5星',
    rating_comment VARCHAR(500) COMMENT '评价内容',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提问时间'
);

-- 7. 线下活动表
CREATE TABLE IF NOT EXISTS offline_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    activity_type VARCHAR(50) NOT NULL COMMENT '活动类型: 鉴宝会、展览、讲座等',
    description TEXT COMMENT '活动描述',
    cover_image VARCHAR(500) COMMENT '封面图片',
    images TEXT COMMENT '活动图片列表',
    address VARCHAR(500) NOT NULL COMMENT '活动地址',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    sign_up_start DATETIME COMMENT '报名开始时间',
    sign_up_end DATETIME COMMENT '报名结束时间',
    max_participants INT COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前报名人数',
    organizer VARCHAR(200) COMMENT '主办方',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-取消 1-报名中 2-进行中 3-已结束',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 8. 活动报名表
CREATE TABLE IF NOT EXISTS activity_registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '报名ID',
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '报名姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    participant_count INT DEFAULT 1 COMMENT '参与人数',
    sign_up_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-取消 1-已报名 2-已签到',
    check_in_time DATETIME COMMENT '签到时间'
);

-- 9. 交易订单表
CREATE TABLE IF NOT EXISTS trade_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    buyer_id BIGINT NOT NULL COMMENT '买家ID',
    heritage_id BIGINT NOT NULL COMMENT '文物ID',
    heritage_name VARCHAR(200) COMMENT '文物名称快照',
    price DECIMAL(12,2) NOT NULL COMMENT '成交价格',
    service_fee DECIMAL(10,2) COMMENT '服务费',
    total_amount DECIMAL(12,2) COMMENT '订单总金额',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态: 0-待支付 1-已支付 2-已退款',
    order_status TINYINT DEFAULT 1 COMMENT '订单状态: 1-待发货 2-已发货 3-已收货 4-已完成 5-已取消 6-冻结',
    freeze_reason VARCHAR(500) COMMENT '冻结原因',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    tracking_number VARCHAR(100) COMMENT '物流单号',
    is_supervised TINYINT DEFAULT 1 COMMENT '是否监管: 0-否 1-是',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 10. 文物流转登记表
CREATE TABLE IF NOT EXISTS transfer_registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '登记ID',
    order_id BIGINT NOT NULL COMMENT '关联订单ID',
    heritage_id BIGINT NOT NULL COMMENT '文物ID',
    transferor_id BIGINT NOT NULL COMMENT '出让方ID',
    transferee_id BIGINT NOT NULL COMMENT '受让方ID',
    department_permit VARCHAR(500) COMMENT '文物部门许可文件',
    permit_no VARCHAR(100) COMMENT '许可编号',
    permit_issue_date DATE COMMENT '许可签发日期',
    permit_expire_date DATE COMMENT '许可有效期',
    registration_status TINYINT DEFAULT 0 COMMENT '登记状态: 0-待审核 1-通过 2-拒绝',
    review_remark VARCHAR(500) COMMENT '审核备注',
    review_time DATETIME COMMENT '审核时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间'
);

-- 11. 拍卖表
CREATE TABLE IF NOT EXISTS auction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '拍卖ID',
    heritage_id BIGINT NOT NULL COMMENT '文物ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    title VARCHAR(200) NOT NULL COMMENT '拍卖标题',
    start_price DECIMAL(12,2) NOT NULL COMMENT '起拍价',
    min_increment DECIMAL(10,2) NOT NULL COMMENT '最小加价幅度',
    current_price DECIMAL(12,2) COMMENT '当前最高价',
    highest_bid_id BIGINT COMMENT '最高出价ID',
    start_time DATETIME NOT NULL COMMENT '拍卖开始时间',
    end_time DATETIME NOT NULL COMMENT '拍卖结束时间',
    bid_count INT DEFAULT 0 COMMENT '出价次数',
    viewer_count INT DEFAULT 0 COMMENT '围观人数',
    auction_status TINYINT DEFAULT 1 COMMENT '状态: 1-即将开始 2-进行中 3-已结束 4-已成交 5-流拍',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 12. 拍卖出价表
CREATE TABLE IF NOT EXISTS auction_bid (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '出价ID',
    auction_id BIGINT NOT NULL COMMENT '拍卖ID',
    bidder_id BIGINT NOT NULL COMMENT '出价人ID',
    bid_price DECIMAL(12,2) NOT NULL COMMENT '出价金额',
    is_valid TINYINT DEFAULT 1 COMMENT '是否有效: 0-无效 1-有效',
    bid_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '出价时间'
);

-- 13. 捐赠表
CREATE TABLE IF NOT EXISTS donation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '捐赠ID',
    user_id BIGINT NOT NULL COMMENT '捐赠人ID',
    heritage_id BIGINT NOT NULL COMMENT '捐赠文物ID',
    museum_id BIGINT NOT NULL COMMENT '博物馆ID',
    museum_name VARCHAR(200) COMMENT '博物馆名称',
    description TEXT COMMENT '捐赠说明',
    images TEXT COMMENT '捐赠凭证图片',
    donation_status TINYINT DEFAULT 0 COMMENT '状态: 0-待审核 1-已接受 2-已拒绝',
    review_remark VARCHAR(500) COMMENT '审核备注',
    review_time DATETIME COMMENT '审核时间',
    certificate_no VARCHAR(100) COMMENT '捐赠证书编号',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间'
);

-- 14. 博物馆表
CREATE TABLE IF NOT EXISTS museum (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '博物馆ID',
    name VARCHAR(200) NOT NULL COMMENT '博物馆名称',
    address VARCHAR(500) COMMENT '地址',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    description TEXT COMMENT '博物馆介绍',
    logo VARCHAR(500) COMMENT 'logo',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-启用'
);

-- 15. AI审核记录表
CREATE TABLE IF NOT EXISTS ai_review_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    business_type VARCHAR(50) NOT NULL COMMENT '业务类型: post/comment/trading',
    business_id BIGINT NOT NULL COMMENT '业务ID',
    content TEXT COMMENT '审核内容',
    review_result TINYINT NOT NULL COMMENT '审核结果: 1-通过 2-拒绝',
    risk_level VARCHAR(20) COMMENT '风险等级: low/medium/high',
    risk_keywords TEXT COMMENT '风险关键词',
    review_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间'
);

-- ============================================
-- 创建索引
-- ============================================
CREATE INDEX idx_user_phone ON sys_user(phone);
CREATE INDEX idx_post_user ON community_post(user_id);
CREATE INDEX idx_post_status ON community_post(status, ai_review_status);
CREATE INDEX idx_comment_post ON post_comment(post_id);
CREATE INDEX idx_question_user ON expert_question(user_id);
CREATE INDEX idx_question_expert ON expert_question(expert_id);
CREATE INDEX idx_activity_status ON offline_activity(status);
CREATE INDEX idx_registration_activity ON activity_registration(activity_id);
CREATE INDEX idx_order_seller ON trade_order(seller_id);
CREATE INDEX idx_order_buyer ON trade_order(buyer_id);
CREATE INDEX idx_auction_status ON auction(auction_status);
CREATE INDEX idx_bid_auction ON auction_bid(auction_id);

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入测试用户
INSERT INTO sys_user (username, password, nickname, phone, user_type, is_verified, balance) VALUES
('admin', 'encrypted_admin_pass', '管理员', '13800138000', 3, 1, 10000.00),
('expert1', 'encrypted_expert1', '张专家', '13800138001', 2, 1, 5000.00),
('expert2', 'encrypted_expert2', '李专家', '13800138002', 2, 1, 6000.00),
('user1', 'encrypted_user1', '收藏爱好者小王', '13800138003', 1, 1, 8000.00),
('user2', 'encrypted_user2', '古董藏家老李', '13800138004', 1, 1, 15000.00);

-- 插入测试博物馆
INSERT INTO museum (name, address, contact_person, contact_phone, description) VALUES
('故宫博物院', '北京市东城区景山前街4号', '王主任', '010-85007421', '中国最大的古代文化艺术博物馆'),
('国家博物馆', '北京市东城区东长安街16号', '李老师', '010-65116400', '代表国家收藏、研究、展示的综合性博物馆'),
('上海博物馆', '上海市黄浦区人民大道201号', '张老师', '021-63723500', '大型中国古代艺术博物馆');

-- 插入测试文物
INSERT INTO heritage_item (user_id, name, category, era, description, images, price, is_on_sale, status) VALUES
(4, '清代青花瓷瓶', '瓷器', '清代康熙年间', '清代康熙年间精品青花瓷瓶，保存完好，釉色温润，花纹精美。', 'http://example.com/imgs/qing1.jpg,http://example.com/imgs/qing2.jpg', 28000.00, 1, 1),
(5, '明代青铜器', '青铜器', '明代宣德年间', '明代宣德年间铸造的青铜香炉，工艺精湛，包浆自然。', 'http://example.com/imgs/bronze1.jpg', 55000.00, 1, 1),
(4, '齐白石虾图', '书画', '近代', '齐白石大师晚年作品，水墨写意，栩栩如生。', 'http://example.com/imgs/painting1.jpg', 120000.00, 0, 1),
(5, '和田玉籽料', '玉器', '清代', '新疆和田籽料，玉质温润，皮色自然。', 'http://example.com/imgs/jade1.jpg', 35000.00, 1, 1);

-- 插入测试社区帖子
INSERT INTO community_post (user_id, title, content, post_type, ai_review_status, like_count, view_count) VALUES
(4, '我的清代青花瓷瓶收藏之路', '分享一下我收藏这件清代青花瓷瓶的经历，从偶然发现到最终入手，过程非常有趣。', 1, 1, 128, 2560),
(5, '如何辨别青铜器真伪？', '作为一个有十几年收藏经验的藏家，今天给大家分享一些青铜器辨别的小技巧。', 1, 1, 256, 5120),
(2, '专家解读：明清瓷器鉴定要点', '作为文物鉴定专家，今天给大家系统讲解一下明清瓷器的鉴定要点。', 1, 1, 512, 10240);

-- 插入测试线下活动
INSERT INTO offline_activity (title, activity_type, description, address, start_time, end_time, sign_up_start, sign_up_end, max_participants, organizer, status) VALUES
('2024春季文物鉴宝大会', '鉴宝会', '本次鉴宝大会邀请了国内知名文物鉴定专家，为广大藏友提供免费鉴定服务。', '北京市朝阳区国际会议中心', '2024-06-15 09:00:00', '2024-06-15 17:00:00', '2024-05-20 00:00:00', '2024-06-10 23:59:59', 500, '中国收藏家协会', 1),
('明清瓷器精品展', '展览', '本次展览汇集了国内外私人收藏的百余件明清瓷器精品。', '上海博物馆展厅', '2024-07-01 09:00:00', '2024-07-31 17:00:00', '2024-06-01 00:00:00', '2024-06-30 23:59:59', 10000, '上海博物馆', 1);

-- 插入测试拍卖
INSERT INTO auction (heritage_id, seller_id, title, start_price, min_increment, current_price, start_time, end_time, auction_status) VALUES
(2, 5, '明代宣德青铜香炉', 50000.00, 1000.00, 58000.00, '2024-05-20 10:00:00', '2024-05-25 22:00:00', 2);

-- 插入测试拍卖出价
INSERT INTO auction_bid (auction_id, bidder_id, bid_price) VALUES
(1, 4, 51000.00),
(1, 5, 55000.00),
(1, 4, 58000.00);
