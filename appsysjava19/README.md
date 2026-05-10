# 种子库存管理系统

基于Spring Boot + H2 + Swagger + UniApp的移动库存管理应用系统。

## 项目结构

```
appsysjava19/
├── backend/          # 后端Spring Boot项目
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/seedinventory/
│       │   ├── entity/        # 实体类
│       │   ├── repository/    # 数据访问层
│       │   ├── service/       # 业务逻辑层
│       │   ├── controller/    # 控制器层
│       │   ├── util/          # 工具类
│       │   ├── config/        # 配置类
│       │   ├── common/        # 公共类
│       │   └── SeedInventoryApplication.java
│       └── resources/
│           └── application.yml
└── frontend/         # 前端UniApp项目
    ├── pages/        # 页面组件
    ├── utils/        # 工具类
    ├── App.vue
    ├── main.js
    ├── pages.json
    ├── manifest.json
    └── package.json
```

## 环境要求

### 后端
- Java 8 或 Java 17+
- Maven 3.6+
- Spring Boot 2.7.18（兼容Java 8）

### 前端
- Node.js 16+
- HBuilderX 或 UniApp CLI

## 快速开始

### 1. 启动后端

进入backend目录：

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
```

或者直接运行jar包：
```bash
java -jar target/seed-inventory-backend-1.0.0.jar
```

启动成功后访问：
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:seedinventorydb`
  - 用户名: `sa`
  - 密码: (空)

### 2. 启动前端

进入frontend目录：

```bash
cd frontend
npm install
npm run dev:h5    # H5开发模式
npm run dev:mp-weixin  # 微信小程序开发模式
```

或者使用HBuilderX打开frontend目录，点击运行。

## 核心功能

### 移动库存管理
- **扫码出入库**：扫码或手动输入批次号完成入库、出库操作
- **库存查询**：实时查看各仓库存量与保质期分布
- **多仓库切换**：支持多仓库管理，可切换不同仓库查看
- **近效期预警**：自动检测近效期和过期库存，触发预警通知
- **缺货提醒**：库存低于阈值时自动提醒补货

### 数据校验规则
- **批次编号**：8位数字+字母组合，全局唯一
- **保质期**：不得早于当前日期+6个月
- **发芽率**：0–100%，精度保留1位小数
- **客户手机号**：中国大陆手机号格式（1开头，11位）

### 数据加密
- 敏感信息（客户联系方式、地址、邮箱）采用AES-256加密存储

## 测试数据

系统启动时自动初始化以下测试数据：

### 仓库（3个）
- 北京中心仓库 (WH001)
- 上海分仓 (WH002)
- 广州冷藏仓 (WH003)

### 种子（5种）
- 京农1号小麦、绿秀番茄、东北大豆、早佳8424西瓜、德玉1号玉米

### 库存（5个批次）
- WH01AB1234 - 小麦，保质期12个月
- WH02CD5678 - 番茄，保质期8个月
- WH03EF9012 - 大豆，保质期18个月
- WH04GH3456 - 西瓜，保质期6个月
- WH05IJ7890 - 玉米，保质期60天（近效期）

### 客户（3个）
- 北京现代农业合作社
- 上海果蔬种植基地
- 广州种子经销部

## API接口

### 库存管理
- `POST /api/inventory/inbound` - 入库操作
- `POST /api/inventory/outbound` - 出库操作
- `GET /api/inventory/scan/{batchNo}` - 扫码查询
- `GET /api/inventory/list` - 库存列表
- `POST /api/inventory/check-expiry` - 检测近效期

### 仓库管理
- `GET /api/warehouses` - 仓库列表
- `POST /api/warehouses` - 新增仓库
- `PUT /api/warehouses/{id}` - 更新仓库
- `DELETE /api/warehouses/{id}` - 删除仓库

### 种子管理
- `GET /api/seeds` - 种子列表
- `POST /api/seeds` - 新增种子
- `PUT /api/seeds/{id}` - 更新种子
- `DELETE /api/seeds/{id}` - 删除种子

### 客户管理
- `GET /api/customers` - 客户列表
- `POST /api/customers` - 新增客户
- `PUT /api/customers/{id}` - 更新客户
- `DELETE /api/customers/{id}` - 删除客户

### 消息通知
- `GET /api/notifications` - 通知列表
- `GET /api/notifications/unread/count` - 未读数量
- `PUT /api/notifications/{id}/read` - 标记已读
- `PUT /api/notifications/{id}/handle` - 标记已处理

## 前端页面

1. **首页** - 统计数据、快捷操作、当前仓库、最近消息
2. **库存管理** - 库存列表、筛选、仓库切换
3. **扫码操作** - 扫码/手动输入批次号查询
4. **入库登记** - 表单填写入库信息
5. **出库登记** - 选择客户、填写出库数量
6. **库存详情** - 库存信息、出入库记录
7. **消息通知** - 预警和提醒消息
8. **仓库管理** - 仓库列表、切换当前仓库
9. **客户管理** - 客户列表、搜索

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- SpringDoc OpenAPI (Swagger)
- Spring Security
- Lombok
- AES-256 加密

### 前端
- UniApp (Vue 3)
- 微信小程序/H5/APP多端适配
- 响应式设计

## 注意事项

1. **Java版本**：当前配置支持Java 8，如需使用Java 17+，请修改pom.xml中的Spring Boot版本为3.2.5
2. **图标文件**：前端tabBar需要图标文件，请将PNG图标放入 `frontend/static/icons/` 目录
3. **数据持久化**：H2为内存数据库，重启后数据会重置，如需持久化请修改application.yml
4. **加密密钥**：生产环境请修改application.yml中的加密密钥

## 测试说明

### 后端API测试
1. 启动后端服务
2. 访问 http://localhost:8080/swagger-ui.html
3. 使用Swagger UI测试各个接口

### 功能测试流程
1. **入库测试**：
   - 准备有效的批次号（8位数字字母组合）
   - 设置保质期（当前日期+6个月以上）
   - 设置发芽率（0.0-100.0，1位小数）
   - 调用入库接口

2. **出库测试**：
   - 使用现有批次号
   - 出库数量不超过当前库存
   - 可选关联客户

3. **预警测试**：
   - 调用 `/api/inventory/check-expiry`
   - 查看消息通知列表

4. **数据校验测试**：
   - 测试无效批次号（格式错误、重复）
   - 测试保质期小于6个月
   - 测试发芽率超出范围
   - 测试无效手机号
