# 共享单车运维管理系统

## 项目简介

本项目是一个完整的共享单车服务端管理系统，包含后端API服务和前端运维APP。系统提供车辆调度管理、车辆维护、电池管理等核心功能，帮助运维人员高效管理共享单车运营。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 内存数据库
- **缓存**: Redis
- **API文档**: Swagger-OpenAPI (SpringDoc)
- **ORM**: Spring Data JPA
- **工具**: Lombok, FastJSON

### 前端
- **框架**: UniApp (Vue 3)
- **状态管理**: Vuex
- **UI组件**: uview-plus
- **网络请求**: uni.request

## 项目结构

```
appsysjava64/
├── backend/                    # 后端SpringBoot项目
│   ├── src/main/java/com/bike/
│   │   ├── common/            # 公共类
│   │   │   └── Result.java    # 统一响应结果
│   │   ├── config/            # 配置类
│   │   │   ├── DataInitializer.java      # 测试数据初始化
│   │   │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │   │   ├── OpenApiConfig.java        # Swagger配置
│   │   │   └── RedisConfig.java          # Redis配置
│   │   ├── controller/        # 控制器层
│   │   │   ├── AreaController.java
│   │   │   ├── BatteryController.java
│   │   │   ├── BikeController.java
│   │   │   ├── DispatchController.java
│   │   │   ├── MaintenanceController.java
│   │   │   └── StaffController.java
│   │   ├── entity/            # 实体类
│   │   │   ├── Area.java
│   │   │   ├── Battery.java
│   │   │   ├── BatteryLog.java
│   │   │   ├── Bike.java
│   │   │   ├── DispatchTask.java
│   │   │   ├── FaultRecord.java
│   │   │   ├── HeatPoint.java
│   │   │   ├── MaintenanceStaff.java
│   │   │   ├── RepairOrder.java
│   │   │   ├── SparePart.java
│   │   │   ├── SparePartLog.java
│   │   │   └── SwapStation.java
│   │   ├── repository/        # 数据访问层
│   │   ├── service/           # 业务逻辑层
│   │   └── BikeSharingApplication.java  # 启动类
│   ├── src/main/resources/
│   │   └── application.yml    # 配置文件
│   └── pom.xml                # Maven依赖
│
└── frontend/                   # 前端UniApp项目
    ├── api/
    │   └── index.js           # API接口封装
    ├── pages/                  # 页面
    │   ├── login/             # 登录页
    │   ├── tasks/             # 任务列表
    │   ├── task-detail/       # 任务详情
    │   ├── bike-detail/       # 车辆详情
    │   ├── scan/              # 扫码
    │   ├── map/               # 地图导航
    │   ├── battery/           # 电池管理
    │   ├── station/           # 换电站
    │   ├── profile/           # 个人中心
    │   └── heatmap/           # 热力图
    ├── store/
    │   └── index.js           # Vuex状态管理
    ├── static/                # 静态资源
    ├── App.vue                # 根组件
    ├── main.js                # 入口文件
    ├── manifest.json          # 应用配置
    ├── pages.json             # 页面路由配置
    └── package.json           # 依赖配置
```

## 功能模块

### 1. 车辆调度管理
- **热力图展示**: 显示高需求区域（地铁站、商圈等）
- **自动调度任务**: 根据车辆分布自动生成调度建议
- **任务接单**: 运维人员APP接单并导航

### 2. 车辆维护
- **故障标签**: 需维修、待报废、已修复等状态管理
- **维修工单**: 全流程跟踪（上报→接单→维修→完成）
- **备件库存**: 链条、轮胎、电池等备件管理

### 3. 电池管理（电动车型）
- **电量监控**: 实时监控，低于20%触发预警
- **换电站管理**: 地图导航与库存显示
- **生命周期**: 充电次数、健康度追踪

## 数据库设计

### 主要数据表
1. **bike** - 车辆表
2. **dispatch_task** - 调度任务表
3. **repair_order** - 维修工单表
4. **battery** - 电池表
5. **swap_station** - 换电站表
6. **spare_part** - 备件表
7. **heat_point** - 热力点表
8. **maintenance_staff** - 运维人员表
9. **area** - 区域表
10. **fault_record** - 故障记录表

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 1.8+
   - Maven 3.6+
   - Redis (可选，用于缓存)

2. **启动步骤**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **访问地址**
   - API服务: http://localhost:8080
   - Swagger文档: http://localhost:8080/swagger-ui.html
   - H2控制台: http://localhost:8080/h2-console

### 前端启动

1. **环境要求**
   - Node.js 14+
   - HBuilderX 或 UniApp CLI

2. **启动步骤**
   ```bash
   cd frontend
   npm install
   npm run dev:h5
   ```

3. **访问地址**
   - H5开发: http://localhost:3000

## API接口说明

### 车辆管理
- `GET /api/bike/list` - 获取所有车辆
- `GET /api/bike/{id}` - 获取车辆详情
- `GET /api/bike/qrcode/{qrCode}` - 通过二维码获取车辆
- `POST /api/bike/create` - 创建车辆
- `PUT /api/bike/{id}` - 更新车辆
- `DELETE /api/bike/{id}` - 删除车辆

### 调度管理
- `GET /api/dispatch/tasks/sorted` - 获取按优先级排序的任务
- `GET /api/dispatch/tasks/{id}` - 获取任务详情
- `POST /api/dispatch/tasks/{id}/accept` - 接单
- `POST /api/dispatch/tasks/{id}/complete` - 完成任务
- `GET /api/dispatch/heatpoints` - 获取热力图数据
- `GET /api/dispatch/suggestions` - 获取调度建议

### 维修管理
- `GET /api/maintenance/orders/sorted` - 获取按优先级排序的工单
- `GET /api/maintenance/orders/{id}` - 获取工单详情
- `POST /api/maintenance/orders/{id}/accept` - 接维修单
- `POST /api/maintenance/orders/{id}/start` - 开始维修
- `POST /api/maintenance/orders/{id}/complete` - 完成维修
- `GET /api/maintenance/parts` - 获取备件列表

### 电池管理
- `GET /api/battery/batteries` - 获取电池列表
- `GET /api/battery/warnings` - 获取低电量预警
- `GET /api/battery/stations` - 获取换电站列表
- `GET /api/battery/lifecycle-stats` - 获取生命周期统计
- `POST /api/battery/swap` - 换电操作

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 100辆共享单车（含电动车）
- 50块电池
- 10个换电站
- 20个备件
- 10名运维人员
- 15个调度任务
- 10个维修工单
- 20个热力点

## 注意事项

1. **Redis缓存**: 如未安装Redis，缓存功能会自动降级，不影响核心功能
2. **H2数据库**: 内存数据库，重启后数据会重置，生产环境建议改用MySQL
3. **前端部署**: UniApp支持多端发布（H5、小程序、App），请根据需要配置
4. **地图功能**: 实际项目中需接入高德/百度地图API

## 开发建议

1. 后端API开发时，请保持Swagger注释完整
2. 前端开发时，请遵循UniApp的跨端规范
3. 数据库表设计遵循第三范式，避免数据冗余
4. 重要业务操作请添加事务管理
5. 频繁访问的数据考虑添加Redis缓存

## 许可证

MIT License
