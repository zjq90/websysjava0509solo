# 大学生社团管理系统

## 项目简介

本项目是一款大学生社团管理端APP，包含**互动与交流**和**经费与资源管理**两大功能模块。

## 技术栈

### 后端技术
- **Spring Boot 3.2.0** - 后端框架
- **H2 Database** - 内存数据库
- **Redis** - 缓存数据库
- **Spring Data JPA** - ORM框架
- **SpringDoc OpenAPI 3.0** - API文档
- **Lombok** - 简化代码
- **Hutool** - 工具类库

### 前端技术
- **uni-app + Vue 3** - 跨端开发框架
- **uview-plus** - UI组件库
- **Vuex** - 状态管理
- **luch-request** - 网络请求库

## 功能模块

### 📬 消息通知
- **系统推送消息**：审核结果通知、活动开始提醒、社团公告、招新进度通知
- **群聊功能**：每个社团/部门可创建专属群聊，成员可在线文字、图片交流
- **一对一私聊**：成员之间可互发消息，方便沟通工作
- **已读未读状态**：重要通知可查看成员已读未读状态，未读成员可二次提醒

### 🏃 广场互动
- **活动圈**：所有用户可发布参与活动的动态，上传照片，其他用户可点赞评论
- **话题板块**：围绕社团生活、学习、就业创建热门话题，用户可发帖讨论
- **学长分享**：邀请毕业校友、优秀学长分享社团经验、就业建议，内容可分类浏览

### 💰 经费管理
- **收支记录**：管理者可记录每一笔收支（会费、赞助、拨款、活动物料、场地费等），可上传凭证
- **公开透明**：经费流水对社团所有成员公开透明，成员可随时查看明细
- **报销审批**：支持在线审批报销，成员提交申请，管理者在线审批
- **统计报表**：自动生成经费统计报表，按月份分类展示收支，支持图表可视化，可导出报表

### 📚 资源共享
- **资料分享**：支持社团成员上传分享学习资料、比赛经验、技能教程，分类存储，可在线预览、下载
- **校企对接**：支持企业发布实习、赞助信息，社团可对接赞助资源，学生可获取实习机会

## 项目结构

```
appsysjava75/
├── backend/                          # 后端项目
│   ├── src/main/java/com/club/management/
│   │   ├── ClubManagementApplication.java    # 启动类
│   ├── common/                               # 公共模块
│   │   ├── entity/BaseEntity.java            # 基础实体
│   │   ├── exception/                        # 异常处理
│   │   └── result/                           # 统一返回结果
│   ├── config/                               # 配置类
│   │   ├── SwaggerConfig.java                # Swagger配置
│   │   ├── CorsConfig.java                   # 跨域配置
│   │   ├── RedisConfig.java                  # Redis配置
│   │   └── TestDataInitializer.java          # 测试数据初始化
│   ├── system/                               # 系统模块（用户）
│   ├── club/                                 # 社团模块
│   ├── message/                              # 消息通知模块
│   ├── square/                               # 广场互动模块
│   ├── fund/                                 # 经费管理模块
│   └── resource/                             # 资源共享模块
├── frontend/                         # 前端项目
│   ├── pages/                          # 页面
│   │   ├── message/                    # 消息通知页面
│   │   ├── square/                     # 广场互动页面
│   │   ├── fund/                       # 经费管理页面
│   │   ├── resource/                   # 资源共享页面
│   │   └── mine/                       # 个人中心页面
│   ├── api/                            # API接口
│   ├── utils/                          # 工具类
│   ├── store/                          # Vuex状态管理
│   ├── static/                         # 静态资源
│   ├── App.vue                         # 根组件
│   ├── main.js                         # 入口文件
│   ├── pages.json                      # 页面配置
│   └── manifest.json                   # 应用配置
└── README.md
```

## 快速开始

### 后端启动

1. 进入后端目录
```bash
cd backend
```

2. 编译运行
```bash
mvn spring-boot:run
```

3. 访问地址
- 应用地址：http://localhost:8080/api
- H2控制台：http://localhost:8080/api/h2-console
- Swagger文档：http://localhost:8080/api/swagger-ui.html

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 运行H5版本
```bash
npm run dev:h5
```

4. 运行微信小程序
```bash
npm run dev:mp-weixin
```

## 数据库设计

项目包含以下核心数据表：

### 用户与社团
- `sys_user` - 用户表
- `club` - 社团表
- `club_member` - 社团成员表

### 消息通知
- `sys_message` - 系统消息表
- `chat_group` - 群聊表
- `chat_group_member` - 群成员表
- `chat_group_message` - 群聊消息表
- `chat_private_message` - 私聊消息表
- `message_read_status` - 消息已读状态表

### 广场互动
- `activity_post` - 活动圈动态表
- `topic` - 话题表
- `topic_post` - 话题帖子表
- `senior_share` - 学长分享表
- `comment` - 评论表
- `like_record` - 点赞表

### 经费管理
- `fund_record` - 经费记录表
- `reimbursement` - 报销申请表
- `fund_statistics` - 经费统计报表表

### 资源共享
- `resource_share` - 资源分享表
- `enterprise_cooperation` - 校企对接表
- `cooperation_apply` - 校企对接申请表

## 测试账号

系统已预置以下测试账号：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 系统管理员 |
| zhangsan | 123456 | 社团负责人 |
| lisi | 123456 | 社团负责人 |
| wangwu | 123456 | 社团负责人 |
| zhaoliu | 123456 | 普通用户 |
| sunqi | 123456 | 普通用户 |
| zhouba | 123456 | 普通用户 |
| wujiu | 123456 | 毕业学长 |

## 主要功能截图

### 消息通知模块
- 系统消息列表、已读未读状态
- 群聊列表、群聊消息发送
- 一对一私聊
- 已读状态查看、二次提醒

### 广场互动模块
- 活动圈动态发布、浏览、点赞、评论
- 话题列表、话题详情、发布帖子
- 学长分享板块、分类浏览
- 评论点赞、回复功能

### 经费管理模块
- 经费流水查看、收支记录
- 报销申请提交、审批
- 经费统计报表、图表展示
- 报表导出功能

### 资源共享模块
- 资料上传、下载、预览
- 资源分类浏览、搜索
- 校企对接信息发布、浏览
- 实习申请、赞助对接

## 开发规范

### 后端开发规范
1. 严格按照Spring Boot分层架构：Entity → Repository → Service → Controller
2. 重要操作必须记录详细日志
3. 统一使用Result作为返回结果
4. 业务异常使用BusinessException抛出
5. 所有API接口必须添加Swagger注解

### 前端开发规范
1. 页面文件存放在pages目录下，按模块分类
2. API接口统一封装在api目录下
3. 统一使用uview-plus组件库
4. 适配手机、平板等不同屏幕尺寸
5. 页面设计美观，布局合理

## 许可证

MIT License
