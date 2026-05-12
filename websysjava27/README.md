# 医院统计分析与决策支持系统

## 项目简介

本项目是一个基于Spring Boot + Vue.js的医院统计分析与决策支持系统，提供运营指标、医疗质量、成本效益等多维度的数据统计分析功能。

## 技术栈

### 后端技术
- **JDK**: 1.8+
- **框架**: Spring Boot 2.7.x
- **ORM**: Spring Data JPA
- **数据库**: H2 (内存数据库)
- **API文档**: SpringDoc OpenAPI (Swagger)
- **构建工具**: Maven

### 前端技术
- **框架**: Vue 2.6.x
- **路由**: Vue Router
- **UI组件**: Element UI
- **图表**: ECharts
- **HTTP客户端**: Axios

## 项目结构

```
websysjava27/
├── src/
│   └── main/
│       ├── java/com/hospital/management/
│       │   ├── entity/          # 实体类
│       │   │   ├── OperationMetrics.java
│       │   │   ├── MedicalQuality.java
│       │   │   ├── CostBenefit.java
│       │   │   └── CustomReport.java
│       │   ├── repository/      # 数据访问层
│       │   ├── service/         # 业务逻辑层
│       │   ├── controller/      # 控制器层
│       │   ├── config/          # 配置类
│       │   │   └── DataInitializer.java
│       │   └── HospitalManagementApplication.java
│       └── resources/
│           └── application.yml
├── frontend/                     # 前端项目
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   │   ├── Home.vue
│   │   │   ├── operation/
│   │   │   ├── medical-quality/
│   │   │   ├── cost-benefit/
│   │   │   └── custom-reports/
│   │   ├── router/
│   │   ├── App.vue
│   │   └── main.js
│   ├── public/
│   ├── package.json
│   └── babel.config.js
└── pom.xml
```

## 功能模块

### 1. 运营指标分析
- 门诊量、住院量、手术量统计
- 床位使用率监控
- 平均住院日统计
- 药占比、耗占比分析
- 多维度趋势分析图表

### 2. 医疗质量分析
- 病历质量统计（甲级、乙级、丙级）
- 合理用药率分析
- 院内感染监控
- 不良事件统计
- 抗菌药物使用强度分析

### 3. 成本效益分析
- 科室成本统计（人力、药品、耗材、设备折旧等）
- 收入构成分析
- 利润与利润率计算
- 成本收益率分析
- 人均创收统计

### 4. 自定义报表
- 报表模板管理
- 灵活的统计维度配置
- 多种图表类型支持（折线图、柱状图、饼图、表格）
- 报表导出功能

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 1.8 或更高版本
   - Maven 3.6 或更高版本

2. **启动步骤**
   ```bash
   # 进入项目根目录
   cd websysjava27

   # 编译项目
   mvn clean compile

   # 启动项目
   mvn spring-boot:run
   ```

3. **访问地址**
   - 应用服务: http://localhost:8080
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/api/h2-console
     - JDBC URL: `jdbc:h2:mem:hospitaldb`
     - 用户名: `sa`
     - 密码: (空)

### 前端启动

1. **环境要求**
   - Node.js 12.x 或更高版本
   - npm 6.x 或更高版本

2. **启动步骤**
   ```bash
   # 进入前端目录
   cd frontend

   # 安装依赖
   npm install

   # 启动开发服务器
   npm run serve
   ```

3. **访问地址**
   - 前端应用: http://localhost:8081

## API接口说明

### 运营指标接口 (`/api/operation-metrics`)
- `GET /`: 查询所有运营指标
- `GET /{id}`: 根据ID查询
- `POST /`: 新增运营指标
- `PUT /{id}`: 更新运营指标
- `DELETE /{id}`: 删除运营指标
- `GET /date-range`: 按日期范围查询
- `GET /statistics-summary`: 获取统计汇总
- `GET /aggregate-by-department`: 按科室分组统计

### 医疗质量接口 (`/api/medical-quality`)
- 与运营指标接口结构类似

### 成本效益接口 (`/api/cost-benefit`)
- 与运营指标接口结构类似

### 自定义报表接口 (`/api/custom-reports`)
- 与运营指标接口结构类似

## 数据库设计

系统启动时会自动初始化测试数据，包含以下科室的示例数据：
- 内科、外科、妇产科、儿科、骨科、眼科、口腔科、肿瘤科、神经内科、急诊科等

## 开发说明

### 后端开发
1. 实体类位于 `entity` 包，使用JPA注解映射数据库表
2. Repository层继承 `JpaRepository` 提供基础CRUD功能
3. Service层实现业务逻辑
4. Controller层提供RESTful API接口

### 前端开发
1. 页面组件位于 `src/views/` 目录
2. 使用Element UI组件库构建界面
3. ECharts用于数据可视化
4. Axios用于与后端API通信

## 测试说明

系统内置了测试数据生成器 (`DataInitializer.java`)，启动时会自动生成：
- 近30天的运营指标数据
- 近30天的医疗质量数据
- 近30天的成本效益数据
- 4个系统预设报表模板

## 注意事项

1. H2数据库为内存数据库，重启后数据会重置
2. 生产环境建议使用MySQL或PostgreSQL等持久化数据库
3. 前端默认后端地址为 `http://localhost:8080/api`，如需修改请调整 `main.js` 中的配置

## 许可证

MIT License
