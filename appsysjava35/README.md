# 用户端APP系统

## 项目简介

基于Spring Boot + H2数据库 + Redis + UniApp开发的用户端APP系统，包含会员权益体系、积分系统、消息通知中心等功能。

## 技术栈

### 后端
- Spring Boot 2.7.x
- Spring Data JPA
- H2 Database（嵌入式数据库）
- Redis（缓存）
- SpringDoc OpenAPI（API文档）
- Spring Security
- RSA加密

### 前端
- UniApp（跨端框架）
- Vue 2.x

## 项目结构

```
appsysjava35/
├── backend/                          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/appsys/
│   │   │   │   ├── config/           # 配置类
│   │   │   │   ├── controller/       # 控制器
│   │   │   │   ├── entity/           # 实体类
│   │   │   │   ├── repository/       # 数据访问层
│   │   │   │   ├── service/          # 业务逻辑层
│   │   │   │   ├── util/             # 工具类
│   │   │   │   └── common/           # 通用类
│   │   │   └── resources/
│   │   │       └── application.yml   # 配置文件
│   └── pom.xml
├── frontend/                         # 前端项目
│   ├── pages/                        # 页面
│   ├── common/                       # 通用工具
│   ├── static/                       # 静态资源
│   ├── App.vue
│   ├── main.js
│   ├── pages.json
│   └── package.json
└── README.md
```

## 功能模块

### 1. 会员与权益体系
- 会员等级：普通会员 → 银卡会员 → 金卡会员 → 钻石会员
- 成长值升级机制
- 积分倍率加成
- 高星级用户专属客户经理
- 优先客服通道

### 2. 积分系统
- 每日签到获取积分
- 办理业务、评价服务获取积分
- 积分兑换话费
- 积分兑换视频会员
- 积分兑换实物礼品
- 积分记录查询

### 3. 消息与通知中心
- 分类通知：账单提醒、服务进度更新、优惠活动、安全预警
- 支持标记已读/全部已读
- 未读消息数量统计

### 4. 其他功能
- 长辈模式：字体放大、界面简化
- RSA加密传输敏感信息
- 响应式设计，适配手机/平板

## 快速开始

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 编译项目：
```bash
mvn clean install
```

3. 启动应用：
```bash
mvn spring-boot:run
```

或者直接运行JAR包：
```bash
java -jar target/user-app-backend-1.0.0.jar
```

### 访问地址

- 应用服务：http://localhost:8080/api
- H2数据库控制台：http://localhost:8080/api/h2-console
  - JDBC URL：jdbc:h2:file:./data/appsysdb
  - 用户名：sa
  - 密码：（空）
- Swagger API文档：http://localhost:8080/api/swagger-ui.html

### 测试账号

系统启动后会自动初始化以下测试用户：

| 用户名 | 密码 | 会员等级 | 积分 | 成长值 | 备注 |
|--------|------|----------|------|--------|------|
| zhangsan | 123456 | 普通会员 | 500 | 500 | - |
| lisi | 123456 | 金卡会员 | 3500 | 6000 | 有专属客户经理 |
| wangwu | 123456 | 钻石会员 | 12500 | 25000 | 有专属客户经理 |

## API接口说明

### 用户管理
- POST /api/user/login - 用户登录
- POST /api/user/register - 用户注册
- GET /api/user/{id} - 获取用户信息
- PUT /api/user/{id} - 更新用户信息
- POST /api/user/elder-mode - 切换长辈模式

### 会员管理
- GET /api/member/levels - 获取所有会员等级
- GET /api/member/user/{userId} - 获取用户会员信息
- POST /api/member/upgrade - 增加成长值

### 积分管理
- GET /api/point/goods - 获取积分商品列表
- GET /api/point/records/{userId} - 获取用户积分记录
- POST /api/point/checkin - 签到
- POST /api/point/exchange - 积分兑换商品

### 通知管理
- GET /api/notification/user/{userId} - 获取用户通知列表
- GET /api/notification/unread/{userId} - 获取未读消息数量
- POST /api/notification/read/{id} - 标记单条已读
- POST /api/notification/read-all/{userId} - 标记全部已读

## 前端开发

### 安装依赖
```bash
cd frontend
npm install
```

### 运行开发模式
```bash
npm run dev
```

### 构建
```bash
npm run build
```

## 特色功能

### 1. RSA数据加密
- 后端自动生成RSA密钥对
- 支持加密/解密敏感数据
- 支持数字签名验证

### 2. 长辈模式
- 一键切换
- 字体自动放大
- 界面元素简化
- 提升可访问性

### 3. 响应式设计
- 适配不同屏幕尺寸
- 手机、平板均有良好体验

## 开发说明

### 数据库
使用H2嵌入式数据库，数据文件保存在 `backend/data/` 目录下。

### 缓存
Redis用于缓存热点数据，需确保Redis服务运行在 localhost:6379。

### API文档
集成Swagger OpenAPI，启动后可在线查看和测试接口。

## 许可证
MIT License
