-- 订单测试数据
INSERT INTO orders (order_no, user_id, user_name, product_name, amount, pay_channel, pay_status, ship_status, order_status, remark, pay_time, create_time) VALUES
('ORD20260501001', 1, '张三', 'iPhone 15 Pro', 7999.00, 'WECHAT', 'SUCCESS', 'SHIPPED', 'NORMAL', '正常订单', '2026-05-01 10:30:00', '2026-05-01 10:00:00'),
('ORD20260501002', 2, '李四', 'MacBook Air', 8999.00, 'ALIPAY', 'SUCCESS', 'PENDING', 'EXCEPTION', '支付成功未出货', '2026-05-01 14:20:00', '2026-05-01 14:00:00'),
('ORD20260502001', 3, '王五', '华为Mate 60', 5999.00, 'UNIONPAY', 'SUCCESS', 'FAILED', 'EXCEPTION', '出货失败', '2026-05-02 09:15:00', '2026-05-02 09:00:00'),
('ORD20260502002', 4, '赵六', '小米14', 3999.00, 'WECHAT', 'SUCCESS', 'SHIPPED', 'EXCEPTION', '重复扣款', '2026-05-02 11:30:00', '2026-05-02 11:00:00'),
('ORD20260503001', 5, '孙七', 'iPad Pro', 6999.00, 'ALIPAY', 'SUCCESS', 'SHIPPED', 'NORMAL', '正常订单', '2026-05-03 16:45:00', '2026-05-03 16:00:00'),
('ORD20260504001', 1, '张三', 'AirPods Pro', 1899.00, 'WECHAT', 'SUCCESS', 'SHIPPED', 'NORMAL', '正常订单', '2026-05-04 10:00:00', '2026-05-04 09:30:00'),
('ORD20260505001', 2, '李四', 'Apple Watch', 2999.00, 'ALIPAY', 'SUCCESS', 'PENDING', 'NORMAL', '正常订单', '2026-05-05 15:30:00', '2026-05-05 15:00:00'),
('ORD20260505002', 6, '周八', '三星Galaxy', 4999.00, 'UNIONPAY', 'SUCCESS', 'FAILED', 'EXCEPTION', '出货失败待退款', '2026-05-05 17:00:00', '2026-05-05 16:30:00');

-- 异常订单测试数据
INSERT INTO exception_order (order_id, order_no, exception_type, exception_desc, handle_status, create_time) VALUES
(2, 'ORD20260501002', 'PAY_SUCCESS_NO_SHIP', '支付成功但超过24小时未出货', 'PENDING', '2026-05-02 14:00:00'),
(3, 'ORD20260502001', 'SHIP_FAILED', '仓库库存不足，出货失败', 'PROCESSING', '2026-05-02 10:00:00'),
(4, 'ORD20260502002', 'DUPLICATE_PAY', '用户重复支付，同一订单扣款两次', 'PENDING', '2026-05-02 12:00:00'),
(8, 'ORD20260505002', 'SHIP_FAILED', '物流系统异常，出货失败', 'PENDING', '2026-05-05 18:00:00');

-- 支付记录测试数据
INSERT INTO payment_record (order_id, order_no, pay_channel, trade_no, amount, pay_status, pay_time, create_time) VALUES
(1, 'ORD20260501001', 'WECHAT', 'WX202605011030001', 7999.00, 'SUCCESS', '2026-05-01 10:30:00', '2026-05-01 10:30:00'),
(2, 'ORD20260501002', 'ALIPAY', 'AL202605011420001', 8999.00, 'SUCCESS', '2026-05-01 14:20:00', '2026-05-01 14:20:00'),
(3, 'ORD20260502001', 'UNIONPAY', 'UP202605020915001', 5999.00, 'SUCCESS', '2026-05-02 09:15:00', '2026-05-02 09:15:00'),
(4, 'ORD20260502002', 'WECHAT', 'WX202605021130001', 3999.00, 'SUCCESS', '2026-05-02 11:30:00', '2026-05-02 11:30:00'),
(4, 'ORD20260502002', 'WECHAT', 'WX202605021130002', 3999.00, 'SUCCESS', '2026-05-02 11:30:01', '2026-05-02 11:30:01'),
(5, 'ORD20260503001', 'ALIPAY', 'AL202605031645001', 6999.00, 'SUCCESS', '2026-05-03 16:45:00', '2026-05-03 16:45:00'),
(6, 'ORD20260504001', 'WECHAT', 'WX202605041000001', 1899.00, 'SUCCESS', '2026-05-04 10:00:00', '2026-05-04 10:00:00'),
(7, 'ORD20260505001', 'ALIPAY', 'AL202605051530001', 2999.00, 'SUCCESS', '2026-05-05 15:30:00', '2026-05-05 15:30:00'),
(8, 'ORD20260505002', 'UNIONPAY', 'UP202605051700001', 4999.00, 'SUCCESS', '2026-05-05 17:00:00', '2026-05-05 17:00:00');

-- 对账记录测试数据
INSERT INTO reconciliation (recon_date, pay_channel, recon_type, system_amount, third_amount, diff_amount, system_count, third_count, diff_count, status, remark, recon_time, create_time) VALUES
('2026-05-01', 'WECHAT', 'DAILY', 7999.00, 7999.00, 0.00, 1, 1, 0, 'MATCHED', '对账一致', '2026-05-02 02:00:00', '2026-05-02 02:00:00'),
('2026-05-01', 'ALIPAY', 'DAILY', 8999.00, 8999.00, 0.00, 1, 1, 0, 'MATCHED', '对账一致', '2026-05-02 02:05:00', '2026-05-02 02:05:00'),
('2026-05-02', 'WECHAT', 'DAILY', 3999.00, 7998.00, 3999.00, 1, 2, 1, 'UNMATCHED', '存在重复扣款', '2026-05-03 02:00:00', '2026-05-03 02:00:00'),
('2026-05-02', 'UNIONPAY', 'DAILY', 5999.00, 5999.00, 0.00, 1, 1, 0, 'MATCHED', '对账一致', '2026-05-03 02:10:00', '2026-05-03 02:10:00'),
('2026-05-03', 'ALIPAY', 'DAILY', 6999.00, 6999.00, 0.00, 1, 1, 0, 'MATCHED', '对账一致', '2026-05-04 02:00:00', '2026-05-04 02:00:00'),
('2026-05-01', 'WECHAT', 'MONTHLY', 7999.00, 7999.00, 0.00, 1, 1, 0, 'MATCHED', '月度对账一致', '2026-06-01 10:00:00', '2026-06-01 10:00:00');

-- 发票申请测试数据
INSERT INTO invoice_application (order_id, order_no, user_id, user_name, invoice_type, invoice_title, tax_no, invoice_content, amount, email, phone, status, create_time) VALUES
(1, 'ORD20260501001', 1, '张三', 'PERSONAL', '张三', NULL, '电子产品', 7999.00, 'zhangsan@example.com', '13800138001', 'PENDING', '2026-05-02 09:00:00'),
(2, 'ORD20260501002', 2, '李四', 'ENTERPRISE', 'XX科技有限公司', '91110105MA00XXXXX', '办公设备', 8999.00, 'lisi@company.com', '13800138002', 'APPROVED', '2026-05-02 10:00:00'),
(5, 'ORD20260503001', 5, '孙七', 'PERSONAL', '孙七', NULL, '电子产品', 6999.00, 'sunqi@example.com', '13800138005', 'ISSUED', '2026-05-04 09:00:00'),
(6, 'ORD20260504001', 1, '张三', 'ENTERPRISE', 'XX贸易公司', '91110106MA00YYYYY', '数码配件', 1899.00, 'admin@trade.com', '13800138001', 'REJECTED', '2026-05-05 10:00:00');

UPDATE invoice_application SET 
    invoice_no = 'FP20260504001',
    invoice_code = '1100261234',
    issue_time = '2026-05-04 14:00:00',
    reviewer = '财务小张'
WHERE id = 3;

UPDATE invoice_application SET 
    reject_reason = '发票抬头与订单信息不符，请重新申请',
    reviewer = '财务小王'
WHERE id = 4;

-- 销售记录测试数据
INSERT INTO sales_record (order_id, order_no, user_id, amount, pay_channel, sale_date, sale_time, create_time) VALUES
(1, 'ORD20260501001', 1, 7999.00, 'WECHAT', '2026-05-01', '2026-05-01 10:30:00', '2026-05-01 10:30:00'),
(2, 'ORD20260501002', 2, 8999.00, 'ALIPAY', '2026-05-01', '2026-05-01 14:20:00', '2026-05-01 14:20:00'),
(3, 'ORD20260502001', 3, 5999.00, 'UNIONPAY', '2026-05-02', '2026-05-02 09:15:00', '2026-05-02 09:15:00'),
(4, 'ORD20260502002', 4, 3999.00, 'WECHAT', '2026-05-02', '2026-05-02 11:30:00', '2026-05-02 11:30:00'),
(5, 'ORD20260503001', 5, 6999.00, 'ALIPAY', '2026-05-03', '2026-05-03 16:45:00', '2026-05-03 16:45:00'),
(6, 'ORD20260504001', 1, 1899.00, 'WECHAT', '2026-05-04', '2026-05-04 10:00:00', '2026-05-04 10:00:00'),
(7, 'ORD20260505001', 2, 2999.00, 'ALIPAY', '2026-05-05', '2026-05-05 15:30:00', '2026-05-05 15:30:00'),
(8, 'ORD20260505002', 6, 4999.00, 'UNIONPAY', '2026-05-05', '2026-05-05 17:00:00', '2026-05-05 17:00:00');
