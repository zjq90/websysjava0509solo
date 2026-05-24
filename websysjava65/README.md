# 共享单车管理后台系统

## 项目简介

基于 Spring Boot + H2 + Swagger 后端技术栈和 Vue 3 + Element Plus 前端技术栈开发的共享单车管理后台系统。

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- Swagger (SpringDoc OpenAPI)
- Lombok

### 前端
- Vue 3
- Vue Router 4
- Element Plus
- ECharts 5
- Axios
- Vite

## 项目结构

```
websysjava65/
├── src/
│   └── main/
│       ├── java/com/bikeshare/
│       │   ├── BikeShareAdminApplication.java    # 启动类
│       │   ├── common/                           # 公共模块
│       │   │   └── Result.java                   # 统一响应结果
│       │   ├── config/                           # 配置类
│       │   │   ├── SwaggerConfig.java            # Swagger配置
│       │   │   └── CorsConfig.java               # 跨域配置
│       │   ├── controller/                       # 控制层
│       │   │   ├── DashboardController.java      # 数据看板
│       │   │   ├── UserController.java           # 用户管理
│       │   │   └── FinanceController.java        # 财务管理
│       │   ├── dto/                              # 数据传输对象
│       │   │   ├── DashboardDTO.java
│       │   │   └── FinanceDTO.java
│       │   ├── entity/                           # 实体类
│       │   │   ├── User.java
│       │   │   ├── UserBlacklist.java
│       │   │   ├── CustomerTicket.java
│       │   │   ├── UserMembership.java
│       │   │   ├── Bike.java
│       │   │   ├── Area.java
│       │   │   ├── RideRecord.java
│       │   │   ├── PaymentRecord.java
│       │   │   ├── Invoice.java
│       │   │   ├── CostRecord.java
│       │   │   └── Reconciliation.java
│       │   ├── repository/                       # 数据访问层
│       │   └── service/                          # 业务层
│       └── resources/
│           ├── application.yml                   # 配置文件
│           └── data.sql                          # 初始化数据
├── frontend/                                     # 前端项目
│   ├── src/
│   │   ├── views/                                # 页面
│   │   │   ├── Dashboard.vue
│   │   │   ├── user/
│   │   │   │   ├── Blacklist.vue
│   │   │   │   ├── Tickets.vue
│   │   │   │   └── Membership.vue
│   │   │   └── finance/
│   │   │       ├── Reconciliation.vue
│   │   │       ├── Invoices.vue
│   │   │       └── Cost.vue
│   │   ├── router/                               # 路由
│   │   ├── api/                                  # API接口
│   │   ├── utils/                                # 工具类
│   │   └── App.vue
│   └── package.json
└── pom.xml
```

## 功能模块

### 数据看板
- 实时运营数据：骑行次数、收入、用户增长
- 车辆状态分布：在线、离线、故障、维修中
- 区域使用率对比：朝阳区 vs 海淀区等

### 用户管理
- 用户黑名单：恶意破坏车辆者永久封禁
- 客服工单处理：退款申诉、投诉反馈
- 用户分层运营：高频用户赠送月卡

### 财务管理
- 自动对账：用户支付、押金、运维支出
- 发票管理：企业用户批量开票
- 成本分析：车辆折旧、运维人力、充电费用

## 快速开始

### 后端启动

1. 确保已安装 JDK 11+ 和 Maven

2. 在项目根目录执行：
```bash
mvn clean install
mvn spring-boot:run
```

3. 后端服务启动后访问：
   - API地址: http://localhost:8080/api
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2控制台: http://localhost:8080/api/h2-console
     - JDBC URL: jdbc:h2:mem:bikeshare_db
     - 用户名: admin
     - 密码: admin

### 前端启动

1. 确保已安装 Node.js 16+

2. 在 frontend 目录执行：
```bash
npm install
npm run dev
```

3. 前端服务启动后访问：http://localhost:3000

## 数据库表设计

符合数据库设计范式，共11张表：
1. users - 用户表
2. user_blacklist - 用户黑名单表
3. customer_tickets - 客服工单表
4. user_memberships - 用户会员表
5. bikes - 车辆表
6. areas - 区域表
7. ride_records - 骑行记录表
8. payment_records - 支付记录表
9. invoices - 发票表
10. cost_records - 成本记录表
11. reconciliations - 对账记录表

## 初始化数据

系统启动时会自动加载示例数据，包括：
- 5个区域（朝阳区、海淀区等）
- 5个测试用户
- 8辆单车
- 多条骑行记录、支付记录、工单记录等
