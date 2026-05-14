# 运营端后台管理系统

## 项目简介

这是一个完整的运营端后台管理系统，包含工单调度运维、数据分析报表、营销活动管理等功能模块。

## 技术栈

### 后端
- **Spring Boot 2.7.x**: Web应用框架
- **JPA/Hibernate**: ORM框架
- **H2 Database**: 内存数据库
- **Spring Data Redis**: Redis缓存
- **SpringDoc OpenAPI**: API文档
- **iText7**: PDF报表生成
- **Apache POI**: Excel报表生成

### 前端
- **Vue 2.x**: 前端框架
- **Vue Router**: 路由管理
- **Element UI**: UI组件库
- **Axios**: HTTP客户端
- **ECharts**: 数据可视化

## 项目结构

```
appsysjava36/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/ops/
│   │       │   ├── controller/    # 控制器层
│   │       │   ├── service/       # 服务层
│   │       │   ├── repository/    # 数据访问层
│   │       │   ├── entity/        # 实体类
│   │       │   ├── config/        # 配置类
│   │       │   └── common/        # 通用类
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
└── frontend/                   # 前端项目
    ├── src/
    │   ├── views/              # 页面组件
    │   ├── router/             # 路由配置
    │   ├── App.vue
    │   └── main.js
    ├── public/
    │   └── index.html
    └── package.json
```

## 功能模块

### 1. 工单调度与运维支持
- 智能派单：根据用户地址、装维人员位置、负载情况自动分配
- 工单状态管理：创建、接单、处理、完成全流程
- 支持拍照上传、电子签名、服务评价
- 工单详情查看与管理

### 2. 数据分析与报表
- 用户行为分析：套餐分布、活跃度统计
- 服务效能监控：响应时长、修复率、满意度
- 工单趋势图表展示
- 报表导出：支持PDF日报和Excel数据导出

### 3. 营销与活动管理
- 优惠券管理：创建、发放、定向推送
- 营销活动：限时折扣、套餐组合、老用户回馈
- 目标用户群体细分：即将到期用户、低活跃用户等

## 快速开始

### 后端启动

```bash
cd backend
# 使用Maven编译运行
mvn spring-boot:run
```

后端服务启动后访问：
- API文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console

### 前端启动

```bash
cd frontend
# 安装依赖
npm install
# 启动开发服务器
npm run serve
```

前端访问：http://localhost:8081

## API接口说明

### 工单管理接口
- `GET /api/workorders` - 获取所有工单
- `POST /api/workorders` - 创建工单
- `POST /api/workorders/{id}/assign` - 智能派单
- `POST /api/workorders/{id}/accept` - 接单
- `POST /api/workorders/{id}/complete` - 完成工单
- `POST /api/workorders/{id}/evaluate` - 提交评价

### 数据分析接口
- `GET /api/analytics/metrics` - 获取服务效能指标
- `GET /api/analytics/order-trend` - 获取工单趋势
- `GET /api/analytics/report/daily-pdf` - 导出日报PDF
- `GET /api/analytics/report/workorder-excel` - 导出Excel

### 营销管理接口
- `GET /api/marketing/coupons` - 获取优惠券列表
- `POST /api/marketing/coupons` - 创建优惠券
- `POST /api/marketing/coupons/{id}/distribute` - 发放优惠券
- `GET /api/marketing/promotions` - 获取活动列表
- `POST /api/marketing/promotions` - 创建营销活动

## 数据库说明

系统使用H2内存数据库，启动时自动初始化测试数据：
- 8个测试用户（包含不同套餐类型）
- 5个装维人员（包含不同技能和区域）
- 20个测试工单（覆盖各种状态和类型）

H2控制台配置：
- JDBC URL: `jdbc:h2:mem:opsdb`
- 用户名: `sa`
- 密码: （空）

## 注意事项

1. 确保已安装JDK 11+和Node.js
2. Redis为可选依赖，如未安装Redis，缓存相关功能会降级
3. 前端默认访问后端地址：http://localhost:8080
4. 生产环境请更换数据库配置和密钥

## 开发说明

- 后端代码遵循MVC分层架构
- 前端代码采用组件化设计
- 所有API接口均有Swagger文档
- 支持跨域访问，便于前后端分离开发
