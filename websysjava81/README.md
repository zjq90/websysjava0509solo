# 游戏平台管理系统

## 项目简介

游戏平台的核心管理模块，包含用户管理和内容审核两大功能模块。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.2
- H2 内存数据库
- Lombok
- Hutool 工具库

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios
- ECharts

## 项目结构

```
websysjava81/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gameplatform/
│   │   │   │   ├── common/     # 通用类
│   │   │   │   ├── config/     # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── dto/        # 数据传输对象
│   │   │   │   ├── entity/     # 实体类
│   │   │   │   ├── mapper/     # 数据访问层
│   │   │   │   ├── service/    # 业务逻辑层
│   │   │   │   ├── vo/         # 视图对象
│   │   │   │   └── GamePlatformApplication.java
│   │   │   └── resources/
│   │   │       ├── db/         # 数据库脚本
│   │   │       └── application.yml
│   │   └── pom.xml
│
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/                # API接口
    │   ├── assets/             # 静态资源
    │   ├── router/             # 路由配置
    │   ├── views/              # 页面组件
    │   ├── App.vue
    │   └── main.js
    ├── public/
    ├── package.json
    └── vue.config.js
```

## 功能模块

### 1. 用户管理
- **用户列表**：展示用户ID、昵称、手机号（脱敏）、注册时间、最后登录时间、游戏总时长、会员状态
- **筛选功能**：注册日期范围、会员状态、用户状态
- **搜索功能**：支持用户ID、昵称、手机号精确或模糊搜索
- **用户详情**：基本信息、游戏记录（最近20条）、收藏列表、评论记录、登录设备信息
- **行为轨迹**：展示用户行为轨迹（注册→浏览首页→搜索游戏→开始游戏）
- **封禁功能**：
  - 支持临时封禁/永久封禁
  - 设置封禁原因和解封时间
  - 封禁记录展示与撤销
  - 批量封禁（CSV导入）

### 2. 内容审核
- **评论审核**：
  - 待审核评论队列
  - 审核操作：通过、删除、屏蔽
  - 关键词自动过滤，命中敏感词自动进入待审核

- **敏感词库管理**：
  - 增删改敏感词
  - 支持正则表达式匹配（手机号、身份证号等）
  - 导入/导出敏感词库（TXT格式）

- **举报处理**：
  - 举报列表展示
  - 处理操作：确认违规/驳回举报
  - 统计看板：举报类型分布、处理时效统计

## 快速开始

### 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务启动后访问：
- 应用地址：http://localhost:8080
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:game_platform`
  - 用户名: `sa`
  - 密码: (空)

### 前端启动

```bash
cd frontend
npm install
npm run serve
```

前端服务启动后访问：http://localhost:3000

## 数据库说明

项目使用H2内存数据库，启动时自动执行：
- `schema.sql`：创建数据表结构
- `data.sql`：插入测试数据

测试数据包含：
- 10个用户（包含正常/封禁状态）
- 5款游戏
- 8条评论（包含待审核/已通过状态）
- 5条举报记录
- 5个敏感词（包含普通词和正则表达式）
- 5条游戏记录
- 4条收藏记录
- 5条用户行为记录
- 5条登录设备记录

## API 接口

### 用户管理
- `POST /api/user/list` - 获取用户列表
- `GET /api/user/detail/{userId}` - 获取用户详情
- `POST /api/user/ban` - 封禁用户
- `POST /api/user/unban/{banRecordId}` - 解封用户
- `POST /api/user/batch-ban` - 批量封禁
- `GET /api/user/ban-records/{userId}` - 获取封禁记录

### 内容审核
- `GET /api/audit/comments` - 获取评论列表
- `POST /api/audit/comment/audit` - 审核评论
- `GET /api/audit/sensitive-words` - 获取敏感词列表
- `POST /api/audit/sensitive-word` - 添加敏感词
- `PUT /api/audit/sensitive-word/{id}` - 更新敏感词
- `DELETE /api/audit/sensitive-word/{id}` - 删除敏感词
- `GET /api/audit/sensitive-word/export` - 导出敏感词
- `POST /api/audit/sensitive-word/import` - 导入敏感词
- `GET /api/audit/reports` - 获取举报列表
- `POST /api/audit/report/handle` - 处理举报
- `GET /api/audit/report/statistics` - 获取举报统计

## 浏览器兼容性

- Chrome (推荐)
- Firefox
- Safari
- Edge
- 其他现代浏览器
