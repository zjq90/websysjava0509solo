# 大学生社团管理端 App - 活动管理模块

## 项目简介

本项目是一个完整的大学生社团管理端 App 的活动管理功能模块，采用前后端分离架构设计。后端基于 Spring Boot 3.2.0 开发，前端基于 UniApp 框架开发，支持手机和平板设备。

## 功能特性

### 活动发布与报名
- **活动发布**: 社团管理者可发布活动，填写活动名称、时间、地点、名额、报名起止时间、活动要求、简介，上传活动海报
- **报名限制**: 支持设置报名范围（仅社团成员可报/全校开放），设置是否需要审核
- **在线报名**: 学生可在线报名，自动绑定个人信息，无需重复填写
- **报名状态**: 学生可在个人中心查看报名状态（待审核/已通过/已拒绝）
- **满额关闭**: 报名满额后自动关闭报名通道
- **名单导出**: 管理者可实时导出报名名单，支持筛选导出院系、学号信息

### 活动现场管理
- **签到二维码**: 支持生成活动专属签到二维码，管理者可设置签到有效时间，防止代签
- **扫码签到**: 学生打开 APP 扫码即可完成签到，签到结果实时同步
- **实时数据**: 管理者可在后台查看实时签到数据
- **手动补签**: 管理者可对未签到成员进行补签操作
- **签到统计**: 活动结束后自动生成签到统计（报名人数、实到人数、签到率）
- **签到表导出**: 支持导出签到表

### 活动总结与留存
- **活动总结**: 活动结束后，管理者可发布活动总结，上传活动现场照片、成果展示
- **社团主页同步**: 活动总结同步到社团主页展示
- **评分评价**: 参与学生可对活动进行评分评价，填写反馈建议
- **活动归档**: 所有活动自动归档到社团活动列表，按时间排序，可随时查看历史活动

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.0
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **API文档**: Swagger-OpenAPI 3.0
- **安全认证**: JWT (JSON Web Token)
- **ORM**: Spring Data JPA
- **简化开发**: Lombok
- **Excel导出**: Apache POI
- **二维码生成**: ZXing
- **构建工具**: Maven

### 前端技术
- **框架**: UniApp (Vue 3)
- **UI组件库**: uView Plus
- **状态管理**: Vuex
- **路由管理**: UniApp 内置路由
- **日期处理**: dayjs
- **HTTP请求**: UniApp 内置 request API

## 项目结构

### 后端项目结构
```
backend/
├── src/main/java/com/club/management/
│   ├── ClubManagementApplication.java    # Spring Boot 启动类
│   ├── config/                           # 配置类
│   │   ├── OpenApiConfig.java            # Swagger 配置
│   │   ├── RedisConfig.java              # Redis 配置
│   │   └── SecurityConfig.java           # Spring Security 配置
│   ├── security/                         # 安全认证
│   │   ├── JwtTokenProvider.java         # JWT 令牌生成与验证
│   │   └── JwtAuthenticationFilter.java  # JWT 认证过滤器
│   ├── common/                           # 通用模块
│   │   ├── ApiResponse.java              # 统一响应格式
│   │   ├── GlobalExceptionHandler.java   # 全局异常处理
│   │   ├── BusinessException.java        # 业务异常
│   │   └── PageQuery.java                # 分页查询参数
│   ├── entity/                           # 数据库实体
│   │   ├── enums/                        # 枚举类
│   │   ├── BaseEntity.java               # 基础实体
│   │   ├── User.java                     # 用户实体
│   │   ├── Club.java                     # 社团实体
│   │   ├── Activity.java                 # 活动实体
│   │   ├── Registration.java             # 报名实体
│   │   ├── SignIn.java                   # 签到实体
│   │   ├── ActivitySummary.java          # 活动总结实体
│   │   ├── ActivityRating.java           # 活动评价实体
│   │   └── ClubMember.java               # 社团成员实体
│   ├── repository/                       # 数据访问层
│   ├── service/                          # 业务逻辑层
│   ├── controller/                       # 控制层
│   ├── task/                             # 定时任务
│   ├── dto/                              # 数据传输对象
│   ├── util/                             # 工具类
│   │   ├── QrCodeUtil.java               # 二维码生成工具
│   │   └── ExcelUtil.java                # Excel导出工具
│   └── init/                             # 数据初始化
│       └── DataInitializer.java          # 测试数据初始化
└── src/main/resources/
    ├── application.yml                   # 应用配置
    └── data.sql                          # 初始SQL脚本
```

### 前端项目结构
```
frontend/
├── pages/                                # 页面目录
│   ├── login/                            # 登录页
│   ├── index/                            # 活动列表页（首页）
│   ├── activity/                         # 活动相关页面
│   │   ├── detail.vue                    # 活动详情
│   │   ├── publish.vue                   # 发布活动
│   │   ├── edit.vue                      # 编辑活动
│   │   └── archived.vue                  # 历史活动
│   ├── registration/                     # 报名管理
│   │   └── list.vue                      # 报名列表
│   ├── signin/                           # 签到管理
│   │   ├── qrcode.vue                    # 签到二维码
│   │   ├── list.vue                      # 签到列表
│   │   ├── statistics.vue                # 签到统计
│   │   └── makeup.vue                    # 手动补签
│   ├── summary/                          # 活动总结
│   │   ├── publish.vue                   # 发布总结
│   │   └── detail.vue                    # 总结详情
│   ├── rating/                           # 评价管理
│   │   ├── submit.vue                    # 提交评价
│   │   └── list.vue                      # 评价列表
│   ├── user/                             # 用户中心
│   │   ├── center.vue                    # 个人中心
│   │   └── my-registrations.vue          # 我的报名
│   └── scan/                             # 扫码功能
│       └── scan.vue                      # 扫码签到
├── store/                                # Vuex 状态管理
│   └── index.js
├── utils/                                # 工具类
│   └── api.js                            # API 请求封装
├── static/                               # 静态资源
├── App.vue                               # 根组件
├── main.js                               # 入口文件
├── pages.json                            # 页面配置
├── manifest.json                         # 应用配置
└── package.json                          # 项目依赖
```

## 数据库设计

### 核心数据表
1. **user** - 用户表（学生、社团管理员、系统管理员）
2. **club** - 社团表
3. **club_member** - 社团成员表
4. **activity** - 活动表
5. **registration** - 活动报名表
6. **sign_in** - 活动签到表
7. **activity_summary** - 活动总结表
8. **activity_rating** - 活动评价表

### 数据库关系
- 社团 -> 社团成员 -> 用户 (一对多)
- 社团 -> 活动 (一对多)
- 活动 -> 报名 (一对多)
- 报名 -> 签到 (一对一)
- 活动 -> 活动总结 (一对一)
- 活动 -> 活动评价 (一对多)
- 用户 -> 活动评价 (一对多)

## 快速开始

### 后端启动

#### 环境要求
- JDK 17+
- Maven 3.6+
- Redis 6.0+

#### 启动步骤
1. 启动 Redis 服务
2. 进入后端项目目录
   ```bash
   cd backend
   ```
3. 编译项目
   ```bash
   mvn clean package
   ```
4. 运行项目
   ```bash
   java -jar target/club-management-1.0.0.jar
   ```
5. 访问 API 文档: http://localhost:8080/swagger-ui.html
6. 访问 H2 控制台: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:clubdb`
   - 用户名: `sa`
   - 密码: (空)

#### 测试账号
系统启动时会自动初始化测试数据，包含以下账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | ADMIN | 系统管理员 |
| clubadmin | 123456 | CLUB_ADMIN | 社团管理员（计算机协会） |
| clubadmin2 | 123456 | CLUB_ADMIN | 社团管理员（摄影协会） |
| student1 | 123456 | STUDENT | 学生用户（张三） |
| student2 | 123456 | STUDENT | 学生用户（李四） |
| student3 | 123456 | STUDENT | 学生用户（王五） |

### 前端启动

#### 环境要求
- Node.js 16+
- HBuilderX (推荐) 或 Vue CLI
- 微信开发者工具 (如需发布到微信小程序)

#### 启动步骤
1. 进入前端项目目录
   ```bash
   cd frontend
   ```
2. 安装依赖
   ```bash
   npm install
   ```
3. 配置后端 API 地址
   - 修改 `utils/api.js` 中的 `baseURL` 为你的后端地址
4. 运行项目
   ```bash
   # 运行到浏览器
   npm run dev:h5
   
   # 运行到微信小程序
   npm run dev:mp-weixin
   
   # 运行到App
   npm run dev:app
   ```

#### 使用 HBuilderX 运行
1. 用 HBuilderX 打开 frontend 目录
2. 点击顶部菜单「运行」->「运行到浏览器」->「Chrome」
3. 或点击「运行」->「运行到手机或模拟器」

## API 接口说明

### 认证接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/auth/login | 用户登录 | 公开 |
| GET | /api/auth/me | 获取当前用户信息 | 登录用户 |

### 活动接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/activities | 获取活动列表 | 登录用户 |
| GET | /api/activities/{id} | 获取活动详情 | 登录用户 |
| POST | /api/activities | 创建活动 | 社团管理员/管理员 |
| PUT | /api/activities/{id} | 更新活动 | 社团管理员/管理员 |
| DELETE | /api/activities/{id} | 删除活动 | 社团管理员/管理员 |
| POST | /api/activities/{id}/publish | 发布活动 | 社团管理员/管理员 |
| POST | /api/activities/{id}/cancel | 取消活动 | 社团管理员/管理员 |
| GET | /api/activities/{id}/qrcode | 生成签到二维码 | 社团管理员/管理员 |
| GET | /api/activities/archived | 获取归档活动列表 | 登录用户 |
| GET | /api/activities/{id}/statistics | 获取活动统计 | 社团管理员/管理员 |

### 报名接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/registrations | 报名活动 | 学生 |
| GET | /api/registrations/my | 获取我的报名列表 | 登录用户 |
| GET | /api/activities/{activityId}/registrations | 获取活动报名列表 | 社团管理员/管理员 |
| PUT | /api/registrations/{id}/approve | 审核通过报名 | 社团管理员/管理员 |
| PUT | /api/registrations/{id}/reject | 拒绝报名 | 社团管理员/管理员 |
| PUT | /api/registrations/batch-audit | 批量审核报名 | 社团管理员/管理员 |
| PUT | /api/registrations/{id}/cancel | 取消报名 | 学生 |
| GET | /api/activities/{activityId}/registrations/export | 导出报名名单 | 社团管理员/管理员 |

### 签到接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/signin/qrcode | 扫码签到 | 学生 |
| POST | /api/signin/manual | 手动补签 | 社团管理员/管理员 |
| GET | /api/activities/{activityId}/signin | 获取签到列表 | 社团管理员/管理员 |
| GET | /api/activities/{activityId}/signin/statistics | 获取签到统计 | 社团管理员/管理员 |
| GET | /api/activities/{activityId}/signin/export | 导出签到表 | 社团管理员/管理员 |

### 活动总结接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/activities/{activityId}/summary | 获取活动总结 | 登录用户 |
| POST | /api/summaries | 创建活动总结 | 社团管理员/管理员 |
| PUT | /api/summaries/{id} | 更新活动总结 | 社团管理员/管理员 |
| DELETE | /api/summaries/{id} | 删除活动总结 | 社团管理员/管理员 |
| POST | /api/summaries/{id}/publish | 发布活动总结 | 社团管理员/管理员 |

### 活动评价接口
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/ratings | 提交评价 | 学生 |
| GET | /api/activities/{activityId}/ratings | 获取活动评价列表 | 登录用户 |
| GET | /api/activities/{activityId}/ratings/statistics | 获取评价统计 | 登录用户 |
| DELETE | /api/ratings/{id} | 删除评价 | 评价作者/管理员 |

## 核心功能实现说明

### 二维码签到防代签机制
1. 二维码包含活动ID和过期时间戳，并使用JWT签名
2. 二维码有效时间可由管理者设置（默认30分钟）
3. 签到时验证二维码签名和过期时间
4. 记录签到设备信息和位置信息（可选）
5. 同一用户在短时间内（如5分钟）只能签到一次

### 活动状态自动更新
使用 Spring Scheduling 定时任务，每分钟检查活动状态：
- 报名开始时间到了自动开启报名
- 报名结束时间到了自动关闭报名
- 活动开始时间到了自动更新为进行中状态
- 活动结束时间到了自动更新为已结束状态，并归档

### 报名满额自动关闭
- 报名时使用数据库乐观锁机制
- 检查当前报名人数是否达到名额上限
- 达到上限后自动更新活动报名状态为已满

### 数据导出功能
- 使用 Apache POI 生成 Excel 文件
- 支持按院系、学号筛选导出报名名单
- 签到表包含完整的签到信息（姓名、学号、院系、签到时间、签到方式）

## 测试数据说明

系统启动时会自动初始化以下测试数据：
- 6个用户账号（管理员、2个社团管理员、3个学生）
- 5个社团（计算机协会、摄影协会、篮球社、文学社、音乐社）
- 10个活动（包含不同状态：未开始、报名中、进行中、已结束、已归档）
- 30+ 条报名记录
- 20+ 条签到记录
- 5 条活动总结
- 15+ 条活动评价

## 部署说明

### 后端部署
1. 确保已安装 JDK 17+ 和 Redis
2. 修改 `application.yml` 中的配置（数据库、Redis、JWT密钥等）
3. 打包项目: `mvn clean package -DskipTests`
4. 运行 Jar 包: `java -jar club-management-1.0.0.jar`
5. 建议使用 Nginx 作为反向代理

### 前端部署
#### H5 部署
1. 执行 `npm run build:h5`
2. 将 `dist/build/h5` 目录部署到静态服务器
3. 配置 Nginx 路由重写

#### 小程序部署
1. 执行 `npm run build:mp-weixin`
2. 用微信开发者工具打开 `dist/build/mp-weixin` 目录
3. 上传代码并提交审核

#### App 部署
1. 使用 HBuilderX 打开项目
2. 点击「发行」->「原生App-云打包」
3. 填写相关信息后生成安装包

## 常见问题

### 后端启动失败
1. 检查 Redis 是否启动
2. 检查端口 8080 是否被占用
3. 检查 JDK 版本是否为 17+

### 前端无法连接后端
1. 检查后端服务是否正常启动
2. 检查 `utils/api.js` 中的 `baseURL` 配置是否正确
3. 检查防火墙设置，确保 8080 端口可访问

### H2 数据库数据丢失
H2 为内存数据库，重启后数据会清空。如需持久化，可修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/clubdb;DB_CLOSE_DELAY=-1
```

## 许可证

MIT License

## 联系方式

如有问题或建议，欢迎提交 Issue 或 Pull Request。
