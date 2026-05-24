-- ============================================
-- 共享单车系统测试数据 (H2数据库兼容版)
-- ============================================

-- 测试用户数据
INSERT INTO "user" (phone, password, nickname, avatar, gender, real_name, id_card, id_card_verified, face_verified, real_name_verified, deposit_status, deposit_amount, balance, credit_score, credit_level, status) VALUES
('13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '骑行达人小明', 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoming', 1, '张三', '110101199001011234', 1, 1, 1, 1, 199.00, 99.50, 95, 'EXCELLENT', 1),
('13800138002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '快乐骑行者', 'https://api.dicebear.com/7.x/avataaars/svg?seed=happy', 2, '李四', '110101199202022345', 1, 1, 1, 2, 0, 50.00, 100, 'EXCELLENT', 1),
('13800138003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '新手用户', 'https://api.dicebear.com/7.x/avataaars/svg?seed=newbie', 0, NULL, NULL, 0, 0, 0, 0, 0, 0, 100, 'NORMAL', 1),
('13800138004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '城市漫步者', 'https://api.dicebear.com/7.x/avataaars/svg?seed=walker', 1, '王五', '110101198803033456', 1, 1, 1, 1, 199.00, 25.80, 75, 'WARNING', 1),
('13800138005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '环保先锋', 'https://api.dicebear.com/7.x/avataaars/svg?seed=eco', 2, '赵六', '110101199504044567', 1, 0, 0, 1, 199.00, 200.00, 85, 'NORMAL', 1);

-- 测试车辆数据 (模拟北京市区车辆分布)
INSERT INTO bike (bike_no, bike_type, qr_code, bluetooth_mac, battery_level, range_km, latitude, longitude, location_address, status) VALUES
('B00001', 'STANDARD', 'bike://B00001', 'AA:BB:CC:00:00:01', NULL, NULL, 39.904200, 116.407400, '北京市东城区天安门广场附近', 'AVAILABLE'),
('B00002', 'STANDARD', 'bike://B00002', 'AA:BB:CC:00:00:02', NULL, NULL, 39.914200, 116.417400, '北京市东城区王府井大街', 'AVAILABLE'),
('B00003', 'STANDARD', 'bike://B00003', 'AA:BB:CC:00:00:03', NULL, NULL, 39.924200, 116.427400, '北京市东城区东四路口', 'AVAILABLE'),
('B00004', 'STANDARD', 'bike://B00004', 'AA:BB:CC:00:00:04', NULL, NULL, 39.934200, 116.437400, '北京市东城区东直门', 'MAINTENANCE'),
('B00005', 'STANDARD', 'bike://B00005', 'AA:BB:CC:00:00:05', NULL, NULL, 39.908823, 116.397470, '北京市东城区故宫博物院', 'AVAILABLE'),
('E00001', 'ELECTRIC', 'bike://E00001', 'AA:BB:CC:00:01:01', 85, 45.0, 39.906217, 116.391276, '北京市西城区西单商场', 'AVAILABLE'),
('E00002', 'ELECTRIC', 'bike://E00002', 'AA:BB:CC:00:01:02', 60, 30.0, 39.916217, 116.401276, '北京市西城区西四路口', 'AVAILABLE'),
('E00003', 'ELECTRIC', 'bike://E00003', 'AA:BB:CC:00:01:03', 95, 50.0, 39.926217, 116.411276, '北京市西城区新街口', 'AVAILABLE'),
('E00004', 'ELECTRIC', 'bike://E00004', 'AA:BB:CC:00:01:04', 25, 12.0, 39.936217, 116.421276, '北京市西城区德胜门', 'FAULTY'),
('E00005', 'ELECTRIC', 'bike://E00005', 'AA:BB:CC:00:01:05', 100, 55.0, 39.946217, 116.431276, '北京市西城区积水潭', 'AVAILABLE'),
('A00001', 'ASSIST', 'bike://A00001', 'AA:BB:CC:00:02:01', 90, 40.0, 39.982314, 116.316453, '北京市海淀区中关村', 'AVAILABLE'),
('A00002', 'ASSIST', 'bike://A00002', 'AA:BB:CC:00:02:02', 75, 35.0, 39.992314, 116.326453, '北京市海淀区北京大学', 'AVAILABLE'),
('A00003', 'ASSIST', 'bike://A00003', 'AA:BB:CC:00:02:03', 55, 25.0, 40.002314, 116.336453, '北京市海淀区清华大学', 'RESERVED'),
('A00004', 'ASSIST', 'bike://A00004', 'AA:BB:CC:00:02:04', 80, 38.0, 40.012314, 116.346453, '北京市海淀区五道口', 'AVAILABLE'),
('A00005', 'ASSIST', 'bike://A00005', 'AA:BB:CC:00:02:05', 40, 18.0, 40.022314, 116.356453, '北京市海淀区西二旗', 'AVAILABLE'),
('B00006', 'STANDARD', 'bike://B00006', 'AA:BB:CC:00:00:06', NULL, NULL, 39.929833, 116.453750, '北京市朝阳区国贸', 'AVAILABLE'),
('B00007', 'STANDARD', 'bike://B00007', 'AA:BB:CC:00:00:07', NULL, NULL, 39.939833, 116.463750, '北京市朝阳区大望路', 'IN_USE'),
('E00006', 'ELECTRIC', 'bike://E00006', 'AA:BB:CC:00:01:06', 70, 35.0, 39.949833, 116.473750, '北京市朝阳区四惠', 'AVAILABLE'),
('A00006', 'ASSIST', 'bike://A00006', 'AA:BB:CC:00:02:06', 88, 42.0, 39.959833, 116.483750, '北京市朝阳区青年路', 'AVAILABLE');

-- 测试骑行记录
INSERT INTO ride_record (user_id, bike_id, bike_no, start_time, end_time, start_latitude, start_longitude, start_address, end_latitude, end_longitude, end_address, duration_minutes, distance_km, base_price, time_price, peak_surcharge, total_amount, actual_amount, payment_status, payment_method, payment_time, is_peak_hour, unlock_type, status) VALUES
(1, 1, 'B00001', TIMESTAMP '2024-01-15 08:30:00', TIMESTAMP '2024-01-15 08:45:00', 39.904200, 116.407400, '天安门广场', 39.914200, 116.417400, '王府井大街', 15, 1.8, 1.50, 0.65, 0.75, 2.90, 2.90, 'PAID', 'BALANCE', TIMESTAMP '2024-01-15 08:45:00', 1, 'QRCODE', 'COMPLETED'),
(1, 6, 'E00001', TIMESTAMP '2024-01-15 18:20:00', TIMESTAMP '2024-01-15 18:40:00', 39.906217, 116.391276, '西单商场', 39.926217, 116.411276, '新街口', 20, 3.5, 2.00, 0.90, 1.45, 4.35, 4.35, 'PAID', 'ALIPAY', TIMESTAMP '2024-01-15 18:40:00', 1, 'QRCODE', 'COMPLETED'),
(2, 11, 'A00001', TIMESTAMP '2024-01-16 10:00:00', TIMESTAMP '2024-01-16 10:25:00', 39.982314, 116.316453, '中关村', 40.002314, 116.336453, '清华大学', 25, 4.2, 2.50, 1.15, 0, 3.65, 3.65, 'PAID', 'WECHAT', TIMESTAMP '2024-01-16 10:25:00', 0, 'BLUETOOTH', 'COMPLETED'),
(2, 2, 'B00002', TIMESTAMP '2024-01-17 07:45:00', TIMESTAMP '2024-01-17 08:10:00', 39.914200, 116.417400, '王府井大街', 39.908823, 116.397470, '故宫博物院', 25, 2.1, 1.50, 1.15, 1.33, 3.98, 3.98, 'PAID', 'BALANCE', TIMESTAMP '2024-01-17 08:10:00', 1, 'QRCODE', 'COMPLETED'),
(5, 7, 'E00002', TIMESTAMP '2024-01-18 14:30:00', NULL, 39.916217, 116.401276, '西四路口', NULL, NULL, NULL, 0, 0, 0, 0, 0, 0, 0, 'UNPAID', NULL, NULL, 0, 'QRCODE', 'ONGOING');

-- 测试预约记录
INSERT INTO reservation (user_id, bike_id, bike_no, reserve_time, expire_time, status) VALUES
(1, 13, 'A00003', TIMESTAMP '2024-01-18 15:00:00', TIMESTAMP '2024-01-18 15:15:00', 'ACTIVE'),
(2, 3, 'B00003', TIMESTAMP '2024-01-15 09:00:00', TIMESTAMP '2024-01-15 09:15:00', 'USED'),
(3, 7, 'E00002', TIMESTAMP '2024-01-10 10:00:00', TIMESTAMP '2024-01-10 10:15:00', 'EXPIRED');

-- 测试故障上报记录
INSERT INTO fault_report (user_id, bike_id, bike_no, fault_type, fault_description, latitude, longitude, status, reward_amount) VALUES
(1, 4, 'B00004', 'CHAIN', '车辆链条脱落，无法正常骑行', 39.934200, 116.437400, 'RESOLVED', 2.00),
(2, 9, 'E00004', 'BRAKE', '刹车失灵，存在安全隐患', 39.936217, 116.421276, 'PROCESSING', 0),
(5, 15, 'A00005', 'TIRE', '后轮胎漏气，需要打气或更换', 40.022314, 116.356453, 'PENDING', 0);

-- 测试信用分记录
INSERT INTO credit_record (user_id, change_type, score_change, reason, related_type, before_score, after_score) VALUES
(1, 'REWARD', 5, '上报车辆故障，感谢您的反馈', 'FAULT', 90, 95),
(4, 'DEDUCT', -20, '车辆乱停放，影响交通秩序', 'VIOLATION', 95, 75),
(1, 'REWARD', 3, '规范停车，文明骑行', 'RIDE', 92, 95),
(2, 'REWARD', 2, '首次完成实名认证', 'REGISTER', 98, 100),
(4, 'DEDUCT', -5, '预约车辆未使用且未取消', 'RESERVATION', 80, 75);

-- 测试优惠券数据
INSERT INTO coupon (user_id, coupon_name, coupon_type, discount_value, min_amount, issue_time, expire_time, status, source) VALUES
(1, '新用户专享5折券', 'DISCOUNT', 0.50, 1.00, TIMESTAMP '2024-01-01 00:00:00', TIMESTAMP '2024-02-01 00:00:00', 'UNUSED', 'REGISTER'),
(1, '故障上报奖励券', 'FIXED', 2.00, 0, TIMESTAMP '2024-01-15 09:00:00', TIMESTAMP '2024-02-15 00:00:00', 'UNUSED', 'FAULT_REPORT'),
(2, '周末免费骑行券', 'FREE', 30.00, 0, TIMESTAMP '2024-01-13 00:00:00', TIMESTAMP '2024-01-21 00:00:00', 'USED', 'ACTIVITY'),
(3, '新手礼包-1元立减', 'FIXED', 1.00, 1.00, TIMESTAMP '2024-01-10 00:00:00', TIMESTAMP '2024-02-10 00:00:00', 'UNUSED', 'REGISTER'),
(5, '满5减2优惠券', 'FIXED', 2.00, 5.00, TIMESTAMP '2024-01-05 00:00:00', TIMESTAMP '2024-02-05 00:00:00', 'UNUSED', 'ACTIVITY');

-- 测试押金记录
INSERT INTO deposit_record (user_id, record_type, amount, payment_method, status) VALUES
(1, 'PAY', 199.00, 'WECHAT', 'SUCCESS'),
(2, 'EXEMPT', 0, NULL, 'SUCCESS'),
(4, 'PAY', 199.00, 'ALIPAY', 'SUCCESS'),
(5, 'PAY', 199.00, 'BALANCE', 'SUCCESS');

-- 系统配置数据
INSERT INTO sys_config (config_key, config_value, config_name, config_desc) VALUES
('deposit.amount', '199.00', '押金金额', '用户需要缴纳的押金金额'),
('deposit.free_credit_score', '650', '免押金信用分', '芝麻信用分达到此分数可免押金'),
('reservation.hold_minutes', '15', '预约保留时间', '预约车辆保留的分钟数'),
('pricing.base_price', '1.50', '基础费用', '骑行起步价'),
('pricing.per_minute_price', '0.05', '每分钟价格', '超出基础时长后的每分钟价格'),
('pricing.peak_multiplier', '1.5', '高峰时段倍率', '高峰时段费用倍率'),
('pricing.free_minutes', '2', '免费时长', '开始骑行后的免费分钟数'),
('credit.initial_score', '100', '初始信用分', '新用户初始信用分数'),
('credit.warning_threshold', '80', '信用警告阈值', '低于此分数进入警告状态'),
('credit.excellent_threshold', '90', '信用优秀阈值', '高于此分数享受优惠');
