# 宽带服务用户端App系统

## 项目简介

这是一个完整的宽带服务用户端App系统，包含后端Spring Boot API服务和前端UniApp应用。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 内嵌内存数据库
- **缓存**: Redis
- **API文档**: Swagger OpenAPI 3.0
- **ORM**: Spring Data JPA
- **安全**: RSA非对称加密

### 前端
- **框架**: Vue 3 + UniApp
- **UI设计**: 响应式设计、长辈模式适老化
- **状态管理**: Vue 3 Composition API

## 项目结构

```
.
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/broadband/
│   │       │   ├── common/        # 公共类
│   │       │   ├── config/       # 配置类
│   │       │   ├── controller/  # 控制器
│   │       │   ├── entity/      # 实体类
│   │       │   ├── repository/ # 数据访问层
│   │       │   ├── service/    # 业务逻辑层
│   │       │   └── util/       # 工具类
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
└── frontend/               # 前端项目
    ├── src/
    │   ├── pages/       # 页面
    │   │   ├── login/    # 登录
    │   │   ├── index/    # 首页
    │   │   ├── package/  # 套餐
    │   │   ├── order/    # 工单
    │   │   ├── bill/      # 账单
    │   │   └── user/      # 用户中心
    │   ├── api/         # API接口
    │   └── utils/       # 工具类
    ├── manifest.json
    ├── pages.json
    └── package.json
```

## 核心功能

### 1. 账户与身份管理
- 手机号验证码登录
- 人脸识别登录
- 微信/支付宝第三方登录
- 实名认证（身份证上传 + 活体检测）

### 2. 宽带全生命周期服务
- 新装办理
- 套餐管理
- 移机服务
- 销户申请

### 3. 账单与支付系统
- 账单查询
- 在线支付
- 支付渠道：微信/支付宝/银联/对公转账

### 4. 长辈模式
- 字体放大
- 界面简化
- 一键切换

## 快速开始

### 后端启动

1. 确保Redis服务已启动（端口6379）

2. 进入后端目录：
```bash
cd backend
```

3. 使用Maven编译并启动：
```bash
mvn clean package
mvn spring-boot:run
```

4. 访问地址：
   - API服务: http://localhost:8080
   - Swagger文档: http://localhost:8080/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/h2-console
     - JDBC URL: jdbc:h2:file:./data/broadband_db
     - 用户名: sa
     - 密码: (空)

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 运行到浏览器：
```bash
npm run dev:h5
```

4. HBuilderX方式：
   - 使用HBuilderX打开frontend目录
   - 选择"运行" → "运行到浏览器" → 选择Chrome

## API接口说明

### 认证接口
- `GET /api/auth/publicKey` - 获取RSA公钥
- `POST /api/auth/sendCode` - 发送验证码
- `POST /api/auth/login/phone` - 手机号登录
- `POST /api/auth/login/wechat` - 微信登录
- `POST /api/auth/login/alipay` - 支付宝登录
- `POST /api/auth/login/face` - 人脸识别登录

### 套餐接口
- `GET /api/package/list` - 获取套餐列表
- `GET /api/package/detail?id={id}` - 获取套餐详情

### 号码接口
- `GET /api/number/list` - 获取可选号码列表
- `GET /api/number/fancy` - 获取靓号列表

### 工单接口
- `POST /api/order/install` - 提交新装工单
- `POST /api/order/move` - 提交移机工单
- `POST /api/order/cancel` - 提交销户工单
- `GET /api/order/list` - 获取工单列表
- `GET /api/order/detail?id={id}` - 获取工单详情

### 账单接口
- `GET /api/bill/list` - 获取账单列表
- `GET /api/bill/detail?id={id}` - 获取账单详情
- `POST /api/bill/pay` - 支付账单

### 用户接口
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息
- `POST /api/user/realname` - 提交实名认证
- `GET /api/user/realname-status` - 获取实名认证状态

## 测试数据

系统启动时会自动初始化测试数据：

- **套餐数据**: 4款基础套餐 + 3款增值包
- **号码数据**: 8个城市，每个城市20个普通号码 + 15个靓号

## 注意事项

1. **Redis配置**: 如未安装Redis，可先安装Redis或修改配置使用内存存储验证码（仅限开发环境）
2. **RSA加密**: 系统启动时自动生成RSA密钥对，重启后会更新，之前加密的数据在重启后无法解密
3. **H2数据库**: 数据存储在文件中，不会因重启丢失
4. **活体检测**: 当前为模拟实现，生产环境需接入专业活体检测SDK

## 开发说明

- 后端代码有详细的注释，便于理解和扩展
- 前端页面采用统一的设计规范，支持长辈模式
- 所有API都有Swagger文档说明
- 敏感数据采用RSA加密传输

## 版本信息

- 版本: v1.0.0
- 最后更新: 2024
