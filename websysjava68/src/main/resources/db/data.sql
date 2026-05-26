-- =============================================
-- 个人记账系统 - 测试数据
-- =============================================

-- 账户测试数据
INSERT INTO account (account_name, account_type, account_number, balance, currency, icon, remark, is_default, sort_order) VALUES
('现金', 'CASH', NULL, 1500.00, 'CNY', '💵', '日常零用现金', 1, 1),
('招商银行储蓄卡', 'BANK_CARD', '6225****1234', 35800.50, 'CNY', '🏦', '主要工资账户', 0, 2),
('工商银行信用卡', 'CREDIT_CARD', '6222****5678', 5000.00, 'CNY', '💳', '日常消费信用卡', 0, 3),
('支付宝', 'ALIPAY', 'user@alipay.com', 8500.00, 'CNY', '📱', '支付宝余额', 0, 4),
('微信钱包', 'WECHAT', 'wechat_user', 3200.00, 'CNY', '💬', '微信支付余额', 0, 5);

-- 收入分类测试数据
INSERT INTO category (category_name, category_type, parent_id, icon, sort_order) VALUES
('工资', 'INCOME', NULL, '💰', 1),
('奖金', 'INCOME', NULL, '🎁', 2),
('投资收益', 'INCOME', NULL, '📈', 3),
('兼职收入', 'INCOME', NULL, '💼', 4),
('其他收入', 'INCOME', NULL, '💵', 5);

-- 支出分类测试数据
INSERT INTO category (category_name, category_type, parent_id, icon, sort_order) VALUES
('餐饮', 'EXPENSE', NULL, '🍜', 1),
('交通', 'EXPENSE', NULL, '🚌', 2),
('购物', 'EXPENSE', NULL, '🛍️', 3),
('娱乐', 'EXPENSE', NULL, '🎮', 4),
('房贷/租金', 'EXPENSE', NULL, '🏠', 5),
('水电燃气', 'EXPENSE', NULL, '💡', 6),
('医疗', 'EXPENSE', NULL, '💊', 7),
('教育', 'EXPENSE', NULL, '📚', 8),
('通讯', 'EXPENSE', NULL, '📞', 9),
('其他支出', 'EXPENSE', NULL, '💸', 10);

-- 标签测试数据
INSERT INTO tag (tag_name, tag_color, tag_type, icon, sort_order) VALUES
('旅行', '#FF6B6B', 'CUSTOM', '✈️', 1),
('生日礼物', '#4ECDC4', 'CUSTOM', '🎂', 2),
('医疗', '#45B7D1', 'CUSTOM', '💊', 3),
('工作', '#96CEB4', 'CUSTOM', '💼', 4),
('家庭', '#FFEAA7', 'CUSTOM', '👨‍👩‍👧', 5),
('学习', '#DDA0DD', 'CUSTOM', '📖', 6),
('健身', '#98D8C8', 'CUSTOM', '💪', 7),
('聚会', '#F7DC6F', 'CUSTOM', '🎉', 8);

-- 交易记录测试数据（收入）
INSERT INTO transaction (transaction_type, amount, account_id, category_id, transaction_time, description, merchant) VALUES
('INCOME', 15000.00, 2, 6, '2024-01-05 10:00:00', '2024年1月工资', 'XX科技公司'),
('INCOME', 3000.00, 2, 7, '2024-01-15 14:30:00', '季度奖金', 'XX科技公司'),
('INCOME', 2500.00, 4, 9, '2024-01-20 09:00:00', '周末兼职收入', '自由职业平台'),
('INCOME', 800.00, 2, 8, '2024-01-25 16:00:00', '股票收益', '证券交易所'),
('INCOME', 12000.00, 2, 6, '2024-02-05 10:00:00', '2024年2月工资', 'XX科技公司');

-- 交易记录测试数据（支出）
INSERT INTO transaction (transaction_type, amount, account_id, category_id, transaction_time, description, merchant) VALUES
('EXPENSE', 35.50, 1, 11, '2024-01-10 08:30:00', '早餐', '麦当劳'),
('EXPENSE', 128.00, 5, 11, '2024-01-10 12:00:00', '午餐', '外婆家'),
('EXPENSE', 25.00, 3, 12, '2024-01-10 18:00:00', '地铁充值', '地铁站'),
('EXPENSE', 299.00, 4, 13, '2024-01-11 15:30:00', '超市购物', '大润发超市'),
('EXPENSE', 45.00, 5, 11, '2024-01-12 19:00:00', '晚餐外卖', '美团外卖'),
('EXPENSE', 5000.00, 2, 15, '2024-01-15 09:00:00', '1月房租', '房东'),
('EXPENSE', 180.00, 2, 16, '2024-01-16 10:00:00', '电费', '国家电网'),
('EXPENSE', 80.00, 2, 16, '2024-01-16 10:00:00', '水费', '自来水公司'),
('EXPENSE', 68.00, 3, 14, '2024-01-17 20:00:00', '电影票', '万达影城'),
('EXPENSE', 320.00, 4, 17, '2024-01-18 14:00:00', '感冒药', '大药房'),
('EXPENSE', 128.00, 5, 11, '2024-01-19 12:30:00', '聚餐', '海底捞'),
('EXPENSE', 298.00, 4, 18, '2024-01-20 10:00:00', '购买书籍', '当当网'),
('EXPENSE', 100.00, 2, 19, '2024-01-21 09:00:00', '手机话费充值', '中国移动'),
('EXPENSE', 55.00, 1, 12, '2024-01-22 08:00:00', '打车费', '滴滴出行'),
('EXPENSE', 199.00, 3, 13, '2024-01-23 16:00:00', '买衣服', '淘宝'),
('EXPENSE', 88.00, 5, 11, '2024-01-24 19:30:00', '晚餐', '肯德基'),
('EXPENSE', 200.00, 4, 14, '2024-01-25 14:00:00', 'KTV唱歌', '唱吧麦颂'),
('EXPENSE', 150.00, 2, 17, '2024-01-26 10:00:00', '牙科检查', '口腔医院'),
('EXPENSE', 45.00, 1, 12, '2024-01-27 07:30:00', '公交充值', '公交公司'),
('EXPENSE', 3000.00, 2, 13, '2024-01-28 15:00:00', '购买电子产品', '京东'),
('EXPENSE', 65.00, 5, 11, '2024-01-29 12:00:00', '午餐', '真功夫'),
('EXPENSE', 1500.00, 4, 1, '2024-01-30 10:00:00', '春节礼物', '网购'),
('EXPENSE', 200.00, 2, 14, '2024-01-31 19:00:00', '游乐场门票', '欢乐谷');

-- 交易标签关联测试数据
INSERT INTO transaction_tag (transaction_id, tag_id) VALUES
(6, 8),
(11, 8),
(14, 3),
(21, 3),
(13, 2),
(23, 2),
(10, 4),
(16, 6),
(17, 7),
(22, 5);

-- 转账记录测试数据
INSERT INTO transfer_record (from_account_id, to_account_id, amount, transfer_fee, transfer_time, description) VALUES
(2, 1, 1000.00, 0.00, '2024-01-05 11:00:00', '提取现金'),
(4, 2, 5000.00, 0.00, '2024-01-10 09:30:00', '支付宝提现到银行卡'),
(2, 3, 3000.00, 0.00, '2024-01-15 14:00:00', '银行卡还款信用卡'),
(5, 4, 800.00, 0.00, '2024-01-20 16:00:00', '微信零钱转入支付宝'),
(2, 5, 2000.00, 0.00, '2024-01-25 10:00:00', '银行卡充值微信钱包');

-- 分类规则测试数据
INSERT INTO category_rule (rule_name, match_field, match_type, match_value, target_category_id, priority, is_enabled) VALUES
('超市购物规则', 'MERCHANT', 'CONTAINS', '超市', 13, 10, 1),
('外卖规则', 'DESCRIPTION', 'CONTAINS', '外卖', 11, 10, 1),
('地铁规则', 'MERCHANT', 'CONTAINS', '地铁', 12, 10, 1),
('打车规则', 'MERCHANT', 'CONTAINS', '滴滴', 12, 10, 1),
('电影规则', 'DESCRIPTION', 'CONTAINS', '电影', 14, 10, 1),
('网购规则', 'MERCHANT', 'REGEX', '淘宝|京东|当当|拼多多|天猫', 13, 5, 1),
('工资规则', 'DESCRIPTION', 'CONTAINS', '工资', 6, 20, 1),
('医疗规则', 'MERCHANT', 'CONTAINS', '医院|药房|诊所', 17, 10, 1),
('话费规则', 'DESCRIPTION', 'CONTAINS', '话费', 19, 10, 1);
