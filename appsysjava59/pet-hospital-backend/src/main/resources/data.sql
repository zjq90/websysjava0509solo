-- 初始化测试数据

-- 用户表（医生/管理员）
INSERT INTO users (username, password, real_name, phone, email, role, avatar, status, create_time) VALUES
('doctor1', '123456', '张医生', '13800138001', 'zhang@hospital.com', 'DOCTOR', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20male%20veterinarian%20portrait&image_size=square', 'ACTIVE', CURRENT_TIMESTAMP),
('doctor2', '123456', '李医生', '13800138002', 'li@hospital.com', 'DOCTOR', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20female%20veterinarian%20portrait&image_size=square', 'ACTIVE', CURRENT_TIMESTAMP),
('admin', 'admin123', '王主任', '13800138003', 'admin@hospital.com', 'ADMIN', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hospital%20director%20portrait&image_size=square', 'ACTIVE', CURRENT_TIMESTAMP);

-- 宠物主人表
INSERT INTO pet_owner (name, phone, email, address, create_time) VALUES
('王小明', '13900139001', 'wang@email.com', '北京市朝阳区', CURRENT_TIMESTAMP),
('李小红', '13900139002', 'li@email.com', '北京市海淀区', CURRENT_TIMESTAMP),
('张大伟', '13900139003', 'zhang@email.com', '北京市西城区', CURRENT_TIMESTAMP);

-- 宠物表
INSERT INTO pet (owner_id, name, species, breed, gender, age, weight, color, avatar, allergy_history, status, create_time) VALUES
(1, '咪咪', 'CAT', '英短', 'FEMALE', 2, 3.5, '灰色', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20british%20shorthair%20cat&image_size=square', '对青霉素过敏', 'HEALTHY', CURRENT_TIMESTAMP),
(1, '旺财', 'DOG', '金毛', 'MALE', 3, 25.0, '金色', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=golden%20retriever%20dog&image_size=square', NULL, 'HEALTHY', CURRENT_TIMESTAMP),
(2, '球球', 'CAT', '布偶', 'MALE', 1, 4.2, '白色', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ragdoll%20cat%20white&image_size=square', '海鲜过敏', 'CHRONIC', CURRENT_TIMESTAMP),
(3, '豆豆', 'DOG', '泰迪', 'FEMALE', 5, 5.0, '棕色', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=brown%20teddy%20dog&image_size=square', NULL, 'POST_OPERATION', CURRENT_TIMESTAMP);

-- 宠物标签表
INSERT INTO pet_tag (pet_id, tag_name, tag_color, create_time) VALUES
(3, '慢性病', '#ff4444', CURRENT_TIMESTAMP),
(4, '术后', '#ffaa00', CURRENT_TIMESTAMP),
(1, '易紧张', '#9933cc', CURRENT_TIMESTAMP),
(2, '需轻柔操作', '#33b5e5', CURRENT_TIMESTAMP);

-- 疫苗记录表
INSERT INTO vaccine_record (pet_id, vaccine_name, vaccine_date, next_date, hospital, doctor_name, notes, create_time) VALUES
(1, '猫三联', '2025-01-15', '2026-01-15', '爱心宠物医院', '张医生', '接种正常', CURRENT_TIMESTAMP),
(1, '狂犬疫苗', '2025-01-15', '2026-01-15', '爱心宠物医院', '张医生', '接种正常', CURRENT_TIMESTAMP),
(2, '六联疫苗', '2025-02-20', '2026-02-20', '爱心宠物医院', '李医生', '接种正常', CURRENT_TIMESTAMP);

-- 药品表
INSERT INTO medicine (name, category, specification, manufacturer, price, stock, unit, description, create_time) VALUES
('福来恩滴剂', '驱虫药', '0.67ml/支', '梅里亚', 85.00, 50, '支', '体外驱虫药，用于防治跳蚤、蜱虫', CURRENT_TIMESTAMP),
('大宠爱', '驱虫药', '0.75ml/支', '硕腾', 120.00, 30, '支', '体内外同驱，安全有效', CURRENT_TIMESTAMP),
('驱球虫片', '驱虫药', '50mg/片', '拜耳', 35.00, 100, '片', '用于治疗球虫感染', CURRENT_TIMESTAMP),
('阿莫西林', '抗生素', '0.25g/粒', '华北制药', 15.00, 200, '粒', '广谱抗生素', CURRENT_TIMESTAMP),
('头孢氨苄', '抗生素', '0.5g/粒', '石药集团', 25.00, 150, '粒', '第一代头孢菌素', CURRENT_TIMESTAMP),
('益生菌', '肠胃药', '5g/袋', '麦德氏', 45.00, 80, '袋', '调节肠道菌群', CURRENT_TIMESTAMP),
('化毛膏', '保健品', '120g/支', '红狗', 68.00, 60, '支', '帮助排出毛球', CURRENT_TIMESTAMP);

-- 问诊表
INSERT INTO consultation (pet_id, owner_id, doctor_id, title, symptom, emergency_level, status, consultation_type, start_time, create_time) VALUES
(1, 1, 1, '呼吸困难', '猫咪出现呼吸急促，张口呼吸', 'URGENT', 'PENDING', 'VIDEO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 1, '食欲不振', '狗狗最近两天不爱吃饭', 'NORMAL', 'PENDING', 'TEXT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 2, '皮肤瘙痒', '猫咪频繁抓挠，皮肤发红', 'NORMAL', 'IN_PROGRESS', 'TEXT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 3, 2, '术后复查', '泰迪术后一周复查', 'NORMAL', 'COMPLETED', 'VIDEO', '2025-05-10 10:00:00', CURRENT_TIMESTAMP);

-- 问诊消息表
INSERT INTO consultation_message (consultation_id, sender_type, sender_id, content, message_type, is_read, create_time) VALUES
(3, 'OWNER', 2, '医生你好，我家猫咪最近总是抓耳朵', 'TEXT', true, CURRENT_TIMESTAMP),
(3, 'DOCTOR', 2, '您好，请问这种情况持续多久了？有没有发现皮肤发红或者掉毛？', 'TEXT', true, CURRENT_TIMESTAMP),
(3, 'OWNER', 2, '大概有3天了，耳朵附近有点红，还有一些皮屑', 'TEXT', true, CURRENT_TIMESTAMP),
(3, 'DOCTOR', 2, '好的，根据描述可能是耳螨或者真菌感染，建议您带它来做个检查', 'TEXT', false, CURRENT_TIMESTAMP);

-- 处方表
INSERT INTO prescription (consultation_id, doctor_id, pet_id, owner_id, prescription_no, total_amount, status, notes, create_time) VALUES
(4, 2, 4, 3, 'P20250510001', 185.00, 'DISPENSED', '术后恢复用药', '2025-05-10 10:30:00');

-- 处方明细表
INSERT INTO prescription_item (prescription_id, medicine_id, medicine_name, specification, quantity, unit, price, amount, dosage, frequency, duration, notes) VALUES
(1, 4, '阿莫西林', '0.25g/粒', 14, '粒', 15.00, 210.00, '1粒/次', '每日2次', '7天', '饭后服用'),
(1, 6, '益生菌', '5g/袋', 7, '袋', 45.00, 315.00, '1袋/次', '每日1次', '7天', '温水冲服');

-- 排班表
INSERT INTO schedule (doctor_id, schedule_date, shift_type, status, create_time) VALUES
(1, CURRENT_DATE, 'MORNING', 'CONFIRMED', CURRENT_TIMESTAMP),
(1, CURRENT_DATE, 'AFTERNOON', 'CONFIRMED', CURRENT_TIMESTAMP),
(2, CURRENT_DATE, 'NIGHT', 'CONFIRMED', CURRENT_TIMESTAMP),
(1, DATEADD('DAY', 1, CURRENT_DATE), 'MORNING', 'CONFIRMED', CURRENT_TIMESTAMP),
(2, DATEADD('DAY', 1, CURRENT_DATE), 'AFTERNOON', 'CONFIRMED', CURRENT_TIMESTAMP),
(2, DATEADD('DAY', 1, CURRENT_DATE), 'NIGHT', 'CONFIRMED', CURRENT_TIMESTAMP);

-- 调班申请表
INSERT INTO schedule_change_request (doctor_id, original_schedule_id, request_type, target_date, target_shift, reason, status, approver_id, create_time) VALUES
(1, NULL, 'SWAP', DATEADD('DAY', 3, CURRENT_DATE), 'MORNING', '家中有事需要调班', 'PENDING', NULL, CURRENT_TIMESTAMP);

-- 休息日表
INSERT INTO holiday (doctor_id, holiday_date, holiday_type, reason, status, create_time) VALUES
(1, DATEADD('DAY', 7, CURRENT_DATE), 'PERSONAL', '年假', 'APPROVED', CURRENT_TIMESTAMP);
