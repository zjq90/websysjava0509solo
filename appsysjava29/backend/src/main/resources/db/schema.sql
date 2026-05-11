-- 医院预约挂号系统数据库表结构设计
-- 支持H2数据库

-- 用户表
DROP TABLE IF EXISTS sys_user CASCADE;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    id_card VARCHAR(50),
    email VARCHAR(100),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0 COMMENT '0-未知 1-男 2-女',
    birthday DATE,
    address VARCHAR(255),
    status TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    elder_mode TINYINT DEFAULT 0 COMMENT '0-普通模式 1-长辈模式',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
DROP TABLE IF EXISTS sys_role CASCADE;
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户角色关联表
DROP TABLE IF EXISTS sys_user_role CASCADE;
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 权限表
DROP TABLE IF EXISTS sys_permission CASCADE;
CREATE TABLE sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perm_name VARCHAR(100) NOT NULL,
    perm_code VARCHAR(100) NOT NULL UNIQUE,
    resource_type VARCHAR(20) COMMENT 'menu/button/api',
    parent_id BIGINT DEFAULT 0,
    path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色权限关联表
DROP TABLE IF EXISTS sys_role_permission CASCADE;
CREATE TABLE sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 科室表
DROP TABLE IF EXISTS department CASCADE;
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    dept_code VARCHAR(50) UNIQUE,
    parent_id BIGINT DEFAULT 0,
    dept_type VARCHAR(20) COMMENT '临床/医技/行政',
    location VARCHAR(255),
    description TEXT,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 医生表
DROP TABLE IF EXISTS doctor CASCADE;
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    dept_id BIGINT NOT NULL,
    doctor_name VARCHAR(50) NOT NULL,
    title VARCHAR(50) COMMENT '主任/副主任/主治/住院',
    title_level VARCHAR(20) COMMENT 'senior/deputy/attending/resident',
    specialty VARCHAR(255) COMMENT '专长',
    introduction TEXT COMMENT '简介',
    avatar VARCHAR(255),
    consultation_fee DECIMAL(10,2) COMMENT '挂号费',
    is_expert TINYINT DEFAULT 0 COMMENT '0-普通 1-专家',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 号源表(每日排班)
DROP TABLE IF EXISTS schedule CASCADE;
CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    schedule_date DATE NOT NULL,
    time_period VARCHAR(20) COMMENT 'morning/afternoon/night',
    period_name VARCHAR(50) COMMENT '上午/下午/夜间',
    start_time TIME,
    end_time TIME,
    total_slots INT DEFAULT 0,
    booked_slots INT DEFAULT 0,
    available_slots INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '0-停诊 1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 号源时段明细表
DROP TABLE IF EXISTS schedule_slot CASCADE;
CREATE TABLE schedule_slot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    slot_time VARCHAR(20) NOT NULL COMMENT '09:00-09:15',
    slot_start TIME NOT NULL,
    slot_end TIME NOT NULL,
    slot_index INT,
    status VARCHAR(20) DEFAULT 'available' COMMENT 'available/locked/booked',
    lock_user_id BIGINT,
    lock_expire_time TIMESTAMP,
    appointment_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 预约表
DROP TABLE IF EXISTS appointment CASCADE;
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    slot_id BIGINT,
    doctor_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    appointment_no VARCHAR(50) UNIQUE,
    appointment_date DATE,
    slot_time VARCHAR(20),
    patient_name VARCHAR(50),
    patient_phone VARCHAR(20),
    patient_id_card VARCHAR(50),
    patient_relation VARCHAR(20) COMMENT 'self/family',
    symptoms TEXT,
    consultation_fee DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/paid/confirmed/cancelled/completed',
    cancel_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 支付表
DROP TABLE IF EXISTS payment CASCADE;
CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_no VARCHAR(50) UNIQUE,
    appointment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    insurance_amount DECIMAL(10,2) DEFAULT 0 COMMENT '医保报销金额',
    self_pay_amount DECIMAL(10,2) DEFAULT 0 COMMENT '自付金额',
    pay_method VARCHAR(20) COMMENT 'wechat/alipay/unionpay/insurance',
    pay_status VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/paid/refunded/failed',
    insurance_no VARCHAR(50) COMMENT '医保电子凭证号',
    insurance_data TEXT COMMENT '医保结算数据',
    third_party_no VARCHAR(100) COMMENT '第三方支付单号',
    paid_time TIMESTAMP,
    fail_reason TEXT,
    refund_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 操作日志表
DROP TABLE IF EXISTS operation_log CASCADE;
CREATE TABLE operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    module VARCHAR(100),
    operation VARCHAR(100),
    method VARCHAR(255),
    request_params TEXT,
    request_uri VARCHAR(255),
    ip VARCHAR(50),
    user_agent VARCHAR(255),
    status TINYINT DEFAULT 1 COMMENT '1-成功 0-失败',
    error_msg TEXT,
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 患者表(就诊人)
DROP TABLE IF EXISTS patient CASCADE;
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    patient_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    id_card VARCHAR(50),
    gender TINYINT,
    birthday DATE,
    relation VARCHAR(20) COMMENT 'self/father/mother/son/daughter/spouse/other',
    is_default TINYINT DEFAULT 0,
    insurance_no VARCHAR(50),
    allergy_history TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_phone ON sys_user(phone);
CREATE INDEX idx_doctor_dept ON doctor(dept_id);
CREATE INDEX idx_doctor_title ON doctor(title_level);
CREATE INDEX idx_schedule_doctor ON schedule(doctor_id);
CREATE INDEX idx_schedule_date ON schedule(schedule_date);
CREATE INDEX idx_slot_schedule ON schedule_slot(schedule_id);
CREATE INDEX idx_slot_status ON schedule_slot(status);
CREATE INDEX idx_appointment_user ON appointment(user_id);
CREATE INDEX idx_appointment_status ON appointment(status);
CREATE INDEX idx_payment_appointment ON payment(appointment_id);
CREATE INDEX idx_log_time ON operation_log(operation_time);
CREATE INDEX idx_patient_user ON patient(user_id);
