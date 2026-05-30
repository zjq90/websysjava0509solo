# 游戏Web平台核心模块

基于 Spring Boot + H2 + Vue 的前后端分离游戏Web平台核心模块功能开发。

## 项目结构

```
websysjava83/
├── backend/                 # 后端项目（Spring Boot）
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gameplatform/
│   │   │   │   ├── GamePlatformApplication.java    # 主启动类
│   │   │   │   ├── common/                        # 通用类
│   │   │   │   ├── config/                        # 配置类
│   │   │   │   ├── controller/                    # 控制器
│   │   │   │   ├── dto/                           # 数据传输对象
│   │   │   │   ├── entity/                        # 实体类
│   │   │   │   ├── repository/                    # 数据访问层
│   │   │   │   └── service/                       # 业务逻辑层
│   │   │   └── resources/
│   │   │       ├── application.yml                 # 应用配置
│   │   │       └── data.sql                        # 初始化数据
│   │   └── test/
│   └── pom.xml
└── frontend/                # 前端项目（Vue）
    ├── src/
    │   ├── api/             # API接口
    │   ├── router/          # 路由配置
    │   ├── store/           # 状态管理
    │   ├── views/           # 页面组件
    │   ├── App.vue          # 根组件
    │   └── main.js          # 入口文件
    ├── public/
    ├── package.json
    ├── vue.config.js
    └── babel.config.js
```

## 功能模块

### 1. 数据统计与分析

#### 整体数据
- **用户数据**：日活/月活、新增用户、留存率（次日/7日/30日）、用户地域分布热力图
- **游戏数据**：游戏启动次数、平均游玩时长、游戏人均游玩次数TOP50排行榜
- **收入数据**：广告总收入、会员收入、收入趋势图
- 支持按小时/天/周/月聚合数据
- 支持自定义日期范围对比

#### 游戏单品数据
- 搜索并选择某一款游戏，查看其专属数据
  - 启动次数趋势、平均游玩时长、留存情况
  - 用户来源渠道分析（直接访问/搜索/推荐位/外部分享）
  - 付费转化数据（广告点击率、内购转化率）
- 支持最多5款游戏同时对比关键指标

### 2. 系统配置
- 站点名称、Logo、版权信息、客服联系方式、社交媒体链接
- SEO配置：首页标题（Title）、描述（Description）、关键词（Keywords）

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database（内存数据库）
- Lombok
- Validation

### 前端
- Vue 2.6.14
- Vue Router 3.5.1
- Vuex 3.6.2
- Element UI 2.15.6
- ECharts 5.2.2
- Axios 0.24.0

## 快速开始

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 使用Maven编译并运行：
```bash
mvn clean install
mvn spring-boot:run
```

3. 后端服务将在 `http://localhost:8080/api` 启动

4. H2控制台访问：`http://localhost:8080/api/h2-console`
   - JDBC URL: `jdbc:h2:mem:gameplatform`
   - 用户名: `sa`
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

3. 启动开发服务器：
```bash
npm run serve
```

4. 前端服务将在 `http://localhost:8081` 启动

## API接口文档

### 数据统计接口

- `GET /api/analytics/users` - 获取用户统计数据
- `GET /api/analytics/games` - 获取游戏统计数据
- `GET /api/analytics/revenue` - 获取收入统计数据
- `GET /api/analytics/games/{gameId}` - 获取游戏详情统计
- `POST /api/analytics/games/compare` - 对比多款游戏数据
- `GET /api/analytics/games/search` - 搜索游戏

### 系统配置接口

- `GET /api/system-config` - 获取系统配置
- `PUT /api/system-config` - 更新系统配置

## 数据库表说明

- `games` - 游戏表
- `users` - 用户表
- `user_activities` - 用户活动记录表
- `game_play_sessions` - 游戏游玩会话表
- `revenues` - 收入记录表
- `system_configs` - 系统配置表

## 开发说明

### 包结构规范
- `entity` - 数据库实体类
- `dto` - 数据传输对象（请求/响应）
- `repository` - 数据访问层（JPA接口）
- `service` - 业务逻辑层
- `controller` - 控制层（REST API）
- `config` - 配置类
- `common` - 通用工具类

### 前端开发规范
- 使用Vue 2 + Element UI组件库
- 使用ECharts进行数据可视化
- API调用统一封装在 `src/api/index.js`
- 路由配置在 `src/router/index.js`
- 状态管理使用Vuex
