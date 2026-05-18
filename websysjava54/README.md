# 文物收藏管理后台系统

## 项目简介

这是一个完整的文物收藏管理后台系统，包含后端API和前端页面，采用前后端分离架构。

## 技术栈

### 后端
- **JDK 8**
- **Spring Boot 2.7.18**
- **H2 内存数据库**
- **Spring Data JPA**
- **Swagger OpenAPI (springdoc)**
- **Maven**

### 前端
- **Vue 2**
- **Vue Router**
- **Element UI**
- **ECharts**
- **Axios**

## 项目结构

```
websysjava54/
├── pom.xml                           # Maven配置文件
├── README.md                         # 项目说明
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── heritage/
│       │           ├── HeritageApplication.java          # 启动类
│       │           ├── config/
│       │           │   ├── SwaggerConfig.java            # Swagger配置
│       │           │   └── DataInitializer.java          # 数据初始化
│       │           ├── common/
│       │           │   └── Result.java                   # 统一响应类
│       │           ├── enums/
│       │           │   ├── RiskLevel.java                # 风险等级枚举
│       │           │   └── AuditStatus.java              # 审核状态枚举
│       │           ├── entity/
│       │           │   ├── User.java                     # 用户实体
│       │           │   ├── Heritage.java                 # 文物实体
│       │           │   ├── CategoryTag.java              # 分类标签实体
│       │           │   ├── DesensitizationRule.java      # 脱敏规则实体
│       │           │   ├── MonitoringRule.java           # 监测规则实体
│       │           │   ├── Transaction.java              # 交易实体
│       │           │   └── Report.java                   # 举报实体
│       │           ├── repository/
│       │           │   ├── UserRepository.java
│       │           │   ├── HeritageRepository.java
│       │           │   ├── CategoryTagRepository.java
│       │           │   ├── DesensitizationRuleRepository.java
│       │           │   ├── MonitoringRuleRepository.java
│       │           │   ├── TransactionRepository.java
│       │           │   └── ReportRepository.java
│       │           ├── service/
│       │           │   ├── UserService.java
│       │           │   ├── HeritageService.java
│       │           │   ├── DesensitizationService.java
│       │           │   ├── TransactionService.java
│       │           │   ├── ReportService.java
│       │           │   └── DashboardService.java
│       │           └── controller/
│       │               ├── UserController.java
│       │               ├── HeritageController.java
│       │               ├── TransactionController.java
│       │               ├── ReportController.java
│       │               ├── DashboardController.java
│       │               └── DesensitizationRuleController.java
│       └── resources/
│           └── application.yml                            # Spring Boot配置
└── frontend/                                              # Vue前端项目
    ├── package.json
    ├── public/
    │   └── index.html
    └── src/
        ├── main.js
        ├── App.vue
        ├── router/
        │   └── index.js
        └── views/
            ├── Home.vue
            ├── heritage/
            │   ├── HeritageAudit.vue
            │   ├── HeritageList.vue
            │   └── CategoryTags.vue
            ├── user/
            │   ├── UserList.vue
            │   ├── RealnameAudit.vue
            │   ├── ExpertAudit.vue
            │   └── SuspiciousUsers.vue
            ├── transaction/
            │   ├── TransactionList.vue
            │   ├── AbnormalTransactions.vue
            │   ├── FrozenTransactions.vue
            │   └── ReportList.vue
            ├── dashboard/
            │   ├── Dashboard.vue
            │   └── Heatmap.vue
            └── settings/
                ├── DesensitizationRules.vue
                └── MonitoringRules.vue
```

## 功能模块

### 1. 文物管理
- 文物信息审核（按风险等级排序：高/中/低）
- 支持对接国家文物局API验证来源
- 文物详情查看
- 分类标签体系（按朝代/材质/用途）
- 敏感信息脱敏规则配置（支持正则表达式）

### 2. 用户管理
- 用户列表管理
- 实名认证审核
- 专家资质认证（支持人工复核）
- 可疑用户标记与处理
- 资金冻结/解冻操作
- 信用分体系（影响鉴定服务优先级）

### 3. 交易监管
- 交易列表查询
- 异常交易监测（高频交易/价格偏离）
- 监测规则动态配置
- 交易弹窗详情（买家/卖家信用分、历史行为）
- 资金冻结申请与审核（法务部门审批）
- 举报处理流程

### 4. 数据看板
- 文物分布热力图（按省份）
- 支持点击省份查看具体文物列表
- 按朝代/材质分类统计
- 用户信用分分布统计
- 数据导出脱敏处理
- 综合统计概览

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 8+
   - Maven 3.6+

2. **构建项目**
   ```bash
   mvn clean install
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问地址**
   - 应用地址：http://localhost:8080
   - Swagger文档：http://localhost:8080/swagger-ui.html
   - H2数据库控制台：http://localhost:8080/h2-console

5. **H2数据库配置**
   - JDBC URL：`jdbc:h2:mem:heritage_db`
   - 用户名：`sa`
   - 密码：（空）

### 前端启动

1. **环境要求**
   - Node.js 14+
   - npm 6+

2. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

3. **启动开发服务器**
   ```bash
   npm run serve
   ```

4. **访问地址**
   - 前端地址：http://localhost:8081（或其他端口）

## API接口说明

### 用户管理接口
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取用户详情
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `POST /api/users/{id}/mark-suspicious` - 标记可疑用户
- `POST /api/users/{id}/ignore-suspicious` - 忽略可疑标记
- `POST /api/users/{id}/freeze-funds` - 冻结资金
- `POST /api/users/{id}/unfreeze-funds` - 解冻资金
- `GET /api/users/suspicious` - 获取可疑用户列表
- `GET /api/users/frozen-funds` - 获取资金冻结用户列表
- `GET /api/users/expert-audit/pending` - 获取待专家认证列表
- `POST /api/users/{id}/expert-audit` - 审核专家认证
- `PUT /api/users/{id}/credit-score` - 更新信用分

### 文物管理接口
- `GET /api/heritage` - 获取所有文物
- `GET /api/heritage/{id}` - 获取文物详情
- `POST /api/heritage` - 创建文物
- `PUT /api/heritage/{id}` - 更新文物
- `DELETE /api/heritage/{id}` - 删除文物
- `GET /api/heritage/pending-audit` - 获取待审核文物列表
- `POST /api/heritage/{id}/audit` - 审核文物
- `POST /api/heritage/{id}/verify-api` - API验证来源
- `GET /api/heritage/dynasty/{dynasty}` - 按朝代查询
- `GET /api/heritage/material/{material}` - 按材质查询
- `GET /api/heritage/usage/{usageType}` - 按用途查询
- `GET /api/heritage/province/{province}` - 按省份查询

### 交易监管接口
- `GET /api/transactions` - 获取所有交易
- `GET /api/transactions/{id}` - 获取交易详情
- `POST /api/transactions` - 创建交易
- `GET /api/transactions/abnormal` - 获取异常交易
- `GET /api/transactions/frozen` - 获取冻结交易
- `POST /api/transactions/{id}/request-freeze` - 申请冻结资金
- `POST /api/transactions/{id}/audit-freeze` - 审核冻结申请
- `GET /api/transactions/user/{userId}` - 获取用户交易记录
- `GET /api/transactions/recent/{minutes}` - 获取最近交易

### 举报管理接口
- `GET /api/reports` - 获取所有举报
- `GET /api/reports/{id}` - 获取举报详情
- `POST /api/reports` - 创建举报
- `GET /api/reports/pending` - 获取待处理举报
- `POST /api/reports/{id}/handle` - 处理举报

### 数据看板接口
- `GET /api/dashboard/statistics` - 获取统计数据
- `GET /api/dashboard/heritage/province` - 按省份分布统计
- `GET /api/dashboard/heritage/dynasty` - 按朝代分布统计
- `GET /api/dashboard/heritage/material` - 按材质分布统计
- `GET /api/dashboard/heritage/province/{province}` - 获取某省份文物列表
- `GET /api/dashboard/user/province` - 用户按省份分布
- `GET /api/dashboard/user/credit-score` - 用户信用分分布

### 脱敏规则接口
- `GET /api/desensitization` - 获取所有脱敏规则
- `GET /api/desensitization/{id}` - 获取规则详情
- `POST /api/desensitization` - 创建规则
- `PUT /api/desensitization/{id}` - 更新规则
- `DELETE /api/desensitization/{id}` - 删除规则
- `GET /api/desensitization/enabled` - 获取启用的规则

## 测试数据

系统启动时会自动初始化以下测试数据：

1. **用户数据**：10个测试用户，包含3名已认证专家、2名待审核专家、1名可疑用户、1名资金冻结用户
2. **文物数据**：10个测试文物，包含高/中/低风险等级
3. **交易数据**：8笔交易记录，包含2笔异常交易
4. **举报数据**：5条举报记录
5. **分类标签**：朝代、材质、用途三类标签
6. **脱敏规则**：手机号、身份证、姓名三类脱敏规则
7. **监测规则**：高频交易、价格偏离、低信用分三类监测规则

## 主要功能说明

### 风险等级
- **HIGH (高风险)** - 需要优先审核
- **MEDIUM (中风险)** - 普通审核
- **LOW (低风险)** - 快速审核

### 审核状态
- **PENDING (待审核)**
- **APPROVED (审核通过)**
- **REJECTED (审核拒绝)**
- **REVIEWING (审核中)**

### 脱敏规则
系统内置三种脱敏规则，支持自定义正则表达式：
1. 手机号脱敏：`(\\d{3})\\d{4}(\\d{4})` → `$1****$2`
2. 身份证脱敏：`(\\d{6})\\d{8}(\\d{4})` → `$1********$2`
3. 姓名脱敏：`([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]+)` → `$1*`

### 监测规则
系统支持三种异常交易监测规则：
1. 高频交易监测：指定时间内交易次数超过阈值
2. 价格偏离监测：交易价格偏离估值超过阈值百分比
3. 低信用分监测：交易用户信用分低于阈值

## 开发说明

### 后端开发
- 采用Spring Data JPA进行数据持久化
- 统一返回格式：`Result<T>`
- 使用Swagger进行API文档自动生成
- H2内存数据库，支持数据自动初始化

### 前端开发
- 采用Vue 2 + Element UI组件库
- 使用ECharts进行数据可视化
- 前后端通过Axios进行数据交互
- 支持响应式布局

## 注意事项

1. 本项目使用H2内存数据库，重启后数据会丢失
2. 前端项目需单独启动，默认后端端口为8080
3. Swagger文档包含所有API的详细说明和测试功能
4. 所有测试数据在应用启动时自动创建，可直接用于功能测试

## 许可证

MIT License
