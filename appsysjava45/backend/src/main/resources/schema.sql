-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender VARCHAR(10),
    age INT,
    address VARCHAR(500),
    points INT DEFAULT 0,
    enabled BOOLEAN DEFAULT TRUE,
    is_new_user BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(255),
    sort_order INT DEFAULT 0,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    detail TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    image VARCHAR(255),
    category VARCHAR(50),
    stock INT DEFAULT 0,
    sales INT DEFAULT 0,
    is_hot BOOLEAN DEFAULT FALSE,
    is_new BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 购物车表
CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    selected BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS sys_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    freight_amount DECIMAL(10,2) DEFAULT 0,
    pay_amount DECIMAL(10,2) NOT NULL,
    pay_method VARCHAR(20),
    pay_time TIMESTAMP,
    delivery_type VARCHAR(20),
    receiver_name VARCHAR(50),
    receiver_phone VARCHAR(20),
    receiver_address VARCHAR(500),
    status VARCHAR(20) DEFAULT 'pending_payment',
    remark VARCHAR(500),
    cancel_reason VARCHAR(500),
    cancel_time TIMESTAMP,
    delivery_time TIMESTAMP,
    complete_time TIMESTAMP,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    product_image VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    create_time TIMESTAMP
);

-- 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20),
    discount_value DECIMAL(10,2),
    min_amount DECIMAL(10,2),
    total_count INT,
    used_count INT DEFAULT 0,
    receive_count INT DEFAULT 0,
    is_new_user_only BOOLEAN DEFAULT FALSE,
    valid_days INT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'unused',
    use_time TIMESTAMP,
    expire_time TIMESTAMP,
    receive_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_time TIMESTAMP
);

-- 限时秒杀表
CREATE TABLE IF NOT EXISTS flash_sale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sale_price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    sold_count INT DEFAULT 0,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 拼团活动表
CREATE TABLE IF NOT EXISTS group_buy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    group_price DECIMAL(10,2) NOT NULL,
    required_people INT NOT NULL,
    current_people INT DEFAULT 0,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 自提点表
CREATE TABLE IF NOT EXISTS pickup_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    latitude VARCHAR(100),
    longitude VARCHAR(100),
    phone VARCHAR(20),
    business_hours VARCHAR(200),
    description VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

-- 积分记录表
CREATE TABLE IF NOT EXISTS point_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    order_id BIGINT,
    create_time TIMESTAMP
);
