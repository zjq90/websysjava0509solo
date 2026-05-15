# 教学辅助系统

## 项目简介

这是一个功能完整的教学辅助系统，采用前后端分离架构，后端使用Spring Boot + H2数据库，前端使用Vue.js。

## 技术栈

### 后端
- JDK 1.8
- Spring Boot 2.7.18
- H2 内存数据库
- Spring Data JPA
- JWT 认证
- Swagger-OpenAPI 3.0 (springdoc-openapi)
- Maven

### 前端
- Vue.js 2.x
- Vue Router
- Vuex
- Axios
- Element UI
- SCSS

## 功能模块

### 用户管理
- 用户登录/注册
- 个人资料查看和修改
- 邮箱绑定/解绑
- 密码修改

### 课程管理
- 课程列表查看
- 课程详情查看
- 创建课程（教师）
- 选修/退选课程
- 我的课程列表
- 课程学生列表

### 课程资料
- 资料上传
- 资料下载
- 资料列表查看
- 资料删除

### 课程视频
- 视频上传
- 视频播放
- 视频列表查看
- 视频删除

### 课程公告
- 发布公告
- 公告列表查看
- 公告置顶/取消置顶
- 公告删除

### 课程评论
- 发表评论
- 评论列表查看
- 评论删除

### 消息中心
- 消息发送
- 消息列表查看
- 未读消息统计
- 标记已读

## 项目结构

```
websysjava43/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/teaching/
│   │   │   │   ├── TeachingApplication.java    # 启动类
│   │   │   │   ├── common/                      # 公共类
│   │   │   │   ├── config/                      # 配置类
│   │   │   │   ├── controller/                  # 控制器层
│   │   │   │   ├── dto/                         # 数据传输对象
│   │   │   │   ├── entity/                      # 实体类
│   │   │   │   ├── repository/                  # 数据访问层
│   │   │   │   ├── service/                     # 业务逻辑层
│   │   │   │   └── utils/                       # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml              # 配置文件
│   └── pom.xml
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── components/      # 公共组件
│   │   ├── router/          # 路由配置
│   │   ├── store/           # 状态管理
│   │   ├── styles/          # 样式文件
│   │   ├── utils/           # 工具类
│   │   ├── views/           # 页面组件
│   │   ├── App.vue          # 根组件
│   │   └── main.js          # 入口文件
│   ├── public/
│   ├── package.json
│   └── vue.config.js
└── README.md
```

## 快速开始

### 环境要求
- JDK 1.8+
- Node.js 14+
- Maven 3.6+

### 后端启动

1. 进入后端目录
```bash
cd backend
```

2. 编译项目
```bash
mvn clean compile
```

3. 启动项目
```bash
mvn spring-boot:run
```

4. 访问地址
- 后端服务：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run serve
```

4. 访问地址：http://localhost:3000

## 测试账号

系统启动时会自动初始化测试数据：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| teacher | 123456 | 教师 | 可以创建课程、发布公告等 |
| student1 | 123456 | 学生 | 可以选修课程、上传资料等 |
| student2 | 123456 | 学生 | 测试学生账号 |
| student3 | 123456 | 学生 | 测试学生账号 |

## API文档

启动后端服务后，访问 Swagger UI 查看完整的API文档：
http://localhost:8080/swagger-ui.html

主要API接口：

- 用户接口：`/api/user/*`
- 课程接口：`/api/course/*`
- 文件接口：`/api/file/*`
- 公告接口：`/api/announcement/*`
- 评论接口：`/api/comment/*`
- 消息接口：`/api/message/*`

## 数据库说明

项目使用H2内存数据库，数据存储在内存中，重启服务后数据重置。

H2控制台配置：
- JDBC URL：`jdbc:h2:mem:teachingdb`
- 用户名：`sa`
- 密码：（空）

## 主要功能说明

### 1. 用户认证
- 使用JWT Token进行身份认证
- 登录成功后返回Token，后续请求在Header中携带
- Token格式：`Authorization: Bearer {token}`

### 2. 文件上传
- 支持课程资料和视频上传
- 文件存储在本地 `uploads/` 目录
- 支持大文件上传（最大500MB）

### 3. 消息通知
- 支持系统消息和用户间消息
- 支持未读消息数量统计
- 支持批量标记已读

## 开发说明

### 后端开发
- 实体类位于 `entity` 包，对应数据库表
- Repository 继承 `JpaRepository`，提供基础CRUD操作
- Service 层处理业务逻辑
- Controller 层提供REST API
- 使用 `@Valid` 进行参数校验
- 全局异常处理统一返回格式

### 前端开发
- 使用 Vue CLI 脚手架搭建
- 使用 Element UI 组件库
- 使用 Vuex 管理用户状态
- 使用 Axios 进行HTTP请求
- 路由守卫控制页面访问权限

## 注意事项

1. H2数据库为内存数据库，重启服务后数据会重置
2. 文件上传目录为 `backend/uploads/`，请确保有写入权限
3. JWT Token默认有效期24小时
4. 生产环境请修改JWT密钥和数据库配置

## 许可证

MIT License
