# 文物收藏管理系统

## 项目简介

这是一个专业的文物收藏管理系统，采用前后端分离架构，提供文物展示、鉴定服务、溯源查询、收藏管理等核心功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **API文档**: Swagger/OpenAPI 3.0
- **安全**: RSA加密、JWT认证
- **ORM**: Spring Data JPA

### 前端
- **框架**: UniApp (Vue 3)
- **样式**: SCSS
- **状态管理**: Vuex

## 项目结构

```
appsysjava52/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/heritage/
│   │   │   │       ├── config/          # 配置类
│   │   │   │       ├── entity/          # 实体类
│   │   │   │       ├── repository/      # 数据访问层
│   │   │   │       ├── controller/      # 控制器
│   │   │   │       └── HeritageApplication.java
│   │   │   └── resources/
│   │   │       └── application.yml      # 配置文件
│   │   └── test/
│   └── pom.xml
├── frontend/                   # 前端项目
│   ├── pages/                  # 页面
│   ├── static/                 # 静态资源
│   ├── App.vue
│   ├── manifest.json
│   ├── pages.json
│   └── package.json
└── README.md
```

## 核心功能

### 1. 文物展示
- 3D模型加载
- AR入口支持
- 高清图片放大镜
- 全景展示
- 分类筛选和搜索

### 2. 鉴定服务
- 在线申请
- 照片/视频上传
- 专家匹配
- 鉴定报告生成
- 流程跟踪

### 3. 溯源查询
- 区块链验证
- 拍卖记录查询
- 历史交易追踪
- 真伪验证

### 4. 收藏管理
- 个人收藏库
- 环境监测数据
- 保养提醒
- 估值统计

### 5. 长辈模式
- 字体放大
- 简化界面
- 语音输入支持
- 大按钮设计

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 8+
   - Maven 3.6+
   - Redis (可选，默认使用模拟)

2. **启动命令**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **访问地址**
   - 应用: http://localhost:8080/api
   - H2控制台: http://localhost:8080/api/h2-console
   - Swagger文档: http://localhost:8080/api/swagger-ui.html

### 前端启动

1. **环境要求**
   - Node.js 14+
   - HBuilderX (推荐)

2. **启动命令**
   ```bash
   cd frontend
   npm install
   npm run dev:h5
   ```

3. **演示账号**
   - 普通用户: user / 123456
   - 管理员: admin / 123456

## 数据库设计

### 核心表结构

1. **用户表 (user)**
   - 基本信息、角色类型、认证状态

2. **专家表 (expert)**
   - 专业领域、资质信息、审核状态

3. **文物表 (heritage)**
   - 名称、年代、材质、估值、3D模型地址

4. **鉴定申请表 (authentication_request)**
   - 申请信息、状态、专家分配、报告

5. **溯源记录表 (trace_record)**
   - 交易信息、区块链哈希、时间戳

6. **收藏表 (collection_item)**
   - 收藏信息、环境监测、保养状态

7. **环境数据表 (environment_data)**
   - 温度、湿度、光照、时间戳

8. **保养提醒表 (maintenance_reminder)**
   - 提醒类型、内容、状态、参考资料

## API接口

### 文物模块
- `GET /api/heritage/list` - 文物列表
- `GET /api/heritage/{id}` - 文物详情
- `GET /api/heritage/{id}/trace` - 溯源记录

### 鉴定模块
- `POST /api/auth/submit` - 提交鉴定申请
- `GET /api/auth/list` - 申请列表
- `GET /api/auth/{id}` - 申请详情
- `POST /api/auth/{id}/report` - 提交报告

### 收藏模块
- `GET /api/collection/list` - 收藏列表
- `GET /api/collection/{id}` - 收藏详情
- `GET /api/collection/{id}/environment` - 环境数据
- `GET /api/collection/{id}/maintenance` - 保养提醒

### 用户模块
- `POST /api/user/login` - 登录
- `GET /api/user/info` - 用户信息
- `POST /api/user/expert/apply` - 专家认证申请

## 安全设计

### RSA加密
- 敏感数据传输加密
- 公钥加密，私钥解密
- 密钥对自动生成

### JWT认证
- Token无状态认证
- 过期时间控制
- 角色权限验证

## 长辈模式特性

1. **界面优化**
   - 字体放大30%
   - 按钮尺寸增大
   - 配色对比度提高
   - 减少动画效果

2. **功能简化**
   - 常用功能前置
   - 复杂流程分步引导
   - 语音输入支持
   - 一键求助

3. **操作辅助**
   - 操作确认提示
   - 步骤语音播报
   - 错误信息简化
   - 帮助说明增强

## 开发说明

### 后端开发
- 采用分层架构：Controller -> Service -> Repository
- 使用Lombok简化代码
- Swagger自动生成API文档
- 测试数据自动初始化

### 前端开发
- UniApp跨平台框架
- SCSS样式预处理器
- 组件化开发
- 响应式设计

## 注意事项

1. **H2数据库**: 内存数据库，重启数据丢失
2. **Redis**: 默认模拟，可配置真实Redis
3. **RSA密钥**: 启动时自动生成，可配置固定密钥
4. **长辈模式**: 需在"我的"页面手动开启

## 许可证

MIT License
