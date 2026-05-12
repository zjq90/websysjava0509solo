# 医院住院管理系统

## 项目简介

本项目是一个完整的医院住院管理Web平台系统，采用前后端分离架构，实现了住院管理的全流程功能。

## 技术栈

### 后端技术
- **JDK 1.8** - Java开发环境
- **Spring Boot 2.7.18** - 后端框架
- **H2 Database** - 内存数据库
- **Maven** - 项目构建工具
- **Swagger-OpenAPI** - API文档自动生成
- **Spring Data JPA** - 数据持久化
- **Lombok** - 简化Java代码

### 前端技术
- **Vue 2.6.14** - 前端框架
- **Vue Router 3.5.3** - 路由管理
- **Element UI 2.15.9** - UI组件库
- **Axios 0.27.2** - HTTP客户端
- **ECharts 5.3.3** - 数据可视化

## 项目结构

### 后端目录结构
```
src/main/java/com/hospital/
├── HospitalApplication.java    # 应用启动类
├── common/                     # 公共类
│   └── Result.java             # 统一响应结果封装
├── config/                     # 配置类
│   ├── SwaggerConfig.java      # Swagger配置
│   ├── CorsConfig.java         # 跨域配置
│   └── DataInitializer.java    # 数据初始化
├── entity/                     # 实体类
│   ├── Patient.java            # 患者实体
│   ├── Hospitalization.java    # 住院记录实体
│   ├── Bed.java                # 床位实体
│   ├── MedicalOrder.java       # 医嘱实体
│   ├── MedicalRecord.java      # 病历实体
│   ├── Medicine.java           # 药品实体
│   ├── VitalSigns.java         # 生命体征实体
│   ├── FeeRecord.java          # 费用记录实体
│   └── Staff.java              # 医护人员实体
├── repository/                 # 数据访问层
├── service/                    # 业务逻辑层
└── controller/                 # 控制层
```

### 前端目录结构
```
frontend/
├── public/
│   └── index.html              # HTML入口
├── src/
│   ├── main.js                 # Vue应用入口
│   ├── App.vue                 # 主组件
│   ├── router/
│   │   └── index.js            # 路由配置
│   └── views/                  # 页面组件
│       ├── Home.vue            # 首页
│       ├── Patient.vue         # 患者管理
│       ├── Hospitalization.vue # 住院管理
│       ├── MedicalOrder.vue    # 医嘱管理
│       ├── MedicalRecord.vue   # 病历管理
│       ├── VitalSigns.vue      # 生命体征
│       ├── Bed.vue             # 床位管理
│       ├── Medicine.vue        # 药品管理
│       ├── Fee.vue             # 费用管理
│       └── Staff.vue           # 医护人员管理
└── package.json                # 依赖配置
```

## 功能模块

### 1. 入院管理
- 患者入院登记
- 床位分配
- 患者基本信息采集
- 自动生成住院号

### 2. 住院医生工作站
- 住院病历书写（首次病程、病程记录、出院小结）
- 长期/临时医嘱开具
- 检查检验申请
- 手术指令下达

### 3. 护士工作站
- 医嘱执行（给药、治疗、护理操作）
- 护理文书记录
- 患者生命体征录入
- 床位管理
- 费用记账

### 4. 出院管理
- 出院手续办理
- 费用结算
- 出院带药
- 病历归档

### 5. 病房管理
- 床位状态实时视图
- 床位调换
- 患者转科

### 6. 住院药房管理
- 医嘱摆药、发药
- 病区小药柜管理

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+

### 后端启动

1. 进入项目根目录
```bash
cd websysjava23
```

2. 编译并运行
```bash
mvn clean compile
mvn spring-boot:run
```

3. 后端服务地址
- 应用地址：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console

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

4. 前端访问地址
- 应用地址：http://localhost:8081

## 数据库说明

本项目使用H2内存数据库，系统启动时自动初始化测试数据。

### H2控制台访问
- 地址：http://localhost:8080/h2-console
- JDBC URL：`jdbc:h2:mem:hospitaldb`
- 用户名：`sa`
- 密码：（空）

## API文档

启动后端服务后，访问 Swagger UI 查看完整API文档：
http://localhost:8080/swagger-ui.html

## 主要API接口

### 患者管理
- `GET /api/patients` - 获取患者列表
- `POST /api/patients` - 新增患者
- `GET /api/patients/{id}` - 获取患者详情
- `PUT /api/patients/{id}` - 更新患者信息
- `DELETE /api/patients/{id}` - 删除患者

### 住院管理
- `GET /api/hospitalizations` - 获取住院记录列表
- `POST /api/hospitalizations/admission` - 办理入院
- `POST /api/hospitalizations/{id}/discharge` - 办理出院

### 医嘱管理
- `GET /api/medical-orders` - 获取医嘱列表
- `POST /api/medical-orders` - 新增医嘱
- `POST /api/medical-orders/{id}/execute` - 执行医嘱
- `POST /api/medical-orders/{id}/stop` - 停止医嘱

### 其他模块
- 床位管理、病历管理、药品管理、生命体征、费用管理、医护人员管理等均提供完整的CRUD接口。

## 系统特点

1. **模块化架构**：清晰的包结构，分层设计，易于维护和扩展
2. **详细注释**：代码包含详细的中文注释，便于理解
3. **测试数据**：系统启动时自动生成完整的测试数据
4. **API文档**：集成Swagger-OpenAPI，自动生成接口文档
5. **响应式设计**：前端页面采用响应式设计，美观简洁
6. **统一响应格式**：后端API统一返回格式，便于前端处理

## 开发说明

### 后端开发
- 使用Spring Boot 2.7.18，兼容JDK 1.8
- 使用Spring Data JPA进行数据持久化
- 使用Lombok简化实体类代码
- 所有Controller返回统一的Result格式

### 前端开发
- 使用Vue 2.x + Element UI组件库
- 使用Axios进行HTTP请求
- 所有页面都包含完整的CRUD功能
- 支持搜索、分页、表单验证等功能

## 注意事项

1. H2为内存数据库，重启后数据会重置
2. 系统启动时会自动初始化测试数据
3. 前端默认访问后端地址为 http://localhost:8080/api
4. 如需修改后端地址，请修改 `frontend/src/main.js` 中的 `axios.defaults.baseURL`

## 版本信息

- 版本号：1.0.0
- 更新日期：2025年
