# 宠物医院问诊系统

## 项目简介

基于 SpringBoot + Uniapp 开发的宠物医院问诊管理系统，支持医生端的问诊管理、患者管理、处方管理、排班管理等功能，包含响应式设计和适老化长辈模式。

## 技术栈

### 后端
- **框架**: SpringBoot 2.7.x
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **API文档**: Swagger / SpringDoc OpenAPI
- **ORM**: Spring Data JPA
- **状态管理**: Pinia (前端)

### 前端
- **框架**: Uniapp + Vue 3
- **UI**: 原生样式，响应式设计
- **特性**: 长辈模式（字体放大、界面简化）

## 项目结构

```
appsysjava59/
├── pet-hospital-backend/       # 后端项目
│   ├── src/main/java/com/pethospital/
│   │   ├── entity/             # 实体类
│   │   ├── repository/         # 数据访问层
│   │   ├── service/            # 业务逻辑层
│   │   └── controller/         # 控制器层
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── data.sql            # 初始化测试数据
│   └── pom.xml
│
└── pet-hospital-uniapp/        # 前端项目
    ├── pages/                   # 页面文件
    │   ├── login/              # 登录页
    │   ├── index/              # 问诊列表
    │   ├── chat/               # 问诊聊天
    │   ├── pet/                # 患者/宠物管理
    │   ├── prescription/       # 处方管理
    │   └── schedule/           # 排班管理
    ├── api/                    # API接口封装
    ├── store/                  # Pinia状态管理
    ├── utils/                  # 工具函数
    └── static/                 # 静态资源
```

## 功能模块

### 1. 问诊管理
- 待接诊列表（按紧急程度排序）
- 紧急问诊红色标签标注
- 问诊详情页：左侧待接诊，右侧聊天窗口
- 图文/视频问诊切换
- 聊天底部工具栏：处方、检查单、转诊
- 消息发送与接收

### 2. 患者管理
- 宠物档案列表
- 宠物详情查看
- 疫苗记录管理
- 过敏史红色高亮显示
- 自定义标签管理（慢性病、术后、易紧张、需轻柔操作等）

### 3. 电子处方
- 药品搜索联想（输入"驱"显示"福来恩"等）
- 库存自动校验
- 用量用法强制选择（避免手动输入错误）
- 处方历史查看
- 处方明细展示

### 4. 排班管理
- 日历视图展示排班
- 班次设置（上午/下午/夜班）
- 调班申请功能
- 调班审批流程
- 休息日标记（事假/年假）

## 快速开始

### 后端启动

1. 确保已安装 JDK 11+ 和 Maven
2. 确保 Redis 服务已启动（localhost:6379）
3. 进入后端目录：
   ```bash
   cd pet-hospital-backend
   ```
4. 启动项目：
   ```bash
   mvn spring-boot:run
   ```
5. 访问地址：
   - API接口: http://localhost:8080
   - Swagger文档: http://localhost:8080/swagger-ui.html
   - H2控制台: http://localhost:8080/h2-console

### 前端启动

1. 确保已安装 Node.js 和 HBuilderX
2. 进入前端目录：
   ```bash
   cd pet-hospital-uniapp
   ```
3. 使用 HBuilderX 打开项目，选择运行到浏览器或小程序

## 测试账号

系统已初始化以下测试账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| doctor1 | 123456 | 医生 | 张医生 |
| doctor2 | 123456 | 医生 | 李医生 |
| admin | admin123 | 管理员 | 王主任 |

## API接口列表

### 用户模块
- `POST /api/user/login` - 用户登录
- `GET /api/user/doctors` - 获取医生列表
- `GET /api/user/{id}` - 获取用户信息

### 问诊模块
- `GET /api/consultation/doctor/{id}/pending` - 获取待接诊列表
- `GET /api/consultation/{id}` - 获取问诊详情
- `GET /api/consultation/{id}/messages` - 获取问诊消息
- `POST /api/consultation/{id}/message` - 发送消息
- `PUT /api/consultation/{id}/status` - 更新问诊状态

### 宠物模块
- `GET /api/pet` - 获取所有宠物
- `GET /api/pet/{id}` - 获取宠物详情
- `GET /api/pet/{id}/tags` - 获取宠物标签
- `POST /api/pet/{id}/tags` - 添加宠物标签
- `DELETE /api/pet/tags/{id}` - 删除宠物标签
- `GET /api/pet/{id}/vaccines` - 获取疫苗记录

### 处方模块
- `GET /api/prescription/doctor/{id}` - 获取医生处方
- `POST /api/prescription` - 创建处方
- `GET /api/prescription/{id}/items` - 获取处方明细

### 药品模块
- `GET /api/medicine` - 获取所有药品
- `GET /api/medicine/search?keyword=xxx` - 搜索药品
- `POST /api/medicine/check-stock` - 检查库存

### 排班模块
- `GET /api/schedule?startDate=&endDate=` - 获取排班列表
- `POST /api/schedule` - 添加排班
- `DELETE /api/schedule/{id}` - 删除排班
- `GET /api/schedule/change-request/pending` - 获取待审批调班申请
- `POST /api/schedule/change-request` - 提交调班申请
- `PUT /api/schedule/change-request/{id}/approve` - 审批通过
- `PUT /api/schedule/change-request/{id}/reject` - 审批拒绝
- `GET /api/schedule/holiday/all` - 获取所有休息日
- `POST /api/schedule/holiday` - 添加休息日
- `DELETE /api/schedule/holiday/{id}` - 删除休息日

## 数据库表结构

### 主要数据表
1. `users` - 用户表（医生/管理员）
2. `pet_owner` - 宠物主人表
3. `pet` - 宠物表
4. `pet_tag` - 宠物标签表
5. `vaccine_record` - 疫苗记录表
6. `medicine` - 药品表
7. `consultation` - 问诊表
8. `consultation_message` - 问诊消息表
9. `prescription` - 处方表
10. `prescription_item` - 处方明细表
11. `schedule` - 排班表
12. `schedule_change_request` - 调班申请表
13. `holiday` - 休息日表

## 特性说明

### 适老化设计（长辈模式）
- 字体整体放大 20%-30%
- 按钮尺寸增大，便于点击
- 简化界面元素，突出核心功能
- 支持语音输入预留接口

### 响应式设计
- 适配手机、平板等多种屏幕尺寸
- 使用 rpx 单位实现屏幕自适应
- 平板端双栏布局，手机端单栏布局

### 数据安全
- 药品库存实时校验
- 处方开具记录完整追溯
- 用户登录状态管理

## 开发说明

### 后端开发规范
- 使用 RESTful API 设计规范
- 统一返回格式：`{ code, message, data }`
- 使用 JPA 进行数据访问
- Service 层处理业务逻辑

### 前端开发规范
- 使用 Vue 3 Composition API
- Pinia 进行状态管理
- API 请求统一封装
- 组件化开发

## 许可证

MIT License
