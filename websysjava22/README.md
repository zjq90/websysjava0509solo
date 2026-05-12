# 医院门诊管理系统

## 项目简介

本项目是一个完整的医院门诊管理系统，包含预约挂号、分诊叫号、医生工作站、门诊收费、药房管理、综合查询等功能模块。

## 技术栈

### 后端
- JDK 1.8+
- Spring Boot 2.7.x
- H2 内存数据库
- Spring Data JPA
- SpringDoc OpenAPI (Swagger)
- Lombok

### 前端
- Vue 3
- Vue Router
- Element Plus
- Axios
- Vite

## 项目结构

```
websysjava22/
├── src/
│   └── main/
│       ├── java/com/hospital/clinic/
│       │   ├── config/          # 配置类
│       │   ├── entity/          # 实体类
│       │   ├── repository/      # 数据访问层
│       │   ├── service/         # 业务逻辑层
│       │   └── controller/      # 控制层
│       └── resources/
│           └── application.yml  # 配置文件
├── frontend/                     # 前端项目
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── api/                 # API封装
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── pom.xml                       # Maven配置
└── README.md
```

## 功能模块

### 1. 预约挂号管理
- 支持多种预约方式
- 号源管理
- 专家排班
- 患者信息登记与验证
- 分时段预约

### 2. 门诊分诊叫号
- 按科室、医生智能分诊
- 显示屏叫号
- 语音叫号
- 过号处理
- 复诊管理

### 3. 医生工作站
- 患者接诊
- 病史记录
- 电子处方
- 检查检验项目申请
- 结构化录入
- 用药安全警示

### 4. 门诊收费
- 多种支付方式
- 费用计算
- 票据打印
- 科室信息联动

### 5. 药房管理
- 处方接收
- 药品调配
- 发药核对
- 库存预警
- 处方点评

### 6. 综合查询
- 就诊人次统计
- 医生工作量统计
- 处方查询统计

## 启动说明

### 后端启动

1. 确保已安装 JDK 1.8+ 和 Maven
2. 在项目根目录执行：
   ```bash
   mvn spring-boot:run
   ```
3. 后端服务端口：8080
4. Swagger文档地址：http://localhost:8080/swagger-ui.html
5. H2控制台地址：http://localhost:8080/h2-console

### 前端启动

1. 确保已安装 Node.js 14+
2. 进入 frontend 目录：
   ```bash
   cd frontend
   ```
3. 安装依赖：
   ```bash
   npm install
   ```
4. 启动开发服务器：
   ```bash
   npm run dev
   ```
5. 前端访问地址：http://localhost:3000

## 数据库说明

- 使用 H2 内存数据库
- 启动时自动初始化测试数据
- 包含科室、医生、患者、排班、预约、药品等测试数据

## API 接口

系统提供完整的 RESTful API 接口，包括：
- 医生管理接口
- 患者管理接口
- 预约挂号接口
- 叫号接口
- 药品管理接口
- 处方管理接口
- 收费管理接口

详细的API文档请访问 Swagger UI。
