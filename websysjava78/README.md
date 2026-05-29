# 音乐播放器项目

一个功能完整的在线音乐播放器，采用前后端分离架构。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.0
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **认证**: JWT + Spring Security
- **构建**: Maven

### 前端
- **框架**: Vue 3 (Composition API)
- **构建**: Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios

## 功能特性

### 播放器功能
- ✅ 播放/暂停控制
- ✅ 进度条拖动定位
- ✅ 音量控制和静音
- ✅ 播放模式切换（顺序/循环/随机）
- ✅ 上一首/下一首切换
- ✅ 播放列表管理
- ✅ 背景播放（支持页面切换时继续播放）

### 歌词功能
- ✅ LRC格式歌词解析
- ✅ 歌词同步高亮显示
- ✅ 点击歌词跳转到对应时间
- ✅ 纯文本歌词支持

### 音乐管理
- ✅ 音乐列表浏览
- ✅ 音乐搜索（按歌名/歌手）
- ✅ 热门音乐推荐
- ✅ 批量导入音乐列表（JSON格式）
- ✅ 音乐上传（支持多音质文件）
- ✅ 歌词文件上传

### 音质选择
- ✅ 普通音质 (128kbps MP3)
- ✅ 高清音质 (320kbps MP3)
- ✅ 无损音质 (FLAC) - 会员专享

### 下载管理
- ✅ 付费会员可下载高音质/无损音乐
- ✅ 下载次数限制（免费用户5次/天，VIP用户100次/天）
- ✅ 下载记录查询
- ✅ VIP会员升级

### 用户系统
- ✅ 用户注册/登录
- ✅ JWT令牌认证
- ✅ 角色权限管理（免费用户/VIP/管理员）
- ✅ 个人中心

## 项目结构

```
websysjava78/
├── backend/                    # 后端Spring Boot项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/musicplayer/
│   │       │   ├── MusicPlayerApplication.java    # 主应用类
│   │       │   ├── config/                        # 配置类
│   │       │   │   ├── RedisConfig.java
│   │       │   │   ├── SecurityConfig.java
│   │       │   │   ├── CorsConfig.java
│   │       │   │   └── DataInitializer.java       # 数据初始化
│   │       │   ├── controller/                    # 控制器
│   │       │   │   ├── AuthController.java
│   │       │   │   ├── MusicController.java
│   │       │   │   ├── UserController.java
│   │       │   │   └── DownloadController.java
│   │       │   ├── service/                       # 服务层
│   │       │   │   ├── MusicService.java
│   │       │   │   ├── UserService.java
│   │       │   │   └── DownloadService.java
│   │       │   ├── repository/                    # 数据访问层
│   │       │   │   ├── MusicRepository.java
│   │       │   │   ├── UserRepository.java
│   │       │   │   ├── LyricsRepository.java
│   │       │   │   └── DownloadRecordRepository.java
│   │       │   ├── entity/                        # 实体类
│   │       │   │   ├── Music.java
│   │       │   │   ├── Lyrics.java
│   │       │   │   ├── User.java
│   │       │   │   └── DownloadRecord.java
│   │       │   ├── dto/                           # 数据传输对象
│   │       │   │   ├── LoginRequest.java
│   │       │   │   └── RegisterRequest.java
│   │       │   └── util/                          # 工具类
│   │       │       └── JwtUtil.java
│   │       └── resources/
│   │           └── application.yml                 # 应用配置
│   └── pom.xml                                     # Maven配置
└── frontend/                   # 前端Vue项目
    ├── src/
    │   ├── main.js                               # 入口文件
    │   ├── App.vue                               # 根组件
    │   ├── router/index.js                       # 路由配置
    │   ├── stores/                               # Pinia状态管理
    │   │   ├── player.js                         # 播放器状态
    │   │   └── user.js                           # 用户状态
    │   ├── api/                                  # API接口
    │   │   ├── request.js                        # Axios配置
    │   │   ├── music.js                          # 音乐相关API
    │   │   └── user.js                           # 用户相关API
    │   ├── components/                           # 组件
    │   │   ├── HeaderBar.vue                     # 顶部导航
    │   │   ├── MusicPlayer.vue                   # 底部播放器
    │   │   ├── MusicCard.vue                     # 音乐卡片
    │   │   └── LyricsDisplay.vue                 # 歌词显示
    │   ├── views/                                # 页面视图
    │   │   ├── Home.vue                          # 首页
    │   │   ├── MusicList.vue                     # 音乐列表
    │   │   ├── Lyrics.vue                        # 歌词页
    │   │   ├── Login.vue                         # 登录页
    │   │   ├── Register.vue                      # 注册页
    │   │   ├── UserCenter.vue                    # 个人中心
    │   │   └── Admin.vue                         # 管理后台
    │   └── styles/                               # 样式
    │       └── global.scss                       # 全局样式
    ├── index.html
    ├── vite.config.js
    └── package.json
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- Redis (用于缓存和热门排行)

### 启动Redis
```bash
# Windows (使用Docker或本地安装)
redis-server
```

### 启动后端
```bash
cd backend

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/music-player-backend-1.0.0.jar

# 或者使用Maven直接运行
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

H2控制台: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:musicdb`
- 用户名: `sa`
- 密码: (空)

### 启动前端
```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

## 默认账号

系统启动时会自动创建以下测试账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | ADMIN | 管理员，可访问管理后台 |
| vip | vip123 | PREMIUM | VIP会员，可下载无损音质 |
| user | user123 | FREE | 普通用户，每天5次下载 |

## API接口说明

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录

### 音乐接口
- `GET /api/music/list` - 获取音乐列表（公开）
- `GET /api/music/{id}` - 获取音乐详情（公开）
- `GET /api/music/hot` - 获取热门音乐（公开）
- `GET /api/music/stream/{id}/{quality}` - 流媒体播放（公开）
- `GET /api/music/lyrics/{id}` - 获取歌词（公开）
- `POST /api/music` - 上传音乐（需要登录）
- `POST /api/music/batch-import` - 批量导入音乐（需要登录）
- `PUT /api/music/{id}` - 更新音乐信息（需要登录）
- `DELETE /api/music/{id}` - 删除音乐（需要登录）
- `POST /api/music/play/{id}` - 增加播放计数（公开）

### 用户接口
- `GET /api/user/me` - 获取当前用户信息（需要登录）
- `PUT /api/user/me` - 更新用户信息（需要登录）
- `POST /api/user/{id}/upgrade` - 升级为VIP（需要登录）
- `GET /api/user/me/downloads/remaining` - 获取剩余下载次数（需要登录）

### 下载接口
- `POST /api/download/{musicId}/{quality}` - 创建下载记录（需要登录）
- `GET /api/download/file/{musicId}/{quality}` - 下载音乐文件（需要登录）
- `GET /api/download/my` - 获取我的下载记录（需要登录）
- `GET /api/download/check/{musicId}/{quality}` - 检查是否可下载（需要登录）

## 批量导入格式

批量导入音乐时，JSON格式如下：

```json
[
  {
    "title": "歌曲名",
    "artist": "歌手",
    "album": "专辑",
    "genre": "流派",
    "duration": 240,
    "isPremium": false,
    "filePath128": "song_128.mp3",
    "filePath320": "song_320.mp3",
    "filePathFlac": "song.flac"
  }
]
```

## 注意事项

1. **Redis连接**: 确保Redis服务已启动，否则后端会启动失败
2. **音乐文件**: 上传的音乐文件会保存在 `backend/uploads/music/` 目录下
3. **H2数据库**: 使用内存数据库，重启后数据会丢失（但DataInitializer会重新创建示例数据）
4. **音质切换**: 切换音质时会保持当前播放进度
5. **歌词格式**: 支持标准LRC格式歌词，时间标签精确到毫秒
6. **下载限制**: 下载次数按自然日统计，每天凌晨自动重置

## 开发说明

### 后端开发
- 所有API返回统一格式的JSON响应
- 使用JWT进行身份认证，Token放在请求头 `Authorization: Bearer {token}`
- 使用Redis缓存音乐详情和热门排行榜
- 使用Spring Data JPA进行数据库操作

### 前端开发
- 使用Vue 3 Composition API
- 使用Pinia管理播放器和用户状态
- 播放器全局存在，支持页面切换时继续播放
- 使用Element Plus组件库
- 支持响应式布局

## License

MIT
