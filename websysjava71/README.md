# 个人记账系统管理后台 - 数据分析与可视化模块

## 项目简介

这是一个功能完整的个人记账系统管理后台，包含完善的数据分析与可视化功能。系统采用前后端分离架构，后端使用Spring Boot + H2数据库，前端使用Vue 3 + Element Plus。

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.0
- **数据库**: H2 内存数据库
- **ORM**: Spring Data JPA
- **API文档**: Swagger/OpenAPI 3.0
- **Excel处理**: Apache POI
- **工具库**: Lombok, Spring Boot DevTools

### 前端技术
- **框架**: Vue 3.4.0
- **构建工具**: Vite 5.0
- **UI组件**: Element Plus 2.4.0
- **数据可视化**: ECharts 5.4.0
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **拖拽功能**: SortableJS
- **日期处理**: Day.js

## 功能特性

### 1. 仪表盘概览
- ✅ 今日/本周/本月收支总额统计
- ✅ 支出分类占比环形图
- ✅ 账户余额趋势折线图
- ✅ 月度收支趋势对比图
- ✅ 可拖拽的自定义看板组件
- ✅ 看板布局模板保存功能

### 2. 账单管理
- ✅ 账单的增删改查操作
- ✅ 多条件搜索（关键词、类型、日期范围）
- ✅ 批量选择和操作（修改分类、删除）
- ✅ 右键菜单操作
- ✅ 快捷键支持（Ctrl+C复制、Delete删除、Ctrl+V粘贴）
- ✅ 异常交易标记

### 3. 深度分析
- ✅ 消费趋势分析（按月/年对比）
- ✅ 分类对比排行（找出"最烧钱"的分类）
- ✅ 异常交易检测（大额支出自动标记）
- ✅ 3个月分类对比变化表
- ✅ 异常统计卡片

### 4. 预算与目标管理
- ✅ 分类月度预算设置
- ✅ 预算进度条实时显示
- ✅ 超支预警提醒
- ✅ 储蓄目标设定和跟踪
- ✅ 智能预算建议

### 5. 多维度报表
- ✅ 支出热力图（按小时/星期分布）
- ✅ 分类对比明细表
- ✅ 资产趋势图（支持钻取到具体账户）
- ✅ 账户资产详情
- ✅ 资产/负债/净资产统计
- ✅ 多格式报表导出（Excel/PDF/CSV）

### 6. Excel无缝对接
- ✅ 银行CSV文件导入解析
- ✅ 自动分类匹配（基于关键词）
- ✅ 导出带公式的Excel报表
- ✅ 导入数据预览功能

## 项目结构

```
websysjava71/
├── backend/                          # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/personal/accounting/
│   │       │   ├── AccountingApplication.java    # 主启动类
│   │       │   ├── config/                       # 配置类
│   │       │   │   ├── CorsConfig.java           # CORS配置
│   │       │   │   ├── DataInitializer.java      # 测试数据初始化
│   │       │   │   └── OpenApiConfig.java        # Swagger配置
│   │       │   ├── controller/                   # 控制器层
│   │       │   │   ├── AccountController.java
│   │       │   │   ├── BudgetController.java
│   │       │   │   ├── CategoryController.java
│   │       │   │   ├── DashboardController.java
│   │       │   │   ├── DashboardTemplateController.java
│   │       │   │   ├── ExcelController.java
│   │       │   │   ├── SavingGoalController.java
│   │       │   │   └── TransactionController.java
│   │       │   ├── dto/                          # 数据传输对象
│   │       │   ├── entity/                       # 实体类
│   │       │   │   └── enums/                    # 枚举类型
│   │       │   ├── exception/                    # 异常处理
│   │       │   ├── repository/                   # 数据访问层
│   │       │   └── service/                      # 业务逻辑层
│   │       └── resources/
│   │           └── application.yml               # 应用配置
│   └── pom.xml                                   # Maven依赖配置
│
└── frontend/                         # 前端项目
    ├── src/
    │   ├── api/                      # API接口层
    │   │   └── index.js
    │   ├── router/                   # 路由配置
    │   │   └── index.js
    │   ├── styles/                   # 全局样式
    │   │   └── global.scss
    │   ├── utils/                    # 工具函数
    │   │   └── request.js
    │   ├── views/                    # 页面组件
    │   │   ├── Dashboard.vue         # 仪表盘
    │   │   ├── Transactions.vue      # 账单管理
    │   │   ├── Analysis.vue          # 深度分析
    │   │   ├── Budget.vue            # 预算与目标
    │   │   └── Reports.vue           # 多维度报表
    │   ├── App.vue                   # 根组件
    │   └── main.js                   # 入口文件
    ├── index.html                    # HTML模板
    ├── package.json                  # 依赖配置
    └── vite.config.js                # Vite配置
```

## 数据库设计

### 核心实体关系
```
Transaction (交易记录)
    ├── Category (分类) - 多对一
    ├── Account (账户) - 多对一
    └── tags (标签) - 逗号分隔字符串

Budget (预算)
    └── Category (分类) - 多对一

SavingGoal (储蓄目标) - 独立实体

DashboardTemplate (看板模板) - 独立实体
```

### 主要数据表
- **category**: 收支分类（餐饮、交通、购物等）
- **account**: 账户（现金、银行卡、信用卡等）
- **transaction**: 交易记录
- **budget**: 月度预算
- **saving_goal**: 储蓄目标
- **dashboard_template**: 看板布局模板

## 快速开始

### 环境要求
- JDK 1.8+ (推荐JDK 1.8，已兼容配置)
- Node.js 16+
- Maven 3.8+ (项目已包含Maven Wrapper，可直接使用mvnw.cmd)

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 设置JAVA_HOME环境变量（如需要）：
```bash
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
```

3. 编译项目（使用Maven Wrapper）：
```bash
mvnw.cmd clean compile
```

4. 运行应用：
```bash
mvnw.cmd spring-boot:run
```

> **注意**: 首次运行时，Maven Wrapper会自动下载Maven 3.8.8，需要联网。
> 如果已安装Maven，也可直接使用 `mvn` 命令替代 `mvnw.cmd`。

4. 访问地址：
- 应用接口: http://localhost:8080
- Swagger文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:accountingdb`
  - 用户名: `sa`
  - 密码: 空

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

4. 访问地址：http://localhost:5173

### 批量启动（推荐）

可以使用两个终端分别启动前后端：

**终端1 - 后端:**
```bash
cd backend && mvnw.cmd spring-boot:run
```

**终端2 - 前端:**
```bash
cd frontend && npm install && npm run dev
```

## API接口示例

### 仪表盘API
- `GET /api/dashboard/overview` - 获取概览数据
- `GET /api/dashboard/monthly-trend?months=12` - 获取月度趋势
- `GET /api/dashboard/abnormal-transactions` - 获取异常交易

### 账单管理API
- `GET /api/transactions` - 分页查询账单
- `POST /api/transactions` - 创建账单
- `PUT /api/transactions/{id}` - 更新账单
- `DELETE /api/transactions/{id}` - 删除账单
- `POST /api/transactions/batch-delete` - 批量删除
- `POST /api/transactions/batch-update-category` - 批量更新分类

### Excel导入导出API
- `GET /api/excel/export` - 导出Excel
- `POST /api/excel/import/preview` - 预览导入数据
- `POST /api/excel/import` - 确认导入

## 快捷键说明

在账单管理页面支持以下快捷键：

| 快捷键 | 功能 |
|--------|------|
| `Ctrl + C` | 复制选中的账单 |
| `Ctrl + V` | 以复制的账单为模板新建 |
| `Delete` | 删除选中的账单 |
| `Esc` | 关闭右键菜单 |

## 测试数据说明

系统启动时会自动初始化测试数据，包括：
- 8个收支分类（餐饮、交通、购物、娱乐、居住、医疗、教育、其他）
- 5个账户（现金、工资卡、信用卡、余额宝、股票账户）
- 最近90天的交易记录（约450条）
- 6个分类月度预算
- 3个储蓄目标

## 特色功能说明

### 1. 智能分类匹配
导入银行CSV时，系统会根据描述和商家信息自动匹配分类，支持关键词匹配规则。

### 2. 异常交易检测
系统自动标记单笔超过月均支出3倍的大额交易，并在异常检测页面展示。

### 3. 自定义看板
用户可以通过拖拽调整看板组件的位置，并保存为模板，支持"月度复盘模板"等多种场景。

### 4. 预算智能建议
系统根据预算执行情况给出智能建议，如"餐饮支出过高，建议减少外出就餐"。

### 5. 数据钻取
资产趋势图支持点击账户卡片钻取到具体账户的数据展示。

## 开发说明

### 后端开发规范
- 采用分层架构：Controller → Service → Repository
- 使用DTO进行数据传输，避免直接暴露实体
- 统一异常处理机制
- Swagger注解完善API文档

### 前端开发规范
- 使用Vue 3 Composition API
- 组件化开发，代码清晰模块化
- 统一的API请求封装
- Element Plus组件库规范使用
- SCSS样式管理

## 后续扩展建议

1. **用户认证**: 添加Spring Security + JWT认证
2. **数据持久化**: 可切换到MySQL/PostgreSQL
3. **数据同步**: 支持微信/支付宝账单自动导入
4. **消息通知**: 超支提醒、预算进度通知
5. **数据备份**: 支持数据导出备份和恢复
6. **多币种**: 支持多币种记账和汇率转换

## 许可证

MIT License
