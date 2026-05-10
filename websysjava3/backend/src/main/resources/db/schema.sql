-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(64) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(128) COMMENT '用户名称',
    product_name VARCHAR(255) NOT NULL COMMENT '商品名称',
    amount DECIMAL(12,2) NOT NULL COMMENT '订单金额',
    pay_channel VARCHAR(32) COMMENT '支付渠道: WECHAT/ALIPAY/UNIONPAY',
    pay_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '支付状态: PENDING/SUCCESS/FAILED/REFUNDED',
    ship_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '出货状态: PENDING/SHIPPED/FAILED',
    order_status VARCHAR(32) DEFAULT 'NORMAL' COMMENT '订单状态: NORMAL/EXCEPTION',
    remark VARCHAR(512) COMMENT '备注',
    pay_time TIMESTAMP COMMENT '支付时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 异常订单表
CREATE TABLE IF NOT EXISTS exception_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    exception_type VARCHAR(64) NOT NULL COMMENT '异常类型: PAY_SUCCESS_NO_SHIP/SHIP_FAILED/DUPLICATE_PAY/OTHER',
    exception_desc VARCHAR(512) COMMENT '异常描述',
    handle_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '处理状态: PENDING/PROCESSING/RESOLVED',
    handle_type VARCHAR(32) COMMENT '处理方式: REFUND/RESHIP/OTHER',
    handle_remark VARCHAR(512) COMMENT '处理备注',
    handle_time TIMESTAMP COMMENT '处理时间',
    handler VARCHAR(64) COMMENT '处理人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 支付记录表
CREATE TABLE IF NOT EXISTS payment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    pay_channel VARCHAR(32) COMMENT '支付渠道',
    trade_no VARCHAR(128) COMMENT '第三方交易号',
    amount DECIMAL(12,2) NOT NULL COMMENT '支付金额',
    pay_status VARCHAR(32) DEFAULT 'PENDING' COMMENT '支付状态',
    pay_time TIMESTAMP COMMENT '支付时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 对账记录表
CREATE TABLE IF NOT EXISTS reconciliation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recon_date DATE NOT NULL COMMENT '对账日期',
    pay_channel VARCHAR(32) NOT NULL COMMENT '支付渠道',
    recon_type VARCHAR(32) COMMENT '对账类型: DAILY/MONTHLY',
    system_amount DECIMAL(12,2) DEFAULT 0 COMMENT '系统金额',
    third_amount DECIMAL(12,2) DEFAULT 0 COMMENT '第三方金额',
    diff_amount DECIMAL(12,2) DEFAULT 0 COMMENT '差异金额',
    system_count INT DEFAULT 0 COMMENT '系统订单数',
    third_count INT DEFAULT 0 COMMENT '第三方订单数',
    diff_count INT DEFAULT 0 COMMENT '差异订单数',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '对账状态: PENDING/MATCHED/UNMATCHED',
    remark VARCHAR(512) COMMENT '备注',
    recon_time TIMESTAMP COMMENT '对账时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 对账差异明细表
CREATE TABLE IF NOT EXISTS reconciliation_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recon_id BIGINT NOT NULL COMMENT '对账ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    diff_type VARCHAR(32) COMMENT '差异类型: MISSING_SYSTEM/MISSING_THIRD/AMOUNT_DIFF',
    system_amount DECIMAL(12,2) COMMENT '系统金额',
    third_amount DECIMAL(12,2) COMMENT '第三方金额',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '处理状态',
    remark VARCHAR(512) COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 发票申请表
CREATE TABLE IF NOT EXISTS invoice_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(128) COMMENT '用户名称',
    invoice_type VARCHAR(32) COMMENT '发票类型: PERSONAL/ENTERPRISE',
    invoice_title VARCHAR(255) NOT NULL COMMENT '发票抬头',
    tax_no VARCHAR(64) COMMENT '税号',
    invoice_content VARCHAR(255) COMMENT '发票内容',
    amount DECIMAL(12,2) NOT NULL COMMENT '开票金额',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(32) COMMENT '电话',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED/REJECTED/ISSUED',
    invoice_no VARCHAR(64) COMMENT '发票号码',
    invoice_code VARCHAR(64) COMMENT '发票代码',
    issue_time TIMESTAMP COMMENT '开票时间',
    reject_reason VARCHAR(512) COMMENT '拒绝原因',
    reviewer VARCHAR(64) COMMENT '审核人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 销售记录表
CREATE TABLE IF NOT EXISTS sales_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(12,2) NOT NULL COMMENT '销售金额',
    pay_channel VARCHAR(32) COMMENT '支付渠道',
    sale_date DATE NOT NULL COMMENT '销售日期',
    sale_time TIMESTAMP COMMENT '销售时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);
