# 二手交易系统小程序 - 完整项目说明

## 📋 项目概述

基于 Spring Boot + Vue + Uniapp 开发的完整二手交易系统小程序，包含商品发布、附近推荐、拼团砍价、积分兑换、在线客服等核心功能。

## 🛠 技术栈

### 后端技术
- **Spring Boot 2.7.18** - 核心框架
- **Spring Data JPA** - ORM框架
- **H2 Database** - 内存数据库
- **Redis** - 缓存与会话管理
- **SpringDoc OpenAPI** - API文档生成
- **RSA加密** - 敏感数据安全

### 前端技术
- **Uniapp / Vue 3** - 跨端小程序框架
- **CSS3 / Flex布局** - 响应式设计

## 📁 项目结构

```
appsysjava50/
├── backend/                           # 后端项目
│   ├── pom.xml                       # Maven依赖配置
│   └── src/main/java/com/secondhand/
│       ├── SecondhandApplication.java  # 启动类
│       ├── config/                    # 配置类
│       │   ├── SwaggerConfig.java    # Swagger配置
│       │   ├── RedisConfig.java      # Redis配置
│       │   └── DataInitializer.java  # 测试数据初始化
│       ├── controller/                # 控制器层
│       │   ├── UserController.java   # 用户管理
│       │   ├── ProductController.java # 商品管理
│       │   ├── OrderController.java   # 订单管理
│       │   ├── GroupBuyController.java # 拼团管理
│       │   └── BargainController.java # 砍价管理
│       ├── service/                   # 业务逻辑层
│       │   ├── UserService.java      # 用户服务
│       │   ├── ProductService.java   # 商品服务
│       │   ├── OrderService.java     # 订单服务
│       │   ├── GroupBuyService.java  # 拼团服务
│       │   ├── BargainService.java   # 砍价服务
│       │   └── PointService.java     # 积分服务
│       ├── repository/               # 数据访问层
│       ├── entity/                   # 实体类
│       └── util/                     # 工具类
│           ├── RSAUtil.java          # RSA加密工具
│           ├── DistanceUtil.java     # 距离计算工具
│           └── KeyGenerator.java     # 密钥生成器
│
└── frontend/                         # 前端小程序项目
    ├── pages.json                    # 页面路由配置
    ├── manifest.json                 # 小程序配置
    ├── package.json                  # 依赖配置
    ├── App.vue                       # 入口组件
    ├── main.js                       # 入口文件
    ├── static/css/
    │   └── common.css                # 公共样式
    └── pages/                        # 页面文件
        ├── index/index.vue           # 首页
        ├── nearby/index.vue          # 附近商品
        ├── product/
        │   ├── detail.vue            # 商品详情
        │   └── publish.vue           # 发布商品
        ├── order/
        │   ├── list.vue              # 订单列表
        │   └── detail.vue            # 订单详情
        ├── groupbuy/index.vue        # 拼团活动
        ├── bargain/index.vue         # 砍价活动
        ├── coupon/index.vue          # 优惠券中心
        ├── chat/index.vue            # 在线客服
        └── user/
            ├── login.vue             # 登录页
            ├── index.vue             # 个人中心
            └── points.vue            # 积分中心
```

## ✨ 功能特性

### 1. 核心交易功能
- ✅ 商品发布与浏览
- ✅ 商品搜索功能
- ✅ 下单购买流程
- ✅ 订单状态管理
- ✅ 支付/取消/退款

### 2. 地理位置服务
- ✅ 附近商品推荐
- ✅ 距离实时计算（Haversine公式）
- ✅ 5公里范围筛选
- ✅ 距离排序展示
- ✅ 地图导航功能

### 3. 拼团功能
- ✅ 拼团活动列表
- ✅ 3人成团机制
- ✅ 开团/参团操作
- ✅ 拼团进度展示
- ✅ 我的拼团记录

### 4. 砍价功能
- ✅ 砍价活动列表
- ✅ 发起砍价功能
- ✅ 好友助力砍价
- ✅ 随机金额计算
- ✅ 砍价进度展示

### 5. 积分系统
- ✅ 每日签到领积分
- ✅ 发布商品获积分
- ✅ 完成订单获积分
- ✅ 积分兑换优惠券
- ✅ 积分记录查询

### 6. 优惠券系统
- ✅ 优惠券展示列表
- ✅ 积分兑换优惠券
- ✅ 我的优惠券管理
- ✅ 满减优惠规则

### 7. 在线客服
- ✅ 实时聊天界面
- ✅ 快捷回复功能
- ✅ 常见问题自动回复
- ✅ 消息时间显示

### 8. 适老化设计
- ✅ 长辈模式开关
- ✅ 字体放大功能
- ✅ 简化界面布局
- ✅ 大按钮设计

## 🚀 快速开始

### 后端启动

1. **环境要求**
   - JDK 11+
   - Maven 3.6+
   - Redis (可选，无Redis时使用内存缓存)

2. **启动步骤**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **访问地址**
   - API文档：http://localhost:8080/api/swagger-ui.html
   - H2控制台：http://localhost:8080/api/h2-console
     - JDBC URL: `jdbc:h2:mem:secondhand`
     - 用户名: `sa`
     - 密码: (空)

4. **测试账号**
   | 用户名 | 密码 | 昵称 | 说明 |
   |--------|------|------|------|
   | user1 | 123456 | 小明 | 普通用户，500积分 |
   | user2 | 123456 | 小红 | 普通用户，300积分 |
   | seller1 | 123456 | 诚信卖家 | 卖家账号，1000积分 |

### 前端启动

1. **环境要求**
   - HBuilderX 3.0+
   - 微信开发者工具

2. **启动步骤**
   - 使用HBuilderX打开 `frontend` 目录
   - 配置小程序AppID
   - 运行到微信开发者工具

## 📱 页面说明

### 首页 (pages/index/index)
- 轮播Banner展示活动
- 快捷功能入口导航
- 热门商品推荐列表
- 最新上架商品展示

### 附近商品 (pages/nearby/index)
- 地理位置定位功能
- 距离范围选择器
- 商品按距离排序
- 距离标签展示

### 商品详情 (pages/product/detail)
- 商品图片轮播
- 价格与描述展示
- 卖家信息与联系方式
- 立即购买按钮
- 地图导航功能

### 发布商品 (pages/product/publish)
- 商品信息填写表单
- 图片上传功能
- 分类与成色选择
- 地理位置设置

### 订单列表 (pages/order/list)
- 订单状态Tab切换
- 订单卡片展示
- 取消/支付/确认收货操作
- 退款申请功能

### 拼团活动 (pages/groupbuy/index)
- 拼团活动列表
- 拼团进度展示
- 参团/开团按钮
- 我的拼团记录

### 砍价活动 (pages/bargain/index)
- 砍价活动列表
- 砍价进度条
- 发起砍价操作
- 邀请好友助力

### 积分中心 (pages/user/points)
- 当前积分展示
- 每日签到按钮
- 任务列表与说明
- 积分记录查询

### 优惠券中心 (pages/coupon/index)
- 可兑换优惠券列表
- 优惠券面额与条件
- 积分兑换操作
- 我的优惠券展示

### 在线客服 (pages/chat/index)
- 聊天对话界面
- 快捷回复选项
- 自动回复机器人
- 消息时间戳显示

### 个人中心 (pages/user/index)
- 用户信息与头像
- 快捷入口导航
- 长辈模式开关
- 退出登录功能

## 🔧 API接口说明

### 用户模块
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息
- `POST /api/user/sign` - 每日签到

### 商品模块
- `POST /api/product/create` - 发布商品
- `GET /api/product/{id}` - 商品详情
- `PUT /api/product/{id}` - 更新商品
- `DELETE /api/product/{id}` - 删除商品
- `GET /api/product/nearby` - 附近商品列表
- `GET /api/product/latest` - 最新商品
- `GET /api/product/hot` - 热门商品

### 订单模块
- `POST /api/order/create` - 创建订单
- `POST /api/order/pay/{orderNo}` - 支付订单
- `POST /api/order/cancel/{orderNo}` - 取消订单
- `POST /api/order/confirm/{orderNo}` - 确认收货
- `POST /api/order/refund/{orderNo}` - 申请退款
- `GET /api/order/{orderNo}` - 订单详情
- `GET /api/order/my` - 我的订单

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
- `POST /api/bargain/help` - 助力砍价
- `GET /api/bargain/detail/{recordId}` - 砍价详情
- `GET /api/bargain/my` - 我的砍价

## 📊 数据库设计

### 核心表结构
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

## 🔐 安全设计

1. **RSA非对称加密**
   - 用户密码加密存储
   - 敏感数据传输加密
   - 公钥加密、私钥解密

2. **Token会话管理**
   - 基于Redis的Token认证
   - Token过期自动失效
   - 单点登录支持

3. **数据隔离**
   - 逻辑删除标记
   - 用户数据权限控制
   - 操作日志记录

## 🎨 设计特色

### 视觉设计
- 渐变色按钮与卡片
- 圆角设计风格
- 统一的间距与字体规范
- 舒适的配色方案

### 交互体验
- 底部固定支付按钮
- 快捷回复悬浮栏
- 距离标签位置提示
- 长辈模式一键切换
- 空状态友好提示

## 📱 响应式适配

- ✅ 手机端完美适配
- ✅ 平板端适配
- ✅ 不同分辨率兼容
- ✅ 长辈模式大字体适配

## 🔄 测试数据

系统启动时自动初始化测试数据，包括：
- 3个测试用户账号
- 5个测试商品（手机、电脑、鞋子、净化器、游戏机）
- 3个自提点位置
- 3种优惠券配置
- 每日签到任务

## 📝 开发说明

### 添加新功能步骤
1. 在后端创建对应Entity实体类
2. 创建Repository数据访问接口
3. 实现Service业务逻辑
4. 创建Controller接口层
5. 在前端创建对应页面
6. 配置页面路由与导航

### 接口调用规范
- 统一使用 `/api` 前缀
- 请求头携带Authorization Token
- 返回格式统一为JSON
- 错误码与消息统一规范

## 🎯 项目亮点

1. **完整的业务闭环** - 从商品发布到交易完成的完整流程
2. **地理位置服务** - 基于经纬度的距离计算与附近推荐
3. **社交电商功能** - 拼团、砍价等社交裂变功能
4. **积分激励体系** - 签到、任务、兑换完整积分生态
5. **适老化设计** - 一键切换长辈模式，贴心设计
6. **安全加密** - RSA加密保障数据安全
7. **响应式设计** - 多端适配体验
8. **详细API文档** - 前后端对接效率高

## 📄 许可证

MIT License

---

**项目完成时间**: 2026年  
**开发团队**: Solo Java  
**版本**: v1.0.0
