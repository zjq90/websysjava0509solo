# 共享单车用户端APP系统

## 项目简介

本项目是一个完整的共享单车用户端APP系统，包含后端API服务和前端UniApp应用。系统支持用户注册登录、实名认证、押金缴纳、车辆查找预约、扫码开锁、骑行计费、故障上报、信用体系等完整功能。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 (内嵌数据库，无需额外安装)
- **缓存**: Redis
- **ORM**: MyBatis-Plus 3.5.3.1
- **认证**: JWT (JSON Web Token)
- **API文档**: Swagger/OpenAPI 3.0
- **工具库**: Hutool 5.8.20
- **构建工具**: Maven

### 前端技术栈
- **框架**: UniApp (Vue 3)
- **状态管理**: Pinia
- **样式**: SCSS
- **适配**: 手机、平板

## 项目结构

```
appsysjava63/
├── backend/                    # 后端项目
│   ├── src/main/java/com/bikesystem/
│   │   ├── common/            # 公共类
│   │   │   ├── Result.java
│   │   │   ├── ResultCode.java
│   │   │   ├── BusinessException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── PageResult.java
│   │   ├── config/            # 配置类
│   │   │   ├── CorsConfig.java
│   │   │   ├── MybatisPlusConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   ├── SwaggerConfig.java
│   │   │   ├── WebMvcConfig.java
│   │   │   └── MyMetaObjectHandler.java
│   │   ├── controller/        # 控制器层
│   │   │   ├── UserController.java
│   │   │   ├── BikeController.java
│   │   │   ├── RideController.java
│   │   │   ├── FaultReportController.java
│   │   │   └── CouponController.java
│   │   ├── service/           # 服务层
│   │   │   ├── UserService.java
│   │   │   ├── BikeService.java
│   │   │   ├── RideService.java
│   │   │   ├── FaultReportService.java
│   │   │   └── CouponService.java
│   │   ├── mapper/            # 数据访问层
│   │   ├── entity/            # 实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── interceptor/       # 拦截器
│   │   ├── utils/             # 工具类
│   │   ├── task/              # 定时任务
│   │   └── BikeSharingApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml    # 配置文件
│   │   └── db/
│   │       ├── schema.sql     # 数据库表结构
│   │       └── data.sql       # 测试数据
│   └── pom.xml
└── frontend/                   # 前端项目
    ├── pages/                  # 页面
    │   ├── index/              # 首页
    │   ├── login/              # 登录页
    │   ├── register/           # 注册页
    │   ├── realname/           # 实名认证页
    │   ├── deposit/            # 押金缴纳页
    │   ├── ride/               # 骑行页
    │   ├── mine/               # 个人中心
    │   ├── records/            # 骑行记录
    │   ├── coupons/            # 优惠券
    │   ├── credit/             # 信用分
    │   ├── fault/              # 故障上报
    │   ├── recharge/           # 账户充值
    │   └── scan/               # 扫码开锁
    ├── stores/                 # Pinia状态管理
    ├── api/                    # API接口
    ├── utils/                  # 工具类
    ├── styles/                 # 全局样式
    ├── App.vue
    ├── main.js
    ├── pages.json
    ├── manifest.json
    └── package.json
```

## 数据库设计

系统包含以下12张数据表：

1. **user** - 用户表
2. **bike** - 车辆表
3. **ride_record** - 骑行记录表
4. **reservation** - 预约记录表
5. **fault_report** - 故障上报表
6. **credit_record** - 信用记录表
7. **coupon** - 优惠券表
8. **deposit_record** - 押金记录表
9. **payment_record** - 支付记录表
10. **maintenance_record** - 运维记录表
11. **bike_station** - 停车点表
12. **system_config** - 系统配置表

## 功能模块

### 1. 用户模块
- 手机号注册/登录
- 微信/支付宝第三方登录
- 实名认证（身份证+人脸识别）
- 押金缴纳（支持信用免押）
- 账户充值
- 个人信息管理

### 2. 车辆模块
- 地图显示附近可用车辆
- 车辆筛选（车型、电量、价格）
- 车辆预约（保留15分钟）
- 预约超时自动释放

### 3. 骑行模块
- 扫码开锁（动态二维码）
- 蓝牙近场开锁
- 实时计费（分时段计价）
- 骑行中实时显示费用、时长、里程
- 自动扣费/后付费模式
- 开锁失败自动退款

### 4. 故障上报模块
- 一键拍照上传车辆问题
- 故障类型选择
- 位置自动获取
- 上报后自动派单
- 用户获赠骑行券奖励

### 5. 信用体系模块
- 违规行为扣分（乱停放-20分、破坏车辆-50分）
- 规范行为加分（规范还车+2分、故障上报+5分）
- 信用分<80分提高押金或限制用车
- 信用分≥90分享受优先调度、折扣优惠

## 快速启动

### 后端启动

#### 环境要求
- JDK 11+
- Maven 3.6+
- Redis (可选，用于缓存)

#### 启动步骤

1. **进入后端目录**
```bash
cd backend
```

2. **编译项目**
```bash
mvn clean compile
```

3. **运行项目**
```bash
mvn spring-boot:run
```

或者打包后运行：
```bash
mvn clean package
java -jar target/bike-sharing-backend-1.0.0.jar
```

4. **访问接口文档**
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console

#### 默认测试账号
- 手机号: 13800138001
- 密码: 123456

### 前端启动

#### 环境要求
- Node.js 14+
- HBuilderX 或 微信开发者工具

#### 启动步骤

1. **进入前端目录**
```bash
cd frontend
```

2. **安装依赖**
```bash
npm install
```

3. **运行项目**

使用HBuilderX打开项目，选择运行到：
- 浏览器
- 微信开发者工具
- 手机模拟器

或者使用命令行：
```bash
# 运行到浏览器
npm run dev:h5

# 运行到微信小程序
npm run dev:mp-weixin
```

## API接口说明

### 用户相关接口
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `POST /api/user/realname/verify` - 实名认证
- `POST /api/user/deposit/pay` - 缴纳押金
- `POST /api/user/deposit/refund` - 退还押金
- `POST /api/user/recharge` - 账户充值

### 车辆相关接口
- `GET /api/bike/nearby` - 获取附近车辆
- `POST /api/bike/reserve` - 预约车辆
- `DELETE /api/bike/reserve/{id}` - 取消预约

### 骑行相关接口
- `POST /api/ride/unlock` - 开锁
- `POST /api/ride/lock` - 锁车
- `GET /api/ride/current` - 获取当前骑行信息
- `GET /api/ride/records` - 获取骑行记录

### 故障上报接口
- `POST /api/fault/report` - 上报故障
- `GET /api/fault/my` - 获取我的上报记录

### 优惠券接口
- `GET /api/coupon/my` - 获取我的优惠券
- `POST /api/coupon/use` - 使用优惠券

## 系统配置

### 后端配置 (application.yml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:file:./data/bikedb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
    username: sa
    password:
  redis:
    host: localhost
    port: 6379

jwt:
  secret: bike-sharing-system-secret-key-2024
  expiration: 604800000  # 7天

bike:
  deposit:
    amount: 199.00
    free-credit-score: 650
```

### 前端配置
修改 `utils/request.js` 中的API基础地址：
```javascript
const BASE_URL = 'http://localhost:8080/api'
```

## 测试数据说明

系统启动时会自动初始化以下测试数据：
- 10个测试用户（含不同信用等级）
- 50辆测试车辆（普通单车、电动车、助力车）
- 若干骑行记录、预约记录、优惠券
- 信用记录示例

## 注意事项

1. **Redis可选**: 如果没有Redis服务，系统仍可正常运行，缓存功能会自动降级
2. **H2数据库**: 数据存储在 `backend/data/` 目录下，删除该目录可重置数据
3. **跨域配置**: 后端已配置CORS支持前端跨域访问
4. **移动端适配**: 前端已做响应式适配，支持手机和平板

## 开发说明

### 后端开发规范
- 统一使用Result作为返回结果
- 业务异常使用BusinessException抛出
- 使用MyBatis-Plus简化数据库操作
- 接口添加Swagger注解方便文档生成

### 前端开发规范
- 使用Pinia进行状态管理
- API统一封装在api目录下
- 全局样式在styles/common.scss中定义
- 页面采用组件化开发

## License

MIT License
