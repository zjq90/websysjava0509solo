-- =============================================
-- 个人记账系统 - 数据库表结构
-- 符合数据库范式，包含完整约束
-- =============================================

-- 账户表：存储多账户信息
CREATE TABLE IF NOT EXISTS account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    account_name VARCHAR(50) NOT NULL COMMENT '账户名称',
    account_type VARCHAR(20) NOT NULL COMMENT '账户类型：CASH-现金，BANK_CARD-银行卡，CREDIT_CARD-信用卡，ALIPAY-支付宝，WECHAT-微信钱包，OTHER-其他',
    account_number VARCHAR(100) COMMENT '账户号码（银行卡号/账号）',
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    currency VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
    icon VARCHAR(50) COMMENT '账户图标',
    remark VARCHAR(200) COMMENT '备注说明',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认账户：0-否，1-是',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
);

-- 分类表：收入/支出分类
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_type VARCHAR(10) NOT NULL COMMENT '分类类型：INCOME-收入，EXPENSE-支出',
    parent_id BIGINT COMMENT '父分类ID，支持多级分类',
    icon VARCHAR(50) COMMENT '分类图标',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    remark VARCHAR(200) COMMENT '备注说明',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
);

-- 交易记录表：收支记录核心表
CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    transaction_type VARCHAR(10) NOT NULL COMMENT '交易类型：INCOME-收入，EXPENSE-支出',
    amount DECIMAL(12,2) NOT NULL COMMENT '交易金额',
    account_id BIGINT NOT NULL COMMENT '账户ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    transaction_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    description VARCHAR(500) COMMENT '交易描述/备注',
    merchant VARCHAR(100) COMMENT '商家名称',
    location VARCHAR(200) COMMENT '交易地点',
    is_recurring TINYINT NOT NULL DEFAULT 0 COMMENT '是否周期性交易：0-否，1-是',
    recurring_type VARCHAR(20) COMMENT '周期类型：DAILY-每天，WEEKLY-每周，MONTHLY-每月，YEARLY-每年',
    attachment VARCHAR(500) COMMENT '附件（小票照片等）',
    remark VARCHAR(200) COMMENT '备注说明',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 标签表：自定义标签
CREATE TABLE IF NOT EXISTS tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_color VARCHAR(20) COMMENT '标签颜色',
    tag_type VARCHAR(20) NOT NULL DEFAULT 'CUSTOM' COMMENT '标签类型：CUSTOM-自定义，SYSTEM-系统',
    icon VARCHAR(50) COMMENT '标签图标',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    remark VARCHAR(200) COMMENT '备注说明',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    CONSTRAINT uk_tag_name UNIQUE (tag_name)
);

-- 交易标签关联表：多对多关系
CREATE TABLE IF NOT EXISTS transaction_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    transaction_id BIGINT NOT NULL COMMENT '交易记录ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_tt_transaction FOREIGN KEY (transaction_id) REFERENCES transaction(id),
    CONSTRAINT fk_tt_tag FOREIGN KEY (tag_id) REFERENCES tag(id),
    CONSTRAINT uk_transaction_tag UNIQUE (transaction_id, tag_id)
);

-- 转账记录表：账户间资金流动
CREATE TABLE IF NOT EXISTS transfer_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    from_account_id BIGINT NOT NULL COMMENT '转出账户ID',
    to_account_id BIGINT NOT NULL COMMENT '转入账户ID',
    amount DECIMAL(12,2) NOT NULL COMMENT '转账金额',
    transfer_fee DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '转账手续费',
    transfer_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转账时间',
    description VARCHAR(200) COMMENT '转账说明',
    remark VARCHAR(200) COMMENT '备注说明',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    CONSTRAINT fk_transfer_from_account FOREIGN KEY (from_account_id) REFERENCES account(id),
    CONSTRAINT fk_transfer_to_account FOREIGN KEY (to_account_id) REFERENCES account(id)
);

-- 分类规则表：自定义匹配规则
CREATE TABLE IF NOT EXISTS category_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    rule_name VARCHAR(50) NOT NULL COMMENT '规则名称',
    match_field VARCHAR(20) NOT NULL COMMENT '匹配字段：DESCRIPTION-描述，MERCHANT-商家，LOCATION-地点',
    match_type VARCHAR(20) NOT NULL COMMENT '匹配类型：CONTAINS-包含，EQUALS-等于，REGEX-正则表达式',
    match_value VARCHAR(500) NOT NULL COMMENT '匹配值/正则表达式',
    target_category_id BIGINT NOT NULL COMMENT '目标分类ID',
    priority INT NOT NULL DEFAULT 0 COMMENT '优先级，数值越大优先级越高',
    is_enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    remark VARCHAR(200) COMMENT '备注说明',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    CONSTRAINT fk_rule_category FOREIGN KEY (target_category_id) REFERENCES category(id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_transaction_account_time ON transaction(account_id, transaction_time);
CREATE INDEX IF NOT EXISTS idx_transaction_category_time ON transaction(category_id, transaction_time);
CREATE INDEX IF NOT EXISTS idx_transaction_type_time ON transaction(transaction_type, transaction_time);
CREATE INDEX IF NOT EXISTS idx_transaction_tag_transaction ON transaction_tag(transaction_id);
CREATE INDEX IF NOT EXISTS idx_transaction_tag_tag ON transaction_tag(tag_id);
CREATE INDEX IF NOT EXISTS idx_transfer_record_time ON transfer_record(transfer_time);
CREATE INDEX IF NOT EXISTS idx_category_rule_priority ON category_rule(priority, is_enabled);
