# 裁判管理系统 (Referee Management System)

基于 Spring Boot + Vue 3 的裁判管理系统，支持运动员管理、比赛管理、评分审核等功能。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.x
- H2 内存数据库
- Spring Data JPA
- Swagger/OpenAPI 3.0
- Maven

### 前端
- Vue 3 (Composition API)
- Vue Router 4
- Element Plus
- Axios
- Vite

## 功能模块

### 1. 管理员功能
- 用户登录
- 重置非管理员用户密码
- 查看所有用户列表

### 2. 裁判/裁判长功能
- 裁判管理（增删改查）
- 运动员管理（增删改查、评价）
- 比赛管理（增删改查、添加参赛运动员、签到管理）
- 评分管理（提交评分、编辑评分）
- 评分审核（裁判长专属功能）

### 3. 运动员功能
- 查看个人比赛成绩

## 数据库表结构

### 主要数据表
- `sys_user` - 用户表（登录账号）
- `referee` - 裁判信息表
- `athlete` - 运动员信息表
- `competition` - 比赛信息表
- `competition_athlete` - 比赛运动员关联表
- `score` - 评分记录表

## 快速开始

### 环境要求
- JDK 8+
- Node.js 14+
- Maven 3.6+

### 后端启动

1. 进入后端项目根目录
2. 执行 Maven 构建：
```bash
mvn clean install
```

3. 启动应用：
```bash
mvn spring-boot:run
```

或直接运行打包后的 jar：
```bash
java -jar target/referee-system-1.0.0.jar
```

### 前端启动

1. 进入 frontend 目录
2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

### 访问地址
- 前端应用：http://localhost:3000
- Swagger API文档：http://localhost:8080/api/swagger-ui.html
- H2 数据库控制台：http://localhost:8080/api/h2-console

## 测试账号

系统启动时会自动初始化以下测试账号：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 裁判长 | referee_R001 | 123456 |
| 裁判 | referee_R002 | 123456 |
| 裁判 | referee_R003 | 123456 |
| 运动员 | athlete_A001 | 123456 |
| 运动员 | athlete_A002 | 123456 |
| 运动员 | athlete_A003 | 123456 |

## 项目结构

### 后端结构
```
src/main/java/com/referee/
├── RefereeSystemApplication.java    # Spring Boot 启动类
├── config/                            # 配置类
│   ├── CorsConfig.java               # 跨域配置
│   └── DataInitializer.java         # 数据初始化
├── controller/                        # 控制器层
│   ├── AuthController.java         # 认证接口
│   ├── RefereeController.java      # 裁判接口
│   ├── AthleteController.java      # 运动员接口
│   ├── CompetitionController.java  # 比赛接口
│   └── ScoreController.java         # 评分接口
├── dto/                             # 数据传输对象
│   └── LoginRequest.java
├── entity/                          # 实体类
│   ├── User.java
│   ├── Referee.java
│   ├── Athlete.java
│   ├── Competition.java
│   ├── CompetitionAthlete.java
│   └── Score.java
├── repository/                      # 数据访问层
│   ├── UserRepository.java
│   ├── RefereeRepository.java
│   ├── AthleteRepository.java
│   ├── CompetitionRepository.java
│   ├── CompetitionAthleteRepository.java
│   └── ScoreRepository.java
├── service/                         # 业务逻辑层
│   ├── AuthService.java
│   ├── RefereeService.java
│   ├── AthleteService.java
│   ├── CompetitionService.java
│   └── ScoreService.java
└── common/                          # 公共类
    └── Result.java
```

### 前端结构
```
frontend/
├── src/
│   ├── views/                        # 页面组件
│   │   ├── Login.vue              # 登录页
│   │   ├── Layout.vue             # 布局组件
│   │   ├── Dashboard.vue          # 首页
│   │   ├── RefereeManage.vue      # 裁判管理
│   │   ├── AthleteManage.vue      # 运动员管理
│   │   ├── CompetitionManage.vue  # 比赛管理
│   │   ├── ScoreManage.vue        # 评分管理
│   │   ├── ScoreAudit.vue         # 评分审核
│   │   ├── MyScores.vue            # 我的成绩
│   │   └── UserManage.vue         # 用户管理
│   ├── router/                     # 路由配置
│   │   └── index.js
│   ├── api/                         # API 接口
│   │   └── index.js
│   ├── App.vue
│   └── main.js
├── index.html
├── vite.config.js
└── package.json
```

## API 接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/reset-password/{id}` - 重置密码
- `GET /api/auth/users` - 获取用户列表

### 裁判接口
- `GET /api/referee` - 获取裁判列表
- `POST /api/referee` - 新增裁判
- `PUT /api/referee/{id}` - 更新裁判
- `DELETE /api/referee/{id}` - 删除裁判

### 运动员接口
- `GET /api/athlete` - 获取运动员列表
- `POST /api/athlete` - 新增运动员
- `PUT /api/athlete/{id}` - 更新运动员
- `DELETE /api/athlete/{id}` - 删除运动员
- `POST /api/athlete/{id}/evaluate` - 评价运动员

### 比赛接口
- `GET /api/competition` - 获取比赛列表
- `POST /api/competition` - 新增比赛
- `PUT /api/competition/{id}` - 更新比赛
- `DELETE /api/competition/{id}` - 删除比赛
- `GET /api/competition/{id}/athletes` - 获取比赛参赛运动员
- `POST /api/competition/{compId}/athlete/{athId}` - 添加运动员到比赛
- `DELETE /api/competition/{compId}/athlete/{athId}` - 移除运动员
- `POST /api/competition/{compId}/athlete/{athId}/checkin` - 签到

### 评分接口
- `GET /api/score` - 获取评分列表
- `POST /api/score` - 提交评分
- `PUT /api/score/{id}` - 更新评分
- `POST /api/score/{id}/audit` - 审核评分
- `GET /api/score/pending` - 获取待审核评分
- `GET /api/score/competition/{id}` - 按比赛查询评分
- `GET /api/score/athlete/{id}` - 按运动员查询评分

## 功能使用说明

### 1. 登录系统
使用测试账号登录系统，根据角色权限显示对应菜单。

### 2. 裁判管理（裁判/裁判长可访问）
- 查看、添加、编辑、删除裁判信息
- 新增裁判时自动创建登录账号

### 3. 运动员管理（裁判/裁判长可访问）
- 查看、添加、编辑、删除运动员信息
- 对运动员进行评价
- 新增运动员时自动创建登录账号

### 4. 比赛管理（裁判/裁判长可访问）
- 管理比赛信息（增删改查）
- 添加参赛运动员
- 管理运动员签到

### 5. 评分管理（裁判/裁判长可访问）
- 提交运动员比赛评分
- 编辑未审核的评分

### 6. 评分审核（仅裁判长可访问）
- 审核待审核的评分记录
- 通过或驳回评分

### 7. 我的成绩（仅运动员可访问）
- 查看个人所有比赛成绩

### 8. 用户管理（仅管理员可访问）
- 查看所有用户
- 重置用户密码

## 注意事项

1. H2 为内存数据库，重启后数据会重置
2. 管理员密码无法被其他用户重置
3. 已审核通过的评分无法修改
4. 前端开发模式下通过 Vite 代理后端 API

## 许可证

MIT License
