# 医院管理系统

## 项目简介

这是一个基于SpringBoot + Vue的医院管理系统，采用前后端分离架构，包含系统管理模块及各业务角色功能演示。

## 技术栈

### 后端
- JDK 1.8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- SpringDoc OpenAPI (Swagger)
- Spring Security + JWT
- Maven

### 前端
- Vue 2.x
- Vue Router
- Vuex
- Element UI
- Axios
- js-cookie

## 项目结构

```
websysjava21/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hospital/
│   │   │   │   ├── config/         # 配置类
│   │   │   │   ├── controller/     # 控制器
│   │   │   │   ├── entity/         # 实体类
│   │   │   │   ├── repository/     # 数据访问层
│   │   │   │   ├── service/        # 业务逻辑层
│   │   │   │   ├── dto/            # 数据传输对象
│   │   │   │   ├── util/           # 工具类
│   │   │   │   └── HospitalApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── data.sql        # 初始化数据
│   │   │       └── schema.sql
│   └── pom.xml
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   │   ├── system/         # 系统管理模块
│   │   │   ├── doctor/         # 医生模块
│   │   │   ├── nurse/          # 护士模块
│   │   │   ├── finance/        # 财务模块
│   │   │   ├── Login.vue
│   │   │   ├── Layout.vue
│   │   │   └── Dashboard.vue
│   │   ├── router/             # 路由配置
│   │   ├── store/              # Vuex状态管理
│   │   ├── utils/              # 工具类
│   │   ├── assets/             # 静态资源
│   │   └── main.js
│   ├── package.json
│   └── vue.config.js
└── README.md
```

## 功能模块

### 系统管理模块
1. **用户管理**：用户信息的增删改查
2. **角色管理**：角色定义与权限分配
3. **字典管理**：维护系统基础数据字典（科室、药品、疾病等）
4. **日志管理**：记录用户操作日志与系统运行日志
5. **系统配置**：系统参数配置管理

### 业务角色功能
1. **医院管理者**：首页数据看板、运营指标查看
2. **临床医生**：患者管理、病历查看、医嘱管理
3. **护士**：病房管理、护理记录、体征采集
4. **医技科室人员**：检查检验执行、结果报告
5. **收费人员**：收费登记、日结对账
6. **行政后勤人员**：物资设备、人力资源管理

## 快速开始

### 后端启动

1. 进入后端目录
```bash
cd backend
```

2. 编译项目
```bash
mvn clean install
```

3. 启动项目
```bash
mvn spring-boot:run
```

4. 访问地址
- 后端API：http://localhost:8080/api
- Swagger文档：http://localhost:8080/api/swagger-ui.html
- H2控制台：http://localhost:8080/api/h2-console

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run serve
```

4. 访问地址：http://localhost:8081

## 默认账号

| 用户名 | 密码 | 角色 | 说明 |
|-------|------|------|------|
| admin | 123456 | 系统管理员 | 拥有所有权限 |
| manager | 123456 | 医院管理者 | 管理运营相关功能 |
| doctor | 123456 | 临床医生 | 医生相关功能 |
| nurse | 123456 | 护士 | 护理相关功能 |

## 数据库说明

本项目使用H2内存数据库，数据在内存中存储，重启后会重置。如需持久化，可修改`application.yml`中的数据库配置。

H2控制台登录信息：
- JDBC URL：jdbc:h2:mem:hospitaldb
- 用户名：sa
- 密码：（空）

## API文档

启动后端后，访问Swagger文档查看所有API接口：
http://localhost:8080/api/swagger-ui.html

主要API分类：
- 认证接口：/auth/**
- 用户管理：/system/user/**
- 角色管理：/system/role/**
- 字典管理：/system/dict/**
- 日志管理：/system/log/**
- 配置管理：/system/config/**

## 开发说明

### 后端开发规范
1. Controller层负责接收请求和返回响应
2. Service层负责业务逻辑处理
3. Repository层负责数据访问
4. Entity对应数据库表
5. DTO用于前后端数据传输

### 前端开发规范
1. 使用Vue组件化开发
2. 页面组件放在views目录
3. 路由统一在router/index.js配置
4. 状态管理使用Vuex
5. API请求统一封装在utils/request.js

## 版本信息

- 版本号：1.0.0
- 更新日期：2024-01-15
