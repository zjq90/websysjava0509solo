# 花店APP系统

一个完整的电商花店应用系统，包含后端API和前端移动端。

## 技术栈

### 后端
- **Spring Boot 2.7.18**: 核心框架
- **Spring Data JPA**: 数据持久层
- **H2 Database**: 内存数据库（开发环境）
- **Redis**: 缓存和会话管理
- **Spring Security + JWT**: 安全认证
- **RSA**: 敏感数据加密
- **Swagger OpenAPI**: API文档

### 前端
- **UniApp**: 跨平台移动端框架
- **Vue 3**: 前端框架
- **Vuex**: 状态管理
- **响应式设计**: 适配手机、平板
- **长辈模式**: 字体放大、界面简化

## 项目结构

```
appsysjava45/
├── backend/                    # 后端项目
│   ├── src/main/java/com/flower/
│   │   ├── FlowerShopApplication.java    # 启动类
│   │   ├── config/             # 配置类
│   │   │   ├── SwaggerConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── RedisConfig.java
│   │   ├── entity/             # 实体类
│   │   │   ├── User.java
│   │   │   ├── Product.java
│   │   │   ├── Category.java
│   │   │   ├── Cart.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java
│   │   │   ├── Coupon.java
│   │   │   ├── UserCoupon.java
│   │   │   ├── FlashSale.java
│   │   │   ├── GroupBuy.java
│   │   │   ├── PickupPoint.java
│   │   │   └── PointRecord.java
│   │   ├── repository/         # 数据访问层
│   │   ├── service/            # 业务逻辑层
│   │   ├── controller/         # 控制器层
│   │   ├── utils/              # 工具类
│   │   │   ├── JwtUtil.java
│   │   │   └── RsaUtil.java
│   │   └── DataInitializer.java    # 测试数据初始化
│   └── src/main/resources/
│       ├── application.yml     # 配置文件
│       └── schema.sql          # 数据库脚本
└── frontend/                   # 前端项目
    ├── pages/                  # 页面
    │   ├── index/              # 首页
    │   ├── product/            # 商品列表/详情
    │   ├── cart/               # 购物车
    │   ├── order/              # 订单相关
    │   ├── user/               # 用户中心/登录/注册
    │   └── coupon/             # 优惠券
    ├── store/                  # Vuex状态管理
    ├── utils/                  # 工具类
    ├── static/                 # 静态资源
    ├── App.vue                 # 根组件
    ├── main.js                 # 入口文件
    ├── manifest.json           # 应用配置
    └── pages.json              # 路由配置
```

## 功能特性

### 1. 用户模块
- 用户注册/登录
- JWT令牌认证
- 个人信息管理
- 积分系统

### 2. 商品模块
- 商品分类浏览
- 商品详情查看
- 商品搜索
- 限时秒杀

### 3. 购物车
- 添加/删除商品
- 修改商品数量
- 多商品合并下单
- 实时计算总价

### 4. 订单模块
- 创建订单
- 订单状态追踪（待付款→制作中→配送中→已完成）
- 修改/取消订单（发货前）
- 订单详情查看

### 5. 支付方式
- 微信支付
- 支付宝
- 银联卡
- 分期付款
- 优惠券抵扣
- 积分兑换

### 6. 配送选项
- 同城急送（1/2/3小时达，按距离加收运费）
- 预约配送（可选具体日期和时间）
- 自提点（线下门店导航，到店核销）

### 7. 活动营销
- **优惠券系统**
  - 满减券
  - 折扣券
  - 新人专享券
  - 分享裂变
- **限时秒杀**：每日固定时段特价商品
- **拼团活动**：3人成团享折扣，团长额外奖励积分
- **社交分享**：分享商品/订单至朋友圈获积分奖励

### 8. 安全特性
- RSA加密传输敏感信息（身份证、手机号、支付数据）
- BCrypt密码加密
- JWT无状态认证

### 9. 用户体验
- 响应式设计（适配手机、平板）
- 长辈模式（字体放大、界面简化）
- 语音输入支持

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 8+
   - Maven 3.6+
   - Redis (可选，默认使用内存缓存)

2. **启动步骤**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **访问地址**
   - 应用地址：http://localhost:8080
   - H2控制台：http://localhost:8080/h2-console
   - Swagger文档：http://localhost:8080/swagger-ui.html

4. **默认测试账号**
   - 管理员：admin / admin123
   - 普通用户：user / user123

### 前端启动

1. **环境要求**
   - Node.js 14+
   - HBuilderX 或 UniApp CLI

2. **启动步骤**
   ```bash
   cd frontend
   npm install
   # 使用HBuilderX打开项目，选择运行到浏览器或模拟器
   ```

3. **API配置**
   - 修改 `frontend/utils/request.js` 中的 `baseUrl` 为实际后端地址

## API接口说明

### 用户相关
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息

### 商品相关
- `GET /api/product/list` - 商品列表
- `GET /api/product/{id}` - 商品详情
- `GET /api/product/category/{categoryId}` - 分类商品
- `GET /api/product/search` - 商品搜索

### 购物车相关
- `GET /api/cart/list` - 购物车列表
- `POST /api/cart/add` - 添加商品到购物车
- `PUT /api/cart/update` - 更新购物车商品
- `DELETE /api/cart/delete/{id}` - 删除购物车商品

### 订单相关
- `POST /api/order/create` - 创建订单
- `GET /api/order/list` - 订单列表
- `GET /api/order/{id}` - 订单详情
- `PUT /api/order/cancel/{id}` - 取消订单
- `PUT /api/order/status/{id}` - 更新订单状态

### 活动相关
- `GET /api/activity/coupons` - 优惠券列表
- `GET /api/activity/user-coupons` - 用户优惠券
- `POST /api/activity/receive-coupon` - 领取优惠券
- `GET /api/activity/flash-sales` - 限时秒杀
- `GET /api/activity/group-buys` - 拼团活动

## 数据库设计

### 核心表结构
- **user**: 用户表（id, username, password, nickname, phone, points, status）
- **category**: 商品分类表（id, name, icon, sort_order）
- **product**: 商品表（id, name, description, price, original_price, stock, image, category_id, status）
- **cart**: 购物车表（id, user_id, product_id, quantity, selected）
- **order**: 订单表（id, order_no, user_id, total_amount, discount_amount, payment_method, delivery_type, status, receiver_name, receiver_phone, receiver_address, remark）
- **order_item**: 订单项表（id, order_id, product_id, product_name, product_image, price, quantity）
- **coupon**: 优惠券表（id, name, coupon_type, discount_value, min_amount, total_count, used_count, valid_start_time, valid_end_time, status）
- **user_coupon**: 用户优惠券表（id, user_id, coupon_id, status, used_time, receive_time）
- **flash_sale**: 限时秒杀表（id, product_id, sale_price, stock, start_time, end_time, status）
- **group_buy**: 拼团活动表（id, product_id, group_price, required_people, current_people, start_time, end_time, status）
- **pickup_point**: 自提点表（id, name, address, phone, latitude, longitude, business_hours）
- **point_record**: 积分记录表（id, user_id, points, type, description, create_time）

## 测试数据

系统启动时会自动初始化以下测试数据：
- 2个测试用户（admin / user）
- 5个商品分类
- 20个示例商品
- 5种优惠券
- 3个限时秒杀活动
- 2个拼团活动
- 3个自提点

## 注意事项

1. **生产环境配置**
   - 更换RSA密钥对
   - 配置真实的Redis服务
   - 更换为MySQL/PostgreSQL等生产数据库
   - 配置HTTPS

2. **第三方服务对接**
   - 微信支付/支付宝支付需要申请对应商户账号
   - 地图导航API（如高德/百度地图）
   - 短信服务（用于验证码等）

3. **前端部署**
   - H5：部署到Web服务器
   - 小程序：提交到微信/支付宝等平台审核
   - APP：打包成APK/IPA提交应用商店

## 开发说明

- 后端代码遵循RESTful API设计规范
- 前端采用组件化开发，页面结构清晰
- 所有接口都有详细的Swagger文档
- 代码包含详细的中文注释

## License

MIT License
