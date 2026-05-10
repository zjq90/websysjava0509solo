# Web平台管理系统

## 项目概述

这是一个基于 JDK 8 + Spring Boot + H2 + Maven + Swagger-OpenAPI 的后端，以及 Vue 的前端组成的完整 Web 平台管理系统。

## 技术栈

### 后端
- **JDK**: 1.8+
- **框架**: Spring Boot 2.7.18
- **数据库**: H2（内存数据库）
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **API文档**: Swagger/OpenAPI (springdoc-openapi)
- **构建工具**: Maven
- **其他**: Lombok

### 前端
- **框架**: Vue 2.6
- **路由**: Vue Router 3
- **状态管理**: Vuex 3
- **HTTP客户端**: Axios
- **UI组件库**: Element UI
- **样式**: SCSS

## 项目结构

```
websysjava5/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/websys/
│   │   │   │   ├── WebsysApplication.java      # 启动类
│   │   │   │   ├── common/                     # 公共类
│   │   │   │   │   ├── Result.java            # 统一响应
│   │   │   │   │   └── PageResult.java        # 分页响应
│   │   │   │   ├── config/                     # 配置类
│   │   │   │   │   ├── DataInitializer.java   # 数据初始化
│   │   │   │   │   ├── JwtUtil.java           # JWT工具
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   └── SecurityConfig.java    # Security配置
│   │   │   │   ├── controller/                 # 控制器
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── AgentController.java
│   │   │   │   │   ├── OperationLogController.java
│   │   │   │   │   └── SystemConfigController.java
│   │   │   │   ├── dto/                        # 数据传输对象
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   └── LoginResponse.java
│   │   │   │   ├── entity/                     # 实体类
│   │   │   │   │   ├── Agent.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── OperationLog.java
│   │   │   │   │   └── SystemConfig.java
│   │   │   │   ├── repository/                 # 数据访问层
│   │   │   │   │   ├── AgentRepository.java
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── OperationLogRepository.java
│   │   │   │   │   └── SystemConfigRepository.java
│   │   │   │   └── service/                    # 业务层
│   │   │   │       ├── AuthService.java
│   │   │   │       ├── AgentService.java
│   │   │   │       ├── OperationLogService.java
│   │   │   │       ├── SystemConfigService.java
│   │   │   │       └── UserDetailsServiceImpl.java
│   │   │   └── resources/
│   │   │       └── application.yml            # 配置文件
│   └── pom.xml                                # Maven配置
│
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/                            # API接口
    │   │   ├── auth.js
    │   │   ├── agent.js
    │   │   ├── log.js
    │   │   └── config.js
    │   ├── router/                         # 路由
    │   │   └── index.js
    │   ├── store/                          # 状态管理
    │   │   └── index.js
    │   ├── styles/                         # 样式
    │   │   └── index.scss
    │   ├── utils/                          # 工具类
    │   │   └── request.js                  # Axios封装
    │   ├── views/                          # 页面组件
    │   │   ├── Login.vue
    │   │   ├── Layout.vue
    │   │   ├── Dashboard.vue
    │   │   ├── agent/
    │   │   │   └── AgentList.vue
    │   │   ├── log/
    │   │   │   └── OperationLogList.vue
    │   │   └── config/
    │   │       └── ConfigList.vue
    │   ├── App.vue
    │   └── main.js
    ├── public/
    │   └── index.html
    ├── package.json
    └── vue.config.js
```

## 功能模块

### 1. 代理商管理
- 支持多级代理商体系（一级、二级、三级...）
- 不同代理商只能查看和管理其所属的数据
- 完整的增删改查功能
- 支持按名称、编码、级别、状态等条件筛选

### 2. 操作日志
- 记录所有后台操作人员的关键行为
- 包括登录、创建、更新、删除等操作
- 支持按用户、模块、类型、状态、时间范围筛选
- 可查看日志详情

### 3. 系统基础配置
- 系统参数设置
- 支付接口配置（支付宝、微信支付等）
- 短信服务配置（阿里云、腾讯云等）
- 支持多种配置类型：字符串、数字、布尔值、JSON

## 快速开始

### 后端启动

1. 确保已安装 JDK 8+ 和 Maven

2. 进入后端目录：
```bash
cd backend
```

3. 编译并运行：
```bash
mvn clean spring-boot:run
```

4. 访问地址：
- 应用首页: http://localhost:8080
- Swagger文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:websysdb
  - 用户名: sa
  - 密码: (空)

### 前端启动

1. 确保已安装 Node.js (建议 v14+)

2. 进入前端目录：
```bash
cd frontend
```

3. 安装依赖：
```bash
npm install
```

4. 启动开发服务器：
```bash
npm run serve
```

5. 访问地址：http://localhost:8081

## 默认账户

系统启动时会自动初始化以下测试账户：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | 超级管理员 | 拥有所有权限 |
| agentadmin1 | admin123 | 代理商管理员 | 管理一级代理商A |
| agentadmin2 | admin123 | 代理商管理员 | 管理一级代理商B |
| agentuser1 | admin123 | 代理商用户 | 一级代理商A下的普通用户 |

## 数据库表结构

### t_agent (代理商表)
- id: 主键
- agent_name: 代理商名称
- agent_code: 代理商编码（唯一）
- parent_id: 父级代理商ID
- level: 代理商级别
- contact_name: 联系人
- contact_phone: 联系电话
- contact_email: 联系邮箱
- address: 地址
- status: 状态（1-正常，0-禁用）
- create_time: 创建时间
- update_time: 更新时间

### t_user (用户表)
- id: 主键
- username: 用户名（唯一）
- password: 密码（加密）
- real_name: 真实姓名
- agent_id: 所属代理商ID
- role_type: 角色类型
- phone: 联系电话
- email: 邮箱
- status: 状态
- create_time: 创建时间
- update_time: 更新时间
- last_login_time: 最后登录时间

### t_operation_log (操作日志表)
- id: 主键
- user_id: 操作用户ID
- username: 操作用户名
- operation_type: 操作类型
- module: 操作模块
- description: 操作描述
- target_id: 操作对象ID
- target_name: 操作对象名称
- request_method: 请求方法
- request_url: 请求URL
- request_params: 请求参数
- response_result: 响应结果
- ip_address: IP地址
- browser: 浏览器
- os: 操作系统
- status: 操作状态
- error_msg: 错误信息
- cost_time: 耗时
- create_time: 操作时间

### t_system_config (系统配置表)
- id: 主键
- config_group: 配置分组
- config_key: 配置键名（唯一）
- config_value: 配置值
- config_name: 配置名称
- description: 描述
- config_type: 配置类型
- enabled: 是否启用
- is_system: 是否系统内置
- sort_order: 排序号
- create_time: 创建时间
- update_time: 更新时间
- update_by: 更新人

## 测试功能

### 测试代理商管理
1. 使用 admin 账户登录
2. 进入"代理商管理"页面
3. 测试新增、编辑、删除功能
4. 测试搜索筛选功能
5. 验证多级代理商级别关系

### 测试操作日志
1. 执行一些操作（如新增代理商）
2. 进入"操作日志"页面
3. 查看生成的日志记录
4. 测试按条件筛选日志
5. 查看日志详情

### 测试系统配置
1. 进入"系统配置"页面
2. 查看默认的系统配置
3. 测试新增配置（选择不同类型）
4. 测试编辑配置
5. 验证系统内置配置不可删除

### 测试角色权限
1. 使用不同角色的账户登录
2. 验证超级管理员可以看到所有数据
3. 验证代理商用户的数据隔离

## API 接口

### 认证接口
- POST /api/auth/login - 用户登录
- GET /api/auth/me - 获取当前用户信息

### 代理商接口
- GET /api/agents - 分页查询代理商列表
- GET /api/agents/{id} - 查询代理商详情
- POST /api/agents - 新增代理商
- PUT /api/agents - 更新代理商
- DELETE /api/agents/{id} - 删除代理商
- GET /api/agents/top-level - 获取顶级代理商
- GET /api/agents/children/{parentId} - 获取子级代理商
- GET /api/agents/descendants/{agentId} - 获取所有下级代理商ID

### 操作日志接口
- GET /api/operation-logs - 分页查询日志
- GET /api/operation-logs/{id} - 查询日志详情
- GET /api/operation-logs/modules - 获取模块列表
- GET /api/operation-logs/operation-types - 获取操作类型列表

### 系统配置接口
- GET /api/configs - 分页查询配置
- GET /api/configs/{id} - 查询配置详情
- GET /api/configs/key/{configKey} - 按键名查询配置
- POST /api/configs - 新增配置
- PUT /api/configs - 更新配置
- DELETE /api/configs/{id} - 删除配置
- GET /api/configs/group/{configGroup} - 按分组查询配置
- GET /api/configs/groups - 获取所有分组
- GET /api/configs/types - 获取所有配置类型

## 注意事项

1. H2 数据库为内存数据库，应用重启后数据会重置
2. 首次启动时会自动初始化测试数据
3. 默认超级管理员账户：admin / admin123
4. Swagger 文档中可以直接测试所有 API 接口
5. 前端开发服务器已配置代理，请求 /api 会自动转发到后端 8080 端口
