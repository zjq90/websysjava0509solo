# 二手交易系统 App

基于 Spring Boot + UniApp 开发的二手交易平台，支持商品发布、搜索、交易、社区互动等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.x
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **API文档**: Swagger OpenAPI 3
- **ORM**: Spring Data JPA
- **工具库**: Hutool

### 前端
- **框架**: UniApp (Vue 3)
- **状态管理**: Vuex
- **UI组件**: 原生UniApp组件

## 功能特性

### 1. 商品模块
- ✅ 商品发布（支持多图上传、裁剪、滤镜）
- ✅ 图片上传进度显示
- ✅ 标题/描述编辑（500字限制）
- ✅ 三级分类选择（树形弹窗，支持搜索）
- ✅ 关键词搜索（模糊匹配 + 实时联想）
- ✅ 筛选条件（价格区间、成色、品牌）
- ✅ 排序方式（热度/价格/距离）

### 2. 交易模块
- ✅ 担保支付（微信/支付宝）
- ✅ 支付页面显示订单金额、卖家信息
- ✅ 物流跟踪（对接顺丰/菜鸟）
- ✅ 物流信息自动刷新（2小时）
- ✅ 自提点导航（高德地图）

### 3. 消息中心
- ✅ 消息列表按时间倒序
- ✅ 未读消息红点提示
- ✅ 系统通知（订单状态变更）
- ✅ 交易消息（买家询价、卖家回复）
- ✅ 推送设置开关

### 4. 用户中心
- ✅ 信用分展示（满分5分，星级可视化）
- ✅ 历史评价（支持图片查看）
- ✅ 实名认证（人脸识别，分步引导）
- ✅ 个人资料编辑

### 5. 社区互动
- ✅ 热门话题卡片展示
- ✅ 二手经验分享（图文发布）
- ✅ 话题讨论区
- ✅ 点赞/评论功能
- ✅ 评论支持@用户

### 6. 特殊设计
- ✅ 响应式设计（适配手机、平板）
- ✅ 适老化设计（长辈模式：字体放大、界面简化）
- ✅ 语音输入支持
- ✅ RSA加密保护敏感数据

## 项目结构

```
appsysjava49/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/secondhand/
│   │   │   │   ├── entity/    # 实体类
│   │   │   │   ├── repository/ # 数据访问层
│   │   │   │   ├── service/   # 业务逻辑层
│   │   │   │   ├── controller/ # 控制层
│   │   │   │   ├── config/    # 配置类
│   │   │   │   ├── util/      # 工具类
│   │   │   │   └── common/    # 公共类
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── pom.xml
│
├── frontend/                   # 前端项目
│   ├── pages/                  # 页面文件
│   ├── store/                  # Vuex状态管理
│   ├── utils/                  # 工具类
│   ├── App.vue
│   ├── main.js
│   ├── pages.json
│   ├── manifest.json
│   └── package.json
│
└── README.md
```

## 快速开始

### 后端启动

1. **环境要求**:
   - JDK 1.8+
   - Maven 3.6+
   - Redis (可选，用于缓存)

2. **启动步骤**:
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **访问地址**:
   - API: http://localhost:8080/api
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2控制台: http://localhost:8080/api/h2-console

4. **测试账号**:
   - 用户名: admin / 密码: 123456
   - 用户名: test / 密码: 123456

### 前端启动

1. **环境要求**:
   - Node.js 14+
   - HBuilderX (推荐)

2. **启动步骤**:
   ```bash
   cd frontend
   npm install
   # 使用HBuilderX打开项目，选择运行到浏览器或小程序
   ```

## 数据库设计

### 核心表
- **sys_user**: 用户表
- **sys_user_auth**: 用户认证表
- **product**: 商品表
- **product_category**: 商品分类表
- **sys_order**: 订单表
- **sys_message**: 消息表
- **community_post**: 社区帖子表
- **community_comment**: 评论表
- **community_topic**: 话题表
- **user_review**: 评价表
- **logistics**: 物流表
- **pickup_point**: 自提点表

## API接口示例

### 用户模块
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/{id}` - 获取用户信息
- `PUT /api/user/{id}` - 更新用户信息

### 商品模块
- `POST /api/product` - 发布商品
- `GET /api/product/{id}` - 商品详情
- `GET /api/product/search` - 搜索商品
- `DELETE /api/product/{id}` - 删除商品

### 订单模块
- `POST /api/order` - 创建订单
- `GET /api/order/{id}` - 订单详情
- `POST /api/order/pay` - 支付订单

## 测试数据

系统启动时会自动初始化以下测试数据：
- 2个测试用户 (admin/test)
- 8个一级分类 + 多个二级分类
- 10个测试商品

## 开发说明

### 后端开发
1. 新增实体类需添加 `@Entity` 注解
2. Repository 接口继承 `JpaRepository` 和 `JpaSpecificationExecutor`
3. Controller 统一返回 `Result<T>` 格式
4. 使用 `@Tag` 和 `@Operation` 注解添加Swagger文档

### 前端开发
1. 页面文件放在 `pages/` 目录
2. 在 `pages.json` 中注册页面路由
3. 使用 Vuex 管理全局状态
4. API 请求通过 `utils/request.js` 统一处理

## 特色功能说明

### 长辈模式
- 字体放大 30%
- 按钮和点击区域增大
- 界面简化，隐藏高级功能
- 语音输入支持

### RSA加密
- 用户敏感信息加密存储
- 接口请求签名验证
- 支付数据加密传输

### 智能推荐
- 基于用户浏览历史推荐商品
- 基于地理位置推荐附近商品
- 热门话题实时更新

## 许可证

MIT License
