-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    student_no VARCHAR(30) UNIQUE COMMENT '学号',
    college VARCHAR(100) COMMENT '院系',
    major VARCHAR(100) COMMENT '专业',
    grade VARCHAR(20) COMMENT '年级',
    gender TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    bio VARCHAR(500) COMMENT '个人简介',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    openid VARCHAR(100) UNIQUE COMMENT '微信openid',
    qq_openid VARCHAR(100) UNIQUE COMMENT 'QQ openid',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0未删除 1已删除',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 社团分类表
CREATE TABLE IF NOT EXISTS club_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    icon VARCHAR(255) COMMENT '分类图标',
    sort INT DEFAULT 0 COMMENT '排序',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 社团表
CREATE TABLE IF NOT EXISTS club (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '社团名称',
    logo VARCHAR(255) COMMENT '社团logo',
    cover_image VARCHAR(255) COMMENT '封面图',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '社团简介',
    purpose TEXT COMMENT '社团宗旨',
    rules TEXT COMMENT '社团章程',
    member_count INT DEFAULT 0 COMMENT '成员数',
    max_members INT DEFAULT 100 COMMENT '最大成员数',
    president_id BIGINT COMMENT '社长ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0停用 1正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 社团成员表
CREATE TABLE IF NOT EXISTS club_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_id BIGINT NOT NULL COMMENT '社团ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role TINYINT DEFAULT 0 COMMENT '角色 0普通成员 1副社长 2社长',
    join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_club_user (club_id, user_id)
);

-- 入团申请表
CREATE TABLE IF NOT EXISTS club_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_id BIGINT NOT NULL COMMENT '社团ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    reason VARCHAR(500) COMMENT '申请理由',
    resume TEXT COMMENT '个人简介',
    works VARCHAR(500) COMMENT '作品链接',
    status TINYINT DEFAULT 0 COMMENT '审核状态 0待审核 1已通过 2已拒绝',
    review_remark VARCHAR(500) COMMENT '审核意见',
    reviewer_id BIGINT COMMENT '审核人ID',
    review_time TIMESTAMP COMMENT '审核时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 活动表
CREATE TABLE IF NOT EXISTS club_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_id BIGINT NOT NULL COMMENT '社团ID',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    cover VARCHAR(255) COMMENT '活动封面',
    description TEXT COMMENT '活动描述',
    activity_type VARCHAR(50) COMMENT '活动类型',
    location VARCHAR(200) COMMENT '活动地点',
    start_time TIMESTAMP NOT NULL COMMENT '开始时间',
    end_time TIMESTAMP NOT NULL COMMENT '结束时间',
    sign_up_start_time TIMESTAMP COMMENT '报名开始时间',
    sign_up_end_time TIMESTAMP COMMENT '报名结束时间',
    max_participants INT DEFAULT 0 COMMENT '最大参与人数 0不限制',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    need_sign_in TINYINT DEFAULT 1 COMMENT '是否需要签到 0不需要 1需要',
    sign_in_qr_code VARCHAR(255) COMMENT '签到二维码',
    status TINYINT DEFAULT 0 COMMENT '状态 0未开始 1进行中 2已结束 3已取消',
    publisher_id BIGINT COMMENT '发布人ID',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 活动报名表
CREATE TABLE IF NOT EXISTS activity_sign_up (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    sign_up_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    status TINYINT DEFAULT 1 COMMENT '报名状态 0已取消 1已报名',
    remark VARCHAR(200) COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_activity_user (activity_id, user_id)
);

-- 活动签到表
CREATE TABLE IF NOT EXISTS activity_sign_in (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    sign_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    sign_in_type TINYINT DEFAULT 1 COMMENT '签到类型 1扫码 2手动',
    is_offline TINYINT DEFAULT 0 COMMENT '是否离线签到 0否 1是',
    sync_status TINYINT DEFAULT 1 COMMENT '同步状态 0未同步 1已同步',
    location VARCHAR(200) COMMENT '签到地点',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_activity_user_sign (activity_id, user_id)
);

-- 社团动态表
CREATE TABLE IF NOT EXISTS club_feed (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_id BIGINT NOT NULL COMMENT '社团ID',
    user_id BIGINT NOT NULL COMMENT '发布人ID',
    content TEXT NOT NULL COMMENT '动态内容',
    images VARCHAR(1000) COMMENT '图片列表，逗号分隔',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 动态点赞表
CREATE TABLE IF NOT EXISTS feed_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feed_id BIGINT NOT NULL COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_feed_user (feed_id, user_id)
);

-- 动态评论表
CREATE TABLE IF NOT EXISTS feed_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feed_id BIGINT NOT NULL COMMENT '动态ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content VARCHAR(500) NOT NULL COMMENT '评论内容',
    parent_id BIGINT COMMENT '父评论ID',
    reply_to_id BIGINT COMMENT '回复目标用户ID',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 消息表
CREATE TABLE IF NOT EXISTS sys_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    type VARCHAR(50) NOT NULL COMMENT '消息类型 system/activity/club/chat',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    biz_id BIGINT COMMENT '业务ID',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0未读 1已读',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 消息通知设置表
CREATE TABLE IF NOT EXISTS notification_setting (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    activity_reminder TINYINT DEFAULT 1 COMMENT '活动提醒 0关闭 1开启',
    club_announcement TINYINT DEFAULT 1 COMMENT '社团公告 0关闭 1开启',
    new_member_notice TINYINT DEFAULT 1 COMMENT '新成员通知 0关闭 1开启',
    activity_sign_in_reminder TINYINT DEFAULT 1 COMMENT '签到提醒 0关闭 1开启',
    chat_message_notice TINYINT DEFAULT 1 COMMENT '聊天消息通知 0关闭 1开启',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 聊天消息表
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    club_id BIGINT NOT NULL COMMENT '社团ID',
    sender_id BIGINT NOT NULL COMMENT '发送人ID',
    message_type VARCHAR(20) NOT NULL COMMENT '消息类型 text/image/voice',
    content TEXT COMMENT '消息内容',
    media_url VARCHAR(255) COMMENT '媒体文件地址',
    duration INT COMMENT '语音时长(秒)',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
