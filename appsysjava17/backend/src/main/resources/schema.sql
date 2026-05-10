-- ============================================
-- 种子质量追溯系统 - 数据库初始化脚本
-- 包含：表结构创建 + 测试数据
-- ============================================

-- ============================================
-- 核心表：种子批次表 (seed_batch)
-- 存储种子批次的基本信息
-- ============================================
DROP TABLE IF EXISTS seed_batch;
CREATE TABLE seed_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_code VARCHAR(8) NOT NULL UNIQUE COMMENT '批次编号：8位数字+字母组合，全局唯一',
    seed_name VARCHAR(100) NOT NULL COMMENT '种子名称',
    seed_variety VARCHAR(100) COMMENT '种子品种',
    germination_rate DECIMAL(4,1) NOT NULL COMMENT '发芽率：0-100%，保留1位小数',
    purity DECIMAL(4,1) COMMENT '纯度(%)',
    moisture_content DECIMAL(4,1) COMMENT '水分含量(%)',
    production_date DATE NOT NULL COMMENT '生产日期',
    shelf_life DATE NOT NULL COMMENT '保质期(到期日期，不早于当前日期+6个月)',
    quantity INT NOT NULL COMMENT '数量(kg)',
    unit_price DECIMAL(10,2) COMMENT '单价',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE(活跃)/INACTIVE(停用)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50)
);

-- ============================================
-- 亲本信息表 (parent_info)
-- 存储种子的亲本来源信息
-- ============================================
DROP TABLE IF EXISTS parent_info;
CREATE TABLE parent_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    female_parent_code VARCHAR(50) COMMENT '母本编号',
    female_parent_name VARCHAR(100) COMMENT '母本名称',
    female_parent_origin VARCHAR(200) COMMENT '母本来源地',
    male_parent_code VARCHAR(50) COMMENT '父本编号',
    male_parent_name VARCHAR(100) COMMENT '父本名称',
    male_parent_origin VARCHAR(200) COMMENT '父本来源地',
    breeding_method VARCHAR(100) COMMENT '育种方式',
    breeding_organization VARCHAR(100) COMMENT '育种单位',
    breeding_year INT COMMENT '育种年份',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 田间管理记录表 (field_management)
-- 存储田间管理的详细记录（施肥、用药等）
-- ============================================
DROP TABLE IF EXISTS field_management;
CREATE TABLE field_management (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    operation_date DATE NOT NULL COMMENT '操作日期',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型：SOWING(播种)/FERTILIZER(施肥)/PESTICIDE(打药)/WATERING(浇水)/HARVEST(收获)',
    operation_name VARCHAR(100) NOT NULL COMMENT '操作名称',
    substance_name VARCHAR(200) COMMENT '物资名称(农药/化肥)',
    dosage VARCHAR(100) COMMENT '用量',
    concentration VARCHAR(100) COMMENT '浓度',
    application_method VARCHAR(100) COMMENT '施用方法',
    weather_condition VARCHAR(100) COMMENT '天气情况',
    temperature DECIMAL(5,2) COMMENT '温度(℃)',
    operator VARCHAR(100) COMMENT '操作人员',
    location VARCHAR(200) COMMENT '种植地点',
    soil_type VARCHAR(100) COMMENT '土壤类型',
    remark VARCHAR(500) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 加工信息表 (processing_info)
-- 存储种子加工流程的参数信息
-- ============================================
DROP TABLE IF EXISTS processing_info;
CREATE TABLE processing_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    process_date DATE NOT NULL COMMENT '加工日期',
    process_step VARCHAR(50) NOT NULL COMMENT '加工步骤：CLEANING(清选)/GRADING(分级)/DRYING(干燥)/PACKAGING(包装)',
    equipment_name VARCHAR(100) COMMENT '设备名称',
    equipment_model VARCHAR(100) COMMENT '设备型号',
    process_parameter VARCHAR(500) COMMENT '加工参数(温度、转速、时间等)',
    input_quantity DECIMAL(12,2) COMMENT '投入数量(kg)',
    output_quantity DECIMAL(12,2) COMMENT '产出数量(kg)',
    operator VARCHAR(100) COMMENT '操作人员',
    processing_plant VARCHAR(100) COMMENT '加工厂',
    quality_check_result VARCHAR(20) COMMENT '质检结果：PASS/FAIL',
    remark VARCHAR(500) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 质检报告表 (quality_report)
-- 存储种子质量检测报告
-- ============================================
DROP TABLE IF EXISTS quality_report;
CREATE TABLE quality_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    report_no VARCHAR(50) UNIQUE COMMENT '报告编号',
    inspection_date DATE NOT NULL COMMENT '检验日期',
    inspection_organization VARCHAR(100) COMMENT '检验机构',
    inspector VARCHAR(100) COMMENT '检验人员',
    germination_rate_test DECIMAL(4,1) COMMENT '发芽率检测值(%)',
    purity_test DECIMAL(4,1) COMMENT '纯度检测值(%)',
    moisture_test DECIMAL(4,1) COMMENT '水分检测值(%)',
    clarity_test DECIMAL(4,1) COMMENT '净度检测值(%)',
    vigor_test VARCHAR(100) COMMENT '活力检测',
    health_test VARCHAR(100) COMMENT '健康检测',
    other_tests VARCHAR(500) COMMENT '其他检测项目',
    overall_result VARCHAR(20) NOT NULL COMMENT '总体结论：QUALIFIED(合格)/UNQUALIFIED(不合格)',
    conclusion VARCHAR(500) COMMENT '检验结论',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 销售信息表 (sales_info)
-- 存储种子销售和客户信息
-- 注意：敏感信息使用AES-256加密存储
-- ============================================
DROP TABLE IF EXISTS sales_info;
CREATE TABLE sales_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    sales_date DATE NOT NULL COMMENT '销售日期',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_phone VARCHAR(200) NOT NULL COMMENT '客户手机号(加密存储)',
    customer_address VARCHAR(500) COMMENT '客户地址',
    customer_id_card VARCHAR(200) COMMENT '客户身份证号(加密存储)',
    purchase_quantity DECIMAL(12,2) NOT NULL COMMENT '购买数量(kg)',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价(元/kg)',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '总金额',
    sales_person VARCHAR(100) COMMENT '销售人员',
    sales_channel VARCHAR(50) COMMENT '销售渠道',
    delivery_address VARCHAR(500) COMMENT '配送地址',
    remark VARCHAR(500) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 溯源查询日志表 (trace_log)
-- 存储二维码扫描查询记录
-- ============================================
DROP TABLE IF EXISTS trace_log;
CREATE TABLE trace_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL COMMENT '批次ID',
    batch_code VARCHAR(8) NOT NULL COMMENT '批次编号',
    query_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '查询时间',
    query_ip VARCHAR(100) COMMENT '查询IP',
    query_device VARCHAR(200) COMMENT '查询设备信息',
    query_location VARCHAR(200) COMMENT '查询位置',
    user_agent VARCHAR(500) COMMENT '浏览器/应用信息',
    result_count INT COMMENT '返回结果数量',
    FOREIGN KEY (batch_id) REFERENCES seed_batch(id) ON DELETE CASCADE
);

-- ============================================
-- 系统用户表 (sys_user)
-- 存储系统管理员和操作人员信息
-- ============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) COMMENT '真实姓名',
    phone VARCHAR(200) COMMENT '手机号(加密)',
    email VARCHAR(100) COMMENT '邮箱',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色：ADMIN(管理员)/USER(普通用户)',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/INACTIVE',
    last_login_time TIMESTAMP COMMENT '最后登录时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
