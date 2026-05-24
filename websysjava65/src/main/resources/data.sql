-- 初始化区域数据
INSERT INTO areas (area_name, area_code, description, bike_count, today_rides, is_active, create_time, update_time)
VALUES ('朝阳区', 'CY001', '北京市朝阳区', 150, 320, true, NOW(), NOW()),
       ('海淀区', 'HD002', '北京市海淀区', 180, 450, true, NOW(), NOW()),
       ('东城区', 'DC003', '北京市东城区', 100, 210, true, NOW(), NOW()),
       ('西城区', 'XC004', '北京市西城区', 120, 280, true, NOW(), NOW()),
       ('丰台区', 'FT005', '北京市丰台区', 90, 180, true, NOW(), NOW());

-- 初始化用户数据
INSERT INTO users (username, phone, email, real_name, balance, deposit, ride_count, is_vip, is_blacklisted, user_level, create_time, update_time)
VALUES ('zhangsan', '13800138001', 'zhangsan@example.com', '张三', 99.50, 299.00, 45, true, false, 'HIGH_FREQUENCY', NOW(), NOW()),
       ('lisi', '13800138002', 'lisi@example.com', '李四', 150.00, 299.00, 23, false, false, 'NORMAL', NOW(), NOW()),
       ('wangwu', '13800138003', 'wangwu@example.com', '王五', 50.00, 0.00, 8, false, false, 'LOW_FREQUENCY', NOW(), NOW()),
       ('zhaoliu', '13800138004', 'zhaoliu@example.com', '赵六', 200.00, 299.00, 67, true, false, 'HIGH_FREQUENCY', NOW(), NOW()),
       ('qianqi', '13800138005', 'qianqi@example.com', '钱七', 0.00, 299.00, 15, false, true, 'NORMAL', NOW(), NOW());

-- 初始化黑名单数据
INSERT INTO user_blacklist (user_id, username, phone, reason, ban_type, operator, create_time)
VALUES (5, 'qianqi', '13800138005', '恶意破坏车辆，造成严重损失', 'PERMANENT', 'admin', NOW());

-- 初始化车辆数据
INSERT INTO bikes (bike_no, status, bike_type, area_id, location, battery, total_rides, total_distance, original_cost, depreciation_months, purchase_date, create_time, update_time)
VALUES ('BK00001', 'ONLINE', 'STANDARD', 1, '朝阳公园东门', 85, 156, 520.50, 1500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00002', 'ONLINE', 'ELECTRIC', 1, '国贸地铁站', 92, 203, 890.20, 3500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00003', 'OFFLINE', 'STANDARD', 2, '中关村大街', 0, 178, 650.00, 1500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00004', 'FAULT', 'ELECTRIC', 2, '五道口地铁站', 45, 245, 1020.80, 3500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00005', 'MAINTENANCE', 'STANDARD', 3, '王府井步行街', 60, 89, 320.30, 1500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00006', 'ONLINE', 'ELECTRIC', 4, '西单商场', 78, 312, 1250.60, 3500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00007', 'ONLINE', 'STANDARD', 5, '丰台科技园', 95, 134, 480.90, 1500.00, 36, '2024-01-15', NOW(), NOW()),
       ('BK00008', 'FAULT', 'ELECTRIC', 1, '三里屯太古里', 30, 198, 780.40, 3500.00, 36, '2024-01-15', NOW(), NOW());

-- 初始化骑行记录
INSERT INTO ride_records (order_no, user_id, username, bike_id, bike_no, start_area_id, start_area_name, end_area_id, end_area_name, start_location, end_location, start_time, end_time, duration, distance, amount, status, create_time, update_time)
VALUES ('ORD20240520001', 1, 'zhangsan', 1, 'BK00001', 1, '朝阳区', 1, '朝阳区', '朝阳公园东门', '国贸地铁站', '2024-05-20 08:30:00', '2024-05-20 08:55:00', 25, 3.2, 5.00, 'COMPLETED', NOW(), NOW()),
       ('ORD20240520002', 2, 'lisi', 2, 'BK00002', 1, '朝阳区', 2, '海淀区', '国贸地铁站', '中关村大街', '2024-05-20 09:15:00', '2024-05-20 10:00:00', 45, 8.5, 12.00, 'COMPLETED', NOW(), NOW()),
       ('ORD20240520003', 4, 'zhaoliu', 6, 'BK00006', 4, '西城区', 4, '西城区', '西单商场', '金融街', '2024-05-20 10:30:00', '2024-05-20 10:50:00', 20, 2.1, 3.50, 'COMPLETED', NOW(), NOW()),
       ('ORD20240520004', 1, 'zhangsan', 7, 'BK00007', 5, '丰台区', 5, '丰台区', '丰台科技园', '北京西站', '2024-05-20 14:00:00', '2024-05-20 14:25:00', 25, 4.2, 6.00, 'COMPLETED', NOW(), NOW()),
       ('ORD20240520005', 3, 'wangwu', 1, 'BK00001', 1, '朝阳区', NULL, NULL, '国贸地铁站', NULL, '2024-05-20 16:30:00', NULL, NULL, NULL, 0.00, 'IN_PROGRESS', NOW(), NOW());

-- 初始化客服工单
INSERT INTO customer_tickets (user_id, username, ticket_no, title, content, type, refund_amount, status, reply, handler, handle_time, create_time, update_time)
VALUES (2, 'lisi', 'TK20240520001', '骑行计费异常申诉', '昨日骑行显示计费20元，但实际只骑了10分钟，要求退款', 'REFUND', 15.00, 'PENDING', NULL, NULL, NULL, NOW(), NOW()),
       (1, 'zhangsan', 'TK20240520002', '月卡赠送问题', '我是高频用户，为什么没有赠送月卡？', 'COMPLAINT', NULL, 'PROCESSING', '正在核实您的骑行记录，请稍候', '客服小王', NOW(), NOW(), NOW()),
       (4, 'zhaoliu', 'TK20240520003', '车辆故障投诉', '骑行中车辆突然断电，差点造成事故', 'COMPLAINT', NULL, 'RESOLVED', '非常抱歉给您带来不便，已安排维修人员处理，赠送您一张5元骑行券', '客服小李', NOW(), NOW(), NOW());

-- 初始化支付记录
INSERT INTO payment_records (payment_no, order_no, user_id, username, amount, payment_type, payment_method, third_party_no, status, remark, success_time, create_time)
VALUES ('PAY20240520001', 'ORD20240520001', 1, 'zhangsan', 5.00, 'RIDE', 'ALIPAY', 'ALI202405200001', 'SUCCESS', '骑行支付', '2024-05-20 08:55:00', NOW()),
       ('PAY20240520002', 'ORD20240520002', 2, 'lisi', 12.00, 'RIDE', 'WECHAT', 'WX202405200001', 'SUCCESS', '骑行支付', '2024-05-20 10:00:00', NOW()),
       ('PAY20240520003', 'ORD20240520003', 4, 'zhaoliu', 3.50, 'RIDE', 'ALIPAY', 'ALI202405200002', 'SUCCESS', '骑行支付', '2024-05-20 10:50:00', NOW()),
       ('PAY20240520004', NULL, 1, 'zhangsan', 299.00, 'DEPOSIT', 'ALIPAY', 'ALI202405200003', 'SUCCESS', '押金充值', '2024-05-01 10:00:00', NOW()),
       ('PAY20240520005', NULL, 2, 'lisi', 100.00, 'RECHARGE', 'WECHAT', 'WX202405200002', 'SUCCESS', '余额充值', '2024-05-15 14:30:00', NOW());

-- 初始化会员记录
INSERT INTO user_memberships (user_id, username, order_no, plan_type, days, amount, source, remark, start_time, end_time, create_time)
VALUES (1, 'zhangsan', 'VIP20240501001', 'MONTHLY', 30, 25.00, 'PURCHASED', '用户自行购买月卡', '2024-05-01 00:00:00', '2024-05-31 23:59:59', NOW()),
       (4, 'zhaoliu', 'VIP20240510001', 'MONTHLY', 30, 0.00, 'GIFTED', '高频用户赠送月卡', '2024-05-10 00:00:00', '2024-06-09 23:59:59', NOW());

-- 初始化发票记录
INSERT INTO invoices (invoice_no, user_id, username, company_name, tax_no, company_address, company_phone, bank_name, bank_account, invoice_amount, tax_amount, total_amount, invoice_type, status, remark, issue_time, create_time)
VALUES ('INV202405001', 1, 'zhangsan', '北京科技有限公司', '91110000MA001ABCDE', '北京市朝阳区科技园区88号', '010-12345678', '中国工商银行北京分行', '0200000012345678901', 890.00, 53.40, 943.40, 'SPECIAL', 'ISSUED', '企业月度骑行发票', '2024-05-15 10:30:00', NOW()),
       ('INV202405002', 4, 'zhaoliu', '北京创新科技公司', '91110000MA002FGHIJ', '北京市海淀区创新大厦100号', '010-87654321', '中国建设银行北京分行', '1100000023456789012', 560.00, 33.60, 593.60, 'NORMAL', 'ISSUED', '企业季度发票', '2024-05-18 14:00:00', NOW());

-- 初始化成本记录
INSERT INTO cost_records (record_no, cost_type, cost_category, bike_id, bike_no, area_id, area_name, amount, description, cost_date, operator, create_time)
VALUES ('COST202405001', 'DEPRECIATION', 'BIKE_DEPRECIATION', 1, 'BK00001', 1, '朝阳区', 41.67, '5月份车辆折旧费用', '2024-05-01', 'system', NOW()),
       ('COST202405002', 'MAINTENANCE', 'REPAIR', 4, 'BK00004', 2, '海淀区', 150.00, '电机故障维修', '2024-05-10', '张师傅', NOW()),
       ('COST202405003', 'OPERATION', 'CHARGING', NULL, NULL, 1, '朝阳区', 200.00, '5月上旬充电费用', '2024-05-10', '李运维', NOW()),
       ('COST202405004', 'OPERATION', 'SALARY', NULL, NULL, NULL, NULL, 8000.00, '运维人员5月工资', '2024-05-15', '财务小张', NOW()),
       ('COST202405005', 'MAINTENANCE', 'REPAIR', 8, 'BK00008', 1, '朝阳区', 80.00, '电池更换', '2024-05-18', '王师傅', NOW());

-- 初始化对账记录
INSERT INTO reconciliations (recon_no, recon_date, recon_type, system_amount, actual_amount, diff_amount, status, remark, operator, handle_time, create_time)
VALUES ('RECON20240519', '2024-05-19', 'DAILY_PAYMENT', 5280.50, 5280.50, 0.00, 'MATCHED', '对账成功，金额一致', '财务小张', '2024-05-20 09:00:00', NOW()),
       ('RECON20240518', '2024-05-18', 'DAILY_PAYMENT', 4890.00, 4885.00, 5.00, 'UNMATCHED', '存在5元差异，待核实', '财务小张', '2024-05-19 09:00:00', NOW());
