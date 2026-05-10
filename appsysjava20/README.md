# 财务与报表管理系统

## 项目概述

本系统是一个基于 Spring Boot + UniApp 的财务与报表管理系统，包含后端API服务和移动端App前端。

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- SpringDoc OpenAPI (Swagger)
- AES-256 数据加密
- Lombok

### 前端
- UniApp (Vue 2.x)
- 微信小程序 / H5 / App 多端支持

## 项目结构

```
appsysjava20/
├── backend/                    # 后端Spring Boot项目
│   ├── pom.xml
│   └── src/main/java/com/appsys/finance/
│       ├── FinanceApplication.java
│       ├── config/             # 配置类
│       │   ├── DataInitializer.java
│       │   ├── GlobalExceptionHandler.java
│       │   └── SwaggerConfig.java
│       ├── controller/         # 控制器层
│       │   ├── CustomerController.java
│       │   ├── ProductController.java
│       │   ├── EmployeeController.java
│       │   ├── FinanceController.java
│       │   └── DashboardController.java
│       ├── entity/             # 实体类
│       │   ├── Customer.java
│       │   ├── Product.java
│       │   ├── Employee.java
│       │   └── SalesOrder.java
│       ├── repository/         # 数据访问层
│       │   ├── CustomerRepository.java
│       │   ├── ProductRepository.java
│       │   ├── EmployeeRepository.java
│       │   └── SalesOrderRepository.java
│       ├── service/            # 业务逻辑层
│       │   ├── CustomerService.java
│       │   ├── ProductService.java
│       │   ├── EmployeeService.java
│       │   ├── SalesOrderService.java
│       │   └── DashboardService.java
│       ├── util/               # 工具类
│       │   └── AesEncryptUtil.java
│       └── resources/
│           └── application.yml
└── frontend/                   # 前端UniApp项目
    ├── manifest.json
    ├── pages.json
    ├── package.json
    ├── App.vue
    ├── main.js
    ├── api/
    │   └── index.js
    ├── utils/
    │   └── request.js
    └── pages/
        ├── index/
        ├── login/
        ├── performance/
        ├── dashboard/
        ├── customer/
        ├── product/
        ├── order/
        └── mine/
```

## 功能模块

### 1. 财务与报表
- 个人业绩查询（销售金额、回款金额、提成计算）
- 订单管理（创建、编辑、回款登记）
- 客户管理（增删改查，敏感信息加密）
- 产品管理（增删改查，业务规则校验）

### 2. 管理层经营看板
- 销售额统计
- 毛利率计算
- 库存周转率
- 销售趋势分析
- 销售排行榜

### 3. 业务规则校验
- **批次编号**：必须为8位数字+字母组合，且全局唯一
- **保质期**：不得早于当前日期+6个月
- **发芽率**：数值范围 0–100%，精度保留1位小数
- **客户手机号**：符合中国大陆手机号格式（1开头，11位）

### 4. 数据安全
- 敏感信息（客户联系方式、员工电话、财务数据）采用 AES-256 加密存储
- 数据库中存储加密后的数据，读取时自动解密

## 快速启动

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 使用 Maven 编译运行：
```bash
mvn spring-boot:run
```

3. 或打包后运行：
```bash
mvn clean package
java -jar target/finance-app-1.0.0.jar
```

4. 访问地址：
- API服务：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console

### 前端启动

1. 使用 HBuilderX 打开 `frontend` 目录

2. 选择运行方式：
- **运行到浏览器**：点击菜单栏 运行 → 运行到浏览器 → Chrome
- **运行到小程序**：点击菜单栏 运行 → 运行到小程序模拟器 → 微信开发者工具
- **运行到手机**：手机连接后，点击菜单栏 运行 → 运行到手机或模拟器

## 测试账号

系统初始化时会自动创建以下测试数据：

### 员工账号
| 姓名 | 工号 | 角色 | 提成比例 |
|------|------|------|----------|
| 王经理 | EMP001 | 管理人员 | 3.5% |
| 销售员A | EMP002 | 普通员工 | 2.0% |
| 销售员B | EMP003 | 普通员工 | 2.5% |

### 测试客户
- 张三、李四、王五、赵六、钱七

### 测试产品
- 优质小麦种子 (BAT001A1)
- 高产玉米种子 (BAT002B2)
- 有机蔬菜种子 (BAT003C3)
- 大豆种子 (BAT004D4)

## API接口

### 客户管理
- `GET /api/customers` - 获取客户列表
- `GET /api/customers/{id}` - 获取客户详情
- `POST /api/customers` - 创建客户
- `PUT /api/customers/{id}` - 更新客户
- `DELETE /api/customers/{id}` - 删除客户

### 产品管理
- `GET /api/products` - 获取产品列表
- `GET /api/products/{id}` - 获取产品详情
- `GET /api/products/batch/{batchNumber}` - 按批次查询
- `POST /api/products` - 创建产品
- `PUT /api/products/{id}` - 更新产品
- `DELETE /api/products/{id}` - 删除产品

### 财务报表
- `GET /api/finance/performance/{employeeId}` - 个人业绩
- `GET /api/finance/orders` - 订单列表
- `POST /api/finance/orders` - 创建订单
- `PUT /api/finance/orders/{id}` - 更新订单

### 经营看板
- `GET /api/dashboard` - 获取经营看板数据

## 数据库配置

默认使用 H2 内存数据库，配置如下：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:finance_db
    username: sa
    password: 
  h2:
    console:
      enabled: true
      path: /h2-console
```

如需使用 MySQL 等其他数据库，可修改 `application.yml` 配置。

## 安全说明

### AES-256 加密配置

```yaml
app:
  aes:
    key: MySecretKey1234567890
    iv: 1234567890123456
```

**注意**：生产环境请修改默认的加密密钥！

### 加密字段
- Customer.phone - 客户手机号
- Customer.email - 客户邮箱
- Employee.phone - 员工电话

## 前端页面说明

| 页面 | 路径 | 说明 |
|------|------|------|
| 登录页 | pages/login/login | 选择测试账号登录 |
| 首页 | pages/index/index | 功能入口、概览数据 |
| 个人业绩 | pages/performance/performance | 销售业绩、回款情况、提成 |
| 经营看板 | pages/dashboard/dashboard | 管理层指标看板 |
| 客户管理 | pages/customer/* | 客户增删改查 |
| 产品管理 | pages/product/* | 产品增删改查 |
| 订单管理 | pages/order/* | 订单创建、回款登记 |
| 我的 | pages/mine/mine | 用户信息、系统设置 |

## 业务规则验证

### 前端验证
- 手机号格式：`/^1\d{10}$/`
- 批次编号格式：`/^[A-Za-z0-9]{8}$/`
- 发芽率范围：0.0 - 100.0
- 保质期：≥ 当前日期 + 6个月

### 后端验证
- 实体类注解验证
- Service 层业务逻辑校验
- 全局异常处理

## 测试功能

### 后端API测试
访问 Swagger UI：http://localhost:8080/swagger-ui.html

### 前端业务规则测试
在 App 中：
1. 首页 → 🔧 测试功能
2. 我的 → 🔧 业务规则测试

包含以下测试项：
- 手机号格式验证演示
- 批次编号格式验证演示
- 保质期规则说明
- 发芽率范围说明
- AES加密机制说明

## 开发环境要求

### 后端
- JDK 1.8+
- Maven 3.6+
- IDE：IntelliJ IDEA 或 Eclipse

### 前端
- HBuilderX 3.0+
- 微信开发者工具（如开发小程序）
- Node.js 14+

## 部署说明

### 后端部署
1. 打包：`mvn clean package`
2. 运行：`java -jar finance-app-1.0.0.jar`
3. 或使用 Docker 容器化部署

### 前端部署
- **H5**：构建后部署到 Web 服务器
- **小程序**：上传代码包到微信公众平台
- **App**：打包生成 apk/ipa 后发布到应用市场

## 版本历史

- v1.0.0 - 初始版本，包含完整功能

## 许可证

本项目仅供学习和开发使用。
