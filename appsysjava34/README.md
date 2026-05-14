# 智慧家庭服务系统

## 项目简介

这是一个完整的用户端APP系统，包含后端API服务和前端UniApp应用。系统提供服务进度可视化、故障报修与智能诊断、家庭网络管理等功能模块。

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- Redis 缓存
- SpringDoc OpenAPI (Swagger)
- Hutool 工具类库

### 前端
- UniApp
- Vue 2.x
- 响应式设计
- 适老化模式支持

## 功能特性

### 1. 服务进度可视化
- 全流程状态展示（资料审核 → 装维派单 → 上门安装 → 已完成）
- 装维人员位置实时显示
- 预计到达时间展示
- 一键联系装维人员

### 2. 故障报修与智能诊断
- 一键报修（常见问题分类选择）
- 截图/视频上传支持
- 自动诊断光猫状态、信号强度、DNS配置
- AI智能客服自动解答
- 复杂问题转接人工坐席

### 3. 家庭网络管理
- WiFi管理：修改密码、隐藏SSID、访客网络设置
- 设备控制：查看连接设备、限速、拉黑/踢出设备
- 儿童守护：上网时段控制、应用白名单、防沉迷提醒
- FTTR组网管理：远程重启、信号优化建议、设备拓扑展示

### 4. 其他特性
- 敏感信息RSA加密传输
- 响应式设计，适配手机/平板
- 长辈模式：字体放大、界面简化

## 项目结构

```
appsysjava34/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/appsys/
│   │   │   │   ├── entity/    # 实体类
│   │   │   │   ├── repository/ # 数据访问层
│   │   │   │   ├── service/   # 业务逻辑层
│   │   │   │   ├── controller/ # 控制器层
│   │   │   │   ├── config/     # 配置类
│   │   │   │   └── util/       # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── pom.xml
│
└── frontend/                # 前端UniApp项目
    ├── pages/               # 页面文件
    │   ├── index/           # 首页
    │   ├── service-order/   # 服务进度
    │   ├── fault-report/    # 故障报修
    │   ├── network/         # 网络管理
    │   ├── chat/            # AI客服
    │   └── mine/            # 我的
    ├── api/                 # API封装
    ├── utils/               # 工具函数
    ├── static/              # 静态资源
    ├── App.vue
    ├── main.js
    ├── pages.json
    └── manifest.json
```

## 快速开始

### 后端启动

1. 确保已安装 JDK 11+ 和 Maven
2. 进入后端目录：`cd backend`
3. 编译项目：`mvn clean install`
4. 启动应用：`mvn spring-boot:run`
5. 访问地址：
   - API服务：http://localhost:8080/api
   - Swagger文档：http://localhost:8080/api/swagger-ui.html
   - H2控制台：http://localhost:8080/api/h2-console

### 前端启动

1. 确保已安装 HBuilderX 或 Node.js
2. 进入前端目录：`cd frontend`
3. 使用HBuilderX打开项目并运行到浏览器或真机

## API接口文档

### 用户管理
- GET /api/user/{id} - 获取用户信息
- PUT /api/user/{id}/elder-mode - 切换长辈模式
- POST /api/user/login - 用户登录

### 服务工单
- GET /api/service-order/user/{userId} - 获取用户工单列表
- GET /api/service-order/{id} - 获取工单详情
- POST /api/service-order/create - 创建工单
- PUT /api/service-order/{id}/status - 更新工单状态
- PUT /api/service-order/{id}/location - 更新装维人员位置

### 故障报修
- GET /api/fault-report/user/{userId} - 获取报修记录列表
- POST /api/fault-report/create - 创建报修单
- POST /api/fault-report/diagnose - 智能诊断

### 网络管理
- GET /api/network/devices/{userId} - 获取设备列表
- PUT /api/network/device/{id}/block - 拉黑/取消拉黑设备
- GET /api/network/wifi/{userId} - 获取WiFi设置
- PUT /api/network/wifi/{userId}/password - 修改WiFi密码
- PUT /api/network/wifi/{userId}/visibility - 切换WiFi可见性
- GET /api/network/child-guard/{userId} - 获取儿童守护设置

### AI客服
- GET /api/chat/history/user/{userId} - 获取聊天历史
- POST /api/chat/send - 发送消息

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 测试用户（用户名: testuser, 密码: 123456）
- 服务工单示例
- 故障报示例
- 5个网络设备（手机、电脑、电视等）
- WiFi设置（SSID: HomeWiFi, 密码: 12345678）
- 儿童守护设置

## 安全说明

- 用户手机号、身份证号等敏感信息使用RSA加密存储
- API接口可配合Spring Security进行权限控制
- 建议生产环境使用HTTPS协议

## 注意事项

1. H2为内存数据库，重启后数据会丢失，生产环境建议切换为MySQL
2. Redis为可选依赖，如未安装Redis，可注释掉相关配置和代码
3. 前端UniApp项目建议使用HBuilderX进行开发和打包
4. 长辈模式开启后会自动放大字体，简化界面交互

## 开发建议

1. 后端开发可通过Swagger文档进行接口调试
2. 前端开发可通过HBuilderX的热更新功能提高效率
3. 建议配合Postman等工具进行API测试
4. 可根据实际需求扩展更多功能模块

## License

MIT
