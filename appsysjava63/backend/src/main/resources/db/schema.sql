-- ============================================
-- 共享单车系统数据库表结构设计 (H2数据库兼容版)
-- ============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(100),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    real_name VARCHAR(50),
    id_card VARCHAR(20) UNIQUE,
    id_card_verified TINYINT DEFAULT 0,
    face_verified TINYINT DEFAULT 0,
    real_name_verified TINYINT DEFAULT 0,
    wechat_openid VARCHAR(100) UNIQUE,
    alipay_openid VARCHAR(100) UNIQUE,
    deposit_status TINYINT DEFAULT 0,
    deposit_amount DECIMAL(10,2) DEFAULT 0,
    balance DECIMAL(10,2) DEFAULT 0,
    credit_score INT DEFAULT 100,
    credit_level VARCHAR(20) DEFAULT 'NORMAL',
    status TINYINT DEFAULT 1,
    last_login_time TIMESTAMP,
    last_login_ip VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_user_phone ON "user"(phone);
CREATE INDEX IF NOT EXISTS idx_user_status ON "user"(status);
CREATE INDEX IF NOT EXISTS idx_user_credit_score ON "user"(credit_score);

-- 2. 车辆表
CREATE TABLE IF NOT EXISTS bike (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bike_no VARCHAR(50) UNIQUE NOT NULL,
    bike_type VARCHAR(20) NOT NULL,
    qr_code VARCHAR(255) UNIQUE,
    bluetooth_mac VARCHAR(50) UNIQUE,
    battery_level INT DEFAULT 100,
    range_km DECIMAL(5,1),
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    location_address VARCHAR(255),
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    last_maintenance_time TIMESTAMP,
    total_rides INT DEFAULT 0,
    total_km DECIMAL(10,2) DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_bike_type ON bike(bike_type);
CREATE INDEX IF NOT EXISTS idx_bike_status ON bike(status);
CREATE INDEX IF NOT EXISTS idx_bike_location ON bike(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_bike_battery ON bike(battery_level);

-- 3. 骑行记录表
CREATE TABLE IF NOT EXISTS ride_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    bike_no VARCHAR(50),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    start_latitude DECIMAL(10,6),
    start_longitude DECIMAL(10,6),
    start_address VARCHAR(255),
    end_latitude DECIMAL(10,6),
    end_longitude DECIMAL(10,6),
    end_address VARCHAR(255),
    duration_minutes INT DEFAULT 0,
    distance_km DECIMAL(10,2) DEFAULT 0,
    base_price DECIMAL(10,2) DEFAULT 0,
    time_price DECIMAL(10,2) DEFAULT 0,
    peak_surcharge DECIMAL(10,2) DEFAULT 0,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    coupon_id BIGINT,
    total_amount DECIMAL(10,2) DEFAULT 0,
    actual_amount DECIMAL(10,2) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'UNPAID',
    payment_method VARCHAR(20),
    payment_time TIMESTAMP,
    is_peak_hour TINYINT DEFAULT 0,
    unlock_type VARCHAR(20),
    unlock_success TINYINT DEFAULT 1,
    fail_reason VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ONGOING',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_ride_user_id ON ride_record(user_id);
CREATE INDEX IF NOT EXISTS idx_ride_bike_id ON ride_record(bike_id);
CREATE INDEX IF NOT EXISTS idx_ride_status ON ride_record(status);
CREATE INDEX IF NOT EXISTS idx_ride_start_time ON ride_record(start_time);
CREATE INDEX IF NOT EXISTS idx_ride_payment_status ON ride_record(payment_status);

-- 4. 预约记录表
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    bike_no VARCHAR(50),
    reserve_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    cancel_time TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_reservation_user_id ON reservation(user_id);
CREATE INDEX IF NOT EXISTS idx_reservation_bike_id ON reservation(bike_id);
CREATE INDEX IF NOT EXISTS idx_reservation_status ON reservation(status);
CREATE INDEX IF NOT EXISTS idx_reservation_expire_time ON reservation(expire_time);

-- 5. 故障上报表
CREATE TABLE IF NOT EXISTS fault_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    bike_no VARCHAR(50),
    fault_type VARCHAR(50) NOT NULL,
    fault_description CLOB,
    image_urls CLOB,
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    status VARCHAR(20) DEFAULT 'PENDING',
    maintenance_id BIGINT,
    reward_coupon_id BIGINT,
    reward_amount DECIMAL(10,2) DEFAULT 0,
    handle_remark VARCHAR(500),
    handle_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_fault_user_id ON fault_report(user_id);
CREATE INDEX IF NOT EXISTS idx_fault_bike_id ON fault_report(bike_id);
CREATE INDEX IF NOT EXISTS idx_fault_status ON fault_report(status);
CREATE INDEX IF NOT EXISTS idx_fault_type ON fault_report(fault_type);

-- 6. 信用分记录表
CREATE TABLE IF NOT EXISTS credit_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    change_type VARCHAR(20) NOT NULL,
    score_change INT NOT NULL,
    reason VARCHAR(200) NOT NULL,
    related_id BIGINT,
    related_type VARCHAR(50),
    before_score INT NOT NULL,
    after_score INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_credit_user_id ON credit_record(user_id);
CREATE INDEX IF NOT EXISTS idx_credit_change_type ON credit_record(change_type);
CREATE INDEX IF NOT EXISTS idx_credit_create_time ON credit_record(create_time);

-- 7. 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_template_id BIGINT,
    coupon_name VARCHAR(100) NOT NULL,
    coupon_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(5,2),
    min_amount DECIMAL(10,2) DEFAULT 0,
    max_discount DECIMAL(10,2),
    issue_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    use_time TIMESTAMP,
    ride_record_id BIGINT,
    status VARCHAR(20) DEFAULT 'UNUSED',
    source VARCHAR(50),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_coupon_user_id ON coupon(user_id);
CREATE INDEX IF NOT EXISTS idx_coupon_status ON coupon(status);
CREATE INDEX IF NOT EXISTS idx_coupon_expire_time ON coupon(expire_time);

-- 8. 押金记录表
CREATE TABLE IF NOT EXISTS deposit_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    record_type VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) DEFAULT 0,
    payment_method VARCHAR(20),
    payment_no VARCHAR(100),
    credit_auth_source VARCHAR(50),
    credit_score INT,
    status VARCHAR(20) DEFAULT 'SUCCESS',
    remark VARCHAR(500),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_deposit_user_id ON deposit_record(user_id);
CREATE INDEX IF NOT EXISTS idx_deposit_record_type ON deposit_record(record_type);
CREATE INDEX IF NOT EXISTS idx_deposit_status ON deposit_record(status);

-- 9. 支付记录表
CREATE TABLE IF NOT EXISTS payment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    related_id BIGINT,
    related_type VARCHAR(20),
    payment_type VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    third_party_no VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING',
    pay_time TIMESTAMP,
    fail_reason VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_payment_user_id ON payment_record(user_id);
CREATE INDEX IF NOT EXISTS idx_payment_order_no ON payment_record(order_no);
CREATE INDEX IF NOT EXISTS idx_payment_status ON payment_record(status);
CREATE INDEX IF NOT EXISTS idx_payment_create_time ON payment_record(create_time);

-- 10. 充值记录表
CREATE TABLE IF NOT EXISTS recharge_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recharge_amount DECIMAL(10,2) NOT NULL,
    bonus_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_record_id BIGINT,
    status VARCHAR(20) DEFAULT 'PENDING',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_recharge_user_id ON recharge_record(user_id);
CREATE INDEX IF NOT EXISTS idx_recharge_status ON recharge_record(status);

-- 11. 动态二维码表
CREATE TABLE IF NOT EXISTS dynamic_qrcode (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bike_id BIGINT NOT NULL,
    qrcode_content VARCHAR(255) NOT NULL,
    generate_time TIMESTAMP NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    used TINYINT DEFAULT 0,
    use_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_qrcode_bike_id ON dynamic_qrcode(bike_id);
CREATE INDEX IF NOT EXISTS idx_qrcode_expire_time ON dynamic_qrcode(expire_time);

-- 12. 系统配置表
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value CLOB,
    config_name VARCHAR(100),
    config_desc VARCHAR(500),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
