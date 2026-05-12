-- 初始化系统角色数据
INSERT INTO sys_role (id, role_code, role_name, description, status, create_time, update_time) VALUES
(1, 'ADMIN', '系统管理员', '拥有系统所有权限', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'HOSPITAL_MANAGER', '医院管理者', '负责医院整体运营管理', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'DOCTOR', '临床医生', '负责患者诊断和治疗', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'NURSE', '护士', '负责患者护理工作', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'TECHNICIAN', '医技人员', '负责检验检查工作', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'CASHIER', '收费人员', '负责收费结算工作', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'ADMIN_STAFF', '行政后勤人员', '负责行政后勤工作', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化系统字典数据
INSERT INTO sys_dict (id, dict_type, dict_type_name, dict_code, dict_name, dict_value, sort_order, remark, status, create_time, update_time) VALUES
-- 科室字典
(1, 'DEPARTMENT', '科室字典', '001', '内科', '1', 1, '内科科室', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'DEPARTMENT', '科室字典', '002', '外科', '2', 2, '外科科室', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'DEPARTMENT', '科室字典', '003', '儿科', '3', 3, '儿科科室', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'DEPARTMENT', '科室字典', '004', '妇产科', '4', 4, '妇产科科室', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'DEPARTMENT', '科室字典', '005', '眼科', '5', 5, '眼科科室', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 性别字典
(6, 'GENDER', '性别字典', '0', '未知', '0', 1, '未知性别', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'GENDER', '性别字典', '1', '男', '1', 2, '男性', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'GENDER', '性别字典', '2', '女', '2', 3, '女性', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 药品分类字典
(9, 'MEDICINE_TYPE', '药品分类', '001', '抗生素', '1', 1, '抗生素类药品', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'MEDICINE_TYPE', '药品分类', '002', '解热镇痛', '2', 2, '解热镇痛类药品', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'MEDICINE_TYPE', '药品分类', '003', '维生素', '3', 3, '维生素类药品', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 疾病分类字典
(12, 'DISEASE_TYPE', '疾病分类', '001', '呼吸系统疾病', '1', 1, '呼吸系统疾病', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'DISEASE_TYPE', '疾病分类', '002', '消化系统疾病', '2', 2, '消化系统疾病', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'DISEASE_TYPE', '疾病分类', '003', '心血管系统疾病', '3', 3, '心血管系统疾病', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 收费项目字典
(15, 'CHARGE_ITEM', '收费项目', '001', '挂号费', '10', 1, '普通挂号费', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'CHARGE_ITEM', '收费项目', '002', '检查费', '50', 2, '普通检查费', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, 'CHARGE_ITEM', '收费项目', '003', '化验费', '30', 3, '普通化验费', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 'CHARGE_ITEM', '收费项目', '004', '药费', '0', 4, '药品费用', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化系统配置数据
INSERT INTO sys_config (id, config_key, config_value, config_name, description, status, create_time, update_time) VALUES
(1, 'system.name', '医院管理系统', '系统名称', '医院管理系统名称', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'system.logo', '/logo.png', '系统Logo', '系统Logo图片地址', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'system.copyright', 'Copyright © 2024 医院管理系统', '版权信息', '系统版权信息', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'system.patient.no.prefix', 'P', '病历号前缀', '患者病历号前缀', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'system.register.fee', '10', '挂号费用', '普通挂号费用(元)', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
