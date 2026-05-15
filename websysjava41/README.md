# 实时聊天系统

基于Spring Boot + Vue.js的实时聊天系统，支持文字、图片、文件、表情发送。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.x
- WebSocket (实时通信)
- H2 数据库 (内存数据库)
- Spring Data JPA
- SpringDoc OpenAPI (Swagger API文档)
- Maven

### 前端
- Vue.js 2.x
- Axios (HTTP请求)
- 原生CSS

## 功能特性

### 1. 用户管理
- 用户注册
- 用户登录/登出
- 用户信息管理

### 2. 好友管理
- 搜索用户
- 发送好友请求
- 接受/拒绝好友请求
- 好友列表展示

### 3. 聊天功能
- 实时消息推送 (WebSocket)
- 文字消息发送
- 图片消息发送
- 文件消息发送
- 表情消息发送
- 消息历史记录
- 未读消息计数
- 消息已读标记

## 项目结构

```
websysjava41/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/chatsystem/
│   │       │   ├── ChatSystemApplication.java    # 启动类
│   │       │   ├── common/                       # 通用类
│   │       │   │   └── Result.java              # 统一响应结果
│   │       │   ├── config/                       # 配置类
│   │       │   │   ├── CorsConfig.java          # 跨域配置
│   │       │   │   ├── DataInitializer.java     # 数据初始化
│   │       │   │   ├── SwaggerConfig.java       # Swagger配置
│   │       │   │   └── WebSocketConfig.java     # WebSocket配置
│   │       │   ├── controller/                   # 控制器
│   │       │   │   ├── ChatMessageController.java
│   │       │   │   ├── FriendController.java
│   │       │   │   └── UserController.java
│   │       │   ├── dto/                          # 数据传输对象
│   │       │   │   ├── ChatMessageDTO.java
│   │       │   │   ├── LoginDTO.java
│   │       │   │   └── RegisterDTO.java
│   │       │   ├── entity/                       # 实体类
│   │       │   │   ├── ChatMessage.java
│   │       │   │   ├── Friend.java
│   │       │   │   └── User.java
│   │       │   ├── repository/                   # 数据访问层
│   │       │   │   ├── ChatMessageRepository.java
│   │       │   │   ├── FriendRepository.java
│   │       │   │   └── UserRepository.java
│   │       │   ├── service/                      # 业务逻辑层
│   │       │   │   ├── ChatMessageService.java
│   │       │   │   ├── FriendService.java
│   │       │   │   └── UserService.java
│   │       │   └── websocket/                    # WebSocket处理
│   │       │       └── ChatWebSocket.java
│   │       └── resources/
│   │           └── application.yml               # 应用配置
│   └── pom.xml                                    # Maven配置
│
└── frontend/                   # 前端项目
    ├── index.html              # 主页面
    ├── css/
    │   └── style.css           # 样式文件
    └── js/
        └── app.js              # Vue应用逻辑
```

## 快速开始

### 环境要求
- JDK 8+
- Maven 3.6+
- 现代浏览器 (支持WebSocket)

### 启动后端

1. 进入后端目录
```bash
cd backend
```

2. 使用Maven启动
```bash
mvn spring-boot:run
```

或者打包后运行
```bash
mvn package
java -jar target/chat-system-1.0.0.jar
```

后端启动后访问地址：
- 应用端口: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console

### 启动前端

直接在浏览器中打开 `frontend/index.html` 文件即可。

## 测试账号

系统启动时会自动创建以下测试账号：

| 用户名 | 密码 | 昵称 | 说明 |
|--------|------|------|------|
| user1 | 123456 | 张三 | 有好友user2、user3，有好友请求 |
| user2 | 123456 | 李四 | 有好友user1 |
| user3 | 123456 | 王五 | 有好友user1 |
| user4 | 123456 | 赵六 | 向user1发送了好友请求 |

## API接口说明

### 用户管理
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `POST /api/user/logout/{userId}` - 用户登出
- `GET /api/user/{userId}` - 获取用户信息
- `GET /api/user/list` - 获取所有用户
- `GET /api/user/search` - 搜索用户
- `PUT /api/user/{userId}` - 更新用户信息

### 好友管理
- `POST /api/friend/request` - 发送好友请求
- `POST /api/friend/accept/{requestId}` - 接受好友请求
- `POST /api/friend/reject/{requestId}` - 拒绝好友请求
- `DELETE /api/friend/delete` - 删除好友
- `GET /api/friend/list/{userId}` - 获取好友列表
- `GET /api/friend/requests/{userId}` - 获取好友请求列表
- `GET /api/friend/check` - 检查是否为好友

### 聊天管理
- `GET /api/chat/history` - 获取聊天历史
- `POST /api/chat/mark-read` - 标记消息为已读
- `GET /api/chat/unread-count` - 获取未读消息数
- `POST /api/chat/recall/{messageId}` - 撤回消息
- `POST /api/chat/upload` - 上传文件
- `POST /api/chat/send` - 发送消息(REST方式)
- `POST /api/chat/batch-send` - 批量发送消息

## WebSocket接口

连接地址: `ws://localhost:8080/ws/chat/{userId}`

消息格式（JSON）：
```json
{
  "fromUserId": 1,
  "toUserId": 2,
  "type": 0,
  "content": "你好！",
  "fileName": "文件名",
  "fileUrl": "文件URL",
  "fileSize": 1024
}
```

消息类型说明：
- 0 - 文字消息
- 1 - 图片消息
- 2 - 文件消息
- 3 - 表情消息

## 数据库说明

使用H2内存数据库，启动后可以通过H2控制台查看数据：
- JDBC URL: `jdbc:h2:mem:chatdb`
- 用户名: `sa`
- 密码: (空)

## 主要数据表

### sys_user (用户表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR | 用户名 |
| password | VARCHAR | 密码 |
| nickname | VARCHAR | 昵称 |
| avatar | VARCHAR | 头像URL |
| status | INT | 状态 0离线 1在线 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### sys_friend (好友关系表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| friend_id | BIGINT | 好友ID |
| remark | VARCHAR | 备注 |
| status | INT | 状态 0待确认 1已添加 2已拒绝 |
| create_time | DATETIME | 创建时间 |

### chat_message (聊天消息表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| from_user_id | BIGINT | 发送者ID |
| to_user_id | BIGINT | 接收者ID |
| type | INT | 消息类型 0文字 1图片 2文件 3表情 |
| content | TEXT | 消息内容 |
| file_url | VARCHAR | 文件URL |
| file_name | VARCHAR | 文件名 |
| file_size | BIGINT | 文件大小 |
| status | INT | 状态 0未读 1已读 2已撤回 |
| send_time | DATETIME | 发送时间 |
