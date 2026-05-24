# 车牌识别系统管理后台

## 项目简介

基于 Spring Boot + Vue 开发的车牌识别系统管理后台，包含实时监控、历史记录查询、黑名单管理等功能。

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Data JPA
- H2 内存数据库
- Swagger/OpenAPI 3.0
- Lombok

### 前端
- Vue 3
- Vue Router 4
- Element Plus
- Axios
- ECharts

## 项目结构

```
websysjava66/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/plate/
│   │       │   ├── config/     # 配置类
│   │       │   ├── controller/ # 控制器
│   │       │   ├── entity/     # 实体类
│   │       │   ├── repository/ # 数据访问层
│   │       │   ├── service/    # 业务逻辑层
│   │       │   ├── dto/        # 数据传输对象
│   │       │   ├── common/     # 公共类
│   │       │   └── PlateRecognitionApplication.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/                # API接口
    │   ├── utils/              # 工具类
    │   ├── views/              # 页面组件
    │   ├── router/             # 路由配置
    │   ├── assets/             # 静态资源
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── vite.config.js
    └── package.json
```

## 数据库设计

### 摄像头表 (cameras)
- id: 主键
- cameraId: 摄像头编号
- name: 摄像头名称
- location: 位置描述
- longitude: 经度
- latitude: 纬度
- isOnline: 是否在线
- nightMode: 夜间模式
- streamUrl: 视频流地址

### 识别记录表 (recognition_records)
- id: 主键
- plateNumber: 车牌号
- cameraId: 摄像头编号
- cameraName: 摄像头名称
- passTime: 通过时间
- confidence: 识别置信度
- imageUrl: 图片地址
- anomalyType: 异常类型
- isAnomaly: 是否异常

### 黑名单表 (blacklists)
- id: 主键
- plateNumber: 车牌号
- reason: 原因
- evidenceImage: 证据图片
- expireAt: 过期时间
- isActive: 是否生效
- createdBy: 创建人
- removeReason: 解除原因

### 广播消息表 (broadcast_messages)
- id: 主键
- content: 广播内容
- sentBy: 发送人
- sentAt: 发送时间

## 快速开始

### 后端启动

```bash
cd backend

# 编译打包
mvn clean package

# 运行
mvn spring-boot:run
# 或
java -jar target/plate-recognition-1.0.0.jar
```

后端服务地址：http://localhost:8080

- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:platedb
  - 用户名: admin
  - 密码: admin

### 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build
```

前端服务地址：http://localhost:3000

## 功能模块

### 1. 实时监控大屏
- 统计卡片：在线摄像头、今日识别量、异常事件、黑名单数量
- 地图集成：显示摄像头分布位置
- 快捷操作：一键切换夜间模式、紧急广播
- 摄像头状态列表

### 2. 历史记录查询
- 多条件筛选：车牌号、摄像头、异常类型、时间范围
- 表格展示：支持分页
- 置信度标识：<90%显示黄色警告
- 图片预览：支持放大、缩小、旋转
- 数据导出：CSV/Excel格式（管理员可见）

### 3. 黑名单管理
- 添加黑名单：手动输入 + 上传证据图片
- 从历史记录直接标记
- 解除黑名单：自动过期/手动解除
- 黑名单详情查看

### 4. 摄像头管理
- 摄像头增删改查
- 夜间模式切换
- 视频流查看

## API接口

### 仪表盘
- `GET /api/dashboard/stats` - 获取统计数据
- `GET /api/dashboard/cameras` - 获取所有摄像头
- `POST /api/dashboard/night-mode` - 切换所有摄像头夜间模式

### 识别记录
- `POST /api/records/query` - 分页查询识别记录
- `POST /api/records` - 创建识别记录
- `DELETE /api/records/{id}` - 删除识别记录
- `POST /api/records/export/csv` - 导出CSV
- `POST /api/records/export/excel` - 导出Excel

### 黑名单
- `GET /api/blacklist` - 获取所有黑名单
- `GET /api/blacklist/active` - 获取生效的黑名单
- `POST /api/blacklist` - 添加黑名单
- `POST /api/blacklist/{id}/remove` - 解除黑名单
- `GET /api/blacklist/check/{plateNumber}` - 检查是否在黑名单

### 广播
- `POST /api/broadcast/send` - 发送广播
- `GET /api/broadcast/history` - 获取广播历史

## 初始化数据

项目启动时会自动初始化测试数据：
- 8个摄像头
- 50条识别记录
- 3条黑名单记录

## 注意事项

1. 高德地图集成需要申请API Key，当前使用占位符显示
2. 视频流播放需要集成相应的播放器组件
3. 生产环境建议使用MySQL/PostgreSQL替代H2数据库
4. 建议添加用户认证和权限控制
