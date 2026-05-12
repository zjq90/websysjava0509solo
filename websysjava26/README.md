# 医院收费与财务管理系统

## 项目简介
本项目是一个基于Spring Boot + Vue的医院收费与财务管理系统，包含门诊收费、住院收费、医保结算、财务报表等核心功能模块。

## 技术栈

### 后端
- JDK 1.8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- Swagger OpenAPI 3.0
- Maven

### 前端
- Vue 2.x
- Vue Router
- Element UI
- Axios
- ECharts

## 项目结构

```
hospital-finance/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/finance/
│   │   │   ├── entity/          # 实体类
│   │   │   ├── repository/      # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── controller/      # 控制器层
│   │   │   └── config/          # 配置类
│   │   └── resources/
│   │       └── application.yml  # 应用配置
├── frontend/                     # 前端项目
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── App.vue
│   │   └── main.js
│   └── package.json
└── pom.xml
```

## 功能模块

### 1. 患者管理
- 患者信息的增删改查
- 医保信息管理

### 2. 门诊收费
- 门诊收费记录创建
- 费用明细管理
- 收费结算
- 退费处理

### 3. 住院收费
- 住院登记
- 费用明细添加
- 出院结算
- 退费处理

### 4. 医保结算
- 医保结算记录管理
- 医保对接接口
- 结算撤销

### 5. 财务报表
- 日报表生成
- 月报表生成
- 报表审核
- 结账处理

## 快速开始

### 后端启动

1. 确保已安装JDK 1.8和Maven
2. 在项目根目录下执行：
```bash
mvn spring-boot:run
```
3. 访问以下地址：
   - 应用服务：http://localhost:8080
   - Swagger文档：http://localhost:8080/swagger-ui.html
   - H2控制台：http://localhost:8080/h2-console

### 前端启动

1. 进入frontend目录
2. 安装依赖：
```bash
npm install
```
3. 启动开发服务器：
```bash
npm run serve
```
4. 访问：http://localhost:8081

## 数据库说明

项目使用H2内存数据库，系统启动时会自动初始化测试数据，包括：
- 5条患者测试数据
- 门诊收费记录及明细
- 住院收费记录及明细
- 医保结算记录

## API接口说明

所有API接口均已通过Swagger OpenAPI进行文档化，访问Swagger UI即可查看完整的接口文档。

主要接口：
- `/api/patients` - 患者管理接口
- `/api/outpatient-charges` - 门诊收费接口
- `/api/inpatient-charges` - 住院收费接口
- `/api/insurance/settlements` - 医保结算接口
- `/api/financial-reports` - 财务报表接口

## 测试说明

系统已内置测试数据初始化功能，启动后可直接进行功能测试：

1. 查看首页数据概览
2. 测试患者信息的增删改查
3. 新建门诊收费并完成结算
4. 新建住院收费并完成结算
5. 创建医保结算记录
6. 生成财务日报表和月报表
