# 文物收藏APP - Heritage Collection

一个专业的文物收藏交流与交易平台，包含社区交流、专家问答、线下活动、交易市场、拍卖专区、捐赠通道等功能。

## 技术栈

### 后端技术
- **框架**: Spring Boot 2.7.x
- **数据库**: H2 (嵌入式数据库，支持持久化)
- **缓存**: Redis
- **API文档**: Swagger/OpenAPI 3
- **工具库**: Hutool
- **加密**: RSA非对称加密

### 前端技术
- **框架**: Vue 3 + Vite (高性能开发体验)
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **UI组件**: Element Plus
- **样式**: SCSS
- **响应式设计**: 适配手机、平板
- **长辈模式**: 内置适老化设计

## 核心功能

### 1. 社区交流
- 支持图文帖子发布
- AI内容审核，检测非法交易信息
- 帖子点赞、评论功能
- 置顶帖子、热门推荐

### 2. 藏友圈
- 用户关注系统
- 动态信息流
- 私信功能

### 3. 专家问答
- 专家认证入驻
- 付费咨询服务
- 支持匿名提问
- 评分评价系统

### 4. 线下活动
- 鉴宝会、展览、讲座报名
- 活动日历
- 签到功能
- 活动相册

### 5. 交易市场
- 文物发布与浏览
- **实名认证** + **人脸识别**
- **资金监管**系统
- 合法流转登记（需文物部门许可）
- 订单管理

### 6. 拍卖专区
- 限时竞价拍卖
- 实时出价提醒
- 拍卖状态追踪

### 7. 捐赠通道
- 对接博物馆
- 捐赠申请与审核
- 证书颁发

## 特色功能

### 适老化设计
- **长辈模式**: 字体放大、界面简化
- **语音输入**: 支持语音发布内容
- **语音登录**: 长辈专属登录方式
- **大按钮设计**: 便于操作

### 安全保障
- **RSA加密**: 敏感数据加密传输存储
- **实名认证**: 交易必备条件
- **人脸识别**: 身份二次验证
- **资金监管**: 冻结非法所得，保障交易安全

## 项目结构

```
appsysjava53/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven配置
│   └── src/main/
│       ├── java/com/heritage/
│       │   ├── controller/           # 控制器层
│       │   │   ├── UserController.java
│       │   │   └── CommunityController.java
│       │   ├── service/              # 服务层
│       │   │   ├── UserService.java
│       │   │   ├── CommunityService.java
│       │   │   └── AIReviewService.java
│       │   ├── repository/           # 数据访问层
│       │   │   ├── UserRepository.java
│       │   │   ├── CommunityPostRepository.java
│       │   │   └── HeritageItemRepository.java
│       │   ├── entity/               # 实体类
│       │   │   ├── User.java
│       │   │   ├── HeritageItem.java
│       │   │   ├── CommunityPost.java
│       │   │   ├── ExpertQuestion.java
│       │   │   ├── OfflineActivity.java
│       │   │   ├── TradeOrder.java
│       │   │   ├── Auction.java
│       │   │   ├── Donation.java
│       │   │   └── ...
│       │   ├── config/               # 配置类
│       │   │   └── RedisConfig.java
│       │   ├── util/                 # 工具类
│       │   │   └── RSAUtil.java
│       │   └── HeritageApplication.java  # 启动类
│       └── resources/
│           ├── application.yml       # 应用配置
│           └── schema.sql            # 数据库初始化脚本
│
├── frontend/                          # 前端H5项目 (Vue3 + Vite)
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── router/index.js
│       ├── store/app.js
│       ├── layouts/Layout.vue
│       ├── styles/
│       ├── views/
│       │   ├── Home.vue              # 首页
│       │   ├── Login.vue             # 登录
│       │   ├── Community.vue         # 社区
│       │   ├── Market.vue            # 交易市场
│       │   ├── Activity.vue          # 线下活动
│       │   ├── Mine.vue              # 个人中心
│       │   ├── PostDetail.vue        # 帖子详情
│       │   └── HeritageDetail.vue    # 文物详情
│
└── README.md
```

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- Redis 6.0+
- Node.js 16+

### 后端启动

1. 启动Redis服务（默认端口6379）

2. 进入后端目录：
   ```bash
   cd backend
   ```

3. 编译并启动：
   ```bash
   mvn spring-boot:run
   ```

### 访问地址

- **后端服务**: http://localhost:8080/api
- **Swagger文档**: http://localhost:8080/api/swagger-ui.html
- **H2控制台**: http://localhost:8080/api/h2-console

### 前端启动

1. 进入前端目录：
   ```bash
   cd frontend
   ```

2. 安装依赖：
   ```bash
   npm install
   ```

3. 开发模式运行：
   ```bash
   npm run dev
   ```

4. 访问地址：http://localhost:5173

## API接口

### 用户模块
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/{id}` - 获取用户信息
- `PUT /api/user/update` - 更新用户信息
- `POST /api/user/elder-mode` - 切换长辈模式
- `GET /api/user/experts` - 获取专家列表

### 社区模块
- `POST /api/community/post` - 发布帖子（AI自动审核）
- `GET /api/community/posts` - 获取帖子列表
- `GET /api/community/post/{id}` - 获取帖子详情
- `POST /api/community/post/{id}/like` - 点赞帖子
- `DELETE /api/community/post/{id}` - 删除帖子

## 数据库设计

核心数据表：
- `sys_user` - 用户表
- `heritage_item` - 文物表
- `community_post` - 社区帖子表
- `expert_question` - 专家问答表
- `offline_activity` - 线下活动表
- `trade_order` - 交易订单表
- `auction` - 拍卖表
- `donation` - 捐赠表
- `museum` - 博物馆表
- `ai_review_log` - AI审核记录表

## 测试数据

项目预置了测试数据，包括：
- 测试用户（管理员、专家、普通用户）
- 测试文物（瓷器、青铜器、书画、玉器）
- 社区帖子
- 线下活动
- 拍卖商品

## 安全说明

1. **敏感数据加密**: 用户手机号、身份证号等敏感信息采用RSA加密存储
2. **交易安全**: 所有交易需要实名认证和人脸识别双重验证
3. **资金监管**: 交易资金接受监管，可冻结异常订单
4. **AI审核**: 社区内容通过AI自动审核，防止非法交易信息

## 响应式设计

- 适配手机端
- 适配平板端
- 长辈模式专属适配

## 开发团队

- 后端开发: Java/Spring Boot
- 前端开发: Vue.js/uni-app

## 版本历史

- v1.0.0 - 初始版本，包含核心功能

## 许可证

本项目仅供学习交流使用。
