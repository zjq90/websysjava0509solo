# 医院药品与物资管理系统

## 项目简介

本系统是一个基于Spring Boot + Vue的医院药品与物资管理平台，实现了医院药品及各类物资耗材的全生命周期管理，确保供应、保障安全、控制成本。

## 技术栈

### 后端

- JDK 8
- Spring Boot 2.7.x
- H2 内存数据库
- Maven
- SpringDoc OpenAPI (Swagger)
- Spring Data JPA

### 前端

- Vue 2.x
- Element UI
- Axios
- Vue Router

## 功能模块

### 药品管理模块

1. 药品字典维护
2. 采购计划管理
3. 入库验收
4. 库存管理（包括效期管理、批号管理、近效期预警、滞销预警）
5. 出库管理
6. 盘点
7. 报损
8. 调价

### 物资耗材管理模块

1. 非药品类物资分类管理
2. 从申请、采购、入库、领用消耗到盘点的全过程追踪
3. 高值耗材唯一标识管理

## 项目结构

```
websysjava24/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/
│   │   │   ├── HospitalApplication.java       # 启动类
│   │   │   ├── config/                         # 配置类
│   │   │   │   ├── OpenApiConfig.java          # Swagger配置
│   │   │   │   └── DataInitializer.java        # 数据初始化
│   │   │   ├── entity/                           # 实体类
│   │   │   │   ├── medicine/                    # 药品相关实体
│   │   │   │   └── material/                   # 物资相关实体
│   │   │   ├── repository/                       # 数据访问层
│   │   │   │   ├── medicine/
│   │   │   │   └── material/
│   │   │   ├── service/                          # 业务逻辑层
│   │   │   │   ├── medicine/
│   │   │   │   └── material/
│   │   │   └── controller/                       # 控制层
│   │   │       ├── medicine/
│   │   │       └── material/
│   │   └── resources/
│   │       └── application.yml                    # 配置文件
├── frontend/                                       # 前端项目
│   ├── package.json
│   ├── public/
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── router/
│       └── views/
│           ├── medicine/
│           └── material/
└── pom.xml
```

## 快速开始

### 后端启动

1. 确保已安装JDK 8和Maven
2. 在项目根目录执行：

```bash
mvn clean install
mvn spring-boot:run
```

3. 后端服务启动后访问：
   - API地址: http://localhost:8080/api
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/api/h2-console
     - JDBC URL: jdbc:h2:mem:hospitaldb
     - 用户名: sa
     - 密码: (空)

### 前端启动

1. 确保已安装Node.js
2. 在frontend目录执行：

```bash
cd frontend
npm install
npm run serve
```

3. 前端访问地址: http://localhost:8081

## API接口说明

### 药品管理接口

- `GET /api/medicine` - 查询所有药品
- `GET /api/medicine/{id}` - 根据ID查询药品
- `GET /api/medicine/page` - 分页查询药品
- `POST /api/medicine` - 新增药品
- `PUT /api/medicine/{id}` - 更新药品
- `DELETE /api/medicine/{id}` - 删除药品

### 药品库存接口

- `GET /api/medicine/inventory` - 查询所有药品库存
- `GET /api/medicine/inventory/near-expiry` - 查询近效期药品
- `GET /api/medicine/inventory/medicine/{medicineId}` - 根据药品ID查询库存

### 物资管理接口

- `GET /api/material` - 查询所有物资
- `GET /api/material/{id}` - 根据ID查询物资
- `GET /api/material/page` - 分页查询物资
- `POST /api/material` - 新增物资
- `PUT /api/material/{id}` - 更新物资
- `DELETE /api/material/{id}` - 删除物资

### 物资库存接口

- `GET /api/material/inventory` - 查询所有物资库存
- `GET /api/material/inventory/high-value` - 查询高值耗材库存
- `GET /api/material/inventory/near-expiry` - 查询近效期物资

## 前端页面功能

1. **药品字典管理** - 药品信息的增删改查，包括药品编码、名称、规格、剂型、厂家等
2. **药品库存管理** - 查看库存数量、批号、效期、库位，近效期预警
3. **物资字典管理** - 物资信息的增删改查，支持高值耗材标识
4. **物资库存管理** - 查看库存、高值耗材唯一标识追踪

## 数据库表结构

### 主要数据表

- medicine - 药品字典表
- medicine_inventory - 药品库存表
- medicine_purchase_plan - 药品采购计划表
- medicine_in_stock - 药品入库记录表
- medicine_out_stock - 药品出库记录表
- material - 物资字典表
- material_inventory - 物资库存表
- material_application - 物资领用申请表

## 注意事项

1. 本项目使用H2内存数据库，重启后数据会丢失
2. 前端项目默认端口为8081，后端为8080
3. 已配置跨域支持，前后端分离开发
4. 系统预置了部分测试数据，可直接使用
