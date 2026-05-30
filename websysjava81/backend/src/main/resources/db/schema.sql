CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL UNIQUE,
    `nickname` VARCHAR(50) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `avatar` VARCHAR(255),
    `email` VARCHAR(100),
    `member_status` TINYINT DEFAULT 0 COMMENT '0-免费用户, 1-付费用户',
    `user_status` TINYINT DEFAULT 0 COMMENT '0-正常, 1-已封禁',
    `total_game_time` INT DEFAULT 0 COMMENT '游戏总时长(分钟)',
    `register_time` DATETIME NOT NULL,
    `last_login_time` DATETIME,
    `deleted` TINYINT DEFAULT 0,
    INDEX `idx_user_user_id` (`user_id`),
    INDEX `idx_user_nickname` (`nickname`),
    INDEX `idx_user_register_time` (`register_time`)
);

CREATE TABLE IF NOT EXISTS `ban_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `ban_type` TINYINT NOT NULL COMMENT '0-临时封禁, 1-永久封禁',
    `ban_reason` VARCHAR(500) NOT NULL,
    `unban_time` DATETIME,
    `operator_id` VARCHAR(32),
    `operator_name` VARCHAR(50),
    `status` TINYINT DEFAULT 1 COMMENT '0-已撤销, 1-生效中',
    `create_time` DATETIME NOT NULL,
    INDEX `idx_ban_record_user_id` (`user_id`),
    INDEX `idx_ban_record_status` (`status`)
);

CREATE TABLE IF NOT EXISTS `game` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `game_id` VARCHAR(32) NOT NULL UNIQUE,
    `game_name` VARCHAR(100) NOT NULL,
    `cover` VARCHAR(255),
    `description` TEXT,
    `deleted` TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `comment_id` VARCHAR(32) NOT NULL UNIQUE,
    `game_id` VARCHAR(32) NOT NULL,
    `user_id` VARCHAR(32) NOT NULL,
    `content` TEXT NOT NULL,
    `audit_status` TINYINT DEFAULT 0 COMMENT '0-待审核, 1-已通过, 2-已删除, 3-已屏蔽',
    `has_sensitive_word` TINYINT DEFAULT 0,
    `create_time` DATETIME NOT NULL,
    `audit_time` DATETIME,
    `auditor_id` VARCHAR(32),
    `deleted` TINYINT DEFAULT 0,
    INDEX `idx_comment_audit_status` (`audit_status`),
    INDEX `idx_comment_game_id` (`game_id`),
    INDEX `idx_comment_user_id` (`user_id`),
    INDEX `idx_comment_create_time` (`create_time`)
);

CREATE TABLE IF NOT EXISTS `report` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `report_id` VARCHAR(32) NOT NULL UNIQUE,
    `report_type` VARCHAR(20) NOT NULL COMMENT 'user/comment/game',
    `target_id` VARCHAR(32) NOT NULL,
    `reason_type` VARCHAR(20) NOT NULL COMMENT 'porn/violence/advertise/other',
    `reason_detail` VARCHAR(500),
    `reporter_id` VARCHAR(32) NOT NULL,
    `status` TINYINT DEFAULT 0 COMMENT '0-待处理, 1-已确认违规, 2-已驳回',
    `handle_time` DATETIME,
    `handler_id` VARCHAR(32),
    `handler_name` VARCHAR(50),
    `create_time` DATETIME NOT NULL,
    INDEX `idx_report_status` (`status`),
    INDEX `idx_report_reason_type` (`reason_type`),
    INDEX `idx_report_create_time` (`create_time`)
);

CREATE TABLE IF NOT EXISTS `sensitive_word` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `word` VARCHAR(100) NOT NULL,
    `word_type` TINYINT DEFAULT 0 COMMENT '0-普通词, 1-正则表达式',
    `category` VARCHAR(50),
    `create_time` DATETIME NOT NULL,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE `uk_sensitive_word` (`word`)
);

CREATE TABLE IF NOT EXISTS `game_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `game_id` VARCHAR(32) NOT NULL,
    `game_name` VARCHAR(100) NOT NULL,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME,
    `duration` INT DEFAULT 0 COMMENT '游戏时长(分钟)',
    INDEX `idx_game_record_user_id` (`user_id`),
    INDEX `idx_game_record_start_time` (`start_time`)
);

CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `game_id` VARCHAR(32) NOT NULL,
    `game_name` VARCHAR(100) NOT NULL,
    `game_cover` VARCHAR(255),
    `create_time` DATETIME NOT NULL,
    UNIQUE `uk_favorite_user_game` (`user_id`, `game_id`)
);

CREATE TABLE IF NOT EXISTS `user_behavior` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `behavior_type` VARCHAR(50) NOT NULL,
    `behavior_detail` VARCHAR(500),
    `ip_address` VARCHAR(50),
    `device_info` VARCHAR(200),
    `create_time` DATETIME NOT NULL,
    INDEX `idx_user_behavior_user_id` (`user_id`),
    INDEX `idx_user_behavior_create_time` (`create_time`)
);

CREATE TABLE IF NOT EXISTS `login_device` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `device_type` VARCHAR(50),
    `device_model` VARCHAR(100),
    `os_version` VARCHAR(100),
    `ip_address` VARCHAR(50),
    `login_time` DATETIME NOT NULL,
    INDEX `idx_login_device_user_id` (`user_id`)
);
