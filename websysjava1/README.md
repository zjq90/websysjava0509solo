# Web平台管理系统

## 项目概述

这是一个基于 Spring Boot + Vue 的Web平台管理系统，用于管理自动售卖设备。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- Maven
- Swagger-OpenAPI (springdoc-openapi-ui)
- Lombok

### 前端
- Vue 2.6.14
- Vue Router 3.5.3
- Element UI 2.15.13
- Axios 0.27.2

## 项目结构

```
websysjava1/
├── backend/                    # 后端项目
│   ├── pom.xml                # Maven配置
│   └── src/main/
│       ├── java/com/websys/
│       │   ├── common/        # 公共类
│       │   ├── config/        # 配置类
│       │   ├── controller/    # 控制器层
│       │   ├── entity/        # 实体层
│       │   ├── repository/    # 数据访问层
│       │   ├── service/       # 业务逻辑层
│       │   └── WebsysApplication.java
│       └── resources/
│           └── application.yml
└── frontend/                   # 前端项目
    ├── package.json
    ├── vue.config.js
    └── src/
        ├── api/               # API调用
        ├── router/            # 路由配置
        ├── utils/             # 工具类
        └── views/             # 页面组件
```

## 功能模块

### 1. 系统用户管理
- 系统管理员、运营、维护、财务等不同角色
- 分配不同的菜单和操作权限
- 用户CRUD操作

### 2. 设备管理
- 设备基本信息管理
- 投放位置管理
- 实时展示设备运行状态
- 网络质量、信号强度监控
- 货道电机状态、门锁状态监控
- 温湿度、电力状况监控

### 3. 远程控制
- 远程重启设备
- 远程开锁
- 调整制冷/加热温度
- 设置屏幕广告内容
- 固件远程升级（支持批量或单个）

### 4. 商品管理
- 商品种类管理
- 商品总量、余量统计
- 货道状况管理
- 补货、出货操作

## 数据库表结构

### 核心实体
1. **User** - 用户表
2. **Role** - 角色表
3. **Permission** - 权限表
4. **Menu** - 菜单表
5. **Device** - 设备表
6. **DeviceStatus** - 设备状态表
7. **Product** - 商品表
8. **Slot** - 货道表

## 快速开始

### 后端运行

#### 环境要求
- JDK 8+
- Maven 3.6+

#### 启动步骤
```bash
cd backend

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

启动成功后，访问：
- Swagger文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:websysdb`
  - 用户名: `sa`
  - 密码: `(空)`

### 前端运行

#### 环境要求
- Node.js 14+
- npm 6+ 或 yarn

#### 启动步骤
```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run serve
```

启动成功后，访问：http://localhost:3000

## API接口说明

### 用户管理
- `GET /api/user/page` - 分页查询用户
- `POST /api/user` - 新增用户
- `PUT /api/user` - 更新用户
- `DELETE /api/user/{id}` - 删除用户

### 设备管理
- `GET /api/device/page` - 分页查询设备
- `POST /api/device` - 新增设备
- `PUT /api/device` - 更新设备
- `DELETE /api/device/{id}` - 删除设备
- `GET /api/device-status/latest/{deviceId}` - 获取设备最新状态

### 远程控制
- `POST /api/device-status/{deviceId}/restart` - 远程重启
- `POST /api/device-status/{deviceId}/unlock` - 远程开锁
- `POST /api/device-status/{deviceId}/temperature` - 调整温度
- `POST /api/device-status/{deviceId}/advert` - 设置广告
- `POST /api/device-status/firmware-upgrade` - 固件升级

### 商品管理
- `GET /api/product/page` - 分页查询商品
- `POST /api/product` - 新增商品
- `PUT /api/product` - 更新商品
- `DELETE /api/product/{id}` - 删除商品

### 货道管理
- `GET /api/slot/page` - 分页查询货道
- `POST /api/slot` - 新增货道
- `PUT /api/slot` - 更新货道
- `DELETE /api/slot/{id}` - 删除货道
- `POST /api/slot/{id}/replenish` - 补货
- `POST /api/slot/{id}/dispense` - 出货

## 测试数据

系统启动时会自动初始化测试数据：

### 用户
- admin / 123456 (系统管理员)
- operator / 123456 (运营)
- maintainer / 123456 (维护)
- finance / 123456 (财务)

### 设备
- DEV001 - DEV005 五台测试设备

### 商品
- 可口可乐、农夫山泉、乐事薯片、康师傅牛肉面、红牛等

### 货道
- 每台设备配置多个货道

## 注意事项

1. H2数据库为内存数据库，重启后数据会重置
2. 首次运行后端需要下载Maven依赖
3. 首次运行前端需要安装npm依赖
4. 确保8080和3000端口未被占用

## 测试步骤

### 1. 后端测试
1. 启动后端项目
2. 访问Swagger文档 http://localhost:8080/swagger-ui.html
3. 使用Swagger UI测试各接口
4. 访问H2控制台 http://localhost:8080/h2-console 查看数据

### 2. 前端测试
1. 启动前端项目
2. 访问 http://localhost:3000
3. 测试各页面功能：
   - 用户管理：新增、编辑、删除用户
   - 设备管理：查看设备详情和状态
   - 远程控制：测试重启、开锁、调温等功能
   - 商品管理：新增、编辑、删除商品
   - 货道管理：查看货道库存、执行补货操作
