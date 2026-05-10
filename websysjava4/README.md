# Web平台管理系统

## 项目概述

这是一个基于Spring Boot + Vue的Web平台管理系统，提供完整的数据统计和分析功能。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.18
- H2 内存数据库
- Maven
- SpringDoc OpenAPI (Swagger)
- JPA

### 前端
- Vue 3
- Vue Router 4
- Vuex 4
- Element Plus
- ECharts 5
- Axios

## 功能模块

### 1. 商品统计
- 热销商品排行
- 滞销商品分析
- 毛利率分析（按分类统计）

### 2. 设备效能统计
- 单机产出排行
- 故障率统计
- 运维成本分析

### 3. 用户行为统计
- 活跃用户数统计
- 复购率分析
- 新用户增长趋势（近30天）
- 购买时段分布热力图

### 4. 数据大屏
- 全局视角的运营指标展示
- 实时数据更新

## 项目结构

```
websysjava4/
├── backend/                    # 后端项目
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/management/platform/
│       │   ├── ManagementPlatformApplication.java
│       │   ├── entity/         # 实体类
│       │   │   ├── Product.java
│       │   │   ├── Device.java
│       │   │   ├── User.java
│       │   │   ├── Order.java
│       │   │   ├── DeviceFault.java
│       │   │   └── UserBehavior.java
│       │   ├── repository/     # 数据访问层
│       │   ├── service/        # 业务逻辑层
│       │   ├── controller/     # 控制器层
│       │   ├── config/         # 配置类
│       │   └── init/           # 数据初始化
│       └── resources/
│           └── application.yml
│
├── frontend/                   # 前端项目
│   ├── package.json
│   ├── public/
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── api/                # API服务
│       ├── router/             # 路由配置
│       ├── store/              # 状态管理
│       ├── styles/             # 样式文件
│       ├── components/         # 组件
│       │   └── Layout.vue
│       └── views/              # 页面
│           ├── Dashboard.vue   # 数据大屏
│           ├── Products.vue    # 商品统计
│           ├── Devices.vue     # 设备效能
│           └── Users.vue       # 用户统计
│
└── README.md
```

## 快速开始

### 环境要求
- JDK 8+
- Maven 3.6+
- Node.js 14+ (用于前端)
- npm 或 yarn (用于前端)

### 后端运行

1. 进入后端目录：
```bash
cd backend
```

2. 编译并打包：
```bash
mvn clean package
```

3. 运行应用：
```bash
java -jar target/platform-1.0.0.jar
```

4. 访问地址：
- 应用地址：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

### 前端运行

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 运行开发服务器：
```bash
npm run serve
```

4. 访问地址：http://localhost:8081

## API接口

### 商品管理
- `GET /api/products` - 分页查询商品
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品
- `GET /api/products/stats/hot-selling` - 热销商品
- `GET /api/products/stats/unsold` - 滞销商品
- `GET /api/products/stats/gross-margin` - 毛利率分析

### 设备管理
- `GET /api/devices` - 分页查询设备
- `GET /api/devices/{id}` - 获取设备详情
- `POST /api/devices` - 创建设备
- `PUT /api/devices/{id}` - 更新设备
- `DELETE /api/devices/{id}` - 删除设备
- `GET /api/devices/stats/single-output` - 单机产出
- `GET /api/devices/stats/fault-rate` - 故障率统计
- `GET /api/devices/stats/maintenance-cost` - 运维成本

### 用户管理
- `GET /api/users` - 分页查询用户
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `GET /api/users/stats/active` - 活跃用户统计
- `GET /api/users/stats/repurchase-rate` - 复购率
- `GET /api/users/stats/growth-trend` - 新用户增长
- `GET /api/users/stats/purchase-heatmap` - 购买时段分布

### 数据大屏
- `GET /api/dashboard` - 大屏综合数据

## 测试数据

系统启动时会自动生成测试数据：
- 25个商品
- 20个设备
- 50个用户
- 100个订单
- 30条设备故障记录
- 200条用户行为记录

## 开发说明

### 后端开发
- 代码使用清晰的包结构：entity → repository → service → controller
- 每个类都有详细的注释说明
- 使用Spring Data JPA简化数据访问

### 前端开发
- 使用Vue 3 Composition API
- ECharts用于数据可视化
- Element Plus提供UI组件
- 页面样式统一，布局合理

## 许可证

Apache License 2.0
