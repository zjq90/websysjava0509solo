# 二手交易系统

## 项目简介

这是一个基于Spring Boot + Vue.js的二手交易系统，提供商品管理、订单管理、纠纷处理、数据统计和系统管理等功能。

## 技术栈

### 后端
- **JDK 1.8**
- **Spring Boot 2.7.18**
- **H2 Database** (内存数据库)
- **Spring Data JPA**
- **Swagger OpenAPI 3.0**
- **Apache POI** (Excel导入导出)

### 前端
- **Vue 2.6.x**
- **Vue Router 3.x**
- **Vuex 3.x**
- **Element UI 2.x**
- **ECharts 5.x**
- **Axios**

## 项目结构

```
websysjava51/
├── src/
│   └── main/
│       ├── java/com/secondhand/
│       │   ├── controller/        # 控制器层
│       │   │   ├── ProductController.java
│       │   │   ├── OrderController.java
│       │   │   ├── DisputeController.java
│       │   │   ├── StatisticsController.java
│       │   │   └── SystemController.java
│       │   ├── entity/            # 实体类
│       │   │   ├── Product.java
│       │   │   ├── Order.java
│       │   │   ├── Dispute.java
│       │   │   ├── User.java
│       │   │   ├── Role.java
│       │   │   ├── OperationLog.java
│       │   │   └── InventoryConfig.java
│       │   ├── repository/        # 数据访问层
│       │   ├── service/           # 业务逻辑层
│       │   ├── config/            # 配置类
│       │   ├── common/            # 公共类
│       │   └── SecondhandTradingApplication.java
│       └── resources/
│           └── application.yml    # 配置文件
├── frontend/                       # 前端项目
│   ├── src/
│   │   ├── views/                 # 页面组件
│   │   │   ├── product/
│   │   │   ├── order/
│   │   │   ├── dispute/
│   │   │   ├── statistics/
│   │   │   └── system/
│   │   ├── layout/                # 布局组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # Vuex状态管理
│   │   ├── utils/                 # 工具类
│   │   ├── assets/                # 静态资源
│   │   ├── App.vue
│   │   └── main.js
│   └── package.json
└── pom.xml
```

## 功能模块

### 1. 商品管理
- 商品列表查询、新增、编辑、删除
- 批量导入商品（Excel）
- 批量下架、批量删除商品
- 全选/反选功能
- 库存预警功能（阈值可设置）
- 库存预警商品红色标记
- Excel导出功能

### 2. 订单管理
- 订单列表查询
- 按状态筛选（待付款、待发货、已发货、已完成、已取消、退款中、已退款）
- 异常订单显示警告图标
- 批量发货、批量退款
- 物流信息编辑
- 标记异常订单

### 3. 纠纷处理
- 纠纷列表查询
- 投诉详情查看
- 证据上传（文件大小限制5MB）
- 仲裁结果录入

### 4. 数据统计
- 交易额趋势图（日/周/月）
- 商品销量排行榜
- 用户增长分析
- 统计数据导出Excel

### 5. 系统管理
- 角色管理（管理员、卖家、客服、买家）
- 用户管理
- 操作日志审计
- 按用户、时间筛选操作日志

## 快速开始

### 后端启动

1. 确保已安装JDK 1.8+和Maven

2. 进入项目根目录，执行：
```bash
mvn clean install
mvn spring-boot:run
```

3. 访问地址：
- 后端API：http://localhost:8080/api
- Swagger文档：http://localhost:8080/api/swagger-ui.html
- H2控制台：http://localhost:8080/api/h2-console

### 前端启动

1. 确保已安装Node.js和npm

2. 进入frontend目录，执行：
```bash
cd frontend
npm install
npm run serve
```

3. 访问地址：http://localhost:8081

## 默认测试账号

- **管理员**：admin / admin123
- **卖家**：seller / seller123

## 数据库配置

项目使用H2内存数据库，启动时自动初始化测试数据，无需额外配置。

数据库连接信息：
- JDBC URL：`jdbc:h2:mem:secondhanddb`
- 用户名：`sa`
- 密码：（空）

## API文档

启动后端服务后，访问Swagger UI查看完整的API文档：
http://localhost:8080/api/swagger-ui.html

## 主要接口

### 商品管理
- `GET /products` - 分页查询商品列表
- `GET /products/{id}` - 查询商品详情
- `POST /products` - 新增商品
- `PUT /products/{id}` - 更新商品
- `DELETE /products/{id}` - 删除商品
- `POST /products/batch/off-shelf` - 批量下架
- `POST /products/batch/delete` - 批量删除
- `GET /products/low-stock` - 库存预警商品
- `POST /products/import` - 导入商品
- `POST /products/export` - 导出商品

### 订单管理
- `GET /orders` - 分页查询订单列表
- `GET /orders/{id}` - 查询订单详情
- `POST /orders` - 新增订单
- `PUT /orders/{id}` - 更新订单
- `POST /orders/batch/ship` - 批量发货
- `POST /orders/batch/refund` - 批量退款
- `POST /orders/{id}/mark-abnormal` - 标记异常订单
- `PUT /orders/{id}/logistics` - 更新物流信息

### 纠纷处理
- `GET /disputes` - 分页查询纠纷列表
- `GET /disputes/{id}` - 查询纠纷详情
- `POST /disputes` - 新增纠纷
- `PUT /disputes/{id}` - 更新纠纷
- `POST /disputes/upload-evidence` - 上传证据
- `POST /disputes/{id}/arbitration-result` - 录入仲裁结果

### 数据统计
- `GET /statistics/transaction/daily` - 日交易额趋势
- `GET /statistics/transaction/weekly` - 周交易额趋势
- `GET /statistics/transaction/monthly` - 月交易额趋势
- `GET /statistics/products/ranking` - 商品销量排行
- `GET /statistics/users/growth` - 用户增长分析

### 系统管理
- `GET /system/roles` - 角色列表
- `GET /system/roles/{id}` - 角色详情
- `POST /system/roles` - 新增角色
- `PUT /system/roles/{id}` - 更新角色
- `DELETE /system/roles/{id}` - 删除角色
- `GET /system/users` - 用户列表
- `GET /system/users/{id}` - 用户详情
- `POST /system/users` - 新增用户
- `PUT /system/users/{id}` - 更新用户
- `DELETE /system/users/{id}` - 删除用户
- `GET /system/operation-logs` - 操作日志列表
- `GET /system/operation-logs/{id}` - 操作日志详情

## 注意事项

1. 本项目为演示项目，使用内存数据库H2，重启后数据会重置
2. 文件上传功能会将文件保存到`uploads`目录下
3. 前端页面使用了Mock数据，实际使用时需要对接后端API
4. JDK版本要求1.8或以上

## 开发说明

### 后端开发
- 使用Lombok简化实体类代码
- 使用Spring Data JPA进行数据访问
- 统一返回结果格式
- 全局异常处理

### 前端开发
- 使用Element UI组件库
- 使用Vue Router进行路由管理
- 使用Vuex进行状态管理
- 使用ECharts进行图表展示
- 使用Axios进行API请求

## License

MIT
