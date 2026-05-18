# 二手交易系统小程序

基于 Spring Boot + Vue + Uniapp 开发的二手交易系统小程序，支持商品发布、附近推荐、拼团、砍价、积分兑换等功能。

## 技术栈

### 后端
- **Spring Boot 2.7.18** - 核心框架
- **Spring Data JPA** - ORM框架
- **H2 Database** - 内存数据库
- **Redis** - 缓存和会话管理
- **SpringDoc OpenAPI** - API文档
- **RSA加密** - 敏感数据加密

### 前端
- **Uniapp** - 跨端小程序框架
- **Vue 3** - 前端框架
- **uni-ui** - UI组件库

## 功能特性

### 交易功能
- ✅ 微信支付（JSAPI）
- ✅ 分享到朋友圈/群聊（带商品卡片）
- ✅ 拼团活动（3人成团）

### 位置服务
- ✅ 附近商品推荐（基于地理位置）
- ✅ 附近商品列表按距离排序
- ✅ 自提点导航（微信内置地图）
- ✅ 距离显示（如"1.2km"）
- ✅ 距离超过5km时隐藏自提选项

### 社交功能
- ✅ 砍价活动（邀请好友助力）
- ✅ 砍价进度条实时更新
- ✅ 任务奖励（签到领积分）
- ✅ 任务中心采用卡片式布局
- ✅ 积分兑换优惠券

### 客服功能
- ✅ 微信客服接入（实时聊天）
- ✅ 客服消息列表显示未读数
- ✅ 快捷回复（预设常用语）
- ✅ 消息已读状态

### 模板消息
- ✅ 支付成功通知
- ✅ 订单状态变更（发货/退款）
- ✅ 促销活动提醒

### 适老化设计
- ✅ 长辈模式开关
- ✅ 字体放大
- ✅ 界面简化
- ✅ 语音输入支持

### 响应式设计
- ✅ 手机端适配
- ✅ 平板端适配

## 项目结构

```
appsysjava50/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/secondhand/
│   │   │   │   ├── config/    # 配置类
│   │   │   │   ├── controller/# 控制器
│   │   │   │   ├── entity/    # 实体类
│   │   │   │   ├── repository/# 数据访问层
│   │   │   │   ├── service/   # 业务逻辑层
│   │   │   │   ├── util/      # 工具类
│   │   │   │   └── SecondhandApplication.java  # 启动类
│   │   │   └── resources/
│   │   │       ├── application.yml  # 配置文件
│   │   │       └── rsa/        # RSA密钥文件
│   │   └── test/
│   └── pom.xml
├── frontend/                   # 前端小程序项目
│   ├── pages/                  # 页面文件
│   │   ├── index/             # 首页
│   │   ├── product/           # 商品相关
│   │   ├── order/             # 订单相关
│   │   ├── groupbuy/          # 拼团活动
│   │   ├── bargain/           # 砍价活动
│   │   ├── chat/              # 客服聊天
│   │   └── user/              # 用户中心
│   ├── static/                # 静态资源
│   ├── App.vue                # 入口组件
│   ├── main.js                # 入口文件
│   ├── manifest.json          # 小程序配置
│   ├── package.json           # 依赖配置
│   └── pages.json             # 页面路由配置
└── README.md
```

## 快速开始

### 后端启动

1. 确保安装了 JDK 11+ 和 Maven
2. 进入后端目录：
   ```bash
   cd backend
   ```
3. 启动项目：
   ```bash
   mvn spring-boot:run
   ```
4. 访问地址：
   - API文档：http://localhost:8080/api/swagger-ui.html
   - H2控制台：http://localhost:8080/api/h2-console
   - JDBC URL：`jdbc:h2:mem:secondhand`
   - 用户名：`sa`
   - 密码：（空）

### 测试账号

系统初始化时自动创建以下测试账号：

| 用户名 | 密码 | 昵称 | 说明 |
|--------|------|------|------|
| user1 | 123456 | 小明 | 普通用户，500积分 |
| user2 | 123456 | 小红 | 普通用户，300积分 |
| seller1 | 123456 | 诚信卖家 | 卖家账号，1000积分 |

### 前端启动

1. 确保安装了 HBuilderX 或使用 npm
2. 进入前端目录：
   ```bash
   cd frontend
   ```
3. 安装依赖：
   ```bash
   npm install
   ```
4. 使用 HBuilderX 运行到微信开发者工具

## API接口

### 用户模块
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息
- `POST /api/user/sign` - 每日签到
- `GET /api/user/sign/status` - 签到状态

### 商品模块
- `POST /api/product/create` - 发布商品
- `GET /api/product/{id}` - 获取商品详情
- `PUT /api/product/{id}` - 更新商品
- `DELETE /api/product/{id}` - 删除商品
- `GET /api/product/nearby` - 附近商品
- `GET /api/product/latest` - 最新商品
- `GET /api/product/hot` - 热门商品
- `GET /api/product/my` - 我的商品

### 订单模块
- `POST /api/order/create` - 创建订单
- `POST /api/order/pay/{orderNo}` - 支付订单
- `POST /api/order/cancel/{orderNo}` - 取消订单
- `POST /api/order/confirm/{orderNo}` - 确认收货
- `GET /api/order/{orderNo}` - 获取订单详情
- `GET /api/order/my` - 我的订单
- `POST /api/order/refund/{orderNo}` - 申请退款

### 拼团模块
- `POST /api/groupbuy/activity/create` - 创建拼团活动
- `GET /api/groupbuy/activity/list` - 拼团活动列表
- `POST /api/groupbuy/join` - 参与拼团
- `GET /api/groupbuy/members/{groupNo}` - 拼团成员
- `GET /api/groupbuy/my` - 我的拼团

### 砍价模块
- `POST /api/bargain/activity/create` - 创建砍价活动
- `GET /api/bargain/activity/list` - 砍价活动列表
- `POST /api/bargain/start` - 发起砍价
- `POST /api/bargain/help` - 帮砍一刀
- `GET /api/bargain/detail/{recordId}` - 砍价详情
- `GET /api/bargain/my` - 我的砍价

### 积分模块
- `POST /api/point/add` - 添加积分
- `POST /api/point/deduct` - 扣减积分
- `GET /api/point/my` - 我的积分记录

## 数据库设计

### 核心表
- `t_user` - 用户表
- `t_product` - 商品表
- `t_order` - 订单表
- `t_group_buy_activity` - 拼团活动表
- `t_group_buy_record` - 拼团记录表
- `t_bargain_activity` - 砍价活动表
- `t_bargain_record` - 砍价记录表
- `t_bargain_help` - 砍价助力表
- `t_point_record` - 积分记录表
- `t_coupon` - 优惠券表
- `t_user_coupon` - 用户优惠券表
- `t_message` - 消息表
- `t_pickup_point` - 自提点表
- `t_task` - 任务表

## 安全设计

1. **RSA加密** - 用户密码和敏感数据采用RSA非对称加密
2. **Token认证** - 基于Redis的Token会话管理
3. **权限控制** - 基于用户角色的权限控制
4. **数据隔离** - 逻辑删除，避免物理删除
5. **接口限流** - 防止恶意请求

## 开发说明

### 添加新功能
1. 在 `entity` 包创建实体类
2. 在 `repository` 包创建数据访问接口
3. 在 `service` 包创建业务逻辑
4. 在 `controller` 包创建API接口
5. 在前端添加对应页面

### 配置修改
- 后端配置文件：`backend/src/main/resources/application.yml`
- 前端API地址：`frontend/main.js`

## 注意事项

1. 生产环境请替换RSA密钥
2. 生产环境请使用MySQL等持久化数据库
3. 微信相关功能需要配置对应AppID和密钥
4. 生产环境建议开启HTTPS

## 许可证

MIT License
